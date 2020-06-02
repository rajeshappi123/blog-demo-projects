
package com.mogikanensoftware.azure.accountreceiver.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NUMBER", nullable = false)
    private String number;

    @Column(name = "OPEN_DATE", columnDefinition = "DATE", nullable = true)
    private LocalDate openDate;

    @Column(name = "BANK_NAME", nullable = false)
    private String bankName;
    
    @Embedded
    private Branch branch;
}