package com.tonngw.rpc.exception;

import com.tonngw.rpc.enumeration.RpcError;

/**
 * Rpc 调用异常
 *
 * @author tonngw
 * @date 2022-01-26 12:30
 */
public class RpcException extends RuntimeException {
    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError error) {
        super(error.getMessage());
    }
}
