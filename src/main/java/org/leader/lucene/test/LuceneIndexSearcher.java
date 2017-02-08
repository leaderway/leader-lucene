package org.leader.lucene.test;

import org.ansj.lucene5.AnsjAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

/**
 * Lucene检索类
 *
 * @author ldh
 * @since 2016-10-08 11:24
 */
public class LuceneIndexSearcher {
    public static void main(String[] args) throws Exception{
        Analyzer analyzer = new AnsjAnalyzer(AnsjAnalyzer.TYPE.query);
        Directory directory = FSDirectory.open(Paths.get("./index"));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        QueryParser queryParser = new QueryParser("test", analyzer);
        queryParser.setDefaultOperator(QueryParser.Operator.AND);//设置默认的逻辑运算为AND
        queryParser.setPhraseSlop(2);
        Query query = queryParser.parse("索引");
        TopDocs topDocs = indexSearcher.search(query,100);
        System.out.println("总共匹配" + topDocs.totalHits);
        ScoreDoc[] hits = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : hits) {
            System.out.println("得分：" + scoreDoc.score);
            System.out.println("id:" + scoreDoc.doc);
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(document.get("test"));
        }

    }
}
