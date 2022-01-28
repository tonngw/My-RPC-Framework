package com.tonngw.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 客户端向服务端传输的请求对象
 * 
 * @author tonngw
 * @date 2022-01-26 10:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {
    
    /**
     * 请求号
     */
    private String requestId;

    /**
     * 待调用接口名称
     */
    private String interfaceName;

    /**
     * 待调用方法名称
     */
    private String methodName;

    /**
     * 待调用方法的参数类型
     */
    private Object[] parameters;

    /**
     * 待调用方法的参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 是否是心跳包
     */
    private Boolean heartBeat;
}
