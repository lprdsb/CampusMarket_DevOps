package cn.pojo.dto.query.extend;

import cn.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 互动行为查询条件类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StarQueryDto extends QueryDto {
    /**
     * 用户ID
     */
    private Integer user1Id;
    private Integer user2Id;
}
