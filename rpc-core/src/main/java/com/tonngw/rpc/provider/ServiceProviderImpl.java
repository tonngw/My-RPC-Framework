package com.tonngw.rpc.provider;

import com.tonngw.rpc.enumeration.RpcError;
import com.tonngw.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地服务提供接口默认实现
 *
 * @author tonngw
 * @date 2022-01-26 12:20
 */
public class ServiceProviderImpl implements ServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);
    /**
     * key = 服务名称(即接口名), value = 服务实体(即实现类的实例对象)
     */
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    /**
     * 用来存放服务名称(即接口名）
     */
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    /**
     * 保存服务到本地服务注册表
     *
     * @param service     待注册的服务
     * @param serviceName 服务名称
     * @param <T>         T
     */
    @Override
    public <T> void addServiceProvider(T service, String serviceName) {
        if (registeredService.contains(serviceName)) {
            return;
        }
        registeredService.add(serviceName);
        serviceMap.put(serviceName, service);
        logger.info("向接口：{} 注册服务：{}", service.getClass().getInterfaces(), serviceName);
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}