package com.tonngw.rpc.transport.socket.server;

import com.tonngw.rpc.factory.ThreadPoolFactory;
import com.tonngw.rpc.handler.RequestHandler;
import com.tonngw.rpc.hook.ShutdownHook;
import com.tonngw.rpc.provider.ServiceProviderImpl;
import com.tonngw.rpc.register.nacos.NacosServiceRegistry;
import com.tonngw.rpc.register.zookeeper.ZkServiceRegistry;
import com.tonngw.rpc.serializer.CommonSerializer;
import com.tonngw.rpc.transport.AbstractRpcServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Socket 服务端
 *
 * @author tonngw
 * @date 2022-01-26 11:02
 */
public class SocketServer extends AbstractRpcServer {
    
    private final ExecutorService threadPool;
    private final CommonSerializer serializer;
    private final RequestHandler requestHandler = new RequestHandler();

    public SocketServer(String host, int port) {
        this(host, port, DEFAULT_SERIALIZER);
    }
    
    public SocketServer(String host, int port, Integer serializerCode) {
        this.host = host;
        this.port = port;
        // serviceRegistry = new NacosServiceRegistry();
        serviceRegistry = new ZkServiceRegistry();
        serviceProvider = new ServiceProviderImpl();
        serializer = CommonSerializer.getByCode(serializerCode);
        // 创建线程池
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        // 自动注册服务
        scanServices();
    }

    /**
     * 启动服务端
     */
    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress(host, port));
            logger.info("服务器正在启动...");
            // 添加钩子，服务端关闭时会注销服务
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            // 当未接收到连接请求时，accept()会一直阻塞
            while ((socket = serverSocket.accept()) != null) {
                logger.info("客户端连接, {}:{}" + socket.getInetAddress(), socket.getPort());
                threadPool.execute(new SocketRequestHandlerThread(socket, requestHandler, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("服务器启动时有错误发生：", e);
        }
    }
}
