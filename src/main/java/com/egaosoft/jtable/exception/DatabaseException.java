package com.egaosoft.jtable.exception;

import java.sql.SQLException;

import com.jfinal.plugin.activerecord.ActiveRecordException;

<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
/**
 * 业务异常
 * 
 * @author DoubleCome
 * @date 2018年7月6日 下午2:12:54
 */
public class DatabaseException extends BusinessException {

<<<<<<< Updated upstream
	private static final long serialVersionUID = -1543126965117436740L;
	
	public DatabaseException(int errorCode){
		super(errorCode);
	}
	
	public DatabaseException(ActiveRecordException e){
		super(e);
	}
	
	public DatabaseException(SQLException e){
		super(e);
	}
=======
    private static final long serialVersionUID = -1543126965117436740L;

    public DatabaseException(int errorCode, Exception e) {
        super(errorCode, e);
    }

    public DatabaseException(ActiveRecordException e) {
        super(e);
    }

    public DatabaseException(SQLException e) {
        super(e);
    }
>>>>>>> Stashed changes

}
