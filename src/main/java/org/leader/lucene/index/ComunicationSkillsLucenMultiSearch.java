package org.leader.lucene.index;

import org.ansj.lucene5.AnsjAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.leader.lucene.util.LuceneUtils;

import java.util.List;

/**
 * 话术检索(多字段检索)
 *
 * @author ldh
 * @since 2016-10-12 13:45
 */
public class ComunicationSkillsLucenMultiSearch {

    public static void main(String[] args) throws Exception {
        Analyzer analyzer = new AnsjAnalyzer(AnsjAnalyzer.TYPE.search);
        Directory directory = LuceneUtils.openFSDirectory("./index");
        IndexReader reader = LuceneUtils.getIndexReader(directory);
        IndexSearcher searcher = LuceneUtils.getIndexSearcher(reader);
        MultiFieldQueryParser parser = LuceneUtils.createMultiFieldQueryParser(new String[]{"answer", "question"}, analyzer);
        parser.setDefaultOperator(QueryParser.Operator.AND);
        Query query = parser.parse("稳盈");

        long searchStartTime = System.currentTimeMillis();
        List<Document> documentList = LuceneUtils.query(searcher, query);
        long searchEndTime = System.currentTimeMillis();
        System.out.println("检索总用时：" + (searchEndTime - searchStartTime));
        System.out.println("匹配的集合大小为：" + documentList.size());

        for (int i=0; i < documentList.size(); i++) {
            System.out.println("========" + (i + 1) + "=========");
            Document document = documentList.get(i);
            System.out.println("question" + document.get("question"));
            System.out.println("answer" + document.get("answer"));
            System.out.println();
        }
        LuceneUtils.closeIndexReader(reader);
        LuceneUtils.closeDirectory(directory);

    }


}
