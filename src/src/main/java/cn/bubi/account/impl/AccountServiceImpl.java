package cn.bubi.account.impl;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.ByteString;
import cn.bubi.account.AccountService;
import cn.bubi.common.Constant;
import cn.bubi.common.General;
import cn.bubi.common.Tools;
import cn.bubi.crypto.http.HttpKit;
import cn.bubi.crypto.protobuf.Chain;
import cn.bubi.encryption.exception.EncException;
import cn.bubi.encryption.key.PrivateKey;
import cn.bubi.encryption.key.PublicKey;
import cn.bubi.exception.SDKException;
import cn.bubi.exception.SdkError;
import cn.bubi.model.request.*;
import cn.bubi.model.request.operation.AccountActivateOperation;
import cn.bubi.model.request.operation.AccountSetMetadataOperation;
import cn.bubi.model.request.operation.AccountSetPrivilegeOperation;
import cn.bubi.model.response.*;
import cn.bubi.model.response.result.*;
import cn.bubi.model.response.result.data.AssetInfo;
import cn.bubi.model.response.result.data.MetadataInfo;
import cn.bubi.model.response.result.data.Signer;
import cn.bubi.model.response.result.data.TypeThreshold;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @Author riven
 * @Date 2018/7/3 12:34
 */

public class AccountServiceImpl implements AccountService {

