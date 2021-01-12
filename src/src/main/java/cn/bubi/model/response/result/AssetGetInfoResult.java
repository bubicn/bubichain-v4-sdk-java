package cn.bubi.model.response.result;

import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.model.response.result.data.AssetInfo;

/**
 * @Author riven
 * @Date 2018/7/5 11:40
 */
public class AssetGetInfoResult {
    @JSONField(name = "assets")
    private AssetInfo[] assets;

    /**
     * @Author riven
     * @Method getAssetService
     * @Params []
     * @Return AssetInfo
     * @Date 2018/7/5 11:46
     */
    public AssetInfo[] getAssets() {
        return assets;
    }

    /**
     * @Author riven
     * @Method setAsset
     * @Params [token]
     * @Return void
     * @Date 2018/7/5 11:46
     */
    public void setAssets(AssetInfo[] assets) {
        this.assets = assets;
    }
}
