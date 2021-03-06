package org.leader.lucene.util;

import org.ansj.library.UserDefineLibrary;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

/**
 * 用户自定义词典工具类
 *
 * @author ldh
 * @since 2016-10-12 15:06
 */
public class UserLibraryUtils {

    /**
     * 通过字符串数组往字典中增加新词
     * @param words
     */
    public static void insertWords (String[] words) {
        for (String word : words) {
            if (StringUtils.isNotBlank(word)) {
                UserDefineLibrary.insertWord(word);//将词语插入词典
            }
        }
    }

    /**
     * 通过词典文件往字典中增加新词
     * @param dicPath
     * @throws IOException
     */
    public static void insertWords(String dicPath) throws IOException{
        File dicFile = FileUtils.getFile(dicPath);
        if (dicFile.exists()) {
            LineIterator iterator = FileUtils.lineIterator(dicFile);
            while (iterator.hasNext()) {
                String word = iterator.next();
                if (StringUtils.isNotBlank(word)) {
                    //System.out.println("增加新词" + word);
                    String[] wordArray = word.split("\\s+");
                    try {
                        UserDefineLibrary.insertWord(wordArray[0], wordArray[1], Integer.parseInt(wordArray[2]));
                    } catch (Exception e) {
                        System.out.println("词典新增词语错误：词条为：" + word + "\n" + e);
                    }
                }
            }
        } else {
            throw new FileNotFoundException("字典文件不存在");
        }
    }

    /**
     * 通过词典文件往字典中增加新词
     * @param dicFile
     * @throws IOException
     */
    public static void insertWords(File dicFile) throws IOException{
        if (dicFile.exists()) {
            LineIterator iterator = FileUtils.lineIterator(dicFile);
            while (iterator.hasNext()) {
                String word = iterator.next();
                if (StringUtils.isNotBlank(word)) {
                    //System.out.println("增加新词" + word);
                    String[] wordArray = word.split("\\s+");
                    try {
                        UserDefineLibrary.insertWord(wordArray[0], wordArray[1], Integer.parseInt(wordArray[2]));
                    } catch (Exception e) {
                        System.out.println("词典新增词语错误：词条为：" + word + "\n" + e);
                    }
                }
            }
        } else {
            throw new FileNotFoundException("字典文件不存在");
        }
    }

    /**
     * 通过字符串数组删除词语
     * @param words
     */
    public static void removeWords (String[] words) {
        for (String word : words) {
            if (StringUtils.isNotBlank(word)) {
                UserDefineLibrary.removeWord(word);
            }
        }
    }

    /**
     * 通过词典文件删除词语
     * @param dicPath
     * @throws IOException
     */
    public static void removeWords(String dicPath) throws IOException{
        File dicFile = FileUtils.getFile(dicPath);
        if (dicFile.exists()) {
            LineIterator iterator = FileUtils.lineIterator(dicFile);
            while (iterator.hasNext()) {
                String word = iterator.next();
                if (StringUtils.isNotBlank(word)) {
                    //System.out.println("删除词语" + word);
                    UserDefineLibrary.removeWord(word);
                }
            }
        }
    }

}
