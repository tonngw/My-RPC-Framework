package com.tonngw.rpc.handler;

import com.tonngw.rpc.entity.RpcRequest;
import com.tonngw.rpc.entity.RpcResponse;
import com.tonngw.rpc.enumeration.ResponseCode;
import com.tonngw.rpc.provider.ServiceProvider;
import com.tonngw.rpc.provider.ServiceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 实际进行反射调用方法的处理器
 *
 * @author tonngw
 * @date 2022-01-26 12:53
 */
public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final ServiceProvider serviceProvider;

    static {
        serviceProvider = new ServiceProviderImpl();
    }

    public Object handle(RpcRequest rpcRequest) {
        // 从服务端本地注册表中获取服务实体
        Object service = serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest, service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            // getClass() 获取的是实例对象的类型
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            logger.info("服务：{}成功调用方法：{}", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            // 方法调用失败
            return RpcResponse.fail(ResponseCode.METHOD_NOT_FOUND, rpcRequest.getRequestId());
        }
        // 方法调用成功
        return RpcResponse.success(result, rpcRequest.getRequestId());
    }
}
