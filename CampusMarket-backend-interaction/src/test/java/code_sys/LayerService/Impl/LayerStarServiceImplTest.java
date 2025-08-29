package code_sys.LayerService.Impl;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

public class LayerStarServiceImplTest {

    @Before
    public void setup() {

    }

    LayerStarServiceImpl tmp = new LayerStarServiceImpl();

    @Test
    public void test() {
        System.out.println("开始测试re45函数");
        System.out.println("正测试结果为："+(tmp.re45(0)==18));
        System.out.println("反测试结果为："+(tmp.re45(1)==18));
        System.out.println("测试结束");

        System.out.println("开始测试flatline函数");
        System.out.println("正测试结果为："+(tmp.flatline(0)==21));
        System.out.println("反测试结果为："+(tmp.flatline(2)==21));
        System.out.println("测试结束");

        System.out.println("开始测试hemlok函数");
        System.out.println("正测试结果为："+(tmp.hemlok(0)==24));
        System.out.println("反测试结果为："+(tmp.hemlok(3)==24));
        System.out.println("测试结束");
    }
}
