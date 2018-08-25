package com.egaosoft.jtable.transaction.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings({"serial", "unchecked"})
public class BaseFlowNode<M extends BaseFlowNode<M>> extends Model<M> implements IBean {

	public java.lang.Long getNodeId(){
		return getLong("nodeId");
	}
	
	public M setNodeId(java.lang.Long nodeId){
		set("nodeId", nodeId);
		return (M)this;
	}
	
	public java.lang.Long getFlowId(){
		return getLong("flowId");
	}
	
	public M setFlowId(java.lang.Long flowId){
		set("flowId", flowId);
		return (M)this;
	}
	
	public java.lang.String getClassName(){
		return get("className");
	}
	
	public M setClassName(java.lang.String className){
		set("className", className);
		return (M)this;
	}
	
	public java.lang.String getMethodName(){
		return get("methodName");
	}
	
	public M setMethodName(java.lang.String methodName){
		set("methodName", methodName);
		return (M)this;
	}
	
	public java.lang.Integer getIndex(){
		return getInt("index");
	}
	
	public M setIndex(java.lang.Integer index){
		set("index", index);
		return (M)this;
	}
	
	public java.lang.String getParameters(){
		return get("parameters");
	}
	
	public M setParameters(java.lang.String parameters){
		set("parameters", parameters);
		return (M)this;
	}
	
	public java.lang.String getParameterTypes(){
		return get("parameterTypes");
	}
	
	public M setParameterTypes(java.lang.String parameterTypes){
		set("parameterTypes", parameterTypes);
		return (M)this;
	}
	
	public java.util.Date getCreationTime(){
		return getDate("creationTime");
	}
	
	public M setCreationTime(java.util.Date creationTime){
		set("creationTime", creationTime);
		return (M)this;
	}
	
	public java.util.Date getUpdateTime(){
		return getDate("updateTime");
	}
	
	public M setUpdateTime(java.util.Date updateTime){
		set("updateTime", updateTime);
		return (M)this;
	}
	
}
