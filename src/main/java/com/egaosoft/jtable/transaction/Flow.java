package com.egaosoft.jtable.transaction;

import com.egaosoft.jtable.transaction.base.BaseFlow;

@SuppressWarnings("serial")
public class Flow extends BaseFlow<Flow> {
	public static final Flow dao = new Flow().dao();
}

