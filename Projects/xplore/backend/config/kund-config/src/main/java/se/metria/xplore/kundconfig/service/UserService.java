package se.metria.xplore.kundconfig.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.metria.xplore.keycloak.service.KeyCloakService;
import se.metria.xplore.kundconfig.entity.PermissionsEntity;
import se.metria.xplore.kundconfig.entity.UserEntity;
import se.metria.xplore.kundconfig.openapi.model.PermissionsDto;
import se.metria.xplore.kundconfig.openapi.model.XpUserDto;
import se.metria.xplore.kundconfig.openapi.model.XpUserInfoDto;
import se.metria.xplore.kundconfig.repository.KundConfigRepository;
import se.metria.xplore.kundconfig.repository.PermissionsRepository;
import se.metria.xplore.kundconfig.repository.UserRepository;
import se.metria.xplore.kundconfig.util.CollectionUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @NonNull
    private final UserRepository userRepository;

    @NonNull
    private final PermissionsRepository permissionsRepository;

    @NonNull
    private final KeyCloakService keyCloakService;

    @NonNull
    private final KundConfigRepository kundConfigRepository;

    @NonNull
    private final ModelMapper modelMapper;

    @Transactional
    public XpUserDto createUser(XpUserInfoDto xpUserInfoDto){
        var entity = modelMapper.map(xpUserInfoDto, UserEntity.class);
        entity.setId(xpUserInfoDto.getEmail());
        entity.setPermissions(null);
        var user = userRepository.save(entity);

        keyCloakService.createUser(xpUserInfoDto.getEmail(), xpUserInfoDto.getFirstName(), xpUserInfoDto.getLastName(), "Ettlösenordlångtöver16tecken");

        for (PermissionsDto permissions: xpUserInfoDto.getPermissions()) {
            var kund = kundConfigRepository.getOne(permissions.getKundId());
            var permissionsEntity = new PermissionsEntity();
            permissionsEntity.setProdukt(permissions.getProdukt().toString());
            permissionsEntity.setRoll(permissions.getRoll().toString());
            permissionsEntity.setUser(user);
            permissionsEntity.setKund(kund);
            permissionsRepository.save(permissionsEntity);

            keyCloakService.addRealmRoleToUser(xpUserInfoDto.getEmail(), permissionsEntity.getKund().getId());
        }

        log.info("Lagt till användare {}", user.getId());

        return modelMapper.map(userRepository.getFirstById(user.getId()), XpUserDto.class);
    }

    public List<XpUserDto> getAllUsers(){
        var entities = userRepository.findAll();
        return CollectionUtil.modelMapperList(entities, modelMapper, XpUserDto.class);
    }

    @Transactional
    public void deleteUser(String id){
        userRepository.deleteById(id);
        KeyCloakService.UserRepresentation[] users = keyCloakService.findUser(id);
        this.keyCloakService.deleteUser(users[0].getId());
        log.info("Tagit bort användare {}", id);
    }

    public List<XpUserDto>getAllUsersForKund(String kundId){
        var users = userRepository.getUsersForKund(kundId);
        return CollectionUtil.modelMapperList(users, modelMapper, XpUserDto.class);
    }

    public XpUserDto getUser(String id) {
        var user = userRepository.getFirstById(id);
        log.info("Hämtat användare {}", user.getId());
        return modelMapper.map(user, XpUserDto.class);
    }
}
