package com.movision.utils.excel;


import com.movision.facade.boss.XmlParseFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.compressImg.service.CompressImgService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.entity.PostTo;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postLabel.service.PostLabelService;
import com.movision.mybatis.postLabelRelation.entity.PostLabelRelation;
import com.movision.mybatis.postLabelRelation.service.PostLabelRelationService;
import com.movision.mybatis.user.entity.User;
import com.movision.utils.JsoupCompressImg;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/8/3 13:41
 */
@Service
public class ExcelIntoEnquiryUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelIntoEnquiryUtil.class);

    @Autowired
    private CircleService circleService;

    @Autowired
    private PostService postService;

    @Autowired
    private JsoupCompressImg jsoupCompressImg;

    @Autowired
    private XmlParseFacade xmlParseFacade;

    @Autowired
    private CompressImgService compressImgService;

    @Autowired
    private PostLabelRelationService postLabelRelationService;

    @Autowired
    private PostLabelService postLabelService;


    //总行数
    private int totalRows = 0;
    //总条数
    private int totalCells = 0;
    //错误信息接收器
    private String errorMsg;

    //构造方法
    //public ReadExcel(){}
    //获取总行数
    /*public int getTotalRows() {
        return totalRows;
    }

    //获取总列数
    public int getTotalCells() {
        return totalCells;
    }

    //获取错误信息
    public String getErrorInfo() {
        return errorMsg;
    }*/

    public Map queryExcelIntoEnquiry(HttpServletRequest request, MultipartFile file) {
        String fileName = file.getOriginalFilename();//获取文件名
        Map map = null;
        try {
            if (!validateExcel(fileName)) {// 验证文件名是否合格
                return null;
            }
            boolean isExcel2003 = true;// 根据文件名判断文件是2003版本还是2007版本
            if (isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            map = createExcel(request, file.getInputStream(), isExcel2003);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 验证EXCEL文件
     *
     * @param filePath
     * @return
     */
    public boolean validateExcel(String filePath) {
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            errorMsg = "文件名不是excel格式";
            return false;
        }
        return true;
    }

    // @描述：是否是2003的excel，返回true是2003
    public static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    //@描述：是否是2007的excel，返回true是2007
    public static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }


    /**
     * 读取根据excel里面的内容信息
     *
     * @param is          输入流
     * @param isExcel2003 excel是2003还是2007版本
     * @return
     * @throws IOException
     */
    public Map createExcel(HttpServletRequest request, InputStream is, boolean isExcel2003) {
        Map userList = null;
        try {
            Workbook wb = null;
            if (isExcel2003) {// 当excel是2003时,创建excel2003
                wb = new HSSFWorkbook(is);
            } else {// 当excel是2007时,创建excel2007
                wb = new XSSFWorkbook(is);
            }
            userList = readExcelValue(request, wb);// 读取Excel里面的信息
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userList;
    }


    /**
     * 读取Excel里面客户的信息
     *
     * @param wb
     * @return
     */
    private Map readExcelValue(HttpServletRequest request, Workbook wb) {
        // 得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        // 得到Excel的行数
        this.totalRows = sheet.getPhysicalNumberOfRows();
        //this.totalRows = sheet.getLastRowNum() + 1;
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        Map map = new HashMap();


        String resault = "";
        int ir = 0;
        try {
            // 循环Excel行数
            for (int r = 1; r < totalRows; r++) {
                ir = r;
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }

                Post post = new Post();
                //用于存放圈子名称
                String circlename = "";
                //帖子内容
                String content = "";
                //贴子封面
                String covimg = "";
                //id
                String postid = "";
                //用户id
                String uid = "";
                String label = "";
                Circle circle = new Circle();
                User user = new User();

                // 循环Excel的列
                for (int c = 0; c < this.totalCells; c++) {
                    Cell cell = row.getCell(c);
                    //对数据封装，保存数据
                    if (null != cell) {
                        switch (c) {
                            case 0:
                                try {
                                    //帖子id
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        //postid = cell.getStringCellValue();
                                        post.setId(Integer.parseInt(String.valueOf(cell.getStringCellValue())));
                                    }
                                } catch (Exception e) {
                                    if (resault.length() > 0) {
                                        resault += "," + ir + 1;
                                    }
                                    map.put("messager", "错误行：" + resault);
                                    continue;
                                }
                            case 1:
                                try {
                                    //标题
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        //uid = cell.getStringCellValue();
                                        post.setTitle(String.valueOf(cell.getStringCellValue()));
                                    }
                                    continue;
                                } catch (Exception e) {
                                    if (resault.length() > 0) {
                                        resault += "," + ir + 1;
                                    }
                                    map.put("messager", "错误行：" + resault);
                                    continue;
                                }
                            case 2:
                                try {
                                    //副标题
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        //circle = cell.getStringCellValue();
                                        post.setSubtitle(String.valueOf(cell.getStringCellValue()));
                                    }
                                    continue;
                                } catch (Exception e) {
                                    if (resault.length() > 0) {
                                        resault += "," + ir + 1;
                                    }
                                    map.put("messager", "错误行：" + resault);
                                    continue;
                                }
                            case 3:
                                try {
                                    //帖子封面
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        //post.setTitle(cell.getStringCellValue());
                                        post.setCoverimg(String.valueOf(cell.getStringCellValue()));
                                    }
                                    continue;
                                } catch (Exception e) {
                                    if (resault.length() > 0) {
                                        resault += "," + ir + 1;
                                    }
                                    map.put("messager", "错误行：" + resault);
                                    continue;
                                }
                            case 4:
                                try {
                                    //帖子内容
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        //content = cell.getStringCellValue();
                                        //post.setPostcontent(String.valueOf(cell.getStringCellValue()));
                                    }
                                    continue;
                                } catch (Exception e) {
                                    if (resault.length() > 0) {
                                        resault += "," + ir + 1;
                                    }
                                    map.put("messager", "错误行：" + resault);
                                    continue;
                                }
                            case 5:
                                try {
                                    //圈子id
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        //covimg = cell.getStringCellValue();
                                        post.setCircleid(Integer.parseInt(String.valueOf(cell.getStringCellValue())));
                                    }
                                    continue;
                                } catch (Exception e) {
                                    if (resault.length() > 0) {
                                        resault += "," + ir + 1;
                                    }
                                    map.put("messager", "错误行：" + resault);
                                    continue;
                                }
                            case 6:
                                try {
                                    //圈子名称
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        circlename = cell.getStringCellValue();
                                        circle.setName(String.valueOf(cell.getStringCellValue()));
                                    }
                                    continue;
                                } catch (Exception e) {
                                    if (resault.length() > 0) {
                                        resault += "," + ir + 1;
                                    }
                                    map.put("messager", "错误行：" + resault);
                                    continue;
                                }
                            case 7:
                                try {
                                    //标签
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        label = cell.getStringCellValue();
                                    }
                                    continue;
                                } catch (Exception e) {
                                    if (resault.length() > 0) {
                                        resault += "," + ir + 1;
                                    }
                                    map.put("messager", "错误行：" + resault);
                                    continue;
                                }
                            case 8:
                                try {
                                    //用户id
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        //covimg = cell.getStringCellValue();
                                        user.setId(Integer.parseInt(String.valueOf(cell.getStringCellValue())));
                                        post.setUserid(Integer.parseInt(String.valueOf(cell.getStringCellValue())));
                                    }
                                    continue;
                                } catch (Exception e) {
                                    if (resault.length() > 0) {
                                        resault += "," + ir + 1;
                                    }
                                    map.put("messager", "错误行：" + resault);
                                    continue;
                                }
                            case 9:
                                try {
                                    //用户昵称
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        //covimg = cell.getStringCellValue();
                                        user.setNickname(String.valueOf(cell.getStringCellValue()));
                                    }
                                    continue;
                                } catch (Exception e) {
                                    if (resault.length() > 0) {
                                        resault += "," + ir + 1;
                                    }
                                    map.put("messager", "错误行：" + resault);
                                    continue;
                                }
                            case 10:
                                try {
                                    //手机号
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        //covimg = cell.getStringCellValue();
                                        user.setPhone(String.valueOf(cell.getStringCellValue()));
                                    }
                                    continue;
                                } catch (Exception e) {
                                    if (resault.length() > 0) {
                                        resault += "," + ir + 1;
                                    }
                                    map.put("messager", "错误行：" + resault);
                                    continue;
                                }
                        }
                    }
                }

                //业务代码
                //根据圈子名称查询圈子，
                Integer circleid = circleService.queryCircleByNameAndEntirely(circlename);
                if (circleid != null) {
                    post.setCircleid(circleid);
                }

                //标签操作
                updatePostLabel(post, label);

                //帖子封面操作
                if (StringUtil.isNotEmpty(covimg)) {
                    //查询封面是否是有变动
                    String isproto = compressImgService.queryUrlIsProtoimg(covimg);
                    //没有查到，封面处理操作
                    if (isproto == null || isproto == "") {
                        //下载图片 第一个参数原文件路径，第二个文件操作 1 ：img 2：video
                        Map t = xmlParseFacade.download(covimg, "img");
                        if (t != null) {
                            //操作封面
                            xmlParseFacade.postCompressImg(post, null, t, t.get("oldurl").toString());
                        }
                    }
                }

                //内容转换
                /*Map con = null;
                if (StringUtil.isNotEmpty(content)) {
                    //内容转换
                    con = jsoupCompressImg.newCompressImg(request, content);
                    System.out.println(con);
                    if ((int) con.get("code") == 200) {
                        String str = con.get("content").toString();
                        post.setPostcontent(str);//帖子内容
                    } else {
                        logger.error("帖子内容转换异常");
                        post.setPostcontent(content);
                    }
                }*/

                //执行帖子编辑操作
                postService.updateActivePostById(post);

                //删除没有title帖子（运营不要的报废帖子）
                postService.deletePostByIDAndCircleid(post);

            }
            //返回值
            map.put("code", "200");
            map.put("messager", "成功");
        } catch (NumberFormatException e) {
            map.put("code", "400");
            map.put("messager", "错误行：" + ir + 1 + "");
            return map;
        }
        return map;
    }

    /**
     * 标签操作
     *
     * @param post
     * @param label
     */
    private void updatePostLabel(Post post, String label) {
        String[] labels = label.split(",");
        //清空帖子标签
        postLabelRelationService.deletePostLabelRelaton(post.getId());
        //帖子标签增加操作
        for (int i = 0; i < labels.length; i++) {
            //查询标签是否存在
            Integer lid = postLabelService.queryPostLabelByNameCompletely(labels[i]);
            if (lid != null) {
                PostLabelRelation relation = new PostLabelRelation();
                relation.setPostid(post.getId());
                relation.setLabelid(lid);
                //存在使用,新增标签帖子关系
                postLabelRelationService.insertPostToLabel(relation);
            } else {
                PostLabel label1 = new PostLabel();
                label1.setName(labels[i]);
                label1.setIsdel(0);
                label1.setIntime(new Date());
                label1.setType(1);
                label1.setUserid(post.getUserid());
                //不存在新增
                Integer labelid = postLabelService.insertPostLabel(label1);
                PostLabelRelation relation = new PostLabelRelation();
                relation.setLabelid(labelid);
                relation.setPostid(post.getId());
                postLabelRelationService.insertPostToLabel(relation);
            }
        }
    }

}
