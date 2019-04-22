package top.bingk.jtable.config;

/**
 * 此类用于和model对象配合使用，用于条件判断 ;LEFT_LIKE->'%?', RIGHT_LIKE->'?%', ALL_LIKE->'%?%', EQUAL->'=?', LESS_THAN->'<=',
 * MORE_THAN->'>=', LESS->'<', MORE->'>';
 * 
 * @author DoubleCome
 * @date 2018年7月4日 下午3:53:31
 */
public enum Equation {

    /**
     * 左匹配对象，值为'%?'
     */
    LEFT_LIKE,

    /**
     * 右边匹配对象，值为'?%'
     */
    RIGHT_LIKE,

    /**
     * 全匹配对象，值为'%?%'
     */
    ALL_LIKE,

    /**
     * 等于，值为'=?'
     */
    EQUAL,

    /**
     * 小于等于，值为'<='
     */
    LESS_THAN,

    /**
     * 大于等于，值为'>='
     */
    MORE_THAN,

    /**
     * 小于，值为'<'
     */
    LESS,

    /**
     * 大于，值为'>'
     */
    MORE;

}
