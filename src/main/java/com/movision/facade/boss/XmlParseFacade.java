package com.movision.facade.boss;

import com.google.gson.Gson;
import com.movision.common.constant.PointConstant;
import com.movision.facade.im.ImFacade;
import com.movision.facade.pointRecord.PointRecordFacade;
import com.movision.facade.user.AppRegisterFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.compressImg.entity.CompressImg;
import com.movision.mybatis.compressImg.service.CompressImgService;
import com.movision.mybatis.coupon.entity.Coupon;
import com.movision.mybatis.coupon.service.CouponService;
import com.movision.mybatis.couponTemp.entity.CouponTemp;
import com.movision.mybatis.imuser.entity.ImUser;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostList;
import com.movision.mybatis.post.entity.PostXml;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postLabel.service.PostLabelService;
import com.movision.mybatis.systemLayout.service.SystemLayoutService;
import com.movision.mybatis.user.entity.RegisterUser;
import com.movision.mybatis.user.entity.User;
import com.movision.mybatis.user.service.UserService;
import com.movision.utils.*;
import com.movision.utils.excel.ExcelIntoEnquiryUtil;
import com.movision.utils.excel.InsertExcelToPost;
import com.movision.utils.im.CheckSumBuilder;
import com.movision.utils.oss.AliOSSClient;
import com.movision.utils.oss.MovisionOssClient;
import com.movision.utils.pagination.model.Paging;
import javafx.geometry.Pos;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * @Author zhurui
 * @Date 2017/10/25 16:40
 */
@Service
public class XmlParseFacade {

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private MovisionOssClient movisionOssClient;

    @Autowired
    private UserService userService;

    @Autowired
    private PointRecordFacade pointRecordFacade;

    @Autowired
    private AppRegisterFacade appRegisterFacade;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ImFacade imFacade;

    @Autowired
    private PostLabelService postLabelService;

    @Autowired
    private SystemLayoutService systemLayoutService;

    @Autowired
    private PostService postService;

    @Autowired
    private ExcelIntoEnquiryUtil excelIntoEnquiryUtil;

    @Autowired
    private InsertExcelToPost insertExcelToPost;

    @Autowired
    private static ImgCompressUtil imgCompressUtil;

    @Autowired
    private CoverImgCompressUtil coverImgCompressUtil;

    @Autowired
    private AliOSSClient aliOSSClient;

    @Autowired
    private CompressImgService compressImgService;


