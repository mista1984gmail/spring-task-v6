package ru.clevertec.servlet.task.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import ru.clevertec.servlet.task.entity.dto.ClientDto;
import ru.clevertec.servlet.task.entity.model.Client;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-01-05T19:26:43+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.jar, environment: Java 17.0.8 (Oracle Corporation)"
)
public class ClientMapperImpl implements ClientMapper {

    @Override
    public Client clientDtoToClient(ClientDto clientDto) {
        if ( clientDto == null ) {
            return null;
        }

        Client.ClientBuilder client = Client.builder();

        client.firstName( clientDto.getFirstName() );
        client.lastName( clientDto.getLastName() );
        client.email( clientDto.getEmail() );
        client.telephone( clientDto.getTelephone() );
        client.birthday( clientDto.getBirthday() );
        client.registrationDate( clientDto.getRegistrationDate() );

        return client.build();
    }

    @Override
    public ClientDto clientToClientDto(Client client) {
        if ( client == null ) {
            return null;
        }

        ClientDto clientDto = new ClientDto();

        clientDto.setFirstName( client.getFirstName() );
        clientDto.setLastName( client.getLastName() );
        clientDto.setEmail( client.getEmail() );
        clientDto.setTelephone( client.getTelephone() );
        clientDto.setBirthday( client.getBirthday() );
        clientDto.setRegistrationDate( client.getRegistrationDate() );

        return clientDto;
    }

    @Override
    public List<ClientDto> clientsToClientsDto(List<Client> clients) {
        if ( clients == null ) {
            return null;
        }

        List<ClientDto> list = new ArrayList<ClientDto>( clients.size() );
        for ( Client client : clients ) {
            list.add( clientToClientDto( client ) );
        }

        return list;
    }
}
