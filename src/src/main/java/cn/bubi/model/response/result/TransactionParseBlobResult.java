package cn.bubi.model.response.result;

import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.model.response.result.data.OperationFormat;

/**
 * @Author riven
 * @Date 2018/7/10 16:55
 */
public class TransactionParseBlobResult {
    @JSONField(name = "source_address")
    private String sourceAddress;

    @JSONField(name = "fee_limit")
    private Long feeLimit;

    @JSONField(name = "gas_price")
    private Long gasPrice;

    @JSONField(name = "nonce")
    private Long nonce;

    @JSONField(name = "operations")
    private OperationFormat[] operations;

    @JSONField(name = "chain_id")
    private Long chainId;

    /**
     * @Author riven
     * @Method getAddress
     * @Params []
     * @Return java.lang.String
     * @Date 2018/7/6 00:33
     */
    public String getSourceAddress() {
        return sourceAddress;
    }

    /**
     * @Author riven
     * @Method setAddress
     * @Params [sourceAddress]
     * @Return void
     * @Date 2018/7/6 00:33
     */
    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    /**
     * @Author riven
     * @Method getFeeLimit
     * @Params []
     * @Return java.lang.Long
     * @Date 2018/7/6 00:33
     */
    public Long getFeeLimit() {
        return feeLimit;
    }

    /**
     * @Author riven
     * @Method setFeeLimit
     * @Params [feeLimit]
     * @Return void
     * @Date 2018/7/6 00:33
     */
    public void setFeeLimit(Long feeLimit) {
        this.feeLimit = feeLimit;
    }

    /**
     * @Author riven
     * @Method getGasPrice
     * @Params []
     * @Return java.lang.Long
     * @Date 2018/7/6 00:33
     */
    public Long getGasPrice() {
        return gasPrice;
    }

    /**
     * @Author riven
     * @Method setGasPrice
     * @Params [gasPrice]
     * @Return void
     * @Date 2018/7/6 00:33
     */
    public void setGasPrice(Long gasPrice) {
        this.gasPrice = gasPrice;
    }

    /**
     * @Author riven
     * @Method getNonce
     * @Params []
     * @Return java.lang.Long
     * @Date 2018/7/6 00:33
     */
    public Long getNonce() {
        return nonce;
    }

    /**
     * @Author riven
     * @Method setNonce
     * @Params [nonce]
     * @Return void
     * @Date 2018/7/6 00:33
     */
    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    /**
     * @Author riven
     * @Method getOperations
     * @Params []
     * @Return cn.bubi.model.response.result.data.operation[]
     * @Date 2018/7/6 00:33
     */
    public OperationFormat[] getOperations() {
        return operations;
    }

    /**
     * @Author riven
     * @Method setOperations
     * @Params [operations]
     * @Return void
     * @Date 2018/7/6 00:34
     */
    public void setOperations(OperationFormat[] operations) {
        this.operations = operations;
    }

    /**
     * @Author riven
     * @Method getChainId
     * @Params []
     * @Return java.lang.Long
     * @Date 2018/11/27 15:27
     */
    public Long getChainId() {
        return chainId;
    }

    /**
     * @Author riven
     * @Method setChainId
     * @Params [chainId]
     * @Return void
     * @Date 2018/11/27 15:29
     */
    public void setChainId(Long chainId) {
        this.chainId = chainId;
    }
}
