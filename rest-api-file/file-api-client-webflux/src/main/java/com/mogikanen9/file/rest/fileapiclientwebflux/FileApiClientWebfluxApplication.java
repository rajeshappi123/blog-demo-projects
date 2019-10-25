package com.mogikanen9.file.rest.fileapiclientwebflux;

import java.nio.file.Paths;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class FileApiClientWebfluxApplication implements CommandLineRunner{

	public static void main(String[] args) {		
		new SpringApplicationBuilder(FileApiClientWebfluxApplication.class).web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
				.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("there...");
		FileApiClient client = new FileApiClient("http://localhost:8080/file/download");
		client.downloadFile("eag_summary_report_by_period.rptdesign", Paths.get("c:/opt/folder23/data.rptdesign"));
		System.out.println("Yup.");
	}

}
