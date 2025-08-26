package code_sys.LayerService.Impl;

import code_sys.Interceptor.LocalThreadHolder;
import code_sys.LayerMap.LayerChatterMapper;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.ChatterQueryDto;
import code_sys.Po.Entity.Chatter;
import code_sys.Po.Vo.ChatterVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LayerChatterServiceImplTest {

    private LayerChatterServiceImpl service;
    private LayerChatterMapper mapperMock;

    @Before
    public void setUp() throws Exception {
        service = new LayerChatterServiceImpl();
        mapperMock = Mockito.mock(LayerChatterMapper.class);
        // // 通过反射注入mock
        java.lang.reflect.Field field = LayerChatterServiceImpl.class.getDeclaredField("layerChatterMapper");
        field.setAccessible(true);
        field.set(service, mapperMock);
    }

    @Test
    public void testAdd() {
        int res = service.add(2, 3);
        Assert.assertEquals(5, res);
    }

    @Test
    public void testQuery() {
        ChatterQueryDto dto = new ChatterQueryDto();
        dto.setSenderId(1);
        dto.setReceiverId(2);

        List<ChatterVO> firstList = Collections.singletonList(new ChatterVO());
        List<ChatterVO> secondList = Collections.singletonList(new ChatterVO());

        Mockito.when(mapperMock.query(Mockito.any())).thenReturn(firstList,
                secondList);

        Result<List<ChatterVO>> result = service.query(dto);

        Assert.assertTrue(result.isSuccess());
        // Assert.assertEquals(2, result.getData().size());
        // 检查参数恢复
        Assert.assertEquals(Integer.valueOf(1), dto.getSenderId());
        Assert.assertEquals(Integer.valueOf(2), dto.getReceiverId());
    }

    @Test
    public void testQueryTable() {
        ChatterQueryDto dto = new ChatterQueryDto();
        List<ChatterVO> tableList = Arrays.asList(new ChatterVO(), new ChatterVO());
        Mockito.when(mapperMock.queryTable(dto)).thenReturn(tableList);

        Result<List<ChatterVO>> result = service.queryTable(dto);

        Assert.assertTrue(result.isSuccess());
        // Assert.assertEquals(2, result.getData().size());
    }
}