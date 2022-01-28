package com.tonngw.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据包类型
 *
 * @author tonngw
 * @date 2022-01-26 15:51
 */
@Getter
@AllArgsConstructor
public enum PackageType {

    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;

}
