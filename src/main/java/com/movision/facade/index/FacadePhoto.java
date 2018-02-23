package com.movision.facade.index;

import com.movision.common.constant.PhotoConstant;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.photo.entity.Photo;
import com.movision.mybatis.photo.entity.PhotoVo;
import com.movision.mybatis.photo.service.PhotoService;
import com.movision.mybatis.photoOrder.entity.PhotoOrder;
import com.movision.mybatis.photoOrder.entity.PhotoOrderVo;
import com.movision.mybatis.photoOrder.service.PhotoOrderService;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2018/2/2 14:26
 */
@Service
public class FacadePhoto {
    private static Logger log = LoggerFactory.getLogger(FacadePhoto.class);
    @Autowired
    private PhotoService photoService;
    @Autowired
    private PhotoOrderService photoOrderService;
    @Autowired
    private FacadePost facadePost;

    /**
     *
     *发布约拍
     * @param userid
     * @param title
     * @param money
     * @param content
     * @param intime
     * @param returnMap
     * @param personnal
     * @param city
     * @param subjectMatter
     * @param personnalnumber
     * @return
     */
    @Transactional
    public Map releaseAnAppointmentPost(String userid, String title, String money, String content, String intime, String returnMap,
                                        String personnal, String city, String subjectMatter, String personnalnumber, String labellist){

        Map map = new HashMap();
        Photo photo = new Photo();
        try {
            if (StringUtil.isNotEmpty(userid)) {
                photo.setUserid(Integer.parseInt(userid));
            }
            if (StringUtil.isNotEmpty(title)) {
                photo.setTitle(title);
            }
            if (StringUtil.isNotEmpty(content)) {
                photo.setContent(content);
            }
            if (StringUtil.isNotEmpty(city)) {
                photo.setCity(city);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StringUtil.isNotEmpty(intime)) {
                try {
                    photo.setIntime(sdf.parse(intime));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (StringUtil.isNotEmpty(money)) {
                photo.setMoney(Double.parseDouble(money));
            }
            if (StringUtil.isNotEmpty(returnMap)) {
                photo.setReturnmap(Integer.parseInt(returnMap));
            }
            if (StringUtil.isNotEmpty(personnal)) {
                if (personnal.equals("0")) {
                    if (StringUtil.isNotEmpty(personnalnumber)) {
                        photo.setPersonnalnumber(Integer.parseInt(personnalnumber));
                    }
                }
            }
            if (StringUtil.isNotEmpty(subjectMatter)) {
                photo.setSubjectmatter(subjectMatter);
            }
            photo.setIsdel(0);
            photo.setPubdate(new Date());
            photo.setStatus(0);
            photoService.insertSelective(photo);
            //返回的主键--帖子id
            int flag = photo.getId();
            //3 标签业务逻辑处理
            if (StringUtils.isNotBlank(labellist)) {
                facadePost.addLabelProcess(labellist, flag);
            }
            map.put("flag", flag);
            return map;
        }catch (Exception e){
            log.error("系统异常，APP发帖失败");
            map.put("flag", -2);
            e.printStackTrace();
            return map;
        }
    }


    /**
     *查询约拍详情
     * @return
     */
    public PhotoVo queryPhotoDetails(String id){
        PhotoVo photoVo = photoService.queryPhotoDetails(Integer.parseInt(id));
        List<PhotoOrderVo> photoOrderVos=photoOrderService.queryAllPhotoOrder(Integer.parseInt(id));
        photoVo.setPhotoOrders(photoOrderVos);
        return photoVo;
    }

    /**
     * 查询约拍列表
     * @param photoVoPaging
     * @return
     */
    public List<PhotoVo> findAllPhoto(String title,String money,String city,Paging<PhotoVo> photoVoPaging){
        Map map = new HashMap();
        if(StringUtil.isNotEmpty(title)){
            map.put("title",title);
        }
        if(StringUtil.isNotEmpty(money)){
            map.put("money",money);
        }
        if(StringUtil.isNotEmpty(city)){
            map.put("city",city);
        }
        List<PhotoVo> photoVo=photoService.findAllPhoto(map,photoVoPaging);
        return photoVo;
    }

    /**
     * 添加竞拍者
     * @param userid
     * @param money
     * @param photoid
     * @return
     */
    public Map  insertPhotoOrder(String userid,String money,String photoid){
        //根据photoid查询发布者
        Photo photo=photoService.selectCreditScore(Integer.parseInt(photoid));
        int uid=photo.getUserid();//发布者
        PhotoOrder photoOrder = new PhotoOrder();
        Map map = new HashMap();
        //查询该用户有没有竞拍过
        Map  map1 = new HashMap();
        map1.put("userid",userid);
        map1.put("id",photoid);
        int count=photoOrderService.selectOrderCount(map1);
         try {
            Map mapp = new HashMap();
            if(uid!=Integer.parseInt(userid)) {
                if(count==0) {
                    if (StringUtil.isNotEmpty(userid)) {
                        photoOrder.setUserid(Integer.parseInt(userid));
                    }
                    if (StringUtil.isNotEmpty(userid)) {
                        photoOrder.setMoney(Double.parseDouble(money));
                    }
                    if (StringUtil.isNotEmpty(userid)) {
                        photoOrder.setPhotoid(Integer.parseInt(photoid));
                    }
                    photoOrder.setIntime(new Date());
                    photoOrder.setIsselect(0);
                    int flag = photoOrderService.insertSelective(photoOrder);
                    //修改约拍的状态为1
                    int status = PhotoConstant.TYPE.pendingorder.getCode();
                    mapp.put("status", status);
                    mapp.put("id", photoid);
                    int ss = photoService.updatePhotoStatus(mapp);
                    map.put("flag", flag);
                    return map;
                }else {
                    log.error("您已竞拍过该约拍");
                    map.put("flag", -3);
                    return map;
                }
            }else {
                log.error("发布者不能自己竞拍自己的");
                map.put("flag", -1);
                return map;
            }
        }catch (Exception e){
            log.error("系统异常，添加竞拍人失败");
            map.put("flag", -2);
            e.printStackTrace();
            return map;
        }
    }


    /**
     *约拍信用系统
     * @param flag 0 好评 1 中评 2 差评
     * @param id
     * @return
     */
    public int photoCreditScore(int flag,String id,String uid){
        //根据id去查询这条信息的价格
        Map map = new HashMap();
        Photo photo=photoService.selectCreditScore(Integer.parseInt(id));
        Double money=photo.getMoney();
        Double zm = money * 0.6;
        int userid=photo.getUserid();//发布方
        //查询接单人的id
        int juid=photoOrderService.selectOrderUserid(Integer.parseInt(id));
        int update=0;
        if(userid==Integer.parseInt(uid)) {//发布方对于接单人进行评价
            if (flag == 0) {
                //修改用户表中的信用分
                map.put("money", Integer.parseInt(new java.text.DecimalFormat("0").format(money)));
                map.put("userid", userid);
                update = photoService.updateCreditScore(map);
            } else if (flag == 1) {
                map.put("money", Integer.parseInt(new java.text.DecimalFormat("0").format(zm)));
                map.put("userid", userid);
                update = photoService.updateCreditScore(map);
            }
        }else if(juid==Integer.parseInt(uid)){//接单人对发布方进行评价
            if (flag == 0) {
                //修改用户表中的信用分
                map.put("money", Integer.parseInt(new java.text.DecimalFormat("0").format(money)));
                map.put("userid", userid);
                update = photoService.updateCreditScore(map);
            } else if (flag == 1) {
                map.put("money", Integer.parseInt(new java.text.DecimalFormat("0").format(zm)));
                map.put("userid", userid);
                update = photoService.updateCreditScore(map);
            }
        }
        //修改订单状态
        Map mapp = new HashMap();
        int status=PhotoConstant.TYPE.ordercompletion.getCode();
        mapp.put("status",status);
        mapp.put("id",id);
        photoService.updatePhotoStatus(mapp);
        return  update;
    }


    /**
     * 前台修改价格
     * @param userid 用户id
     * @param id 约拍id
     * @return
     */
    public Map updatePhotoMoney(String userid,String id,String money){
        Map map = new HashMap();
        //根据id查询
        Photo photo=photoService.selectCreditScore(Integer.parseInt(id));
        int uid=photo.getUserid();//发布者
        if(uid==Integer.parseInt(userid)){
            //可以修改（自己修改自己发布的约拍信息）
            //根据id修改金额
            Map mapp = new HashMap();
            mapp.put("money",Double.parseDouble(money));
            mapp.put("id",Integer.parseInt(id));
            int flag=photoService.updatePhotoMoney(mapp);
            map.put("flag",flag);
        }else {
            log.error("您没有权限修改此条信息");
            map.put("flag",-2);
        }
        return  map;
    }


    /**
     * 确定接单人
     * @param id
     * @param userid
     * @return
     */
    public int selectedOrder(String id,String userid){
        Map map = new HashMap();
        map.put("id",id);
        map.put("userid",userid);
        int flag=photoOrderService.updateOrder(map);
        //修改订单状态
        Map mapp =new HashMap();
        int status=PhotoConstant.TYPE.alreadymadeup.getCode();
        mapp.put("status",status);
        mapp.put("id",id);
        photoService.updatePhotoStatus(mapp);
        return  flag;
    }


    /**
     * 发布人确定拍摄完成
     * @param id
     * @return
     */
    public Map updateOrderPhoto(String id,String userid) {
        //根据id查询
        Photo photo = photoService.selectCreditScore(Integer.parseInt(id));
        int uid = photo.getUserid();//发布者
        Map mapp = new HashMap();
        if (uid == Integer.parseInt(userid)) {
            int status = PhotoConstant.TYPE.agencyfund.getCode();
            mapp.put("status", status);
            mapp.put("id", id);
            int flag = photoService.updatePhotoStatus(mapp);
            mapp.put("flag",flag);
            return mapp;
        }else {
            log.error("您没有权限确定拍摄");
            mapp.put("flag",-2);
            return mapp;
        }
    }


    /**
     * 修改订单类型
     */
    @Transactional
    public void  updateOrderType(int id, String trade_no, Date intime, int type, String total_amount){
        //type  支付方式：1支付宝 2微信;   ids 订单号int数组;   trade_no 支付流水号; intime 交易时间
        log.info("更新约拍订单>>>>>>>>>>>>>>>>>>>");
        Map<String, Object> parammap = new HashMap<>();
        parammap.put("id", id);
        parammap.put("trade_no", trade_no);
        parammap.put("intime", intime);
        parammap.put("type", type);
        parammap.put("money",total_amount);
        photoOrderService.updateOrderType(parammap);
        Map mapp =new HashMap();
        int status= PhotoConstant.TYPE.agencyfund.getCode();
        mapp.put("status",status);
        mapp.put("id",id);
        photoService.updatePhotoStatus(mapp);
    }


    /**
     * 是否免单
     * @param userid 免单用户
     * @param id 订单id
     */
     public int clickSure(String userid,String id){
             //免单退款
            //修改
            Map map =new HashMap();
            //自动退款

            map.put("userid",userid);
            map.put("id",id);
            int flag=photoOrderService.updateMian(map);
            return flag;
     }

}
