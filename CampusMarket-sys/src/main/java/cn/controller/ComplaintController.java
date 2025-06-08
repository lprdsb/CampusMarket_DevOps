package cn.controller;

import cn.pojo.entity.Complaint;
import cn.service.ComplaintService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/complaint")
public class ComplaintController {
    @Resource
    private ComplaintService complaintService;

    // 用户提交投诉
    @PostMapping("/submit")
    public String submit(@RequestBody Complaint complaint) {
        complaintService.submitComplaint(complaint);
        return "投诉已提交";
    }

    // 用户查看自己的投诉
    @GetMapping("/my")
    public List<Complaint> myComplaints(@RequestParam Integer complainantId) {
        return complaintService.getMyComplaints(complainantId);
    }

    // 管理员查看所有投诉
    @GetMapping("/all")
    public List<Complaint> allComplaints() {
        return complaintService.getAllComplaints();
    }

    // 管理员处理投诉
    @PostMapping("/handle")
    public String handle(@RequestParam Integer id, @RequestParam String status, @RequestParam Integer complainantId,
            @RequestParam Integer targetId) {
        // System.out.println("asdasdd");
        complaintService.handleComplaint(id, status, complainantId, targetId);
        return "处理完成";
    }
}
