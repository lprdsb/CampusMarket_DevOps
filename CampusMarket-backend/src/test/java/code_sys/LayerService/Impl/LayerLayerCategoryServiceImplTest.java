package code_sys.LayerService.Impl;

import org.junit.Before;
import org.junit.Test;

public class LayerLayerCategoryServiceImplTest
{
    @Before
    public void setup() {

    }

    LayerLayerCategoryServiceImpl tmp = new LayerLayerCategoryServiceImpl();

    @Test
    public void test() {
        System.out.println("开始测试查找结果函数");
        System.out.println("正测试结果为："+(tmp.queryResult("bob").equals("成功查找到:bob")));
        System.out.println("反测试结果为："+(tmp.queryResult("bob").equals("成功查找到:amy")));
        System.out.println("测试结束");

        System.out.println("开始测试删除函数");
        System.out.println("正测试结果为："+(tmp.deleteResult("bob").equals("成功删除bob")));
        System.out.println("反测试结果为："+(tmp.deleteResult("bob").equals("成功删除amy")));
        System.out.println("测试结束");

        System.out.println("开始测试修改函数");
        System.out.println("正测试结果为："+(tmp.saveOrUpdateResult()));
        System.out.println("反测试结果为："+(!tmp.saveOrUpdateResult()));
        System.out.println("测试结束");
    }
}
