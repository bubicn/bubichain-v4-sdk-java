package cn.bubi.blockchain.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.bubi.blockchain.BlockService;
import cn.bubi.common.General;
import cn.bubi.common.Tools;
import cn.bubi.crypto.http.HttpKit;
import cn.bubi.exception.SDKException;
import cn.bubi.exception.SdkError;
import cn.bubi.model.request.*;
import cn.bubi.model.response.*;
import cn.bubi.model.response.result.*;
import cn.bubi.model.response.result.data.LedgerSeq;
import cn.bubi.model.response.result.data.ValidatorRewardInfo;
import cn.bubi.model.response.result.data.Rewards;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Map;

/**
 * @Author riven
 * @Date 2018/7/3 17:23
 */
public class BlockServiceImpl implements BlockService {
    /**
     * @Author riven
     * @Method getNumber
     * @Params []
     * @Return BlockGetNumberResponse
     * @Date 2018/7/6 02:17
     */
    @Override
    public BlockGetNumberResponse getNumber() {
        BlockGetNumberResponse blockGetNumberResponse = new BlockGetNumberResponse();
        BlockGetNumberResult blockGetNumberResult = new BlockGetNumberResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            String getNumberUrl = General.getInstance().blockGetNumberUrl();
            String result = HttpKit.get(getNumberUrl);
            blockGetNumberResponse = JSONObject.parseObject(result, BlockGetNumberResponse.class);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetNumberResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, blockGetNumberResult);
        } catch (Exception e) {
            blockGetNumberResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), blockGetNumberResult);
        }
        return blockGetNumberResponse;
    }

    /**
     * @Author riven
     * @Method checkStatus
     * @Params []
     * @Return BlockCheckStatusResponse
     * @Date 2018/7/6 02:17
     */
    @Override
    public BlockCheckStatusResponse checkStatus() {
        BlockCheckStatusResponse blockCheckStatusResponse = new BlockCheckStatusResponse();
        BlockCheckStatusResult blockCheckStatusResult = new BlockCheckStatusResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            String checkStatusUrl = General.getInstance().blockCheckStatusUrl();
            String result = HttpKit.get(checkStatusUrl);
            BlockCheckStatusLedgerSeqResponse blockCheckStatusLedgerSeqResponse = JSONObject.parseObject(result, BlockCheckStatusLedgerSeqResponse.class);
            if (blockCheckStatusLedgerSeqResponse == null) {
                throw new SDKException(SdkError.CONNECTNETWORK_ERROR);
            }
            LedgerSeq ledgerSeq = blockCheckStatusLedgerSeqResponse.getLedgerSeq();
            if (ledgerSeq.getLedgerSequence() < ledgerSeq.getChainMaxLedgerSeq()) {
                blockCheckStatusResult.setSynchronous(false);
            } else {
                blockCheckStatusResult.setSynchronous(true);
            }
            blockCheckStatusResponse.buildResponse(SdkError.SUCCESS, blockCheckStatusResult);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockCheckStatusResponse.buildResponse(errorCode, errorDesc, blockCheckStatusResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockCheckStatusResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, blockCheckStatusResult);
        } catch (Exception e) {
            blockCheckStatusResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), blockCheckStatusResult);
        }
        return blockCheckStatusResponse;
    }

    /**
     * @Author riven
     * @Method getTransactions
     * @Params [blockGetTransactionsRequest]
     * @Return BlockGetTransactionsResponse
     * @Date 2018/7/12 00:38
     */
    @Override
    public BlockGetTransactionsResponse getTransactions(BlockGetTransactionsRequest blockGetTransactionsRequest) {
        BlockGetTransactionsResponse blockGetTransactions = new BlockGetTransactionsResponse();
        BlockGetTransactionsResult transactionGetInfoResult = new BlockGetTransactionsResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(blockGetTransactionsRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            Long blockNumber = blockGetTransactionsRequest.getBlockNumber();
            if (Tools.isEmpty(blockNumber) || blockNumber < 1) {
                throw new SDKException(SdkError.INVALID_BLOCKNUMBER_ERROR);
            }
            String getTransactionsUrl = General.getInstance().blockGetTransactionsUrl(blockNumber);
            String result = HttpKit.get(getTransactionsUrl);
            blockGetTransactions = JSONObject.parseObject(result, BlockGetTransactionsResponse.class);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetTransactions.buildResponse(errorCode, errorDesc, transactionGetInfoResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetTransactions.buildResponse(SdkError.CONNECTNETWORK_ERROR, transactionGetInfoResult);
        } catch (Exception e) {
            blockGetTransactions.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), transactionGetInfoResult);
        }
        return blockGetTransactions;
    }

    /**
     * @Author riven
     * @Method getInfo
     * @Params [blockGetInfoRequest]
     * @Return BlockGetInfoResponse
     * @Date 2018/7/12 00:38
     */
    @Override
    public BlockGetInfoResponse getInfo(BlockGetInfoRequest blockGetInfoRequest) {
        BlockGetInfoResponse blockGetInfoResponse = new BlockGetInfoResponse();
        BlockGetInfoResult blockGetInfoResult = new BlockGetInfoResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(blockGetInfoRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            Long blockNumber = blockGetInfoRequest.getBlockNumber();
            if (Tools.isEmpty(blockNumber) || blockNumber < 1) {
                throw new SDKException(SdkError.INVALID_BLOCKNUMBER_ERROR);
            }
            String getInfoUrl = General.getInstance().blockGetInfoUrl(blockNumber);
            String result = HttpKit.get(getInfoUrl);
            blockGetInfoResponse = JSONObject.parseObject(result, BlockGetInfoResponse.class);
            Integer errorCode = blockGetInfoResponse.getErrorCode();
            String errorDesc = blockGetInfoResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode == 4) {
                throw new SDKException(4, (Tools.isEmpty(errorDesc) ? "Block (" + blockNumber + ") does not exist" : errorDesc));
            }
            SdkError.checkErrorCode(blockGetInfoResponse);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetInfoResponse.buildResponse(errorCode, errorDesc, blockGetInfoResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetInfoResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, blockGetInfoResult);
        } catch (Exception e) {
            blockGetInfoResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), blockGetInfoResult);
        }

        return blockGetInfoResponse;
    }

    /**
     * @Author riven
     * @Method getLatestInfo
     * @Params []
     * @Return BlockGetLatestInfoResponse
     * @Date 2018/7/12 00:52
     */
    @Override
    public BlockGetLatestInfoResponse getLatestInfo() {
        BlockGetLatestInfoResponse blockGetLatestInfoResponse = new BlockGetLatestInfoResponse();
        BlockGetLatestInfoResult blockGetLatestInfoResult = new BlockGetLatestInfoResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            String getInfoUrl = General.getInstance().blockGetLatestInfoUrl();
            String result = HttpKit.get(getInfoUrl);
            blockGetLatestInfoResponse = JSONObject.parseObject(result, BlockGetLatestInfoResponse.class);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetLatestInfoResponse.buildResponse(errorCode, errorDesc, blockGetLatestInfoResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetLatestInfoResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, blockGetLatestInfoResult);
        } catch (Exception e) {
            blockGetLatestInfoResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), blockGetLatestInfoResult);
        }
        return blockGetLatestInfoResponse;
    }

    /**
     * @Author riven
     * @Method getValidators
     * @Params [blockGetValidatorsRequest]
     * @Return BlockGetValidatorsResponse
     * @Date 2018/7/12 01:24
     */
    @Override
    public BlockGetValidatorsResponse getValidators(BlockGetValidatorsRequest blockGetValidatorsRequest) {
        BlockGetValidatorsResponse blockGetValidatorsResponse = new BlockGetValidatorsResponse();
        BlockGetValidatorsResult blockGetValidatorsResult = new BlockGetValidatorsResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(blockGetValidatorsRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            Long blockNumber = blockGetValidatorsRequest.getBlockNumber();
            if (Tools.isEmpty(blockNumber) || blockNumber < 1) {
                throw new SDKException(SdkError.INVALID_BLOCKNUMBER_ERROR);
            }
            String getInfoUrl = General.getInstance().blockGetValidatorsUrl(blockNumber);
            String result = HttpKit.get(getInfoUrl);
            blockGetValidatorsResponse = JSONObject.parseObject(result, BlockGetValidatorsResponse.class);
            Integer errorCode = blockGetValidatorsResponse.getErrorCode();
            String errorDesc = blockGetValidatorsResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode == 4) {
                throw new SDKException(4, (Tools.isEmpty(errorDesc) ? "Block (" + blockNumber + ") does not exist" : errorDesc));
            }
            SdkError.checkErrorCode(blockGetValidatorsResponse);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetValidatorsResponse.buildResponse(errorCode, errorDesc, blockGetValidatorsResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetValidatorsResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, blockGetValidatorsResult);
        } catch (Exception e) {
            blockGetValidatorsResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), blockGetValidatorsResult);
        }

        return blockGetValidatorsResponse;
    }

    /**
     * @Author riven
     * @Method getLatestValidators
     * @Params []
     * @Return BlockGetLatestValidatorsResponse
     * @Date 2018/7/12 01:36
     */
    @Override
    public BlockGetLatestValidatorsResponse getLatestValidators() {
        BlockGetLatestValidatorsResponse blockGetLatestValidatorsResponse = new BlockGetLatestValidatorsResponse();
        BlockGetLatestValidatorsResult blockGetLatestValidatorsResult = new BlockGetLatestValidatorsResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            String getInfoUrl = General.getInstance().blockGetLatestValidatorsUrl();
            String result = HttpKit.get(getInfoUrl);
            blockGetLatestValidatorsResponse = JSONObject.parseObject(result, BlockGetLatestValidatorsResponse.class);
            SdkError.checkErrorCode(blockGetLatestValidatorsResponse);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetLatestValidatorsResponse.buildResponse(errorCode, errorDesc, blockGetLatestValidatorsResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetLatestValidatorsResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, blockGetLatestValidatorsResult);
        } catch (Exception e) {
            blockGetLatestValidatorsResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), blockGetLatestValidatorsResult);
        }

        return blockGetLatestValidatorsResponse;
    }

    /**
     * @Author riven
     * @Method getReward
     * @Params [blockGetRewardRequest]
     * @Return BlockGetRewardResponse
     * @Date 2018/7/12 01:48
     */
    @Override
    public BlockGetRewardResponse getReward(BlockGetRewardRequest blockGetRewardRequest) {
        BlockGetRewardResponse blockGetRewardResponse = new BlockGetRewardResponse();
        BlockGetRewardResult blockGetRewardResult = new BlockGetRewardResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(blockGetRewardRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            Long blockNumber = blockGetRewardRequest.getBlockNumber();
            if (Tools.isEmpty(blockNumber) || blockNumber < 1) {
                throw new SDKException(SdkError.INVALID_BLOCKNUMBER_ERROR);
            }
            JSONObject input = new JSONObject();
            input.put("method", "getRewardDistribute");
            JSONObject params = new JSONObject();
            params.put("opt_type", 2);
            params.put("fee_limit", 1000000000L);
            params.put("contract_address", "adxSmrzvnbzSp1zXF26kuJCZ6hJbFYydPpBzX");
            params.put("input", input.toJSONString());
            String contractCallUrl = General.getInstance().contractCallUrl();
            String result = HttpKit.post(contractCallUrl, params.toJSONString());
            ContractCallResponse callResp = JSONObject.parseObject(result, ContractCallResponse.class);
            if (callResp.getErrorCode() == 0) {
                String value = callResp.getResult().getQueryRets().getJSONObject(0).getJSONObject("result").getString("value");
                JSONObject valueJson = JSONObject.parseObject(value);
                JSONObject validators = valueJson.getJSONObject("rewards").getJSONObject("validators");
                JSONObject kols = valueJson.getJSONObject("rewards").getJSONObject("kols");
                for (Map.Entry<String, Object> entry : validators.entrySet()) {
                    Rewards validatorObj = new Rewards();
                    validatorObj.setAddress(entry.getKey());
                    validatorObj.setReward((JSONArray)entry.getValue());
                    blockGetRewardResult.addValidator(validatorObj);
                }
                for (Map.Entry<String, Object> entry : kols.entrySet()) {
                    Rewards kolObj = new Rewards();
                    kolObj.setAddress(entry.getKey());
                    kolObj.setReward((JSONArray)entry.getValue());
                    blockGetRewardResult.addKol(kolObj);
                }
                blockGetRewardResponse.buildResponse(SdkError.SUCCESS, blockGetRewardResult);
            } else {
                blockGetRewardResponse.buildResponse(callResp.getErrorCode(), callResp.getErrorDesc());
            }
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetRewardResponse.buildResponse(errorCode, errorDesc, blockGetRewardResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetRewardResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, blockGetRewardResult);
        } catch (Exception e) {
            blockGetRewardResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), blockGetRewardResult);
        }
        return blockGetRewardResponse;
    }

    /**
     * @Author riven
     * @Method getLatestReward
     * @Params []
     * @Return BlockGetLatestRewardResponse
     * @Date 2018/7/12 02:08
     */
    @Override
    public BlockGetLatestRewardResponse getLatestReward() {
        BlockGetLatestRewardResponse blockGetLatestRewardResponse = new BlockGetLatestRewardResponse();
        BlockGetLatestRewardResult blockGetLatestRewardResult = new BlockGetLatestRewardResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            JSONObject input = new JSONObject();
            input.put("method", "getRewardDistribute");
            JSONObject params = new JSONObject();
            params.put("opt_type", 2);
            params.put("fee_limit", 1000000000L);
            params.put("contract_address", "adxSmrzvnbzSp1zXF26kuJCZ6hJbFYydPpBzX");
            params.put("input", input.toJSONString());
            String contractCallUrl = General.getInstance().contractCallUrl();
            String result = HttpKit.post(contractCallUrl, params.toJSONString());
            ContractCallResponse callResp = JSONObject.parseObject(result, ContractCallResponse.class);
            if (callResp.getErrorCode() == 0) {
                String value = callResp.getResult().getQueryRets().getJSONObject(0).getJSONObject("result").getString("value");
                JSONObject valueJson = JSONObject.parseObject(value);
                JSONObject validators = valueJson.getJSONObject("rewards").getJSONObject("validators");
                JSONObject kols = valueJson.getJSONObject("rewards").getJSONObject("kols");
                for (Map.Entry<String, Object> entry : validators.entrySet()) {
                    Rewards validatorObj = new Rewards();
                    validatorObj.setAddress(entry.getKey());
                    validatorObj.setReward((JSONArray)entry.getValue());
                    blockGetLatestRewardResult.addValidator(validatorObj);
                }
                for (Map.Entry<String, Object> entry : kols.entrySet()) {
                    Rewards kolObj = new Rewards();
                    kolObj.setAddress(entry.getKey());
                    kolObj.setReward((JSONArray)entry.getValue());
                    blockGetLatestRewardResult.addKol(kolObj);
                }
                blockGetLatestRewardResponse.buildResponse(SdkError.SUCCESS, blockGetLatestRewardResult);
            } else {
                blockGetLatestRewardResponse.buildResponse(callResp.getErrorCode(), callResp.getErrorDesc());
            }
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetLatestRewardResponse.buildResponse(errorCode, errorDesc, blockGetLatestRewardResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetLatestRewardResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, blockGetLatestRewardResult);
        } catch (Exception e) {
            blockGetLatestRewardResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), blockGetLatestRewardResult);
        }
        return blockGetLatestRewardResponse;
    }

    /**
     * @Author riven
     * @Method getFees
     * @Params [blockGetFeesRequest]
     * @Return BlockGetFeesResponse
     * @Date 2018/7/6 02:17
     */
    @Override
    public BlockGetFeesResponse getFees(BlockGetFeesRequest blockGetFeesRequest) {
        BlockGetFeesResponse blockGetFeesResponse = new BlockGetFeesResponse();
        BlockGetFeesResult blockGetFeesResult = new BlockGetFeesResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(blockGetFeesRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            Long blockNumber = blockGetFeesRequest.getBlockNumber();
            if (Tools.isEmpty(blockNumber) || blockNumber < 1) {
                throw new SDKException(SdkError.INVALID_BLOCKNUMBER_ERROR);
            }
            String blockGetFeesUrl = General.getInstance().blockGetFeesUrl(blockNumber);
            String result = HttpKit.get(blockGetFeesUrl);
            blockGetFeesResponse = JSON.parseObject(result, BlockGetFeesResponse.class);
            Integer errorCode = blockGetFeesResponse.getErrorCode();
            String errorDesc = blockGetFeesResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode == 4) {
                throw new SDKException(4, (Tools.isEmpty(errorDesc) ? "Block (" + blockNumber + ") does not exist" : errorDesc));
            }
            SdkError.checkErrorCode(blockGetFeesResponse);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            blockGetFeesResponse.buildResponse(errorCode, errorDesc, blockGetFeesResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetFeesResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, blockGetFeesResult);
        } catch (Exception e) {
            blockGetFeesResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), blockGetFeesResult);
        }

        return blockGetFeesResponse;
    }

    /**
     * @Author riven
     * @Method getLatestFees
     * @Params []
     * @Return BlockGetLatestFeesResponse
     * @Date 2018/7/6 10:35
     */
    @Override
    public BlockGetLatestFeesResponse getLatestFees() {
        BlockGetLatestFeesResponse blockGetLatestFeesResponse = new BlockGetLatestFeesResponse();
        BlockGetLatestFeesResult blockGetLatestFeesResult = new BlockGetLatestFeesResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            String blockGetLatestFeeUrl = General.getInstance().blockGetLatestFeeUrl();
            String result = HttpKit.get(blockGetLatestFeeUrl);
            blockGetLatestFeesResponse = JSON.parseObject(result, BlockGetLatestFeesResponse.class);
            SdkError.checkErrorCode(blockGetLatestFeesResponse);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            blockGetLatestFeesResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, blockGetLatestFeesResult);
        } catch (Exception e) {
            blockGetLatestFeesResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), blockGetLatestFeesResult);
        }

        return blockGetLatestFeesResponse;
    }


}
