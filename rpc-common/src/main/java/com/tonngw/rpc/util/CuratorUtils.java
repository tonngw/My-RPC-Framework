package com.tonngw.rpc.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 操作 Zookeeper 客户端工具类
 *
 * @author tonngw
 * @date 2022-01-28 18:30
 */
public class CuratorUtils {
    private static final Logger logger = LoggerFactory.getLogger(CuratorUtils.class);

    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    public static final String ZK_REGISTER_ROOT_PATH = "/my-rpc";
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    private static CuratorFramework zkClient;
    private static final String DEFAULT_ZOOKEEPER_ADDRESS = "127.0.0.1:2181";

    private CuratorUtils() {
    }

    /**
     * 创建持久节点。与临时节点不同，当客户端断开连接时不会删除持久节点
     *
     * @param zkClient zk 客户端
     * @param path     路径
     */
    public static void createPersistentNode(CuratorFramework zkClient, String path) {
        try {
            if (REGISTERED_PATH_SET.contains(path) || zkClient.checkExists().forPath(path) != null) {
                logger.info("The node already exists. The node is:[{}]", path);
            } else {
                //eg: /my-rpc/com.xzs.HelloService/127.0.0.1:9999
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                logger.info("The node was created successfully. The node is:[{}]", path);
            }
            REGISTERED_PATH_SET.add(path);
        } catch (Exception e) {
            logger.error("create persistent node for path [{}] fail", path);
        }
    }

    /**
     * 获取节点下的子节点
     *
     * @param rpcServiceName rpc service name eg:com.tonngw.HelloService
     * @return 指定节点下的所有子节点
     */
    public static List<String> getChildrenNodes(CuratorFramework zkClient, String rpcServiceName) {
        if (SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)) {
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }
        List<String> result = null;
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        try {
            result = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, result);
            registerWatcher(rpcServiceName, zkClient);
        } catch (Exception e) {
            logger.error("get children nodes for path [{}] fail", servicePath);
        }
        return result;
    }

    /**
     * 清空注册中心的数据
     *
     * @param zkClient          zk 客户端
     * @param inetSocketAddress 服务地址
     */
    public static void clearRegistry(CuratorFramework zkClient, InetSocketAddress inetSocketAddress) {
        REGISTERED_PATH_SET.stream().parallel().forEach(p -> {
            try {
                if (p.endsWith(inetSocketAddress.toString())) {
                    zkClient.delete().forPath(p);
                }
            } catch (Exception e) {
                logger.error("clear registry for path [{}] fail", p);
            }
        });
        logger.info("All registered services on the server are cleared:[{}]", REGISTERED_PATH_SET.toString());
    }

    public static CuratorFramework getZkClient() {
        // System.out.println("0000000000000000000000000000");
        // 检查用户是否设置了zk地址
        Properties properties = PropertiesFileUtil.readPropertiesFile("rpc.properties");
        String zookeeperAddress = DEFAULT_ZOOKEEPER_ADDRESS;
        // System.out.println("111111111111111111111111");
        // 如果zkClient已经启动，则直接返回
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            return zkClient;
        }
        // 重试策略。重试3次，并将增加重试之间的睡眠时间。
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        // System.out.println("2222222222222222222222222");
        zkClient = CuratorFrameworkFactory.builder()
                // 要连接到的服务器（可以是服务器列表）
                .connectString(zookeeperAddress)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        try {
            // 等待30秒，直到连接到zookeeper
            if (!zkClient.blockUntilConnected(30, TimeUnit.SECONDS)) {
                throw new RuntimeException("Time out waiting to connect to ZK!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zkClient;
    }

    /**
     * Registers to listen for changes to the specified node
     * 注册以侦听对指定节点的更改
     *
     * @param rpcServiceName rpc service name eg:com.tonngw.HelloService
     */
    private static void registerWatcher(String rpcServiceName, CuratorFramework zkClient) throws Exception {
        String servicePath = ZK_REGISTER_ROOT_PATH + "/" + rpcServiceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(zkClient, servicePath, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> serviceAddresses = curatorFramework.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName, serviceAddresses);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        pathChildrenCache.start();
    }
}
