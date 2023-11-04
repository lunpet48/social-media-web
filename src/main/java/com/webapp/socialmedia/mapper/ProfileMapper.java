package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper( ProfileMapper.class );

//    Profile ProfileRequestToProfile(ProfileRequest profileRequest);

    ProfileResponse ProfileToProfileResponse(Profile profile);
}
