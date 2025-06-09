package cn.aop;

import cn.context.LocalThreadHolder;
import cn.pojo.api.ApiResult;
import cn.pojo.em.RoleEnum;
import cn.service.UserService;
import cn.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 接口鉴权保护切面
 */
@Aspect
@Component
public class ProtectorAspect {

    @Around("@annotation(cn.aop.Protector)")
    public Object auth(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();

        String token = request.getHeader("token");
        if (token == null) {
            return ApiResult.error("身份认证失败，请先登录");
        }

        Claims claims = JwtUtil.parseToken(token);
        if (claims == null) {
            return ApiResult.error("身份认证失败，请先登录");
        }

        Integer userId = claims.get("id", Integer.class);
        Integer roleId = claims.get("role", Integer.class);

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Protector protector = signature.getMethod().getAnnotation(Protector.class);
        if (protector == null) {
            return ApiResult.error("身份认证失败，请先登录");
        }

        String requiredRole = protector.role();
        if (!requiredRole.isEmpty()) {
            String userRole = RoleEnum.ROLE(roleId);
            if (!requiredRole.equals(userRole)) {
                return ApiResult.error("无操作权限");
            }
        }

        LocalThreadHolder.setUserId(userId, roleId);
        try {
            return joinPoint.proceed();
        } finally {
            LocalThreadHolder.clear();
        }
    }

}
