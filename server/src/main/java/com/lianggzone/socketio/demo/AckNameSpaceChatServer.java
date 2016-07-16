package com.lianggzone.socketio.demo;

import com.corundumstudio.socketio.AckCallback;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.BroadcastAckCallback;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.VoidAckCallback;
import com.corundumstudio.socketio.listener.DataListener;
import com.lianggzone.socketio.corundumstudio.ChatObject;

/**
 * <p>Title: AckNameSpaceChatServer  </p>
 * <p>Description: AckNameSpaceChatServer </p>
 * <p>Create Time: 2016年7月16日           </p>
 * @author lianggz
 * @see https://github.com/mrniko/netty-socketio
 * @see https://github.com/mrniko/netty-socketio-demo
 */
public class AckNameSpaceChatServer {

    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        // 获取SocketIOServer
        final SocketIOServer server = new SocketIOServer(config);
        // 设置命名空间
        final SocketIONamespace namespace = server.addNamespace("/chat1");
        
        // 监听ackevent1事件
        //server.addEventListener("ackevent1", ChatObject.class, new DataListener<ChatObject>() {
        namespace.addEventListener("ackevent1", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(final SocketIOClient client, ChatObject data, final AckRequest ackRequest) {

                // check is ack requested by client, but it's not required check
                if (ackRequest.isAckRequested()) {
                    // send ack response with data to client
                    ackRequest.sendAckData("client message was delivered to server!", "yeah!");
                }

                // 发送消息给客户端，带ack callback WITH data
                ChatObject ackChatObjectData = new ChatObject(data.getUserName(), "message with ack data");
                client.sendEvent("ackevent2", new AckCallback<String>(String.class) {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println("ack from client: " + client.getSessionId() + " data: " + result);
                    }
                }, ackChatObjectData);

                // 发送消息给客户端，不带ack callback WITH data
                ChatObject ackChatObjectData1 = new ChatObject(data.getUserName(), "message with void ack");
                client.sendEvent("ackevent3", new VoidAckCallback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("void ack from: " + client.getSessionId());
                    }

                }, ackChatObjectData1);
                
                // 使用命名空间
                ChatObject ackChatObjectData2 = new ChatObject(data.getUserName(), "ack data broadcast message no namespace");
                server.getBroadcastOperations().sendEvent("ackevent4", data);
                server.getBroadcastOperations().sendEvent("ackevent4", ackChatObjectData2);
                
                
                // 不使用命名空间
                ChatObject ackChatObjectData3 = new ChatObject(data.getUserName(), "ack data broadcast message with namespace");
                namespace.getBroadcastOperations().sendEvent("ackevent5", ackChatObjectData3, new BroadcastAckCallback<String>(String.class){
                    protected void onAllSuccess() {
                        System.out.println("ack data broadcast message with namespace ok");
                    }
                });
            }
        });

        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }
}