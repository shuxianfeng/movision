package com.movision.controller.boss;

import com.movision.common.Response;
import com.movision.facade.boss.GoodsListFacade;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsCom;
import com.movision.mybatis.goods.entity.GoodsImg;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessment;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentVo;
import com.movision.mybatis.goodscombo.entity.GoodsCombo;
import com.movision.mybatis.goodscombo.entity.GoodsComboDetail;
import com.movision.mybatis.goodscombo.entity.GoodsComboVo;
import com.movision.utils.file.FileUtil;
import com.movision.utils.oss.MovisionOssClient;
import com.movision.utils.pagination.model.Paging;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhanglei
 * @Date 2017/2/23 9:51
 */
@RestController
@RequestMapping("/boss/goods")
public class GoodsController {

    @Autowired
    GoodsListFacade goodsFacade = new GoodsListFacade();
    @Autowired
    private MovisionOssClient movisionOssClient;

    /**
     * 商品管理*--商品列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "商品列表（分页）", notes = "商品列表（分页）", response = Response.class)
    @RequestMapping(value = "query_goods_list", method = RequestMethod.POST)
    public Response queryGoodsList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                   @RequestParam(required = false, defaultValue = "10") String pageSize
    ) {
        Response response = new Response();
        Paging<GoodsVo> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<GoodsVo> list = goodsFacade.queryGoodsList(pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 商品管理-删除商品
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除商品", notes = "删除商品", response = Response.class)
    @RequestMapping(value = "delete_goods", method = RequestMethod.POST)
    public Response deleteGoods(@ApiParam(value = "商品编号") @RequestParam Integer id) {
        Response response = new Response();
        int result = goodsFacade.deleteGoods(id);
        if (response.getCode() == 200) {
            response.setMessage("删除成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 商品管理-删除评价
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除评价", notes = "删除评价", response = Response.class)
    @RequestMapping(value = "delete_assesment", method = RequestMethod.POST)
    public Response deleteAssessment(@ApiParam(value = "评价编号") @RequestParam Integer id) {
        Response response = new Response();
        int result = goodsFacade.deleteAssessment(id);
        if (response.getCode() == 200) {
            response.setMessage("删除成功");
        }
        response.setData(result);
        return response;
    }
    /**
     * 商品管理---条件查询
     *
     * @param name
     * @param producttags
     * @param brand
     * @param protype
     * @param isdel
     * @param allstatue
     * @param minorigprice
     * @param maxorigprice
     * @param
     * @param
     * @param
     * @param
     * @param
     * @param
     * @param
     * @param
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "条件查询", notes = "条件查询", response = Response.class)
    @RequestMapping(value = "query_goods_condition", method = RequestMethod.POST)
    public Response queryGoodsCondtion(@ApiParam(value = "商品名称") @RequestParam(required = false) String name,
                                       @ApiParam(value = "商品标签") @RequestParam(required = false) String producttags,
                                       @ApiParam(value = "商品品牌") @RequestParam(required = false) String brand,
                                       @ApiParam(value = "商品分类") @RequestParam(required = false) String protype,
                                       @ApiParam(value = "商品状态") @RequestParam(required = false) String isdel,
                                       @ApiParam(value = "商品类型") @RequestParam(required = false) String allstatue,
                                       @ApiParam(value = "最小原价") @RequestParam(required = false) String minorigprice,
                                       @ApiParam(value = "最大原价") @RequestParam(required = false) String maxorigprice,
                                       @ApiParam(value = "排序") @RequestParam(required = false) String pai,
                                       @ApiParam(value = "开始时间") @RequestParam(required = false) String mintime,
                                       @ApiParam(value = "结束时间") @RequestParam(required = false) String maxtime,
                                       @RequestParam(required = false) String pageNo,
                                       @RequestParam(required = false) String pageSize
    ) {
        Response response = new Response();
        Paging<GoodsVo> pager = new Paging<GoodsVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<GoodsVo> list = goodsFacade.queryGoodsCondition(name, producttags, brand, protype, isdel, allstatue, minorigprice, maxorigprice, pai, mintime, maxtime, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;

    }

    /**
     * 商品管理--上架
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "上架下架", notes = "上架下架", response = Response.class)
    @RequestMapping(value = "grounding_goods", method = RequestMethod.POST)
    public Response queryByGoods(@ApiParam(value = "商品id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        int result = goodsFacade.queryByGoods(id);
        if (response.getCode() == 200) {
            response.setMessage("上架下架成功");
        }
        response.setData(result);
        return response;
    }


    /**
     * 商品管理--推荐到热门
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "推荐到热门", notes = "推荐到热门", response = Response.class)
    @RequestMapping(value = "recommend_ishot", method = RequestMethod.POST)
    public Response recommendishot(@ApiParam(value = "商品id") @RequestParam Integer id) {
        Response response = new Response();
        int result = goodsFacade.queryHot(id);
        if (response.getCode() == 200) {
            response.setMessage("上架成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 商品管理--推荐到精选
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "推荐到精选", notes = "推荐到精选", response = Response.class)
    @RequestMapping(value = "recommend_isessence", method = RequestMethod.POST)
    public Response recommendisessence(@ApiParam(value = "商品id") @RequestParam Integer id) {
        Response response = new Response();
        int result = goodsFacade.queryisessence(id);
        if (response.getCode() == 200) {
            response.setMessage("上架成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 商品管理*--修改推荐日期
     *
     * @param id
     * @param recommenddate
     * @return
     */
    @ApiOperation(value = "修改推荐日期", notes = "修改推荐日期", response = Response.class)
    @RequestMapping(value = "update_recommenddate", method = RequestMethod.POST)
    public Response updateDate(@ApiParam(value = "商品id") @RequestParam String id,
                               @ApiParam(value = "推荐日期") @RequestParam(required = false) String recommenddate) {
        Response response = new Response();
        Map<String, Integer> map = goodsFacade.updateDate(id, recommenddate);
        if (response.getCode() == 200) {
            response.setMessage("修改成功");
        }
        response.setData(map);
        return response;
    }


