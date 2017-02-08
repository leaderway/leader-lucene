package org.leader.lucene.token;

import org.ansj.domain.Result;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.leader.lucene.util.UserLibraryUtils;

import java.io.IOException;

/**
 * 用户自定义词典操作
 *
 * @author ldh
 * @since 2016-10-12 14:42
 */
public class CustomDicOperator {

    public static void main(String[] args) throws IOException{
        UserLibraryUtils.insertWords("./dic/dic.txt");
        System.out.println("添加新词完成");

        Result term = ToAnalysis.parse("盈嘉宝随金宝推荐的接口项目，系普惠金融信息服务（上海）有限公司旗下专注互联网金融的业务平台");
        System.out.println(term.getTerms());

        UserLibraryUtils.removeWords("./dic/dic.txt");
        System.out.println("删除词语完成");
        term = ToAnalysis.parse("盈嘉宝随金宝推荐的接口项目，系普惠金融信息服务（上海）有限公司旗下专注互联网金融的业务平台");
        System.out.println(term.getTerms());
    }
}
