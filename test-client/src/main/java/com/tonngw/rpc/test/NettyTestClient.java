package com.tonngw.rpc.test;

import com.tonngw.rpc.api.ByeService;
import com.tonngw.rpc.api.HelloObject;
import com.tonngw.rpc.api.HelloService;
import com.tonngw.rpc.serializer.CommonSerializer;
import com.tonngw.rpc.transport.RpcClient;
import com.tonngw.rpc.transport.RpcClientProxy;
import com.tonngw.rpc.transport.netty.client.NettyClient;

/**
 * 测试 Netty 客户端
 * 
 * @author tonngw
 * @date 2022-01-26 16:51
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient(CommonSerializer.PROTOBUF_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is netty message");
        String res = helloService.hello(object);
        System.out.println(res);
        ByeService byeService = rpcClientProxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));
    }
}
