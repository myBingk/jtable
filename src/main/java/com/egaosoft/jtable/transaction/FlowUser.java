package com.egaosoft.jtable.transaction;

import com.egaosoft.jtable.transaction.base.BaseFlowUser;


@SuppressWarnings("serial")
public class FlowUser extends BaseFlowUser<FlowUser> {
	public static final FlowUser dao = new FlowUser().dao();
}
