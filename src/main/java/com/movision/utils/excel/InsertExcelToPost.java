package com.movision.utils.excel;

import com.movision.facade.boss.PostFacade;
import com.movision.facade.boss.XmlParseFacade;
import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.circle.service.CircleService;
import com.movision.mybatis.post.entity.Post;
import com.movision.mybatis.user.entity.User;
import com.movision.utils.oss.MovisionOssClient;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/11/1 17:32
 */
@Service
public class InsertExcelToPost {

    @Autowired
    private CircleService circleService;

    @Autowired
    private XmlParseFacade xmlParseFacade;

    @Autowired
    private PostFacade postFacade;

    @Autowired
    private MovisionOssClient movisionOssClient;


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
                String circle = "";
                //存放帖子图片
                List imgList = new ArrayList();
                String imgs = "";
                //存放帖子文字
                String text = "";
                //存放用户名称和手机号
                User user = new User();
                //存放标签id
                String labels = "";

                // 循环Excel的列
                for (int c = 0; c < this.totalCells; c++) {
                    Cell cell = row.getCell(c);
                    //对数据封装，保存数据
                    if (null != cell) {
                        switch (c) {
                            case 0:
                                try {
                                    //帖子标题
                                    String title = String.valueOf(cell.getStringCellValue());
                                    if (StringUtil.isNotBlank(title)) {
                                        post.setTitle(cell.getStringCellValue());
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
                                    //帖子图片
                                    String img = String.valueOf(cell.getStringCellValue());
                                    if (StringUtil.isNotBlank(img)) {
                                        imgs = img;
                                        //解析图片字符串
                                        String[] imgss = imgs.split(",");
                                        for (int i = 0; i < imgss.length; i++) {
                                            imgList.add(imgss[i]);
                                        }
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
                                    //帖子文字
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        text = cell.getStringCellValue();
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
                                    //圈子
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        //根据圈子名称查询圈子id
                                        Integer circleid = circleService.queryCircleByNameAndEntirely(cell.getStringCellValue());
                                        post.setCircleid(circleid);
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
                                    //用户昵称
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        user.setNickname(cell.getStringCellValue());
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
                                    //用户手机号
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        user.setPhone(cell.getStringCellValue());
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
                                    //标签名称
                                    if (StringUtil.isNotBlank(cell.getStringCellValue())) {
                                        labels = cell.getStringCellValue();
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
                //操作标签,获取到标签的id
                String labelids = xmlParseFacade.postLabel(post, labels);

                //操作用户
                Integer userid = xmlParseFacade.queryUser(user.getNickname().toString(), user.getPhone().toString());

                if (userid != null) {
                    //操作帖子内容
                    //获取服务器上图片位置
                    String imgPath = "";

                    List list = new ArrayList();
                    //封面图片
                    String covimg = "";

                    //帖子内容
                    String postContent = "";

                    //循环解析图片名称
                    encapsulationPostContent(post, imgs, imgPath, list, covimg, postContent, text);
                    //操作帖子封面

                    //新增帖子操作
                    postFacade.addPostTest(request, post.getTitle().toString(), "", post.getCircleid().toString(), String.valueOf(userid),
                            post.getCoverimg(), post.getPostcontent(), labelids, "", "1");
                }

            }
            //返回值
            map.put("code", "200");

        } catch (NumberFormatException e) {
            map.put("code", "400");
            map.put("messager", "错误行：" + ir + 1 + "");
            return map;
        }
        return map;
    }

    /**
     * 封装帖子内容
     *
     * @param post
     * @param imgs
     * @param imgPath
     * @param list
     * @param covimg
     * @param postContent
     */
    private void encapsulationPostContent(Post post, String imgs, String imgPath, List list, String covimg, String postContent, String text) {
        String[] is = imgs.split(",");
        postContent += "[";
        try {
            for (int i = 0; i < is.length; i++) {
                String imgName = is[i];
                //查看并获取指定图片的路径
                String sysimgurl = getImgToSystemUrl(imgPath, imgName);
                int h = 0;
                int w = 0;
                String size = "";
                //获取图片宽高
                FileInputStream iss = null;
                BufferedImage src = null;
                try {
                    iss = new FileInputStream(sysimgurl);
                    src = ImageIO.read(iss);
                    w = src.getWidth(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                postContent += "{\"dir\": \"\",\"orderid\": " + i + ",\"type\": 1,\"value\": \"";
                //图片上传
                Map t = movisionOssClient.uploadFileObject(new File(sysimgurl), "img", "post");
                Thread.sleep(500);
                postContent += t.get("url").toString() + "\",\"wh\": \"" + w + "×" + h + "\"},";
                size = xmlParseFacade.getImgSize(sysimgurl);
                t.put("size", size);
                t.put("newurl", postContent);
                //获取第一张图片作为封面
                if (i == 0) {
                    xmlParseFacade.postCompressImg(post, list, t, covimg);
                    post.setCoverimg(covimg);
                }
                //拼接帖子文字
                if (i == is.length - 1) {
                    if (StringUtil.isNotEmpty(text)) {
                        postContent += "";
                    } else {
                        postContent = postContent.substring(0, postContent.lastIndexOf(","));
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        post.setPostcontent(postContent);
    }

    private String getImgToSystemUrl(String imgPath, String imgName) {
        String sysimgurl = "";
        File mFile = new File(imgPath);
        if (mFile.exists() && mFile.isDirectory()) {
            List<File> mlist = new ArrayList<File>();
            getAllFile(mFile, mlist);
            // 已经获取了所有图片
            for (File file2 : mlist) {
                //切割图片文件路径，获取其名称
                String str = file2.getAbsolutePath().substring(file2.getAbsolutePath().lastIndexOf("\\") + 1, file2.getAbsolutePath().length());
                System.out.println(str + "============================");
                if (str.equals(imgName)) {
                    sysimgurl = file2.getAbsolutePath();
                }
            }
        }
        return sysimgurl;
    }

    /**
     * 获取路径下所有图片
     *
     * @param mFile
     * @param mlist
     */
    public void getAllFile(File mFile, List<File> mlist) {
        // 1.获取子目录
        File[] files = mFile.listFiles();
        // 2.判断files是否是空的 否则程序崩溃
        if (files != null) {

            for (File file : files) {
                if (file.isDirectory()) {
                    getAllFile(file, mlist);//调用递归的方式
                } else {
                    // 4. 添加到集合中去
                    String fileName = file.getName();
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".png")
                            || fileName.endsWith(".gif")) {
                        mlist.add(file);//如果是这几种图片格式就添加进去
                    }
                }
            }
        }
    }


}
