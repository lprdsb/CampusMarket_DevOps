package cn.pojo.entity;

import java.util.Date;

public class Complaint {
    private Integer id;
    private Integer complainantId; // 投诉人ID
    private Integer targetId;      // 被投诉人ID
    private Integer orderId;       // 关联订单ID
    private String content;        // 投诉内容
    private String status;         // 状态: pending/resolved/rejected
    private Date createTime;
    private Date handleTime;

    // getter/setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getComplainantId() { return complainantId; }
    public void setComplainantId(Integer complainantId) { this.complainantId = complainantId; }
    public Integer getTargetId() { return targetId; }
    public void setTargetId(Integer targetId) { this.targetId = targetId; }
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getHandleTime() { return handleTime; }
    public void setHandleTime(Date handleTime) { this.handleTime = handleTime; }
}
