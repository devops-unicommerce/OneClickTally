package org.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class downloadFile {
    public downloadFile(String url, String fileName) throws IOException {
        URL website = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(fileName + ".csv");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        System.out.println("download completed, File name is : " + fileName + ".csv");
    }

}
