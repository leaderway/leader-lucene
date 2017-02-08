package org.leader.lucene.test;

import org.ansj.lucene5.AnsjAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.leader.lucene.util.LuceneUtils;

import java.util.List;

/**
 * lucene工具类测试
 *
 * @author ldh
 * @since 2016-10-09 10:57
 */
public class LuceneUtilsTest {
    public static void main(String[] args) throws Exception{
        long indexStartTime = System.currentTimeMillis();
        Analyzer analyzer = new AnsjAnalyzer(AnsjAnalyzer.TYPE.index);//索引分析器
        Directory directory = LuceneUtils.openFSDirectory("./index");
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = LuceneUtils.getIndexWriter(directory, config);//实例化IndexWriter

        //测试字符数组
        String[] fieldText = {"IndexReader：读取索引的工具类，常用子类有DirectoryReader",
                "IndexReader：读取索引的工具操作哈哈哈类，常用子类有DirectoryReader",
                "IndexReader：读取索引的工具操共作哈哈哈类，常用子类有DirectoryReader",
                "IndexReader：读取索引的工具操共有作哈哈哈类，常用子类有DirectoryReader",
                "IndexReader：读取索引的工具删除，常用子类有DirectoryReader"};

        for (String text : fieldText) {
            Document document = new Document();
            document.add(new TextField("test", text, Field.Store.YES));
            writer.addDocument(document);
        }
        LuceneUtils.closeIndexWriter(writer);
        long indexEndTime = System.currentTimeMillis();
        System.out.println("索引时间：" + (indexEndTime - indexStartTime));

        long searchStartTime = System.currentTimeMillis();
        IndexReader reader = LuceneUtils.getIndexReader(directory);
        IndexSearcher searcher = LuceneUtils.getIndexSearcher(reader);
        QueryParser queryParser = LuceneUtils.createQueryParser("test", analyzer);
        Query query = queryParser.parse("索引");

        List<Document> documentList = LuceneUtils.query(searcher, query);
        System.out.println("匹配的集合大小为" + documentList.size());
        for (Document doc : documentList) {
            System.out.println("命中的内容为" + doc.get("test"));
        }
        long searchEndTime = System.currentTimeMillis();
        System.out.println("检索总用时：" + (searchEndTime - searchStartTime));
        LuceneUtils.closeIndexReader(reader);
        LuceneUtils.closeDirectory(directory);
    }
}
