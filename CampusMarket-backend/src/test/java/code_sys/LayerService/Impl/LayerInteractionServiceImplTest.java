package code_sys.LayerService.Impl;

import org.junit.Before;
import org.junit.Test;

public class LayerInteractionServiceImplTest
{
    @Before
    public void setup() {

    }

    LayerInteractionServiceImpl tmp = new LayerInteractionServiceImpl();

    @Test
    public void test() {
        System.out.println("开始测试输出喜欢结果函数");
        System.out.println("正测试结果为："+(tmp.likeResult("bob","大虾").equals("bob喜欢商品:大虾")));
        System.out.println("反测试结果为："+(tmp.likeResult("bob","大虾").equals("bob喜欢商品:海参")));
        System.out.println("测试结束");

        System.out.println("开始测试保存结果函数");
        System.out.println("正测试结果为："+(tmp.saveResult("bob","大虾").equals("bob对大虾的交互成功")));
        System.out.println("反测试结果为："+(tmp.saveResult("bob","大虾").equals("bob对海参的交互成功")));
        System.out.println("测试结束");

        System.out.println("开始测试查找结果函数");
        System.out.println("正测试结果为："+(tmp.queryResult("bob").equals("成功查找到:bob")));
        System.out.println("反测试结果为："+(tmp.queryResult("bob").equals("成功查找到:amy")));
        System.out.println("测试结束");
    }
}
