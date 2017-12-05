package com.movision.utils.excel;


import com.movision.facade.boss.XmlParseFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.circle.entity.Circle;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.compressImg.service.CompressImgService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.post.service.PostService;
import com.movision.mybatis.postLabel.entity.PostLabel;
import com.movision.mybatis.postLabel.service.PostLabelService;
import com.movision.mybatis.postLabelRelation.entity.PostLabelRelation;
import com.movision.mybatis.postLabelRelation.service.PostLabelRelationService;
import com.movision.mybatis.user.entity.User;
import com.movision.utils.JsoupCompressImg;
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
import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/8/3 13:41
 */
@Service
public class ExcelUpdatePostByLable {

    private static Logger logger = LoggerFactory.getLogger(ExcelUpdatePostByLable.class);

    @Autowired
    private CircleService circleService;

    @Autowired
    private PostService postService;

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
                String userid = "";
                String label = "";
                // 循环Excel的列
                for (int c = 0; c < this.totalCells; c++) {
                    Cell cell = row.getCell(c);
                    //对数据封装，保存数据
                    if (null != cell) {
                        switch (c) {
                            case 0:
                                continue;
                            case 1:
                                try {
                                    //标题
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
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
                                continue;
                            case 3:
                                continue;
                            case 4:
                                continue;
                            case 5:
                                continue;
                            case 6:
                                continue;
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
                                    //标题
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        post.setUserid(Integer.valueOf(cell.getStringCellValue()));
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
                                continue;
                            case 10:
                                continue;
                        }
                    }
                }

                //业务代码
                //标签操作
                updatePostLabel(post, label);

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
        //查询正式环境帖子id
        List<Integer> postid = postService.queryPostIdByTitle(post);
        if (postid.size() == 1) {
            post.setId(postid.get(0));
            //清空帖子标签
            postLabelRelationService.deletePostLabelRelaton(postid.get(0));
            //帖子标签增加操作
            for (int i = 0; i < labels.length; i++) {
                //查询标签是否存在
                Integer lid = postLabelService.queryPostLabelByNameCompletely(labels[i]);
                if (lid != null) {
                    PostLabelRelation relation = new PostLabelRelation();
                    relation.setPostid(postid.get(0));
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
                    postLabelService.insertPostLabel(label1);
                    PostLabelRelation relation = new PostLabelRelation();
                    relation.setLabelid(relation.getId());
                    relation.setPostid(postid.get(0));
                    postLabelRelationService.insertPostToLabel(relation);
                }
            }
        }

    }

}
