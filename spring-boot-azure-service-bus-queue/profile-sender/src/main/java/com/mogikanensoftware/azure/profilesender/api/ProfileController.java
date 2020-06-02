
package com.mogikanensoftware.azure.profilesender.api;

import com.mogikanensoftware.azure.profilesender.service.ProfileProducer;
import com.mogikanensoftware.azure.profilesender.model.Profile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/profile")
@Slf4j
public class ProfileController {

    private ProfileProducer profileProducer;

    @PostMapping("/send")
    public ResponseEntity <Profile> send(@RequestBody Profile profile) {

        try {
            this.profileProducer.produce(profile);
            return new ResponseEntity <>(profile, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity <>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ProfileController(ProfileProducer profileProducer) {
        this.profileProducer = profileProducer;
    }
}