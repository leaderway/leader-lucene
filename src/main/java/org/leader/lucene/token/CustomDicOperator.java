package org.leader.lucene.token;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.DicAnalysis;
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
        //UserLibraryUtils.insertWords("./library/default.dic");
        //System.out.println("添加新词完成");

        //Result term = ToAnalysis.parse("众筹炒股平台首先需要具备相关的金融资质，平台是否合规？是否有完整的风控体系？资金流向如何保证？这些都是难题");
        //Result term = ToAnalysis.parse("昨日，北京市交通委等8个部门再度约谈滴滴、优步等平台负责人，明确指出以上平台涉嫌违法组织客运经营、逃漏税、违规发送商业性短信息等，此前，上海已加大对专车的查处力度，而北京此番再度将矛头指向专车，京沪双城收紧专车的态度愈发明显。");
        Result termList = DicAnalysis.parse("在优步进入中国之初，选择了百度作为重要的战略合作伙伴，由于阿里和腾讯已经参投了滴滴快的，因此优步与百度的联姻也是无奈之举，但事实上优步进入中国后百度除了提供地图外并无更多实质性的支持，若非支付宝愿意为优步提供支付服务，恐怕优步早已在这场战争中落败。");
        StringBuilder stringBuilder = new StringBuilder();
        for (Term term : termList) {
            String natureStr = term.getNatureStr();
            if ("null".equals(natureStr)) {
                natureStr = "w";
            }
            String name = term.getName();
            String sentenctResult = "<" + natureStr + ">" + name + "</" + natureStr + ">";

            stringBuilder.append(sentenctResult);
        }
        System.out.println(stringBuilder.toString());

        //UserLibraryUtils.removeWords("./library/dic.txt");
        //System.out.println("删除词语完成");
        //term = ToAnalysis.parse("盈嘉宝随金宝推荐的接口项目，系普惠金融信息服务（上海）有限公司旗下专注互联网金融的业务平台");
        //System.out.println(term.getTerms());
    }
}
