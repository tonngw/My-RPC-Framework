package com.tonngw.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 轮询负载均衡策略
 *
 * @author tonngw
 * @date 2022-01-26 20:22
 */
public class RoundRobinLoadBalancer implements LoadBalancer {

    private int index = 0;

    /**
     * 基于 Nacos 的轮询负载均衡策略
     *
     * @param instances 实例集合
     * @return 服务实例
     */
    @Override
    public Instance select(List<Instance> instances) {
        if (index >= instances.size()) {
            index %= instances.size();
        }
        return instances.get(index++);
    }

    /**
     * 基于 Zookeeper 的轮询负载均衡策略
     *
     * @param serviceAddresses 服务地址列表
     * @param rpcServiceName   服务名称
     * @return 服务地址
     */
    protected String doSelect(List<String> serviceAddresses, String rpcServiceName) {
        if (index >= serviceAddresses.size()) {
            index %= serviceAddresses.size();
        }
        return serviceAddresses.get(index++);
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
