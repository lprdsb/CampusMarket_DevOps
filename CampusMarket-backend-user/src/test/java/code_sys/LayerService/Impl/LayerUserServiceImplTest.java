package code_sys.LayerService.Impl;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

public class LayerUserServiceImplTest {

    @Before
    public void setup() {

    }

    LayerUserServiceImpl tmp = new LayerUserServiceImpl();

    @Test
    public void test() {
        System.out.println("开始测试r99函数");
        System.out.println("正测试结果为："+(tmp.r99(0)==18));
        System.out.println("反测试结果为："+(tmp.r99(1)==18));
        System.out.println("测试结束");

        System.out.println("开始测试car函数");
        System.out.println("正测试结果为："+(tmp.car(0)==21));
        System.out.println("反测试结果为："+(tmp.car(2)==21));
        System.out.println("测试结束");

        System.out.println("开始测试r301函数");
        System.out.println("正测试结果为："+(tmp.r301(0)==24));
        System.out.println("反测试结果为："+(tmp.r301(3)==24));
        System.out.println("测试结束");
    }
}