package com.ReportListener;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

	public void onFinish(ITestResult Result) {

	}

	public void onStart(ITestResult result) {
		System.out.println("test started");
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult Result) {

	}


	public void onTestSkipped(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		System.out.println("Test just skipped " + methodName);
	}

	public void onTestStart(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		System.out.println("Test just start " + methodName);
	}

	public void onTestSuccess(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		System.out.println("Test just pass " + methodName);
	}
	

	public void onTestFailure(ITestResult result) {
		String methodName = result.getMethod().getMethodName();
		System.out.println("Test just fail " + methodName);
	}

	public static String takeScreenshot(WebDriver driver, String methodeName) {
		String fileName = getScreenshotName(methodeName);
		String directory = System.getProperty("user.dir") + "/myscreenshot/";
		new File(directory).mkdirs();
		String path = directory + fileName;
		try {
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, new File(path));
			System.out.println("screen" + path);
		} catch (IOException e) {
			System.out.println("Capture Failed " + e.getMessage());
			e.printStackTrace();
		}
		return path;

	}

	public static String getScreenshotName(String methodName) {
		Date d = new Date();
		String fileName = methodName + d.toString().replace(":", "_").replace(" ", "_") + ".png";
		// String fileName ="automationReport" +System.currentTimeMillis();
		return fileName;
	}
}
