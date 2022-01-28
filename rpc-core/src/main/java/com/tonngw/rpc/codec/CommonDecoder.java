package com.tonngw.rpc.codec;

import com.tonngw.rpc.entity.RpcRequest;
import com.tonngw.rpc.entity.RpcResponse;
import com.tonngw.rpc.enumeration.PackageType;
import com.tonngw.rpc.enumeration.RpcError;
import com.tonngw.rpc.exception.RpcException;
import com.tonngw.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 通用的解码拦截器
 *
 * @author tonngw
 * @date 2022-01-26 15:56
 */
public class CommonDecoder extends ReplayingDecoder {
    private static final Logger logger = LoggerFactory.getLogger(CommonDecoder.class);
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    /**
     * 解码器就是将收到的字节序列还原为实际对象，主要就是进行字段的校验，比较重要的就是取出序列化器编号，以获得正确的反序列化方式，
     * 并且利用length字段来确定数据包的长度（防止粘包），读出正确长度的字节数组，然后反序列化成对应的对象。
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        // 从缓冲区中读取数据

        // 获取魔数
        int magic = in.readInt();
        if (magic != MAGIC_NUMBER) {
            logger.error("不识别的协议包：{}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }

        // 获取数据包类型编号
        int packageCode = in.readInt();
        Class<?> packageClass;
        // 请求包
        if (packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if (packageCode == PackageType.RESPONSE_PACK.getCode()) {
            // 响应包
            packageClass = RpcResponse.class;
        } else {
            logger.error("不识别的数据包：{}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }

        // 获取序列器编号
        int serializerCode = in.readInt();
        // 根据序列化器编号获取序列化器
        CommonSerializer serializer = CommonSerializer.getByCode(serializerCode);
        if (serializer == null) {
            logger.error("不能识别的反序列化器：{}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }

        // 获取数据的长度并读取
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.readBytes(bytes);
        Object obj = serializer.deserialize(bytes, packageClass);
        // 添加到对象列表中
        list.add(obj);
    }
}
