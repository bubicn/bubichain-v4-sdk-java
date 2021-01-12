package cn.bubi.model.response.result;

import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.model.response.result.data.BlockHeader;

/**
 * @Author riven
 * @Date 2018/7/12 00:34
 */
public class BlockGetLatestInfoResult {
    @JSONField(name = "header")
    private BlockHeader header;

    /**
     * @Author riven
     * @Method getHeader
     * @Params []
     * @Return BlockHeader
     * @Date 2018/7/12 10:27
     */
    public BlockHeader getHeader() {
        return header;
    }

    /**
     * @Author riven
     * @Method setHeader
     * @Params [header]
     * @Return void
     * @Date 2018/7/12 10:27
     */
    public void setHeader(BlockHeader header) {
        this.header = header;
    }
}
