package com.mogikanen9.file.rest.fileapiclientwebflux;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FileApiClient {

    private String downloadUrl;

    public FileApiClient(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public void downloadFile(final String fileNameToDownload, final Path destFile) throws FileNotFoundException {
        WebClient client = WebClient.create(downloadUrl);
        Flux<DataBuffer> resp = client.get().uri("/{fileNameToDownload}", fileNameToDownload).accept(MediaType.APPLICATION_OCTET_STREAM).retrieve()
        .bodyToFlux(DataBuffer.class);
        Mono<Void> writeOperation = DataBufferUtils.write(resp, new FileOutputStream(destFile.toFile()))
        .map(DataBufferUtils::release)
        .then();
        //writeOperation.block()
    }
}