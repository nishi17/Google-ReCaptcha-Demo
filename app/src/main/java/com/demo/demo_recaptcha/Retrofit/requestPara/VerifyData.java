package com.demo.demo_recaptcha.Retrofit.requestPara;

public class VerifyData {

    private String secret;
    private String response;
    private String remoteip;


    public VerifyData(String secret, String response, String remoteip) {
        this.secret = secret;
        this.response = response;
        this.remoteip = remoteip;
    }

    public String getRemoteip() {
        return remoteip;
    }

    public void setRemoteip(String remoteip) {
        this.remoteip = remoteip;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
