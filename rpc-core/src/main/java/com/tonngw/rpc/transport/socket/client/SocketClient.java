package com.tonngw.rpc.transport.socket.client;

import com.tonngw.rpc.entity.RpcRequest;
import com.tonngw.rpc.entity.RpcResponse;
import com.tonngw.rpc.enumeration.RpcError;
import com.tonngw.rpc.exception.RpcException;
import com.tonngw.rpc.loadbalancer.LoadBalancer;
import com.tonngw.rpc.loadbalancer.RandomLoadBalancer;
import com.tonngw.rpc.register.nacos.NacosServiceDiscovery;
import com.tonngw.rpc.register.ServiceDiscovery;
import com.tonngw.rpc.register.zookeeper.ZkServiceDiscovery;
import com.tonngw.rpc.serializer.CommonSerializer;
import com.tonngw.rpc.transport.RpcClient;
import com.tonngw.rpc.transport.socket.util.ObjectReader;
import com.tonngw.rpc.transport.socket.util.ObjectWriter;
import com.tonngw.rpc.util.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Socket 客户端
 * 
 * @author tonngw
 * @date 2022-01-28 11:14:50
 */
public class SocketClient implements RpcClient {

    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    private final ServiceDiscovery serviceDiscovery;
    
    private final CommonSerializer serializer;

    public SocketClient(){
        // 以默认序列化器调用构造函数
        this(DEFAULT_SERIALIZER, new RandomLoadBalancer());
    }

    public SocketClient(LoadBalancer loadBalancer){
        this(DEFAULT_SERIALIZER, loadBalancer);
    }

    public SocketClient(Integer serializerCode){
        this(serializerCode, new RandomLoadBalancer());
    }

    public SocketClient(Integer serializerCode, LoadBalancer loadBalancer){
        // serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        serviceDiscovery = new ZkServiceDiscovery(loadBalancer);
        serializer = CommonSerializer.getByCode(serializerCode);
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        // 从 Nacos 中获取提供对应服务的服务端地址
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        
        // 使用 socket 套接字实现 TCP 网络传输
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            RpcMessageChecker.check(rpcRequest, rpcResponse);
            return rpcResponse;
        } catch (IOException e) {
            logger.error("调用时有错误发生：" + e);
            throw new RpcException("服务调用失败：", e);
        }
    }
}