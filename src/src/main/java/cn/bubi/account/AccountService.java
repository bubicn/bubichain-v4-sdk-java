package cn.bubi.account;

import cn.bubi.model.request.*;
import cn.bubi.model.response.*;

/**
 * @Author riven
 * @Date 2018/7/3 12:34
 */

public interface AccountService {
    /**
     * @Author riven
     * @Method checkValid
     * @Params [accountCheckValidRequest]
     * @Return cn.bubi.model.response.SDKResponse
     * @Date 2018/7/3 12:56
     */
    public AccountCheckValidResponse checkValid(AccountCheckValidRequest accountCheckValidRequest);

    /**
     * @Author riven
     * @Method create
     * @Params []
     * @Return cn.bubi.model.response.SDKResponse
     * @Date 2018/7/3 12:56
     */
    public AccountCreateResponse create();

    /**
     * @Author riven
     * @Method getInfo
     * @Params [accountGetInfoRequest]
     * @Return AccountGetInfoResponse
     * @Date 2018/7/4 15:15
     */
    public AccountGetInfoResponse getInfo(AccountGetInfoRequest accountGetInfoRequest);

    /**
     * @Author riven
     * @Method getNonce
     * @Params [accountGetNonceRequest]
     * @Return AccountGetNonceResponse
     * @Date 2018/7/4 17:02
     */
    public AccountGetNonceResponse getNonce(AccountGetNonceRequest accountGetNonceRequest);

    /**
     * @Author riven
     * @Method getBalance
     * @Params [accountGetBalanceRequest]
     * @Return AccountGetBalanceResponse
     * @Date 2018/7/4 17:11
     */
    public AccountGetBalanceResponse getBalance(AccountGetBalanceRequest accountGetBalanceRequest);

    /**
     * @Author riven
     * @Method getAssets
     * @Params [accountGetAssetsRequest]
     * @Return AccountGetAssetsResponse
     * @Date 2018/7/5 12:04
     */
    public AccountGetAssetsResponse getAssets(AccountGetAssetsRequest accountGetAssetsRequest);

    /**
     * @Author riven
     * @Method getMetadata
     * @Params [accountGetMetadataRequest]
     * @Return AccountGetMetadataResponse
     * @Date 2018/7/15 15:02
     */
    public AccountGetMetadataResponse getMetadata(AccountGetMetadataRequest accountGetMetadataRequest);

    /**
     * @Author riven
     * @Method checkActivated
     * @Params [accountCheckActivatedRequst]
     * @Return AccountCheckActivatedResponse
     * @Date 2018/8/6 17:44
     */
    public AccountCheckActivatedResponse checkActivated(AccountCheckActivatedRequst accountCheckActivatedRequst);
}
