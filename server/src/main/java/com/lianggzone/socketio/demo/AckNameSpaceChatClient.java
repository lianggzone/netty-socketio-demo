package com.lianggzone.socketio.demo;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>Title: AckNameSpaceChatClient  </p>
 * <p>Description: AckNameSpaceChatClient </p>
 * <p>Create Time: 2016年7月16日           </p>
 * @author lianggz
 * @see https://github.com/socketio/socket.io-client-java
 */
public class AckNameSpaceChatClient {

    public static void main(String[] args) throws Exception {
        // 配置IO Options 
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = true; // DEFALUT
        opts.timeout = 20000;     // DEFALUT, Connection timeout (ms). Set -1 to disable.
        // 配置Socket
        final Socket socket = IO.socket("http://localhost:9092/chat1", opts);
        // 建立监听
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("the client has connected to the server!");
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("the client has disconnected!");
            }
        }).on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
            // HTTP headers
            @Override
            public void call(Object... args) {
                Transport transport = (Transport)args[0];
                // Request Headers
                transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        @SuppressWarnings("unchecked")
                        Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
                        // modify request headers
                        headers.put("Authorization", Arrays.asList("DEBUG userid=723181"));
                    }
                });
                // Response Headers
                transport.on(Transport.EVENT_RESPONSE_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        @SuppressWarnings("unchecked")
                        Map<String, List<String>> headers = (Map<String, List<String>>)args[0];
                        // access response headers
                        String cookie = headers.get("Set-Cookie").get(0);
                        System.out.println(cookie);
                    }
                });
            }
        })
        // 自定义，事件监听
        .on("ackevent4", new Emitter.Listener() {
              @Override
              public void call(Object... data) {
                  List<Object> list = Arrays.asList(data);
                  System.out.println(list.get(0).toString());
              }

        }).on("ackevent5", new Emitter.Listener() {
              @Override
              public void call(Object... data) {
                  List<Object> list = Arrays.asList(data);
                  System.out.println(list.get(0).toString());
              }

        })
      
        ;
        
        socket.connect();
      
      
        while (true) {
            System.out.println("=====>");
            Thread.sleep(20000L);
            // 发送一个消息
            JSONObject obj = new JSONObject();
            obj.put("userName", "lianggzone");
            obj.put("message", "LiangGzone， 定时发送消息：" + new Date());
            socket.emit("ackevent1", obj);
            System.out.println("<====="); 
        } 
    }
}