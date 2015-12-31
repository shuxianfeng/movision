package com.zhuhuibao.common.view;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by wangxiang2 on 14-6-19.
 * <p/>
 * REPORTINFO 报表名称信息 Map<String,String> 包括 TITLE 报表标题  FILENAME 下载后文件名
 * QUERYPARAMS 查询参数信息 Map<String,String>
 * HEAD 表头信息 Map<String,String>
 * DATA 表体信息 List<Map<String,String>>
 */
public class XlsExcelView extends AbstractExcelView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!validateModelInfo(model)) {
            logger.error("model:" + model.toString());
            logger.error("导出xlsx报表参数不全，请确认");
            return;
        }
        int r = 0;
        Object reportInfo = model.get("REPORTINFO");
        HashMap<String, String> paramMap = (HashMap<String, String>) reportInfo;
        String title = paramMap.get("TITLE");
        HSSFSheet sheet = workbook.createSheet(title);

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
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename="
                + new String(filename.getBytes("GBK"), "ISO8859-1") + ".xls");
//                + new String(filename.getBytes("UTF-8"), "ISO8859-1") + ".xls");
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
    private int dealSheetQueryParams(HSSFWorkbook wb, Map<String, Object> model, HSSFSheet sheet, int r) {
        Map<String, String> params = (Map<String, String>) model.get("QUERYPARAMS");
        HSSFCellStyle cellStyle = createQueryParamCellStyle(wb);
        if (params != null && params.keySet().size() > 0) {
            int i = 0;  //控制行
            int j = 0;    //控制列
            for (String key : params.keySet()) {
                if (i % 2 == 0) {
                    r++;
                    j = 0;
                }
                HSSFCell c1 = getCell(sheet, r, j + 1);
                c1.setCellValue(key);
                c1.setCellStyle(cellStyle);
                sheet.addMergedRegion(new Region(r, (short) (j + 1), r, (short) (j + 2)));
//                sheet.addMergedRegion(new CellRangeAddress(r, (short) (j+1), r, (short) (j+2)));
                HSSFCell c2 = getCell(sheet, r, j + 3);
                c2.setCellValue(params.get(key) == null ? "" : params.get(key));
                c2.setCellStyle(cellStyle);
                sheet.addMergedRegion(new Region(r, (short) (j + 3), r, (short) (j + 4)));
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
    private ArrayList<String> dealSheetHeadMsg(HSSFWorkbook wb, Map<String, Object> model,
                                               HSSFSheet sheet, int r) {
        ArrayList<String> al = new ArrayList<String>();
        HSSFCellStyle cellStyle = createHeadCellStyle(wb);
        Object headObj = model.get("HEAD");
        LinkedHashMap<String, String> hreadMap = (LinkedHashMap<String, String>) headObj;
        Set<String> set = hreadMap.keySet();
        Iterator<String> it = set.iterator();
        int c = 0;
        while (it.hasNext()) {
            String key = it.next();
            String value = StringUtils.isNotEmpty(hreadMap.get(key)) ? String
                    .valueOf(hreadMap.get(key)) : " ";
            HSSFCell cell = getCell(sheet, r, c);
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
    private void dealSheetBody(HSSFWorkbook wb, Map<String, Object> model, ArrayList<String> al,
                               HSSFSheet sheet, int r) {
        Object dataObj = model.get("DATA");
        List<Map<String, Object>> dataList = (ArrayList<Map<String, Object>>) dataObj;
        int t = r;
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION);
        for (Map<String, Object> dataMap : dataList) {
            int i = 0;
            for (String key : al) {
                HSSFCell cell = getCell(sheet, t, i);
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
    private void createNormalTitle(HSSFWorkbook wb, HSSFSheet sheet, String title, int colSum) {
        HSSFRow row = sheet.createRow(0);

        // 设置第一行
        HSSFCell cell = row.createCell(0);
        row.setHeight((short) 400);

        // 定义单元格为字符串类型
        cell.setCellType(HSSFCell.ENCODING_UTF_16);
        cell.setCellValue(new HSSFRichTextString(title));

        // 指定合并区域
        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) colSum));

        HSSFCellStyle cellStyle = wb.createCellStyle();

        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        cellStyle.setWrapText(true);// 指定单元格自动换行

// 设置单元格字体
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
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
    private HSSFCellStyle createQueryParamCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        cellStyle.setWrapText(true);// 指定单元格自动换行

        // 设置单元格字体
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
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
    private HSSFCellStyle createHeadCellStyle(HSSFWorkbook wb) {
        // 创建单元格样式
        HSSFCellStyle cellStyle = wb.createCellStyle();

        // 指定单元格居中对齐
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 指定单元格垂直居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        // 指定当单元格内容显示不下时自动换行
        cellStyle.setWrapText(true);

        // 设置单元格字体
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
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
    private HSSFCellStyle createBodyCellStyle(HSSFWorkbook wb) {
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER_SELECTION); // 指定单元格居中对齐
        return cellStyle;
    }


}