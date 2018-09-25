package com.egaosoft.jtable.service;

/**
 * @author DoubleCome
 * @date 2018年7月6日 下午3:36:36
 */
public class IllegalAgumentException extends BusinessException {

    private static final long serialVersionUID = -1646962659817310752L;

    public IllegalAgumentException() {
        super();
    }

    public IllegalAgumentException(int errorCode, Exception e) {
        super(errorCode, e);
    }

}
