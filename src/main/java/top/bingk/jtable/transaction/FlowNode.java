package top.bingk.jtable.transaction;

import top.bingk.jtable.transaction.base.BaseFlowNode;

@SuppressWarnings("serial")
public class FlowNode extends BaseFlowNode<FlowNode> {
	public static final FlowNode dao = new FlowNode().dao();
}
