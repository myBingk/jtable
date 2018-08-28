package com.egaosoft.jtable.dubbo.filter;

import java.lang.reflect.Method;
import java.util.List;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.jfinal.plugin.activerecord.Model;

/**
 * @author DoubleCome
 * @date 2018年7月12日 上午10:46:28
 */
public class ServiceFilter implements Filter {

    private static Long systemId = null;
    private static String systemIdString = null;

    @Override
    @SuppressWarnings("rawtypes")
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (systemId != null) {
            Object[] args = invocation.getArguments();
            for (Object arg : args) {
                if (arg == null) {
                    continue;
                }
                if (arg.getClass().isArray()) {
                    for (Object subArg : (Object[])arg) {
                        if (subArg == null) {
                            continue;
                        }
                        if (subArg instanceof Model) {
                            if (hasSystemIdField(arg)) {
                                ((Model)subArg).put("systemId", systemId);
                            }
                        }
                    }
                }
                if (arg instanceof List) {
                    for (Object subArg : (List)arg) {
                        if (subArg == null) {
                            continue;
                        }
                        if (subArg instanceof Model) {
                            if (hasSystemIdField(subArg)) {
                                ((Model)subArg).put("systemId", systemId);
                            }
                        }
                    }
                }
                if (arg instanceof Model) {
                    if (hasSystemIdField(arg)) {
                        ((Model)arg).put("systemId", systemId);
                    }
                }
            }
            RpcContext.getContext().setAttachment("systemId", systemIdString);
        }
        Result result = invoker.invoke(invocation);
        return result;
    }

    public static boolean hasSystemIdField(Object obj) {
        for (Method method : obj.getClass().getMethods()) {
            if (method.getName().equals("setSystemId")) {
                return true;
            }
        }
        return false;
    }

    public static void setSystemId(Long setSystemId) {
        if (systemId == null) {
            systemId = setSystemId;
            systemIdString = String.valueOf(systemId);
        }
    }

}
