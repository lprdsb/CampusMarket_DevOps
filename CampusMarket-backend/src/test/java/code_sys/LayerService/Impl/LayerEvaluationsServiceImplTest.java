package code_sys.LayerService.Impl;

import org.junit.Before;
import org.junit.Test;

public class LayerEvaluationsServiceImplTest
{
    @Before
    public void setup() {

    }

    LayerEvaluationsServiceImpl tmp = new LayerEvaluationsServiceImpl();

    @Test
    public void test() {
        System.out.println("开始测试查询禁言状态函数");
        System.out.println("正测试结果为："+tmp.show_IsWord("小明").equals("小明已被禁言"));
        System.out.println("反测试结果为："+tmp.show_IsWord("小明").equals("小红已被禁言"));
        System.out.println("测试结束");

        System.out.println("开始测试计算评论数函数");
        System.out.println("正测试结果为："+(tmp.addCountVotes(1,2)==3));
        System.out.println("反测试结果为："+(tmp.addCountVotes(1,2)==4));
        System.out.println("测试结束");

        System.out.println("开始测试计算全部评论数函数");
        System.out.println("正测试结果为："+(tmp.showCommentCount(1)==1));
        System.out.println("反测试结果为："+(tmp.showCommentCount(1)==2));
        System.out.println("测试结束");

        System.out.println("开始测试点赞通知函数");
        System.out.println("正测试结果为："+(tmp.noice("bob","amy").equals("已通知bob来自amy的点赞")));
        System.out.println("反测试结果为："+(tmp.noice("bob","amy").equals("已通知bob来自black的点赞")));
        System.out.println("测试结束");

        System.out.println("开始测试删除结果函数");
        System.out.println("正测试结果为："+(tmp.deleteResult("bob","牛逼").equals("bob删除了评论：牛逼")));
        System.out.println("反测试结果为："+(tmp.deleteResult("bob","牛逼").equals("bob删除了评论：傻逼")));
        System.out.println("测试结束");
    }
}
