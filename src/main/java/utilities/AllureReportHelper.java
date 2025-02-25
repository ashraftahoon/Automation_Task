package utilities;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class AllureReportHelper {

    private static final Logger logger = LoggerFactory.getLogger(AllureReportHelper.class);
    private static final String ALLURE_RESULTS_DIR = "test-outputs/allure-results";

    private AllureReportHelper() {
        // Private constructor to prevent instantiation
    }

    /**
     * Cleans the Allure results directory.
     */
    public static void cleanAllureReport() {
        File allureResultsDir = new File(ALLURE_RESULTS_DIR);
        System.out.println(allureResultsDir);
        // Debug the exists() check
        boolean exists = allureResultsDir.exists();
        if (!exists) {
            logger.error("Allure results directory does not exist: {}", allureResultsDir.getAbsolutePath());
            return;
        }
        // Attempt to delete the directory
        try {
            FileUtils.deleteDirectory(allureResultsDir);
            System.out.println("Allure Report Cleaned Successfully00000");
            logger.info("Successfully cleaned Allure results directory: {}", allureResultsDir.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Failed to clean Allure results directory: {}", allureResultsDir.getAbsolutePath(), e);
        }
    }


}