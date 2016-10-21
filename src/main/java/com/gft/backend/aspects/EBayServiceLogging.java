package com.gft.backend.aspects;

import com.gft.backend.annotations.LogMethodTime;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by miav on 2016-10-18.
 */
@Aspect
public class EBayServiceLogging {
    private static final Logger logger = Logger.getLogger(EBayServiceLogging.class);


    //@Around("execution(* com.gft.backend.services.EBayService.*(..))")
    @Around(value = "@annotation(logMethodTime)", argNames = "logMethodTime")
    public Object around(ProceedingJoinPoint point, LogMethodTime logMethodTime) {
        logger.debug("METHODLOG:try to check "+MethodSignature.class.cast(point.getSignature()).getMethod().getName());
        Object result = null;
        try {
            LocalTime start = LocalTime.now();
            result = point.proceed();
            LocalTime end = LocalTime.now();
            long between = ChronoUnit.MILLIS.between(start, end);
            if(between > logMethodTime.millisecondsThreshold()){
                logger.info("METHODLOG "+
                    MethodSignature.class.cast(point.getSignature()).getMethod().getName() + " in " +
                    ChronoUnit.MILLIS.between(start,end)
                );
            }
        } catch (Throwable throwable) {
            logger.error("LOG Method proceed is fail",throwable);
        }
        return result;
    }
}
