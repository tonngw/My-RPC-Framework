package com.tonngw.rpc.transport;

import com.tonngw.rpc.entity.RpcRequest;
import com.tonngw.rpc.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * 服务端工作线程处理客户端请求（弃用）
 *
 * @author tonngw
 * @date 2022-01-26 11:12
 */
@Deprecated
public class WorkerThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(WorkerThread.class);

    private Socket socket;
    private Object service;

    public WorkerThread(Socket socket, Object service) {
        this.socket = socket;
        this.service = service;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            //利用反射原理找到远程所需调用的方法
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            // 反射调用
            Object returnObject = method.invoke(service, rpcRequest.getParameters());
            objectOutputStream.writeObject(RpcResponse.success(returnObject, rpcRequest.getRequestId()));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.info("调用或发送时有错误发生：" + e);
        }
    }
}
