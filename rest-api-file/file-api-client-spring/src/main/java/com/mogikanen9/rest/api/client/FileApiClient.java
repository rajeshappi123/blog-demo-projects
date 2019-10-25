package com.mogikanen9.rest.api.client;

import java.io.File;
import java.io.FileOutputStream;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

public class FileApiClient{

    private String downloadUrl;
    private String uploadUrl;
    
    public FileApiClient(String downloadUrl, String uploadUrl) {
        this.downloadUrl = downloadUrl;
        this.uploadUrl = uploadUrl;
    }

    public void downloadFile(final String fileNameToDownload,final File destFile){
        RestTemplate client = new RestTemplate();
        client.execute(this.downloadUrl+"/"+fileNameToDownload, HttpMethod.GET, null, clientHttpResponse ->{            
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(destFile));
            return destFile;
        });
    }

    public void uploadFile(final File source){
        
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", this.geFileResource(source));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        
        RestTemplate client = new RestTemplate();
        ResponseEntity<String> res = client.exchange(this.uploadUrl, HttpMethod.POST, requestEntity, String.class);
        System.out.println(String.format("resp code value->%d", res.getStatusCodeValue()));
        System.out.println(String.format("resp->%s", res.getBody()));
    }
 
    protected Resource geFileResource(final File source){
        return new FileSystemResource(source);
    }
}