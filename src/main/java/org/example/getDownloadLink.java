package org.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


public class getDownloadLink {

    private String status;
    private String downloadLink;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }


    public getDownloadLink(ReadFile readResouceFile, String jobCode) throws InterruptedException, IOException {
        String xml = getXML(readResouceFile, jobCode);

        File configFile = new File("config.properties");
        FileReader reader = new FileReader(configFile);
        Properties props = new Properties();
        props.load(reader);

        int retry = Integer.parseInt(props.getProperty("retriesCount"));
        int delay = Integer.parseInt(props.getProperty("delay"));


        while (retry > 0) {
            if (callSoapService(readResouceFile, xml)) {
                return;
            }
            retry--;
            TimeUnit.SECONDS.sleep(delay);

        }
        System.out.println("Time out Error, The export is taking longer than expeted");
        this.setStatus("FAILED");
    }


    boolean callSoapService(ReadFile readResouceFile, String soapRequest) {
        try {
            String url = readResouceFile.getUrl();
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(soapRequest);
            wr.flush();
            wr.close();
            String responseStatus = con.getResponseMessage();
            System.out.println(responseStatus);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String finalvalue = response.toString();
            String responseState = finalvalue.substring(finalvalue.indexOf("<Status>") + 8, finalvalue.indexOf("</Status>"));


            if (responseState.equals("COMPLETE")) {
                String responseDownloadLink = finalvalue.substring(finalvalue.indexOf("<FilePath>") + 10, finalvalue.indexOf("</FilePath>"));
                this.setDownloadLink(responseDownloadLink);
                this.setStatus("COMPLETE");
                System.out.println("File is ready to download");
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            System.out.println("Exception occured while executing the script " + e.getMessage());
            this.setStatus("FAILED");
            return true;

        }
    }

    static String getXML(ReadFile readResouceFile, String jobCode) {

        String xml = "<soapenv:Envelope xmlns:ser=\"http://uniware.unicommerce.com/services/\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" +
                "<soapenv:Header>\r\n" +
                "<wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">\r\n" +
                "<wsse:UsernameToken wsu:Id=\"UsernameToken-ECD69ABA834E9A5C8816813856284734\">\r\n" +
                "<wsse:Username>" + readResouceFile.getUserName() + "</wsse:Username>\r\n" +
                "<wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">" + readResouceFile.getApiKey() + "</wsse:Password>\r\n" +
                "<wsse:Nonce EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\">" + "lI8oZMrGh/eJpQvZ6VXAbA==" + "</wsse:Nonce>\r\n" +
                "</wsse:UsernameToken>\r\n" +
                "</wsse:Security>\r\n" +
                "</soapenv:Header>\r\n" +
                "<soapenv:Body>\r\n" +
                "<ser:GetExportJobStatusRequest>\r\n" +
                "<ser:JobCode>" + jobCode + "</ser:JobCode>\r\n" +
                "</ser:GetExportJobStatusRequest>\r\n" +
                "</soapenv:Body>\r\n" +
                "</soapenv:Envelope>";

        return xml;

    }

}
