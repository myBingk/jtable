package top.bingk.jtable.transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程工具类，节点
 * 
 * @author yujiaxin Create Time 2018年7月2日
 */
public class TransactionNode implements Serializable {

    private static final long serialVersionUID = -6848824409530196591L;

    private String className;

    private String methodName;

    private Map<String, Object> serviceConfig = new HashMap<String, Object>();

    private List<String> parameterTypes = new ArrayList<String>();

    private List<Object> args = new ArrayList<Object>();

    /**
     * @return the serviceConfig
     */
    public Map<String, Object> getServiceConfig() {
        return serviceConfig;
    }

    /**
     * @param serviceConfig the serviceConfig to set
     * @return 单个事物节点
     */
    public TransactionNode setServiceConfig(Map<String, Object> serviceConfig) {
        this.serviceConfig = serviceConfig;
        return this;
    }

    /**
     * @return the parameterTypes
     */
    public List<String> getParameterTypes() {
        return parameterTypes;
    }

    /**
     * @param parameterTypes the parameterTypes to set
     */
    public void setParameterTypes(List<String> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    /**
     * @return the args
     */
    public List<Object> getArgs() {
        return args;
    }

    /**
     * @param args the args to set
     */
    public void setArgs(List<Object> args) {
        this.args = args;
    }

    /**
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public TransactionNode addParameterType(String parameterType) {
        if (null == parameterTypes) {
            throw new NullPointerException(
                "parameterTypes is null,Initialize the parameterTypes before calling the addParameterType method");
        }
        parameterTypes.add(parameterType);
        return this;
    }

    public TransactionNode addArg(Object arg) {
        if (null == args) {
            throw new NullPointerException("args is null ,Initialize the args before calling the addArg method");
        }
        args.add(arg);
        return this;
    }

}
