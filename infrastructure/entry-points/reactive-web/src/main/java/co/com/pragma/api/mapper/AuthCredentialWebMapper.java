package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.request.AuthCredentialRequestDto;
import co.com.pragma.api.dto.response.AuthCredentialResponseDto;
import co.com.pragma.model.authcredential.AuthCredential;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthCredentialWebMapper {


    AuthCredential toDomain(AuthCredentialRequestDto credentialRequestDto);

    @Mapping(target = "authenticated", ignore = true)
    AuthCredentialResponseDto toResponse(AuthCredential authCredential);

}
