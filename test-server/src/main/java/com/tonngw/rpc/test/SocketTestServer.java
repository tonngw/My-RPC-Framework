package com.tonngw.rpc.test;

import com.tonngw.rpc.annotation.ServiceScan;
import com.tonngw.rpc.api.HelloService;
import com.tonngw.rpc.serializer.CommonSerializer;
import com.tonngw.rpc.transport.RpcServer;
import com.tonngw.rpc.transport.socket.server.SocketServer;

import javax.sql.CommonDataSource;

/**
 * 测试 Socket 服务端
 *
 * @author tonngw
 * @date 2022-01-26 11:31
 */
@ServiceScan
public class SocketTestServer {
    public static void main(String[] args) {
        RpcServer server = new SocketServer("127.0.0.1", 9999, CommonSerializer.HESSIAN_SERIALIZER);
        server.start();
    }
}
