package cn.bubi.model.response.result;

import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.model.response.result.data.MetadataInfo;

/**
 * @Author riven
 * @Date 2018/7/15 14:59
 */
public class AccountGetMetadataResult {
    @JSONField(name = "metadatas")
    private MetadataInfo[] metadatas;

    /**
     * @Author riven
     * @Method getMetadatas
     * @Params []
     * @Return MetadataInfo[]
     * @Date 2018/7/15 14:59
     */
    public MetadataInfo[] getMetadatas() {
        return metadatas;
    }

    /**
     * @Author riven
     * @Method setMetadatas
     * @Params [metadatas]
     * @Return void
     * @Date 2018/7/15 15:02
     */
    public void setMetadatas(MetadataInfo[] metadatas) {
        this.metadatas = metadatas;
    }
}
