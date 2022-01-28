package com.tonngw.rpc.register.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import com.tonngw.rpc.enumeration.RpcError;
import com.tonngw.rpc.exception.RpcException;
import com.tonngw.rpc.register.ServiceRegistry;
import com.tonngw.rpc.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Nacos 服务注册中心
 *
 * @author tonngw
 * @date 2022-01-26 18:57
 */
public class NacosServiceRegistry implements ServiceRegistry {

    private static final Logger logger = LoggerFactory.getLogger(NacosServiceRegistry.class);

    /**
     * 向 Nacos 中注册服务
     *
     * @param serviceName       服务名称
     * @param inetSocketAddress 服务地址
     */
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName, inetSocketAddress);
        } catch (NacosException e) {
            logger.error("注册服务时有错误发生" + e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }
}
