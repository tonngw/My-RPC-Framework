package com.tonngw.rpc.util;

import com.tonngw.rpc.entity.RpcRequest;
import com.tonngw.rpc.entity.RpcResponse;
import com.tonngw.rpc.enumeration.ResponseCode;
import com.tonngw.rpc.enumeration.RpcError;
import com.tonngw.rpc.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 检查响应和请求消息
 *
 * @author tonngw
 * @date 2022-01-26 18:02
 */
public class RpcMessageChecker {

    private static final String INTERFACE_NAME = "interfaceName";

    public static final Logger logger = LoggerFactory.getLogger(RpcMessageChecker.class);

    private RpcMessageChecker() {
    }

    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse) {
        // 响应为 null
        if (rpcResponse == null) {
            logger.error("调用服务失败，serviceName:{}", rpcRequest.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        // 响应与请求的请求号不同
        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcError.RESPONSE_NOT_MATCH, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
        // 服务调用失败
        if (rpcResponse.getStatusCode() == null || !rpcResponse.getStatusCode().equals(ResponseCode.SUCCESS.getCode())) {
            logger.error("调用服务失败，serviceName:{}，RpcResponse:{}", rpcRequest.getInterfaceName(), rpcResponse);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }

}
