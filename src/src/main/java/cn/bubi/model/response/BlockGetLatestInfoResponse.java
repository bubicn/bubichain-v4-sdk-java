package cn.bubi.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.exception.SdkError;
import cn.bubi.model.response.result.BlockGetLatestInfoResult;

/**
 * @Author riven
 * @Date 2018/7/12 00:36
 */
public class BlockGetLatestInfoResponse extends BaseResponse {
    @JSONField(name = "result")
    private BlockGetLatestInfoResult result;

    /**
     * @Author riven
     * @Method getResult
     * @Params []
     * @Return BlockGetFeesResult
     * @Date 2018/7/4 15:08
     */
    public BlockGetLatestInfoResult getResult() {
        return result;
    }

    /**
     * @Author riven
     * @Method setResult
     * @Params [result]
     * @Return void
     * @Date 2018/7/4 15:08
     */
    public void setResult(BlockGetLatestInfoResult result) {
        this.result = result;
    }

    /**
     * @Author riven
     * @Method buildResponse
     * @Params [sdkError, result]
     * @Return void
     * @Date 2018/7/4 15:08
     */
    public void buildResponse(SdkError sdkError, BlockGetLatestInfoResult result) {
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
    public void buildResponse(int errorCode, String errorDesc, BlockGetLatestInfoResult result) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.result = result;
    }
}
