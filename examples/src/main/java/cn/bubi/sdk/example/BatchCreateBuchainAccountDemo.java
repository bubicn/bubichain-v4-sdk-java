package cn.bubi.sdk.example;

import com.alibaba.fastjson.JSON;
import cn.bubi.SDK;
import cn.bubi.common.ToBaseUnit;
import cn.bubi.crypto.Keypair;
import cn.bubi.model.request.TransactionBuildBlobRequest;
import cn.bubi.model.request.TransactionSignRequest;
import cn.bubi.model.request.TransactionSubmitRequest;
import cn.bubi.model.request.operation.AccountActivateOperation;
import cn.bubi.model.request.operation.BaseOperation;
import cn.bubi.model.response.TransactionBuildBlobResponse;
import cn.bubi.model.response.TransactionSignResponse;
import cn.bubi.model.response.TransactionSubmitResponse;
import cn.bubi.model.response.result.TransactionBuildBlobResult;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BatchCreateBuchainAccountDemo {
    SDK sdk = SDK.getInstance("http://node.bubidev.cn");
    /**
     * 批量创建账户
     *
     * 注意：一次最多100个创建账户的操作，该操作是原子性的。每个初始化账户推荐给0.02Gas,交易费用100个操作的情况下建议feeLimist为0.1Gas
     */
   @Test
    public void createBuchainAccount(){
        // 推荐人账户地址
        String refereeAddress = "adxSYvndiFG4zpbLGugX3j93fDn9nWZfWp8Gd";
        // 推荐人账户私钥
        String refereePrivateKey = "privbw1Uup9Qif12uysm9LyCby3HC5s6m7FMBzU4Ac64NVZ2tvB4f8wB";

        // 推荐人账户nonce
        // TODO 需要查询当前推荐人的nonce值
        Long refereeNonce = 4L;

        // The fixed write 1000L, the unit is UGas
        Long gasPrice = 1000L;
        // Set up the maximum cost 0.01Gas
        Long feeLimit = ToBaseUnit.ToUGas("0.1");

        // 每个账户初始化0.02Gas
        Long initBalance = ToBaseUnit.ToUGas("0.02");
        List<AccountActivateOperation> accountActivateOperationList = new ArrayList<AccountActivateOperation>();
        Keypair keypair = null;
        // 注意：交易里最多100个操作
        for (int i = 0; i < 100; i++) {
            keypair = Keypair.generator();
            AccountActivateOperation operation = new AccountActivateOperation();
            operation.setSourceAddress(refereeAddress);
            operation.setDestAddress(keypair.getAddress());
            operation.setInitBalance(initBalance);
            accountActivateOperationList.add(operation);
            System.out.println("第" + i + " " + JSON.toJSONString(keypair));
        }

        // 提交交易（广播交易），生成的hash，需要通过hash再次查询交易来确认最终状态。
        String hash = submitTransaction(refereePrivateKey,refereeAddress,accountActivateOperationList,refereeNonce, gasPrice, feeLimit, "");
       if(hash != null){
           System.out.println("tx hash:" + hash);
       }else{
           System.out.println("操作失败");
       }
    }

    /**
     * @param submiterPrivateKey The account private key to start transaction
     * @param submiterAddresss   The account address to start transaction
     * @param operations       operations
     * @param submiterNonce initiation account's Nonce
     * @param gasPrice         Gas price
     * @param feeLimit         fee limit
     * @return java.lang.String transaction hash
     */
    private String submitTransaction(String submiterPrivateKey, String submiterAddresss, List<AccountActivateOperation> operations, Long submiterNonce, Long gasPrice, Long feeLimit, String transMetadata) {
        // 1. Build transaction
        TransactionBuildBlobRequest transactionBuildBlobRequest = new TransactionBuildBlobRequest();
        transactionBuildBlobRequest.setSourceAddress(submiterAddresss);
        transactionBuildBlobRequest.setNonce(submiterNonce);
        transactionBuildBlobRequest.setFeeLimit(feeLimit);
        transactionBuildBlobRequest.setGasPrice(gasPrice);
        for (BaseOperation opt: operations
             ) {
            transactionBuildBlobRequest.addOperation(opt);
        }
        transactionBuildBlobRequest.setMetadata(transMetadata);

        // 2. Build transaction BLob
        String transactionBlob;
        TransactionBuildBlobResponse transactionBuildBlobResponse = sdk.getTransactionService().buildBlob(transactionBuildBlobRequest);
        if (transactionBuildBlobResponse.getErrorCode() != 0) {
            System.out.println("error: " + transactionBuildBlobResponse.getErrorDesc());
            return null;
        }
        TransactionBuildBlobResult transactionBuildBlobResult = transactionBuildBlobResponse.getResult();
        transactionBlob = transactionBuildBlobResult.getTransactionBlob();

        // 3. Sign transaction BLob
        String[] signerPrivateKeyArr = {submiterPrivateKey};
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

        // 4. Broadcast transaction
        String Hash = null;
        TransactionSubmitRequest transactionSubmitRequest = new TransactionSubmitRequest();
        transactionSubmitRequest.setTransactionBlob(transactionBlob);
        transactionSubmitRequest.setSignatures(transactionSignResponse.getResult().getSignatures());
        TransactionSubmitResponse transactionSubmitResponse = sdk.getTransactionService().submit(transactionSubmitRequest);
        if (0 == transactionSubmitResponse.getErrorCode()) {
            Hash = transactionSubmitResponse.getResult().getHash();
        } else {
            System.out.println(JSON.toJSONString(transactionSubmitResponse, true));
        }
        return Hash;
    }


}
