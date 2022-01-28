package com.tonngw.rpc.serializer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tonngw.rpc.entity.RpcRequest;
import com.tonngw.rpc.enumeration.SerializerCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Json 序列化器
 * <p>
 * 基于Jackson的序列化器，但通过使用也发现存在一个问题，Json进行反序列化时，如果某个类的属性声明是Object类型，就会造成反序列化出错，通常会把Object属性直接反序列化成String类型，此时就需要其他参数辅助反序列化。同时，JSON序列化器是基于字符串（JSON串）的，占用空间较大且速度较慢。
 *
 * @author tonngw
 * @date 2022-01-26 16:19
 */
public class JsonSerializer implements CommonSerializer {
    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    private ObjectMapper ojbectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Object obj) {
        try {
            return ojbectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            logger.error("序列化时有错误发生：{}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            Object obj = ojbectMapper.readValue(bytes, clazz);
            if (obj instanceof RpcRequest) {
                obj = handleRequest(obj);
            }
            return obj;
        } catch (IOException e) {
            logger.error("反序列化时有错误发生：{}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用JSON反序列化Object数组时，无法保证反序列化后仍然为原实例类，因此要特殊处理
     *
     * @param obj
     * @return
     */
    private Object handleRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        for (int i = 0; i < rpcRequest.getParamTypes().length; i++) {
            Class<?> clazz = rpcRequest.getParamTypes()[i];
            if (!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())) {
                byte[] bytes = ojbectMapper.writeValueAsBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = ojbectMapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }
}
