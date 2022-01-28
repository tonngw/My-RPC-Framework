package com.tonngw.rpc.codec;

import com.tonngw.rpc.entity.RpcRequest;
import com.tonngw.rpc.enumeration.PackageType;
import com.tonngw.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 通用的编码拦截器
 *
 * @author tonngw
 * @date 2022-01-26 15:47
 */
public class CommonEncoder extends MessageToByteEncoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    /**
     * 自定义协议数据包格式
     * +---------------+---------------+-----------------+-------------+
     * |  Magic Number |  Package Type | Serializer Type | Data Length |
     * |    4 bytes    |    4 bytes    |     4 bytes     |   4 bytes   |
     * +---------------+---------------+-----------------+-------------+
     * |                          Data Bytes                           |
     * |                   Length: ${Data Length}                      |
     * +---------------------------------------------------------------+
     * <p>
     * 编码器的工作就是把原始数据转换为字节流，然后根据上面提到的协议格式，将各个字段写到一个字节数组中（堆外内存ByteBuf[ ]），
     * 这样的字节数组就是发送出去的自定义协议包。
     *
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 将数据写到缓冲区
        out.writeInt(MAGIC_NUMBER);
        if (msg instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        } else {
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        }
        out.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
