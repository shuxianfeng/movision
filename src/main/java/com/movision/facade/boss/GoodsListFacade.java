package com.movision.facade.boss;

import com.ibm.icu.text.SimpleDateFormat;
import com.movision.mybatis.goods.entity.Goods;
import com.movision.mybatis.goods.entity.GoodsImg;
import com.movision.mybatis.goods.entity.GoodsVo;
import com.movision.mybatis.goods.service.GoodsService;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessment;
import com.movision.mybatis.goodsAssessment.entity.GoodsAssessmentVo;
import com.movision.utils.pagination.model.Paging;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author zhanglei
 * @Date 2017/2/23 9:45
 */
@Service
public class GoodsListFacade {
    @Autowired
    GoodsService goodsService = new GoodsService();
    private static Logger log = LoggerFactory.getLogger(GoodsListFacade.class);
    @Value("#{configProperties['img.domain']}")
    private String imgdomain;
    /**
     * 商品管理--查询商品
     *
     * @param pager
     * @return
     */
    public List<GoodsVo> queryGoodsList(Paging<GoodsVo> pager) {
        List<GoodsVo> list = goodsService.queryGoodsList(pager);
        return list;
    }


    /**
     * 商品管理--删除商品
     *
     * @param id
     * @return
     */
    public int deleteGoods(Integer id) {
        return goodsService.deleteGoods(id);
    }

    /**
     * 商品管理--删除评价
     *
     * @param id
     * @return
     */
    public int deleteAssessment(Integer id) {
        return goodsService.deleteAssessment(id);
    }


    /**
     * 商品管理--上架
     *
     * @param id
     * @return
     */
    public int queryByGoods(Integer id) {
        int isdel = goodsService.queryisdel(id);
        int result = 0;
        if (isdel == 1) {
            result = goodsService.queryByGoods(id);
        } else if (isdel == 0) {
            result = goodsService.queryByGoodsDown(id);
        }
        return result;
    }


    /**
     * 商品管理--推荐到热门
     * @param id
     * @return
     */
    public int queryHot(Integer id) {
        return goodsService.queryHot(id);
    }

    /**
     * 商品管理--推荐到精选
     *
     * @param id
     * @return
     */
    public int queryisessence(Integer id) {
        return goodsService.queryisessence(id);
    }

