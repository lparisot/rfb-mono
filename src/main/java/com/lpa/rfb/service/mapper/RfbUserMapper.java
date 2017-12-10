package com.lpa.rfb.service.mapper;

import com.lpa.rfb.domain.*;
import com.lpa.rfb.service.dto.RfbUserDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RfbUser and its DTO RfbUserDTO.
 */
@Mapper(componentModel = "spring", uses = {RfbLocationMapper.class, UserMapper.class})
public interface RfbUserMapper extends EntityMapper<RfbUserDTO, RfbUser> {

    @Mapping(source = "homeLocation", target = "rfbLocationDTO")
    @Mapping(source = "user", target = "userDTO")
    RfbUserDTO toDto(RfbUser rfbUser);

    @Mapping(source = "rfbLocationDTO", target = "homeLocation")
    @Mapping(source = "userDTO", target = "user")
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
