package com.zhuhuibao.common.view;

import com.zhuhuibao.common.view.abstractview.AbstractExcelView;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by wangxiang2 on 14-6-18.
 * <p/>
 * REPORTINFO 报表名称信息 Map<String,String> 包括 TITLE 报表标题  FILENAME 下载后文件名
 * QUERYPARAMS 查询参数信息 Map<String,String>
 * HEAD 表头信息 Map<String,String>
 * DATA 表体信息 List<Map<String,String>>
 */
public class XlsxExcelView extends AbstractExcelView {

    private static final Logger logger = LoggerFactory.getLogger(XlsxExcelView.class);

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!validateModelInfo(model)) {
            logger.error("model:" + model.toString());
            logger.error("导出xlsx报表参数不全，请确认");
            return;
        }
        int r = 0;
        Object reportInfo = model.get("REPORTINFO");
        HashMap<String, String> paramMap = (HashMap<String, String>) reportInfo;
        String title = paramMap.get("TITLE");
        Sheet sheet = workbook.createSheet(title);

        Object objMap = model.get("HEAD");
        LinkedHashMap<String, String> headMap = (LinkedHashMap<String, String>) objMap;

        createNormalTitle(workbook, sheet, title, headMap.size() + 4);
        //处理查询条件
        r = dealSheetQueryParams(workbook, model, sheet, r + 1);
        // 处理头
        ArrayList<String> al = dealSheetHeadMsg(workbook, model, sheet, r + 2);
        // 处理体
        dealSheetBody(workbook, model, al, sheet, r + 3);
        String filename = paramMap.get("FILENAME");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename="
                + new String(filename.getBytes("GBK"), "ISO8859-1") + ".xlsx");
    }

    private boolean validateModelInfo(Map<String, Object> model) {
        if (model == null || model.get("REPORTINFO") == null || model.get("HEAD") == null || model.get("DATA") == null) {
            return false;
        }
        HashMap<String, String> paramMap = (HashMap<String, String>) model.get("REPORTINFO");
        return !(paramMap.get("TITLE") == null || paramMap.get("FILENAME") == null);
    }

    /**
     * 处理查询条件
     *
     * @param model
     * @param sheet
     * @param r
     * @return
     */
    private int dealSheetQueryParams(Workbook wb, Map<String, Object> model, Sheet sheet, int r) {
        Map<String, String> params = (Map<String, String>) model.get("QUERYPARAMS");
        CellStyle cellStyle = createQueryParamCellStyle(wb);
        if (params != null && params.keySet().size() > 0) {
            int i = 0;  //控制行
            int j = 0;    //控制列
            for (String key : params.keySet()) {
                if (i % 2 == 0) {
                    r++;
                    j = 0;
                }
                Cell c1 = getCell(sheet, r, j + 1);
                c1.setCellValue(key);
                c1.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(r, r, (j + 1), (j + 2)));
//                sheet.addMergedRegion(new CellRangeAddress(r, (short) (j+1), r, (short) (j+2)));
                Cell c2 = getCell(sheet, r, j + 3);
                c2.setCellValue(params.get(key));
                c2.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(r, r, (j + 3), (j + 4)));
                i++;
                j += 4;
            }
        }
        return r;
    }

    /**
     * 处理EXEL表头
     *
     * @param model 参数列表
     * @param sheet
     * @return 列列表
     */
    private ArrayList<String> dealSheetHeadMsg(Workbook wb, Map<String, Object> model,
                                               Sheet sheet, int r) {
        ArrayList<String> al = new ArrayList<String>();
        CellStyle cellStyle = createHeadCellStyle(wb);
        Object headObj = model.get("HEAD");
        LinkedHashMap<String, String> hreadMap = (LinkedHashMap<String, String>) headObj;
        Set<String> set = hreadMap.keySet();
        Iterator<String> it = set.iterator();
        int c = 0;
        while (it.hasNext()) {
            String key = it.next();
            String value = StringUtils.isNotEmpty(hreadMap.get(key)) ? String
                    .valueOf(hreadMap.get(key)) : " ";
            Cell cell = getCell(sheet, r, c);
            cell.setCellValue(value);
            cell.setCellStyle(cellStyle);
            c++;
            al.add(key);
        }
        return al;
    }

    /**
     * 处理EXCEL报文体
     *
     * @param model 参数列表
     * @param al    头信息
     * @param sheet
     */
    private void dealSheetBody(Workbook wb, Map<String, Object> model, ArrayList<String> al,
                               Sheet sheet, int r) {
        Object dataObj = model.get("DATA");
        List<Map<String, String>> dataList = (ArrayList<Map<String, String>>) dataObj;
        int t = r;
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION);
        for (Map<String, String> dataMap : dataList) {
            int i = 0;
            for (String key : al) {
                Cell cell = getCell(sheet, t, i);
                cell.setCellValue(String.valueOf(dataMap.get(key) == null ? "" : dataMap.get(key)));
                cell.setCellStyle(cellStyle);
                i++;
            }
            t++;
        }

    }

    /**
     * 创建报表标题
     *
     * @param wb
     * @param sheet
     * @param title
     * @param colSum
     */
    private void createNormalTitle(Workbook wb, Sheet sheet, String title, int colSum) {
        Row row = sheet.createRow(0);

        // 设置第一行
        Cell cell = row.createCell(0);
        row.setHeight((short) 400);

        // 定义单元格为字符串类型
        cell.setCellType(XSSFCell.CELL_TYPE_STRING);
        cell.setCellValue(new XSSFRichTextString(title));

        // 指定合并区域
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colSum));

        CellStyle cellStyle = wb.createCellStyle();

        cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 指定单元格居中对齐
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        cellStyle.setWrapText(true);// 指定单元格自动换行

        // 设置单元格字体
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 300);
        cellStyle.setFont(font);

        cell.setCellStyle(cellStyle);
    }

    /**
     * 创建查询单元格样式
     *
     * @param wb
     * @return
     */
    private CellStyle createQueryParamCellStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER); // 指定单元格居中对齐
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        cellStyle.setWrapText(true);// 指定单元格自动换行

        // 设置单元格字体
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 250);
        cellStyle.setFont(font);

        return cellStyle;
    }

    /**
     * 创建表头样式
     *
     * @param wb
     * @return
     */
    private CellStyle createHeadCellStyle(Workbook wb) {
        // 创建单元格样式
        CellStyle cellStyle = wb.createCellStyle();

        // 指定单元格居中对齐
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);

        // 指定单元格垂直居中对齐
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        // 指定当单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);

        // 设置单元格字体
        Font font = wb.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 200);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 创建body单元格样式
     *
     * @param wb
     * @return
     */
    private CellStyle createBodyCellStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER_SELECTION); // 指定单元格居中对齐
        return cellStyle;
    }

}
