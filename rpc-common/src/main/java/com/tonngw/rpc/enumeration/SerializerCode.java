package com.tonngw.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 序列化器编号枚举类
 *
 * @author tonngw
 * @date 2022-01-26 16:25
 */
@AllArgsConstructor
@Getter
public enum SerializerCode {

    KRYO(0),
    JSON(1),
    HESSIAN(2),
    PROTOBUF(3);

    private final int code;
}
