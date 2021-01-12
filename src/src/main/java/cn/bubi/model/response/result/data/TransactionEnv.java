package cn.bubi.model.response.result.data;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @Author riven
 * @Date 2018/7/11 18:41
 */
public class TransactionEnv {
    @JSONField(name = "transaction")
    private TransactionInfo transaction;

    @JSONField(name = "trigger")
    private ContractTrigger trigger;

    /**
     * @Author riven
     * @Method getTransaction
     * @Params []
     * @Return TransactionInfo
     * @Date 2018/7/11 18:46
     */
    public TransactionInfo getTransaction() {
        return transaction;
    }

    /**
     * @Author riven
     * @Method setTransaction
     * @Params [transaction]
     * @Return void
     * @Date 2018/7/11 18:46
     */
    public void setTransaction(TransactionInfo transaction) {
        this.transaction = transaction;
    }

    /**
     * @Author riven
     * @Method getTrigger
     * @Params []
     * @Return ContractTrigger
     * @Date 2018/7/11 18:46
     */
    public ContractTrigger getTrigger() {
        return trigger;
    }

    /**
     * @Author riven
     * @Method setTrigger
     * @Params [trigger]
     * @Return void
     * @Date 2018/7/11 18:46
     */
    public void setTrigger(ContractTrigger trigger) {
        this.trigger = trigger;
    }
}
