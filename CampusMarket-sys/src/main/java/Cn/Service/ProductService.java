package Cn.Service;

import Cn.Poto.Api.Result;
import Cn.Poto.Dto.query.extend.ProductQueryDto;
import Cn.Poto.Dto.update.OrdersDTO;
import Cn.Poto.Entity.Product;
import Cn.Poto.Vo.ChartVO;
import Cn.Poto.Vo.ProductVO;

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
