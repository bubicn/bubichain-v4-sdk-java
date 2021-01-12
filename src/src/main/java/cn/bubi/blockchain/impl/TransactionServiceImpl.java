package cn.bubi.blockchain.impl;

import cn.bubi.crypto.protobuf.Chain;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import cn.bubi.SDK;
import cn.bubi.account.impl.AccountServiceImpl;
import cn.bubi.blockchain.BlockService;
import cn.bubi.blockchain.TransactionService;
import cn.bubi.common.Constant;
import cn.bubi.common.General;
import cn.bubi.common.OperationType;
import cn.bubi.common.Tools;
import cn.bubi.contract.impl.ContractServiceImpl;
import cn.bubi.crypto.http.HttpKit;
import cn.bubi.encryption.key.PrivateKey;
import cn.bubi.encryption.key.PublicKey;
import cn.bubi.encryption.utils.hash.HashUtil;
import cn.bubi.encryption.utils.hex.HexFormat;
import cn.bubi.exception.SDKException;
import cn.bubi.exception.SdkError;
import cn.bubi.log.LogServiceImpl;
import cn.bubi.model.request.*;
import cn.bubi.model.request.operation.*;
import cn.bubi.model.response.*;
import cn.bubi.model.response.result.*;
import cn.bubi.model.response.result.data.Signature;
import cn.bubi.token.impl.AssetServiceImpl;
import cn.bubi.token.impl.GasServiceImpl;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

/**
 * @Author riven
 * @Date 2018/7/3 17:22
 */
public class TransactionServiceImpl implements TransactionService {

