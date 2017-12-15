package com.movision.utils.excel;

import com.movision.fsearch.utils.StringUtil;
import com.movision.mybatis.robotComment.entity.RobotComment;
import com.movision.mybatis.robotComment.service.RobotCommentService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/11/1 17:32
 */
@Service
public class InputExcelToPostComment {

    @Autowired
    private RobotCommentService commentService;


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
            for (int r = 0; r < totalRows; r++) {
                ir = r;
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }

                RobotComment comment = new RobotComment();
                // 循环Excel的列
                for (int c = 0; c < this.totalCells; c++) {
                    Cell cell = row.getCell(c);
                    //对数据封装，保存数据
                    if (null != cell) {
                        switch (c) {
                            case 0:
                                try {
                                    //评论内容
                                    String content = String.valueOf(cell.getStringCellValue());
                                    if (StringUtil.isNotBlank(content)) {
                                        comment.setContent(content);
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

                //为机器人评论添加数据
                commentService.insertRoboltComment(comment);

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

}
