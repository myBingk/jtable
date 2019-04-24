package top.bingk.jtable.transaction;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程工具类，流程
 * 
 * @author yujiaxin Create Time 2018年7月2日
 */
public class TransactionFlow implements Serializable {

    private static final long serialVersionUID = 2661842397410222856L;

    private List<TransactionNode> flowNodes;

    private int index = 0;

    private String name;

    private Map<TransactionNode, Object> invokeResults = new HashMap<TransactionNode, Object>();

    private Object previousResult;

    public TransactionFlow() {}

    public TransactionFlow(List<TransactionNode> nodes) {
        flowNodes = nodes;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return the previousResult
     */
    public Object getPreviousResult() {
        return previousResult;
    }

    /**
     * @param previousResult the previousResult to set
     */
    public void setPreviousResult(Object previousResult) {
        this.previousResult = previousResult;
    }

    /**
     * @return the invokeResults
     */
    public Map<TransactionNode, Object> getInvokeResults() {
        return invokeResults;
    }

    /**
     * @param invokeResults the invokeResults to set
     */
    public void setInvokeResults(Map<TransactionNode, Object> invokeResults) {
        this.invokeResults = invokeResults;
    }

    /**
     * @return the flowNodes
     */
    public List<TransactionNode> getFlowNodes() {
        return flowNodes;
    }

    /**
     * @param flowNodes the flowNodes to set
     */
    public void setFlowNodes(List<TransactionNode> flowNodes) {
        this.flowNodes = flowNodes;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Before calling {@link TransactionFlow#nextNode()}, you need to call this method to check if there are any
     * transaction nodes that need to be executed. If the return value is false, there is no node to execute, and you
     * cannot call the getNode method.
     * 
     * @return 结束标识
     */
    public boolean isFinish() {
        if (index >= flowNodes.size()) {
            return true;
        } else {
            return false;
        }
    }

    public TransactionNode nextNode() {
        TransactionNode node = flowNodes.get(index);
        index++;
        return node;
    }

    public void addNode(TransactionNode node) {
        if (null == flowNodes) {
            throw new NullPointerException(
                "flowNodes is null,Initialize the flowNodes before calling the addNode method");
        }
        flowNodes.add(node);
    }

}
