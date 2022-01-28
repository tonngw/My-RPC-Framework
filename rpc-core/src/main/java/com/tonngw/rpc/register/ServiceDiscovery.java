package com.tonngw.rpc.register;

import java.net.InetSocketAddress;

/**
 * 服务发现接口
 *
 * @author tonngw
 * @date 2022-01-26 20:25
 */
public interface ServiceDiscovery {
    /**
     * 根据服务名称从注册中心中查找一个服务地址
     *
     * @param serviceName 服务名称
     * @return 服务地址
     */
    InetSocketAddress lookupService(String serviceName);
}
