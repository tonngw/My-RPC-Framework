package com.tonngw.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 负载均衡接口
 *
 * @author tonngw
 * @date 2022-01-26 20:17
 */
public interface LoadBalancer {
    /**
     * 通过负载均衡策略从 instances 集合中选一个实例
     *
     * @param instances 实例集合
     * @return 选出的实例
     */
    Instance select(List<Instance> instances);

    /**
     * Zookeeper 中查询服务
     *
     * @param serviceAddresses 服务地址列表
     * @param rpcServiceName 服务名称
     * @return 服务地址
     */
    String selectServiceAddress(List<String> serviceAddresses, String rpcServiceName);
}
