package com.lpa.rfb.repository;

import com.lpa.rfb.domain.RfbUser;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.Optional;


/**
 * Spring Data JPA repository for the RfbUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RfbUserRepository extends JpaRepository<RfbUser, Long> {
    RfbUser findOneByUserId(Long id);
}
