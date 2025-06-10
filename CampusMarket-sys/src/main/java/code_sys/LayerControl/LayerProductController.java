package code_sys.LayerControl;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import code_sys.Po.Entity.Product;
import code_sys.Po.Vo.ChartVO;
import code_sys.Po.Vo.ProductVO;
import code_sys.Aop.Log;
import code_sys.Aop.Pager;
import code_sys.Po.Api.Result;
import code_sys.LayerService.ProductService;
import code_sys.LayerEnvironment.LocalThreadHolder;
import code_sys.Po.Dto.query.sons.ProductQueryDto;
import code_sys.Po.Dto.update.OrdersDTO;

/**
 * 商品管理接口入口
 */
@CrossOrigin
@RestController
@RequestMapping("/product")
public class LayerProductController {

    @Resource
    ProductService productManager;

    // ==================== 商品操作接口 ====================

    @Log(detail = "创建新商品")
    @PostMapping(value = "/save")
    @ResponseBody
    public Result<String> addNewProduct(@RequestBody Product newProd) {
        return productManager.save(newProd);
    }

    @Log(detail = "变更商品信息")
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> modifyProduct(@RequestBody Product existingProd) {
        return productManager.update(existingProd);
    }

    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> removeProducts(@RequestBody List<Integer> idList) {
        return productManager.batchDelete(idList);
    }

    // ==================== 订单操作接口 ====================

    @Log(detail = "用户购买商品")
    @PostMapping(value = "/buyProduct")
    @ResponseBody
    public Result<String> purchaseItem(@RequestBody OrdersDTO orderData) {
        return productManager.buyProduct(orderData);
    }

    @PostMapping(value = "/placeAnOrder/{ordersId}")
    @ResponseBody
    public Result<String> confirmOrder(@PathVariable Integer ordersId) {
        return productManager.placeAnOrder(ordersId);
    }

    @Log(detail = "用户申请退款")
    @PostMapping(value = "/refund/{ordersId}")
    @ResponseBody
    public Result<String> requestRefund(@PathVariable Integer ordersId) {
        return productManager.refund(ordersId);
    }

    // ==================== 数据查询接口 ====================

    @Pager
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<ProductVO>> searchProducts(@RequestBody ProductQueryDto searchParams) {
        return productManager.query(searchParams);
    }

    @PostMapping(value = "/queryUser")
    @ResponseBody
    public Result<List<ProductVO>> userProductQuery(@RequestBody ProductQueryDto userQuery) {
        userQuery.setUserId(LocalThreadHolder.getUserId());
        return productManager.query(userQuery);
    }

    @GetMapping(value = "/getById/{id}")
    @ResponseBody
    public Result<List<ProductVO>> fetchById(@PathVariable Integer id) {
        ProductQueryDto param = new ProductQueryDto();
        param.setUserId(id);
        return productManager.query(param);
    }

    @PostMapping(value = "/queryProductInfo")
    @ResponseBody
    public Result<List<ChartVO>> retrieveProductStats(@RequestBody ProductQueryDto statParams) {
        return productManager.queryProductInfo(statParams);
    }

    // ==================== 个性化接口 ====================

    @GetMapping("/recommend")
    @ResponseBody
    public Result<List<Product>> getSuggestedItems() {
        Integer currentUserId = LocalThreadHolder.getUserId();
        return productManager.getRecommendedProducts(currentUserId);
    }
}