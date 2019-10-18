package com.mogikanen9.rest.api;

import java.io.IOException;
import java.nio.file.Paths;


public class App {
    public static void main(String[] args) throws IOException, InterruptedException
    {

        FileApiClient client = new FileApiClient("http://localhost:8080/file");

        System.out.println("Downloading file ...");        
        client.downloadFile("eag_summary_report_by_period.rptdesign", Paths.get("c:/temp/body.xml"));
        System.out.println("Done.");
      
    }
}
