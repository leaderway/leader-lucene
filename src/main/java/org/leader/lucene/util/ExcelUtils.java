package org.leader.lucene.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.util.ArrayUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * excel读取工具类
 *
 * @author ldh
 * @since 2016-10-09 17:11
 */
public class ExcelUtils {

    /**
     * 根据xlsx的路径以及工作表名返回xlsx工作表
     * @param excelPath
     * @param sheetName
     * @return
     * @throws IOException
     */
    public static XSSFSheet getXlsxSheetByName(String excelPath, String sheetName) throws IOException{
        InputStream excelFileToLoad = new FileInputStream(excelPath);
        XSSFWorkbook wb = new XSSFWorkbook(excelFileToLoad);
        XSSFSheet sheet = wb.getSheet(sheetName);
        return sheet;
    }

    /**
     * 返回指定索引的cell的值
     * @param sheet
     * @param indexs
     * @return
     */
    public static List<Map<Integer, String>> getCellStringByIndexs(Sheet sheet, int[] indexs) {
        if (indexs != null && indexs.length > 0) {
            Iterator<Row> rows = sheet.rowIterator();
            Row row = null;
            List<Map<Integer, String>> cellStringList = new ArrayList<Map<Integer, String>>();
            while (rows.hasNext()) {
                row = rows.next();
                if (row == null) {
                    continue;
                }
                Map<Integer, String> cellMap = new HashMap<Integer, String>();
                for(int i=0; i < indexs.length; i++) {
                    if (row.getCell(indexs[i]) != null ) {
                        cellMap.put(indexs[i], row.getCell(indexs[i]).getStringCellValue());
                        //System.out.println(i + "读取的内容为:" + row.getCell(indexs[i]).getStringCellValue());
                    }
                }
                cellStringList.add(cellMap);
            }
            return cellStringList;
        }
        return null;
    }
}
