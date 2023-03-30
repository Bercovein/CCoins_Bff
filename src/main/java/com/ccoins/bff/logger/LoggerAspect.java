package com.ccoins.bff.logger;

import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggerAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAspect.class);

	private static final String SEPARATOR = " :: ";
	private static final String EXECUTION_TXT = "Execution time = ";
	private static final String INIT_EXECUTION_TXT = "Init execution = ";
	private static final String END_EXECUTION_TXT = "End execution = ";
	private static final String STATUS_OK = "Status = OK ";
	private static final String STATUS_ERROR = "Status = ERROR ";
	private static final String MS_TXT = "ms";
	private static final String POINT = ".";
	private static final String RESULT_TXT = "Result = ";
	private static final String EQUALS_TXT = "=";
	private static final String HTTP_HEADERS_TXT = "httpHeaders";
	private static final String PATH_PARAMETERS_TXT = "Parameters =";

	@Around("execution(* com.ccoins..controller..*(..)))")
	public Object loggerToControllerMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		this.loggerInit(proceedingJoinPoint);
		this.loggerPathParameters(proceedingJoinPoint, (MethodSignature) proceedingJoinPoint.getSignature());
		return loggerExecutionToOK(proceedingJoinPoint);
	}

	@Around("execution(* com.ccoins..service.impl..*(..)))")
	public Object loggerToServiceMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		this.loggerInit(proceedingJoinPoint);
		return loggerExecutionToOK(proceedingJoinPoint);
	}

	@Around("execution(* com.ccoins..feign..*(..)))")
	public Object loggerToClientMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

		this.loggerInit(proceedingJoinPoint);
		return loggerExecutionToOK(proceedingJoinPoint);
	}

//	@AfterThrowing(pointcut = "execution(* com.ccoins..*(..)))", throwing = "e")
//	public void loggerToExceptionMethods(JoinPoint joinPoint, Exception e) {
//
//		StringBuilder sb = new StringBuilder();
//		Signature signature = joinPoint.getSignature();
//		String point = "\\.";
//		String methodName = signature.getName();
//
//		String stuff = sb.append(signature.getDeclaringTypeName()).append(".").append(methodName).toString();
//		String[] classItems = stuff.split(point);
//		String methodClass = classItems.length > 1 ? classItems[classItems.length-2] + "." + classItems[classItems.length-1] : stuff;
//
//		Object[] args = Arrays.stream(joinPoint.getArgs()).filter(o -> !(o.toString().contains("cookie"))).toArray();
//		String arguments = Arrays.toString(args);
//
//		String exceptionName = e.getClass().getName();
//		String[] exceptionItems = exceptionName.split(point);
//		exceptionName = exceptionItems[exceptionItems.length-1];
//
//		String line = Arrays.toString(Arrays.stream(e.getStackTrace()).filter(ex -> (ex.getClassName().concat(".").concat(ex.getMethodName())).contains(methodClass)).toArray());
//
//		if(!line.isEmpty() && line.contains("(") && line.contains(")"))
//			line = line.substring(line.indexOf("("), line.indexOf(")") + 1);
//
//		String exceptionSource = Strings.EMPTY;
//
//		if(e instanceof CustomException){
//			CustomException customException = (CustomException) e;
//			exceptionSource = customException.getMessage() != null ? customException.getMessage() : exceptionSource;
//		}
//
//		final String endInfo = String.format("%s%s%s%s%s%s%s%s%s%s%s%s%s%s",
//				STATUS_ERROR, "[", exceptionName, "] caught in: [",
//				methodClass, "] in line ", line, " with arguments ",
//				arguments, " and source/message ",exceptionSource,": [",e.getMessage(),"]");
//
//		LOGGER.error(endInfo);
//
//	}

	private void loggerInit(ProceedingJoinPoint proceedingJoinPoint) {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

		final String className = methodSignature.getDeclaringType().getSimpleName();
		final String methodName = methodSignature.getName();
		final String initInfo = INIT_EXECUTION_TXT + className + POINT + methodName;

		LOGGER.info(initInfo);
	}

	private Object loggerExecutionToOK(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

		final String className = methodSignature.getDeclaringType().getSimpleName();
		final String methodName = methodSignature.getName();

		final StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		final Object result = proceedingJoinPoint.proceed();
		stopWatch.stop();

		final String endInfo = String.format("%s%s%s%s%s%s%s%s%s%s", END_EXECUTION_TXT, className, POINT, methodName,
				SEPARATOR, EXECUTION_TXT, stopWatch.getTotalTimeMillis(), MS_TXT, SEPARATOR, STATUS_OK);

		LOGGER.info(endInfo);
		return result;
	}

	private void loggerPathParameters(ProceedingJoinPoint proceedingJoinPoint, MethodSignature methodSignature) {

		Object[] args = proceedingJoinPoint.getArgs();
		String[] parametersName = methodSignature.getParameterNames();
		final StringBuilder parametersAndValue = new StringBuilder();

		if (args.length == 0) {
			return;
		}

		for (int i = 0; i < args.length; i++) {
			if (!HTTP_HEADERS_TXT.equals(parametersName[i])) {
				parametersAndValue.append(parametersName[i])
						.append(EQUALS_TXT)
						.append(args[i] != null ? args[i].toString() : Strings.EMPTY)
						.append((i + 1 < args.length) ? SEPARATOR : Strings.EMPTY);
			}
		}
		var log = String.format("%s %s", PATH_PARAMETERS_TXT, parametersAndValue);
		LOGGER.info(log);
	}

}