package code_sys.LayerService.Impl;

import code_sys.Interceptor.LocalThreadHolder;
import code_sys.LayerMap.LayerComplaintMapper;
import code_sys.LayerMap.LayerMessageMapper;
import code_sys.LayerMap.LayerProductMapper;
import code_sys.LayerMap.LayerUserMapper;
import code_sys.Po.Dto.query.sons.ProductQueryDto;
import code_sys.Po.Entity.Complaint;
import code_sys.Po.Entity.Message;
import code_sys.Po.Entity.User;
import code_sys.Po.Vo.ProductVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class LayerComplaintServiceImplTest {

    private LayerComplaintServiceImpl service;
    private LayerComplaintMapper complaintMapperMock;
    private LayerMessageMapper messageMapperMock;
    private LayerProductMapper productMapperMock;
    private LayerUserMapper userMapperMock;

    @Before
    public void setUp() throws Exception {
        service = new LayerComplaintServiceImpl();
        complaintMapperMock = Mockito.mock(LayerComplaintMapper.class);
        messageMapperMock = Mockito.mock(LayerMessageMapper.class);
        productMapperMock = Mockito.mock(LayerProductMapper.class);
        userMapperMock = Mockito.mock(LayerUserMapper.class);

        // 通过反射注入mock
        java.lang.reflect.Field field1 = LayerComplaintServiceImpl.class.getDeclaredField("layerComplaintMapper");
        field1.setAccessible(true);
        field1.set(service, complaintMapperMock);

        java.lang.reflect.Field field2 = LayerComplaintServiceImpl.class.getDeclaredField("layerMessageMapper");
        field2.setAccessible(true);
        field2.set(service, messageMapperMock);

        java.lang.reflect.Field field3 = LayerComplaintServiceImpl.class.getDeclaredField("layerProductMapper");
        field3.setAccessible(true);
        field3.set(service, productMapperMock);

        java.lang.reflect.Field field4 = LayerComplaintServiceImpl.class.getDeclaredField("layerUserMapper");
        field4.setAccessible(true);
        field4.set(service, userMapperMock);
    }

    @Test
    public void testSubmitComplaint() {
        Complaint complaint = new Complaint();
        complaint.setProductId(100);
        complaint.setContent("商品有问题");

        ProductVO productVO = new ProductVO();
        productVO.setUserId(200);
        productVO.setUserName("卖家A");
        productVO.setName("商品A");
        Mockito.when(productMapperMock.query(Mockito.any(ProductQueryDto.class)))
                .thenReturn(Collections.singletonList(productVO));

        User operator = new User();
        operator.setUserName("买家B");
        Mockito.when(userMapperMock.getByActive(Mockito.any(User.class))).thenReturn(operator);

        try (MockedStatic<LocalThreadHolder> mockedStatic = Mockito.mockStatic(LocalThreadHolder.class)) {
            mockedStatic.when(LocalThreadHolder::getUserId).thenReturn(300);

            service.submitComplaint(complaint);

            Assert.assertEquals("pending", complaint.getStatus());
            Assert.assertEquals(Integer.valueOf(200), complaint.getTargetId());
            Mockito.verify(complaintMapperMock).insertComplaint(complaint);
            Mockito.verify(messageMapperMock, Mockito.times(1)).save(Mockito.any(Message.class));
        }
    }

    @Test
    public void testGetMyComplaints() {
        Complaint c1 = new Complaint();
        Complaint c2 = new Complaint();
        List<Complaint> complaints = Arrays.asList(c1, c2);
        Mockito.when(complaintMapperMock.selectByComplainantId(123)).thenReturn(complaints);

        List<Complaint> result = service.getMyComplaints(123);
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testGetAllComplaints() {
        Complaint c1 = new Complaint();
        List<Complaint> complaints = Collections.singletonList(c1);
        Mockito.when(complaintMapperMock.selectAll()).thenReturn(complaints);

        List<Complaint> result = service.getAllComplaints();
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testHandleComplaint() {
        User operator = new User();
        operator.setUserName("管理员A");
        Mockito.when(userMapperMock.getByActive(Mockito.any(User.class))).thenReturn(operator);

        try (MockedStatic<LocalThreadHolder> mockedStatic = Mockito.mockStatic(LocalThreadHolder.class)) {
            mockedStatic.when(LocalThreadHolder::getUserId).thenReturn(400);

            service.handleComplaint(1, "done", 500, 600);

            Mockito.verify(messageMapperMock, Mockito.times(2)).save(Mockito.any(Message.class));
            Mockito.verify(complaintMapperMock).updateStatus(Mockito.eq(1), Mockito.eq("done"),
                    Mockito.any(Date.class));
        }
    }

    @Test
    public void testGetSubmitComplaintString() throws Exception {
        // 通过反射调用包私有方法
        java.lang.reflect.Method method = LayerComplaintServiceImpl.class.getDeclaredMethod(
                "getSubmitComplaintString", String.class, String.class, String.class, String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(service, "买家B", "卖家A", "商品A", "商品有问题");
        Assert.assertTrue(result.contains("买家B"));
        Assert.assertTrue(result.contains("卖家A"));
        Assert.assertTrue(result.contains("商品A"));
        Assert.assertTrue(result.contains("商品有问题"));
    }

    @Test
    public void testGetHandleComplaintString() throws Exception {
        java.lang.reflect.Method method = LayerComplaintServiceImpl.class.getDeclaredMethod(
                "getHandleComplaintString", String.class, Integer.class, String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(service, "管理员A", 1, "done");
        Assert.assertTrue(result.contains("管理员A"));
        Assert.assertTrue(result.contains("1"));
        Assert.assertTrue(result.contains("done"));
    }
}
