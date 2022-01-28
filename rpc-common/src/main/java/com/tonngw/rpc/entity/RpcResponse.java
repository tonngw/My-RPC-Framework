package com.tonngw.rpc.entity;

import com.tonngw.rpc.enumeration.ResponseCode;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 服务端向客户端返回的响应对象
 * 
 * @author tonngw
 * @date 2022-01-26 10:17
 */
@Data
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {

    /**
     * 响应对应的请求号
     */
    private String requestId;

    /**
     * 响应状态码
     */
    private Integer statusCode;

    /**
     * 响应状态码补充信息
     */
    private String message;

    /**
     * 成功时的响应数据
     */
    private T data;

    /**
     * 处理成功时服务端返回的对象
     * @param data 响应数据
     * @param requestId 请求号
     * @param <T> T
     * @return 响应对象
     */
    public static <T> RpcResponse<T> success(T data, String requestId){
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    /**
     * 处理失败时服务端返回的对象
     * @param code 响应状态码
     * @param requestId 请求号
     * @param <T> T
     * @return 响应对象
     */
    public static <T> RpcResponse<T> fail(ResponseCode code, String requestId){
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}


