package utilities;

import configReader.ConfigPropReader;
import org.testng.*;
import org.testng.internal.invokers.InvokedMethod;

import java.io.IOException;

public class TestNGListeners implements ITestListener, IExecutionListener, ISuiteListener, IInvokedMethodListener {
    private static final String CONFIG_FILE_PATH = "src/main/resources/allure.properties";
    private final ConfigPropReader configPropReader;

    // Constructor to initialize the ConfigPropReader
    public TestNGListeners() {
        this.configPropReader = new ConfigPropReader(CONFIG_FILE_PATH);
    }


    //  Clean Allure report before execution1
    @Override
    public void onExecutionStart() {
        System.out.println("**************** Welcome to Selenium Framework *****************");
        if (configPropReader.getProperty("allure.report.remove.attachments").equalsIgnoreCase("true")){
            AllureReportHelper.cleanAllureReport();
            System.out.println("Allure Report Cleaned Successfully");
        }
    }

    //      Generate Allure report after execution
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

    public void afterInvocation(InvokedMethod invokedMethod, ITestResult iTestResult) {
        if(iTestResult.getStatus() == ITestResult.FAILURE){
            System.out.println("Failed Test: " + iTestResult.getName());
        }
        if (invokedMethod.isTestMethod()){
            ValidationUtility.assertAll();
        }

    }

}
