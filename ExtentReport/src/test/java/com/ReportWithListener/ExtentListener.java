package com.ReportWithListener;

import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.ReportBasics.CustomizedReport;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class ExtentListener implements ITestListener {

	public static WebDriver driver;
	ExtentTest test;
	static Date d = new Date(0);
	static String filenameString = "Extent_" + d.toString().replace(":", "_").replace(" ", "_") + ".html";
	public static ExtentReports extent = ExtentManager.createInstance("./Reports/ListenerReport.html");
	public static ThreadLocal<ExtentTest> testRepor = new ThreadLocal<ExtentTest>();

	public void onFinish(ITestContext context) {
		if (extent != null) {
			extent.flush();
			System.out.println(context.getName()+" *******   EXECUTED   ********* ");
		}
	}

	public void onStart(ITestResult result) {

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailure(ITestResult result) {
		// First arugement
		String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
		test.fail("<details>" + "<summary>" + "<b>" + "<fontcolor=" + "red>" + "Exception occured:Click to see"
				+ "</font>" + "</b>" + "</summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details>" + " \n");

		String failureLogg = "TEST CASE FAILED";
		Markup m = MarkupHelper.createLabel(failureLogg, ExtentColor.RED);
		test.log(Status.FAIL, m);

		// second argument
		String methodName = result.getMethod().getMethodName();

		String logText = "<b>" + "TEST CASE: - " + methodName.toUpperCase() + " FAILED" + "</b>";

		Markup m1 = MarkupHelper.createLabel(logText, ExtentColor.RED);
		test.fail(m1);
		// Third argument individual
		test.fail(result.getThrowable().getMessage());
		test.log(Status.FAIL, result.getName() + " TEST CASE ERROR IS " + result.getThrowable().getMessage());
		// screen shot
		String screenShotPath = CustomizedReport.getScreenshot(driver, result.getName());
		try {
			test.addScreenCaptureFromPath(screenShotPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		// first argument
		String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
		test.skip("<details>" + "<summary>" + "<b>" + "<fontcolor=" + "red>" + "Exception occured:Click to see"
				+ "</font>" + "</b>" + "</summary>" + exceptionMessage.replaceAll(",", "<br>") + "</details>" + " \n");

		String failureLogg = "TEST CASE SKIPPED  " + result.getMethod().getMethodName();
		Markup m = MarkupHelper.createLabel(failureLogg, ExtentColor.ORANGE);
		test.log(Status.SKIP, m);
		// taking screen shot
		String screenShotPath = CustomizedReport.getScreenshot(driver, result.getName());
		try {
			test.addScreenCaptureFromPath(screenShotPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// second argument
		String methodName = result.getMethod().getMethodName();

		String logText = "<b>" + "TEST CASE: - " + methodName.toUpperCase() + " SKIPPED" + "</b>";

		Markup m1 = MarkupHelper.createLabel(logText, ExtentColor.ORANGE);
		test.pass(m1);
		// third argument
		test.skip(result.getThrowable().getMessage());
		test.log(Status.SKIP, result.getName() + " TEST CASE ERROR IS " + result.getThrowable().getMessage());

	}

	@Override
	public void onTestStart(ITestResult result) {
		test = extent.createTest(result.getClass().getName() + " @TestCase" + result.getMethod().getMethodName());
		testRepor.set(test);
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		String methodName = result.getMethod().getMethodName();

		String logText = "<b>" + "TEST CASE: - " + methodName.toUpperCase() + "  PASSED" + "</b>";

		Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
		test.pass(m);

		// logger.getStatus();
		// logger.log(Status.PASS, " TEST CASE PASS IS " + result.getName());
	}
}
