package org.leader.lucene.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by leaderway on 2016/10/12.
 */
public class UserLibraryUtilsTest {

    @Test
    public void testInsertWords() throws Exception {
        UserLibraryUtils.insertWords("E:/NlpProject_dev/NlpProject/dic/dic.txt");
        System.out.println("增加新词完成");
    }
}