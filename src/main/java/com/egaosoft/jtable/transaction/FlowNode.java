package com.egaosoft.jtable.transaction;

import com.egaosoft.jtable.transaction.base.BaseFlowNode;

@SuppressWarnings("serial")
public class FlowNode extends BaseFlowNode<FlowNode> {
	public static final FlowNode dao = new FlowNode().dao();
}
