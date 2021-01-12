package cn.bubi.blockchain;

import cn.bubi.model.request.*;
import cn.bubi.model.response.*;

/**
 * @Author riven
 * @Date 2018/7/3 17:22
 */
public interface TransactionService {
    /**
     * @Author riven
     * @Method buildBlob
     * @Params [transactionBuildBlobRequest]
     * @Return TransactionBuildBlobResponse
     * @Date 2018/7/5 16:57
     */
    public TransactionBuildBlobResponse buildBlob(TransactionBuildBlobRequest transactionBuildBlobRequest);

    /**
     * @Author riven
     * @Method parseBlob
     * @Params [transactionParseBlobRequest]
     * @Return TransactionParseBlobResponse
     * @Date 2018/7/10 17:02
     */
    public TransactionParseBlobResponse parseBlob(TransactionParseBlobRequest transactionParseBlobRequest);

    /**
     * @Author riven
     * @Method evaluateFee
     * @Params [transactionEvaluationFeeRequest]
     * @Return cn.bubi.model.response.TransactionEvaluationFeeResponse
     * @Date 2018/7/5 16:57
     */
    public TransactionEvaluateFeeResponse evaluateFee(TransactionEvaluateFeeRequest transactionEvaluateFeeRequest);

    /**
     * @Author riven
     * @Method sign
     * @Params [transactionSignRequest]
     * @Return TransactionSignResponse
     * @Date 2018/7/5 16:57
     */
    public TransactionSignResponse sign(TransactionSignRequest transactionSignRequest);

    /**
     * @Author riven
     * @Method submit
     * @Params [transactionSubmitRequest]
     * @Return TransactionSubmitResponse
     * @Date 2018/7/5 16:58
     */
    public TransactionSubmitResponse submit(TransactionSubmitRequest transactionSubmitRequest);

    /**
     * @Author riven
     * @Method getInfo
     * @Params [transactionGetInfoRequest]
     * @Return TransactionGetInfoResponse
     * @Date 2018/7/5 21:14
     */
    public TransactionGetInfoResponse getInfo(TransactionGetInfoRequest transactionGetInfoRequest);
}
