package cn.bubi.model.request;

import cn.bubi.model.request.operation.BaseOperation;

import java.util.Arrays;

/**
 * @Author riven
 * @Date 2018/7/5 15:48
 */
public class TransactionBuildBlobRequest {
    private String sourceAddress;
    private Long nonce;
    private Long gasPrice;
    private Long feeLimit;
    private BaseOperation[] operations;
    private Long ceilLedgerSeq;
    private String metadata;

    /**
     * @Author riven
     * @Method getAddress
     * @Params []
     * @Return java.lang.String
     * @Date 2018/7/5 15:48
     */
    public String getSourceAddress() {
        return sourceAddress;
    }

    /**
     * @Author riven
     * @Method setAddress
     * @Params [sourceAddress]
     * @Return void
     * @Date 2018/7/5 15:49
     */
    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    /**
     * @Author riven
     * @Method getNonce
     * @Params []
     * @Return java.lang.Long
     * @Date 2018/7/5 15:49
     */
    public Long getNonce() {
        return nonce;
    }

    /**
     * @Author riven
     * @Method setNonce
     * @Params [nonce]
     * @Return void
     * @Date 2018/7/5 15:49
     */
    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    /**
     * @Author riven
     * @Method getGasPrice
     * @Params []
     * @Return java.lang.Long
     * @Date 2018/7/5 15:49
     */
    public Long getGasPrice() {
        return gasPrice;
    }

    /**
     * @Author riven
     * @Method setGasPrice
     * @Params [gasPrice]
     * @Return void
     * @Date 2018/7/5 15:49
     */
    public void setGasPrice(Long gasPrice) {
        this.gasPrice = gasPrice;
    }

    /**
     * @Author riven
     * @Method getFeeLimit
     * @Params []
     * @Return java.lang.Long
     * @Date 2018/7/5 15:49
     */
    public Long getFeeLimit() {
        return feeLimit;
    }

    /**
     * @Author riven
     * @Method setFeeLimit
     * @Params [feeLimit]
     * @Return void
     * @Date 2018/7/5 15:49
     */
    public void setFeeLimit(Long feeLimit) {
        this.feeLimit = feeLimit;
    }

    /**
     * @Author riven
     * @Method getOperations
     * @Params []
     * @Return java.lang.String
     * @Date 2018/7/5 15:49
     */
    public BaseOperation[] getOperations() {
        return operations;
    }

    /**
     * @Author riven
     * @Method setOperations
     * @Params [operations]
     * @Return void
     * @Date 2018/7/5 15:49
     */
    public void setOperation(BaseOperation operation) {
        if (null == this.operations) {
            this.operations = new BaseOperation[1];
        } else {
            operations = Arrays.copyOf(operations, 1);
        }
        this.operations[0] = operation;
    }

    /**
     * @Author riven
     * @Method addOperation
     * @Params [operation]
     * @Return void
     * @Date 2018/7/9 23:26
     */
    public void addOperation(BaseOperation operation) {
        if (null == operations) {
            operations = new BaseOperation[1];
        } else {
            operations = Arrays.copyOf(operations, operations.length + 1);
        }
        operations[operations.length - 1] = operation;
    }

    /**
     * @Author riven
     * @Method getCeilLedgerSeq
     * @Params []
     * @Return java.lang.String
     * @Date 2018/7/5 17:23
     */
    public Long getCeilLedgerSeq() {
        return ceilLedgerSeq;
    }

    /**
     * @Author riven
     * @Method setCeilLedgerSeq
     * @Params [ceilLedgerSeq]
     * @Return void
     * @Date 2018/7/5 17:23
     */
    public void setCeilLedgerSeq(Long ceilLedgerSeq) {
        this.ceilLedgerSeq = ceilLedgerSeq;
    }

    /**
     * @Author riven
     * @Method getMetadata
     * @Params []
     * @Return java.lang.String
     * @Date 2018/7/5 15:49
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     * @Author riven
     * @Method setMetadata
     * @Params [metadata]
     * @Return void
     * @Date 2018/7/5 15:49
     */
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
