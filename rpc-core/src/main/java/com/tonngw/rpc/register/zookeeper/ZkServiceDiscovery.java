package com.tonngw.rpc.register.zookeeper;

import com.tonngw.rpc.enumeration.RpcError;
import com.tonngw.rpc.exception.RpcException;
import com.tonngw.rpc.loadbalancer.LoadBalancer;
import com.tonngw.rpc.loadbalancer.RandomLoadBalancer;
import com.tonngw.rpc.register.ServiceDiscovery;
import com.tonngw.rpc.util.CuratorUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 基于 zookeeper 的服务发现
 *
 * @author tonngw
 * @date 2022-01-28 18:29
 */
public class ZkServiceDiscovery implements ServiceDiscovery {
    private static final Logger logger = LoggerFactory.getLogger(ZkServiceDiscovery.class);

    private final LoadBalancer loadBalancer;

    public ZkServiceDiscovery(LoadBalancer loadBalancer) {
        if (loadBalancer == null) {
            this.loadBalancer = new RandomLoadBalancer();
        } else {
            this.loadBalancer = loadBalancer;
        }
    }

    /**
     * 根据服务名到 zookeeper 寻找对应的服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     */
    @Override
    public InetSocketAddress lookupService(String serviceName) {
        System.out.println(serviceName + "11111111111");
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        System.out.println(serviceName + "22222222222");
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient, serviceName);
        if (serviceUrlList == null || serviceUrlList.size() == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        // 负载均衡
        String targetServiceUrl = loadBalancer.selectServiceAddress(serviceUrlList, serviceName);
        logger.info("成功寻找到服务地址 :[{}]", targetServiceUrl); //eg：192.168.199.171:6666
        String[] socketAddressArray = targetServiceUrl.split(":");
        // socketAddressArray[0]： 127.0.0.1
        String host = socketAddressArray[0];
        // socketAddressArray[1]： 6666
        int port = Integer.parseInt(socketAddressArray[1]);
        // 返回服务地址(ip + 端口号)
        return new InetSocketAddress(host, port);
    }
}
