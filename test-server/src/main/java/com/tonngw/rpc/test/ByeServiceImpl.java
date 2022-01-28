package com.tonngw.rpc.test;

import com.tonngw.rpc.annotation.Service;
import com.tonngw.rpc.api.ByeService;

/**
 * 服务实现类
 *
 * @author tonngw
 * @date 2022-01-26 22:48
 */
@Service
public class ByeServiceImpl implements ByeService {
    @Override
    public String bye(String name) {
        return "bye," + name;
    }
}
