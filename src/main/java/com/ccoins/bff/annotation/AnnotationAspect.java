package com.ccoins.bff.annotation;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.exceptions.UnauthorizedException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.service.ITablesService;
import com.ccoins.bff.utils.HeaderUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

@Aspect
@Component
public class AnnotationAspect {

    private final ITablesService tablesService;

    @Autowired
    public AnnotationAspect(ITablesService tablesService) {
        this.tablesService = tablesService;
    }

    @Around(value = "@annotation(com.ccoins.bff.annotation.LimitedTime)")
    public Object checkBarLimitedTime(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        HttpHeaders headers = this.getHttpHeaders(request);

        String code = HeaderUtils.getCode(headers);

        GenericRsDTO<Boolean> response = this.tablesService.isTableActiveByCode(code);

        if(Boolean.FALSE.equals(response.getData())){
            throw new UnauthorizedException(ExceptionConstant.UNACTIVE_BAR_ERROR_CODE,
                    response.getMessage().toString());
        }

        return joinPoint.proceed();
    }

    private HttpHeaders getHttpHeaders(HttpServletRequest request){
        return Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        h ->Collections.list(request.getHeaders(h)),
                        (oldValue, newValue) -> newValue,
                        HttpHeaders::new
                ));
    }
}
