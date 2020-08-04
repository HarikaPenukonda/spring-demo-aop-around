package com.udemy.aopdemo.aspect;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.udemy.aopdemo.Account;

@Aspect
@Component
@Order(2)
public class MyDemoLoggingAspect {
	
	@Around("execution(* com.udemy.aopdemo.service.*.getFortune(..))")
	public Object aroundGetFortune(
			ProceedingJoinPoint theProceedingJoinPoint) throws Throwable {
		
		// print out the method we are adving on
		String method = theProceedingJoinPoint.getSignature().toShortString();
		System.out.println("\n=====>>> Executing @Around on method: " + method);
		
		// begin the timestamp
		long begin = System.currentTimeMillis();
		
		// execute the method
		Object result = theProceedingJoinPoint.proceed();
		
		// end timestamp
		long end = System.currentTimeMillis();
		
		// compute duration and display it
		long duration = end-begin;
		System.out.println("\n=====>>> Duration: " +duration/1000.0 + " seconds ");
		
		return result;
		
	}
	
	@After("execution(* com.udemy.aopdemo.dao.AccountDAO.findAccounts(..))")
	public void afterFinallyFindAccountsAdvice(JoinPoint theJoinPoint) {
		
		// print out which method we are advising on
		String method = theJoinPoint.getSignature().toShortString();
		System.out.println("\n=====>>> Executing @After (finally) on method: " + method);
		
	}
	
	@AfterThrowing(
			pointcut="execution(* com.udemy.aopdemo.dao.AccountDAO.findAccounts(..))",
			throwing="theExc")
	public void afterThrowingFindAccountAdvice(
			JoinPoint theJoinPoint, Throwable theExc) {
		
		// print out which method we are advising on
		String method = theJoinPoint.getSignature().toShortString();
		System.out.println("\n=====>>> Executing @AfterThrowing on method: " + method);
		
		// log the exception
		System.out.println("\n======>> The Exception is " + theExc);
	}
	
	// add a new advice for @AfterReturning on the findAccounts method
	
	@AfterReturning(
			pointcut="execution(* com.udemy.aopdemo.dao.AccountDAO.findAccounts(..))",
			returning="result")
	public void afterReturningFindAccountsAdvice(
			JoinPoint theJoinPoint, List<Account> result) {
		
		// print out which method we are advising on
		String method = theJoinPoint.getSignature().toShortString();
		System.out.println("\n=====>>> Executing @AfterReturning on method: " + method);
		
		// print out the results of the metho
		System.out.println("\n=====>>> Result is " + result);
		
		// let's post-process the data.. modify it
		
		// convert the account names to uppercase
		convertAccountNamesToUpperCase(result);
		System.out.println("\n=====>>> Result is " + result);


	}
	private void convertAccountNamesToUpperCase(List<Account> result) {
		
		// loop through accounts
		for(Account tempAccount : result) {
		// get uppercase version of name
		String theUpperName = tempAccount.getName().toUpperCase();
		// update the name of the account
		tempAccount.setName(theUpperName);
	 }
  }


	@Before("com.udemy.aopdemo.aspect.AopExpressions.forDaoPackageNoGetterSetter()") // Apply pointcut declaration to advice
	public void beforeAddAccountAdvice(JoinPoint theJoinPoint) // JoinPoint has the metadata about method call
	{
		System.out.println("\n===> Performing beforeAddAccountAdvice ");
		
		// display the method signature
		MethodSignature methodSign = (MethodSignature)theJoinPoint.getSignature();
		System.out.println("Method: " + methodSign);
		
		// display the method arguments
		
		// get args
		Object[] args = theJoinPoint.getArgs(); // return an array of objects
		// loop through args
		for(Object tempArg : args) {
			System.out.println(tempArg);
			
			if(tempArg instanceof Account) {
				// downcast and print Account specific stuff
				Account theAccount = (Account)tempArg;
				System.out.println("account name: " + theAccount.getName());
				System.out.println("account level: " + theAccount.getLevel());

			}
		}
	}
	
	
	
	
	
	

}
