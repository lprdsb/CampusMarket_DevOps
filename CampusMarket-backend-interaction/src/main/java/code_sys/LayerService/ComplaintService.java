package code_sys.LayerService;

import code_sys.Po.Entity.Complaint;
import java.util.List;

public interface ComplaintService {
    void submitComplaint(Complaint complaint);

    List<Complaint> getMyComplaints(Integer complainantId);

    List<Complaint> getAllComplaints();

    void handleComplaint(Integer id, String status, Integer complainantId, Integer targetId);
}
