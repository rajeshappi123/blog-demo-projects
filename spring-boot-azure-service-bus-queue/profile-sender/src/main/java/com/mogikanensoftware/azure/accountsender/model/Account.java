
package com.mogikanensoftware.azure.accountsender.model;

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
    private LocalDate openDate;

    private String bankName;
    private Branch branch;
    private String profileId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Branch {
        private String name;
        private String code;
        private String location;
    }

}