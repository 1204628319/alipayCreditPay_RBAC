package com.greatwall.jhgx.constants;

public interface PayConstant {
    String param_agencyType = "agencyType";
    String param_authCode = "authCode";
    String param_certId = "certId";
    String param_clientIp = "clientIp";
    String param_description = "description";
    String param_merOrderId = "merOrderId";
    String param_merchantId = "merchantId";
    String param_notifyUrl = "notifyUrl";
    String param_orderAmt = "orderAmt";
    String param_payType = "payType";
    String param_sign = "sign";
    String param_subject = "subject";
    String param_transTime = "transTime";

    String linkChar = "&";
    String equlsChar = "=";

    public static void main(String args[]) {
        String param_merOrderId = "merOrderId";
        String param_merchantId = "merchantId";

        int aa = param_merOrderId.compareTo(param_merchantId);

        System.out.println(aa);
    }













}
