package com.tonngw.rpc.hook;

import com.tonngw.rpc.factory.ThreadPoolFactory;
import com.tonngw.rpc.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * 注销服务钩子
 *
 * @author tonngw
 * @date 2022-01-26 20:12
 */
public class ShutdownHook {

    private static final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    private final ExecutorService threadPool = ThreadPoolFactory.createDefaultThreadPool("shutdown-hook");
    private static final ShutdownHook shutdownHook = new ShutdownHook();

    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    public void addClearAllHook() {
        logger.info("关闭后将自动注销所有服务");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            NacosUtil.clearRegistry();
            threadPool.shutdown();
        }));
    }

}
