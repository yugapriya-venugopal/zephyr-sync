package lv.ctco.zephyr.beans.zapi;

import java.util.List;

public class ZapiExecutionResponse {

    private int totalCount;
    private List<ZapiExecution> executions;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ZapiExecution> getExecutions() {
        return executions;
    }

    public void setExecutions(List<ZapiExecution> executions) {
        this.executions = executions;
    }

}
