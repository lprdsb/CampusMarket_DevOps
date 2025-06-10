package Cn.Aop;

import Cn.Context.LocalThreadHolder;
import Cn.Mapper.OperationLogMapper;
import Cn.Poto.Entity.OperationLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Aspect
@Component
public class LogAspect {

    @Resource
    private OperationLogMapper operationLogMapper;

    @Around("@annotation(log)")
    public Object handlePageableParams(ProceedingJoinPoint joinPoint, Log log) throws Throwable {
        OperationLog logEntity = createLogEntity(log);
        operationLogMapper.save(logEntity);
        return joinPoint.proceed();
    }

    private OperationLog createLogEntity(Log log) {
        return OperationLog.builder()
                .createTime(LocalDateTime.now())
                .detail(log.detail())
                .userId(LocalThreadHolder.getUserId())
                .build();
    }

}
