package code_sys.Po.Dto.query.sons;

import code_sys.Po.Dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ChatterQueryDto extends QueryDto {

    private Integer senderId;

    private Integer receiverId;

    private String content;
}
