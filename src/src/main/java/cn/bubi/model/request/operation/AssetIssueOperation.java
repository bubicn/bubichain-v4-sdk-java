package cn.bubi.model.request.operation;

import cn.bubi.common.OperationType;

/**
 * @Author riven
 * @Date 2018/7/9 16:53
 */
public class AssetIssueOperation extends BaseOperation {
    private String code;
    private Long amount;

    public AssetIssueOperation() {
        operationType = OperationType.ASSET_ISSUE;
    }

    /**
     * @Author riven
     * @Method getOperationType
     * @Params []
     * @Return OperationType
     * @Date 2018/7/9 17:14
     */
    @Override
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     * @Author riven
     * @Method getCode
     * @Params []
     * @Return java.lang.String
     * @Date 2018/7/9 16:53
     */
    public String getCode() {
        return code;
    }

    /**
     * @Author riven
     * @Method setCode
     * @Params [code]
     * @Return void
     * @Date 2018/7/9 16:54
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @Author riven
     * @Method getTokenAmount
     * @Params []
     * @Return java.lang.Long
     * @Date 2018/7/9 16:54
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * @Author riven
     * @Method setTokenAmount
     * @Params [amount]
     * @Return void
     * @Date 2018/7/9 16:54
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
