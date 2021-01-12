package cn.bubi.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.exception.SdkError;
import cn.bubi.model.response.result.BlockGetLatestValidatorsResult;

/**
 * @Author riven
 * @Date 2018/7/12 01:08
 */
public class BlockGetLatestValidatorsResponse extends BaseResponse {
    @JSONField(name = "result")
    private BlockGetLatestValidatorsResult result;

    /**
     * @Author riven
     * @Method getResult
     * @Params []
     * @Return TransactionGetInfoResult
     * @Date 2018/7/5 16:55
     */
    public BlockGetLatestValidatorsResult getResult() {
        return result;
    }

    /**
     * @Author riven
     * @Method setResult
     * @Params [result]
     * @Return void
     * @Date 2018/7/5 16:55
     */
    public void setResult(BlockGetLatestValidatorsResult result) {
        this.result = result;
    }

    /**
     * @Author riven
     * @Method buildResponse
     * @Params [sdkError, result]
     * @Return void
     * @Date 2018/7/5 16:56
     */
    public void buildResponse(SdkError sdkError, BlockGetLatestValidatorsResult result) {
        this.errorCode = sdkError.getCode();
        this.errorDesc = sdkError.getDescription();
        this.result = result;
    }

    /**
     * @Author riven
     * @Method buildResponse
     * @Params [errorCode, errorDesc, result]
     * @Return void
     * @Date 2018/7/5 16:56
     */
    public void buildResponse(int errorCode, String errorDesc, BlockGetLatestValidatorsResult result) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.result = result;
    }
}
