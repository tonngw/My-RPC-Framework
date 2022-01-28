// package com.tonngw.rpc.loadbalancer;
//
// import com.alibaba.nacos.api.naming.pojo.Instance;
// import com.tonngw.rpc.entity.RpcRequest;
// import lombok.extern.slf4j.Slf4j;
//
// import java.nio.charset.StandardCharsets;
// import java.security.MessageDigest;
// import java.security.NoSuchAlgorithmException;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Map;
// import java.util.TreeMap;
// import java.util.concurrent.ConcurrentHashMap;
//
// /**
//  * refer to dubbo consistent hash load balance: https://github.com/apache/dubbo/blob/2d9583adf26a2d8bd6fb646243a9fe80a77e65d5/dubbo-cluster/src/main/java/org/apache/dubbo/rpc/cluster/loadbalance/ConsistentHashLoadBalance.java
//  *
//  * @author RicardoZ
//  * @date  @date 2022-01-28 20:35
//  */
// @Slf4j
// public class ConsistentHashLoadBalance implements LoadBalancer {
//     private final ConcurrentHashMap<String, ConsistentHashSelector> selectors = new ConcurrentHashMap<>();
//
//     protected String doSelect(List<String> serviceAddresses, RpcRequest rpcRequest) {
//         int identityHashCode = System.identityHashCode(serviceAddresses);
//         // build rpc service name by rpcRequest
//         String rpcServiceName = rpcRequest.getRpcServiceName();
//         ConsistentHashSelector selector = selectors.get(rpcServiceName);
//         // check for updates
//         if (selector == null || selector.identityHashCode != identityHashCode) {
//             selectors.put(rpcServiceName, new ConsistentHashSelector(serviceAddresses, 160, identityHashCode));
//             selector = selectors.get(rpcServiceName);
//         }
//         return selector.select(rpcServiceName + Arrays.stream(rpcRequest.getParameters()));
//     }
//
//     @Override
//     public Instance select(List<Instance> instances) {
//         return null;
//     }
//
//     @Override
//     public String selectServiceAddress(List<String> serviceAddresses, String rpcServiceName) {
//         if (serviceAddresses == null || serviceAddresses.size() == 0) {
//             return null;
//         }
//         if (serviceAddresses.size() == 1) {
//             return serviceAddresses.get(0);
//         }
//         return doSelect(serviceAddresses, rpcServiceName);
//     }
//
//     static class ConsistentHashSelector {
//         private final TreeMap<Long, String> virtualInvokers;
//
//         private final int identityHashCode;
//
//         ConsistentHashSelector(List<String> invokers, int replicaNumber, int identityHashCode) {
//             this.virtualInvokers = new TreeMap<>();
//             this.identityHashCode = identityHashCode;
//
//             for (String invoker : invokers) {
//                 for (int i = 0; i < replicaNumber / 4; i++) {
//                     byte[] digest = md5(invoker + i);
//                     for (int h = 0; h < 4; h++) {
//                         long m = hash(digest, h);
//                         virtualInvokers.put(m, invoker);
//                     }
//                 }
//             }
//         }
//
//         static byte[] md5(String key) {
//             MessageDigest md;
//             try {
//                 md = MessageDigest.getInstance("MD5");
//                 byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
//                 md.update(bytes);
//             } catch (NoSuchAlgorithmException e) {
//                 throw new IllegalStateException(e.getMessage(), e);
//             }
//
//             return md.digest();
//         }
//
//         static long hash(byte[] digest, int idx) {
//             return ((long) (digest[3 + idx * 4] & 255) << 24 | (long) (digest[2 + idx * 4] & 255) << 16 | (long) (digest[1 + idx * 4] & 255) << 8 | (long) (digest[idx * 4] & 255)) & 4294967295L;
//         }
//
//         public String select(String rpcServiceKey) {
//             byte[] digest = md5(rpcServiceKey);
//             return selectForKey(hash(digest, 0));
//         }
//
//         public String selectForKey(long hashCode) {
//             Map.Entry<Long, String> entry = virtualInvokers.tailMap(hashCode, true).firstEntry();
//
//             if (entry == null) {
//                 entry = virtualInvokers.firstEntry();
//             }
//
//             return entry.getValue();
//         }
//     }
// }
