package com.egaosoft.jtable.transaction.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings({"serial", "unchecked"})
public class BaseFlowUser<M extends BaseFlowUser<M>> extends Model<M> implements IBean {

	public java.lang.Long getUserId(){
		return getLong("userId");
	}
	
	public M setUserId(java.lang.Long userId){
		set("userId", userId);
		return (M)this;
	}
	
	public java.lang.String getUserName(){
		return get("userName");
	}
	
	public M setUserName(java.lang.String userName){
		set("userName", userName);
		return (M)this;
	}
	
	public java.lang.String getPassword(){
		return get("password");
	}
	
	public M setPassword(java.lang.String password){
		set("password", password);
		return (M)this;
	}
	
	public java.util.Date getLoginTime(){
		return getDate("loginTime");
	}
	
	public M setLoginTime(java.util.Date loginTime){
		set("loginTime", loginTime);
		return (M)this;
	}
	
}
