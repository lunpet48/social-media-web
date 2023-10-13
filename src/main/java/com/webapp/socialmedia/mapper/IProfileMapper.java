package com.webapp.socialmedia.mapper;

import com.webapp.socialmedia.dto.requests.ProfileRequest;
import com.webapp.socialmedia.dto.responses.ProfileResponse;
import com.webapp.socialmedia.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IProfileMapper {
    IProfileMapper INSTANCE = Mappers.getMapper( IProfileMapper.class );
    Profile ProfileRequestToProfile(ProfileRequest profileRequest);

    ProfileResponse ProfileToProfileResponse(Profile profile);
}
