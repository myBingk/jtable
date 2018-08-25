package com.egaosoft.jtable.exception;
/**
 * @author DoubleCome
 * @date 2018年7月6日 下午3:36:36
 */
public class IllegalAgumentException extends BusinessException {

	private static final long serialVersionUID = -1646962659817310752L;

	public IllegalAgumentException(int errorCode) {
		super(errorCode);
	}
	
}
