package com.egaosoft.jtable.test.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseBasicDictionary<M extends BaseBasicDictionary<M>> extends Model<M> implements IBean {

	public M setDictionaryId(java.lang.Long dictionaryId) {
		set("dictionaryId", dictionaryId);
		return (M)this;
	}

	public M setDictionaryIdJudgmentType(com.egaosoft.jtable.config.JudgmentType judgmentType) {
		put(com.egaosoft.jtable.config.JudgmentType.getJudgmentType() + "dictionaryId", judgmentType);
		return (M)this;
	}

	public M setDictionaryIdArray(java.lang.Long[] dictionaryIdArray){
		put("dictionaryIdArray", dictionaryIdArray);
		return(M)this;
	}
	
	public java.lang.Long getDictionaryId() {
		return getLong("dictionaryId");
	}

	public M setSystemId(java.lang.Long systemId) {
		set("systemId", systemId);
		return (M)this;
	}

	public M setSystemIdJudgmentType(com.egaosoft.jtable.config.JudgmentType judgmentType) {
		put(com.egaosoft.jtable.config.JudgmentType.getJudgmentType() + "systemId", judgmentType);
		return (M)this;
	}

	public M setSystemIdArray(java.lang.Long[] systemIdArray){
		put("systemIdArray", systemIdArray);
		return(M)this;
	}
	
	public java.lang.Long getSystemId() {
		return getLong("systemId");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M)this;
	}

	public M setNameJudgmentType(com.egaosoft.jtable.config.JudgmentType judgmentType) {
		put(com.egaosoft.jtable.config.JudgmentType.getJudgmentType() + "name", judgmentType);
		return (M)this;
	}

	public M setNameArray(java.lang.String[] nameArray){
		put("nameArray", nameArray);
		return(M)this;
	}
	
	public java.lang.String getName() {
		return getStr("name");
	}

	public M setValue(java.lang.String value) {
		set("value", value);
		return (M)this;
	}

	public M setValueJudgmentType(com.egaosoft.jtable.config.JudgmentType judgmentType) {
		put(com.egaosoft.jtable.config.JudgmentType.getJudgmentType() + "value", judgmentType);
		return (M)this;
	}

	public M setValueArray(java.lang.String[] valueArray){
		put("valueArray", valueArray);
		return(M)this;
	}
	
	public java.lang.String getValue() {
		return getStr("value");
	}

	public M setCode(java.lang.String code) {
		set("code", code);
		return (M)this;
	}

	public M setCodeJudgmentType(com.egaosoft.jtable.config.JudgmentType judgmentType) {
		put(com.egaosoft.jtable.config.JudgmentType.getJudgmentType() + "code", judgmentType);
		return (M)this;
	}

	public M setCodeArray(java.lang.String[] codeArray){
		put("codeArray", codeArray);
		return(M)this;
	}
	
	public java.lang.String getCode() {
		return getStr("code");
	}

	public M setDescription(java.lang.String description) {
		set("description", description);
		return (M)this;
	}

	public M setDescriptionJudgmentType(com.egaosoft.jtable.config.JudgmentType judgmentType) {
		put(com.egaosoft.jtable.config.JudgmentType.getJudgmentType() + "description", judgmentType);
		return (M)this;
	}

	public M setDescriptionArray(java.lang.String[] descriptionArray){
		put("descriptionArray", descriptionArray);
		return(M)this;
	}
	
	public java.lang.String getDescription() {
		return getStr("description");
	}

	public M setParentId(java.lang.Long parentId) {
		set("parentId", parentId);
		return (M)this;
	}

	public M setParentIdJudgmentType(com.egaosoft.jtable.config.JudgmentType judgmentType) {
		put(com.egaosoft.jtable.config.JudgmentType.getJudgmentType() + "parentId", judgmentType);
		return (M)this;
	}

	public M setParentIdArray(java.lang.Long[] parentIdArray){
		put("parentIdArray", parentIdArray);
		return(M)this;
	}
	
	public java.lang.Long getParentId() {
		return getLong("parentId");
	}

	public M setOrderBy(java.lang.Integer orderBy) {
		set("orderBy", orderBy);
		return (M)this;
	}

	public M setOrderByJudgmentType(com.egaosoft.jtable.config.JudgmentType judgmentType) {
		put(com.egaosoft.jtable.config.JudgmentType.getJudgmentType() + "orderBy", judgmentType);
		return (M)this;
	}

	public M setOrderByArray(java.lang.Integer[] orderByArray){
		put("orderByArray", orderByArray);
		return(M)this;
	}
	
	public java.lang.Integer getOrderBy() {
		return getInt("orderBy");
	}

	public M setCreationTime(java.util.Date creationTime) {
		set("creationTime", creationTime);
		return (M)this;
	}

	public M setCreationTimeJudgmentType(com.egaosoft.jtable.config.JudgmentType judgmentType) {
		put(com.egaosoft.jtable.config.JudgmentType.getJudgmentType() + "creationTime", judgmentType);
		return (M)this;
	}

	public M setCreationTimeArray(java.util.Date[] creationTimeArray){
		put("creationTimeArray", creationTimeArray);
		return(M)this;
	}
	
	public java.util.Date getCreationTime() {
		return get("creationTime");
	}

	public M setUpdateTime(java.util.Date updateTime) {
		set("updateTime", updateTime);
		return (M)this;
	}

	public M setUpdateTimeJudgmentType(com.egaosoft.jtable.config.JudgmentType judgmentType) {
		put(com.egaosoft.jtable.config.JudgmentType.getJudgmentType() + "updateTime", judgmentType);
		return (M)this;
	}

	public M setUpdateTimeArray(java.util.Date[] updateTimeArray){
		put("updateTimeArray", updateTimeArray);
		return(M)this;
	}
	
	public java.util.Date getUpdateTime() {
		return get("updateTime");
	}

}
