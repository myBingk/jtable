package top.bingk.jtable.service;

/**
 * 常规异常
 * 
 * @author DoubleCome
 * Create Time 2018年7月6日
 */
public class ConventionalException extends BusinessException {

    private static final long serialVersionUID = -1646962659817310752L;

    public ConventionalException() {
        super();
    }

    public ConventionalException(int errorCode, Exception e) {
        super(errorCode, e);
    }

}
