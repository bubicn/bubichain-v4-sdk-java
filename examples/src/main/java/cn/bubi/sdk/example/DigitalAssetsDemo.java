package cn.bubi.sdk.example;

import cn.bubi.crypto.protobuf.Chain;
import com.alibaba.fastjson.JSON;
import com.google.protobuf.InvalidProtocolBufferException;
import com.googlecode.protobuf.format.JsonFormat;
import cn.bubi.SDK;
import cn.bubi.common.ToBaseUnit;
import cn.bubi.crypto.Keypair;
import cn.bubi.encryption.key.PrivateKey;
import cn.bubi.encryption.utils.hex.HexFormat;
import cn.bubi.model.request.*;
import cn.bubi.model.request.operation.*;
import cn.bubi.model.response.*;
import cn.bubi.model.response.result.*;
import cn.bubi.model.response.result.data.Signature;
import cn.bubi.model.response.result.data.Signer;
import cn.bubi.model.response.result.data.TransactionFees;
import org.junit.Test;

/**
 * @author riven
 * @date 2018/7/15 14:32
 */

public class DigitalAssetsDemo {
    SDK sdk = SDK.getInstance("http://192.168.10.130:46002");

    @Test
    public void SDKConfigure() {
        SDKConfigure sdkConfigure = new SDKConfigure();
        sdkConfigure.setHttpConnectTimeOut(5000);
        sdkConfigure.setHttpReadTimeOut(5000);
        sdkConfigure.setChainId(9);
        sdkConfigure.setUrl("http://node.bubidev.cn");
        sdk = SDK.getInstance(sdkConfigure);
    }

