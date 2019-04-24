package top.bingk.jtable.config;

/**
 * &lt;p&gt; 此类用于和model对象配合使用，用于条件判断 ;LEFT_LIKE:%&iexcl;， RIGHT_LIKE:&iexcl;%， ALL_LIKE:%&iexcl;%， EQUAL:=&iexcl;，
 * LESS_THAN:&lt;=， MORE_THAN:&gt;=， LESS:&lt;， MORE:&gt;； &lt;p&gt;
 * 
 * @author DoubleCome
 * Create Time 2018/09/25
 */
public enum Equation {

    /**
     * 左匹配对象，值为%&iexcl;
     */
    LEFT_LIKE,

    /**
     * 右边匹配对象，值为&iexcl;%
     */
    RIGHT_LIKE,

    /**
     * 全匹配对象，值为%&iexcl;%
     */
    ALL_LIKE,

    /**
     * 等于，值为=&iexcl;
     */
    EQUAL,

    /**
     * 小于等于，值为&lt;=
     */
    LESS_THAN,

    /**
     * 大于等于，值为&gt;=
     */
    MORE_THAN,

    /**
     * 小于，值为&lt;
     */
    LESS,

    /**
     * 大于，值为&gt;
     */
    MORE;

}
