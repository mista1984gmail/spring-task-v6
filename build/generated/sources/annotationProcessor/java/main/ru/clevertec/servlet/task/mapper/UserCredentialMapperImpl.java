package ru.clevertec.servlet.task.mapper;

import javax.annotation.processing.Generated;
import ru.clevertec.servlet.task.security.entity.UserCredential;
import ru.clevertec.servlet.task.security.entity.dto.UserCredentialDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-05T19:26:43+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.jar, environment: Java 17.0.8 (Oracle Corporation)"
)
public class UserCredentialMapperImpl implements UserCredentialMapper {

    @Override
    public UserCredential userDtoToUser(UserCredentialDto userCredentialDto) {
        if ( userCredentialDto == null ) {
            return null;
        }

        UserCredential.UserCredentialBuilder userCredential = UserCredential.builder();

        userCredential.email( userCredentialDto.getEmail() );
        userCredential.password( userCredentialDto.getPassword() );

        return userCredential.build();
    }

    @Override
    public UserCredentialDto userToUserDto(UserCredential userCredential) {
        if ( userCredential == null ) {
            return null;
        }

        UserCredentialDto userCredentialDto = new UserCredentialDto();

        userCredentialDto.setEmail( userCredential.getEmail() );
        userCredentialDto.setPassword( userCredential.getPassword() );

        return userCredentialDto;
    }
}
