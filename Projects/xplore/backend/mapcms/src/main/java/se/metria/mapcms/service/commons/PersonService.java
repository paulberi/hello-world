package se.metria.mapcms.service.commons;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.mapcms.commons.utils.LoggedInUser;
import se.metria.mapcms.entity.DialogPartEntity;
import se.metria.mapcms.entity.PersonEntity;
import se.metria.mapcms.repository.DialogPartRepository;
import se.metria.mapcms.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static se.metria.mapcms.commons.utils.DialogUtils.createPersonFromAccessToken;

@RequiredArgsConstructor
@Service
@Transactional
public class PersonService {
    @NonNull
    private final PersonRepository personRepository;

    @NonNull
    private final LoggedInUser loggedInUser;

    @NonNull
    private final DialogPartRepository dialogPartRepository;

    public void addDialogpartToActiveUser(UUID dialogpartId) {
        var token = loggedInUser.ActiveUser();

        var dialogPart = dialogPartRepository.getReferenceById(dialogpartId);

        Optional<PersonEntity> personFromDB=personRepository.findById(token.getPreferredUsername());
        PersonEntity person=new PersonEntity();
        if(personFromDB.isPresent()){
            person=personFromDB.get();
            person.getDialogParter().add(dialogPart);
        }else{
            person=createPersonFromAccessToken(token);
            List<DialogPartEntity> dialogParter=new ArrayList<>();
            dialogParter.add(dialogPart);
            person.setDialogParter(dialogParter);
        }

        personRepository.save(person);
    }
}
