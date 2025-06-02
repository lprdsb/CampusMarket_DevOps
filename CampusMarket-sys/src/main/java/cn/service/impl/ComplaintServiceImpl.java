package cn.service.impl;

import cn.context.LocalThreadHolder;
import cn.mapper.ComplaintMapper;
import cn.mapper.MessageMapper;
import cn.mapper.ProductMapper;
import cn.mapper.UserMapper;
import cn.pojo.dto.query.extend.ProductQueryDto;
import cn.pojo.entity.Complaint;
import cn.pojo.entity.Message;
import cn.pojo.entity.User;
import cn.pojo.vo.ProductVO;
import cn.service.ComplaintService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class ComplaintServiceImpl implements ComplaintService {
    @Resource
    private ComplaintMapper complaintMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public void submitComplaint(Complaint complaint) {
        ProductQueryDto productQueryDto = new ProductQueryDto();
        productQueryDto.setId(complaint.getProductId());
        List<ProductVO> productVOList = productMapper.query(productQueryDto);

        Integer userId = LocalThreadHolder.getUserId();
        User user = new User();
        user.setId(userId);
        User operator = userMapper.getByActive(user);
        complaint.setStatus("pending");
        complaint.setCreateTime(new Date());

        complaint.setTargetId(productVOList.get(0).getUserId());
        complaintMapper.insertComplaint(complaint);

        Message message = new Message();
        message.setUserId(productVOList.get(0).getUserId());
        message.setIsRead(false);
        message.setCreateTime(LocalDateTime.now());
        message.setContent("用户【" + operator.getUserName() + "】对【" + productVOList.get(0).getUserName() + "（你）】的【"
                + productVOList.get(0).getName() + "】投诉，投诉内容为【" + complaint.getContent() + "】!");
        messageMapper.save(message);

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
