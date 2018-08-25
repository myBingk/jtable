package com.egaosoft.jtable.transaction;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.egaosoft.jtable.exception.BusinessException;
import com.egaosoft.jtable.exception.ConventionalException;
import com.egaosoft.jtable.exception.DatabaseException;
import com.egaosoft.transaction.FlowKit;
import com.egaosoft.transaction.TransactionFlow;
import com.egaosoft.transaction.TransactionNode;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.DbKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;

public class FlowActuator {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FlowActuator.class);

	private static DruidPlugin DRUID_PLUGIN;

	private static ActiveRecordPlugin ACTIVE_RECORD;
	
	private static final Prop MY_PROP = mergeProp("flow.properties");

	static {
		if(MY_PROP != null){
			DRUID_PLUGIN = new DruidPlugin(MY_PROP.get("flow.jdbcUrl"),
					MY_PROP.get("flow.userName"), MY_PROP.get("flow.password"));
			ACTIVE_RECORD = new ActiveRecordPlugin("flowActiveRecord", DRUID_PLUGIN);
		}
	}

	public static TransactionFlow getFlow(Long flowId){
		try{
			initDataSource();
			Flow flow = Flow.dao.findById(flowId);
			if(flow == null){
				throw new DatabaseException(4000);
			}
			List<FlowNode> nodeList = FlowNode.dao.find("SELECT * FROM flow_node WHERE flowId = ? ORDER BY flow_node.index ASC", flow.getFlowId());
			destoryDataSource();
			List<TransactionNode> transactionNodeList = new ArrayList<TransactionNode>();
			for(FlowNode node : nodeList){
				TransactionNode transactionNode = new TransactionNode();
				transactionNode.setClassName(node.getClassName());
				transactionNode.setMethodName(node.getMethodName());
				transactionNodeList.add(transactionNode);
			}
			TransactionFlow transactionFlow = new TransactionFlow(transactionNodeList);
			transactionFlow.setName(flow.getName());
			return transactionFlow;
		}catch(Exception e){
			LOGGER.error("获取流程失败");
			e.printStackTrace();
			throw new DatabaseException(4000);
		}	
	}
	
	public static void excute(TransactionFlow flow)
			throws BusinessException {
		if(flow == null){
			return;
		}
		if(flow.getIndex() == 0){
			LOGGER.info("流程[" + flow.getName() + "]开始执行。");
		}
		if (flow.isFinish()) {
			LOGGER.info("流程[" + flow.getName() + "]执行结束,结束时间:"
					+ System.currentTimeMillis());
			return;
		}else{
			String method = flow.getFlowNodes().get(flow.getIndex()).getClassName() + "." + flow.getFlowNodes().get(flow.getIndex()).getMethodName();
			LOGGER.info("流程[" + flow.getName() + "]-节点[" + flow.getIndex() + "]，方法[" + method + "]执行时间:" + System.currentTimeMillis());
			FlowKit.executeTransactionFlow(flow);
		}
	}

	private static Connection conn;

	public static void initDataSource() throws Exception {
		DRUID_PLUGIN.start();

		DataSource dataSource = DRUID_PLUGIN.getDataSource();
		Connection connection = dataSource.getConnection();
		Statement st = connection.createStatement();
		st.close();
		connection.close();

		ACTIVE_RECORD.setDialect(new MysqlDialect());
		ACTIVE_RECORD.getEngine().setDevMode(true);
		ACTIVE_RECORD.addMapping("flow", "flowId", Flow.class);
		ACTIVE_RECORD.addMapping("flow_node", "nodeId", FlowNode.class);
		ACTIVE_RECORD.start();
		
		conn = DbKit.getConfig().getDataSource().getConnection();
		DbKit.getConfig().setThreadLocalConnection(conn);
	}

	public static void destoryDataSource() throws Exception {
		ACTIVE_RECORD.stop();
		conn.close();
	}

	public static String serializeToString(Object obj) throws BusinessException {
		try{
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
			objOut.writeObject(obj);
			String str = byteOut.toString("ISO-8859-1");
			return str;
		}catch(Exception e){
			LOGGER.error("序列化对象失败");
			e.printStackTrace();
			throw new ConventionalException(4002);
		}
		
	}

	public static Object deserializeToFlow(String str) throws BusinessException {
		if(str == null || str.equals("")){
			return null;
		}
		try{	
			ByteArrayInputStream byteIn = new ByteArrayInputStream(
					str.getBytes("ISO-8859-1"));
			ObjectInputStream objIn = new ObjectInputStream(byteIn);
			Object obj = objIn.readObject();
			return obj;
		}catch(Exception e){
			LOGGER.error("序列化对象失败");
			e.printStackTrace();
			throw new ConventionalException(4001);
		}
	}

	public static Prop mergeProp(String... propString) {
		Prop result = null;
		List<Prop> propListWithoutNull = new ArrayList<Prop>();
		for (String prop : propString) {
			try{
				if (PropKit.use(prop) != null) {
					propListWithoutNull.add(PropKit.use(prop));
				}
			}catch(Exception e){
				LOGGER.warn("属性对象不存在");
				e.printStackTrace();
				continue;
			}
		}
		switch (propListWithoutNull.size()) {
		case 0:
			return result;
		case 1:
			result = propListWithoutNull.get(0);
			break;
		default:
			result = propListWithoutNull.get(propListWithoutNull.size() - 1);
			for (int i = propListWithoutNull.size() - 2; i >= 0; i--) {
				Properties temp = propListWithoutNull.get(i).getProperties();
				Set<Object> keySet = temp.keySet();
				for (Object key : keySet) {
					if (StrKit.isBlank(result.get(key.toString()))) {
						result.getProperties().setProperty(key.toString(),
								temp.getProperty(key.toString()));
					}
				}
			}
			break;
		}
		return result;
	}

}
