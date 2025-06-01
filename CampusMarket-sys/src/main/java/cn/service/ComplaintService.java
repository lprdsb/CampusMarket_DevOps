package cn.service;

import cn.pojo.entity.Complaint;
import java.util.List;

public interface ComplaintService {
    void submitComplaint(Complaint complaint);
    List<Complaint> getMyComplaints(Integer complainantId);
    List<Complaint> getAllComplaints();
    void handleComplaint(Integer id, String status);
}
