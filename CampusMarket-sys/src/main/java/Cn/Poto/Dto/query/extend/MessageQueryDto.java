package Cn.Poto.Dto.query.extend;

import Cn.Poto.Dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息的查询条件Dto类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageQueryDto extends QueryDto {

    private Integer userId;

    private String content;
}
