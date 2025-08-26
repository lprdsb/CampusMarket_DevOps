package code_sys.LayerService.Impl;

import code_sys.LayerService.Impl.LayerLayerCategoryServiceImpl;
import code_sys.LayerMap.LayerCategoryMapper;
import code_sys.Po.Api.ApiResult;
import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.CategoryQueryDto;
import code_sys.Po.Entity.Category;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LayerCategoryServiceImplTest {

    @Mock
    private LayerCategoryMapper layerCategoryMapper;

    @InjectMocks
    private LayerLayerCategoryServiceImpl categoryService;

    @Before
    public void setUp() {
        // 初始化操作可以放在这里
    }

    @Test
    public void testBatchDelete_Success() {
        // 准备测试数据
        List<Integer> ids = Arrays.asList(1, 2, 3);

        // 执行测试
        Result<String> result = categoryService.batchDelete(ids);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals("商品分类删除成功", result.getMsg());
        verify(layerCategoryMapper, times(1)).batchDelete(ids);
    }

    @Test
    public void testUpdate_Success() {
        // 准备测试数据
        Category category = new Category();
        category.setId(1);
        category.setName("更新后的分类名");

        // 执行测试
        Result<String> result = categoryService.update(category);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals("商品分类修改成功", result.getMsg());
        verify(layerCategoryMapper, times(1)).update(category);
    }

    @Test
    public void testUpdate_NameEmpty() {
        // 准备测试数据
        Category category = new Category();
        category.setId(1);
        category.setName("");

        // 执行测试
        Result<String> result = categoryService.update(category);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("商品分类名不能为空", result.getMsg());
        verify(layerCategoryMapper, never()).update(any());
    }

    @Test
    public void testSave_Success() {
        // 准备测试数据
        Category category = new Category();
        category.setName("新分类");

        // 执行测试
        Result<String> result = categoryService.save(category);

        // 验证结果
        assertTrue(result.isSuccess());
        assertEquals("商品分类新增成功", result.getMsg());
        verify(layerCategoryMapper, times(1)).save(category);
    }

    @Test
    public void testSave_NameEmpty() {
        // 准备测试数据
        Category category = new Category();
        category.setName("");

        // 执行测试
        Result<String> result = categoryService.save(category);

        // 验证结果
        assertFalse(result.isSuccess());
        assertEquals("商品分类名不能为空", result.getMsg());
        verify(layerCategoryMapper, never()).save(any());
    }

    @Test
    public void testSaveOrUpdateResult() {
        boolean result = categoryService.saveOrUpdateResult();
        assertTrue(result);
    }

    @Test
    public void testQueryResult() {
        String result = categoryService.queryResult("电子产品");
        assertEquals("成功查找到:电子产品", result);
    }

    @Test
    public void testDeleteResult() {
        String result = categoryService.deleteResult("电子产品");
        assertEquals("成功删除电子产品", result);
    }
}