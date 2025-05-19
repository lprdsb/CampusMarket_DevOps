package cn.service;

import cn.pojo.api.Result;
import cn.pojo.dto.query.base.QueryDto;
import cn.pojo.dto.query.extend.EvaluationsQueryDto;
import cn.pojo.entity.Evaluations;

import java.util.List;

/**
 * 评论服务接口
 */
public interface EvaluationsService {

    Result<Object> insert(Evaluations evaluations);

    Result<Object> list(Integer contentId, String contentType);

    Result<Object> query(EvaluationsQueryDto evaluationsQueryDto);

    Result<Object> batchDelete(List<Integer> ids);

    Result<String> delete(Integer id);

    Result<Void> update(Evaluations evaluations);

}