    @Test
    public void checkAccountActivated() {
        AccountCheckActivatedRequst request = new AccountCheckActivatedRequst();
        request.setAddress("adxSB2QBLfrMoRs3wRZdXvireup2yypgEC7Vc");

        AccountCheckActivatedResponse response = sdk.getAccountService().checkActivated(request);
        if (response.getErrorCode() == 0) {
            System.out.println("account (adxSB2QBLfrMoRs3wRZdXvireup2yypgEC7Vc) is activated");
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Check whether the nodes in the connection are block synchronously
     */
    @Test
    public void checkBlockStatus() {
        BlockCheckStatusResponse response = sdk.getBlockService().checkStatus();
        System.out.println(response.getResult().getSynchronous());
    }

    /**
     * Generate an account private key, public key and address
     */
    @Test
    public void createAccount() {
        Keypair keypair = Keypair.generator();
        System.out.println(JSON.toJSONString(keypair, true));
    }

    /**
     * Check whether account address is valid
     */
    @Test
    public void checkAccountAddress() {
        // Init request
        String address = "adxSB2QBLfrMoRs3wRZdXvireup2yypgEC7Vc";
        AccountCheckValidRequest request = new AccountCheckValidRequest();
        request.setAddress(address);

        // Call checkValid
        AccountCheckValidResponse response = sdk.getAccountService().checkValid(request);
        if (0 == response.getErrorCode()) {
            System.out.println(response.getResult().isValid());
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get account info
     */
    @Test
    public void getAccountInfo() {
        // Init request
        String accountAddress = "adxSB2QBLfrMoRs3wRZdXvireup2yypgEC7Vc";
        AccountGetInfoRequest request = new AccountGetInfoRequest();
        request.setAddress(accountAddress);

        // Call getInfo
        AccountGetInfoResponse response = sdk.getAccountService().getInfo(request);
        if (response.getErrorCode() == 0) {
            AccountGetInfoResult result = response.getResult();
            System.out.println("账户信息: \n" + JSON.toJSONString(result, true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get account nonce
     */
    @Test
    public void getAccountNonce() {
        // Init request
        String accountAddress = "adxSB2QBLfrMoRs3wRZdXvireup2yypgEC7Vc";
        AccountGetNonceRequest request = new AccountGetNonceRequest();
        request.setAddress(accountAddress);

        // Call getNonce
        AccountGetNonceResponse response = sdk.getAccountService().getNonce(request);
        if (0 == response.getErrorCode()) {
            System.out.println("账户nonce:" + response.getResult().getNonce());
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get account Gas balance
     */
    @Test
    public void getAccountBalance() {
        // Init request
        String accountAddress = "adxSB2QBLfrMoRs3wRZdXvireup2yypgEC7Vc";
        AccountGetBalanceRequest request = new AccountGetBalanceRequest();
        request.setAddress(accountAddress);

        // Call getBalance
        AccountGetBalanceResponse response = sdk.getAccountService().getBalance(request);
        if (0 == response.getErrorCode()) {
            AccountGetBalanceResult result = response.getResult();
            System.out.println("Gas余额：" + ToBaseUnit.ToGas(result.getBalance().toString()) + " Gas");
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Query all assets under the specified account
     */
    @Test
    public void getAccountAssets() {
        // Init request
        AccountGetAssetsRequest request = new AccountGetAssetsRequest();
        request.setAddress("adxSB2QBLfrMoRs3wRZdXvireup2yypgEC7Vc");

        // Call getAssets
        AccountGetAssetsResponse response = sdk.getAccountService().getAssets(request);
        if (response.getErrorCode() == 0) {
            AccountGetAssetsResult result = response.getResult();
            System.out.println(JSON.toJSONString(result, true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get account metadata
     */
    @Test
    public void getAccountMetadata() {
        // Init request
        String accountAddress = "adxSB2QBLfrMoRs3wRZdXvireup2yypgEC7Vc";
        AccountGetMetadataRequest request = new AccountGetMetadataRequest();
        request.setAddress(accountAddress);
        request.setKey("20180704");

        // Call getMetadata
        AccountGetMetadataResponse response = sdk.getAccountService().getMetadata(request);
        if (response.getErrorCode() == 0) {
            AccountGetMetadataResult result = response.getResult();
            System.out.println(JSON.toJSONString(result, true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Query the specified assets of the specified account
     */
    @Test
    public void getAssetInfo() {
        // Init request
        AssetGetInfoRequest request = new AssetGetInfoRequest();
        request.setAddress("adxSB2QBLfrMoRs3wRZdXvireup2yypgEC7Vc");
        request.setIssuer("adxSB2QBLfrMoRs3wRZdXvireup2yypgEC7Vc");
        request.setCode("HNC");

        // Call getInfo
        AssetGetInfoResponse response = sdk.getAssetService().getInfo(request);
        if (response.getErrorCode() == 0) {
            AssetGetInfoResult result = response.getResult();
            System.out.println(JSON.toJSONString(result, true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Serialize the transaction and generate the transaction blob
     */
    @Test
    public void buildTransactionBlob() {
        SDKConfigure();

        // Init variable
        String senderAddresss = "adxSYvndiFG4zpbLGugX3j93fDn9nWZfWp8Gd";
        String destAddress = "adxScpCtbeLP2KGRaCkbtrmz8iB5mu6DQcW3r";
        Long amount = ToBaseUnit.ToUGas("10.9");
        Long gasPrice = 1000L;
        Long feeLimit = ToBaseUnit.ToUGas("0.01");
        Long nonce = 1L;

        // Build sendGas
        GasSendOperation operation = new GasSendOperation();
        operation.setSourceAddress(senderAddresss);
        operation.setDestAddress(destAddress);
        operation.setAmount(amount);

        // Init request
        TransactionBuildBlobRequest request = new TransactionBuildBlobRequest();
        request.setSourceAddress(senderAddresss);
        request.setNonce(nonce);
        request.setFeeLimit(feeLimit);
        request.setGasPrice(gasPrice);
        request.addOperation(operation);

        // Call buildBlob
        TransactionBuildBlobResponse response = sdk.getTransactionService().buildBlob(request);
        if (response.getErrorCode() == 0) {
            TransactionBuildBlobResult result = response.getResult();
            try {
                Chain.Transaction transaction = Chain.Transaction.parseFrom(HexFormat.hexToByte(result.getTransactionBlob()));
                System.out.println(transaction);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Evaluation of transaction costs
     */
    @Test
    public void evaluateTxFees() throws Exception {
        // Init variable
        // 发送方私钥
        String senderPrivateKey = "privbxd841cb7zoDPRZMLpFdaJSbUyX7oiP1BJk38PegLoJDftvLM285";
        // 接收方账户地址
        String destAddress = "adxSc2day174RHjnkfFPZExT5kKyFhHBKb8sc";
        // 发送转出10.9Gas给接收方（目标账户）
        Long amount = ToBaseUnit.ToUGas("10.9");
        // The fixed write 1000L, the unit is UGas
        Long gasPrice = 1000L;
        //Set up the maximum cost 0.01Gas
        Long feeLimit = ToBaseUnit.ToUGas("0.01");
        // Transaction initiation account's nonce + 1;
        Long nonce = 1L;

        // 评估费用
        TransactionFees transactionFees = evaluateFees(senderPrivateKey, destAddress, amount, nonce, gasPrice, feeLimit);
        System.out.println(JSON.toJSONString(transactionFees, true));
    }

    /**
     * Sign transaction
     */
    @Test
    public void signTransaction() {
        // Init request
        String issuePrivateKey = "privbxd841cb7zoDPRZMLpFdaJSbUyX7oiP1BJk38PegLoJDftvLM285";
        String[] signerPrivateKeyArr = {issuePrivateKey};
        String transactionBlob = "123";
        TransactionSignRequest request = new TransactionSignRequest();
        request.setBlob(transactionBlob);
        for (int i = 0; i < signerPrivateKeyArr.length; i++) {
            request.addPrivateKey(signerPrivateKeyArr[i]);
        }
        TransactionSignResponse response = sdk.getTransactionService().sign(request);
        if (0 == response.getErrorCode()) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Submit transaction
     */
    @Test
    public void submitTransaction() {
        // Init request
        String transactionBlob = "0A246275516E6E5545425245773268423670574847507A77616E5837643238786B364B566370102118C0843D20E8073A56080712246275516E6E5545425245773268423670574847507A77616E5837643238786B364B566370522C0A24627551426A4A443142534A376E7A41627A6454656E416870466A6D7852564545746D78481080A9E08704";
        Signature signature = new Signature();
        signature.setSignData("D2B5E3045F2C1B7D363D4F58C1858C30ABBBB0F41E4B2E18AF680553CA9C3689078E215C097086E47A4393BCA715C7A5D2C180D8750F35C6798944F79CC5000A");
        signature.setPublicKey("b0011765082a9352e04678ef38d38046dc01306edef676547456c0c23e270aaed7ffe9e31477");
        TransactionSubmitRequest request = new TransactionSubmitRequest();
        request.setTransactionBlob(transactionBlob);
        request.addSignature(signature);

        // Call submit
        TransactionSubmitResponse response = sdk.getTransactionService().submit(request);
        if (0 == response.getErrorCode()) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get transaction information according to the transaction Hash
     */
    @Test
    public void getTxByHash() {
        String txHash = "eabaa7934c50d89c1debe8aeffa53733ed50e84d9e4d957b7596d9411c62ea3f";
        // Init request
        TransactionGetInfoRequest request = new TransactionGetInfoRequest();
        request.setHash(txHash);

        // Call getInfo
        TransactionGetInfoResponse response = sdk.getTransactionService().getInfo(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Probe user recharge
     * <p>
     * Through the analysis of transactions under the block, to detect the user's charging action
     */
    @Test
    public void getTransactionOfBolck() {
        // Init request
        Long blockNumber = 987032L;
        BlockGetTransactionsRequest request = new BlockGetTransactionsRequest();
        request.setBlockNumber(blockNumber);

        // Call getTransactions
        BlockGetTransactionsResponse response = sdk.getBlockService().getTransactions(request);
        if (0 == response.getErrorCode()) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get the latest block number
     */
    @Test
    public void getLastBlockNumber() {
        // Call getNumber
        BlockGetNumberResponse response = sdk.getBlockService().getNumber();
        if (0 == response.getErrorCode()) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get the block info according to the specified block number
     */
    @Test
    public void getBlockInfo() {
        // Init request
        BlockGetInfoRequest request = new BlockGetInfoRequest();
        request.setBlockNumber(629743L);

        // Call getInfo
        BlockGetInfoResponse response = sdk.getBlockService().getInfo(request);
        if (response.getErrorCode() == 0) {
            BlockGetInfoResult result = response.getResult();
            System.out.println(JSON.toJSONString(result, true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get latest block info
     */
    @Test
    public void getBlockLatestInfo() {
        // Call getLatestInfo
        BlockGetLatestInfoResponse response = sdk.getBlockService().getLatestInfo();
        if (response.getErrorCode() == 0) {
            BlockGetLatestInfoResult result = response.getResult();
            System.out.println(JSON.toJSONString(result, true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get the authentication node information of the specified block height
     */
    @Test
    public void getBlockValidators() {
        // Init request
        BlockGetValidatorsRequest request = new BlockGetValidatorsRequest();
        //request.setBlockNumber(629743L);

        // Call getValidators
        BlockGetValidatorsResponse response = sdk.getBlockService().getValidators(request);
        if (response.getErrorCode() == 0) {
            BlockGetValidatorsResult result = response.getResult();
            System.out.println(JSON.toJSONString(result, true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get the validation node information of the latest block
     */
    @Test
    public void getLatestBlockValidators() {
        BlockGetLatestValidatorsResponse lockGetLatestValidatorsResponse = sdk.getBlockService().getLatestValidators();
        if (lockGetLatestValidatorsResponse.getErrorCode() == 0) {
            BlockGetLatestValidatorsResult lockGetLatestValidatorsResult = lockGetLatestValidatorsResponse.getResult();
            System.out.println(JSON.toJSONString(lockGetLatestValidatorsResult, true));
        } else {
            System.out.println("error: " + lockGetLatestValidatorsResponse.getErrorDesc());
        }
    }

    /**
     * Get block award and verification node reward for specifying block height
     */
    @Test
    public void getBlockReward() {
        // Init request
        BlockGetRewardRequest request = new BlockGetRewardRequest();
        request.setBlockNumber(629743L);

        // Call getReward
        BlockGetRewardResponse response = sdk.getBlockService().getReward(request);
        if (response.getErrorCode() == 0) {
            BlockGetRewardResult result = response.getResult();
            System.out.println(JSON.toJSONString(result, true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get the latest block award and verification node Awards
     */
    @Test
    public void getLatestBlockReward() {
        // Call getLatestReward
        BlockGetLatestRewardResponse response = sdk.getBlockService().getLatestReward();
        if (response.getErrorCode() == 0) {
            BlockGetLatestRewardResult result = response.getResult();
            System.out.println(JSON.toJSONString(result, true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get the cost standard for the specified block
     */
    @Test
    public void getBlockFees() {
        // Init request
        BlockGetFeesRequest request = new BlockGetFeesRequest();
        request.setBlockNumber(629743L);

        // Call getFees
        BlockGetFeesResponse response = sdk.getBlockService().getFees(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get the cost standard for the latest block
     */
    @Test
    public void getBlockLatestFees() {
        // Call getLatestFees
        BlockGetLatestFeesResponse response = sdk.getBlockService().getLatestFees();
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Activate a new account
     */
    @Test
    public void activateAccount() {
        // The account private key to activate a new account
        String activatePrivateKey = "privbxd841cb7zoDPRZMLpFdaJSbUyX7oiP1BJk38PegLoJDftvLM285";
        Long initBalance = ToBaseUnit.ToUGas("1000");
        // The fixed write 1000L, the unit is UGas
        Long gasPrice = 1000L;
        // Set up the maximum cost 0.01Gas
        Long feeLimit = ToBaseUnit.ToUGas("0.01");
        // Transaction initiation account's nonce + 1
        Long nonce = 1L;

        // Generate a new account to be activated
        Keypair keypair = Keypair.generator();
        System.out.println(JSON.toJSONString(keypair, true));
        String destAccount = keypair.getAddress();

        // 1. Get the account address to send this transaction
        String activateAddresss = getAddressByPrivateKey(activatePrivateKey);

        // 2. Build activateAccount
        AccountActivateOperation operation = new AccountActivateOperation();
        operation.setSourceAddress(activateAddresss);
        operation.setDestAddress(destAccount);
        operation.setInitBalance(initBalance);
        operation.setMetadata("activate account");

        String[] signerPrivateKeyArr = {activatePrivateKey};
        // Record txhash for subsequent confirmation of the real result of the transaction.
        // After recommending five blocks, call again through txhash `Get the transaction information
        // from the transaction Hash'(see example: getTxByHash ()) to confirm the final result of the transaction
        String txHash = submitTransaction(signerPrivateKeyArr, activateAddresss, operation, nonce, gasPrice, feeLimit);
        if (txHash != null) {
            System.out.println("hash: " + txHash);
        }
    }


    /**
     * Set account metadata
     */
    @Test
    public void setAccountMetadata() {
        // Init variable
        // The account private key to set metadata
        String accountPrivateKey = "privbyQCRp7DLqKtRFCqKQJr81TurTqG6UKXMMtGAmPG3abcM9XHjWvq";
        // The metadata key
        String key = "test  ";
        // The metadata value
        String value = "asdfasdfa";
        // The fixed write 1000L, the unit is UGas
        Long gasPrice = 1000L;
        //Set up the maximum cost 0.01Gas
        Long feeLimit = ToBaseUnit.ToUGas("0.01");
        // Transaction initiation account's nonce + 1
        Long nonce = 6L;

        // 1. Get the account address to send this transaction
        String accountAddresss = getAddressByPrivateKey(accountPrivateKey);

        // 2. Build setMetadata
        AccountSetMetadataOperation operation = new AccountSetMetadataOperation();
        operation.setSourceAddress(accountAddresss);
        operation.setKey(key);
        operation.setValue(value);

        String[] signerPrivateKeyArr = {accountPrivateKey};
        // Record txhash for subsequent confirmation of the real result of the transaction.
        // After recommending five blocks, call again through txhash `Get the transaction information
        // from the transaction Hash'(see example: getTxByHash ()) to confirm the final result of the transaction
        String txHash = submitTransaction(signerPrivateKeyArr, accountAddresss, operation, nonce, gasPrice, feeLimit);
        if (txHash != null) {
            System.out.println("hash: " + txHash);
        }
    }

    /**
     * Set account privilege
     */
    @Test
    public void setAccountPrivilege() {
        // Init variable
        // The account private key to set privilege
        String accountPrivateKey = "privbyQCRp7DLqKtRFCqKQJr81TurTqG6UKXMMtGAmPG3abcM9XHjWvq";
        // The fixed write 1000L, the unit is UGas
        Long gasPrice = 1000L;
        // Set up the maximum cost 0.01Gas
        Long feeLimit = ToBaseUnit.ToUGas("0.01");
        // Transaction initiation account's nonce + 1
        Long nonce = 6L;

        // 1. Get the account address to send this transaction
        String accountAddresss = getAddressByPrivateKey(accountPrivateKey);

        // 2. Build setPrivilege
        AccountSetPrivilegeOperation operation = new AccountSetPrivilegeOperation();
        operation.setSourceAddress(accountAddresss);
        Signer signer2 = new Signer();
        signer2.setAddress("adxScpCtbeLP2KGRaCkbtrmz8iB5mu6DQcW3r");
        signer2.setWeight(2L);
        operation.addSigner(signer2);
        operation.setTxThreshold("1");

        String[] signerPrivateKeyArr = {accountPrivateKey};
        // Record txhash for subsequent confirmation of the real result of the transaction.
        // After recommending five blocks, call again through txhash `Get the transaction information
        // from the transaction Hash'(see example: getTxByHash ()) to confirm the final result of the transaction
        String txHash = submitTransaction(signerPrivateKeyArr, accountAddresss, operation, nonce, gasPrice, feeLimit);
        if (txHash != null) {
            System.out.println("hash: " + txHash);
        }
    }

    /**
     * Issue asset
     */
    @Test
    public void issueAsset() {
        // Init variable
        // The account private key to issue asset
        String issuePrivateKey = "privbyQCRp7DLqKtRFCqKQJr81TurTqG6UKXMMtGAmPG3abcM9XHjWvq";
        // Asset code
        String assetCode = "TST";
        // Asset amount
        Long assetAmount = 10000000000000L;
        // metadata
        String metadata = "issue TST";
        // The fixed write 1000L, the unit is UGas
        Long gasPrice = 1000L;
        // Set up the maximum cost 50.01Gas
        Long feeLimit = ToBaseUnit.ToUGas("50.01");
        // Transaction initiation account's nonce + 1
        Long nonce = 7L;

        // 1. Get the account address to send this transaction
        String issueAddresss = getAddressByPrivateKey(issuePrivateKey);

        // 2. Build issueAsset
        AssetIssueOperation assetIssueOperation = new AssetIssueOperation();
        assetIssueOperation.setSourceAddress(issueAddresss);
        assetIssueOperation.setCode(assetCode);
        assetIssueOperation.setAmount(assetAmount);
        assetIssueOperation.setMetadata(metadata);


        String[] signerPrivateKeyArr = {issuePrivateKey};
        // Record txhash for subsequent confirmation of the real result of the transaction.
        // After recommending five blocks, call again through txhash `Get the transaction information
        // from the transaction Hash'(see example: getTxByHash ()) to confirm the final result of the transaction
        String txHash = submitTransaction(signerPrivateKeyArr, issueAddresss, assetIssueOperation, nonce, gasPrice, feeLimit);
        if (txHash != null) {
            System.out.println("hash: " + txHash);
        }
    }

    /**
     * Send asset
     */
    @Test
    public void sendAsset() {
        // Init variable
        // The account private key to start this transaction
        String senderPrivateKey = "privby8cFTi4ugxc95qJMF5PvCz6PvQAxhUXAZoqFENUKRE19tM1Vbii";
        // The account to receive asset
        String destAddress = "adxScpCtbeLP2KGRaCkbtrmz8iB5mu6DQcW3r";
        // Asset code
        String assetCode = "TST";
        // The accout address of issuing asset
        String assetIssuer = "adxSYvndiFG4zpbLGugX3j93fDn9nWZfWp8Gd";
        // The asset amount to be sent
        Long amount = ToBaseUnit.ToUGas("100000");
        // The fixed write 1000L, the unit is UGas
        Long gasPrice = 1000L;
        // Set up the maximum cost 0.01Gas
        Long feeLimit = ToBaseUnit.ToUGas("0.01");
        // Transaction initiation account's nonce + 1
        Long nonce = 2L;

        // 1. Get the account address to send this transaction
        String senderAddresss = getAddressByPrivateKey(senderPrivateKey);

        // 2. Build sendAsset
        AssetSendOperation assetSendOperation = new AssetSendOperation();
        assetSendOperation.setSourceAddress(senderAddresss);
        assetSendOperation.setDestAddress(destAddress);
        assetSendOperation.setCode(assetCode);
        assetSendOperation.setIssuer(assetIssuer);
        assetSendOperation.setAmount(amount);
        assetSendOperation.setMetadata("send token");

        String[] signerPrivateKeyArr = {senderPrivateKey};
        // Record txhash for subsequent confirmation of the real result of the transaction.
        // After recommending five blocks, call again through txhash `Get the transaction information
        // from the transaction Hash'(see example: getTxByHash ()) to confirm the final result of the transaction
        String txHash = submitTransaction(signerPrivateKeyArr, senderAddresss, assetSendOperation, nonce, gasPrice, feeLimit);
        if (txHash != null) {
            System.out.println("hash: " + txHash);
        }
    }

    /**
     * Send a transaction of sending bu
     */
    @Test
    public void sendGas() {
        // Init variable
        // The account private key to send gas
        String senderPrivateKey = "privbw1Uup9Qif12uysm9LyCby3HC5s6m7FMBzU4Ac64NVZ2tvB4f8wB";
        // The account address to receive bu
        String destAddress = "adxSYvndiFG4zpbLGugX3j93fDn9nWZfWp8Gd";
        // The amount to be sent
        Long amount = ToBaseUnit.ToUGas("100");
        // The fixed write 1000L, the unit is UGas
        Long gasPrice = 1000L;
        // Set up the maximum cost 0.01Gas
        Long feeLimit = ToBaseUnit.ToUGas("0.01");
        // Transaction initiation account's nonce + 1
        Long nonce = 28L;

        // 1. Get the account address to send this transaction
        String senderAddresss = getAddressByPrivateKey(senderPrivateKey);

        // 2. Build sendGas
        GasSendOperation operation = new GasSendOperation();
        operation.setSourceAddress(senderAddresss);
        operation.setDestAddress(destAddress);
        operation.setAmount(amount);

        String[] signerPrivateKeyArr = {senderPrivateKey};
        // Record txhash for subsequent confirmation of the real result of the transaction.
        // After recommending five blocks, call again through txhash `Get the transaction information
        // from the transaction Hash'(see example: getTxByHash ()) to confirm the final result of the transaction
        String txHash = submitTransaction(signerPrivateKeyArr, senderAddresss, operation, nonce, gasPrice, feeLimit);
        if (txHash != null) {
            System.out.println("hash: " + txHash);
        }

    }

    /**
     * Write logs to the Gas block chain
     */
    @Test
    public void createLog() {
        // Init variable
        // The account private key to create log
        String createPrivateKey = "privbyQCRp7DLqKtRFCqKQJr81TurTqG6UKXMMtGAmPG3abcM9XHjWvq";
        // Log topic
        String topic = "test";
        // Log content
        String data = "this is not a error";
        // notes
        String metadata = "create log";
        // The fixed write 1000L, the unit is UGas
        Long gasPrice = 1000L;
        // Set up the maximum cost 0.01Gas
        Long feeLimit = ToBaseUnit.ToUGas("0.01");
        // Transaction initiation account's nonce + 1
        Long nonce = 59L;

        // 1. Get the account address to send this transaction
        String createAddresss = getAddressByPrivateKey(createPrivateKey);

        // Build createLog
        LogCreateOperation operation = new LogCreateOperation();
        operation.setSourceAddress(createAddresss);
        operation.setTopic(topic);
        operation.addData(data);
        operation.setMetadata(metadata);

        String[] signerPrivateKeyArr = {createPrivateKey};
        // Record txhash for subsequent confirmation of the real result of the transaction.
        // After recommending five blocks, call again through txhash `Get the transaction information
        // from the transaction Hash'(see example: getTxByHash ()) to confirm the final result of the transaction
        String txHash = submitTransaction(signerPrivateKeyArr, createAddresss, operation, nonce, gasPrice, feeLimit);
        if (txHash != null) {
            System.out.println("hash: " + txHash);
        }
    }

    /**
     * @param senderPrivateKeys The account private keys to sign transaction
     * @param senderAddresss   The account address to start transaction
     * @param operation        operation
     * @param senderNonce      Transaction initiation account's Nonce
     * @param gasPrice         Gas price
     * @param feeLimit         fee limit
     * @return java.lang.String transaction hash
     * @author riven
     */
    private String submitTransaction(String[] senderPrivateKeys, String senderAddresss, BaseOperation operation, Long senderNonce, Long gasPrice, Long feeLimit) {
        // 3. Build transaction
        TransactionBuildBlobRequest transactionBuildBlobRequest = new TransactionBuildBlobRequest();
        transactionBuildBlobRequest.setSourceAddress(senderAddresss);
        transactionBuildBlobRequest.setNonce(senderNonce);
        transactionBuildBlobRequest.setFeeLimit(feeLimit);
        transactionBuildBlobRequest.setGasPrice(gasPrice);
        transactionBuildBlobRequest.addOperation(operation);
        // transactionBuildBlobRequest.setMetadata("abc");

        // 4. Build transaction BLob
        String transactionBlob;
        TransactionBuildBlobResponse transactionBuildBlobResponse = sdk.getTransactionService().buildBlob(transactionBuildBlobRequest);
        if (transactionBuildBlobResponse.getErrorCode() != 0) {
            System.out.println("error: " + transactionBuildBlobResponse.getErrorDesc());
            return null;
        }
        TransactionBuildBlobResult transactionBuildBlobResult = transactionBuildBlobResponse.getResult();
        String txHash = transactionBuildBlobResult.getHash();
        transactionBlob = transactionBuildBlobResult.getTransactionBlob();
        try {
            Chain.Transaction tran = Chain.Transaction.parseFrom(HexFormat.hexToByte(transactionBlob));
            JsonFormat jsonFormat = new JsonFormat();
            System.out.println(jsonFormat.printToString(tran));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        // 5. Sign transaction BLob
        String[] signerPrivateKeyArr = senderPrivateKeys;
        TransactionSignRequest transactionSignRequest = new TransactionSignRequest();
        transactionSignRequest.setBlob(transactionBlob);
        for (int i = 0; i < signerPrivateKeyArr.length; i++) {
            transactionSignRequest.addPrivateKey(signerPrivateKeyArr[i]);
        }
        TransactionSignResponse transactionSignResponse = sdk.getTransactionService().sign(transactionSignRequest);
        if (transactionSignResponse.getErrorCode() != 0) {
            System.out.println("error: " + transactionSignResponse.getErrorDesc());
            return null;
        }

        // 6. Broadcast
        TransactionSubmitRequest transactionSubmitRequest = new TransactionSubmitRequest();
        transactionSubmitRequest.setTransactionBlob(transactionBlob);
        transactionSubmitRequest.setSignatures(transactionSignResponse.getResult().getSignatures());
        TransactionSubmitResponse transactionSubmitResponse = sdk.getTransactionService().submit(transactionSubmitRequest);
        if (0 == transactionSubmitResponse.getErrorCode()) {
            System.out.println("Success，hash=" + transactionSubmitResponse.getResult().getHash());
        } else {
            System.out.println("Failure，hash=" + transactionSubmitResponse.getResult().getHash() + "");
            System.out.println(JSON.toJSONString(transactionSubmitResponse, true));
        }
        return txHash;
    }

    /**
     * @param senderPrivateKey The account private key to start transaction
     * @param destAddress      The account to receive bu
     * @param amount           Gas amount
     * @param nonce            The account nonce to start transaction
     * @param gasPrice         Gas price
     * @param feeLimit         Fee limit
     * @return TransactionFees transaction fees
     * @author riven
     */
    private TransactionFees evaluateFees(String senderPrivateKey, String destAddress, Long amount, Long nonce, Long gasPrice, Long feeLimit) throws Exception {
        // 1. Get the account address to send this transaction
        String senderAddresss = getAddressByPrivateKey(senderPrivateKey);

        // 2. Build sendGas
        GasSendOperation gasSendOperation = new GasSendOperation();
        gasSendOperation.setSourceAddress(senderAddresss);
        gasSendOperation.setDestAddress(destAddress);
        gasSendOperation.setAmount(amount);
        gasSendOperation.setMetadata("616263");

        // 3. Init request
        TransactionEvaluateFeeRequest request = new TransactionEvaluateFeeRequest();
        request.addOperation(gasSendOperation);
        request.setSourceAddress(senderAddresss);
        request.setNonce(nonce);
        request.setSignatureNumber(1);
        request.setMetadata("616263");

        TransactionEvaluateFeeResponse response = sdk.getTransactionService().evaluateFee(request);
        if (response.getErrorCode() == 0) {
            return response.getResult().getTxs()[0].getTransactionEnv().getTransactionFees();
        } else {
            System.out.println("error: " + response.getErrorDesc());
            return null;
        }
    }

    /**
     * @param privatekey private key
     * @return java.lang.String Account address
     * @author riven
     */
    private String getAddressByPrivateKey(String privatekey) {
        String publicKey = PrivateKey.getEncPublicKey(privatekey);
        String address = PrivateKey.getEncAddress(publicKey);
        return address;
    }
}
