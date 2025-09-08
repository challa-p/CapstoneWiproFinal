package listeners;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.*;
import org.openqa.selenium.WebDriver;
import Utils.DriverFactory;
import Utils.ScreenshotUtil;

public class ExtentTestNGListener implements ITestListener {
    private static ExtentReports extent = createInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private static ExtentReports createInstance() {
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/extent/ExtentReport.html");
        ExtentReports ext = new ExtentReports();
        ext.attachReporter(spark);
        return ext;
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest t = extent.createTest(result.getMethod().getMethodName());
        test.set(t);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        try { test.get().pass("Test passed"); } catch (Exception ignored) {}
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            test.get().fail(result.getThrowable());
            WebDriver driver = DriverFactory.getDriver();
            String path = null;
            if (driver != null) {
                path = ScreenshotUtil.takeScreenshot(driver, result.getMethod().getMethodName());
            }
            if (path != null) {
                try { test.get().addScreenCaptureFromPath(path); } catch (Exception e) { e.printStackTrace(); }
            } else {
                test.get().info("Screenshot not available (driver null or capture failed).");
            }
        } catch (Exception ex) {
            System.err.println("Listener failure: " + ex.getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        try { test.get().skip("Test skipped"); } catch (Exception ignored) {}
    }

    @Override
    public void onFinish(ITestContext context) {
        try { extent.flush(); } catch (Exception ignored) {}
    }
}
