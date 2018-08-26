package com.egaosoft.jtable.exception;
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
/**
 * @author DoubleCome
 * @date 2018年7月6日 下午3:36:36
 */
public class IllegalAgumentException extends BusinessException {

<<<<<<< Updated upstream
	private static final long serialVersionUID = -1646962659817310752L;

	public IllegalAgumentException(int errorCode) {
		super(errorCode);
	}
	
=======
    private static final long serialVersionUID = -1646962659817310752L;

    public IllegalAgumentException(int errorCode, Exception e) {
        super(errorCode, e);
    }

>>>>>>> Stashed changes
}
