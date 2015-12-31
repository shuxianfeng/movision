package com.zhuhuibao.utils;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;

/**
 * 文件上传导入时一些辅助功能
 *
 * @author wanghong
 */
public class FileUploadUtil {

    static final Logger logger = LoggerFactory
            .getLogger(FileUploadUtil.class);

    public static boolean checktype(String filename, HttpServletRequest request, Model model) {

        if (!(filename.substring(filename.length() - 4)).equals(".xls") && !(filename.substring(filename.length() - 5)).equals(".xlsx")) {
            String msg = "选择文件格式不正确，不是Excel，请重新选择!";
            model.addAttribute("msg", msg);
            return false;
        } else {
            return true;
        }
    }

    //判断单元格cell的类型并且做出相应的转换
    public static String getCellValue(Cell cell) {
        String strCell = "";
        DecimalFormat df = new DecimalFormat("#");
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    strCell = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    strCell = df.format(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    strCell = "";
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    strCell = String.valueOf(cell.getBooleanCellValue());
                    break;
                default:
                    strCell = "";
                    break;
            }
        }
        return strCell;
    }

}
