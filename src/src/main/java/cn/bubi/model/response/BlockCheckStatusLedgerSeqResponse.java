package cn.bubi.model.response;

import com.alibaba.fastjson.annotation.JSONField;
import cn.bubi.model.response.result.data.LedgerSeq;

/**
 * @Author riven
 * @Date 2018/7/6 02:02
 */
public class BlockCheckStatusLedgerSeqResponse {
    @JSONField(name = "ledger_manager")
    private LedgerSeq ledgerSeq;

    /**
     * @Author riven
     * @Method getLedgerSeq
     * @Params []
     * @Return LedgerSeq
     * @Date 2018/7/6 02:06
     */
    public LedgerSeq getLedgerSeq() {
        return ledgerSeq;
    }

    /**
     * @Author riven
     * @Method setLedgerSeq
     * @Params [ledgerSeq]
     * @Return void
     * @Date 2018/7/6 02:06
     */
    public void setLedgerSeq(LedgerSeq ledgerSeq) {
        this.ledgerSeq = ledgerSeq;
    }
}
