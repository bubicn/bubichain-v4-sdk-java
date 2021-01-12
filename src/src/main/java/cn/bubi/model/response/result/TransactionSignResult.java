package cn.bubi.model.response.result;

import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.model.response.result.data.Signature;

/**
 * @Author riven
 * @Date 2018/7/5 16:08
 */
public class TransactionSignResult {
    @JSONField(name = "signatures")
    private Signature[] signatures;

    /**
     * @Author riven
     * @Method getSignatures
     * @Params []
     * @Return Signature[]
     * @Date 2018/7/5 16:09
     */
    public Signature[] getSignatures() {
        return signatures;
    }

    /**
     * @Author riven
     * @Method setSignatures
     * @Params [signatures]
     * @Return void
     * @Date 2018/7/5 16:09
     */
    public void setSignatures(Signature[] signatures) {
        this.signatures = signatures;
    }
}
