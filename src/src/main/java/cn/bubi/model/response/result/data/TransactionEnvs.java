package cn.bubi.model.response.result.data;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Author riven
 * @Date 2018/7/11 20:47
 */
public class TransactionEnvs {
    @JSONField(name = "transaction_env")
    private TransactionEnv transactionEnv;

    /**
     * @Author riven
     * @Method getTxs
     * @Params []
     * @Return TransactionEnv
     * @Date 2018/7/11 20:48
     */
    public TransactionEnv getTransactionEnv() {
        return transactionEnv;
    }

    /**
     * @Author riven
     * @Method setTxs
     * @Params [transactionEnv]
     * @Return void
     * @Date 2018/7/11 22:09
     */
    public void setTransactionEnv(TransactionEnv transactionEnv) {
        this.transactionEnv = transactionEnv;
    }
}
