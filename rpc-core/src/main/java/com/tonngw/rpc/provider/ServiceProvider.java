package com.tonngw.rpc.provider;

/**
 * 本地服务提供接口
 *
 * @author tonngw
 * @date 2022-01-26 12:15
 */
public interface ServiceProvider {

    /**
     * 将一个服务注册到注册表
     *
     * @param service     待注册的服务
     * @param serviceName 服务名称
     * @param <T>         T
     */
    <T> void addServiceProvider(T service, String serviceName);

    /**
     * 根据服务名称获取服务
     *
     * @param serviceName 服务名称
     * @return 服务
     */
    Object getServiceProvider(String serviceName);
}
