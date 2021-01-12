package cn.bubi.model.response.result;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.model.response.result.data.ContractStat;
import cn.bubi.model.response.result.data.TransactionEnvs;

/**
 * @Author riven
 * @Date 2018/7/11 18:16
 */
public class ContractCallResult<T> {
    @JSONField(name = "logs")
    private JSONObject logs;

    @JSONField(name = "query_rets")
    private JSONArray queryRets;

    @JSONField(name = "stat")
    private ContractStat stat;

    @JSONField(name = "txs")
    private TransactionEnvs[] txs;

    /**
     * @Author riven
     * @Method getLogs
     * @Params []
     * @Return java.lang.Object
     * @Date 2018/7/11 18:47
     */
    public JSONObject getLogs() {
        return logs;
    }

    /**
     * @Author riven
     * @Method setLogs
     * @Params [logs]
     * @Return void
     * @Date 2018/7/11 18:47
     */
    public void setLogs(JSONObject logs) {
        this.logs = logs;
    }

    /**
     * @Author riven
     * @Method getQueryRets
     * @Params []
     * @Return java.lang.Object[]
     * @Date 2018/7/11 18:47
     */
    public JSONArray getQueryRets() {
        return queryRets;
    }

    /**
     * @Author riven
     * @Method setQueryRets
     * @Params [queryRets]
     * @Return void
     * @Date 2018/7/11 18:47
     */
    public void setQueryRets(JSONArray queryRets) {
        this.queryRets = queryRets;
    }

    /**
     * @Author riven
     * @Method getStat
     * @Params []
     * @Return ContractStat
     * @Date 2018/7/11 18:47
     */
    public ContractStat getStat() {
        return stat;
    }

    /**
     * @Author riven
     * @Method setStat
     * @Params [stat]
     * @Return void
     * @Date 2018/7/11 18:47
     */
    public void setStat(ContractStat stat) {
        this.stat = stat;
    }

    /**
     * @Author riven
     * @Method getTxs
     * @Params []
     * @Return TransactionEnv[]
     * @Date 2018/7/11 18:47
     */
    public TransactionEnvs[] getTxs() {
        return txs;
    }

    /**
     * @Author riven
     * @Method setTxs
     * @Params [txs]
     * @Return void
     * @Date 2018/7/11 18:48
     */
    public void setTxs(TransactionEnvs[] txs) {
        this.txs = txs;
    }
}
