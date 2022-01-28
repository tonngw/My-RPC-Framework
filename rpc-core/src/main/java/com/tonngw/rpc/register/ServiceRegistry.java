package com.tonngw.rpc.register;

import java.net.InetSocketAddress;

/**
 * 服务注册接口
 *
 * @author tonngw
 * @date 2022-01-26 18:51
 */
public interface ServiceRegistry {

    /**
     * 将一个服务注册到注册中心
     *
     * @param serviceName       服务名称
     * @param inetSocketAddress 服务地址
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);
}
