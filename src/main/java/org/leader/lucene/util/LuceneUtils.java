package org.leader.lucene.util;

import org.ansj.lucene5.AnsjAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Lucene工具类（基于Lucene5.2.1封装）
 *
 * @author ldh
 * @since 2016-10-08 14:12
 */
public class LuceneUtils {
    private static final LuceneManager LUCENE_MANAGER = LuceneManager.getInstance();
    private static Analyzer analyzer = new AnsjAnalyzer(AnsjAnalyzer.TYPE.search);

    //使用静态代码块来导入新词典
    static {
        try {
            UserLibraryUtils.insertWords("./dic/dic.dic");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开索引目录
     * @param luceneDir
     * @return
     */
    public static FSDirectory openFSDirectory(String luceneDir) {
        FSDirectory directory = null;
        try {
            directory = FSDirectory.open(Paths.get(luceneDir));
            /**
             * isLocked方法内部会试图去获取lock，如果获取到lock，就会关闭它，否则return false表示索引目录没有被锁
             */
            IndexWriter.isLocked(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return directory;
    }

    /**
     * 关闭索引目录并销毁
     * @param directory
     * @throws IOException
     */
    public static void closeDirectory(Directory directory) throws IOException {
        if (directory != null) {
            directory.close();
            directory = null;
        }
    }

    /**
     * 获取IndexWriter
     * @param dir
     * @param config
     * @return
     */
    public static IndexWriter getIndexWriter(Directory dir, IndexWriterConfig config) {
        return LUCENE_MANAGER.getIndexWriter(dir, config);
    }

    /**
     * 获取IndexWriter
     * @param directoryPath
     * @param config
     * @return
     */
    public static IndexWriter getIndexWriter(String directoryPath, IndexWriterConfig config) {
        FSDirectory directory = openFSDirectory(directoryPath);
        return LUCENE_MANAGER.getIndexWriter(directory, config);
    }

    /**
     * 获取IndexReader
     * @param dir
     * @param enableNRTReader
     * @return
     */
    public static IndexReader getIndexReader(Directory dir, boolean enableNRTReader) {
        return LUCENE_MANAGER.getIndexReader(dir, enableNRTReader);
    }

    /**
     * 获取IndexReader
     * @param dir
     * @return
     */
    public static IndexReader getIndexReader(Directory dir) {
        return LUCENE_MANAGER.getIndexReader(dir);
    }

    /**
     * 获取IndexReader
     * @param directoryPath
     * @return
     */
    public static IndexReader getIndexReader(String directoryPath) {
        FSDirectory dir = openFSDirectory(directoryPath);
        return LUCENE_MANAGER.getIndexReader(dir);
    }

    /**
     * 获取IndexSearcher对象
     * @param reader
     * @param executor 如果需要开启多线程，需提供ExecutorService对象
     * @return
     */
    public static IndexSearcher getIndexSearcher(IndexReader reader, ExecutorService executor) {
        return LUCENE_MANAGER.getIndexSearcher(reader, executor);
    }

    /**
     * 获取IndexSearcher对象(不支持多线程)
     * @param reader
     * @return
     */
    public static IndexSearcher getIndexSearcher(IndexReader reader) {
        return LUCENE_MANAGER.getIndexSearcher(reader);
    }

    /**
     * 获取QueryParser对象
     * @param field
     * @param analyzer
     * @return
     */
    public static QueryParser createQueryParser(String field, Analyzer analyzer) {
        return new QueryParser(field, analyzer);
    }

    /**
     * 获取MultiFieldQueryParser对象
     * @param fields
     * @param analyzer
     * @return
     */
    public static MultiFieldQueryParser createMultiFieldQueryParser(String[] fields, Analyzer analyzer) {
        return new MultiFieldQueryParser(fields, analyzer);
    }

    /**
     * 获取MultiFieldQueryParser对象
     * @param fields
     * @param analyzer
     * @param boosts
     * @return
     */
    public static MultiFieldQueryParser createMultiFieldQueryParser(String[] fields, Analyzer analyzer,  Map<String,Float> boosts) {
        return new MultiFieldQueryParser(fields, analyzer, boosts);
    }

    /**
     * 关闭并销毁IndexReader
     * @param reader
     */
    public static void closeIndexReader(IndexReader reader) {
        if (reader != null) {
            try {
                reader.close();
                reader = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭并销毁IndexWriter
     * @param writer
     */
    public static void closeIndexWriter(IndexWriter writer) {
        if (writer != null) {
            try {
                writer.close();
                writer = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭IndexWriter和IndexReader
     * @param reader
     * @param writer
     */
    public static void closeAll(IndexReader reader, IndexWriter writer) {
        closeIndexReader(reader);
        closeIndexWriter(writer);
    }

    /**
     * 删除索引（注意：需要自己关闭IndexWriter对象）
     * @param writer
     * @param field
     * @param value
     */
    public static void deleteIndex(IndexWriter writer, String field, String value) {
        try {
            writer.deleteDocuments(new Term[] {new Term(field, value)});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引（注意：需要自己关闭IndexWriter对象）
     * @param writer
     * @param query
     */
    public static void deleteIndex(IndexWriter writer, Query query) {
        try {
            writer.deleteDocuments(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除索引（注意：需要自己关闭IndexWriter对象）
     * @param writer
     * @param terms
     */
    public static void deleteIndexs(IndexWriter writer, Term[] terms) {
        try {
            writer.deleteDocuments(terms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量删除索引（注意：需要自己关闭IndexWriter对象）
     * @param writer
     * @param queries
     */
    public static void deleteIndexs(IndexWriter writer, Query[] queries) {
        try {
            writer.deleteDocuments(queries);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除所有索引
     * @param writer
     */
    public static void deleteAllIndex(IndexWriter writer) {
        try {
            writer.deleteAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新索引
     * @param writer
     * @param term
     * @param document
     */
    public static void updateIndex(IndexWriter writer, Term term, Document document) {
        try {
            writer.updateDocument(term, document);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新索引
     * @param writer
     * @param field
     * @param value
     * @param document
     */
    public static void updateIndex(IndexWriter writer, String field, String value, Document document) {
       updateIndex(writer, new Term(field, value), document);
    }

    /**
     * 添加索引
     * @param writer
     * @param document
     */
    public static void addIndex(IndexWriter writer, Document document) {
        updateIndex(writer, null, document);
    }

    /**
     * 索引查询
     * @param searcher
     * @param query
     * @return
     */
    public static List<Document> query(IndexSearcher searcher, Query query) {
        TopDocs topDocs = null;
        try {
            topDocs = searcher.search(query, Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ScoreDoc[] scores = topDocs.scoreDocs;
        int length = scores.length;//命中的数量
        if (length <= 0) {
            return Collections.emptyList();
        }
        List<Document> documentList = new ArrayList<Document>();
        try {
            for (ScoreDoc scoreDoc : scores) {
                Document document = searcher.doc(scoreDoc.doc);
                documentList.add(document);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentList;
    }

    /**
     * 获取索引文档的总数（注意：需要手动关闭IndexReader）
     * @param reader
     * @return
     */
    public static int getIndexTotalCount(IndexReader reader) {
        return reader.numDocs();
    }

    /**
     * 获取索引文档中最大文档ID（注意：需要手动关闭IndexReader)
     * @param reader
     * @return
     */
    public static int getMaxDocId(IndexReader reader) {
        return reader.maxDoc();
    }

    /**
     * 获取已经删除尚未提交的文档总数（注意：需要手动关闭IndexReader)
     * @param reader
     * @return
     */
    public static int getDeletedDocNum(IndexReader reader) {
        return getMaxDocId(reader) - getIndexTotalCount(reader);
    }

    /**
     * 根据docId查询文档
     * @param reader
     * @param docID
     * @param fieldsToLoad 需要返回的field
     * @return
     */
    public static Document findDocumentByDocId(IndexReader reader, int docID, Set<String> fieldsToLoad) {
        try {
            return reader.document(docID, fieldsToLoad);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据docId查询文档
     * @param reader
     * @param docID
     * @return
     */
    public static Document findDocumentByDocId(IndexReader reader, int docID) {
        return findDocumentByDocId(reader, docID, null);
    }

    /**
     * 创建高亮器
     * @param query 索引查询对象
     * @param prefix 高亮前缀字符串
     * @param suffix 高亮后缀字符串
     * @param fragmenterLength 摘要最大长度
     * @return
     */
    public static Highlighter createHighlighter(Query query, String prefix, String suffix, int fragmenterLength) {
        Formatter formatter = new SimpleHTMLFormatter((prefix == null || prefix.trim().length() == 0) ? "<font color=\"red\">" : prefix,
                (suffix == null || suffix.trim().length() == 0) ? "</font>" : suffix);
        Scorer fragmentScorer = new QueryScorer(query);
        Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
        Fragmenter fragmenter = new SimpleFragmenter(fragmenterLength <= 0 ? 50 : fragmenterLength);
        highlighter.setTextFragmenter(fragmenter);
        return highlighter;
    }

    /**
     * 生成高亮文本
     * @param document 索引文档对象
     * @param highlighter 高亮器
     * @param analyzer 索引分词器
     * @param field 高亮字段
     * @return
     * @throws IOException
     */
    public static String highLight(Document document, Highlighter highlighter, Analyzer analyzer, String field) throws IOException {
        List<IndexableField> indexableFieldList = document.getFields();
        for (IndexableField fieldable : indexableFieldList) {
            String fieldValue = fieldable.stringValue();
            if (fieldable.name().equals(field)) {
                try {
                    fieldValue = highlighter.getBestFragment(analyzer, field, fieldValue);
                } catch (InvalidTokenOffsetsException e) {
                    fieldValue = fieldable.stringValue();
                }
                return (fieldValue == null || fieldValue.trim().length() == 0) ? fieldable.stringValue() : fieldValue;
            }
        }
        return null;
    }

    /**
     * 获取符合条件的总记录数
     * @param searcher
     * @param query
     * @return
     */
    public static int searchTotalRecord(IndexSearcher searcher, Query query) {
        ScoreDoc[] docs = null;
        try {
            TopDocs topDocs = searcher.search(query, Integer.MAX_VALUE);
            if (topDocs == null || topDocs.scoreDocs == null || topDocs.scoreDocs.length ==0) {
                return 0;
            }
            docs = topDocs.scoreDocs;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return docs.length;
    }

    /**
     * lucene分页查询
     * @param searcher
     * @param directory
     * @param query
     * @param page
     */
    public static void pageQuery(IndexSearcher searcher, Directory directory, Query query, Page<Document> page) {
        int totalRecord = searchTotalRecord(searcher, query);
        page.setTotalRecord(totalRecord);//设置总记录数
        TopDocs topDocs = null;
        try {
            topDocs = searcher.searchAfter(page.getAfterDoc(), query, page.getPageSize());
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Document> docList = new ArrayList<Document>();
        ScoreDoc[] docs = topDocs.scoreDocs;
        int index = 0;
        for (ScoreDoc scoreDoc : docs) {
            int docID = scoreDoc.doc;
            Document document = null;
            try {
                document = searcher.doc(docID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (index == docs.length - 1) {
                page.setAfterDoc(scoreDoc);
                page.setAfterDocId(docID);
            }
            docList.add(document);
            index++;
        }
        page.setItems(docList);
        closeIndexReader(searcher.getIndexReader());
    }

    public static void pageQuery(IndexSearcher searcher, Directory directory, Query query, Page<Document> page,
                                 HighlighterParam highlighterParam, IndexWriterConfig writerConfig) throws Exception {
        IndexWriter writer = null;
        //若未设置高亮
        if (highlighterParam == null || !highlighterParam.isHighlight()) {
            pageQuery(searcher, directory, query, page);
        } else {
            int totalRecord = searchTotalRecord(searcher, query);
            System.out.println("totalRecord:" + totalRecord);
            //设置总记录数
            page.setTotalRecord(totalRecord);
            TopDocs topDocs = searcher.searchAfter(page.getAfterDoc(), query, page.getPageSize());
            List<Document> docList = new ArrayList<Document>();
            ScoreDoc[] docs = topDocs.scoreDocs;
            int index = 0;
            writer = getIndexWriter(directory, writerConfig);
            for (ScoreDoc scoreDoc : docs) {
                int docID = scoreDoc.doc;
                Document document = searcher.doc(docID);
                String content = document.get(highlighterParam.getFieldName());//获取高亮字段内容
                if (content != null && content.trim().length() > 0) {
                    //创建高亮器
                    Highlighter highlighter = LuceneUtils.createHighlighter(query, highlighterParam.getPrefix(),
                            highlighterParam.getSuffix(), highlighterParam.getFragmenterLength());
                    String text = highLight(document, highlighter, analyzer, highlighterParam.getFieldName());//生成高亮文本
                    //若高亮文本与原始文本不同，表示高亮成功
                    if (!text.equals(content)) {
                        Document tempDocument = new Document();
                        List<IndexableField> indexableFieldList = document.getFields();
                        if (indexableFieldList != null && indexableFieldList.size() > 0) {
                            for (IndexableField field : indexableFieldList) {
                                if (field.name().equals(highlighterParam.getFieldName())) {
                                    tempDocument.add(new TextField(field.name(), text, Field.Store.YES));//更新高亮的字段内容为高亮后的文本
                                } else {
                                    tempDocument.add(field);
                                }
                            }
                        }
                        updateIndex(writer, new Term(highlighterParam.getFieldName(), content), tempDocument);//更新索引
                        document = tempDocument;
                    }
                }
                //如果为当页最后一个ScoreDoc，则需将其记录在page对象中
                if (index == docs.length -1) {
                    page.setAfterDoc(scoreDoc);
                    page.setAfterDocId(scoreDoc.doc);
                }
                docList.add(document);
                index++;
            }
            page.setItems(docList);
        }
        closeAll(searcher.getIndexReader(), writer);
    }


}
