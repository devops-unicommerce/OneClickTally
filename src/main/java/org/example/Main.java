package org.example;

import java.io.IOException;

public class Main {
    private static final long serialVersionUID = 1L;
    public Main() throws IOException, InterruptedException {

        System.out.println("Script Started");

        ReadFile readResouceFile = new ReadFile("Appconfig.ini");
        if (readResouceFile.getExportName() == null) {
            System.out.println("Req_type is invalid, stopping the export");
            return;
        }

        JobCode jobCode = new JobCode(readResouceFile);

        if (jobCode == null) {
            System.out.println("Error occured while fetching the job code, stopping the export");
            return;
        }


        getDownloadLink downloadLink = new getDownloadLink(readResouceFile, jobCode.getJobCodeValue());

        if (downloadLink.getStatus() != "COMPLETE") {
            System.out.println("Error occured while downloading the file, stopping the export");
            return;
        }

        downloadFile dw = new downloadFile(downloadLink.getDownloadLink(), readResouceFile.getReq_type());
    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        new Main();
    }
}