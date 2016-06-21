package com.zhuhuibao.utils.oss;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20160308.ImageDetectionRequest;
import com.aliyuncs.green.model.v20160308.ImageDetectionResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.zhuhuibao.common.constant.MsgCodeConstant;
import com.zhuhuibao.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import java.util.List;
/**
 * @author jianglz
 * @since 16/6/21.
 */
public class ImageUtil {

    private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);

    private static String accessKeyId = "MpIeQ3cwKShE2zcV";
    private static String accessKeySecret = "0YdrhrEgFwMFc7A9DPDYbP0QxN7ejX";


    /**
     * 图片检测
     * @param imageUrl
     * @return
     */
    public static String syncImageDetection(String imageUrl) {

        //请替换成你自己的accessKeyId、accessKeySecret
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        ImageDetectionRequest imageDetectionRequest = new ImageDetectionRequest();

        /**
         * 是否同步调用
         * false: 同步
         */
        imageDetectionRequest.setAsync(false);


        /**
         * 同步图片检测支持多个场景:
         * porn:  黄图检测
         * ocr:  图文识别
         */
        imageDetectionRequest.setScenes(Arrays.asList("porn"));


        /**
         * 同步图片检测一次只支持单张图片进行检测
         */
        imageDetectionRequest.setImageUrls(Arrays.asList(imageUrl));

        try {
            ImageDetectionResponse imageDetectionResponse = client.getAcsResponse(imageDetectionRequest);

            log.debug(JSON.toJSONString(imageDetectionResponse));
            if ("Success".equals(imageDetectionResponse.getCode())) {
                List<ImageDetectionResponse.ImageResult> imageResults = imageDetectionResponse.getImageResults();
                if (imageResults != null && imageResults.size() > 0) {
                    //同步图片检测只有一个返回的ImageResult
                    ImageDetectionResponse.ImageResult imageResult = imageResults.get(0);

                    //porn场景对应的检测结果放在pornResult字段中
                    /**
                     * 黄图检测结果
                     */
                    ImageDetectionResponse.ImageResult.PornResult pornResult = imageResult.getPornResult();
                    if (pornResult != null) {
                        /**
                         * 绿网给出的建议值, 0表示正常，1表示色情，2表示需要review
                         */
                        Integer label = pornResult.getLabel();

                        /**
                         * 黄图分值, 0-100
                         */
//                        float rate = pornResult.getRate();

                        // 根据业务情况来做处理
                       if(label == 0){
                           return "Success";
                       }else{
                           return "Fail";
                        }
                    }
                    /**
                     *ocr检测结果
                     **/
//                    imageResult.getOcrResult();

                }
                return "Success";
            } else {
                /**
                 * 检测失败
                 */
              throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"图片检测失败");
            }

        } catch (ServerException e) {
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"图片检测失败");
        } catch (ClientException e) {
            e.printStackTrace();
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR,"图片检测失败");
        }

    }

}
