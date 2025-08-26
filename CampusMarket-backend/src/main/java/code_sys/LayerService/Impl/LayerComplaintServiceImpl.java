package code_sys.LayerService.Impl;

import code_sys.Interceptor.LocalThreadHolder;
import code_sys.LayerMap.LayerComplaintMapper;
import code_sys.LayerMap.LayerMessageMapper;
import code_sys.LayerMap.LayerProductMapper;
import code_sys.LayerMap.LayerUserMapper;
import code_sys.Po.Dto.query.sons.ProductQueryDto;
import code_sys.Po.Entity.Complaint;
import code_sys.Po.Entity.Message;
import code_sys.Po.Entity.User;
import code_sys.Po.Vo.ProductVO;
import code_sys.LayerService.ComplaintService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class LayerComplaintServiceImpl implements ComplaintService {
    @Resource
    private LayerComplaintMapper layerComplaintMapper;
    @Resource
    private LayerMessageMapper layerMessageMapper;
    @Resource
    private LayerProductMapper layerProductMapper;
    @Resource
    private LayerUserMapper layerUserMapper;

    String getSubmitComplaintString(String userName1, String userName2, String productName1, String content) {
        return "用户【" + userName1 + "】对【" + userName2 + "（你）】的【"
                + productName1 + "】投诉，投诉内容为【" + content + "】!";
    }

    String getHandleComplaintString(String userName, Integer id, String status) {
        return "管理员【" + userName + "】设置了对你的投诉id为【"
                + id + "】的投诉，投诉状态为【" + status +
                "】!";
    }

    @Override
    public void submitComplaint(Complaint complaint) {
        ProductQueryDto productQueryDto = new ProductQueryDto();
        productQueryDto.setId(complaint.getProductId());
        List<ProductVO> productVOList = layerProductMapper.query(productQueryDto);

        Integer userId = LocalThreadHolder.getUserId();
        User user = new User();
        user.setId(userId);
        User operator = layerUserMapper.getByActive(user);
        complaint.setStatus("pending");
        complaint.setCreateTime(new Date());

        complaint.setTargetId(productVOList.get(0).getUserId());
        layerComplaintMapper.insertComplaint(complaint);

        Message message = new Message();
        message.setUserId(productVOList.get(0).getUserId());
        message.setIsRead(false);
        message.setCreateTime(LocalDateTime.now());
        message.setContent(
                getSubmitComplaintString(operator.getUserName(), productVOList.get(0).getUserName(),
                        productVOList.get(0).getName(), complaint.getContent()));
        layerMessageMapper.save(message);

    }

    @Override
    public List<Complaint> getMyComplaints(Integer complainantId) {
        return layerComplaintMapper.selectByComplainantId(complainantId);
    }

    @Override
    public List<Complaint> getAllComplaints() {
        return layerComplaintMapper.selectAll();
    }

    @Override
    public void handleComplaint(Integer id, String status, Integer complainantId, Integer targetId) {
        Integer userId = LocalThreadHolder.getUserId();
        User user = new User();
        user.setId(userId);
        User operator = layerUserMapper.getByActive(user);
        Message message = new Message();
        message.setUserId(targetId);
        message.setIsRead(false);
        message.setCreateTime(LocalDateTime.now());
        message.setContent(getHandleComplaintString(operator.getUserName(), id, status));
        layerMessageMapper.save(message);
        message.setUserId(complainantId);
        layerMessageMapper.save(message);
        layerComplaintMapper.updateStatus(id, status, new Date());
    }
}
