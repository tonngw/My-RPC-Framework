package com.tonngw.rpc.transport.socket.server;

import com.esotericsoftware.kryo.io.Output;
import com.tonngw.rpc.entity.RpcRequest;
import com.tonngw.rpc.entity.RpcResponse;
import com.tonngw.rpc.handler.RequestHandler;
import com.tonngw.rpc.serializer.CommonSerializer;
import com.tonngw.rpc.transport.socket.util.ObjectReader;
import com.tonngw.rpc.transport.socket.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.net.Socket;

/**
 * 服务端工作线程处理客户端请求
 *
 * @author tonngw
 * @date 2022-01-26 12:44
 */
public class SocketRequestHandlerThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SocketRequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private CommonSerializer serializer;

    public SocketRequestHandlerThread(Socket socket, RequestHandler requestHandler, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream()){
                RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
                Object response = requestHandler.handle(rpcRequest);
                ObjectWriter.writeObject(outputStream, response, serializer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}