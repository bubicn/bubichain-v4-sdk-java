package cn.bubi.log;

import com.google.protobuf.ByteString;
import cn.bubi.common.Constant;
import cn.bubi.common.Tools;
import cn.bubi.crypto.protobuf.Chain;
import cn.bubi.encryption.key.PublicKey;
import cn.bubi.exception.SDKException;
import cn.bubi.exception.SdkError;
import cn.bubi.model.request.operation.LogCreateOperation;

import java.util.List;

/**
 * @Author riven
 * @Date 2018/7/15 14:37
 */
public class LogServiceImpl {
    /**
     * @Author riven
     * @Method send
     * @Params [assetSendRequest]
     * @Return cn.bubi.model.response.AssetSendResponse
     * @Date 2018/7/5 11:45
     */
    public static Chain.Operation create(LogCreateOperation logCreateOperation) throws SDKException {
        Chain.Operation.Builder operation;
        try {
            String sourceAddress = logCreateOperation.getSourceAddress();
            if (!Tools.isEmpty(sourceAddress) && !PublicKey.isAddressValid(sourceAddress)) {
                throw new SDKException(SdkError.INVALID_SOURCEADDRESS_ERROR);
            }
            String topic = logCreateOperation.getTopic();
            if (Tools.isEmpty(topic) || (topic.length() < Constant.LOG_TOPIC_MIN || topic.length() > Constant.LOG_TOPIC_MAX)) {
                throw new SDKException(SdkError.INVALID_LOG_TOPIC_ERROR);
            }
            List<String> datas = logCreateOperation.getDatas();
            if (Tools.isEmpty(datas)) {
                throw new SDKException(SdkError.INVALID_LOG_DATA_ERROR);
            }
            for (int i = 0; i < datas.size(); i++) {
                String data = datas.get(i);
                if (data.length() < Constant.LOG_EACH_DATA_MIN || data.length() > Constant.LOG_EACH_DATA_MAX) {
                    throw new SDKException(SdkError.INVALID_LOG_DATA_ERROR);
                }
            }
            String metadata = logCreateOperation.getMetadata();
            // build operation
            operation = Chain.Operation.newBuilder();
            operation.setType(Chain.Operation.Type.LOG);
            if (!Tools.isEmpty(sourceAddress)) {
                operation.setSourceAddress(sourceAddress);
            }
            if (!Tools.isEmpty(metadata)) {
                operation.setMetadata(ByteString.copyFromUtf8(metadata));
            }
            Chain.OperationLog.Builder operationLog = operation.getLogBuilder();
            if (!Tools.isEmpty(sourceAddress)) {
                operation.setSourceAddress(sourceAddress);
            }
            operationLog.setTopic(topic);
            operationLog.addAllDatas(datas);
            if (!Tools.isEmpty(metadata)) {
                logCreateOperation.setMetadata(metadata);
            }
        } catch (SDKException sdkException) {
            throw sdkException;
        } catch (Exception e) {
            throw new SDKException(SdkError.SYSTEM_ERROR.getCode(), e.getMessage());
        }

        return operation.build();
    }
}
