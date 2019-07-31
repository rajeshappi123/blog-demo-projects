package com.github.mogikanen9.devtest.writer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.github.mogikanen9.devtest.domain.Book;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class RestWriter implements Writer {

    private String apiBaseUrl;
    private String apiUname;
    private String apiPwd;

    @Override
    public void write(Book book) throws WriterException {
        if (log.isDebugEnabled()) {
            log.debug(String.format("RestWriter: writing book->%s", book));
        }

        this.execSaveBook(book, apiBaseUrl, "/book");

    }

    protected void execSaveBook(Book book, String apiBaseUrl, String bookPath) throws WriterException{
        try {
            
            HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(apiUname, apiPwd);

            Client client = ClientBuilder.newClient();
            client.register(feature);
            WebTarget webTarget = client.target(apiBaseUrl);
            WebTarget bookTarget = webTarget.path(bookPath);
            Invocation.Builder invocationBuilder = bookTarget.request(MediaType.APPLICATION_JSON);

            RestBook restBook = RestBook.from(book);

            Response response = invocationBuilder.post(Entity.entity(restBook, MediaType.APPLICATION_JSON));

            if (log.isDebugEnabled()) {
                log.debug(String.format("RestWriter: response from post book->%s", response.toString()));
            }

            
            if(!response.getStatusInfo().equals(Status.CREATED)){
                if(response.getStatusInfo().equals(Status.CONFLICT)){
                    log.debug(String.format("Book with ISBN->%s already exists", book.getIsbn()));
                }else{
                    throw new WriterException(String.format("Book with ISBN->%s was not created", book.getIsbn()));
                }                
            }

        } catch (Exception e) {
            throw new WriterException(e.getMessage(), e);
        }
    }

}