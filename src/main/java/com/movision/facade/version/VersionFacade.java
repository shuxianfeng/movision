package com.movision.facade.version;

import com.movision.mybatis.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author shuxf
 * @Date 2017/7/12 17:11
 */
@Service
public class VersionFacade {
    private static Logger log = LoggerFactory.getLogger(VersionFacade.class);

    @Autowired
    private UserService userService;

    public int queryVersion(String type, String version){

        //根据type查询APP当前最新的版本号
        String newversion = userService.queryVersion(Integer.parseInt(type));
        String[] newstrarr = newversion.split("\\.");
        int[] newintarr = new int[newstrarr.length];
        for (int i = 0; i < newstrarr.length; i++){
            newintarr[i] = Integer.parseInt(newstrarr[i]);
        }

        //APP客户端传参过来的版本号
        String[] verstrarr = version.split("\\.");
        int[] verintarr = new int[verstrarr.length];
        for (int i = 0; i < verstrarr.length; i++){
            verintarr[i] = Integer.parseInt(verstrarr[i]);
        }

        if (verintarr[0] < newintarr[0]){
            return 0;//app版本号过低
        }else if (verintarr[1] < newintarr[1]){
            return 0;//app版本号过低
        }else if (verintarr[2] < newintarr[2]){
            return 0;//app版本号过低
        }else{
            return 1;//app版本最新
        }
    }
}
