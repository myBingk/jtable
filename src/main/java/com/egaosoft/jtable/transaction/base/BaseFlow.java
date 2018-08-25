package com.egaosoft.jtable.transaction.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings({"serial", "unchecked"})
public class BaseFlow<M extends BaseFlow<M>> extends Model<M> implements IBean {

	public java.lang.Long getFlowId(){
		return getLong("flowId");
	}
	
	public M setFlowId(java.lang.Long flowId){
		set("flowId", flowId);
		return (M)this;
	}
	
	public java.lang.String getName(){
		return get("name");
	}
	
	public M setName(java.lang.String name){
		set("name", name);
		return (M)this;
	}
	
	public java.lang.String getFlowSerializeString(){
		return get("flowSerializeString");
	}
	
	public M setFlowSerializeString(java.lang.String flowSerializeString){
		set("flowSerializeString", flowSerializeString);
		return (M)this;
	}
	
	public java.lang.Integer getNodeNum(){
		return getInt("nodeNum");
	}
	
	public M setNodeNum(java.lang.Integer nodeNum){
		set("nodeNum", nodeNum);
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
	
	public java.lang.String getRemark(){
		return get("remark");
	}
	
	public M setRemark(java.lang.String remark){
		set("remark", remark);
		return (M)this;
	}
	
}
