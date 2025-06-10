package Cn.Poto.Dto.query.extend;

import Cn.Poto.Dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品的查询条件类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductQueryDto extends QueryDto {
    /**
     * 商品名
     */
    private String name;
    /**
     * 所属商品类别ID
     */
    private Integer categoryId;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 是否支持砍价
     */
    private Boolean isBargain;
    /**
     * 价格区间
     */
    private Integer priceMin;
    /**
     * 价格区间
     */
    private Integer priceMax;
    /**
     * 推荐条目
     */
    private Integer recommendLimit;
}
