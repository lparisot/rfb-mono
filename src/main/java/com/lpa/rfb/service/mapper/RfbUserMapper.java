package com.lpa.rfb.service.mapper;

import com.lpa.rfb.domain.*;
import com.lpa.rfb.service.dto.RfbUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RfbUser and its DTO RfbUserDTO.
 */
@Mapper(componentModel = "spring", uses = {RfbLocationMapper.class, UserMapper.class})
public interface RfbUserMapper extends EntityMapper<RfbUserDTO, RfbUser> {

    @Mapping(source = "homeLocation.id", target = "homeLocationId")
    @Mapping(source = "user.id", target = "userId")
    RfbUserDTO toDto(RfbUser rfbUser); 

    @Mapping(source = "homeLocationId", target = "homeLocation")
    @Mapping(source = "userId", target = "user")
    @Mapping(target = "rfbEventAttendances", ignore = true)
    RfbUser toEntity(RfbUserDTO rfbUserDTO);

    default RfbUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        RfbUser rfbUser = new RfbUser();
        rfbUser.setId(id);
        return rfbUser;
    }
}
