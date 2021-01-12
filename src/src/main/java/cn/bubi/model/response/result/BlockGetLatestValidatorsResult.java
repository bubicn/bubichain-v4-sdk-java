package cn.bubi.model.response.result;

import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.model.response.result.data.ValidatorInfo;

/**
 * @Author riven
 * @Date 2018/7/12 01:07
 */
public class BlockGetLatestValidatorsResult {
    @JSONField(name = "validators")
    private String[] validators;

    /**
     * @Author riven
     * @Method getValidators
     * @Params []
     * @Return ValidatorInfo[]
     * @Date 2018/7/12 10:22
     */
    public String[] getValidators() {
        return validators;
    }

    /**
     * @Author riven
     * @Method setValidators
     * @Params [validators]
     * @Return void
     * @Date 2018/7/12 11:21
     */
    public void setValidators(String[] validators) {
        this.validators = validators;
    }
}
