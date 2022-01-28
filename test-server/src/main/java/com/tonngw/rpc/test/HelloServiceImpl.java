package com.tonngw.rpc.test;

import com.tonngw.rpc.annotation.Service;
import com.tonngw.rpc.api.HelloObject;
import com.tonngw.rpc.api.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端 api HelloService 接口实现
 *
 * @author tonngw
 * @date 2022-01-26 10:03
 */
@Service
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收信息: {}", object.getMessage());
        return "调用的返回值，id=" + object.getId();
    }
}
