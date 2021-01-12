package cn.bubi.model.request.operation;

import cn.bubi.common.OperationType;

/**
 * @Author riven
 * @Date 2018/7/9 16:56
 */
public class GasSendOperation extends BaseOperation {
    private String destAddress;
    private Long amount;

    public GasSendOperation() {
        operationType = OperationType.GAS_SEND;
    }

    /**
     * @Author riven
     * @Method getOperationType
     * @Params []
     * @Return OperationType
     * @Date 2018/7/9 17:15
     */
    @Override
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     * @Author riven
     * @Method getDestAddress
     * @Params []
     * @Return java.lang.String
     * @Date 2018/7/9 16:57
     */
    public String getDestAddress() {
        return destAddress;
    }

    /**
     * @Author riven
     * @Method setDestAddress
     * @Params [destAddress]
     * @Return void
     * @Date 2018/7/9 16:57
     */
    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    /**
     * @Author riven
     * @Method getTokenAmount
     * @Params []
     * @Return java.lang.Long
     * @Date 2018/7/9 16:57
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * @Author riven
     * @Method setTokenAmount
     * @Params [amount]
     * @Return void
     * @Date 2018/7/9 16:57
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
