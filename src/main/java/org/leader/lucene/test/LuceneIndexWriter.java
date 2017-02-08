package org.leader.lucene.test;

import org.ansj.lucene5.AnsjAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

/**
 * Lucene5.2.1测试
 *
 * @author ldh
 * @since 2016-10-08 10:09
 */

public class LuceneIndexWriter {
    public static void main(String[] args) throws Exception{
        Logger logger = LoggerFactory.getLogger(LuceneIndexWriter.class);
        Analyzer analyzer = new AnsjAnalyzer(AnsjAnalyzer.TYPE.index);
        Directory directory =  FSDirectory.open(Paths.get("./index"));
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);

        String[] fieldText = {"IndexReader：读取索引的工具类，常用子类有DirectoryReader",
                "IndexReader：读取索引的工具操作哈哈哈类，常用子类有DirectoryReader",
                "IndexReader：读取索引的工具操共作哈哈哈类，常用子类有DirectoryReader",
                "IndexReader：读取索引的工具操共有作哈哈哈类，常用子类有DirectoryReader",
                "IndexReader：读取索引的工具删除，常用子类有DirectoryReader"};
        for (String field : fieldText) {
            Document document = new Document();
            document.add(new TextField("test",field , Field.Store.YES));
            indexWriter.addDocument(document);
        }
        indexWriter.close();
        directory.close();
        System.out.println("索引结束");

    }
}
