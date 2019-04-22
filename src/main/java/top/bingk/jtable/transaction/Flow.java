package top.bingk.jtable.transaction;

import top.bingk.jtable.transaction.base.BaseFlow;

@SuppressWarnings("serial")
public class Flow extends BaseFlow<Flow> {
	public static final Flow dao = new Flow().dao();
}

