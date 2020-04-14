
package com.poc.mapqueuelock.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Request",
        indexes =  @Index( name = "idx_ref_number",columnList = "RefNumber",unique = true) ) 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Request implements Serializable {

    private static final long serialVersionUID = -1601394364183233683L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;    

    @Column(name = "FirstName", nullable = false, length = 255)
    private String firstName;

    @Column(name = "LastName", nullable = false, length = 255)
    private String lastName;

    @Column(name = "Email", nullable = false, length = 255)
    private String email;

    @Column(name = "RefNumber", nullable = false, length = 50)
    private String refNumber;

    @Column(name = "RequestDate", nullable = false)
    private Date requestDate;

}
