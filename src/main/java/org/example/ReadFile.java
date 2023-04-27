package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.io.File;
import java.io.FileReader;

import org.ini4j.Ini;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class ReadFile {
    private static final Logger logger = Logger.getLogger(ReadFile.class);

    private String url;
    private String userName;
    private String password;
    private String ApiKey;
    private String Req_type;
    private String Start_Date;
    private String End_Date;

    private String exportName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiKey() {
        return ApiKey;
    }

    public void setApiKey(String apiKey) {
        ApiKey = apiKey;
    }

    public String getReq_type() {
        return Req_type;
    }

    public void setReq_type(String req_type) {
        Req_type = req_type;
    }

    public String getStart_Date() {
        return Start_Date;
    }

    public void setStart_Date(String start_Date) {
        Start_Date = start_Date;
    }

    public String getEnd_Date() {
        return End_Date;
    }

    public void setEnd_Date(String end_Date) {
        End_Date = end_Date;
    }

    public String getExportName() {
        return exportName;
    }

    public void setExportName(String exportName) {
        this.exportName = exportName;
    }


    public ReadFile(String fileName) throws IOException {

        Ini ini = new Ini(new File(fileName));
        String reqType;
        String exportType;

        this.setUrl(ini.get("API", "URL", String.class));
        this.setUserName(ini.get("API", "UserName", String.class));
        this.setApiKey(ini.get("API", "ApiKey", String.class));
        this.setPassword(ini.get("API", "Password", String.class));
        this.setStart_Date(ini.get("API", "Start_Date", String.class));
        this.setEnd_Date(ini.get("API", "End_Date", String.class));

        reqType = ini.get("API", "Req_type", String.class);
        exportType = getExportNameFromReqType(reqType);

        if (exportType == null) {
            logger.info("Req_type value " + reqType + " is invalid");
        } else {
            this.setReq_type(reqType);
            this.setExportName(exportType);

            logger.info("ExportJob Name is" + exportType);
        }

    }

    static String getExportNameFromReqType(String reqType) throws IOException {

        File configFile = new File("config.properties");
        FileReader reader = new FileReader(configFile);
        Properties props = new Properties();
        props.load(reader);

        String exportType = props.getProperty(reqType);


        return exportType;
    }
}