    /**
     * @Author riven
     * @Method activate
     * @Params [accountActivateRequest]
     * @Return cn.bubi.model.response.AccountActivateResponse
     * @Date 2018/7/4 18:19
     */
    public static Chain.Operation activate(AccountActivateOperation accountActivateOperation, String transSourceAddress) throws SDKException {
        Chain.Operation operation;
        try {
            if (Tools.isEmpty(accountActivateOperation)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String sourceAddress = accountActivateOperation.getSourceAddress();
            if (!Tools.isEmpty(sourceAddress) && !PublicKey.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            String destAddress = accountActivateOperation.getDestAddress();
            if (!PublicKey.isAddressValid(destAddress)) {
                throw new SDKException(SdkError.INVALID_DESTADDRESS_ERROR);
            }
            boolean isNotValid = (!Tools.isEmpty(sourceAddress) && sourceAddress.equals(destAddress)) || transSourceAddress.equals(destAddress);
            if (isNotValid) {
                throw new SDKException(SdkError.SOURCEADDRESS_EQUAL_DESTADDRESS_ERROR);
            }
            Long initBalance = accountActivateOperation.getInitBalance();
            if (Tools.isEmpty(initBalance) || initBalance <= 0) {
                throw new SDKException(SdkError.INVALID_INITBALANCE_ERROR);
            }
            String metadata = accountActivateOperation.getMetadata();
            // build operation
            operation = buildActivateOperation(sourceAddress, destAddress, initBalance, metadata);
        } catch (SDKException sdkException) {
            throw sdkException;
        } catch (Exception e) {
            throw new SDKException(SdkError.SYSTEM_ERROR.getCode(), e.getMessage());
        }

        return operation;
    }

    /**
     * @Author riven
     * @Method setMetadata
     * @Params [accountSetMetadataRequest]
     * @Return cn.bubi.model.response.AccountSetMetadataResponse
     * @Date 2018/7/4 18:27
     */
    public static Chain.Operation setMetadata(AccountSetMetadataOperation accountSetMetadataOperation) throws SDKException {
        Chain.Operation.Builder operation;
        try {
            String sourceAddress = accountSetMetadataOperation.getSourceAddress();
            if (!Tools.isEmpty(sourceAddress) && !PublicKey.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            String key = accountSetMetadataOperation.getKey();
            if (Tools.isEmpty(key) || key.length() > Constant.METADATA_KEY_MAX) {
                throw new SDKException(SdkError.INVALID_DATAKEY_ERROR);
            }
            String value = accountSetMetadataOperation.getValue();
            if (Tools.isEmpty(value) || value.length() > Constant.METADATA_VALUE_MAX) {
                throw new SDKException(SdkError.INVALID_DATAVALUE_ERROR);
            }
            Long version = accountSetMetadataOperation.getVersion();
            if (!Tools.isNULL(version) && version < 0) {
                throw new SDKException(SdkError.INVALID_DATAVERSION_ERROR);
            }
            Boolean deleteFlag = accountSetMetadataOperation.getDeleteFlag();
            String metadata = accountSetMetadataOperation.getMetadata();
            // build operation
            operation = Chain.Operation.newBuilder();
            operation.setType(Chain.Operation.Type.SET_METADATA);
            if (!Tools.isEmpty(sourceAddress)) {
                operation.setSourceAddress(sourceAddress);
            }
            if (!Tools.isEmpty(metadata)) {
                operation.setMetadata(ByteString.copyFromUtf8(metadata));
            }
            Chain.OperationSetMetadata.Builder operationSetMetadata = operation.getSetMetadataBuilder();
            operationSetMetadata.setKey(key);
            operationSetMetadata.setValue(value);
            if (!Tools.isEmpty(version) && version > 0) {
                operationSetMetadata.setVersion(version);
            }
            if (!Tools.isEmpty(deleteFlag) && deleteFlag != false) {
                operationSetMetadata.setDeleteFlag(deleteFlag);
            }
        } catch (SDKException sdkException) {
            throw sdkException;
        } catch (Exception e) {
            throw new SDKException(SdkError.SYSTEM_ERROR.getCode(), e.getMessage());
        }

        return operation.build();
    }

    /**
     * @Author riven
     * @Method setPrivilege
     * @Params [accountSetPrivilegeRequest]
     * @Return cn.bubi.model.response.AccountSetPrivilegeResponse
     * @Date 2018/7/5 00:45
     */
    public static Chain.Operation setPrivilege(AccountSetPrivilegeOperation accountSetPrivilegeOperation) throws SDKException {
        Chain.Operation operation;
        try {
            String sourceAddress = accountSetPrivilegeOperation.getSourceAddress();
            if (!Tools.isEmpty(sourceAddress) && !PublicKey.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            String masterWeight = accountSetPrivilegeOperation.getMasterWeight();
            if (!Tools.isEmpty(masterWeight)) {
                Pattern pattern = compile("^[-\\+]?[\\d]*$");
                boolean isNumber = pattern.matcher(masterWeight).matches();
                if (!isNumber || Long.valueOf(masterWeight) < 0 || Long.valueOf(masterWeight) > Constant.UINT_MAX) {
                    throw new SDKException(SdkError.INVALID_MASTERWEIGHT_ERROR);
                }
            }
            String txThreshold = accountSetPrivilegeOperation.getTxThreshold();
            if (!Tools.isEmpty(txThreshold)) {
                Pattern pattern = compile("^[-\\+]?[\\d]*$");
                boolean isNumber = pattern.matcher(txThreshold).matches();
                if (!isNumber || Long.valueOf(txThreshold) < 0) {
                    throw new SDKException(SdkError.INVALID_TX_THRESHOLD_ERROR);
                }
            }
            String metadata = accountSetPrivilegeOperation.getMetadata();
            // build operation
            Signer[] signers = accountSetPrivilegeOperation.getSigners();
            TypeThreshold[] typeThresholds = accountSetPrivilegeOperation.getTypeThresholds();
            operation = buildSetPrivilegeOperation(sourceAddress, masterWeight, txThreshold, signers, typeThresholds, metadata);
        } catch (SDKException sdkException) {
            throw sdkException;
        } catch (NumberFormatException exception) {
            throw new SDKException(SdkError.INVALID_TX_THRESHOLD_ERROR);
        } catch (Exception e) {
            throw new SDKException(SdkError.SYSTEM_ERROR.getCode(), e.getMessage());
        }
        return operation;
    }

    /**
     * @Author riven
     * @Method checkValid
     * @Params [accountCheckValidRequest]
     * @Return AccountCheckValidResponse
     * @Date 2018/7/4 18:19
     */
    @Override
    public AccountCheckValidResponse checkValid(AccountCheckValidRequest accountCheckValidRequest) {
        AccountCheckValidResponse accountCheckValidResponse = new AccountCheckValidResponse();
        AccountCheckValidResult accountCheckValidResult = new AccountCheckValidResult();
        boolean isValid;
        try {
            isValid = PublicKey.isAddressValid(accountCheckValidRequest.getAddress());
        } catch (EncException e) {
            isValid = false;
        }

        try {
            if (null == accountCheckValidRequest) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            // invoke the private method setValid
            Class cls = accountCheckValidResult.getClass();
            Method method = cls.getDeclaredMethod("setValid", new Class[]{boolean.class});
            method.setAccessible(true);
            method.invoke(accountCheckValidResult, isValid);
            accountCheckValidResponse.buildResponse(SdkError.SUCCESS, accountCheckValidResult);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            accountCheckValidResponse.buildResponse(errorCode, errorDesc, accountCheckValidResult);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            accountCheckValidResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), accountCheckValidResult);
        }
        return accountCheckValidResponse;
    }

    /**
     * @Author riven
     * @Method create
     * @Params []
     * @Return AccountCreateResponse
     * @Date 2018/7/4 18:19
     */
    @Override
    public AccountCreateResponse create() {
        AccountCreateResponse accountCreateResponse = new AccountCreateResponse();

        AccountCreateResult accountCreateResult = new AccountCreateResult();
        try {
            PrivateKey privateKey = new PrivateKey();
            accountCreateResult.setPrivateKey(privateKey.getEncPrivateKey());
            accountCreateResult.setPublicKey(privateKey.getEncPublicKey());
            accountCreateResult.setAddress(privateKey.getEncAddress());
            accountCreateResponse.buildResponse(SdkError.SUCCESS, accountCreateResult);
        } catch (Exception e) {
            accountCreateResponse.buildResponse(SdkError.ACCOUNT_CREATE_ERROR, accountCreateResult);
        }
        return accountCreateResponse;
    }

    /**
     * @Author riven
     * @Method getInfo
     * @Params [accountGetInfoRequest]
     * @Return AccountGetInfoResponse
     * @Date 2018/7/4 18:19
     */
    @Override
    public AccountGetInfoResponse getInfo(AccountGetInfoRequest accountGetInfoRequest) {
        AccountGetInfoResponse accountGetInfoResponse = new AccountGetInfoResponse();

        AccountGetInfoResult accountGetInfoResult = new AccountGetInfoResult();
        try {
            if (Tools.isEmpty(accountGetInfoRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String address = accountGetInfoRequest.getAddress();
            if (!PublicKey.isAddressValid(address)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            accountGetInfoResponse = getInfo(address);
            Integer errorCode = accountGetInfoResponse.getErrorCode();
            String errorDesc = accountGetInfoResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode == 4) {
                throw new SDKException(errorCode, (null == errorDesc ? "Account (" + address + ") not exist" : errorDesc));
            }
            SdkError.checkErrorCode(accountGetInfoResponse);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            accountGetInfoResponse.buildResponse(errorCode, errorDesc, accountGetInfoResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            accountGetInfoResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, accountGetInfoResult);
        } catch (Exception e) {
            accountGetInfoResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), accountGetInfoResult);
        }

        return accountGetInfoResponse;
    }

    /**
     * @Author riven
     * @Method getNonce
     * @Params [accountGetNonceRequest]
     * @Return AccountGetNonceResponse
     * @Date 2018/7/4 18:19
     */
    @Override
    public AccountGetNonceResponse getNonce(AccountGetNonceRequest accountGetNonceRequest) {
        AccountGetNonceResponse accountGetNonceResponse = new AccountGetNonceResponse();

        AccountGetNonceResult accountGetNonceResult = new AccountGetNonceResult();
        try {
            if (Tools.isEmpty(accountGetNonceRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String address = accountGetNonceRequest.getAddress();
            if (!PublicKey.isAddressValid(address)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            String accountGetInfoUrl = General.getInstance().accountGetInfoUrl(address);
            String result = HttpKit.get(accountGetInfoUrl);
            accountGetNonceResponse = JSON.parseObject(result, AccountGetNonceResponse.class);
            Integer errorCode = accountGetNonceResponse.getErrorCode();
            String errorDesc = accountGetNonceResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode == 4) {
                throw new SDKException(errorCode, (null == errorDesc ? "Account (" + address + ") not exist" : errorDesc));
            }
            SdkError.checkErrorCode(accountGetNonceResponse);
            if (accountGetNonceResponse.getResult().getNonce() == null) {
                accountGetNonceResponse.getResult().setNonce(0L);
            }
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            accountGetNonceResponse.buildResponse(errorCode, errorDesc, accountGetNonceResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            accountGetNonceResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, accountGetNonceResult);
        } catch (Exception e) {
            accountGetNonceResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), accountGetNonceResult);
        }

        return accountGetNonceResponse;
    }

    /**
     * @Author riven
     * @Method getBalance
     * @Params [accountGetBalanceRequest]
     * @Return AccountGetBalanceResponse
     * @Date 2018/7/4 18:19
     */
    @Override
    public AccountGetBalanceResponse getBalance(AccountGetBalanceRequest accountGetBalanceRequest) {
        AccountGetBalanceResponse accountGetBalanceResponse = new AccountGetBalanceResponse();
        AccountGetBalanceResult accountGetBalanceResult = new AccountGetBalanceResult();
        try {
            if (Tools.isEmpty(accountGetBalanceRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String address = accountGetBalanceRequest.getAddress();
            if (!PublicKey.isAddressValid(address)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            String accountGetInfoUrl = General.getInstance().accountGetInfoUrl(address);
            String result = HttpKit.get(accountGetInfoUrl);
            accountGetBalanceResponse = JSON.parseObject(result, AccountGetBalanceResponse.class);
            Integer errorCode = accountGetBalanceResponse.getErrorCode();
            String errorDesc = accountGetBalanceResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode == 4) {
                throw new SDKException(errorCode, (null == errorDesc ? "Account (" + address + ") not exist" : errorDesc));
            }
            SdkError.checkErrorCode(accountGetBalanceResponse);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            accountGetBalanceResponse.buildResponse(errorCode, errorDesc, accountGetBalanceResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            accountGetBalanceResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, accountGetBalanceResult);
        } catch (Exception e) {
            accountGetBalanceResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), accountGetBalanceResult);
        }

        return accountGetBalanceResponse;
    }

    /**
     * @Author riven
     * @Method getAssets
     * @Params [accountGetAssetsRequest]
     * @Return AccountGetAssetsResponse
     * @Date 2018/7/5 12:00
     */
    @Override
    public AccountGetAssetsResponse getAssets(AccountGetAssetsRequest accountGetAssetsRequest) {
        AccountGetAssetsResponse accountGetAssetsResponse = new AccountGetAssetsResponse();
        AccountGetAssetsResult accountGetAssetsResult = new AccountGetAssetsResult();
        try {
            if (Tools.isEmpty(accountGetAssetsRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String address = accountGetAssetsRequest.getAddress();
            if (!PublicKey.isAddressValid(address)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            String accountGetInfoUrl = General.getInstance().accountGetAssetsUrl(address);
            String result = HttpKit.get(accountGetInfoUrl);
            accountGetAssetsResponse = JSON.parseObject(result, AccountGetAssetsResponse.class);
            Integer errorCode = accountGetAssetsResponse.getErrorCode();
            String errorDesc = accountGetAssetsResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode == 4) {
                throw new SDKException(errorCode, (null == errorDesc ? "Account (" + address + ") not exist" : errorDesc));
            }
            SdkError.checkErrorCode(accountGetAssetsResponse);
            AssetInfo[] assetInfos = accountGetAssetsResponse.getResult().getAssets();
            if (Tools.isEmpty(assetInfos)) {
                throw new SDKException(SdkError.NO_ASSET_ERROR);
            }
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            accountGetAssetsResponse.buildResponse(errorCode, errorDesc, accountGetAssetsResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            accountGetAssetsResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, accountGetAssetsResult);
        } catch (Exception e) {
            accountGetAssetsResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), accountGetAssetsResult);
        }

        return accountGetAssetsResponse;
    }

    /**
     * @Author riven
     * @Method getMetadata
     * @Params [accountGetMetadataRequest]
     * @Return AccountGetMetadataResponse
     * @Date 2018/7/15 15:03
     */
    @Override
    public AccountGetMetadataResponse getMetadata(AccountGetMetadataRequest accountGetMetadataRequest) {
        AccountGetMetadataResponse accountGetMetadataResponse = new AccountGetMetadataResponse();
        AccountGetMetadataResult accountGetMetadataResult = new AccountGetMetadataResult();
        try {
            if (Tools.isEmpty(accountGetMetadataRequest)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String address = accountGetMetadataRequest.getAddress();
            if (!PublicKey.isAddressValid(address)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            String key = accountGetMetadataRequest.getKey();
            if (!Tools.isNULL(key) && (key.length() > Constant.METADATA_KEY_MAX || key.length() < 1)) {
                throw new SDKException(SdkError.INVALID_DATAKEY_ERROR);
            }
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            String accountGetInfoUrl = General.getInstance().accountGetMetadataUrl(address, key);
            String result = HttpKit.get(accountGetInfoUrl);
            accountGetMetadataResponse = JSON.parseObject(result, AccountGetMetadataResponse.class);
            Integer errorCode = accountGetMetadataResponse.getErrorCode();
            String errorDesc = accountGetMetadataResponse.getErrorDesc();
            if (!Tools.isEmpty(errorCode) && errorCode == 4) {
                throw new SDKException(errorCode, (null == errorDesc ? "Account (" + address + ") not exist" : errorDesc));
            }
            SdkError.checkErrorCode(accountGetMetadataResponse);
            MetadataInfo[] metadataInfos = accountGetMetadataResponse.getResult().getMetadatas();
            if (Tools.isEmpty(metadataInfos)) {
                throw new SDKException(SdkError.NO_METADATA_ERROR);
            }
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            accountGetMetadataResponse.buildResponse(errorCode, errorDesc, accountGetMetadataResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            accountGetMetadataResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, accountGetMetadataResult);
        } catch (Exception e) {
            accountGetMetadataResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), accountGetMetadataResult);
        }

        return accountGetMetadataResponse;
    }

    /**
     * @Author riven
     * @Method checkActivated
     * @Params [accountCheckActivatedRequst]
     * @Return AccountCheckActivatedResponse
     * @Date 2018/8/6 17:44
     */
    @Override
    public AccountCheckActivatedResponse checkActivated(AccountCheckActivatedRequst accountCheckActivatedRequst) {
        AccountCheckActivatedResponse accountCheckActivatedResponse = new AccountCheckActivatedResponse();
        AccountCheckActivatedResult accountCheckActivatedResult = new AccountCheckActivatedResult();
        try {
            if (Tools.isEmpty(accountCheckActivatedRequst)) {
                throw new SDKException(SdkError.REQUEST_NULL_ERROR);
            }
            String address = accountCheckActivatedRequst.getAddress();
            if (!PublicKey.isAddressValid(address)) {
                throw new SDKException(SdkError.INVALID_ADDRESS_ERROR);
            }
            if (Tools.isEmpty(General.getInstance().getUrl())) {
                throw new SDKException(SdkError.URL_EMPTY_ERROR);
            }
            accountCheckActivatedResult.setIsActivated(isActivated(address));
            accountCheckActivatedResponse.buildResponse(SdkError.SUCCESS, accountCheckActivatedResult);
        } catch (SDKException apiException) {
            Integer errorCode = apiException.getErrorCode();
            String errorDesc = apiException.getErrorDesc();
            accountCheckActivatedResponse.buildResponse(errorCode, errorDesc, accountCheckActivatedResult);
        } catch (NoSuchAlgorithmException | KeyManagementException | NoSuchProviderException | IOException e) {
            accountCheckActivatedResponse.buildResponse(SdkError.CONNECTNETWORK_ERROR, accountCheckActivatedResult);
        } catch (Exception e) {
            accountCheckActivatedResponse.buildResponse(SdkError.SYSTEM_ERROR.getCode(), e.getMessage(), accountCheckActivatedResult);
        }
        return accountCheckActivatedResponse;
    }

    public static Chain.Operation buildActivateOperation(String sourceAddress,
        String destAddress, Long initBalance, String metadata) {
        Chain.Operation.Builder operation = Chain.Operation.newBuilder();
        operation.setType(Chain.Operation.Type.CREATE_ACCOUNT);
        if (!Tools.isEmpty(sourceAddress)) {
            operation.setSourceAddress(sourceAddress);
        }
        if (!Tools.isEmpty(metadata)) {
            operation.setMetadata(ByteString.copyFromUtf8(metadata));
        }
        Chain.OperationCreateAccount.Builder operationCreateAccount = operation.getCreateAccountBuilder();
        operationCreateAccount.setDestAddress(destAddress);
        operationCreateAccount.setInitBalance(initBalance);
        Chain.AccountPrivilege.Builder accountPrivilege = operationCreateAccount.getPrivBuilder();
        accountPrivilege.setMasterWeight(1);
        Chain.AccountThreshold.Builder accountThreshold = accountPrivilege.getThresholdsBuilder();
        accountThreshold.setTxThreshold(1);
        return operation.build();
    }

    public static Chain.Operation buildSetPrivilegeOperation(String sourceAddress, String masterWeight,
        String txThreshold, Signer[] signers, TypeThreshold[] typeThresholds, String metadata) {
        Chain.Operation.Builder operation = Chain.Operation.newBuilder();
        operation.setType(Chain.Operation.Type.SET_PRIVILEGE);
        if (!Tools.isEmpty(sourceAddress)) {
            operation.setSourceAddress(sourceAddress);
        }
        if (!Tools.isEmpty(metadata)) {
            operation.setMetadata(ByteString.copyFromUtf8(metadata));
        }
        Chain.OperationSetPrivilege.Builder operationSetPrivilege = operation.getSetPrivilegeBuilder();
        if (!Tools.isEmpty(masterWeight)) {
            operationSetPrivilege.setMasterWeight(masterWeight);
        }
        if (!Tools.isEmpty(txThreshold)) {
            operationSetPrivilege.setTxThreshold(txThreshold);
        }

        // add signers
        if (!Tools.isEmpty(signers)) {
            int i = 0;
            int signersLength = signers.length;
            for (; i < signersLength; i++) {
                Signer signer = signers[i];
                String signerAddress = signer.getAddress();
                if (!PublicKey.isAddressValid(signerAddress)) {
                    throw new SDKException(SdkError.INVALID_SIGNER_ADDRESS_ERROR);
                }
                Long signerWeight = signer.getWeight();
                if (Tools.isEmpty(signerWeight) || signerWeight < 0 || signerWeight > Constant.UINT_MAX) {
                    throw new SDKException(SdkError.INVALID_SIGNER_WEIGHT_ERROR);
                }
                Chain.Signer.Builder signerBuilder = operationSetPrivilege.addSignersBuilder();
                signerBuilder.setAddress(signerAddress);
                signerBuilder.setWeight(signerWeight);
            }
        }

        // add type_thresholds
        if (!Tools.isEmpty(typeThresholds)) {
            int i = 0;
            int typeThresholdsLength = typeThresholds.length;
            for (; i < typeThresholdsLength; i++) {
                TypeThreshold typeThreshold = typeThresholds[i];
                Integer type = typeThreshold.getType();
                if (Tools.isEmpty(type) || type < 1) {
                    throw new SDKException(SdkError.INVALID_TYPETHRESHOLD_TYPE_ERROR);
                }
                Long threshold = typeThreshold.getThreshold();
                if (Tools.isEmpty(threshold) || threshold < 0) {
                    throw new SDKException(SdkError.INVALID_TYPE_THRESHOLD_ERROR);
                }
                Chain.OperationTypeThreshold.Builder typeThresholdBuilder = operationSetPrivilege.addTypeThresholdsBuilder();
                Chain.Operation.Type operationType = Chain.Operation.Type.forNumber(type);
                if (Tools.isEmpty(operationType)) {
                    throw new SDKException(SdkError.INVALID_TYPETHRESHOLD_TYPE_ERROR);
                }
                typeThresholdBuilder.setType(operationType);
                typeThresholdBuilder.setThreshold(threshold);
            }
        }
        return operation.build();
    }

    public static Boolean isActivated(String address) throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        AccountGetInfoResponse accountGetInfoResponse = getInfo(address);
        Integer errorCode = accountGetInfoResponse.getErrorCode();
        if (!Tools.isEmpty(errorCode) && errorCode == 4) {
            return false;
        }
        SdkError.checkErrorCode(accountGetInfoResponse);
        return true;
    }

    private static AccountGetInfoResponse getInfo(String address) throws IOException, KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
        String accountGetInfoUrl = General.getInstance().accountGetInfoUrl(address);
        String result = HttpKit.get(accountGetInfoUrl);
        return JSON.parseObject(result, AccountGetInfoResponse.class);
    }
}
