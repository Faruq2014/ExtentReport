package com.ReportListener;

import java.io.File;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
	private static ExtentReports extent;

	public static ExtentReports createInstance() {
		String filepath = getReportName();
		String directory = System.getProperty("user.dir") + "/myreports/";
		new File(directory).mkdirs();
		String path = directory + filepath;
		ExtentHtmlReporter reporter = new ExtentHtmlReporter(path);
		// ExtentHtmlReporter reporter = new ExtentHtmlReporter(filename);

		// reporter = new ExtentHtmlReporter("./myreports/ListenerReport.html");

		extent = new ExtentReports();

		// ExtentHtmlReporter reporter is for setting up look and feel environment.
		reporter.config().setDocumentTitle("Automation Report"); // Tile of report
		reporter.config().setReportName("Functional Testing"); // Name of the report
		reporter.config().setDocumentTitle("utf-8");
		// reporter.config().setTheme(Theme.STANDARD);
		reporter.config().setTheme(Theme.DARK);

//ExtentReports extent; is setting environment with the help of java system class. 
		extent.setSystemInfo("User name", System.getProperty("user.name"));
		extent.setSystemInfo("Time Zone", System.getProperty("user.timezone"));
		extent.setSystemInfo("User Location", System.getProperty("user.country"));
		extent.setSystemInfo("OS name", System.getProperty("os.name"));
		extent.setSystemInfo("OS version", System.getProperty("os.version"));
		extent.setSystemInfo("JDK version", System.getProperty("java.version"));
		extent.setSystemInfo("Selenium version", "3.141.59");
		extent.setSystemInfo("Maven version", "3.15"); // you can write any thing.
		extent.attachReporter(reporter);
		return extent;

	}

	public static String getReportName() {
		Date d = new Date();
		String fileName = "automationReport" + d.toString().replace(":", "_").replace(" ", "_") + ".html";
		// String fileName ="automationReport" +System.currentTimeMillis();
		return fileName;
	}

}