    /**
     * @Author riven
     * @Method buildBlob
     * @Params [transactionBuildBlobRequest]
     * @Return TransactionBuildBlobResponse
     * @Date 2018/7/5 19:04
     */
    @Override
    public TransactionBuildBlobResponse buildBlob(TransactionBuildBlobRequest transactionBuildBlobRequest) {
        TransactionBuildBlobResponse transactionBuildBlobResponse = new TransactionBuildBlobResponse();
        TransactionBuildBlobResult transactionBuildBlobResult = new TransactionBuildBlobResult();
        try {
            if (Tools.isEmpty(transactionBuildBlobRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            // check sourceAddress
            String sourceAddress = transactionBuildBlobRequest.getSourceAddress();
            if (!PublicKey.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            // check nonce
            Long nonce = transactionBuildBlobRequest.getNonce();
            if (Tools.isEmpty(nonce) || nonce < 1) {
                throw new SDKException(SdkError.INVALID_NONCE_ERROR);
            }
            // check gasPrice
            Long gasPrice = transactionBuildBlobRequest.getGasPrice();
            if (Tools.isEmpty(gasPrice) || gasPrice < 0) {
                throw new SDKException(SdkError.INVALID_GASPRICE_ERROR);
            }

            // check feeLimit
            Long feeLimit = transactionBuildBlobRequest.getFeeLimit();
            if (Tools.isEmpty(feeLimit) || feeLimit < 0) {
                throw new SDKException(SdkError.INVALID_FEELIMIT_ERROR);
            }

            Long ceilLedgerSeq = transactionBuildBlobRequest.getCeilLedgerSeq();
            if (!Tools.isEmpty(ceilLedgerSeq) && ceilLedgerSeq < 0) {
                throw new SDKException(SdkError.INVALID_CEILLEDGERSEQ_ERROR);
            }
            // check metadata
            String metadata = transactionBuildBlobRequest.getMetadata();
            // build transaction
            Chain.Transaction.Builder transaction = Chain.Transaction.newBuilder();
            // add note
            if (!Tools.isEmpty(metadata)) {
                transaction.setMetadata(ByteString.copyFromUtf8(metadata));
            }
            // check operation
            BaseOperation[] baseOperations = transactionBuildBlobRequest.getOperations();
            if (Tools.isEmpty(baseOperations)) {
                throw new SDKException(SdkError.OPERATIONS_EMPTY_ERROR);
            }
            buildOperations(baseOperations, sourceAddress, transaction);
            // add other information
            transaction.setSourceAddress(sourceAddress);
            transaction.setNonce(nonce);
            transaction.setFeeLimit(feeLimit);
            transaction.setGasPrice(gasPrice);
            if (!Tools.isEmpty(SDK.getSdk().getChainId())) {
                transaction.setChainId(SDK.getSdk().getChainId());
            }

            if (!Tools.isEmpty(ceilLedgerSeq)) {
                // get blockNumber
                BlockService blockService = new BlockServiceImpl();
                BlockGetNumberResponse blockGetNumberResponse = blockService.getNumber();
                Integer errorCode = blockGetNumberResponse.getErrorCode();
                if (!Tools.isEmpty(errorCode) && errorCode != 0) {
                    String errorDesc = blockGetNumberResponse.getErrorDesc();
                    throw new SDKException(errorCode, errorDesc);
                } else if (Tools.isEmpty(errorCode)) {
                    throw new SDKException(SdkError.CONNECTNETWORK_ERROR);
                }
                // check ceilLedgerSeq
                Long blockNumber = blockGetNumberResponse.getResult().getHeader().getBlockNumber();
                transaction.setCeilLedgerSeq(ceilLedgerSeq + blockNumber);
            }
            byte[] transactionBlobBytes = transaction.build().toByteArray();
            String transactionBlob = HexFormat.byteToHex(transactionBlobBytes);
            transactionBuildBlobResult.setTransactionBlob(transactionBlob);
            transactionBuildBlobResult.setHash(HashUtil.GenerateHashHex(transactionBlobBytes));
            transactionBuildBlobResponse.buildResponse(SdkError.SUCCESS, transactionBuildBlobResult);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            transactionBuildBlobResponse.buildResponse(errorCode, errorDesc, transactionBuildBlobResult);
        } catch (Exception e) {
            transactionBuildBlobResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), transactionBuildBlobResult);
        }

        return transactionBuildBlobResponse;
    }

    /**
     * @Author riven
     * @Method parseBlob
     * @Params [transactionParseBlobRequest]
     * @Return TransactionParseBlobResponse
     * @Date 2018/7/23 10:01
     */
    @Override
    public TransactionParseBlobResponse parseBlob(TransactionParseBlobRequest transactionParseBlobRequest) {
        TransactionParseBlobResponse transactionParseBlobResponse = new TransactionParseBlobResponse();
        TransactionParseBlobResult transactionParseBlobResult = new TransactionParseBlobResult();
        try {
            if (Tools.isEmpty(transactionParseBlobRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String blob = transactionParseBlobRequest.getBlob();
            if (Tools.isEmpty(blob)) {
                throw new SDKException(SdkError.INVALID_BLOB_ERROR);
            }
            Chain.Transaction transaction = Chain.Transaction.parseFrom(HexFormat.hexToByte(blob));
            JsonFormat jsonFormat = new JsonFormat();
            String transactionJson = jsonFormat.printToString(transaction);
            transactionParseBlobResult = JSONObject.parseObject(transactionJson, TransactionParseBlobResult.class);
            transactionParseBlobResponse.buildResponse(SdkError.SUCCESS, transactionParseBlobResult);
        } catch (SDKException sdkException) {
            Integer errorCode = sdkException.getErrorCode();
            String errorDesc = sdkException.getErrorDesc();
            transactionParseBlobResponse.buildResponse(errorCode, errorDesc, transactionParseBlobResult);
        } catch (InvalidProtocolBufferException e) {
            transactionParseBlobResponse.buildResponse(SdkError.INVALID_BLOB_ERROR, transactionParseBlobResult);
        } catch (Exception e) {
            transactionParseBlobResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), transactionParseBlobResult);
        }

        return transactionParseBlobResponse;
    }

    /**
     * @Author riven
     * @Method evaluateFee
     * @Params [TransactionEvaluateFeeRequest]
     * @Return TransactionEvaluateFeeResponse
     * @Date 2018/7/5 19:04
     */
    @Override
    public TransactionEvaluateFeeResponse evaluateFee(TransactionEvaluateFeeRequest transactionEvaluateFeeRequest) {
        TransactionEvaluateFeeResponse transactionEvaluateFeeResponse = new TransactionEvaluateFeeResponse();
        TransactionEvaluateFeeResult transactionEvaluateFeeResult = new TransactionEvaluateFeeResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(transactionEvaluateFeeRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            // check sourceAddress
            String sourceAddress = transactionEvaluateFeeRequest.getSourceAddress();
            if (!PublicKey.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            // check nonce
            Long nonce = transactionEvaluateFeeRequest.getNonce();
            if (Tools.isEmpty(nonce) || nonce < 1) {
                throw new SDKException(SdkError.INVALID_NONCE_ERROR);
            }
            // check signatureNum
            Integer signatureNum = transactionEvaluateFeeRequest.getSignatureNumber();
            if (Tools.isEmpty(signatureNum) || signatureNum < 1) {
                throw new SDKException(SdkError.INVALID_SIGNATURENUMBER_ERROR);
            }
            // check ceilLedgerSeq
            Long ceilLedgerSeq = transactionEvaluateFeeRequest.getCeilLedgerSeq();
            if (!Tools.isEmpty(ceilLedgerSeq) && ceilLedgerSeq < 0) {
                throw new SDKException(SdkError.INVALID_CEILLEDGERSEQ_ERROR);
            }
            // check metadata
            String metadata = transactionEvaluateFeeRequest.getMetadata();
            // build transaction
            Chain.Transaction.Builder transaction = Chain.Transaction.newBuilder();
            BaseOperation[] baseOperations = transactionEvaluateFeeRequest.getOperations();
            if (Tools.isEmpty(baseOperations)) {
                throw new SDKException(SdkError.OPERATIONS_EMPTY_ERROR);
            }
            buildOperations(baseOperations, sourceAddress, transaction);
            transaction.setSourceAddress(sourceAddress);
            transaction.setNonce(nonce);
            if (!Tools.isEmpty(metadata)) {
                transaction.setMetadata(ByteString.copyFromUtf8(metadata));
            }
            if (!Tools.isEmpty(ceilLedgerSeq)) {
                transaction.setCeilLedgerSeq(ceilLedgerSeq);
            }
            // protocol buffer to json
            JsonFormat jsonFormat = new JsonFormat();
            String transactionStr = jsonFormat.printToString(transaction.build());
            JSONObject transactionJson = JSONObject.parseObject(transactionStr);

            // build testTransaction request
            JSONObject testTransactionRequest = new JSONObject();
            JSONArray transactionItems = new JSONArray();
            JSONObject transactionItem = new JSONObject();
            transactionItem.put("transaction_json", transactionJson);
            transactionItems.add(transactionItem);
            testTransactionRequest.put("items", transactionItems);

            String evaluationFeeUrl = General.getInstance().transactionEvaluationFee();
            String result = HttpKit.post(evaluationFeeUrl, testTransactionRequest.toJSONString());
            transactionEvaluateFeeResponse = JSON.parseObject(result, TransactionEvaluateFeeResponse.class);

        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            transactionEvaluateFeeResponse.buildResponse(errorCode, errorDesc, transactionEvaluateFeeResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            transactionEvaluateFeeResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, transactionEvaluateFeeResult);
        } catch (Exception e) {
            transactionEvaluateFeeResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), transactionEvaluateFeeResult);
        }
        return transactionEvaluateFeeResponse;
    }


