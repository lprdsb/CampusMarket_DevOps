package cn.service.impl;

import cn.mapper.ComplaintMapper;
import cn.pojo.entity.Complaint;
import cn.service.ComplaintService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ComplaintServiceImpl implements ComplaintService {
    @Resource
    private ComplaintMapper complaintMapper;

    @Override
    public void submitComplaint(Complaint complaint) {
        complaint.setStatus("pending");
        complaint.setCreateTime(new Date());
        complaintMapper.insertComplaint(complaint);
    }

    @Override
    public List<Complaint> getMyComplaints(Integer complainantId) {
        return complaintMapper.selectByComplainantId(complainantId);
    }

    @Override
    public List<Complaint> getAllComplaints() {
        return complaintMapper.selectAll();
    }

    @Override
    public void handleComplaint(Integer id, String status) {
        complaintMapper.updateStatus(id, status, new Date());
    }
}
