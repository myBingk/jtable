package com.egaosoft.jtable.exception;

/**
 * dubbo服务异常捕获
 * 
 * @author DoubleCome
 * @date 2018年7月9日 下午1:36:25
 */
public class DubboMockException extends BusinessException {

	private static final long serialVersionUID = -7863457725477476912L;
	
	public DubboMockException(int errorCode){
		super(errorCode);
	}

}
