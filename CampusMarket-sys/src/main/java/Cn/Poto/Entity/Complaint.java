package Cn.Poto.Entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Complaint {
    private Integer id;
    private Integer complainantId; // 投诉人ID
    private Integer targetId; // 被投诉人ID
    private Integer orderId; // 关联订单ID
    private Integer productId; // 关联订单ID
    private String content; // 投诉内容
    private String status; // 状态: pending/resolved/rejected
    private Date createTime;
    private Date handleTime;
}
