package com.tonngw.rpc.api;

/**
 * api 服务接口
 * 
 * @author tonngw
 * @date 2022-01-26 9:53
 */
public interface HelloService {
    /**
     * 测试方法
     * @param object HelloObject
     * @return 响应字符串
     */
    String hello(HelloObject object);

}
