package utilities;

import configReader.ConfigPropReader;
import org.openqa.selenium.WebDriver;
import org.testng.IAlterSuiteListener;
import org.testng.IExecutionListener;
import org.testng.ITestListener;

import java.io.IOException;

public class TestNGListeners implements ITestListener, IExecutionListener, IAlterSuiteListener {
    private static final String CONFIG_FILE_PATH = "src/main/resources/allure.properties";
    private final ConfigPropReader configPropReader;

    public TestNGListeners() {
        this.configPropReader = new ConfigPropReader(CONFIG_FILE_PATH);
    }


    @Override
    public void onExecutionStart() {
        System.out.println("**************** Welcome to Selenium Framework *****************");
        if (configPropReader.getProperty("allure.report.remove.attachments").equalsIgnoreCase("true")){
            AllureReportHelper.cleanAllureReport();
            System.out.println("Allure Report Cleaned Successfully");
        }
    }

    @Override
    public void onExecutionFinish() {
        System.out.println("Generating Report........");
        if (configPropReader.getProperty("allure.report.open").equalsIgnoreCase("true")) {
            try {
                System.out.println("Opening Allure Report");
                Runtime.getRuntime().exec("reportGeneration.bat");
            } catch (IOException e) {
                System.out.println("Unable to Generate Allure Report, may be there is an issue in the batch file/commands");
            }
        }
        System.out.println("********************* End of Execution *********************");
    }
}
