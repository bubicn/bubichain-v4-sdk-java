package cn.bubi.token.impl;

import cn.bubi.crypto.protobuf.Chain;
import com.google.protobuf.ByteString;
import cn.bubi.common.Tools;
import cn.bubi.encryption.key.PublicKey;
import cn.bubi.exception.SDKException;
import cn.bubi.exception.SdkError;
import cn.bubi.model.request.operation.GasSendOperation;

/**
 * @Author riven
 * @Date 2018/7/5 12:22
 */
public class GasServiceImpl {

    public static Chain.Operation send(GasSendOperation gasSendOperation, String transSourceAddress) throws SDKException {
        Chain.Operation.Builder operation;
        try {
            String sourceAddress = gasSendOperation.getSourceAddress();
            if (!Tools.isEmpty(sourceAddress) && !PublicKey.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            String destAddress = gasSendOperation.getDestAddress();
            if (!PublicKey.isAddressValid(destAddress)) {
                throw new SDKException(SdkError.INVALID_DESTADDRESS_ERROR);
            }
            boolean isNotValid = (!Tools.isEmpty(sourceAddress) && sourceAddress.equals(destAddress)) ||
                    (Tools.isEmpty(sourceAddress) && transSourceAddress.equals(destAddress));
            if (isNotValid) {
                throw new SDKException(SdkError.SOURCEADDRESS_EQUAL_DESTADDRESS_ERROR);
            }
            Long amount = gasSendOperation.getAmount();
            if (Tools.isEmpty(amount) || amount < 0) {
                throw new SDKException(SdkError.INVALID_GAS_AMOUNT_ERROR);
            }
            String metadata = gasSendOperation.getMetadata();
            // build operation
            operation = Chain.Operation.newBuilder();
            operation.setType(Chain.Operation.Type.PAY_COIN);
            if (!Tools.isEmpty(sourceAddress)) {
                operation.setSourceAddress(sourceAddress);
            }
            if (!Tools.isEmpty(metadata)) {
                operation.setMetadata(ByteString.copyFromUtf8(metadata));
            }
            Chain.OperationPayCoin.Builder operationPayCoin = operation.getPayCoinBuilder();
            operationPayCoin.setDestAddress(destAddress);
            operationPayCoin.setAmount(amount);
        } catch (SDKException sdkException) {
            throw sdkException;
        } catch (Exception e) {
            throw new SDKException(SdkError.SYSTEM_ERROR.getCode(), e.getMessage());
        }

        return operation.build();
    }
}
