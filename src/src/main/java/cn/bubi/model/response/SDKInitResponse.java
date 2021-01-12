package cn.bubi.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.exception.SdkError;
import cn.bubi.model.response.result.SDKInitResult;

/**
 * @Author riven
 * @Date 2018/7/4 14:35
 */
public class SDKInitResponse extends BaseResponse {
    @JSONField(name = "result")
    private SDKInitResult result;

    /**
     * @Author riven
     * @Method getSdkInitResult
     * @Params []
     * @Return SDKInitResult
     * @Date 2018/7/4 15:07
     */
    public SDKInitResult getSdkInitResult() {
        return result;
    }

    /**
     * @Author riven
     * @Method setSdkInitResult
     * @Params [result]
     * @Return void
     * @Date 2018/7/4 15:07
     */
    public void setSdkInitResult(SDKInitResult result) {
        this.result = result;
    }

    /**
     * @Author riven
     * @Method buildResponse
     * @Params [sdkError, result]
     * @Return void
     * @Date 2018/7/4 15:07
     */
    public void buildResponse(SdkError sdkError, SDKInitResult result) {
        this.errorCode = sdkError.getCode();
        this.errorDesc = sdkError.getDescription();
        this.result = result;
    }

    /**
     * @Author riven
     * @Method buildResponse
     * @Params [errorCode, errorDesc, result]
     * @Return void
     * @Date 2018/7/4 15:07
     */
    public void buildResponse(int errorCode, String errorDesc, SDKInitResult result) {
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.result = result;
    }
}
