package code_sys.Po.Dto.query.sons;

import code_sys.Po.Dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 互动行为查询条件类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InteractionQueryDto extends QueryDto {

    private Integer userId;

    private Integer productId;

    private Integer type;
}
