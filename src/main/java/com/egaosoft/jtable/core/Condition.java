package com.egaosoft.jtable.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.jfinal.plugin.activerecord.Model;

/**
 * Attach condition. Examples:Condition.getInstance(BasicDictionary::getNameFieldName).sort(Sort.DESC,
 * 10).equation(Equation.ALL_LIKE).value("1").attachMe(condition);
 *
 * List<Condition> conditionList = new ArrayList<Condition>();
 * conditionList.add(Condition.getInstance(BasicDictionary::getNameFieldName).sort(Sort.DESC, 10)
 * .equation(Equation.ALL_LIKE).value("1").build());
 *
 * conditionList.add(Condition.getInstance(BasicDictionary::getCreationTimeFieldName).sort(Sort.DESC, 10)
 * .equation(Equation.EQUAL).value(new Date()).build());
 *
 * Condition.attach(condition, conditionList);
 * 
 * @author Kangkang Zhang
 * @date 2018年9月25日
 */
@SuppressWarnings("rawtypes")
public class Condition {

    /**
     * The key of builder
     */
    static final String CONDITION_BUILDER = "conditionBuilder";

    /**
     * if this set true, model ownself's attributes can never effective.
     */
    boolean IS_COVER = true;

    private ConditionBuilder builder;

    Condition(ConditionBuilder builder) {
        this.builder = builder;
    }

    public ConditionBuilder getBuilder() {
        return this.builder;
    }

    public static class ConditionBuilder {

        private com.egaosoft.jtable.config.Operator term = com.egaosoft.jtable.config.Operator.AND;
        private com.egaosoft.jtable.config.Equation equation = com.egaosoft.jtable.config.Equation.EQUAL;
        private com.egaosoft.jtable.config.Sort sort = com.egaosoft.jtable.config.Sort.ASC;
        private int sortLevel = 0;
        private Function<? extends Model, String> mapper;
        private Object value;

        ConditionBuilder() {}

        public <T extends Model> ConditionBuilder(Function<T, String> mapper) {
            this.mapper = mapper;
        }

        public ConditionBuilder equation(com.egaosoft.jtable.config.Equation equation) {
            this.equation = equation;
            return this;
        }

        public ConditionBuilder term(com.egaosoft.jtable.config.Operator term) {
            this.term = term;
            return this;
        }

        public ConditionBuilder sort(com.egaosoft.jtable.config.Sort sort) {
            this.sort = sort;
            this.sortLevel = 1;
            return this;
        }

        public ConditionBuilder value(Object value) {
            this.value = value;
            return this;
        }

        public ConditionBuilder sort(com.egaosoft.jtable.config.Sort sort, int level) {
            this.sort = sort;
            this.sortLevel = level;
            return this;
        }

        public String getTerm() {
            return this.term.toString();
        }

        public com.egaosoft.jtable.config.Equation getEquation() {
            return this.equation;
        }

        public String getSort() {
            return this.sort.toString();
        }

        public int getSortLevel() {
            return this.sortLevel;
        }

        @SuppressWarnings("unchecked")
        public <T extends Model> Function<T, String> getMapper() {
            return (Function<T, String>)this.mapper;
        }

        public Object getValue() {
            return this.value;
        }

        public Condition build() {
            return new Condition(this);
        }

        public <T extends Model> T attachMe(T model) {
            return attach(model, new Condition(this));
        }

    }

    public static <T extends Model> ConditionBuilder getInstance(Function<? extends T, String> mapper) {
        ConditionBuilder conditionBuilder = new ConditionBuilder(mapper);
        return conditionBuilder;
    }

    public static <T extends Model> T attach(T model, Condition condition) {
        List<Condition> originalList = new ArrayList<Condition>();
        originalList.add(condition);
        List<Condition> finalList = accumulate(model, originalList);
        model.put(CONDITION_BUILDER, finalList);
        return model;
    }

    public static <T extends Model> T attachCover(T model, Condition builder) {
        List<Condition> originalList = new ArrayList<Condition>();
        originalList.add(builder);
        model.put(CONDITION_BUILDER, originalList);
        return model;
    }

    public static <T extends Model> T attach(T model, List<Condition> builderList) {
        List<Condition> finalList = accumulate(model, builderList);
        model.put(CONDITION_BUILDER, finalList);
        return model;
    }

    public static <T extends Model> T attachCover(T model, List<Condition> builderList) {
        model.put(CONDITION_BUILDER, builderList);
        return model;
    }

    public Condition neverCover() {
        this.IS_COVER = false;
        return this;
    }

    boolean getIsCover() {
        return this.IS_COVER;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Model> List<Condition> accumulate(T model, List<Condition> builderList) {
        if (model.get(CONDITION_BUILDER) != null) {
            Object original = model.get(CONDITION_BUILDER);
            List<Condition> originalList = new ArrayList<Condition>();
            if (original instanceof Condition) {
                originalList.add((Condition)original);
                originalList.addAll(builderList);
                return originalList;
            }
            if (original instanceof List) {
                originalList = (List<Condition>)original;
                originalList.addAll(builderList);
                return originalList;
            }
        }
        return builderList;
    }

}
