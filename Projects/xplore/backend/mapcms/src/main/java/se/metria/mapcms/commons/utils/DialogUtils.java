package se.metria.mapcms.commons.utils;

import org.keycloak.representations.AccessToken;
import se.metria.mapcms.entity.PersonEntity;

import java.time.LocalDateTime;

public class DialogUtils {


    public static PersonEntity createPersonFromAccessToken(AccessToken token){
        PersonEntity person= new PersonEntity();
        person.setNamn(token.getGivenName()+" "+token.getFamilyName());
        person.setSenastInloggad(LocalDateTime.now());
        person.setPnr(token.getPreferredUsername());
        return person;
    }
}
