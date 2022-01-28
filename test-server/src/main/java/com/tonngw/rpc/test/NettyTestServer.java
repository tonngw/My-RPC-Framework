package com.tonngw.rpc.test;

import com.tonngw.rpc.annotation.ServiceScan;
import com.tonngw.rpc.serializer.CommonSerializer;
import com.tonngw.rpc.transport.netty.server.NettyServer;

/**
 * 测试 Netty 服务端
 *
 * @author tonngw
 * @date 2022-01-26 16:51
 */
@ServiceScan
public class NettyTestServer {
    public static void main(String[] args) {
        NettyServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
        server.start();
    }
}
