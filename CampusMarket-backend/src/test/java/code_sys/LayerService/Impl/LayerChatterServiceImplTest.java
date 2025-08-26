package code_sys.LayerService.Impl;

import org.junit.Test;
import org.junit.Before;

public class LayerChatterServiceImplTest {

    @Before
    public void setup() {

    }

    LayerChatterServiceImpl tmp = new LayerChatterServiceImpl();

    @Test
    public void test() {
        System.out.println(tmp.add(1, 2) == 4);
    }
}
