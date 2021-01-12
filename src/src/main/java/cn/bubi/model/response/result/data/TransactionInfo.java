package cn.bubi.model.response.result.data;

import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.encryption.utils.hex.HexFormat;

/**
 * @Author riven
 * @Date 2018/7/5 16:20
 */
public class TransactionInfo {
    @JSONField(name = "source_address")
    private String sourceAddress;

    @JSONField(name = "fee_limit")
    private Long feeLimit;

    @JSONField(name = "gas_price")
    private Long gasPrice;

    @JSONField(name = "nonce")
    private Long nonce;

    @JSONField(name = "metadata")
    private String metadata;

    @JSONField(name = "operations")
    private Operation[] operations;

    @JSONField(name = "chain_id")
    private Long chainId;

    /**
     * @Author riven
     * @Method getMetadata
     * @Params []
     * @Return java.lang.String
     * @Date 2018/7/24 12:00
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     * @Author riven
     * @Method setMetadata
     * @Params [metadata]
     * @Return void
     * @Date 2018/7/24 12:00
     */
    public void setMetadata(String metadata) {
        this.metadata = new String(HexFormat.hexToByte(metadata));
    }

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
    public Operation[] getOperations() {
        return operations;
    }

    /**
     * @Author riven
     * @Method setOperations
     * @Params [operations]
     * @Return void
     * @Date 2018/7/6 00:34
     */
    public void setOperations(Operation[] operations) {
        this.operations = operations;
    }

    /**
     * @Author riven
     * @Method getChainId
     * @Params []
     * @Return java.lang.Long
     * @Date 2018/11/27 15:25
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
