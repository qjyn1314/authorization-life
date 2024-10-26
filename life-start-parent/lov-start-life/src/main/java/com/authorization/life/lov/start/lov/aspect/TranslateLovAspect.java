package com.authorization.life.lov.start.lov.aspect;

import com.authorization.life.lov.start.lov.anno.TranslateLov;
import com.authorization.life.lov.start.lov.handler.LovTranslator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 翻译aop
 */
@Aspect
@Component
public class TranslateLovAspect {

    @Autowired
    private LovTranslator lovTranslator;

    @Pointcut(value = "@annotation(com.authorization.life.lov.start.lov.anno.TranslateLov)")
    public void pointCut() {
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public Object afterReturning(JoinPoint proceedingJoinPoint, Object result) throws Throwable {
        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = signature.getMethod();
        TranslateLov processLovValue = method.getAnnotation(TranslateLov.class);
        result = lovTranslator.translateObject(processLovValue.targetField(), result);
        return result;
    }
}
