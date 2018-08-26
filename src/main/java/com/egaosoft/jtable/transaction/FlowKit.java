package com.egaosoft.jtable.transaction;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.dubbo.rpc.service.GenericService;
import com.jfinal.kit.Kv;

import top.yujiaxin.jfinalplugin.dubbo.core.DubboRpc;

/**
 * 流程工具类
 * 
 * @author yujiaxin
 * @date 2018年7月2日
 */
public class FlowKit {

    @SuppressWarnings("unchecked")
    public static void executeTransactionFlow(TransactionNode node) {

        if (node == null)
            return;

        GenericService genericService = DubboRpc.receiveService(GenericService.class,
            Kv.by("interfaceName", node.getClassName()).set("generic", "true").set(node.getServiceConfig()));

        if (node.getArgs().size() != node.getParameterTypes().size()) {
            throw new RuntimeException("参数值和参数长度不匹配");
        }

        /*执行一次数组拷贝操作，防止递归引用*/
        String[] types = node.getParameterTypes().toArray(new String[node.getParameterTypes().size()]);
        types = Arrays.copyOf(types, types.length);
        Object[] args = node.getArgs().toArray(new Object[node.getArgs().size()]);
        args = Arrays.copyOf(args, args.length);

        genericService.$invoke(node.getMethodName(), types, args);
    }

    public static TransactionFlow generateTransactionFlow(String name) {
        TransactionFlow flow = new TransactionFlow();
        List<TransactionNode> nodes = new ArrayList<TransactionNode>();
        flow.setFlowNodes(nodes);
        flow.setName(name);
        return flow;
    }

    public static TransactionNode generateTransactionNode() {
        TransactionNode node = new TransactionNode();
        List<String> parameterTypes = new ArrayList<String>();
        node.setParameterTypes(parameterTypes);
        List<Object> args = new ArrayList<Object>();
        node.setArgs(args);
        return node;
    }

    public static TransactionNode generateTransactionNode(String className, String methodName) {
        TransactionNode node = generateTransactionNode();
        node.setClassName(className);
        node.setMethodName(methodName);
        return node;
    }

    public static String serializeToString(Object obj) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
            objOut.writeObject(obj);
            String str = byteOut.toString("ISO-8859-1");
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
