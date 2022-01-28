package com.tonngw.rpc.test;

import com.esotericsoftware.kryo.serializers.DefaultSerializers;
import com.tonngw.rpc.api.ByeService;
import com.tonngw.rpc.api.HelloObject;
import com.tonngw.rpc.api.HelloService;
import com.tonngw.rpc.loadbalancer.RoundRobinLoadBalancer;
import com.tonngw.rpc.serializer.CommonSerializer;
import com.tonngw.rpc.transport.RpcClientProxy;
import com.tonngw.rpc.transport.socket.client.SocketClient;

/**
 * 测试 Socket 客户端
 * 
 * @author tonngw
 * @date 2022-01-26 11:30
 */
public class SocketTestClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient(CommonSerializer.HESSIAN_SERIALIZER, new RoundRobinLoadBalancer());
        // 代理客户端
        RpcClientProxy proxy = new RpcClientProxy(client);
        // 创建代理对象
        HelloService helloService = proxy.getProxy(HelloService.class);
        // 接口方法的参数对象
        HelloObject object = new HelloObject(12, "This is a message");
        // 由动态代理可知，代理对象调用 hello() 实际会执行 invoke()
        String res = helloService.hello(object);
        System.out.println(res);
        ByeService byeService = proxy.getProxy(ByeService.class);
        System.out.println(byeService.bye("Netty"));
    }
}
