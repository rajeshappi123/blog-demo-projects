
package com.mogikanensoftware.azure.accountreceiver.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Branch {
 
    @Column(name = "BRANCH_NAME")
    private String name;

    @Column(name = "BRANCH_CODE")
    private String code;

    @Column(name = "BRANCH_LOCATION")
    private String location;
}