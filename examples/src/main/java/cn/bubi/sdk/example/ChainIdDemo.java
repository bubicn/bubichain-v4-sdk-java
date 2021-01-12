package cn.bubi.sdk.example;

import com.alibaba.fastjson.JSON;
import cn.bubi.SDK;
import cn.bubi.common.ToBaseUnit;
import cn.bubi.encryption.key.PrivateKey;
import cn.bubi.model.request.SDKConfigure;
import cn.bubi.model.request.TransactionBuildBlobRequest;
import cn.bubi.model.request.TransactionSignRequest;
import cn.bubi.model.request.TransactionSubmitRequest;
import cn.bubi.model.request.operation.GasSendOperation;
import cn.bubi.model.request.operation.BaseOperation;
import cn.bubi.model.response.TransactionBuildBlobResponse;
import cn.bubi.model.response.TransactionSignResponse;
import cn.bubi.model.response.TransactionSubmitResponse;
import cn.bubi.model.response.result.TransactionBuildBlobResult;
import org.junit.Test;

/**
 * @Author riven
 * @Date 2018/12/3 15:37
 */
public class ChainIdDemo {
    private SDK sdk = null;

    /*
     * Send gas with wrong configure.
     * Return: Failed to check same chain, node self id(10) is not eq (0).
     */
    @Test
    public void sendGasWithWrongConfigure() {
        // Set right configure
        setDefaultConfigure();

        // Send gas
        sendGas();
    }

    /*
     * Send gas with right configure.
     * Return: Successful.
     */
    @Test
    public void sendGasWithRightConfigure() {
        // Set the right configure
        setSDKConfigure(10);

        // Send gas
        sendGas();
    }

    private void setDefaultConfigure() {
        sdk = SDK.getInstance("http://127.0.0.1:36002");
    }

    /*
     * Set sdk configure, including chainId, httpConnectTimeout and httpReadTimeout
     * The chainId: Different chain has different chain id, default is 0.
     * The httpConnectTimeout: The http connection timeout.
     * The httpReadTimeout: The http read timeout.
     */
    private void setSDKConfigure(int chainId) {
        int connectTimeout = 5000;
        int readTimeout = 5000;
        SDKConfigure sdkConfigure = new SDKConfigure();
        sdkConfigure.setChainId(chainId);
        sdkConfigure.setHttpConnectTimeOut(connectTimeout);
        sdkConfigure.setHttpReadTimeOut(readTimeout);
        sdkConfigure.setUrl("http://127.0.0.1:36002");
        sdk = SDK.getInstance(sdkConfigure);
    }

    private void sendGas() {
        // Init variable
        // The account private key to send gas
        String senderPrivateKey = "privby8cFTi4ugxc95qJMF5PvCz6PvQAxhUXAZoqFENUKRE19tM1Vbii";
        // The account address to receive bu
        String destAddress = "adxSYvndiFG4zpbLGugX3j93fDn9nWZfWp8Gd";
        // The amount to be sent
        Long amount = ToBaseUnit.ToUGas("0.01");
        // The fixed write 1000L, the unit is UGas
        Long gasPrice = 1000L;
        // Set up the maximum cost 0.01Gas
        Long feeLimit = ToBaseUnit.ToUGas("0.01");
        // Transaction initiation account's nonce + 1
        Long nonce = 2L;

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
        TransactionSignRequest transactionSignRequest = new TransactionSignRequest();
        transactionSignRequest.setBlob(transactionBlob);
        for (int i = 0; i < senderPrivateKeys.length; i++) {
            transactionSignRequest.addPrivateKey(senderPrivateKeys[i]);
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
