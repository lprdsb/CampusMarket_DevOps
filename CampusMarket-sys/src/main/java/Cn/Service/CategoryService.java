package Cn.Service;

import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.CategoryQueryDto;
import Cn.Poto.Entity.Category;

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
