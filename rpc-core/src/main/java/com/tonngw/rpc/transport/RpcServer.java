package com.tonngw.rpc.transport;

import com.tonngw.rpc.serializer.CommonSerializer;

/**
 * RpcServer 通用接口
 *
 * @author tonngw
 * @date 2022-01-26 13:28
 */
public interface RpcServer {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    void start();

    /**
     * 向注册中心注册服务
     *
     * @param service     服务
     * @param serviceName 服务名称
     * @param <T>         T
     */
    <T> void publishService(T service, String serviceName);
}
