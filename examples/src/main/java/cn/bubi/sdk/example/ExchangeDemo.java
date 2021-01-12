package cn.bubi.sdk.example;

import com.alibaba.fastjson.JSON;
import cn.bubi.SDK;
import cn.bubi.common.ToBaseUnit;
import cn.bubi.crypto.Keypair;
import cn.bubi.encryption.key.PrivateKey;
import cn.bubi.model.request.*;
import cn.bubi.model.request.operation.GasSendOperation;
import cn.bubi.model.response.*;
import cn.bubi.model.response.result.AccountGetNonceResult;
import cn.bubi.model.response.result.BlockGetInfoResult;
import cn.bubi.model.response.result.BlockGetLatestInfoResult;
import cn.bubi.model.response.result.TransactionBuildBlobResult;
import org.junit.Test;

public class ExchangeDemo {
    SDK sdk = SDK.getInstance("http://node.bubidev.cn");

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
        String address = "adxScpCtbeLP2KGRaCkbtrmz8iB5mu6DQcW3r";
        AccountCheckValidRequest accountCheckValidRequest = new AccountCheckValidRequest();
        accountCheckValidRequest.setAddress(address);
        AccountCheckValidResponse accountCheckValidResponse = sdk.getAccountService().checkValid(accountCheckValidRequest);
        if (0 == accountCheckValidResponse.getErrorCode()) {
            System.out.println(accountCheckValidResponse.getResult().isValid());
        } else {
            System.out.println(JSON.toJSONString(accountCheckValidResponse, true));
        }
    }

    /**
     * Get account info
     */
    @Test
    public void getAccountInfo() {
        String accountAddress = "adxScpCtbeLP2KGRaCkbtrmz8iB5mu6DQcW3r";
        AccountGetInfoRequest request = new AccountGetInfoRequest();
        request.setAddress(accountAddress);

        AccountGetInfoResponse response = sdk.getAccountService().getInfo(request);
        System.out.println(JSON.toJSONString(response, true));

    }

    /**
     * Get account Gas balance
     */
    @Test
    public void getAccountBalance() {
        String accountAddress = "adxScpCtbeLP2KGRaCkbtrmz8iB5mu6DQcW3r";
        AccountGetBalanceRequest request = new AccountGetBalanceRequest();
        request.setAddress(accountAddress);

        AccountGetBalanceResponse response = sdk.getAccountService().getBalance(request);

        System.out.println(JSON.toJSONString(response, true));
        if (0 == response.getErrorCode()) {
            System.out.println("Gas balance：" + ToBaseUnit.ToGas(response.getResult().getBalance().toString()) + "Gas");
        }
    }

    /**
     * Get account nonce
     */
    @Test
    public void getAccountNonce() {
        String accountAddress = "adxScpCtbeLP2KGRaCkbtrmz8iB5mu6DQcW3r";
        AccountGetNonceRequest request = new AccountGetNonceRequest();
        request.setAddress(accountAddress);

        AccountGetNonceResponse response = sdk.getAccountService().getNonce(request);
        if (0 == response.getErrorCode()) {
            System.out.println("Account nonce:" + response.getResult().getNonce());
        }
    }

    /**
     * Send a transaction of sending bu
     *
     * @throws Exception
     */
    @Test
    public void sendGas() throws Exception {
        // Init variable
        // The account private key to send gas
        String senderPrivateKey = "privby8cFTi4ugxc95qJMF5PvCz6PvQAxhUXAZoqFENUKRE19tM1Vbii";
        // The account address to receive bu
        String destAddress = "adxScpCtbeLP2KGRaCkbtrmz8iB5mu6DQcW3r";
        // The amount to be sent
        Long amount = ToBaseUnit.ToUGas("0.01");
        // The fixed write 1000L, the unit is UGas
        Long gasPrice = 1000L;
        // Set up the maximum cost 0.01Gas
        Long feeLimit = ToBaseUnit.ToUGas("0.01");
        // Transaction initiation account's nonce + 1
        Long nonce = 1L;

        // Record txhash for subsequent confirmation of the real result of the transaction.
        // After recommending five blocks, call again through txhash `Get the transaction information
        // from the transaction Hash'(see example: getTxByHash ()) to confirm the final result of the transaction
        String txhash = sendGas(senderPrivateKey, destAddress, amount, nonce, gasPrice, feeLimit);

    }

    @Test
    public void getTransactionInfo() {
        TransactionGetInfoRequest request = new TransactionGetInfoRequest();
        request.setHash("0d67997af3777b977deb648082c8288b4ba46b09d910d016f0942bcd853ad518");
        TransactionGetInfoResponse response = sdk.getTransactionService().getInfo(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JSON.toJSONString(response.getResult(), true));
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
    }

    /**
     * 根据交易Hash获取交易信息
     */
    @Test
    public void getTxByHash() {
        String txHash = "fba9c3f73705ca3eb865c7ec2959c30bd27534509796fd5b208b0576ab155d95";
        TransactionGetInfoRequest request = new TransactionGetInfoRequest();
        request.setHash(txHash);
        TransactionGetInfoResponse response = sdk.getTransactionService().getInfo(request);
        if (0 == response.getErrorCode()) {
            System.out.println(JSON.toJSONString(response, true));
        } else {
            System.out.println("失败\n" + JSON.toJSONString(response, true));
        }
    }

    /**
     * 探测用户充值
     * <p>
     * 通过解析区块下的交易，来探测用户的充值动作
     */
    @Test
    public void getTransactionOfBolck() {
        Long blockNumber = 617247L;// 第617247区块
        BlockGetTransactionsRequest request = new BlockGetTransactionsRequest();
        request.setBlockNumber(blockNumber);
        BlockGetTransactionsResponse response = sdk.getBlockService().getTransactions(request);
        if (0 == response.getErrorCode()) {
            System.out.println(JSON.toJSONString(response, true));
        } else {
            System.out.println("失败\n" + JSON.toJSONString(response, true));
        }
        // 探测某账户是否有充值Gas
        // 解析 transactions[n].transaction.operations[n].pay_coin.dest_address 即可

        // 注意！！！！！
        // operations是数组，有可能有多笔转账操作
    }

    /**
     * 查询最新的区块高度
     */
    @Test
    public void getLastBlockNumber() {
        BlockGetNumberResponse response = sdk.getBlockService().getNumber();
        System.out.println(response.getResult().getHeader());
    }

    /**
     * 查询指定区块高度的区块信息
     */
    @Test
    public void getBlockInfo() {
        BlockGetInfoRequest blockGetInfoRequest = new BlockGetInfoRequest();
        blockGetInfoRequest.setBlockNumber(629743L);
        BlockGetInfoResponse lockGetInfoResponse = sdk.getBlockService().getInfo(blockGetInfoRequest);
        if (lockGetInfoResponse.getErrorCode() == 0) {
            BlockGetInfoResult lockGetInfoResult = lockGetInfoResponse.getResult();
            System.out.println(JSON.toJSONString(lockGetInfoResult, true));
        } else {
            System.out.println("error: " + lockGetInfoResponse.getErrorDesc());
        }
    }

    /**
     * 查询最新的区块信息
     */
    @Test
    public void getBlockLatestInfo() {
        BlockGetLatestInfoResponse lockGetLatestInfoResponse = sdk.getBlockService().getLatestInfo();
        if (lockGetLatestInfoResponse.getErrorCode() == 0) {
            BlockGetLatestInfoResult lockGetLatestInfoResult = lockGetLatestInfoResponse.getResult();
            System.out.println(JSON.toJSONString(lockGetLatestInfoResult, true));
        } else {
            System.out.println(lockGetLatestInfoResponse.getErrorDesc());
        }
    }

    private String sendGas(String senderPrivateKey, String destAddress, Long amount, Long senderNonce, Long gasPrice, Long feeLimit) throws Exception {

        // 1. Get the account address to send this transaction
        String senderAddresss = getAddressByPrivateKey(senderPrivateKey);

        // 2. Build sendGas
        GasSendOperation gasSendOperation = new GasSendOperation();
        gasSendOperation.setSourceAddress(senderAddresss);
        gasSendOperation.setDestAddress(destAddress);
        gasSendOperation.setAmount(amount);

        // 3. Build transaction
        TransactionBuildBlobRequest transactionBuildBlobRequest = new TransactionBuildBlobRequest();
        transactionBuildBlobRequest.setSourceAddress(senderAddresss);
        transactionBuildBlobRequest.setNonce(senderNonce);
        transactionBuildBlobRequest.setFeeLimit(feeLimit);
        transactionBuildBlobRequest.setGasPrice(gasPrice);
        transactionBuildBlobRequest.addOperation(gasSendOperation);

        // 4. Build transaction BLob
        String transactionBlob = null;
        TransactionBuildBlobResponse transactionBuildBlobResponse = sdk.getTransactionService().buildBlob(transactionBuildBlobRequest);
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

        // 6. Broadcast transaction
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

    private Long getNonceOfAccount(String senderAddresss) {
        AccountGetNonceRequest accountGetNonceRequest = new AccountGetNonceRequest();
        accountGetNonceRequest.setAddress(senderAddresss);

        AccountGetNonceResponse accountGetNonceResponse = sdk.getAccountService().getNonce(accountGetNonceRequest);
        if (accountGetNonceResponse.getErrorCode() == 0) {
            AccountGetNonceResult accountGetNonceResult = accountGetNonceResponse.getResult();
            return accountGetNonceResult.getNonce();
        } else {
            return null;
        }
    }

    private String getAddressByPrivateKey(String privatekey) throws Exception {
        String publicKey = PrivateKey.getEncPublicKey(privatekey);
        String address = PrivateKey.getEncAddress(publicKey);
        return address;
    }
}

