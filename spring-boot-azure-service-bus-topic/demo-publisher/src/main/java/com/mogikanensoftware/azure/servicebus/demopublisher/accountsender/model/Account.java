
package com.mogikanensoftware.azure.servicebus.demopublisher.accountsender.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    private String number;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate created;

    private String name;
    private AccountType type;
    private String lhin;

    enum AccountType {
        Independent_Practice, Virtual_Care_Clinic
    }

}