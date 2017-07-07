package com.movision.utils.oss;

import com.movision.common.constant.Constants;
import com.movision.common.constant.MsgCodeConstant;
import com.movision.exception.BusinessException;
import com.movision.utils.propertiesLoader.FormalEnvPropertiesLoader;
import com.movision.utils.propertiesLoader.TestEnvPropertiesLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author zhuangyuhao
 * @Date 2017/7/3 11:53
 */
public class UploadUtil {


    private static Logger log = LoggerFactory.getLogger(UploadUtil.class);

    /**
     * 根据服务器ip从不同的配置文件中读取属性值
     *
     * @param var 配置文件中的属性 ，如：upload.mode
     * @return
     */
    public static String getConfigVar(String var) {

        String uploadMode;
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            log.info("本机的IP = " + ip);

            if (StringUtils.isNotEmpty(ip)) {

                if (Constants.TEST_SERVER_IP.equals(ip)) {
                    uploadMode = TestEnvPropertiesLoader.getValue(var);

                } else if (Constants.FORMAL_SERVER_IP.equals(ip)) {
                    uploadMode = FormalEnvPropertiesLoader.getValue(var);

                } else {
                    log.error("获取的ip既不是测试服务器的ip，也不是正式服务器的ip， ip=" + ip);
                    throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "获取的ip既不是测试服务器的ip，也不是正式服务器的ip， ip=" + ip);
                }

            } else {
                log.error("获取服务器ip失败");
                throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "获取服务器ip失败");
            }
        } catch (UnknownHostException e) {
            throw new BusinessException(MsgCodeConstant.SYSTEM_ERROR, "不知道的主机异常：UnknownHostException");
        }
        log.info("获取的uploadMode：" + uploadMode);

        return uploadMode;
    }
}
