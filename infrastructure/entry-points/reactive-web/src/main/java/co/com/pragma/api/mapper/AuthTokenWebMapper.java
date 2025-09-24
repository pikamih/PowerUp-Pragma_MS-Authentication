package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.response.AuthTokenResponseDto;
import co.com.pragma.model.authtoken.AuthToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthTokenWebMapper {

    AuthTokenResponseDto toResponse(AuthToken authToken);

}
