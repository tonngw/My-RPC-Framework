package com.tonngw.rpc.exception;

/**
 * 序列化异常类
 *
 * @author tonngw
 * @date 2022-01-26 17:38
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String msg) {
        super(msg);
    }
}
