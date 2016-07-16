package com.lianggzone.socketio.demo;

public class ChatParamModel {

    private String userId;
    private String message;
    private byte[] byteMsg;

    public ChatParamModel() {
    }

    public ChatParamModel(String userId, String message, byte[] byteMsg) {
        super();
        this.userId = userId;
        this.message = message;
        this.byteMsg = byteMsg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getByteMsg() {
        return byteMsg;
    }

    public void setByteMsg(byte[] byteMsg) {
        this.byteMsg = byteMsg;
    }
}
