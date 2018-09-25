package com.egaosoft.jtable.test.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseBasicDictionary<M extends BaseBasicDictionary<M>> extends Model<M> implements IBean {

    public M setDictionaryId(java.lang.Long dictionaryId) {
        set(getDictionaryIdFieldName(), dictionaryId);
        return (M)this;
    }

    public String getDictionaryIdFieldName() {
        return "dictionaryId";
    }

    public M setDictionaryIdArray(java.lang.Long[] dictionaryIdArray) {
        put(getDictionaryIdFieldName() + "Array", dictionaryIdArray);
        return (M)this;
    }

    public java.lang.Long getDictionaryId() {
        return getLong(getDictionaryIdFieldName());
    }

    public M setSystemId(java.lang.Long systemId) {
        set(getSystemIdFieldName(), systemId);
        return (M)this;
    }

    public String getSystemIdFieldName() {
        return "systemId";
    }

    public M setSystemIdArray(java.lang.Long[] systemIdArray) {
        put(getSystemIdFieldName() + "Array", systemIdArray);
        return (M)this;
    }

    public java.lang.Long getSystemId() {
        return getLong(getSystemIdFieldName());
    }

    public M setName(java.lang.String name) {
        set(getNameFieldName(), name);
        return (M)this;
    }

    public String getNameFieldName() {
        return "name";
    }

    public M setNameArray(java.lang.String[] nameArray) {
        put(getNameFieldName() + "Array", nameArray);
        return (M)this;
    }

    public java.lang.String getName() {
        return getStr(getNameFieldName());
    }

    public M setValue(java.lang.String value) {
        set(getValueFieldName(), value);
        return (M)this;
    }

    public String getValueFieldName() {
        return "value";
    }

    public M setValueArray(java.lang.String[] valueArray) {
        put(getValueFieldName() + "Array", valueArray);
        return (M)this;
    }

    public java.lang.String getValue() {
        return getStr(getValueFieldName());
    }

    public M setCode(java.lang.String code) {
        set(getCodeFieldName(), code);
        return (M)this;
    }

    public String getCodeFieldName() {
        return "code";
    }

    public M setCodeArray(java.lang.String[] codeArray) {
        put(getCodeFieldName() + "Array", codeArray);
        return (M)this;
    }

    public java.lang.String getCode() {
        return getStr(getCodeFieldName());
    }

    public M setDescription(java.lang.String description) {
        set(getDescriptionFieldName(), description);
        return (M)this;
    }

    public String getDescriptionFieldName() {
        return "description";
    }

    public M setDescriptionArray(java.lang.String[] descriptionArray) {
        put(getDescriptionFieldName() + "Array", descriptionArray);
        return (M)this;
    }

    public java.lang.String getDescription() {
        return getStr(getDescriptionFieldName());
    }

    public M setParentId(java.lang.Long parentId) {
        set(getParentIdFieldName(), parentId);
        return (M)this;
    }

    public String getParentIdFieldName() {
        return "parentId";
    }

    public M setParentIdArray(java.lang.Long[] parentIdArray) {
        put(getParentIdFieldName() + "Array", parentIdArray);
        return (M)this;
    }

    public java.lang.Long getParentId() {
        return getLong(getParentIdFieldName());
    }

    public M setOrderBy(java.lang.Integer orderBy) {
        set(getOrderByFieldName(), orderBy);
        return (M)this;
    }

    public String getOrderByFieldName() {
        return "orderBy";
    }

    public M setOrderByArray(java.lang.Integer[] orderByArray) {
        put(getOrderByFieldName() + "Array", orderByArray);
        return (M)this;
    }

    public java.lang.Integer getOrderBy() {
        return getInt(getOrderByFieldName());
    }

    public M setCreationTime(java.util.Date creationTime) {
        set(getCreationTimeFieldName(), creationTime);
        return (M)this;
    }

    public String getCreationTimeFieldName() {
        return "creationTime";
    }

    public M setCreationTimeArray(java.util.Date[] creationTimeArray) {
        put(getCreationTimeFieldName() + "Array", creationTimeArray);
        return (M)this;
    }

    public java.util.Date getCreationTime() {
        return get(getCreationTimeFieldName());
    }

    public M setUpdateTime(java.util.Date updateTime) {
        set(getUpdateTimeFieldName(), updateTime);
        return (M)this;
    }

    public String getUpdateTimeFieldName() {
        return "updateTime";
    }

    public M setUpdateTimeArray(java.util.Date[] updateTimeArray) {
        put(getUpdateTimeFieldName() + "Array", updateTimeArray);
        return (M)this;
    }

    public java.util.Date getUpdateTime() {
        return get(getUpdateTimeFieldName());
    }

}
