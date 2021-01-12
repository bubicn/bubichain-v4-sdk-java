package cn.bubi.sdk.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.bubi.SDK;
import cn.bubi.common.ToBaseUnit;
import cn.bubi.encryption.key.PrivateKey;
import cn.bubi.model.request.*;
import cn.bubi.model.request.operation.BaseOperation;
import cn.bubi.model.request.operation.ContractCreateOperation;
import cn.bubi.model.request.operation.ContractInvokeByAssetOperation;
import cn.bubi.model.request.operation.ContractInvokeByGasOperation;
import cn.bubi.model.response.*;
import cn.bubi.model.response.result.ContractCallResult;
import cn.bubi.model.response.result.ContractCheckValidResult;
import cn.bubi.model.response.result.TransactionBuildBlobResult;
import org.junit.Test;

/**
 * @Author riven
 * @Date 2018/8/6 20:52
 */
public class ContractDemo {
    SDK sdk = SDK.getInstance("http://node.bubidev.cn");

    /**
     * Check whether the contract is valid
     */
    @Test
    public void checkContractValid() {
        // Init request
        ContractCheckValidRequest request = new ContractCheckValidRequest();
        request.setContractAddress("adxSqKcX8wGCMKhzNUBoDWfbeQaMhfnGdtyG2");

        // Call checkValid
        ContractCheckValidResponse response = sdk.getContractService().checkValid(request);
        if (response.getErrorCode() == 0) {
            ContractCheckValidResult result = response.getResult();
            System.out.println(result.getValid());
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get contract info
     */
    @Test
    public void getContractInfo() {
        // Init request
        ContractGetInfoRequest request = new ContractGetInfoRequest();
        request.setContractAddress("adxSqKcX8wGCMKhzNUBoDWfbeQaMhfnGdtyG2");

        // Call getInfo
        ContractGetInfoResponse response = sdk.getContractService().getInfo(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * Get contract address
     */
    @Test
    public void getContractAddress() {
        // Init request
        String hash = "44246c5ba1b8b835a5cbc29bdc9454cdb9a9d049870e41227f2dcfbcf7a07689";
        ContractGetAddressRequest request = new ContractGetAddressRequest();
        request.setHash(hash);

        // Call getAddress
        ContractGetAddressResponse response = sdk.getContractService().getAddress(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * call contract
     */
    @Test
    public void callContract() {
        // Init variable
        // Contract address
        String contractAddress = "adxSqKcX8wGCMKhzNUBoDWfbeQaMhfnGdtyG2";
        // Spender address
        String spender = "adxSYvndiFG4zpbLGugX3j93fDn9nWZfWp8Gd";
        // Amount
        String amount = "1000000";

        // Init input
        JSONObject input = new JSONObject();
        input.put("method", "approve");
        JSONObject params = new JSONObject();
        params.put("spender", spender);
        params.put("value", amount);
        input.put("params", params);

        // Init request
        ContractCallRequest request = new ContractCallRequest();
        request.setContractAddress(contractAddress);
        request.setFeeLimit(1000000000L);
        request.setOptType(1);
        request.setInput(input.toJSONString());

        // Call call
        ContractCallResponse response = sdk.getContractService().call(request);
        if (response.getErrorCode() == 0) {
            ContractCallResult result = response.getResult();
            System.out.println(JSON.toJSONString(result, true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * 创建合约
     */
    @Test
    public void createContract() {
        // The account private key to create contract
        String createPrivateKey = "privbUdwf6xV1d5Jvkcakuz8T8nfFn4U7d5s55VUbwmi79DPxqNWSD1n";
        // Contract account initialization Gas，the unit is UGas，and 1 Gas = 10^8 UGas
        Long initBalance = 100000000L;//ToBaseUnit.ToUGas("0.1");
        // Contract code
        String payload = "\n          \"use strict\";\n          function init(bar)\n          {\n            /*init whatever you want*/\n            return;\n          }\n          \n          function main(input)\n          {\n            let para = JSON.parse(input);\n            if (para.do_foo)\n            {\n              let x = {\n                \'hello\' : \'world\'\n              };\n            }\n          }\n          \n          function query(input)\n          { \n            return input;\n          }\n        ";
        // The fixed write 1000L ，the unit is UGas
        Long gasPrice = 1000L;
        // Set up the maximum cost 10.01Gas
        Long feeLimit = 1015076000L;//ToBaseUnit.ToUGas("10.01");
        // Transaction initiation account's Nonce + 1
        Long nonce = 18L;
        // Contract init function entry
        String initInput = "";

        // 1. Get the account address to send this transaction
        String createAddresss = getAddressByPrivateKey(createPrivateKey);

        // 2. Build activateAccount operation
        ContractCreateOperation operation = new ContractCreateOperation();
        operation.setSourceAddress(createAddresss);
        operation.setInitBalance(initBalance);
        operation.setPayload(payload);
        operation.setInitInput(initInput);
        operation.setMetadata("create contract");

        // Record txhash for subsequent confirmation of the real result of the transaction.
        // After recommending five blocks, call again through txhash `Get the transaction information
        // from the transaction Hash'(see example: getTxByHash ()) to confirm the final result of the transaction
        String txHash = submitTransaction(createPrivateKey, createAddresss, operation, nonce, gasPrice, feeLimit);
        if (txHash != null) {
            System.out.println("hash: " + txHash);
        }
    }

    /**
     * Send asset and trigger contract
     */
    @Test
    public void invokeContractByAsset() {
        // Init variable
        // The account private key to invoke contract
        String invokePrivateKey = "privby8cFTi4ugxc95qJMF5PvCz6PvQAxhUXAZoqFENUKRE19tM1Vbii";
        // The account to receive the assets
        String destAddress = "adxScpCtbeLP2KGRaCkbtrmz8iB5mu6DQcW3r";
        // The asset code to be sent
        String assetCode = "TST";
        // The account address to issue asset
        String assetIssuer = "adxSYvndiFG4zpbLGugX3j93fDn9nWZfWp8Gd";
        // 0 means that the contract is only triggered
        Long amount = 0L;
        // The fixed write 1000L, the unit is UGas
        Long gasPrice = 1000L;
        // Set up the maximum cost 0.01Gas
        Long feeLimit = ToBaseUnit.ToUGas("0.01");
        // Transaction initiation account's Nonce + 1
        Long nonce = 57L;
        // Contract main function entry
        String input = "";

        // 1. Get the account address to send this transaction
        String invokeAddresss = getAddressByPrivateKey(invokePrivateKey);

        // 2. Build sendAsset operation
        ContractInvokeByAssetOperation operation = new ContractInvokeByAssetOperation();
        operation.setSourceAddress(invokeAddresss);
        operation.setContractAddress(destAddress);
        operation.setCode(assetCode);
        operation.setIssuer(assetIssuer);
        operation.setAssetAmount(amount);
        operation.setInput(input);
        operation.setMetadata("send token");

        // Record txhash for subsequent confirmation of the real result of the transaction.
        // After recommending five blocks, call again through txhash `Get the transaction information
        // from the transaction Hash'(see example: getTxByHash ()) to confirm the final result of the transaction
        String txHash = submitTransaction(invokePrivateKey, invokeAddresss, operation, nonce, gasPrice, feeLimit);
        if (txHash != null) {
            System.out.println("hash: " + txHash);
        }
    }

    /**
     * Send Gas and trigger contract
     */
    @Test
    public void invokeContractByGas() {
        // Init variable
        // The account private key to invoke contract
        String invokePrivateKey = "privby8cFTi4ugxc95qJMF5PvCz6PvQAxhUXAZoqFENUKRE19tM1Vbii";
        // The account to receive the Gas
        String destAddress = "adxSqKcX8wGCMKhzNUBoDWfbeQaMhfnGdtyG2";
        // 0 means that the contract is only triggered
        Long amount = 0L;
        // The fixed write 1000L, the unit is UGas
        Long gasPrice = 1000L;
        // Set up the maximum cost 0.01Gas
        Long feeLimit = ToBaseUnit.ToUGas("0.01");
        // Transaction initiation account's Nonce + 1
        Long nonce = 58L;
        // Contract main function entry
        String input = "";

        // 1. Get the account address to send this transaction
        String invokeAddresss = getAddressByPrivateKey(invokePrivateKey);

        // 2. Build sendAsset operation
        ContractInvokeByGasOperation operation = new ContractInvokeByGasOperation();
        operation.setSourceAddress(invokeAddresss);
        operation.setContractAddress(destAddress);
        operation.setBuAmount(amount);
        operation.setInput(input);

        // Record txhash for subsequent confirmation of the real result of the transaction.
        // After recommending five blocks, call again through txhash `Get the transaction information
        // from the transaction Hash'(see example: getTxByHash ()) to confirm the final result of the transaction
        String txHash = submitTransaction(invokePrivateKey, invokeAddresss, operation, nonce, gasPrice, feeLimit);
        if (txHash != null) {
            System.out.println("hash: " + txHash);
        }
    }

    /**
     * @param senderPrivateKey The account private key to start transaction
     * @param senderAddresss   The account address to start transaction
     * @param operation        operation
     * @param senderNonce      Transaction initiation account's Nonce
     * @param gasPrice         Gas price
     * @param feeLimit         fee limit
     * @return java.lang.String transaction hash
     * @author riven
     */
    private String submitTransaction(String senderPrivateKey, String senderAddresss, BaseOperation operation, Long senderNonce, Long gasPrice, Long feeLimit) {
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

        // 5. Sign transaction BLob
        String[] signerPrivateKeyArr = {senderPrivateKey};
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
