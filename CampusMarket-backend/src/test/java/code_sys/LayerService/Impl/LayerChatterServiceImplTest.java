package code_sys.LayerService.Impl;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

public class LayerChatterServiceImplTest {

    @Before
    public void setup() {

    }

    LayerChatterServiceImpl tmp = new LayerChatterServiceImpl();

    @Test
    public void test() {
        // System.out.println(tmp.add(1, 2));
        int hh = tmp.add(1, 2);
        Assert.assertEquals(4, hh);
    }
}
