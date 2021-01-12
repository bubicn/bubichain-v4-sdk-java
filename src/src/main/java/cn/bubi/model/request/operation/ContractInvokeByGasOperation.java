package cn.bubi.model.request.operation;

import cn.bubi.common.OperationType;

/**
 * @Author riven
 * @Date 2018/7/9 17:20
 */
public class ContractInvokeByGasOperation extends BaseOperation {
    private String contractAddress;
    private Long buAmount;
    private String input;

    public ContractInvokeByGasOperation() {
        operationType = OperationType.CONTRACT_INVOKE_BY_GAS;
    }

    /**
     * @Author riven
     * @Method getOperationType
     * @Params []
     * @Return OperationType
     * @Date 2018/7/9 17:21
     */
    @Override
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     * @Author riven
     * @Method getContractAddress
     * @Params []
     * @Return java.lang.String
     * @Date 2018/7/9 17:21
     */
    public String getContractAddress() {
        return contractAddress;
    }

    /**
     * @Author riven
     * @Method setContractAddress
     * @Params [contractAddress]
     * @Return void
     * @Date 2018/7/9 17:21
     */
    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    /**
     * @Author riven
     * @Method getTokenAmount
     * @Params []
     * @Return java.lang.Long
     * @Date 2018/7/9 17:21
     */
    public Long getBuAmount() {
        return buAmount;
    }

    /**
     * @Author riven
     * @Method setTokenAmount
     * @Params [buAmount]
     * @Return void
     * @Date 2018/7/9 17:21
     */
    public void setBuAmount(Long buAmount) {
        this.buAmount = buAmount;
    }

    /**
     * @Author riven
     * @Method getInput
     * @Params []
     * @Return java.lang.String
     * @Date 2018/7/9 17:21
     */
    public String getInput() {
        return input;
    }

    /**
     * @Author riven
     * @Method setInput
     * @Params [input]
     * @Return void
     * @Date 2018/7/9 17:29
     */
    public void setInput(String input) {
        this.input = input;
    }
}
