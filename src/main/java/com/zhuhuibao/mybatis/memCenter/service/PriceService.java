package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.JsonResult;
import com.zhuhuibao.mybatis.memCenter.entity.AskPrice;
import com.zhuhuibao.mybatis.memCenter.mapper.AskPriceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 询报价业务处理
 * Created by cxx on 2016/3/29 0029.
 */
@Service
@Transactional
public class PriceService {

    private static final Logger log = LoggerFactory.getLogger(PriceService.class);

    @Autowired
    AskPriceMapper askPriceMapper;
    /**
     * 询价保存
     */
    public JsonResult saveAskPrice(AskPrice askPrice){

        log.debug("询价保存");
        JsonResult result = new JsonResult();
        try{
            int isSave = askPriceMapper.saveAskPrice(askPrice);
            if(isSave==1){
                result.setCode(200);
            }else{
                result.setCode(400);
                result.setMessage("增加产品定向询价失败");
            }
        }catch (Exception e){
            log.error("saveAskPrice error!"+e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}
