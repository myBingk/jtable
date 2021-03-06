package top.bingk.jtable.service;

/**
 * dubbo服务异常捕获
 * 
 * @author DoubleCome
 * Create Time 2018年7月9日 下午1:36:25
 */
public class DubboMockException extends BusinessException {

    private static final long serialVersionUID = -7863457725477476912L;

    public DubboMockException() {
        super();
    }

    public DubboMockException(int errorCode, Exception e) {
        super(errorCode, e);
    }

}
