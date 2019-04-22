package top.bingk.jtable.core;

import java.util.ArrayList;
import java.util.List;

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
public class Condition implements java.io.Serializable {

    private static final long serialVersionUID = -4842083479766740544L;

    /**
     * The key of builder
     */
    static final String CONDITION_BUILDER = "conditionBuilder";

    /**
     * if this set true, model ownself's attributes can never effective.
     */
    boolean IS_COVER = true;

    private ConditionBuilder builder;

    Condition() {}

    Condition(ConditionBuilder builder) {
        this.builder = builder;
    }

    public ConditionBuilder getBuilder() {
        return this.builder;
    }

    public static class ConditionBuilder implements java.io.Serializable {

        private static final long serialVersionUID = -1331097267142616814L;

        private top.bingk.jtable.config.Operator term = top.bingk.jtable.config.Operator.AND;
        private top.bingk.jtable.config.Equation equation = top.bingk.jtable.config.Equation.EQUAL;
        private top.bingk.jtable.config.Sort sort = top.bingk.jtable.config.Sort.ASC;
        private int sortLevel = 0;
        private String fieldName;
        private Object value;

        ConditionBuilder() {}

        public <T extends Model> ConditionBuilder(String fieldName) {
            this.fieldName = fieldName;
        }

        public ConditionBuilder equation(top.bingk.jtable.config.Equation equation) {
            this.equation = equation;
            return this;
        }

        public ConditionBuilder term(top.bingk.jtable.config.Operator term) {
            this.term = term;
            return this;
        }

        public ConditionBuilder sort(top.bingk.jtable.config.Sort sort) {
            this.sort = sort;
            this.sortLevel = 1;
            return this;
        }

        public ConditionBuilder value(Object value) {
            this.value = value;
            return this;
        }

        public ConditionBuilder sort(top.bingk.jtable.config.Sort sort, int level) {
            this.sort = sort;
            this.sortLevel = level;
            return this;
        }

        public String getTerm() {
            return this.term.toString();
        }

        public top.bingk.jtable.config.Equation getEquation() {
            return this.equation;
        }

        public String getSort() {
            return this.sort.toString();
        }

        public int getSortLevel() {
            return this.sortLevel;
        }

        public String getFieldName() {
            return this.fieldName;
        }

        public Object getValue() {
            return this.value;
        }

        public Condition build() {
            return new Condition(this);
        }

        public <T extends Model> Condition attachMe(T model) {
            Condition condition = new Condition(this);
            attach(model, condition);
            return condition;
        }

    }

    public static <T extends Model> ConditionBuilder getInstance(String fieldName) {
        ConditionBuilder conditionBuilder = new ConditionBuilder(fieldName);
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
            List<Condition> originalList = (List<Condition>)model.get(Condition.CONDITION_BUILDER);
            originalList.addAll(builderList);
            return originalList;
        }
        return builderList;
    }

}
