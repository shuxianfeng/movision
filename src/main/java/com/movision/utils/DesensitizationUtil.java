package com.movision.utils;

import com.movision.mybatis.postSensitiveWords.entity.PostSensitiveWords;
import com.movision.mybatis.postSensitiveWords.service.PostSensitiveWordsService;
import org.apache.poi.util.SystemOutLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author shuxf
 * @Date 2017/4/26 10:12
 * 脱敏工具类（输入需要脱敏的字符串）
 */
@Service
public class DesensitizationUtil {

    private static Logger log = LoggerFactory.getLogger(DesensitizationUtil.class);

    @Autowired
    private PostSensitiveWordsService postSensitiveWordsService;

    public Map<String, Object> desensitization(String str) {

        log.info("对帖子内容进行脱敏处理>>>>>>>>>>>>>>>>start");
        Map<String, Object> map = new HashMap<>();

        int flag = 0;//定义有无敏感词的标志位 =0 无 >0 有

        //查询数据库中所有的敏感词列表
        List<PostSensitiveWords> sensitiveWordsList = postSensitiveWordsService.querySensitiveList();

        //遍历词库中的所有敏感词
        for (int i = 0; i < sensitiveWordsList.size(); i++) {
            String name = sensitiveWordsList.get(i).getName();//敏感词名称
            int index = str.indexOf(name);

            //如有需要脱敏的内容直接用掩码全部替换
            if (index != -1) {

                //计算需要加*号的个数
                StringBuffer remarkBuffer = new StringBuffer();
                for (int j = 0; j < name.length(); j++) {
                    remarkBuffer.append("*");
                }
                String remark = remarkBuffer.toString();

                str = str.replaceAll(name, remark);

                flag = flag + 1;//赋值标志位
            }
        }

        if (flag == 0) {
            map.put("mark", 0);
            map.put("msg", "无敏感词");
        } else {
            map.put("mark", 1);
            map.put("msg", "有敏感词");
        }
        map.put("str", str);

        log.info("对帖子内容进行脱敏处理>>>>>>>>>>>>>>>>end");

        return map;
    }
}
