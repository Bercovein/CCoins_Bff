package com.ccoins.bff.annotation;

import com.ccoins.bff.dto.GenericRsDTO;
import com.ccoins.bff.exceptions.UnauthorizedException;
import com.ccoins.bff.exceptions.constant.ExceptionConstant;
import com.ccoins.bff.service.ITablesService;
import com.ccoins.bff.utils.HeaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ccoins.bff.exceptions.constant.ExceptionConstant.NOT_MOBILE_ERROR;

@Aspect
@Component
@Slf4j
public class AnnotationAspect {

    private final ITablesService tablesService;

    private final Boolean deviceCheck;

    @Autowired
    public AnnotationAspect(ITablesService tablesService,
                            @Value("${api.device.check}") Boolean deviceCheck) {
        this.tablesService = tablesService;
        this.deviceCheck = deviceCheck;
    }

    @Around(value = "@annotation(com.ccoins.bff.annotation.LimitedTime)")
    public Object checkBarLimitedTime(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());

        assert attributes != null;

        HttpServletRequest request = attributes.getRequest();

        HttpHeaders headers = this.getHttpHeaders(request);

        String code = HeaderUtils.getCode(headers);

        GenericRsDTO<Boolean> response = this.tablesService.isTableActiveByCode(code);

        if(Boolean.FALSE.equals(response.getData())){
            throw new UnauthorizedException(ExceptionConstant.UNACTIVE_BAR_ERROR_CODE,
                    response.getMessage().toString());
        }

        return joinPoint.proceed();
    }

    @Around(value = "@annotation(com.ccoins.bff.annotation.MobileCheck)")
    public Object checkDevice(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();

        if(deviceCheck != null && deviceCheck){
            for (Object arg : args) {
                if (arg instanceof Device) {
                    Device device = (Device) arg;
                    if (!device.isMobile()) {
                        log.error("NOT A MOBILE DEVICE");
                        throw new UnauthorizedException(ExceptionConstant.NOT_MOBILE_ERROR_CODE,
                                NOT_MOBILE_ERROR);
                    }
                }
            }
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
