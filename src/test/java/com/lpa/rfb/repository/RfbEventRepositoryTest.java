package com.lpa.rfb.repository;

import com.lpa.rfb.RfbApp;
import com.lpa.rfb.bootstrap.RfbBootstrap;
import com.lpa.rfb.domain.RfbEvent;
import com.lpa.rfb.domain.RfbLocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RfbApp.class})
public class RfbEventRepositoryTest extends AbstractRepositoryTest {
    @Before
    public void setUp() throws Exception {
        RfbBootstrap rfbBootstrap = new RfbBootstrap(
            rfbLocationRepository, rfbEventRepository,
            rfbEventAttendanceRepository, userRepository, passwordEncoder, authorityRepository, rfbUserRepository);
    }

    @Test
    public void findAllByRfbLocationAndEventDate() throws Exception {
        RfbLocation aleAndTheWitch = rfbLocationRepository.findByLocationName("St Pete - Ale and the Witch");

        assertNotNull(aleAndTheWitch);

        RfbEvent event = rfbEventRepository.findByRfbLocationAndEventDate(aleAndTheWitch, LocalDate.now());

        assertNotNull(event);
    }
}
