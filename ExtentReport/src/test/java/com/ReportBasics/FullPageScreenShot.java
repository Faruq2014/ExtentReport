package com.ReportBasics;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.WebDriver;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class FullPageScreenShot {

//	https://www.youtube.com/watch?v=QQAmcnGbVQM&list=PL7BYd102yEfWJ94HefoyRzFd0yXitD8pF&index=6

	public static String captureFullPge(WebDriver driver, String fullPage) throws IOException {

		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
				.takeScreenshot(driver);
		String dest = System.getProperty("user.dir") + "/Screenshot/" + fullPage + System.currentTimeMillis() + ".png";
		ImageIO.write(screenshot.getImage(), "PNG", new File(dest));

		return dest;

	}

}
