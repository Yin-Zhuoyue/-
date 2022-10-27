package com.yzy.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * 对线程进行时间限制的切面，超时强制终止当前运行内容
 */
@Component
@Aspect
public class ThreadTimeLimitAspect {

    @Pointcut("@annotation(com.yzy.aop.ThreadTimeLimit)")
    public void pt() {
    }

    /**
     * 在被代理的方法外套一层限时的线程，如果被代理的方法在时限内运行结束，则返回该方法的返回值，
     * 如果超时，则强制终止被代理的方法并返回null
     *
     * @param pjp 切点
     * @return 返回被代理方法的返回值或null
     */
    @Around("pt()")
    public Object timeLimit(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        ThreadTimeLimit threadTimeLimit = method.getAnnotation(ThreadTimeLimit.class);// 获取注解信息

        ExecutorService executor = Executors.newSingleThreadExecutor();// 构造一个执行器
        Callable<Object> call = () -> {
            try {
                return pjp.proceed();// 运行被代理的方法
            } catch (InterruptedException e) {
                return null;
            } catch (Throwable throwable) {
                System.out.println(throwable.toString());
            }
            return null;
        };

        Future<Object> future = executor.submit(call);
        try {
            return future.get(threadTimeLimit.value(), TimeUnit.SECONDS);// 如果不超时则返回被代理的方法的返回值
        } catch (Exception e) {
            future.cancel(true);    // 如果超时则终止当前方法的运行并返回null
            return null;
        } finally {
            executor.shutdown();
        }
    }
}