    /**
     * 查询品牌
     *
     * @return
     */
    @ApiOperation(value = "查询品牌", notes = "查询品牌", response = Response.class)
    @RequestMapping(value = "query_brand_list", method = RequestMethod.POST)
    public Response queryAllBrand() {
        Response response = new Response();
        List<GoodsVo> goodsVo = goodsFacade.queryAllBrand();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsVo);
        return response;
    }

    /**
     * 查询类别
     *
     * @return
     */
    @ApiOperation(value = "查询类别", notes = "查询类别", response = Response.class)
    @RequestMapping(value = "query_type_list", method = RequestMethod.POST)
    public Response queryAllType() {
        Response response = new Response();
        List<GoodsVo> goodsVo = goodsFacade.queryAllType();
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsVo);
        return response;
    }

    /**
     * 根据id查询商品
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询商品", notes = "根据id查询商品", response = Response.class)
    @RequestMapping(value = "query_byidgood_list", method = RequestMethod.POST)
    public Response queryGoodDetail(@ApiParam(value = "商品id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        GoodsVo goodsVo = goodsFacade.queryGoodDetail(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsVo);
        return response;
    }

    /**
     * 取消今日推荐
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "取消今日推荐", notes = "取消今日推荐", response = Response.class)
    @RequestMapping(value = "update_com", method = RequestMethod.POST)
    public Response updateCom(@ApiParam(value = "商品id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        int result = goodsFacade.updateCom(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 今日推荐
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "今日推荐", notes = "今日推荐", response = Response.class)
    @RequestMapping(value = "update_todaycom", method = RequestMethod.POST)
    public Response todayCommend(@ApiParam(value = "商品id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        Goods result = goodsFacade.todayCommend(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 商品管理*--评价列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "评价列表（分页）", notes = "评价列表（分页）", response = Response.class)
    @RequestMapping(value = "query_assessment_list", method = RequestMethod.POST)
    public Response queryAllAssessment(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                       @RequestParam(required = false, defaultValue = "10") String pageSize
    ) {
        Response response = new Response();
        Paging<GoodsAssessmentVo> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<GoodsAssessmentVo> list = goodsFacade.queryAllAssessment(pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 根据条件查询
     *
     * @param pageNo
     * @param pageSize
     * @param nickname
     * @param content
     * @param mintime
     * @param maxtime
     * @return
     */
    @ApiOperation(value = "根据条件查询（分页）", notes = "根据条件查询（分页）", response = Response.class)
    @RequestMapping(value = "query_assessmentcondition_list", method = RequestMethod.POST)
    public Response queryAllAssessmentCondition(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                                @RequestParam(required = false, defaultValue = "10") String pageSize,
                                                @ApiParam(value = "昵称") @RequestParam(required = false) String nickname,
                                                @ApiParam(value = "内容") @RequestParam(required = false) String content,
                                                @ApiParam(value = "开始时间") @RequestParam(required = false) String mintime,
                                                @ApiParam(value = "排序") @RequestParam(required = false) String pai,
                                                @ApiParam(value = "结束时间") @RequestParam(required = false) String maxtime) {
        Response response = new Response();
        Paging<GoodsAssessmentVo> pager = new Paging<GoodsAssessmentVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<GoodsAssessmentVo> list = goodsFacade.queryAllAssessmentCondition(nickname, content, mintime, pai, maxtime, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(list);
        response.setData(pager);
        return response;
    }

    /**
     * 评价详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "评价详情", notes = "评价详情", response = Response.class)
    @RequestMapping(value = "query_assessmentremark", method = RequestMethod.POST)
    public Response queryAssessmentRemark(@ApiParam(value = "评价id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        GoodsAssessmentVo goodsAssessmentVo = goodsFacade.queryAssessmentRemark(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsAssessmentVo);
        return response;
    }

    /**
     * 商品参数图
     *
     * @param goodsid
     * @return
     */
    @ApiOperation(value = "商品参数图", notes = "商品参数图", response = Response.class)
    @RequestMapping(value = "query_goods_img", method = RequestMethod.POST)
    public Response queryImgGoods(@ApiParam(value = "商品id") @RequestParam(required = false) Integer goodsid) {
        Response response = new Response();
        GoodsImg goodsImg = goodsFacade.queryImgGoods(goodsid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsImg);
        return response;
    }

    /**
     * 商品描述图
     *
     * @param goodsid
     * @return
     */
    @ApiOperation(value = "商品描述图", notes = "商品描述图", response = Response.class)
    @RequestMapping(value = "query_commodityDescription_img", method = RequestMethod.POST)
    public Response queryCommodityDescription(@ApiParam(value = "商品id") @RequestParam(required = false) Integer goodsid) {
        Response response = new Response();
        GoodsImg goodsImg = goodsFacade.queryCommodityDescription(goodsid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsImg);
        return response;
    }

    /**
     * 商品图
     *
     * @param goodsid
     * @return
     */
    @ApiOperation(value = "商品图", notes = "商品图", response = Response.class)
    @RequestMapping(value = "query_allgoods_img", method = RequestMethod.POST)
    public Response queryAllGoodsPicture(@ApiParam(value = "商品id") @RequestParam Integer goodsid) {
        Response response = new Response();
        List<GoodsImg> goodsImg = goodsFacade.queryAllGoodsPicture(goodsid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsImg);
        return response;
    }
    /**
     * 晒图
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "晒图", notes = "晒图", response = Response.class)
    @RequestMapping(value = "query_blueprint_img", method = RequestMethod.POST)
    public Response queryblueprint(@ApiParam(value = "评价id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        List<GoodsImg> goodsImg = goodsFacade.queryblueprint(id);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsImg);
        return response;
    }

    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除图片", notes = "删除图片", response = Response.class)
    @RequestMapping(value = "delete_goodspicture", method = RequestMethod.POST)
    public Response deleteGoodsPicture(@ApiParam(value = "商品id") @RequestParam(required = false) Integer id) {
        Response response = new Response();
        int result = goodsFacade.deleteGoodsPicture(id);
        if (response.getCode() == 200) {
            response.setMessage("删除成功");
        }
        response.setData(result);
        return response;
    }

    /**
     * 回复评论
     *
     * @param content
     * @return
     */
    @ApiOperation(value = "回复评论", notes = "回复评论", response = Response.class)
    @RequestMapping(value = "add_assessment", method = RequestMethod.POST)
    public Response addAssessment(@ApiParam(value = "回复内容") @RequestParam(required = false) String content,
                                  @ApiParam(value = "商品id") @RequestParam(required = false) String goodid,
                                  @ApiParam(value = "父评论id") @RequestParam(required = false) String pid) {
        Response response = new Response();
        Map<String, Integer> goodsAssessment = goodsFacade.addAssessment(content, goodid, pid);
        if (response.getCode() == 200) {
            response.setMessage("回复成功");
        }
        response.setData(goodsAssessment);
        return response;
    }


    /**
     * 修改参数图
     *
     * @param
     * @param
     * @param imgurl
     * @return
     */
    @ApiOperation(value = "修改参数图", notes = "修改参数图", response = Response.class)
    @RequestMapping(value = "update_cimg", method = RequestMethod.POST)
    public Response updateImgGoods(
            @ApiParam(value = "商品id") @RequestParam(required = false) String goodsid,
            @ApiParam(value = "地址") @RequestParam(required = false) String imgurl) {
        Response response = new Response();
        Map<String, Integer> map = goodsFacade.updateImgGoods(goodsid, imgurl);
        if (response.getCode() == 200) {
            response.setMessage("修改成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 修改描述图
     *
     * @param
     * @param
     * @param imgurl
     * @return
     */
    @ApiOperation(value = "修改描述图", notes = "修改描述图", response = Response.class)
    @RequestMapping(value = "update_CommodityDescription", method = RequestMethod.POST)
    public Response updateCommodityDescription(
            @ApiParam(value = "商品id") @RequestParam String goodsid,
            @ApiParam(value = "地址") @RequestParam(required = false) String imgurl) {
        Response response = new Response();
        Map<String, Integer> map = goodsFacade.updateCommodityDescription(goodsid, imgurl);
        if (response.getCode() == 200) {
            response.setMessage("修改成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 增加商品图片
     *
     * @param
     * @param
     * @param imgurl
     * @return
     */
    @ApiOperation(value = "增加商品图片", notes = "增加商品图片", response = Response.class)
    @RequestMapping(value = "add_goodspicture", method = RequestMethod.POST)
    public Response addpicture(
            @ApiParam(value = "商品id") @RequestParam(required = false) String goodsid,
            @ApiParam(value = "地址") @RequestParam(required = false) String imgurl,
            @ApiParam(value = "排序") @RequestParam(required = false) String orderid) {
        Response response = new Response();
        Map<String, Integer> map = goodsFacade.addpicture(goodsid, imgurl, orderid);
        if (response.getCode() == 200) {
            response.setMessage("增加成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 增加商品
     *
     * @param
     * @param imgurl
     * @param
     * @param name
     * @param protype
     * @param brandid
     * @param price
     * @param origprice
     * @param stock
     * @param isdel
     * @param recommenddate
     * @param attribute
     * @param onlinetime
     * @return
     */
    @ApiOperation(value = "增加商品", notes = "增加商品", response = Response.class)
    @RequestMapping(value = "add_goods", method = RequestMethod.POST)
    public Response addGoods(
            @ApiParam(value = "图片地址") @RequestParam String imgurl,
            @ApiParam(value = "商品名称") @RequestParam String name,
            @ApiParam(value = "商品类别") @RequestParam String protype,
            @ApiParam(value = "品牌id") @RequestParam String brandid,
            @ApiParam(value = "折后价") @RequestParam String price,
            @ApiParam(value = "原价") @RequestParam String origprice,
            @ApiParam(value = "库存") @RequestParam String stock,
            @ApiParam(value = "是否上架") @RequestParam String isdel,
            @ApiParam(value = "推荐日期") @RequestParam(required = false) String recommenddate,
            @ApiParam(value = "商品标签") @RequestParam String attribute,
            @ApiParam(value = "上架时间") @RequestParam String onlinetime,
            @ApiParam(value = "热门精选") @RequestParam String ishot,
            @ApiParam(value = "精选") @RequestParam String isessence) {
        Response response = new Response();
        Map<String, Integer> map = goodsFacade.addGoods(imgurl, name, protype, brandid, price, origprice, stock, isdel, recommenddate, attribute, onlinetime, ishot, isessence);
        if (response.getCode() == 200) {
            response.setMessage("插入成功");
        }
        response.setData(map);
        return response;

    }


    /**
     * 修改商品
     *
     * @param
     * @param imgurl
     * @param name
     * @param protype
     * @param id
     * @param price
     * @param origprice
     * @param stock
     * @param isdel
     * @param recommenddate
     * @param brandid
     * @param
     * @param attribute
     * @return
     */
    @ApiOperation(value = "修改商品", notes = "修改商品", response = Response.class)
    @RequestMapping(value = "update_goods", method = RequestMethod.POST)
    public Response updateGoods(
            @ApiParam(value = "图片地址") @RequestParam String imgurl,
            @ApiParam(value = "商品名称") @RequestParam String name,
            @ApiParam(value = "商品类别") @RequestParam String protype,
            @ApiParam(value = "商品id") @RequestParam String id,
            @ApiParam(value = "折后价") @RequestParam String price,
            @ApiParam(value = "原价") @RequestParam String origprice,
            @ApiParam(value = "库存") @RequestParam String stock,
            @ApiParam(value = "是否上架") @RequestParam String isdel,
            @ApiParam(value = "推荐日期") @RequestParam(required = false) String recommenddate,
            @ApiParam(value = "品牌id") @RequestParam String brandid,
            @ApiParam(value = "热选") @RequestParam String ishot,
            @ApiParam(value = "精选") @RequestParam String isessence,
            @ApiParam(value = "商品标签") @RequestParam String attribute
    ) {
        Response response = new Response();
        Map<String, Object> map = goodsFacade.updateGoods(imgurl, name, protype, id, price, origprice, stock, isdel, recommenddate, brandid, ishot, isessence, attribute);
        if (response.getCode() == 200) {
            response.setMessage("修改成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 上传商品图片
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "上传商品图片", notes = "上传商品图片", response = Response.class)
    @RequestMapping(value = {"/upload_good_pic"}, method = RequestMethod.POST)
    public Response updateMyInfo(@RequestParam(value = "file", required = false) MultipartFile file) {

        String url = movisionOssClient.uploadObject(file, "img", "good");
        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        map.put("name", FileUtil.getFileNameByUrl(url));
        return new Response(map);
    }

    /**
     * 商品管理*--套餐列表
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "套餐列表（分页）", notes = "套餐列表（分页）", response = Response.class)
    @RequestMapping(value = "query_combo_list", method = RequestMethod.POST)
    public Response queryComList(@RequestParam(required = false, defaultValue = "1") String pageNo,
                                 @RequestParam(required = false, defaultValue = "10") String pageSize
    ) {
        Response response = new Response();
        Paging<GoodsComboVo> pager = new Paging<>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<GoodsComboVo> list = goodsFacade.queryCom(pager);
        pager.result(list);
        response.setData(pager);
        return response;
    }


    /**
     * 根据id查询商品
     *
     * @param comboid
     * @return
     */
    @ApiOperation(value = "根据id查询商品", notes = "根据id查询商品", response = Response.class)
    @RequestMapping(value = "query_comboid_name", method = RequestMethod.POST)
    public Response queryName(@ApiParam(value = "套餐id") @RequestParam(required = false) Integer comboid) {
        Response response = new Response();
        GoodsComboVo goodsComboVo = goodsFacade.queryName(comboid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(goodsComboVo);
        return response;
    }

    /**
     * 删除套餐
     *
     * @param comboid
     * @return
     */
    @ApiOperation(value = "删除套餐", notes = "删除套餐", response = Response.class)
    @RequestMapping(value = "delete_com", method = RequestMethod.POST)
    public Response deleteComGoods(@ApiParam(value = "套餐id") @RequestParam(required = false) Integer comboid) {
        Response response = new Response();
        int res = goodsFacade.deleteComGoods(comboid);
        if (response.getCode() == 200) {
            response.setMessage("删除成功");
        }
        response.setData(res);
        return response;
    }

    /**
     * 套餐搜索
     *
     * @param comboname
     * @param name
     * @param allstatue
     * @param comboid
     * @param minrex
     * @param maxrex
     * @param mintime
     * @param maxtime
     * @param pai
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "套餐搜索", notes = "套餐搜索", response = Response.class)
    @RequestMapping(value = "query_comcondition", method = RequestMethod.POST)
    public Response findAllComCondition(@ApiParam(value = "套餐名称") @RequestParam(required = false) String comboname,
                                        @ApiParam(value = "商品名称") @RequestParam(required = false) String name,
                                        @ApiParam(value = "状态") @RequestParam(required = false) String allstatue,
                                        @ApiParam(value = "套餐id") @RequestParam(required = false) String comboid,
                                        @ApiParam(value = "最小值") @RequestParam(required = false) String minrex,
                                        @ApiParam(value = "最大值") @RequestParam(required = false) String maxrex,
                                        @ApiParam(value = "最小时间") @RequestParam(required = false) String mintime,
                                        @ApiParam(value = "最大时间") @RequestParam(required = false) String maxtime,
                                        @ApiParam(value = "排序") @RequestParam(required = false) String pai,
                                        @RequestParam(required = false, defaultValue = "1") String pageNo,
                                        @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<GoodsComboVo> pager = new Paging<GoodsComboVo>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<GoodsComboVo> list = goodsFacade.findAllComCondition(comboname, name, allstatue, comboid, minrex, maxrex, mintime, maxtime, pai, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(pager);
        pager.result(list);
        return response;

    }

    /**
     * 根据id查询
     *
     * @param comboid
     * @return
     */
    @ApiOperation(value = "根据id查询", notes = "根据id查询", response = Response.class)
    @RequestMapping(value = "query_byid_com", method = RequestMethod.POST)
    public Response findByIdCom(@ApiParam(value = "套餐id") @RequestParam(required = false) Integer comboid) {
        Response response = new Response();
        List<GoodsComboVo> map = goodsFacade.findByIdCom(comboid);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 根据套餐id查询商品信息
     *
     * @param comboid
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "根据套餐id查询商品信息", notes = "根据套餐id查询商品信息", response = Response.class)
    @RequestMapping(value = "query_byid_com_good", method = RequestMethod.POST)
    public Response findAllGoods(@ApiParam(value = "套餐id") @RequestParam Integer comboid,
                                 @RequestParam(required = false, defaultValue = "1") String pageNo,
                                 @RequestParam(required = false, defaultValue = "10") String pageSize) {
        Response response = new Response();
        Paging<GoodsCom> pager = new Paging<GoodsCom>(Integer.valueOf(pageNo), Integer.valueOf(pageSize));
        List<GoodsCom> map = goodsFacade.findAllGoods(comboid, pager);
        if (response.getCode() == 200) {
            response.setMessage("查询成功");
        }
        pager.result(map);
        response.setData(pager);
        return response;
    }

    /**
     * 修改套餐
     *
     * @param comboid
     * @param comboname
     * @param imgurl
     * @param combodiscountprice
     * @return
     */
    @ApiOperation(value = "修改套餐", notes = "修改套餐", response = Response.class)
    @RequestMapping(value = "update_combo", method = RequestMethod.POST)
    public Response updateComDetail(@ApiParam(value = "套餐id") @RequestParam String comboid,
                                    @ApiParam(value = "套餐名字") @RequestParam(required = false) String comboname,
                                    @ApiParam(value = "图片地址") @RequestParam(required = false) String imgurl,
                                    @ApiParam(value = "折后价") @RequestParam(required = false) String combodiscountprice) {
        Response response = new Response();
        Map<String, Integer> map = goodsFacade.updateComDetail(imgurl, comboname, combodiscountprice, comboid);
        if (response.getCode() == 200) {
            response.setMessage("修改成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 插入套餐
     *
     * @param imgurl
     * @param comboname
     * @param combodiscountprice
     * @param goodsid
     * @return
     */
    @ApiOperation(value = "插入套餐", notes = "插入套餐", response = Response.class)
    @RequestMapping(value = "add_combo", method = RequestMethod.POST)
    public Response addComGoods(@ApiParam(value = "套餐图") @RequestParam(required = false) String imgurl,
                                @ApiParam(value = "套餐id") @RequestParam(required = false) String comboid,
                                @ApiParam(value = "套餐名字") @RequestParam(required = false) String comboname,
                                @ApiParam(value = "折后价") @RequestParam(required = false) String combodiscountprice,
                                @ApiParam(value = "商品id") @RequestParam(required = false) String goodsid) {
        Response response = new Response();
        Map<String, Integer> map = goodsFacade.addCom(imgurl, comboid, comboname, combodiscountprice, goodsid);
        if (response.getCode() == 200) {
            response.setMessage("插入成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 增加参数图
     *
     * @param imgurl
     * @param goodsid
     * @return
     */
    @ApiOperation(value = "增加参数图", notes = "增加参数图", response = Response.class)
    @RequestMapping(value = "add_imgpic", method = RequestMethod.POST)
    public Response addImgGoods(@ApiParam(value = "图片地址") @RequestParam(required = false) String imgurl,
                                @ApiParam(value = "商品id") @RequestParam(required = false) String goodsid) {
        Response response = new Response();
        Map<String, Integer> map = goodsFacade.addImgGoods(imgurl, goodsid);
        if (response.getCode() == 200) {
            response.setMessage("增加成功");
        }
        response.setData(map);
        return response;
    }

    /**
     * 增加描述图
     *
     * @param imgurl
     * @param goodsid
     * @return
     */
    @ApiOperation(value = "增加描述图", notes = "增加描述图", response = Response.class)
    @RequestMapping(value = "add_commoditypic", method = RequestMethod.POST)
    public Response addCommodityDescription(@ApiParam(value = "图片地址") @RequestParam(required = false) String imgurl,
                                            @ApiParam(value = "商品id") @RequestParam(required = false) String goodsid) {
        Response response = new Response();
        Map<String, Integer> map = goodsFacade.addCommodityDescription(imgurl, goodsid);
        if (response.getCode() == 200) {
            response.setMessage("增加成功");
        }
        response.setData(map);
        return response;
    }

}
