package com.dk0124.cdr.cdrApi.controller.logging;

import com.dk0124.cdr.cdrApi.exception.InvalidApiParameterException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class HttpRequestAspect {

    private final ObjectMapper objectMapper;
    Logger logger = LoggerFactory.getLogger(this.getClass());


    @AfterThrowing(pointcut = "execution(public * com.dk0124.cdr.cdrApi.controller.ApiController.*(..))", throwing = "exception")
    public void logError(Exception exception) throws Throwable {
        Map logging = getReqInfo();
        logging.put("message",exception.getMessage());
        if( exception instanceof InvalidApiParameterException)
            logging.put("code" , 400);
        else
            logging.put("code", 500);
        log.info("Error on req : {}",logging);
    }

    @Before("execution(public * com.dk0124.cdr.cdrApi.controller.ApiController.*(..))")
    public void logHttpRequest(JoinPoint joinPoint) throws Throwable {
        logger.info("Request Info: {}", objectMapper.writeValueAsString(getReqInfo()));
    }


    private Map getReqInfo() {
        Map reqInfo = new HashMap<String, String>();
        reqInfo.put("URL", getReqUrl());
        reqInfo.putAll(getReqPathParams());
        reqInfo.putAll(getReqQueryParams());
        return reqInfo;
    }


    private String getReqUrl() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return req.getRequestURI().toString();
    }

    private Map getReqPathParams() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> pathParams = (Map<String, String>) req.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        return pathParams;
    }

    private Map getReqQueryParams() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map pathParams = req.getParameterMap();
        req.getParameterMap()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        k -> k.getKey(),
                        v -> spread(v.getValue())
                        )
                );
        return pathParams;
    }

    private String spread(String[] arr) {
        if (arr.length == 0)
            return null;

        if (arr.length == 1)
            return arr[0];

        StringBuilder sb = new StringBuilder();
        for (String word : arr)
            sb.append(word + ",");
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

}
