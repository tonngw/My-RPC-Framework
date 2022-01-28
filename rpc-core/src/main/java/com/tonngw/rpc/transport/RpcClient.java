package com.tonngw.rpc.transport;

import com.tonngw.rpc.entity.RpcRequest;
import com.tonngw.rpc.serializer.CommonSerializer;

/**
 * RpcClient 通用接口
 *
 * @author tonngw
 * @date 2022-01-26 13:28
 */
public interface RpcClient {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;

    Object sendRequest(RpcRequest rpcRequest);
}
