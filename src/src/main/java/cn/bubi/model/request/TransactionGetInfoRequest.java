package cn.bubi.model.request;

/**
 * @Author riven
 * @Date 2018/7/5 16:59
 */
public class TransactionGetInfoRequest {
    private String hash;

    /**
     * @Author riven
     * @Method getHash
     * @Params []
     * @Return java.lang.String
     * @Date 2018/7/5 16:59
     */
    public String getHash() {
        return hash;
    }

    /**
     * @Author riven
     * @Method setHash
     * @Params [hash]
     * @Return void
     * @Date 2018/7/5 16:59
     */
    public void setHash(String hash) {
        this.hash = hash;
    }
}
