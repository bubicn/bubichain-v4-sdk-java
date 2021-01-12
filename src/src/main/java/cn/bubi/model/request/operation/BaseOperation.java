package cn.bubi.model.request.operation;

import cn.bubi.common.OperationType;

/**
 * @Author riven
 * @Date 2018/7/9 16:43
 */
public class BaseOperation {
    protected OperationType operationType;
    private String sourceAddress;
    private String metadata;

    /**
     * @Author riven
     * @Method getOperationType
     * @Params []
     * @Return OperationType
     * @Date 2018/7/9 22:25
     */
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     * @Author riven
     * @Method getAddress
     * @Params []
     * @Return java.lang.String
     * @Date 2018/7/9 16:46
     */
    public String getSourceAddress() {
        return sourceAddress;
    }

    /**
     * @Author riven
     * @Method setAddress
     * @Params [sourceAddress]
     * @Return void
     * @Date 2018/7/9 16:46
     */
    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    /**
     * @Author riven
     * @Method getMetadata
     * @Params []
     * @Return java.lang.String
     * @Date 2018/7/9 16:46
     */
    public String getMetadata() {
        return metadata;
    }

    /**
     * @Author riven
     * @Method setMetadata
     * @Params [metadata]
     * @Return void
     * @Date 2018/7/9 16:52
     */
    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
