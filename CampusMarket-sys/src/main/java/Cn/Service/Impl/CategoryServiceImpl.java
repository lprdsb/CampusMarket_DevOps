package Cn.Service.Impl;

import Cn.Mapper.CategoryMapper;
import Cn.Poto.Api.ApiResult;
import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.CategoryQueryDto;
import Cn.Poto.Entity.Category;
import Cn.Service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品类别业务逻辑接口实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 保存或修改
     *
     * @param category 商品分类
     * @param isUpdate 是否是更新操作
     * @return Result<String> 响应结果
     */
    private Result<String> saveOrUpdate(Category category, boolean isUpdate) {
        if (!StringUtils.hasText(category.getName())) {
            return ApiResult.error("商品分类名不能为空");
        }
        if (isUpdate) {
            categoryMapper.update(category);
            return ApiResult.success("商品分类修改成功");
        } else {
            categoryMapper.save(category);
            return ApiResult.success("商品分类新增成功");
        }
    }

    /**
     * 新增
     *
     * @param category 商品分类
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> save(Category category) {
        return saveOrUpdate(category, false);
    }

    /**
     * 修改
     *
     * @param category 商品分类
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> update(Category category) {
        return saveOrUpdate(category, true);
    }

    /**
     * 批量删除
     *
     * @param ids 待删除的ID列表
     * @return Result<String> 响应结果
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        categoryMapper.batchDelete(ids);
        return ApiResult.success("商品分类删除成功");
    }

    /**
     * 查询
     *
     * @param categoryQueryDto 查询参数
     * @return Result<List<Category>> 响应结果
     */
    @Override
    public Result<List<Category>> query(CategoryQueryDto categoryQueryDto) {
        int totalCount = categoryMapper.queryCount(categoryQueryDto);
        List<Category> categoryList = categoryMapper.query(categoryQueryDto);
        return ApiResult.success(categoryList, totalCount);
    }
}
