package com.hantong.communication.component;

import codec.EncoderDecoder;
import com.hantong.code.ErrorCode;
import com.hantong.communication.Communicate;
import com.hantong.interfaces.ICodec;
import com.hantong.interfaces.ILifecycle;
import com.hantong.message.MessageType;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.CommunicationConfig;
import com.hantong.service.Service;
import com.hantong.util.Json;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ByteProcessor;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SocketCommunicate extends Communicate {

    private CommunicationConfig.Socket config;
    private Service service;
    private Thread threadSocketWait;
    private SocketCommunicate that;
    private Map<String,ChannelHandlerContext> handlerContextMap;
    private Map<ChannelHandlerContext,String> handlerContextStringMap;

    public Boolean isHardwareExist(String hardwareId) {
        return handlerContextMap.containsKey(hardwareId);
    }

    public String getHardwareId(ChannelHandlerContext ctx) {
        return handlerContextStringMap.getOrDefault(ctx,null);
    }

    public void addHardwareContext(String hardwareId,ChannelHandlerContext ctx) {
        this.handlerContextMap.put(hardwareId,ctx);
        this.handlerContextStringMap.put(ctx,hardwareId);
    }

    public void delHardwareContext(String hardwareId) {
        if (!isHardwareExist(hardwareId)) {
            return;
        }
        this.handlerContextStringMap.remove(this.handlerContextMap.get(hardwareId));
        this.handlerContextMap.remove(hardwareId);
    }

    public void delHardwareContext(ChannelHandlerContext ctx) {
        if (null == getHardwareId(ctx)) {
            return;
        }

        this.handlerContextMap.remove((this.handlerContextStringMap.get(ctx)));
        this.handlerContextStringMap.remove(ctx);
    }

    public SocketCommunicate(CommunicationConfig.Socket conf, EncoderDecoder codec,Service servic) {
        super(codec);
        this.config = conf;
        this.service = servic;
        this.that = this;
        this.handlerContextMap = new HashMap<>();
        this.handlerContextStringMap = new HashMap<>();
    }

    @Override
    public ErrorCode sendData(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        if (null == runtimeMessage || null == requestMessage) {
            return ErrorCode.Success;
        }
        byte[] result = this.encoderDecoder.encode(requestMessage,runtimeMessage);
        ChannelHandlerContext ctx = (ChannelHandlerContext)runtimeMessage.getContext();

        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(result);
        ctx.writeAndFlush(buf);
        return null;
    }

    @Override
    public ErrorCode lifeStart() {
        this.threadSocketWait = new Thread(new SocketWaitConnect());
        this.threadSocketWait.start();
        return ErrorCode.Success;
    }

    @Override
    public ErrorCode lifeStop() {
        this.threadSocketWait.interrupt();
        this.threadSocketWait = null;
        return ErrorCode.Success;
    }

    public static class ServerHandler extends ChannelInboundHandlerAdapter  {
        private ICodec codec;
        private Service service;
        private SocketCommunicate socketCommunicate;
        public ServerHandler (Service servic,ICodec code,SocketCommunicate socketCommunicate) {
            this.codec = code;
            this.service = servic;
            this.socketCommunicate = socketCommunicate;
        }
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)  throws Exception {
            try {
                Long begin = System.currentTimeMillis();
                ByteBuf buf = (ByteBuf)msg;
                byte[] content = new byte[buf.readableBytes()];
                buf.readBytes(content);
                RequestMessage requestMessage = this.codec.decode(content);
                if (!socketCommunicate.isHardwareExist(requestMessage.getHardwareId())){
                    socketCommunicate.addHardwareContext(requestMessage.getHardwareId(),ctx);
                    RequestMessage request = new RequestMessage();
                    request.setType(MessageType.Connect);
                    request.setHardwareId(requestMessage.getHardwareId());
                    request.setEventDate(new Date());
                    RuntimeMessage runtime = new RuntimeMessage();
                    this.service.onReceiveMessage(request,runtime);
                }

                System.out.println(Json.getInstance().toString(requestMessage));
                RuntimeMessage runtimeMessage = new RuntimeMessage();

                runtimeMessage.setCommunicationSource(this.socketCommunicate);
                runtimeMessage.setContext(ctx);

                runtimeMessage.addTimestramp(this.getClass().getSimpleName(),begin,System.currentTimeMillis());
                this.service.onReceiveMessage(requestMessage,runtimeMessage);
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            System.out.println("channelActive");
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            System.out.println("channelInactive");
            String hardwareId = socketCommunicate.getHardwareId(ctx);
            if (null == hardwareId) {
                return;
            }
            RequestMessage request = new RequestMessage();
            request.setType(MessageType.Disconnect);
            request.setHardwareId(hardwareId);
            request.setEventDate(new Date());
            RuntimeMessage runtime = new RuntimeMessage();
            this.service.onReceiveMessage(request,runtime);
            this.socketCommunicate.delHardwareContext(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            Channel incoming = ctx.channel();
            if(!incoming.isActive())    System.out.println("SimpleClient:" + incoming.remoteAddress() + "异常" + cause.getMessage());
            ctx.close();
        }
    }

    public class SocketWaitConnect implements Runnable {
        @Override
        public void run() {
            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                EventLoopGroup pGroup = new NioEventLoopGroup();
                EventLoopGroup cGroup = new NioEventLoopGroup();
                serverBootstrap.group(pGroup, cGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_KEEPALIVE, true)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel sc) throws Exception {
                                sc.pipeline().addLast(new ServerHandler(service,encoderDecoder,that));
                            }
                        });
                ChannelFuture cf = serverBootstrap.bind(config.getPort()).sync();
                cf.channel().closeFuture().sync();
                pGroup.shutdownGracefully();
                cGroup.shutdownGracefully();
            } catch (Exception e) {
                System.out.println("socketError");
            }
        }
    }
}
