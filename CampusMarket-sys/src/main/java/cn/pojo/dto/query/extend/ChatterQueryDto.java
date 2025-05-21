package cn.pojo.dto.query.extend;

import cn.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ChatterQueryDto extends QueryDto {

    private Integer senderId;

    private Integer receiverId;

    private String content;
}
