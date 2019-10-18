package com.mogikanen9.rest.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Path;

public class FileApiClient {

    private String apiUrl;

    public FileApiClient(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public void downloadFile(String fileNameToDownload, Path destFile) throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(this.apiUrl + "/download/" + fileNameToDownload))
                .build();

        HttpResponse<Path> response = client.send(request, BodyHandlers.ofFile(destFile));

        System.out.println(
                String.format("Response in file->%s, responseCode->%d", response.body(), response.statusCode()));
    }

}