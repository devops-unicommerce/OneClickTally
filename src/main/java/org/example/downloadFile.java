package org.example;

import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class downloadFile {
    private static final Logger logger = Logger.getLogger(downloadFile.class);
    public downloadFile(String url, String fileName) throws IOException {
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(fileName + ".csv");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        logger.info("download completed, File name is : " + fileName + ".csv");
    }

}
