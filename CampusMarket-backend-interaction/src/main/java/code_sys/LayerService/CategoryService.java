package code_sys.LayerService;

import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.CategoryQueryDto;
import code_sys.Po.Entity.Category;

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
