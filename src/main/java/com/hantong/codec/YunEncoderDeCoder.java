package com.hantong.codec;

import com.hantong.message.RequestMessage;
import com.hantong.message.RuntimeMessage;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.Map;

public class YunEncoderDeCoder  extends EncoderDecoder  {
    private static final String Algorithm = "DESede"; // 定义 加密算法,可用
    private static final byte START[]={(byte) 0X55,(byte) 0X55};//开始位
    private static final byte END[]={(byte) 0XAA,(byte) 0XAA};//停止位
    private String imei;//imei
    private byte ZHENGTYPE[]={(byte) 0XAA,(byte) 0XAA};
    private static final byte[] PLIST=new byte[]{//协议类型
            (byte)0x01,(byte)0x02,(byte)0x03,(byte)0x04,(byte)0x0C,(byte)0x0D,(byte)0x0E
    };

    @Override
    public RequestMessage decode(byte[] data) {
        RequestMessage msg = new RequestMessage();
        byte[] code=data;


        byte[] start=Arrays.copyOfRange(code,0,2);
        byte[] end=Arrays.copyOfRange(code,code.length-2,code.length);
        /*
         * 验证起始位
         * */
        if(!Arrays.equals(START,start)){
            String msgs="开始位不正确！";
            System.out.println(msgs);
            msg.setHardwareId("yun01:error "+msgs);
            return msg;
        }
        if(!Arrays.equals(END,end)){
            String msgs="结束位不正确！";
            System.out.println(msgs);
            msg.setHardwareId("yun01:error "+msgs);
            return msg;
        }

        //获取总长度;
        int cel= code.length;
        //读取报文长度信息
        int bwl= (code[2]*256)+code[3];

        if(cel!=bwl){
            String msgs="数据长度不正确！";
            System.out.println(msgs);
            msg.setHardwareId("yun01:error "+msgs);
            return msg;
        }

        /*
        * 获取设备唯一标识
        * */
        byte[] imei=Arrays.copyOfRange(code,4,19);


        this.imei=new String(imei);//储存设备唯一标示

        System.out.println("IEMI:"+new String(imei));
        //取出加密字节
        byte[] bcode=Arrays.copyOfRange(code,19,code.length-2);
        if(bcode.length%8!=0){
            String msgs="加密字节长度不对必须是8的倍数！";
            System.out.println(msgs);
            msg.setHardwareId("yun01:error "+msgs);
            return msg;

        }
        //查询是否有该设备存在
        byte[] devicekey=getkey(this.imei);

        if(devicekey==null){
            String msgs="设备不存在！";
            System.out.println(msgs);
            msg.setHardwareId("yun01:error "+msgs);
            return msg;
        }



        //解密
        //获取秘钥

        try {
            //获取秘钥

            //des-3d解密
            ThreeDes des= new ThreeDes();
            byte[] dcode=des.decryptMode(devicekey,bcode);
            //业务处理开始
           // Business business= new Business(dcode[0]);

            for (int i=0;i<dcode.length;i++){
                String c=Integer.toHexString(dcode[i]&0xff);
                if(c!="ffffffff"){
                    System.out.print(Integer.toHexString(dcode[i])+" ");
                }

            }
            System.out.println("");
        } catch (Exception e) {

            e.printStackTrace();
        }


        /*
        *
        * 打印所有的接收内容
        * */

        for (int i=0;i<data.length;i++){
            System.out.print(Integer.toHexString(data[i])+" ");
        }




        msg.setHardwareId("yun01:ok!");
        return msg;
    }

    @Override
    public byte[] encode(RequestMessage requestMessage, RuntimeMessage runtimeMessage) {
        return new byte[0];
    }

    @Override
    public Map<String, Map<String, String>> getMonitorData() {
        return null;
    }
    /*
    * 设备登陆
    * */
    public deviceinfo devicelogin(String[] device){



        deviceinfo loginfo =new deviceinfo();

        return loginfo;
    }

    /*
    * 根据获取设备key用于加密解密
    * */
    public byte[] getkey(String imei){

        byte[] key={
                (byte) 0x8C,(byte) 0xEB,(byte) 0xDC,(byte) 0xB2,
                (byte) 0xC4,(byte) 0x2A,(byte) 0x3B,(byte) 0xA2,
                (byte) 0x6F,(byte) 0xE8,(byte) 0x79,(byte) 0xCF,
                (byte) 0xFE,(byte) 0x7C,(byte) 0x75,(byte) 0x6B,
                (byte) 0x8F,(byte) 0x19,(byte) 0x43,(byte) 0xEF,
                (byte) 0x86,(byte) 0x69,(byte) 0x4A,(byte) 0x81,
        };

        return key;
    }
    /*
    * 查询数据库是否有该设备
    * */
    public boolean getdevice(String imei){
        return true;
    }
    /*
    * 数据效验
    * */
    public boolean calcAdler32CheckSum(){

        return true;
    }
}

