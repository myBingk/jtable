package com.egaosoft.jtable.config;

/**
 * 此类用于和model对象配合使用，用于条件判断
 * 
 * @author DoubleCome
 * @date 2018年7月4日 下午3:53:31
 */
public enum JudgmentType {

	LEFT_LIKE, RIGHT_LIKE, ALL_LIKE, EQUAL, LESS_THAN, MORE_THAN, LENGTH;

	static final String JUDGMENT_TYPE = "judgmentType_";

	public static String getJudgmentType() {
		return JUDGMENT_TYPE;
	}

}
