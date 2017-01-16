package com.movision.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class XlsUtils {


	
    private HSSFWorkbook wb = null;

    private HSSFSheet sheet = null;

    /**
     * @param wb
     * @param sheet
     */
    public XlsUtils(HSSFWorkbook wb, HSSFSheet sheet) {
        super();
        this.wb = wb;
        this.sheet = sheet;
    }

    /**
     * @return the sheet
     */
    public HSSFSheet getSheet() {
        return sheet;
    }

    /**
     * @param sheet the sheet to set
     */
    public void setSheet(HSSFSheet sheet) {
        this.sheet = sheet;
    }

    /**
     * @return the wb
     */
    public HSSFWorkbook getWb() {
        return wb;
    }

    /**
     * @param wb the wb to set
     */
    public void setWb(HSSFWorkbook wb) {
        this.wb = wb;
    }

    /**
     * 创建通用EXCEL头部
     *
     * @param headString 头部显示的字符
     * @param colSum     该报表的列数
     */
    public void createNormalHead(String headString, int colSum) {

        HSSFRow row = sheet.createRow(0);

        // 设置第一行
        HSSFCell cell = row.createCell(0);
        row.setHeight((short) 400);

        // 定义单元格为字符串类型
        cell.setCellType(HSSFCell.ENCODING_UTF_16);
        cell.setCellValue(new HSSFRichTextString(headString));

        // 指定合并区域
        sheet.addMergedRegion(new CellRangeAddress(0, 0, (short) 0,
                (short) colSum));

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
     * 创建通用报表第二行
     *
     * @param params 统计条件数组
     * @param colSum 需要合并到的列索引
     */
    public void createNormalTwoRow(String[] params, int colSum) {
        HSSFRow row1 = sheet.createRow(1);
        row1.setHeight((short) 300);

        HSSFCell cell2 = row1.createCell(0);

        cell2.setCellType(HSSFCell.ENCODING_UTF_16);
        cell2.setCellValue(new HSSFRichTextString("统计时间：" + params[0] + "  至  "
                + params[1]));

        // 指定合并区域
        sheet.addMergedRegion(new CellRangeAddress(1, 1, (short) 0,
                (short) colSum));

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

        cell2.setCellStyle(cellStyle);

    }

    /**
     * 设置报表标题
     *
     * @param columHeader 标题字符串数组
     */
    public void createColumHeader(String[] columHeader) {

        // 设置列头
        HSSFRow row2 = sheet.createRow(2);

        // 指定行高
        row2.setHeight((short) 600);

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        cellStyle.setWrapText(true);// 指定单元格自动换行

        // 单元格字体
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 250);
        cellStyle.setFont(font);

		/*
         * cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 设置单无格的边框为粗体
		 * cellStyle.setBottomBorderColor(HSSFColor.BLACK.index); // 设置单元格的边框颜色．
		 * cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		 * cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		 * cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		 * cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
		 * cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		 * cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
		 */

        // 设置单元格背景色
        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCell cell3 = null;

        for (int i = 0; i < columHeader.length; i++) {
            cell3 = row2.createCell(i);
            cell3.setCellType(HSSFCell.ENCODING_UTF_16);
            cell3.setCellStyle(cellStyle);
            cell3.setCellValue(new HSSFRichTextString(columHeader[i]));
        }

    }

    /**
     * 创建内容单元格
     *
     * @param wb    HSSFWorkbook
     * @param row   HSSFRow
     * @param col   short型的列索引
     * @param align 对齐方式
     * @param val   列值
     */
    public void cteateCell(HSSFWorkbook wb, HSSFRow row, int col, short align,
                           String val) {
        HSSFCell cell = row.createCell(col);
        cell.setCellType(HSSFCell.ENCODING_UTF_16);
        cell.setCellValue(new HSSFRichTextString(val));
        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(align);
        cell.setCellStyle(cellstyle);
    }

    /**
     * 创建合计行
     *
     * @param colSum    需要合并到的列索引
     * @param cellValue
     */
    public void createLastSumRow(int colSum, String[] cellValue) {

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 指定单元格居中对齐
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对齐
        cellStyle.setWrapText(true);// 指定单元格自动换行

        // 单元格字体
        HSSFFont font = wb.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontName("宋体");
        font.setFontHeight((short) 250);
        cellStyle.setFont(font);

        HSSFRow lastRow = sheet.createRow((short) (sheet.getLastRowNum() + 1));
        HSSFCell sumCell = lastRow.createCell(0);

        sumCell.setCellValue(new HSSFRichTextString("合计"));
        sumCell.setCellStyle(cellStyle);
        sheet.addMergedRegion(new CellRangeAddress(sheet.getLastRowNum(),
                sheet.getLastRowNum(), (short) 0, (short) colSum));// 指定合并区域

        for (int i = 2; i < (cellValue.length + 2); i++) {
            sumCell = lastRow.createCell(i);
            sumCell.setCellStyle(cellStyle);
            sumCell.setCellValue(new HSSFRichTextString(cellValue[i - 2]));
        }

    }

    public CellStyle createCellStyle() {
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
     * 设置返回头信息
     *
     * @param response
     * @param fileName
     * @throws UnsupportedEncodingException
     */
    public void setResponse(HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename="
                + new String(fileName.getBytes("GBK"), "ISO8859-1") + ".xls");
    }

    /**
     * 输入EXCEL文件
     *
     * @param fileName 文件名
     */
    public void outputExcel(String fileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(fileName));
            wb.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(String filePath) throws IOException {
        String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
        InputStream stream = new FileInputStream(filePath);
        Workbook wb = null;
        if (fileType.equals("xls")) {
          wb = new HSSFWorkbook(stream);
        } else if (fileType.equals("xlsx")) {
          wb = new XSSFWorkbook(stream);
        } else {
          System.out.println("您输入的excel格式不正确");
        }
        Sheet rs = wb.getSheetAt(0);
        int rowNum = rs.getLastRowNum();
        Row row = rs.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        DecimalFormat formatter = new java.text.DecimalFormat();
//        for (Row row : rs) {
        for (int i = 1; i <= rowNum; i++) {
        	row = rs.getRow(i);
        	 Map<Integer, String> content = new HashMap<Integer, String>();
        	for(int j = 0;j<colNum;j++)
        	{
        		  Cell cell = row.getCell(j);
	        	  String value = "";
	        	  if(cell.CELL_TYPE_STRING == cell.getCellType())
	        	  {
	        		  value = cell.getStringCellValue();
	        	  }
	        	  if(cell.CELL_TYPE_NUMERIC == cell.getCellType())
	        	  {
	        		  value = formatter.format(cell.getNumericCellValue());
	        	  }
	        	  content.put(j, value);
	         }
            // TODO: 2017/1/16
            /*ProductWithBLOBs product = new ProductWithBLOBs();
        	for( Integer key : content.keySet())
        	{
        		System.out.println(key+" "+content.get(key));
        		product.setBrandid(Integer.parseInt(content.get(0)));
        		product.setName(content.get(3));
        		product.setFcateid(Integer.parseInt("1"));
        		product.setScateid(Integer.parseInt("10"));
        		product.setService(content.get(8));
        		product.setDetailDesc(content.get(4));
        		product.setParas(content.get(5));
        		product.setUnit(content.get(6));
        		product.setNumber(content.get(7));
        		product.setPrice("1000");
        		product.setCreateid(Long.parseLong("92"));
        		product.setStatus(1);
        		product.setRepository(Double.parseDouble("1000"));
        		product.setImgUrl("http://sandbox.zhuangyuhao.com/upload/img/KG8H1459409227267.png;http://sandbox.zhuangyuhao.com/upload/img/4XCQ1459409229306.png;http://sandbox.zhuangyuhao.com/upload/img/2P7V1459409231180.png");
        	}
        	productMapper.insertSelective(product);*/
         }
        
      }
}
