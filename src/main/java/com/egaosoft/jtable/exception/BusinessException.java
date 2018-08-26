package com.egaosoft.jtable.exception;

import java.sql.SQLException;

import com.jfinal.plugin.activerecord.ActiveRecordException;

/**
 * @author DoubleCome
 * @date 2018年7月6日 下午2:32:39
 */
public abstract class BusinessException extends RuntimeException {

<<<<<<< Updated upstream
	private static final long serialVersionUID = 465048467474742009L;

	private int errorCode = 1000;
	
	public BusinessException(){
		super();
	}
	
	public BusinessException(String message){
		super(message);
	}
	
	public BusinessException(String message,Throwable e){
		super(message, e);
	}
	
	public BusinessException(Throwable e){
		super(e);
	}
	
	public BusinessException(int errorCode){
		super(transformCode(errorCode) + "，状态码：[" + errorCode + "]");
		this.printStackTrace();
		this.errorCode = errorCode;
	}
	
	public BusinessException(ActiveRecordException e){
		this(getCode(e));
	}
	
	public BusinessException(SQLException e){
		this(e.getErrorCode());
	}
	
	public int getCode(){
		return this.errorCode;
	}
	
	private static String transformCode(int errorCode){
		switch(errorCode){
			case 1000:
				return "数据库操作失败";
			case 1094:
				return "主键违规";	
			case 1098:
				return "对象已存在";	
			case 2102:
				return "对象添加失败";	
			case 2106:
				return "附加对象添加失败";
			case 2110:
				return "对象删除失败"; 
			case 2114:
				return "附加对象删除失败";
			case 2118:
				return "对象修改失败"; 
			case 2122:
				return "附加对象修改失败";	
			case 2126:
				return "主键为空";	
			case 2130:
				return "页码错误";	
			case 2134:
				return "列未找到";	
			case 10094:
				return "查询字段不存在";
			case 10098:
				return "排序字段必须为DESC或ASC";	
			case 10102:
				return "排序字段不存在";	
			case 10106:
				return "主键长度与查询条件长度不匹配";
			case 2000:
				return "网络异常";	
			case 4000:
				return "流程执行失败";	
			case 4001:
				return "反序列化对象失败";	
			case 4002:
				return "序列化对象失败";	
			default:
				return "数据库操作失败";
		}
	}
	
	public static int getCode(ActiveRecordException e){
		if(e.getCause() == null || e.getMessage() == null){
			return 1000;
		}
		String message = e.getMessage();
		if(message.contains("Unique index or primary key violation")){
			return 1094;
		}
		if(message.contains("without Primary Key")){
			return 2126;
		}
		if(message.contains("pageNumber and pageSize must more than 0")){
			return 2130;
		}
		if(message.contains("not found")){
			return 2134;
		}
		return 1000;
	}
=======
    private static final long serialVersionUID = 465048467474742009L;

    private int errorCode = 1000;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable e) {
        super(message, e);
    }

    public BusinessException(Throwable e) {
        super(e);
    }

    public BusinessException(int errorCode, Exception e) {
        super(transformCode(errorCode) + "，状态码：[" + errorCode + "]", e);
        this.printStackTrace();
        this.errorCode = errorCode;
    }

    public BusinessException(ActiveRecordException e) {
        this(getCode(e), e);
    }

    public BusinessException(SQLException e) {
        this(e.getErrorCode(), e);
    }

    public int getCode() {
        return this.errorCode;
    }

    private static String transformCode(int errorCode) {
        switch (errorCode) {
            case 1000:
                return "数据库操作失败";
            case 1094:
                return "主键违规";
            case 1098:
                return "对象已存在";
            case 2102:
                return "对象添加失败";
            case 2106:
                return "附加对象添加失败";
            case 2110:
                return "对象删除失败";
            case 2114:
                return "附加对象删除失败";
            case 2118:
                return "对象修改失败";
            case 2122:
                return "附加对象修改失败";
            case 2126:
                return "主键为空";
            case 2130:
                return "页码错误";
            case 2134:
                return "列未找到";
            case 10094:
                return "查询字段不存在";
            case 10098:
                return "排序字段必须为DESC或ASC";
            case 10102:
                return "排序字段不存在";
            case 10106:
                return "主键长度与查询条件长度不匹配";
            case 2000:
                return "网络异常";
            case 4000:
                return "流程执行失败";
            case 4001:
                return "反序列化对象失败";
            case 4002:
                return "序列化对象失败";
            default:
                return "数据库操作失败";
        }
    }

    public static int getCode(ActiveRecordException e) {
        if (e.getCause() == null || e.getMessage() == null) {
            return 1000;
        }
        String message = e.getMessage();
        if (message.contains("Unique index or primary key violation")) {
            return 1094;
        }
        if (message.contains("without Primary Key")) {
            return 2126;
        }
        if (message.contains("pageNumber and pageSize must more than 0")) {
            return 2130;
        }
        if (message.contains("not found")) {
            return 2134;
        }
        return 1000;
    }
>>>>>>> Stashed changes
}
