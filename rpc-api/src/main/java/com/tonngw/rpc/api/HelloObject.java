package com.tonngw.rpc.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 定义客户端传给服务端接口的参数
 * 
 * @author tonngw
 * @date 2022-01-26 9:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelloObject implements Serializable {
    private Integer id;
    private String message;
}
