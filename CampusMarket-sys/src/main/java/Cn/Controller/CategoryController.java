package Cn.Controller;

import Cn.Aop.Pager;
import Cn.Aop.Protector;
import Cn.Poto.Api.Result;
import Cn.Poto.Entity.Category;
import Cn.Poto.Dto.query.extend.CategoryQueryDto;
import Cn.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品类别控制器
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 基于注解重构通用方法
    private Result<String> saveOrUpdate(Category category, boolean isUpdate) {
        return isUpdate ? categoryService.update(category) : categoryService.save(category);
    }

    /**
     * 新增
     *
     * @param category 参数
     * @return Result<String> 响应结果
     */
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> save(@RequestBody Category category) {
        return saveOrUpdate(category, false);
    }

    /**
     * 修改
     *
     * @param category 参数
     * @return Result<String> 响应结果
     */
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody Category category) {
        return saveOrUpdate(category, true);
    }

    /**
     * 批量删除
     */
    @Protector(role = "管理员") // 只有管理员能够去进行操作
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return categoryService.batchDelete(ids);
    }

    /**
     * 查询
     *
     * @param categoryQueryDto 查询参数
     * @return Result<List < Category>> 响应结果
     */
    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<Category>> query(@RequestBody CategoryQueryDto categoryQueryDto) {
        return categoryService.query(categoryQueryDto);
    }
}
