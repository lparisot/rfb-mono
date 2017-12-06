package com.lpa.rfb.bootstrap;

import com.lpa.rfb.domain.*;
import com.lpa.rfb.repository.*;
import com.lpa.rfb.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class RfbBootstrap implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(RfbBootstrap.class);

    private final RfbLocationRepository rfbLocationRepository;
    private final RfbEventRepository rfbEventRepository;
    private final RfbEventAttendanceRepository rfbEventAttendanceRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final RfbUserRepository rfbUserRepository;

    public RfbBootstrap(RfbLocationRepository rfbLocationRepository, RfbEventRepository rfbEventRepository, RfbEventAttendanceRepository rfbEventAttendanceRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, RfbUserRepository rfbUserRepository) {
        this.rfbLocationRepository = rfbLocationRepository;
        this.rfbEventRepository = rfbEventRepository;
        this.rfbEventAttendanceRepository = rfbEventAttendanceRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.rfbUserRepository = rfbUserRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        log.debug("RfbBootstrap");
        // init RFB Locations
        if(rfbLocationRepository.count() == 0){
            //only load data if no data loaded
            initData();
        }
    }

    private void initData() {
        User user = new User();
        user.setFirstName("Johnny");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setLogin("johnny");
        user.setEmail("johnny@runningforbrews.com");
        user.setActivated(true);
        user.addAuthority(authorityRepository.findOne(AuthoritiesConstants.RUNNER));
        user.addAuthority(authorityRepository.findOne(AuthoritiesConstants.ORGANIZER));
        userRepository.save(user);

        RfbLocation aleAndWitch = getRfbLocation("St Pete - Ale and the Witch", DayOfWeek.MONDAY.getValue());

        RfbUser rfbUser = new RfbUser();
        rfbUser.setUser(user);
        rfbUser.setUserName("Johnny");
        rfbUser.setHomeLocation(aleAndWitch);
        rfbUserRepository.save(rfbUser);

        RfbEvent aleEvent = getRfbEvent(aleAndWitch);
        setRfbEventAttendance(rfbUser, aleEvent);

        RfbLocation ratc = getRfbLocation("St Pete - Right Around The Corner", DayOfWeek.TUESDAY.getValue());
        RfbEvent ratcEvent = getRfbEvent(ratc);
        setRfbEventAttendance(rfbUser, ratcEvent);

        RfbLocation stPeteBrew = getRfbLocation("St Pete - St Pete Brewing", DayOfWeek.WEDNESDAY.getValue());
        RfbEvent stPeteBrewEvent = getRfbEvent(stPeteBrew);
        setRfbEventAttendance(rfbUser, stPeteBrewEvent);

        RfbLocation yardOfAle = getRfbLocation("St Pete - Yard of Ale", DayOfWeek.THURSDAY.getValue());
        RfbEvent yardOfAleEvent = getRfbEvent(yardOfAle);
        setRfbEventAttendance(rfbUser, yardOfAleEvent);

        RfbLocation pourHouse = getRfbLocation("Tampa - Pour House", DayOfWeek.MONDAY.getValue());
        RfbLocation macDintons = getRfbLocation("Tampa - Mac Dintons", DayOfWeek.TUESDAY.getValue());
        RfbLocation satRun = getRfbLocation("Saturday Run for testing", DayOfWeek.SATURDAY.getValue());
    }

    private void setRfbEventAttendance(RfbUser rfbUser, RfbEvent rfbEvent) {
        RfbEventAttendance rfbAttendance = new RfbEventAttendance();
        rfbAttendance.setRfbEvent(rfbEvent);
        rfbAttendance.setRfbUser(rfbUser);
        rfbAttendance.setAttendanceDate(LocalDate.now());

        System.out.println(rfbAttendance.toString());

        rfbEventAttendanceRepository.save(rfbAttendance);
        rfbEventRepository.save(rfbEvent);
    }

    private RfbEvent getRfbEvent(RfbLocation rfbLocation) {
        RfbEvent rfbEvent = new RfbEvent();
        rfbEvent.setEventCode(UUID.randomUUID().toString());
        rfbEvent.setEventDate(LocalDate.now()); // will not be on assigned day...
        rfbLocation.addRfbEvent(rfbEvent);
        rfbLocationRepository.save(rfbLocation);
        rfbEventRepository.save(rfbEvent);

        return rfbEvent;
    }

    private RfbLocation getRfbLocation(String locationName, int value) {
        RfbLocation rfbLocation = new RfbLocation();
        rfbLocation.setLocationName(locationName);
        rfbLocation.setRunDayOfWeek(value);
        rfbLocationRepository.save(rfbLocation);

        return rfbLocation;
    }
}
