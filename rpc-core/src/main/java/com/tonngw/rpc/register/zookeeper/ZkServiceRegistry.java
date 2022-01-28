package com.tonngw.rpc.register.zookeeper;

import com.tonngw.rpc.register.ServiceRegistry;
import com.tonngw.rpc.util.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * 基于 zookeeper 的服务注册
 *
 * @author tonngw
 * @date 2022-01-28 18:29
 */
public class ZkServiceRegistry implements ServiceRegistry {
    @Override
    public void register(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        // eg: /my-rpc/com.tonngw.HelloService/127.0.0.1:9999
        String servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName + inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        // 创建持久节点。与临时节点不同，当客户端断开连接时不会删除持久节点
        CuratorUtils.createPersistentNode(zkClient, servicePath);
    }
}
