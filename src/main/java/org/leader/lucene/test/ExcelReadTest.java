package org.leader.lucene.test;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.leader.lucene.util.ExcelUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * xlsx读取测试
 *
 * @author ldh
 * @since 2016-10-09 13:58
 */
public class ExcelReadTest {
    public static final int QUESTION_INDEX = 1;//问题所在列
    public static final int ANSWER_INDEX = 2;//回答所在列


    public static void main(String[] args) throws IOException{


        //InputStream excelFileToLoad = new FileInputStream("E:/profile/work/kefuanswerdata.xlsx");
        //XSSFWorkbook wb = new XSSFWorkbook(excelFileToLoad);
        //
        //XSSFSheet sheet = wb.getSheet("话术");//工作表

        XSSFSheet sheet = ExcelUtils.getXlsxSheetByName("E:/profile/work/kefuanswerdata.xlsx", "话术");
        XSSFRow row;//行
        XSSFCell cell;//单元格

        int[] indexs = {1, 2};
        List<Map<Integer, String>> resultList = ExcelUtils.getCellStringByIndexs(sheet, indexs);
        System.out.println("返回集合大小" + resultList.size());

        for (Map<Integer, String> result : resultList) {
          //if (result.get(1) != null) {
              System.out.println("question:" + result.get(1));
          //}
          //  if (result.get(2) != null) {
                System.out.println("answer:" + result.get(2));
            //}
        }

        //Iterator rows = sheet.rowIterator();//所有行
        //while (rows.hasNext()) {
        //    row = (XSSFRow) rows.next();
            //Iterator cells = row.cellIterator();//行中所有单元格
            //while (cells.hasNext()) {
            //    cell = (XSSFCell) cells.next();
            //    System.out.println(cell.getStringCellValue());
            //}
            //for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
            //    System.out.println("index=" + cellIndex + ";content=" + row.getCell(cellIndex));
            //}
            //如果该行列小于3，说明该行无问题和答案内容，则结束本次循环
            //if (row.getLastCellNum() < 3) {
            //    continue;
            //}
            //String question = null;
            //String answer = null;
            //if (row.getCell(QUESTION_INDEX) != null) {
            //    question = row.getCell(QUESTION_INDEX).getStringCellValue();//问题
            //}
            //if (row.getCell(ANSWER_INDEX) != null) {
            //    answer = row.getCell(ANSWER_INDEX).getStringCellValue();//答案
            //}

        //    System.out.println("q:" + question + "/a:" + answer);
        //}



    }
}
