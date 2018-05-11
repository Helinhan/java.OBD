package com.hantong.codec;

import org.thymeleaf.util.ArrayUtils;

import java.util.Arrays;
import java.util.zip.Adler32;

public class Business {
    private byte codetype;
    private int SerialNumber=0;
    public Business(byte b) {
        this.codetype=b;
    }
    public void operation(byte[] data,byte[] imei,byte[] len){

        //记录回话序号
        this.SerialNumber=(data[1]*256)+data[2];
        //获取有效数据长度
        int le=(data[3]*256)+data[4];

        //效验

        //读出待效验解密后字段
        byte[] bcode=Arrays.copyOfRange(data,0,5+le);
        //初始化待效验数
        byte[] Adler32data=new byte[len.length+imei.length+bcode.length];
        //组合待效验数据=帧长度+终端编号+帧类型+帧序号+有效数据长度+有效数据
        System.arraycopy(len,0,Adler32data,0,len.length);//装入帧长度
        System.arraycopy(imei,0,Adler32data,len.length,imei.length);//装入imei
        System.arraycopy(bcode,0,Adler32data,len.length+imei.length,bcode.length);//装入imei
        Adler32 adler32=new Adler32();

        adler32.update(Adler32data);
        long aaa = adler32.getValue();

        //获取效验位长度=总长度-帧类型（1）-帧序号（2）-有效数据长度（2）-有效数据（n）-有效数据补位
        int adlent =data.length-le-(le%8>0?8-le:0);
        //读取出效验位
        byte[] Adler32code=new byte[adlent];
        for(int i=Adler32data.length+(len.length%8>0?8-len.length:0); i<data.length;i++){
            Adler32code[i]=data[i];
        }
        //读取有效数据
        byte[] sysdata=new byte[le];

        int index=5;
        for (int i=index;i<le;i++){
            sysdata[i]=data[i];
            index++;
        }






        if(this.codetype==0x01){//设备回话请求


        }else if(this.codetype==0x02){//设备回话建立回复

        }else if(this.codetype==0x03){//连接成功后的业务消息

        }else if(this.codetype==0x04){//业务消息回复(服务客户端都用)

        }else if(this.codetype==0x0c){//心跳包

        }else if(this.codetype==0x0d){//心跳回复

        }else if(this.codetype==0x0e){//结束回话

        }
    }


}
