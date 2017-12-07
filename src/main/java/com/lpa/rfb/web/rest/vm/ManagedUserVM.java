package com.lpa.rfb.web.rest.vm;

import com.lpa.rfb.domain.RfbEventAttendance;
import com.lpa.rfb.domain.RfbLocation;
import com.lpa.rfb.service.dto.UserDTO;
import javax.validation.constraints.Size;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 * See http://www.jhipster.tech/tips/022_tip_registering_user_with_additional_information.html
 */
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    private Long homeLocation;

    private Set<RfbEventAttendance> rfbEventAttendances = new HashSet<>();

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public ManagedUserVM(Long id, String login, String password, String firstName, String lastName,
                         String email, boolean activated, String imageUrl, String langKey,
                         String createdBy, Instant createdDate, String lastModifiedBy, Instant lastModifiedDate,
                         Long homeLocation, Set<RfbEventAttendance> eventAttendances, Set<String> authorities) {

        super(id, login, firstName, lastName, email, activated, imageUrl, langKey,
            createdBy, createdDate, lastModifiedBy, lastModifiedDate, authorities);
        this.password = password;
        this.homeLocation = homeLocation;
        this.rfbEventAttendances = eventAttendances;
    }

    public String getPassword() {
        return password;
    }

    public Long getHomeLocation() {
        return homeLocation;
    }

    public Set<RfbEventAttendance> getRfbEventAttendances() {
        return rfbEventAttendances;
    }

    @Override
    public String toString() {
        return "ManagedUserVM{" +
            "} " + super.toString();
    }
}
