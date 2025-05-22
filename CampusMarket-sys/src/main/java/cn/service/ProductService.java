package cn.service;

import cn.pojo.api.Result;
import cn.pojo.dto.query.extend.ProductQueryDto;
import cn.pojo.dto.update.OrdersDTO;
import cn.pojo.entity.Orders;
import cn.pojo.entity.Product;
import cn.pojo.vo.ChartVO;
import cn.pojo.vo.ProductVO;

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
