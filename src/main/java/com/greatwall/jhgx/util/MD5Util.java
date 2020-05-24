package com.greatwall.jhgx.util;

import java.security.MessageDigest;

public class MD5Util {

    public static String md5(String txt) {
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(txt.getBytes("utf-8"));    //问题主要出在这里，Java的字符串是unicode编码，不受源码文件的编码影响；而PHP的编码是和源码文件的编码一致，受源码编码影响。
            StringBuffer buf=new StringBuffer();
            for(byte b:md.digest()){
                buf.append(String.format("%02x", b&0xff));
            }
            return  buf.toString();
        }catch( Exception e ){
            e.printStackTrace();

            return null;
        }
    }

    public static void main(String args[])
    {
        try {
            String inputStr = "accountId=9558804000146527623&accountName=谭凯旋&address=南山区&bankCode=102100099996&bankName=工商银行&certId=432302197912210012&certType=01&cmd=3012&consFee=10&consRate=0.0036&merCity=深圳市&merName=谭凯旋&merState=广东省&merchantId=999916886929000&mobile=13798594681&operFlag=A&realName=谭凯旋&timeStamp=20180126110530&key=b290d92e";
            //String inputStr =  "璀业";
            System.out.println(md5(inputStr));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
