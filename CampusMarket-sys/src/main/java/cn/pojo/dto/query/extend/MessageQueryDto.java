package cn.pojo.dto.query.extend;

import cn.pojo.dto.query.base.QueryDto;
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
