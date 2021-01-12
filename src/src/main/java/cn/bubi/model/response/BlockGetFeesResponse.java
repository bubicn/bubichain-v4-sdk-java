package cn.bubi.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.exception.SdkError;
import cn.bubi.model.response.result.BlockGetFeesResult;

/**
 * @Author riven
 * @Date 2018/7/4 09:55
 */
public class BlockGetFeesResponse extends BaseResponse {
    @JSONField(name = "result")
    private BlockGetFeesResult result;

    /**
     * @Author riven
     * @Method getResult
     * @Params []
     * @Return BlockGetFeesResult
     * @Date 2018/7/4 15:08
     */
    public BlockGetFeesResult getResult() {
        return result;
    }

    /**
     * @Author riven
     * @Method setResult
     * @Params [result]
     * @Return void
     * @Date 2018/7/4 15:08
     */
    public void setResult(BlockGetFeesResult result) {
        this.result = result;
    }

    /**
     * @Author riven
     * @Method buildResponse
     * @Params [sdkError, result]
     * @Return void
     * @Date 2018/7/4 15:08
     */
    public void buildResponse(SdkError sdkError, BlockGetFeesResult result) {
        this.errorCode = sdkError.getCode();
        this.errorDesc = sdkError.getDescription();
        this.result = result;
    }

    /**
     * @Author riven
     * @Method buildResponse
     * @Params [errorCode, errorDesc, result]
     * @Return void
     * @Date 2018/7/4 15:10
     */
    public void buildResponse(int errorCode, String errorDesc, BlockGetFeesResult result) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.result = result;
    }
}