    /**
     * 解析xml
     *
     * @param request
     * @param file
     * @param nickname
     * @param phone
     * @return
     */
    public Map analysisXml(HttpServletRequest request, MultipartFile file, String nickname, String phone, String circleid) {
        Map resault = new HashMap();
        SAXReader reader = new SAXReader();
        Post post = new Post();
        int kk = 0;
        try {
            //查询用户是否存在，不存在新增操作
            Integer usid = queryUser(nickname, phone);
            if (usid != null) {
                post.setUserid(usid);
                Document document = reader.read(file.getInputStream());
                System.out.println(document);
                //获取跟标签
                Element element = document.getRootElement();
                //获取指定根标签
                List<Element> elements = element.elements("PostItem");
                //存储本地图片路径，以便做空间释放操作
                List list = new ArrayList();
                //循环所有父节点
                for (Element e : elements) {
                    try {
                        boolean flg = false;
                        //用于拼接帖子内容
                        String content = "[";
                        //获取发帖时间并转换为long类型
                        Long publishTime = Long.parseLong(e.element("publishTime").getText());
                        Date intime = new Date(publishTime);
                        post.setIntime(intime);
                        //类型
                        String type = "";
                        if (e.element("type") != null) {
                            type = e.element("type").getText();
                        }
                        //标签
                        String tag = "";
                        if (e.element("tag") != null) {
                            tag = e.element("tag").getText();
                        }

                        //图片内容解析
                        if (type.equals("Photo")) {
                            content = getImgContentAnalysis(post, list, e, content);
                            flg = true;
                        }
                        //视频内容解析
                    /*if (type.equals("Video")) {
                        //视频内容
                        content = getVideoContentAnalysis(post, e, content);
                        flg = true;
                    }*/
                        //纯文本解析
                /*if (type.equals("Text")){
                    //文本
                    s = getTextContentAnalysis(post, e, s);
                    flg = true;
                }*/

                        if (!flg) {
                            content = "";
                        }
                        post.setIntime(new Date());
                        //post.setCircleid(125);
                        post.setPostcontent(content);
                        System.out.println("---------" + content);

                        if (content != "") {
                            //标签操作 //
                            String lbs = postLabel(post, tag);
                            //新增帖子操作
                            postFacade.addPostTest(request, "", "", circleid, post.getUserid().toString(),
                                    post.getCoverimg(), post.getPostcontent(), lbs, "", "1");
                        }
                        kk++;
                    } catch (NumberFormatException e1) {
                        continue;
                    }
                }

                //释放空间,删除本地图片
                /*for (int k = 0; k < list.size(); k++) {
                    File fi = new File(list.get(k).toString());
                    fi.delete();
                }*/
                resault.put("code", 200);
            } else {
                resault.put("code", 300);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resault.put("code", 400);
            resault.put("messager", "报错位置第" + kk + "个PostItem");
        }
        return resault;
    }

    /**
     * 导出excel
     *
     * @return
     */
    public Map exportExcel(String circleid) {
        Map resault = new HashMap();
        try {
            //t.xls为要新建的文件名
            String path = systemLayoutService.queryServiceUrl("file_xml_dwonload_img");
            Long l = new Date().getTime();
            //拼接文件名称
            String urlname = "/" + l + ".xls";
            WritableWorkbook book = Workbook.createWorkbook(new File(path + urlname));
            //生成名为“第一页”的工作表，参数0表示这是第一页
            WritableSheet sheet = book.createSheet("第一页", 0);
            Post post = new Post();
            post.setCircleid(Integer.parseInt(circleid));
            //查询出所有xml导入的帖子
            List<PostXml> posts = postService.queryPostByXmlExport(post);
            /*PostXml postXml = new PostXml();
            Field[] fields = postXml.getClass().getDeclaredFields();*/
            String title[] = {"id", "用户id", "圈子id", "标题", "帖子内容", "帖子封面"};
            //设计表头
            for (int i = 0; i < title.length; i++) {
                sheet.addCell(new Label(i, 0, title[i]));
            }
           /* Post post = new Post();
            //获取对象长度
            Field[] p = post.getClass().getDeclaredFields();*/
            //遍历循环出集合中每一个元素，写到表中
            addExcelFileElement(sheet, posts);
            //
            //写入数据
            book.write();
            if (book != null) {
                //关闭流
                book.close();
            }
            //用于返回文件路径
            String reurl = systemLayoutService.queryServiceUrl("domain_name");
            reurl += "/download/post" + urlname;
            resault.put("code", 200);
            resault.put("date", reurl);
            resault.put("massger", "成功");
            return resault;
        } catch (Exception e) {
            e.printStackTrace();
            resault.put("code", 400);
            resault.put("date", -1);
            resault.put("massger", "失败");
            return resault;
        }
    }

    /**
     * 查询xml解析的帖子列表
     *
     * @param pag
     * @return
     */
    public List<PostList> queryXmlAnalysisAndPost(Paging<PostList> pag, String circleid) {
        Post post = new Post();
        if (StringUtil.isNotEmpty(circleid)) {
            post.setCircleid(Integer.parseInt(circleid));
        }
        return postService.findAllqueryXmlAnalysisAndPost(pag, post);
    }

    /**
     * 解析Excel文件
     *
     * @param file
     * @return
     */
    public Map inputExcelToPost(HttpServletRequest request, MultipartFile file, String type) {
        if (type.equals("xml")) {
            return excelIntoEnquiryUtil.queryExcelIntoEnquiry(request, file);
        } else {
            return insertExcelToPost.queryExcelIntoEnquiry(request, file);
        }
    }

    /**
     * 为excel文件添加元素
     *
     * @param sheet
     * @param posts
     * @throws IllegalAccessException
     * @throws WriteException
     */
    private void addExcelFileElement(WritableSheet sheet, List<PostXml> posts) throws IllegalAccessException, WriteException {
        for (int i = 0; i < posts.size(); i++) {
            PostXml post = posts.get(i);
            //获取对象长度
            Field[] p = post.getClass().getDeclaredFields();
            int k = 0;
            for (Field f : p) {
                //System.out.println(f.getName()+"==========================="+f.get(post));
                String str = "";
                if (f.get(post) != null) {
                    str = f.get(post).toString();
                }
                sheet.addCell(new Label(k, i + 1, str));
                k++;
            }
        }
    }


    /**
     * 标签操作
     *
     * @param post
     * @param tag
     * @return
     */
    public String postLabel(Post post, String tag) {
        String[] tags = tag.split(",");
        String lbs = "";
        for (int i = 0; i < tags.length; i++) {
            //查询标签表中是否有此标签
            Integer lbid = postLabelService.queryPostLabelByNameCompletely(tags[i]);
            if (lbid == null) {
                //新增标签
                PostLabel postLabel = setPostLabel(post, tags[i]);
                lbs += postLabel.getId() + ",";
            } else {
                lbs += lbid + ",";
            }
            if (i == tags.length - 1) {
                lbs.substring(0, lbs.lastIndexOf(","));
            }
        }
        return lbs;
    }

    /**
     * 新增标签
     *
     * @param post
     * @param tag
     * @return
     */
    private PostLabel setPostLabel(Post post, String tag) {
        PostLabel postLabel = new PostLabel();
        postLabel.setName(tag);
        postLabel.setType(1);
        postLabel.setUserid(post.getUserid());
        postLabel.setIntime(new Date());
        postLabel.setIsdel(0);
        postLabelService.insertPostLabel(postLabel);
        return postLabel;
    }

    /**
     * 查询用户
     *
     * @param nickname
     * @param phone
     * @param
     */
    public Integer queryUser(String nickname, String phone) {
        User user = new User();
        if (StringUtil.isNotEmpty(phone)) {
            user.setPhone(phone);
        }
        if (StringUtil.isNotEmpty(nickname)) {
            user.setNickname(nickname);
        }
        //根据手机号或昵称查询
        User userid = userService.queryUserByPhone(phone);
        if (userid != null) {
            return userid.getId();
        } else {
            //注册用户
            int uid = newUserRegistration(phone);
            user.setId(uid);
            //修改用户昵称
            userService.updateUserByNickname(user);
            return uid;
        }
    }

    /**
     * 新用户注册
     *
     * @param phone
     */
    private Integer newUserRegistration(String phone) {
        try {
            // 生成6位随机字串
            String verifyCode = (int) ((Math.random() * 9 + 1) * 100000) + "";
            //1 生成token
            UsernamePasswordToken newToken = new UsernamePasswordToken(phone, verifyCode.toCharArray());
            RegisterUser member = new RegisterUser();
            member.setPhone(phone);
            member.setDeviceno("19dajieshule");
            member.setMobileCheckCode(verifyCode);
            //2 注册用户/修改用户信息
            Gson gson = new Gson();
            String json = gson.toJson(newToken);
            member.setToken(json);
            //2.1 手机号不存在,则新增用户信息
            int uid = appRegisterFacade.registerMember(member);
            //3 如果用户当前手机号有领取过H5页面分享的优惠券，那么不管新老用户统一将优惠券临时表yw_coupon_temp中的优惠券信息全部放入优惠券正式表yw_coupon中
            processCoupon(phone, uid);
            //2.2 增加新用户注册积分流水
            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.new_user_register.getCode(), PointConstant.POINT.new_user_register.getCode(), uid);
            //2.3 增加绑定手机号积分流水
            pointRecordFacade.addPointRecord(PointConstant.POINT_TYPE.binding_phone.getCode(), PointConstant.POINT.binding_phone.getCode(), uid);
            //4 判断该userid是否存在一个im用户，若不存在，则注册im用户;若存在，则查询
            getImuserForReturn(uid);
            return uid;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 判断该userid是否存在一个im用户，若不存在，则注册im用户
     *
     * @param
     * @throws IOException
     */
    private void getImuserForReturn(int userid) throws IOException {

        Boolean isExistImUser = imFacade.isExistAPPImuser(userid);
        if (!isExistImUser) {
            //若不存在，则注册im用户
            ImUser imUser = new ImUser();
            imUser.setUserid(userid);
            imUser.setAccid(CheckSumBuilder.getAccid(String.valueOf(userid)));  //根据userid生成accid
            imUser.setName(StrUtil.genDefaultNickNameByTime());
            imFacade.AddImUser(imUser);
        }
    }


    /**
     * 如果当前手机号在分享的H5页面领取过优惠券，那么不管新老用户统一在这里将优惠券临时表中的数据同步到优惠券正式表中
     *
     * @param phone
     * @param userid
     */
    @Transactional
    void processCoupon(String phone, int userid) {
        //首先检查当前手机号是否领取过优惠券
        List<CouponTemp> couponTempList = couponService.checkIsGetCoupon(phone);
        List<Coupon> couponList = new ArrayList<>();
        if (couponTempList.size() > 0) {
            //遍历替换phone为userid，放入List<Coupon>
            for (int i = 0; i < couponTempList.size(); i++) {
                CouponTemp couponTemp = couponTempList.get(i);
                Coupon coupon = new Coupon();
                coupon.setUserid(userid);
                coupon.setTitle(couponTemp.getTitle());
                coupon.setContent(couponTemp.getContent());
                coupon.setType(couponTemp.getType());
                if (null != couponTemp.getShopid()) {
                    coupon.setShopid(couponTemp.getShopid());
                }
                coupon.setStatue(couponTemp.getStatue());
                coupon.setBegintime(couponTemp.getBegintime());
                coupon.setEndtime(couponTemp.getEndtime());
                coupon.setIntime(couponTemp.getIntime());
                coupon.setTmoney(couponTemp.getTmoney());
                coupon.setUsemoney(couponTemp.getUsemoney());
                coupon.setIsdel(couponTemp.getIsdel());
                couponList.add(coupon);
            }

            //插入优惠券列表
            couponService.insertCouponList(couponList);
            //删除临时表中的优惠券领取记录
            couponService.delCouponTemp(phone);
        }
    }

    /**
     * 文本内容解析
     *
     * @param post
     * @param e
     * @param content
     * @return
     */
    private String getTextContentAnalysis(Post post, Element e, String content) {
        String caption = e.element("content").getText();
        String caps = caption.replace("<p>", "");
        caps = caps.replace("</p>", "");
        content += "{\"type\": 0,\"orderid\":" + 0 + ",\"value\":\"" + caps + "\",\"wh\": \"\",\"dir\": \"\"}";

        //用于内容拼接闭合
        String neirong = post.getPostcontent();
        content += "]";
        //System.out.println("---------"+s);
        post.setPostcontent(neirong);
        return content;
    }

    /**
     * 视频内容解析
     *
     * @param post
     * @param e
     * @param content
     * @return
     */
    private String getVideoContentAnalysis(Post post, Element e, String content) {
        String embed = e.element("embed").getText();
        embed = embed.replace("{", "");
        embed = embed.replace("}", "");
        embed = embed.replace("\"", "");
        String[] embeds = embed.split(",");
        int num = 0;
        for (int i = 0; i < embeds.length; i++) {
            if (embeds[i].substring(0, embeds[i].indexOf(":")).equals("originUrl")) {
                content += "{\"type\": 2,\"orderid\":" + num + ",";
                Map m = download(embeds[i].substring(embeds[i].indexOf(":") + 1, embeds[i].length()), "video");
                content += "\"value\":\"" + m.get("newurl") + "\",\"wh\": \"\",\"dir\": \"\"},";
                //System.out.println(originUrl);
                num++;
            }

        }
        //文本
        Element caption = e.element("caption");
        String caps = caption.getText().replace("<p>", "");
        caps = caps.replace("</p>", "");
        content += "{\"type\": 0,\"orderid\":" + num + ",\"value\":\"" + caps + "\",\"wh\": \"\",\"dir\": \"\"}";

        //用于内容拼接闭合
        String neirong = post.getPostcontent();
        content += "]";
        //System.out.println("---------"+s);
        post.setPostcontent(neirong);
        return content;
    }

    /**
     * 图片内容解析
     *
     * @param post
     * @param list
     * @param e
     * @param content
     * @return
     */
    private String getImgContentAnalysis(Post post, List list, Element e, String content) {
        Element photoLinks = e.element("photoLinks");
        try {
            String pho = photoLinks.getText();
            //pho = pho.substring(2, pho.lastIndexOf("]")-1);
            pho = pho.replace("[", "");
            pho = pho.replace("{", "");
            pho = pho.replace("}", "");
            pho = pho.replace("]", "");
            pho = pho.replace("\"", "");
            String[] substring = pho.split(",");
            int num = 0;
            boolean bln = true;
            //循环子节点拼接帖子内容
            for (int i = 0; i < substring.length; i++) {

                String wh = "\"wh\":";
                if (substring[i].substring(0, substring[i].indexOf(":")).equals("orign")) {
                    content += "\"type\":1,";
                    //图片处理
                    String s = "";
                    if (substring[i].length() - 1 == substring[i].lastIndexOf("?")) {
                        s = substring[i].substring(substring[i].indexOf(":") + 1, substring[i].lastIndexOf("?"));
                    } else {
                        s = substring[i].substring(substring[i].indexOf(":") + 1, substring[i].length());
                    }
                    //s = substring[i].substring(substring[i].indexOf(":") + 1, substring[i].length());
                    Map m = download(s, "img");
                    //获取本地文件
                    list.add(m.get("oldurl"));
                    //帖子封面处理
                    String covimg = m.get("oldurl").toString();
                    if (bln) {
                        //帖子封面处理，包括存储原图和压缩图
                        postCompressImg(post, list, m, covimg);
                    }
                    content += "\"value\":\"" + m.get("newurl").toString() + "\",\"dir\": \"\"},";
                    bln = false;
                }
                if (substring[i].substring(0, substring[i].indexOf(":")).equals("ow")) {
                    content += "{\"orderid\":" + num + ",";
                    num++;
                    content += wh + "\"" + substring[i].substring(substring[i].indexOf(":") + 1, substring[i].length()) + "×";
                }
                if (substring[i].substring(0, substring[i].indexOf(":")).equals("oh")) {
                    content += substring[i].substring(substring[i].indexOf(":"), substring[i].length()) + "\",";
                }
            }

            //文本
            Element caption = e.element("caption");
            String caps = "";
            //当文本中包含p标签执行截取,否则直接获取
            if (caption.getText().toString().indexOf("<p>") != -1) {
                caps = caption.getText().replace("<p>", "");
                caps = caps.replace("</p>", "");
                caps = caps.replace("<br />", "");
                caps = caps.replace("<", "");
                caps = caps.replace(">", "");
                caps = caps.replace("/", "");
                caps = caps.replace("br", "");
                caps = caps.replace("\"", "");
            } else {
                caps = caption.getText();
                if (caps.indexOf("<") != -1) {
                    caps = caps.replace("<", "");
                    caps = caps.replace(">", "");
                    caps = caps.replace("/", "");
                    caps = caps.replace("br", "");
                    caps = caps.replace("\"", "");
                }
            }
            content += "{\"type\": 0,\"orderid\":" + num + ",\"value\":\"" + caps + "\",\"wh\": \"\",\"dir\": \"\"}]";
        } catch (Exception e1) {
            return "";
        }
        return content;
    }

    /**
     * 操作帖子封面，包括原图和压缩图的映射存储
     *
     * @param post
     * @param list
     * @param m
     * @param
     * @param covimg
     */
    public void postCompressImg(Post post, List list, Map m, String covimg) {
        try {
            String newurl = "";
            //把切好的原图和压缩图存放表中
            CompressImg compressImg = new CompressImg();
            compressImg.setProtoimgsize(m.get("size").toString());
            compressImg.setIntime(new Date());

            //封面图片切割压缩操作,newurl返回的是切割后的原图
            newurl = getPostCovimg(post, list, covimg, newurl);
            Thread.sleep(4800);
            System.out.println(m.get("size").toString() + "========================================================================================" + m.get("newurl"));
            compressImg.setCompressimgurl(post.getCoverimg());//压缩后的图片
            compressImg.setProtoimgurl(newurl);//切割后的原图
            compressImgService.insert(compressImg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 操作图片为帖子封面，计算宽高，切割，压缩，上传等操作
     *
     * @param post
     * @param list   返回服务器相对路径中 操作后 和操作前的图片路径
     * @param covimg 需要处理的图片
     * @param newurl 返回处理后的图片 不用传值
     */
    public String getPostCovimg(Post post, List list, String covimg, String newurl) {
        //计算宽高比工具
        Map whs = imgIncision(covimg);
        whs.put("x", 0);
        whs.put("y", 0);
        //切割图片上传到阿里云,并返回url作为原图
        Map tmpurl = imgCuttingUpload(covimg, whs);
        if (list != null) {
            list.add(tmpurl.get("to"));
            list.add(tmpurl.get("form"));
        }
        //4对本地服务器中切割好的图片进行压缩处理
        newurl = imgCompress(newurl, whs, tmpurl.get("to").toString());
        //帖子封面,也用于返回压缩后的图片
        post.setCoverimg(newurl);
        return tmpurl.get("new").toString();
    }

    /**
     * 图片切割工具
     *
     * @param covimg 原图
     * @param whs    Map集合，其中 w,h为宽高,xy为开始坐标
     * @return
     */
    public Map imgCuttingUpload(String covimg, Map whs) {
        Map resault = new HashMap();
        //查询帖子图片存放目录
        UUID uuid = UUID.randomUUID();
        String incise = systemLayoutService.queryServiceUrl("post_incision_img_url");
        //String incise = uploadFacade.getConfigVar("post.incise.domain");
        String suffix = covimg.substring(covimg.lastIndexOf(".") + 1);
        incise += uuid + "." + suffix;
        File fromFile = new File(covimg);
        File toFile = new File(incise);
        resault.put("form", covimg);
        resault.put("to", incise);
        //对宽高取整
        String w = whs.get("w").toString();
        if (w.indexOf(".") != -1) {
            w = w.substring(0, w.lastIndexOf("."));
        }
        String h = whs.get("h").toString();
        if (h.indexOf(".") != -1) {
            h = h.substring(0, h.lastIndexOf("."));
        }
        String x = whs.get("x").toString();
        if (x.indexOf(".") != -1) {
            x = x.substring(0, x.lastIndexOf("."));
        }
        String y = whs.get("y").toString();
        if (y.indexOf(".") != -1) {
            y = y.substring(0, y.lastIndexOf("."));
        }
        //2从服务器获取文件并剪切,
        Map map = movisionOssClient.resizePng(fromFile, toFile, Integer.parseInt(w), Integer.parseInt(h), Integer.parseInt(x), Integer.parseInt(y), false);
        String tmpurl = null;
        if (map.get("code").equals(200)) {
            //上传本地服务器切割完成的图片到阿里云
            Map almap = aliOSSClient.uploadInciseStream(incise, "img", "coverIncise");
            //3获取本地服务器中切割完成后的图片
            tmpurl = String.valueOf(almap.get("url"));
            resault.put("new", tmpurl);
        }
        return resault;
    }

    private String imgCompress(String newurl, Map whs, String tmpurl) {
        int wt = 0;//图片压缩后的宽度
        int ht = 0;//图片压缩后的高度440
        InputStream is = null;
        Map compressmap = new HashMap();
        try {
            //返回图片的宽高
            //BufferedImage bi = ImageIO.read(file.getInputStream());
            File file1 = new File(tmpurl);
            is = new FileInputStream(file1);
            BufferedImage bi = ImageIO.read(is);
            //获取图片压缩比例
            Double ratio = systemLayoutService.queryFileRatio("file_compress_ratio");
            wt = (int) Math.ceil(bi.getWidth() * ratio);
            ht = (int) Math.ceil(bi.getHeight() * ratio);
            //新增压缩部分
            File fs = new File(tmpurl);
            Long fsize = fs.length();//获取文件大小
            String compressUrl = null;
            if (fsize > 800 * 1024) {
                //对图片压缩处理
                compressUrl = coverImgCompressUtil.ImgCompress(tmpurl, wt, ht);
                System.out.println("压缩完的切割图片url==" + compressUrl);
            } else {
                //对宽高值去除小数点
                String ww = whs.get("w").toString();
                String hh = whs.get("h").toString();
                compressUrl = coverImgCompressUtil.ImgCompress(tmpurl, Integer.parseInt(ww), Integer.parseInt(hh));
                System.out.println("压缩完的切割图片url==" + compressUrl);
            }
            //5对压缩完的图片上传到阿里云
            compressmap = aliOSSClient.uploadInciseStream(compressUrl, "img", "coverIncise");
            newurl = String.valueOf(compressmap.get("url"));
        } catch (IOException es) {
            es.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
            }
        }
        return newurl;
    }

    /**
     * 返回截图宽高
     *
     * @param url
     * @return
     */
    private Map imgIncision(String url) {
        File file1 = new File(url);
        Map resault = new HashMap();
        try {
            Image image = ImageIO.read(file1);
            int wth = image.getWidth(null);
            int hht = image.getHeight(null);
            Map map = new HashMap();
            Double thanw = 750.0;
            Double thanh = 440.0;
            //计算宽高比例
            map = imgWhidthAndHeight(wth, hht, thanw, thanh);
            System.out.println("切割后的图片宽度：======================" + map.get("w"));
            System.out.println("切割后的图片高度：======================" + map.get("h"));
            resault.put("w", map.get("w"));
            resault.put("h", map.get("h"));
            return resault;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 计算宽高比例
     *
     * @param w     原宽
     * @param h     原高
     * @param thanh 比例宽
     * @param thanw 比例高
     * @return
     */
    public Map imgWhidthAndHeight(int w, int h, Double thanw, Double thanh) {
        Map resatlt = new HashMap();
       /* if (h > thanh && w > thanw) {
            if (w / h > thanw / thanh) {
                resatlt.put("h", h);
                resatlt.put("w", (int) (h * (thanh / thanw)));
            } else if (h / w > thanh / thanw) {
                resatlt.put("w", w);
                resatlt.put("h", (int) (w * (thanh / thanw)));
            } else if (w < (thanw / thanh) * h) {
                resatlt.put("w", h);
                resatlt.put("h", (int) (h * (thanh / thanw)));
            }
        } else {
            if ((w / h > thanw / thanh) && (h / w > thanh / thanw)) {
                resatlt.put("w", w);
                resatlt.put("h", (int) (w * (thanh / thanw)));
            } else if ((h / w < thanh / thanw) && (w / h > thanw / thanh)) {
                resatlt.put("h", h);
                resatlt.put("w", (int) (h * (thanw / thanh)));
            }
        }*/
        if (w / h > thanw / thanh) { //切宽
            resatlt.put("h", h);
            resatlt.put("w", (int) (h * (thanw / thanh)));
        } else if (w / h < thanw / thanh) {//切高
            resatlt.put("w", w);
            resatlt.put("h", (int) (w * (thanh / thanw)));
        }
        return resatlt;
    }

    /**
     * 下载文件
     *
     * @param str
     */
    public Map download(String str, String type) {
        InputStream is = null;
        OutputStream os = null;
        Map map = new HashMap();
        String path = systemLayoutService.queryServiceUrl("file_xml_dwonload_img");
        //String path = "c://";
        if (type.equals("img")) {
            path += "img/";
        } else if (type.equals("video")) {
            path += "video/";
        }
        try {
            String url = str;
            URL u = new URL(url);
            //获取文件名
            String[] filename = url.split("/");
            String s = filename[filename.length - 1];
            is = u.openStream();
            os = new FileOutputStream(path + s);
            int buff = 0;
            while ((buff = is.read()) != -1) {
                os.write(buff);
            }
            map.put("oldurl", path + s);
            if (type.equals("img")) {
                //图片上传
                Thread.sleep(800);
                Map t = movisionOssClient.uploadFileObject(new File(path + s), "img", "post");
                map.put("newurl", t.get("url"));
                //获取原图大小
                String filesize = getImgSize(path + s);
                map.put("size", filesize);
            } /*else if (type.equals("video")) {
                //视频上传
                //Map m = movisionOssClient.uploadFileObject(new File(path + s), "video", "post");
                videoUploadUtil.videoUpload(path,)
                map.put("newurl", );
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    public String getImgSize(String url) {
        File fdel = new File(url);
        long l = fdel.length();
        float size = (float) l / 1024 / 1024;
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
        return df.format(size);
    }
}
