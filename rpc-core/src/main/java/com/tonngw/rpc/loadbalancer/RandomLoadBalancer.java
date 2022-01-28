package com.tonngw.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * 随机负载均衡策略
 *
 * @author tonngw
 * @date 2022-01-26 20:20
 */
public class RandomLoadBalancer implements LoadBalancer {
    /**
     * 基于 Nacos 的随机负载均衡策略
     *
     * @param instances 实例集合
     * @return 服务实例
     */
    @Override
    public Instance select(List<Instance> instances) {
        return instances.get(new Random().nextInt(instances.size()));
    }

    /**
     * 基于 Zookeeper 的随机负载均衡策略
     *
     * @param serviceAddresses 服务地址列表
     * @param rpcServiceName   服务名称
     * @return 服务地址
     */
    protected String doSelect(List<String> serviceAddresses, String rpcServiceName) {
        return serviceAddresses.get(new Random().nextInt(serviceAddresses.size()));
    }

    @Override
    public String selectServiceAddress(List<String> serviceAddresses, String rpcServiceName) {
        if (serviceAddresses == null || serviceAddresses.size() == 0) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses, rpcServiceName);
    }
}