    /**
     * @Author riven
     * @Method sign
     * @Params [transactionSignRequest]
     * @Return TransactionSignResponse
     * @Date 2018/7/5 19:04
     */
    @Override
    public TransactionSignResponse sign(TransactionSignRequest transactionSignRequest) {
        TransactionSignResponse transactionSignResponse = new TransactionSignResponse();
        TransactionSignResult transactionSignResult = new TransactionSignResult();
        try {
            if (Tools.isEmpty(transactionSignRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String blob = transactionSignRequest.getBlob();
            if (Tools.isEmpty(blob)) {
                throw new SDKException(SdkError.INVALID_BLOB_ERROR);
            }
            byte[] blobBytes = HexFormat.hexToByte(blob);
            Chain.Transaction.parseFrom(blobBytes);
            String[] privateKeys = transactionSignRequest.getPrivateKeys();
            if (Tools.isEmpty(privateKeys)) {
                throw new SDKException(SdkError.PRIVATEKEY_NULL_ERROR);
            }
            int i = 0;
            int length = privateKeys.length;
            Signature[] signatures = new Signature[length];
            for (; i < length; i++) {
                if (!PrivateKey.isPrivateKeyValid(privateKeys[i])) {
                    throw new SDKException(SdkError.PRIVATEKEY_ONE_ERROR);
                }
                Signature signature = new Signature();
                byte[] signBytes = PrivateKey.sign(blobBytes, privateKeys[i]);
                String publicKey = PrivateKey.getEncPublicKey(privateKeys[i]);
                signature.setSignData(HexFormat.byteToHex(signBytes));
                signature.setPublicKey(publicKey);
                signatures[i] = signature;
            }
            transactionSignResult.setSignatures(signatures);
            transactionSignResponse.buildResponse(SdkError.SUCCESS, transactionSignResult);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            transactionSignResponse.buildResponse(errorCode, errorDesc, transactionSignResult);
        } catch (InvalidProtocolBufferException | IllegalArgumentException e) {
            transactionSignResponse.buildResponse(SdkError.INVALID_BLOB_ERROR, transactionSignResult);
        } catch (Exception e) {
            e.printStackTrace();
            transactionSignResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), transactionSignResult);
        }
        return transactionSignResponse;
    }

