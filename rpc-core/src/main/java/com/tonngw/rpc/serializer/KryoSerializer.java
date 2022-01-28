package com.tonngw.rpc.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.tonngw.rpc.entity.RpcRequest;
import com.tonngw.rpc.entity.RpcResponse;
import com.tonngw.rpc.enumeration.SerializerCode;
import com.tonngw.rpc.exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Kryo 序列化器
 * <p>
 * Kryo是一种快速高效的Java对象图（Object graph）序列化框架。 该项目的目标是速度、效率和易于使用的API。 当对象需要持久化时，无论是用于文件、数据库还是通过网络，该项目都很有用。
 *
 * @author tonngw
 * @date 2022-01-26 17:28
 */
public class KryoSerializer implements CommonSerializer {

    private static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        // 注册类
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        // 循环引用检测，默认为 true
        kryo.setReferences(true);
        // 不强制要求注册类，默认为 false，若设置为 true 则要求涉及到的所有类都要注册，包括 jdk 中的所有类，如 Object
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (IOException e) {
            logger.error("序列化时有错误发生：" + e);
            throw new SerializeException("序列化时有错误发生");
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return o;
        } catch (Exception e) {
            logger.error("反序列化时有错误发生：" + e);
            throw new SerializeException("反序列化时有错误发生");
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("KRYO").getCode();
    }
}
