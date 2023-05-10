package org.example;

import java.io.IOException;
import java.io.IOException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);
    private static final long serialVersionUID = 1L;
    public Main() throws IOException, InterruptedException {

        BasicConfigurator.configure();

        logger.info("Script Started");

        ReadFile readResouceFile = new ReadFile("Appconfig.ini");
        if (readResouceFile.getExportName() == null) {
            System.out.println("Req_type is invalid, stopping the export");
            return;
        }

        JobCode jobCode = new JobCode(readResouceFile);

        if (jobCode == null) {
            logger.info("Error occured while fetching the job code, stopping the export");
            return;
        }


        getDownloadLink downloadLink = new getDownloadLink(readResouceFile, jobCode.getJobCodeValue());

        if (downloadLink.getStatus() != "COMPLETE") {
            logger.info("Error occured while downloading the file, stopping the export");
            return;
        }

        downloadFile dw = new downloadFile(downloadLink.getDownloadLink(), readResouceFile.getFile_Name());
    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        new Main();
    }
}