    /**
     * @Author riven
     * @Method submit
     * @Params [transactionSubmitRequest]
     * @Return TransactionSubmitResponse
     * @Date 2018/7/5 19:04
     */
    @Override
    public TransactionSubmitResponse submit(TransactionSubmitRequest transactionSubmitRequest) {
        TransactionSubmitResponse transactionSubmitResponse = new TransactionSubmitResponse();
        TransactionSubmitResult transactionSubmitResult = new TransactionSubmitResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(transactionSubmitRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String blob = transactionSubmitRequest.getTransactionBlob();
            if (Tools.isEmpty(blob)) {
                throw new SDKException(SdkError.INVALID_BLOB_ERROR);
            }
            Chain.Transaction.parseFrom(HexFormat.hexToByte(blob));
            // build transaction request
            JSONObject transactionItemsRequest = new JSONObject();
            JSONArray transactionItems = new JSONArray();
            JSONObject transactionItem = new JSONObject();
            transactionItem.put("transaction_blob", blob);
            JSONArray signatureItems = new JSONArray();
            Signature[] signatures = transactionSubmitRequest.getSignatures();
            if (Tools.isEmpty(signatures)) {
                throw new SDKException(SdkError.SIGNATURE_EMPTY_ERROR);
            }
            int i = 0;
            int length = signatures.length;
            for (; i < length; i++) {
                JSONObject signatureItem = new JSONObject();
                Signature signature = signatures[i];
                String signData = signature.getSignData();
                if (Tools.isEmpty(signData)) {
                    throw new SDKException(SdkError.SIGNDATA_NULL_ERROR);
                }
                String publicKey = signature.getPublicKey();
                if (Tools.isEmpty(publicKey)) {
                    throw new SDKException(SdkError.PUBLICKEY_NULL_ERROR);
                }
                signatureItem.put("sign_data", signData);
                signatureItem.put("public_key", publicKey);
                signatureItems.add(signatureItem);
            }
            transactionItem.put("signatures", signatureItems);
            transactionItems.add(transactionItem);
            transactionItemsRequest.put("items", transactionItems);
            // submit
            String submitUrl = General.getInstance().transactionSubmitUrl();
            String transactionRequest = transactionItemsRequest.toJSONString();
            String result = HttpKit.post(submitUrl, transactionRequest);
            TransactionSubmitHttpResponse transactionSubmitHttpResponse = JSONObject.parseObject(result, TransactionSubmitHttpResponse.class);
            Integer successCount = transactionSubmitHttpResponse.getSuccessCount();
            TransactionSubmitHttpResult[] httpResults = transactionSubmitHttpResponse.getResults();
            if (!Tools.isEmpty(httpResults)) {
                transactionSubmitResult.setHash(httpResults[0].getHash());
                if (!Tools.isEmpty(successCount) && 0 == successCount) {
                    Integer errorCode = httpResults[0].getErrorCode();
                    String errorDesc = httpResults[0].getErrorDesc();
                    throw new SDKException(errorCode, errorDesc);
                }
            } else {
                throw new SDKException(SdkError.INVALID_BLOB_ERROR);
            }
            //System.out.println("[bubichain] hash: " + transactionSubmitResult.getHash() + ", transaction: " + transactionRequest);

            transactionSubmitResponse.buildResponse(SdkError.SUCCESS, transactionSubmitResult);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            transactionSubmitResponse.buildResponse(errorCode, errorDesc, transactionSubmitResult);
        } catch (InvalidProtocolBufferException | IllegalArgumentException e) {
            transactionSubmitResponse.buildResponse(SdkError.INVALID_BLOB_ERROR, transactionSubmitResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            transactionSubmitResponse.buildResponse(SdkError.CONNECTN_BLOCKCHAIN_ERROR, transactionSubmitResult);
        } catch (Exception e) {
            transactionSubmitResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), transactionSubmitResult);
        }
        return transactionSubmitResponse;
    }

    /**
     * @Author riven
     * @Method getInfo
     * @Params [transactionGetInfoRequest]
     * @Return TransactionGetInfoResponse
     * @Date 2018/7/5 19:04
     */
    @Override
    public TransactionGetInfoResponse getInfo(TransactionGetInfoRequest transactionGetInfoRequest) {
        TransactionGetInfoResponse transactionGetInfoResponse = new TransactionGetInfoResponse();
        TransactionGetInfoResult transactionGetInfoResult = new TransactionGetInfoResult();
        try {
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            if (Tools.isEmpty(transactionGetInfoRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String hash = transactionGetInfoRequest.getHash();
            if (Tools.isEmpty(hash) || hash.length() != Constant.HASH_HEX_LENGTH) {
                throw new SDKException(SdkError.INVALID_HASH_ERROR);
            }
            transactionGetInfoResponse = getTransactionInfo(hash);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            transactionGetInfoResponse.buildResponse(errorCode, errorDesc, transactionGetInfoResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            transactionGetInfoResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, transactionGetInfoResult);
        } catch (Exception e) {
            transactionGetInfoResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), transactionGetInfoResult);
        }
        return transactionGetInfoResponse;
    }

    /**
     * @Author riven
     * @Method buildOperations
     * @Params [operationBase, transaction]
     * @Return void
     * @Date 2018/7/23 10:19
     */
    private void buildOperations(BaseOperation[] operationBase, String transSourceAddress, Chain.Transaction.Builder transaction) throws SDKException {
        for (int i = 0; i < operationBase.length; i++) {
            Chain.Operation operation;
            OperationType operationType = operationBase[i].getOperationType();
            switch (operationType) {
                case ACCOUNT_ACTIVATE:
                    operation = AccountServiceImpl.activate((AccountActivateOperation) operationBase[i], transSourceAddress);
                    break;
                case ACCOUNT_SET_METADATA:
                    operation = AccountServiceImpl.setMetadata((AccountSetMetadataOperation) operationBase[i]);
                    break;
                case ACCOUNT_SET_PRIVILEGE:
                    operation = AccountServiceImpl.setPrivilege((AccountSetPrivilegeOperation) operationBase[i]);
                    break;
                case ASSET_ISSUE:
                    operation = AssetServiceImpl.issue((AssetIssueOperation) operationBase[i]);
                    break;
                case ASSET_SEND:
                    operation = AssetServiceImpl.send((AssetSendOperation) operationBase[i], transSourceAddress);
                    break;
                case GAS_SEND:
                    operation = GasServiceImpl.send((GasSendOperation) operationBase[i], transSourceAddress);
                    break;
                case CONTRACT_CREATE:
                    operation = ContractServiceImpl.create((ContractCreateOperation) operationBase[i]);
                    break;
                case CONTRACT_INVOKE_BY_ASSET:
                    operation = ContractServiceImpl.invokeByAsset((ContractInvokeByAssetOperation) operationBase[i], transSourceAddress);
                    break;
                case CONTRACT_INVOKE_BY_GAS:
                    operation = ContractServiceImpl.invokeByGas((ContractInvokeByGasOperation) operationBase[i], transSourceAddress);
                    break;
                case LOG_CREATE:
                    operation = LogServiceImpl.create((LogCreateOperation) operationBase[i]);
                    break;
                default:
                    throw new SDKException(SdkError.OPERATIONS_ONE_ERROR);
            }
            if (Tools.isEmpty(operation)) {
                throw new SDKException(SdkError.OPERATIONS_ONE_ERROR);
            }
            transaction.addOperations(operation);
        }
    }

    public static TransactionGetInfoResponse getTransactionInfo(String hash) throws IOException, KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
        if (Tools.isEmpty(General.getInstance().getUrl())) {
            throw new SDKException(SdkError.URL_EMPTY_ERROR);
        }
        String getInfoUrl = General.getInstance().transactionGetInfoUrl(hash);
        String result = HttpKit.get(getInfoUrl);
        return JSONObject.parseObject(result, TransactionGetInfoResponse.class);
    }
}

