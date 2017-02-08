package org.leader.lucene.index;

import org.ansj.lucene5.AnsjAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.leader.lucene.util.ExcelUtils;
import org.leader.lucene.util.LuceneUtils;

import java.util.List;
import java.util.Map;

/**
 * 话术索引生成
 *
 * @author ldh
 * @since 2016-10-09 17:10
 */
public class ComunicationSkillsLucenIndex {
    public static final int QUESTION_INDEX = 1;//问题所在列
    public static final int ANSWER_INDEX = 2;//回答所在列

    public static void main(String[] args) throws Exception{
        XSSFSheet sheet = ExcelUtils.getXlsxSheetByName("E:/profile/work/kefuanswerdata.xlsx", "话术");
        int[] indexs = {QUESTION_INDEX, ANSWER_INDEX};
        List<Map<Integer, String>> resultList = ExcelUtils.getCellStringByIndexs(sheet, indexs);
        System.out.println("返回集合大小" + resultList.size());

        Analyzer analyzer = new AnsjAnalyzer(AnsjAnalyzer.TYPE.index);//索引分析器
        Directory directory = LuceneUtils.openFSDirectory("./index");
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);//每次运行都会删除原来存在的索引
        IndexWriter writer = LuceneUtils.getIndexWriter(directory, config);//实例化IndexWriter

        for (Map<Integer, String> result : resultList) {
            String question = question = result.get(QUESTION_INDEX);
            String answer = result.get(ANSWER_INDEX);
            System.out.println("question:" + result.get(QUESTION_INDEX));
            System.out.println("answer:" + result.get(ANSWER_INDEX));
            if (question != null && answer != null) {
                Document document = new Document();
                TextField questionField = new TextField("question", question, Field.Store.YES);
                questionField.setBoost(1.5f);//提供问题的权重
                TextField answerField = new TextField("answer", answer, Field.Store.YES);
                document.add(questionField);
                document.add(answerField);
                writer.addDocument(document);
            }
        }
        LuceneUtils.closeIndexWriter(writer);
        LuceneUtils.closeDirectory(directory);
        System.out.println("索引完毕");
    }
}
