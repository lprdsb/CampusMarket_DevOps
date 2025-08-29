package code_sys.LayerService;

import code_sys.Po.Api.Result;
import code_sys.Po.Dto.query.sons.ProductQueryDto;
import code_sys.Po.Dto.update.OrdersDTO;
import code_sys.Po.Entity.Product;
import code_sys.Po.Vo.ChartVO;
import code_sys.Po.Vo.ProductVO;

import java.util.List;

/**
 * 商品的业务逻辑接口
 */
public interface ProductService {

    Result<String> save(Product product);

    Result<String> update(Product product);

    Result<String> batchDelete(List<Integer> ids);

    Result<List<ProductVO>> query(ProductQueryDto productQueryDto);

    Result<String> buyProduct(OrdersDTO ordersDTO);

    Result<String> placeAnOrder(Integer ordersId);

    Result<String> refund(Integer ordersId);

    Result<List<ChartVO>> queryProductInfo(ProductQueryDto productQueryDto);

    Result<List<Product>> getRecommendedProducts(Integer userId);
}
