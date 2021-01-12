package cn.bubi.token;

import cn.bubi.model.request.AssetGetInfoRequest;
import cn.bubi.model.response.AssetGetInfoResponse;

/**
 * @Author riven
 * @Date 2018/7/3 17:21
 */
public interface AssetService {
    /**
     * @Author riven
     * @Method getInfo
     * @Params [assetGetRequest]
     * @Return AssetGetInfoResponse
     * @Date 2018/7/5 12:05
     */
    public AssetGetInfoResponse getInfo(AssetGetInfoRequest assetGetRequest);
}
