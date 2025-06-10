package code_sys.LayerMap;

import code_sys.Po.Entity.Complaint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface LayerComplaintMapper {
    int insertComplaint(Complaint complaint);
    List<Complaint> selectByComplainantId(Integer complainantId);
    List<Complaint> selectAll();
    int updateStatus(@Param("id") Integer id, @Param("status") String status, @Param("handleTime") Date handleTime);
}
