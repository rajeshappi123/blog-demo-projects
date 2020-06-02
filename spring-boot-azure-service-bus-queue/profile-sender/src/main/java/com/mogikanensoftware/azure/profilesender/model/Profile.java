
package com.mogikanensoftware.azure.profilesender.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    private String id;
    private String firstName;
    private String lastName;
    private String email;

}