    /**
     * 商品管理--条件查询
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
     * @return
     */
    public List<GoodsVo> queryGoodsCondition(String name, String producttags, String brand, String protype, String isdel, String allstatue, String minorigprice, String maxorigprice,
                                             String pai, String mintime, String maxtime, Paging<GoodsVo> pager) {

        Map<String, Object> map = new HashedMap();
        if (name != null) {
            map.put("name", name);
        }
        if (producttags != null) {
            map.put("producttags", producttags);
        }
        if (brand != null) {
            map.put("band", brand);
        }
        if (protype != null) {
            map.put("protype", protype);
        }
        if (isdel != null) {
            map.put("isdel", isdel);
        }
        if (allstatue != null) {
            map.put("allstatue", allstatue);
        }
        if (minorigprice != null) {
            map.put("minorigprice", minorigprice);
        }
        if (maxorigprice != null) {
            map.put("maxorigprice", maxorigprice);
        }

        Date isessencetime = null;//开始时间
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        if (mintime != null) {
            try {
                isessencetime = format.parse(mintime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("mintime", isessencetime);
        Date max = null;//最大时间
        if (maxtime != null) {
            try {
                max = format.parse(maxtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("maxtime", max);
        if (pai != null) {
            map.put("pai", pai);
        }
        return goodsService.queryGoodsCondition(map, pager);
    }

    /**
     * 商品管理--修改推荐日期
     *
     * @param id
     * @param recommenddate
     * @return
     */
    public Map<String, Integer> updateDate(String id, String recommenddate) {
        Map<String, Integer> map = new HashedMap();
        GoodsVo goodsVo = new GoodsVo();
        goodsVo.setId(Integer.parseInt(id));
        Date date = null;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        if (recommenddate != null) {
            try {
                date = format.parse(recommenddate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        goodsVo.setRecommenddate(date);
        int result = goodsService.updateDate(goodsVo);
        map.put("result", result);
        return map;
    }


    /**
     * 查询所有品牌
     *
     * @return
     */
    public List<GoodsVo> queryAllBrand() {
        List<GoodsVo> goodsVo = goodsService.findAllBrand();
        return goodsVo;
    }

    /**
     * 查询所有商品类别
     *
     * @return
     */
    public List<GoodsVo> queryAllType() {
        List<GoodsVo> goodsVo = goodsService.findAllType();
        return goodsVo;
    }

    /**
     * 根据id查商品
     *
     * @param id
     * @return
     */
    public GoodsVo queryGoodDetail(Integer id) {
        return goodsService.findGoodDetail(id);
    }

    /**
     * 修改商品
     *
     * @param imgurl
     * @param name
     * @param protype
     * @param id
     * @param price
     * @param origprice
     * @param stock
     * @param isdel
     * @param
     * @param recommenddate
     * @param brandid
     * @return
     */
    public Map<String, Integer> queryGoods(HttpServletRequest request, MultipartFile imgurl, String name, String protype, String id, String price, String origprice, String stock, String isdel, String ishot, String recommenddate, String brandid, String isessence) {
        GoodsVo goodsVo = new GoodsVo();
        Map<String, Integer> map = new HashedMap();
        goodsVo.setId(Integer.parseInt(id));
        goodsVo.setName(name);
        goodsVo.setProtype(Integer.parseInt(protype));
        goodsVo.setOrigprice(Double.parseDouble(origprice));
        goodsVo.setPrice(Double.parseDouble(price));
        goodsVo.setStock(Integer.parseInt(stock));
        goodsVo.setIsdel(Integer.parseInt(isdel));
        goodsVo.setBrandid(brandid);
        Date date = null;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        if (recommenddate != null) {
            try {
                date = format.parse(recommenddate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        goodsVo.setRecommenddate(date);
        goodsVo.setIshot(Integer.parseInt(ishot));
        goodsVo.setIsessence(Integer.parseInt(isessence));
        int result = goodsService.updateGoods(goodsVo);
        try {
            //上传图片到本地服务器
            String savedFileName = "";
            String imgurle = "";
             /*boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;*/
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (imgurl != null && isMultipart) {
                if (!imgurl.isEmpty()) {
                    String fileRealName = imgurl.getOriginalFilename();
                    int pointIndex = fileRealName.indexOf(".");
                    String fileSuffix = fileRealName.substring(pointIndex);
                    UUID FileId = UUID.randomUUID();
                    savedFileName = FileId.toString().replace("-", "").concat(fileSuffix);
                    String savedDir = request.getSession().getServletContext().getRealPath("");
                    //这里将获取的路径/WWW/tomcat-8100/apache-tomcat-7.0.73/webapps/movision后缀movision去除
                    //不保存到项目中,防止部包把图片覆盖掉了
                    String path = savedDir.substring(0, savedDir.length() - 9);
                    //这里组合出真实的图片存储路径
                    String combinpath = path + "/images/goods/coverimg/";
                    File savedFile = new File(combinpath, savedFileName);
                    System.out.println("文件url：" + combinpath + "" + savedFileName);
                    boolean isCreateSuccess = savedFile.createNewFile();
                    if (isCreateSuccess) {
                        imgurl.transferTo(savedFile);  //转存文件
                    }
                }
                imgurle = imgdomain + savedFileName;
            }
            GoodsImg img = new GoodsImg();
            img.setImgurl(imgurle);
            int res = goodsService.updateImage(img);
            map.put("result", result);
            map.put("res", res);
        } catch (Exception e) {
            log.error("修改商品异常", e);
        }
        return map;
    }

    /**
     * 取消今日推荐
     *
     * @param id
     * @return
     */
    public int updateCom(Integer id) {
        return goodsService.updateCom(id);
    }

    /**
     * 今日推荐
     *
     * @param id
     * @return
     */
    public Goods todayCommend(Integer id) {
        return goodsService.todayCommend(id);
    }

    /**
     * 评价列表
     *
     * @param pager
     * @return
     */
    public List<GoodsAssessmentVo> queryAllAssessment(Paging<GoodsAssessmentVo> pager) {
        List<GoodsAssessmentVo> list = goodsService.queryAllAssessment(pager);
        return list;
    }

    /**
     * 条件查询
     *
     * @param nickname
     * @param content
     * @param mintime
     * @param maxtime
     * @return
     */
    public List<GoodsAssessmentVo> queryAllAssessmentCondition(String nickname, String content, String mintime, String maxtime, Paging<GoodsAssessmentVo> pager) {
        Map<String, Object> map = new HashedMap();
        if (nickname != null) {
            map.put("nickname", nickname);
        }
        if (content != null) {
            map.put("content", content);
        }
        Date isessencetime = null;//开始时间
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        if (mintime != null) {
            try {
                isessencetime = format.parse(mintime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("mintime", isessencetime);
        Date max = null;//最大时间
        if (maxtime != null) {
            try {

                max = format.parse(maxtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        map.put("maxtime", max);
        return goodsService.queryAllAssessmentCodition(map, pager);
    }

    /**
     * 评价详情
     *
     * @param id
     * @return
     */
    public GoodsAssessmentVo queryAssessmentRemark(Integer id) {
        return goodsService.queryAssessmentRemark(id);
    }


    /**
     * 商品参数图
     *
     * @param id
     * @return
     */
    public List<GoodsImg> queryImgGoods(Integer id) {
        return goodsService.queryImgGoods(id);
    }

    /**
     * 商品描述图
     *
     * @param id
     * @return
     */
    public List<GoodsImg> queryCommodityDescription(Integer id) {
        return goodsService.queryCommodityDescription(id);
    }

    /**
     * 晒图
     *
     * @param id
     * @return
     */
    public List<GoodsImg> queryblueprint(Integer id) {
        return goodsService.queryblueprint(id);
    }

    /**
     * 删除图片
     *
     * @param id
     * @return
     */
    public Integer deleteGoodsPicture(Integer id) {
        return goodsService.deleteGoodsPicture(id);
    }

    /**
     * 回复评论
     *
     * @param content
     * @param
     * @param
     * @return
     */
    public Map<String, Integer> addAssessment(String content, String goodid) {
        GoodsAssessment goodsAssessment = new GoodsAssessment();
        Map<String, Integer> map = new HashedMap();
        goodsAssessment.setUserid(-1);
        goodsAssessment.setContent(content);
        goodsAssessment.setGoodid(Integer.parseInt(goodid));
        int result = goodsService.addAssessment(goodsAssessment);
        map.put("result", result);
        return map;
    }
}
