package cn.itcast.bos.utils;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.itcast.bos.domain.base.SubArea;

@SuppressWarnings("all")
public class ExportExcelUtils {
	
	public static void exportExcelForXlsx(List<SubArea> list,String fileName){
		// 第一步，创建一个webbook，对应一个Excel文件  
        XSSFWorkbook wb = new XSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        XSSFSheet sheet = wb.createSheet("分区表");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        XSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        XSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
  
        XSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("分拣编号 ");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 1);  
        cell.setCellValue("省");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("市");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 3);  
        cell.setCellValue("区");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);  
        cell.setCellValue("关键字");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 5);  
        cell.setCellValue("起始号");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 6);  
        cell.setCellValue("终止号");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 7);  
        cell.setCellValue("单双号");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 8);  
        cell.setCellValue("辅助关键字");  
        cell.setCellStyle(style);
  
        for (int i = 0; i < list.size(); i++) {  
            row = sheet.createRow((int) i + 1);  
            SubArea subArea = list.get(i);
            // 第五步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(subArea.getId());  
            row.createCell((short) 1).setCellValue(subArea.getArea().getProvince());  
            row.createCell((short) 2).setCellValue(subArea.getArea().getCity());  
            row.createCell((short) 3).setCellValue(subArea.getArea().getDistrict());  
            row.createCell((short) 4).setCellValue(subArea.getKeyWords());  
            row.createCell((short) 5).setCellValue(subArea.getStartNum());  
            row.createCell((short) 6).setCellValue(subArea.getEndNum());
            if (subArea.getSingle() == '0') {
            	row.createCell((short) 7).setCellValue("单双号");
			}else if (subArea.getSingle() == '2') {
				row.createCell((short) 7).setCellValue("单号");
			}else {
				row.createCell((short) 7).setCellValue("双号");
			}
            row.createCell((short) 8).setCellValue(subArea.getAssistKeyWords());  
        } 
        // 第六步，将文件存到指定位置  
        try {  
            FileOutputStream fos = new FileOutputStream(fileName);  
            wb.write(fos);  
            fos.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
	
	public static void exportExcelForXls(List<SubArea> list,String fileName){
		// 第一步，创建一个webbook，对应一个Excel文件  
		HSSFWorkbook wb = new HSSFWorkbook();  
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
		HSSFSheet sheet = wb.createSheet("分区表");  
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
		HSSFRow row = sheet.createRow((int) 0);  
		// 第四步，创建单元格，并设置值表头 设置表头居中  
		HSSFCellStyle style = wb.createCellStyle();  
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		
		HSSFCell cell = row.createCell((short) 0);  
		cell.setCellValue("分拣编号 ");  
		cell.setCellStyle(style);  
		cell = row.createCell((short) 1);  
		cell.setCellValue("省");  
		cell.setCellStyle(style);  
		cell = row.createCell((short) 2);  
		cell.setCellValue("市");  
		cell.setCellStyle(style);  
		cell = row.createCell((short) 3);  
		cell.setCellValue("区");  
		cell.setCellStyle(style);
		cell = row.createCell((short) 4);  
		cell.setCellValue("关键字");  
		cell.setCellStyle(style);
		cell = row.createCell((short) 5);  
		cell.setCellValue("起始号");  
		cell.setCellStyle(style);
		cell = row.createCell((short) 6);  
		cell.setCellValue("终止号");  
		cell.setCellStyle(style);
		cell = row.createCell((short) 7);  
		cell.setCellValue("单双号");  
		cell.setCellStyle(style);
		cell = row.createCell((short) 8);  
		cell.setCellValue("辅助关键字");  
		cell.setCellStyle(style);
		
		for (int i = 0; i < list.size(); i++) {  
			row = sheet.createRow((int) i + 1);  
			SubArea subArea = list.get(i);
			// 第五步，创建单元格，并设置值  
			row.createCell((short) 0).setCellValue(subArea.getId());  
			row.createCell((short) 1).setCellValue(subArea.getArea().getProvince());  
			row.createCell((short) 2).setCellValue(subArea.getArea().getCity());  
			row.createCell((short) 3).setCellValue(subArea.getArea().getDistrict());  
			row.createCell((short) 4).setCellValue(subArea.getKeyWords());  
			row.createCell((short) 5).setCellValue(subArea.getStartNum());  
			row.createCell((short) 6).setCellValue(subArea.getEndNum());  
			if (subArea.getSingle() == '0') {
            	row.createCell((short) 7).setCellValue("单双号");
			}else if (subArea.getSingle() == '2') {
				row.createCell((short) 7).setCellValue("单号");
			}else {
				row.createCell((short) 7).setCellValue("双号");
			}
			row.createCell((short) 8).setCellValue(subArea.getAssistKeyWords());  
		} 
		// 第六步，将文件存到指定位置  
		try {  
			FileOutputStream fos = new FileOutputStream(fileName);  
			wb.write(fos);  
			fos.close();  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}  
}
