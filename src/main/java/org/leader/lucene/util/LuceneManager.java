package org.leader.lucene.util;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Lucene索引读写器、查询器单例获取工具类
 *
 * @author ldh
 * @since 2016-10-08 14:17
 */
public class LuceneManager {
    private volatile static LuceneManager singleton; //获取单例Lucene工具类
    private volatile static IndexWriter writer;//索引生成器
    private volatile static IndexReader reader;//索引读取器
    private volatile static IndexSearcher searcher;//索引检索器
    private final static Lock writerLock = new ReentrantLock();

    public LuceneManager() {
    }

    /**
     * 获取单例的Lucene索引读写器、查询器单例工具类实例
     * @return
     */
    public static LuceneManager getInstance() {
        if (singleton == null) {//如果singleton为null，则说明还没有进行过第一次实例化
            synchronized (LuceneManager.class) {
                if (singleton == null) {
                    return new LuceneManager();
                }
            }
        }
        return singleton;
    }

    /**
     * 获取IndexWriter单例对象
     * @param dir
     * @param config
     * @return
     */
    public IndexWriter getIndexWriter(Directory dir, IndexWriterConfig config) {
        if (dir == null) {
            throw new IllegalArgumentException("Directory can not be null.");
        }
        if (config == null) {
            throw new IllegalArgumentException("IndexWriterConfig can not be null.");
        }
        try {
            writerLock.lock();//上锁
            if (writer == null) {
                if (IndexWriter.isLocked(dir)) {//如果索引目录被锁，则直接抛异常
                    throw new LockObtainFailedException("Directory of index had been locked.");
                }
                writer = new IndexWriter(dir, config);
            }
        } catch (LockObtainFailedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writerLock.unlock();//解锁
        }
        return writer;
    }

    /**
     * 获取IndexReader对象
     * @param dir
     * @param enableNRTReader 是否开启近实时Reader
     * @return
     */
    public IndexReader getIndexReader(Directory dir, boolean enableNRTReader) {
        if (dir == null) {
            throw  new IllegalArgumentException("Directory can not be null.");
        }
        try {
            if (reader == null) {//如果reader还没实例化，则实例化
                reader = DirectoryReader.open(dir);
            } else {
                if (enableNRTReader && reader instanceof DirectoryReader) {//如果开启了近实时Reader，则可看到动态添加、删除索引变化
                    reader = DirectoryReader.openIfChanged((DirectoryReader) reader);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reader;
    }

    /**
     * 获取IndexReader(默认不开启NRTReader)
     * @param dir
     * @return
     */
    public IndexReader getIndexReader(Directory dir) {
        return getIndexReader(dir, false);
    }

    /**
     *获取IndexSearcher对象
     * @param reader
     * @param executor 如果需要开启多线程查询，需提供ExecutorService对象参数
     * @return
     */
    public IndexSearcher getIndexSearcher(IndexReader reader, ExecutorService executor) {
        if (reader == null) {
            throw  new IllegalArgumentException("IndexReader can not be null.");
        }
        if (searcher == null) {
            searcher = new IndexSearcher(reader);
        }
        return searcher;
    }

    /**
     * 获取IndexSearcher对象(单线程)
     * @param reader
     * @return
     */
    public IndexSearcher getIndexSearcher(IndexReader reader) {
        return getIndexSearcher(reader, null);
    }


}
