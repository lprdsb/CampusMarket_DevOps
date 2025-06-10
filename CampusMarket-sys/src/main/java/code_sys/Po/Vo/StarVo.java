package code_sys.Po.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StarVo {
    private Integer id;
    private Integer user1Id;
    private Integer user2Id;
}
