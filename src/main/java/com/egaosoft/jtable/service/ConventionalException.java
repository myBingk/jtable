package com.egaosoft.jtable.service;

/**
 * 常规异常
 * 
 * @author DoubleCome
 * @date 2018年7月6日
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
