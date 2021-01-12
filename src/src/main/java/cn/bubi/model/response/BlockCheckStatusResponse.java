package cn.bubi.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.exception.SdkError;
import cn.bubi.model.response.result.BlockCheckStatusResult;

/**
 * @Author riven
 * @Date 2018/7/6 02:08
 */
public class BlockCheckStatusResponse extends BaseResponse {
    @JSONField(name = "result")
    private BlockCheckStatusResult result;

    /**
     * @Author riven
     * @Method getResult
     * @Params []
     * @Return BlockCheckStatusResult
     * @Date 2018/7/6 02:10
     */
    public BlockCheckStatusResult getResult() {
        return result;
    }

    /**
     * @Author riven
     * @Method setResult
     * @Params [result]
     * @Return void
     * @Date 2018/7/6 02:10
     */
    public void setResult(BlockCheckStatusResult result) {
        this.result = result;
    }

    /**
     * @Author riven
     * @Method buildResponse
     * @Params [sdkError, result]
     * @Return void
     * @Date 2018/7/5 02:08
     */
    public void buildResponse(SdkError sdkError, BlockCheckStatusResult result) {
        this.errorCode = sdkError.getCode();
        this.errorDesc = sdkError.getDescription();
        this.result = result;
    }

    /**
     * @Author riven
     * @Method buildResponse
     * @Params [errorCode, errorDesc, result]
     * @Return void
     * @Date 2018/7/5 02:08
     */
    public void buildResponse(int errorCode, String errorDesc, BlockCheckStatusResult result) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.result = result;
    }
}
