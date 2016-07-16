package com.lianggzone.socketio.demo;

import java.util.Collection;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.VoidAckCallback;
import com.corundumstudio.socketio.listener.DataListener;

/**
 * <p>Title: AckNameSpaceChatServer  </p>
 * <p>Description: AckNameSpaceChatServer </p>
 * <p>Create Time: 2016年7月16日           </p>
 * @author lianggz
 * @see https://github.com/mrniko/netty-socketio
 * @see https://github.com/mrniko/netty-socketio-demo
 */
public class WeChatServer {

    public static void main(String[] args) throws InterruptedException {

        
        
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        // 获取SocketIOServer
        final SocketIOServer server = new SocketIOServer(config);
        
        // 监听ackevent1事件
        server.addEventListener("ackevent1", ChatParamModel.class, new DataListener<ChatParamModel>() {
            @Override
            public void onData(final SocketIOClient client, ChatParamModel data, final AckRequest ackRequest) {
                
                String reqUserId = data.getUserId();
                
                String myClientuserId = client.getHandshakeData().getHeaders().get("UserId")!=null?
                        client.getHandshakeData().getHeaders().get("UserId").get(0) : ""; 
                
                Collection<SocketIOClient> collection = server.getBroadcastOperations().getClients();
                System.out.println("当前连接数：" + collection.size());
                
                for (SocketIOClient socketIOClient : collection) {
                    String userId = socketIOClient.getHandshakeData().getHeaders().get("UserId")!=null?
                            socketIOClient.getHandshakeData().getHeaders().get("UserId").get(0) : "";       
                    
                    if(userId.equals(reqUserId)){
                        String msg = "hi, " + reqUserId + ", 我是: " + myClientuserId + ". Msg：" + data.getMessage() + ". Byte：" + data.getByteMsg();
                        System.out.println(new String(data.getByteMsg()));
                        socketIOClient.sendEvent("ackevent2", new VoidAckCallback() {
                            @Override
                            public void onSuccess() {
                                System.out.println("ack from client: " + client.getSessionId());
                            }
                        }, msg);
                    }
                }
               
            }
        });

        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }
}