package com.hantong.communication.component;

import com.hantong.codec.EncoderDecoder;
import com.hantong.code.ErrorCode;
import com.hantong.communication.Communicate;
import com.hantong.interfaces.ICodec;
import com.hantong.message.MessageType;
import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;
import com.hantong.model.CommunicationConfig;
import com.hantong.service.Service;
import com.hantong.util.Json;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SocketCommunicate extends Communicate {

    private CommunicationConfig.Socket config;
    private Service service;
    private Thread threadSocketWait;
    private SocketCommunicate that;
    private Map<String,ChannelHandlerContext> handlerContextMap;
    private Map<ChannelHandlerContext,String> handlerContextStringMap;
    private Long count = Long.valueOf(0);

    public void addCounter(){
        this.count++;
    }

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

    public void clear() {
        this.handlerContextStringMap.clear();
        this.handlerContextMap.clear();
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
        ChannelHandlerContext ctx ;
        if (null != runtimeMessage.getContext()) {
            ctx = (ChannelHandlerContext)runtimeMessage.getContext();
        } else {
            ctx = this.handlerContextMap.getOrDefault(requestMessage.getHardwareId(),null);
        }

        if (null == ctx) {
            return ErrorCode.Success;
        }

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
                this.socketCommunicate.addCounter();
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
        private ServerBootstrap serverBootstrap;
        private EventLoopGroup pGroup;
        private EventLoopGroup cGroup;
        @Override
        public void run() {
            try {
                serverBootstrap = new ServerBootstrap();
                pGroup = new NioEventLoopGroup();
                cGroup = new NioEventLoopGroup();
                serverBootstrap.group(pGroup, cGroup)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel sc) throws Exception {
                                sc.pipeline().addLast(new ServerHandler(service,encoderDecoder,that));
                            }
                        });
                ChannelFuture cf = serverBootstrap.bind(config.getPort()).sync();

                cf.channel().closeFuture().sync();
            } catch (Exception e) {
                System.out.println("Socket服务关闭监听,端口：" + String.valueOf(config.getPort()));
            } finally {
                pGroup.shutdownGracefully();
                cGroup.shutdownGracefully();
                that.clear();
            }
        }
    }

    @Override
    public Map<String, Map<String, String>> getMonitorData() {
        Map<String, Map<String, String>> monitor = new LinkedHashMap<>();
        Map<String, String> thisMonitor = new LinkedHashMap<>();
        thisMonitor.put("received",String.valueOf(count));
        monitor.put("SocketCommunicate",thisMonitor);

        return monitor;
    }
}
