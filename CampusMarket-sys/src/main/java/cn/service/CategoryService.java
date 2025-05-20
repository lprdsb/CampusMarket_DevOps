package cn.service;

import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.CategoryQueryDto;
import cn.pojo.entity.Category;

import java.util.List;

/**
 * 商品类别的业务逻辑接口
 */
public interface CategoryService {

    Result<String> save(Category category);

    Result<String> update(Category category);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<Category>> query(CategoryQueryDto categoryQueryDto);

}
