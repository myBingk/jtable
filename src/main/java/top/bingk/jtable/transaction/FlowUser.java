package top.bingk.jtable.transaction;

import top.bingk.jtable.transaction.base.BaseFlowUser;


@SuppressWarnings("serial")
public class FlowUser extends BaseFlowUser<FlowUser> {
	public static final FlowUser dao = new FlowUser().dao();
}
