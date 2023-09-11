package se.metria.markkoll.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.modelmapper.ModelMapper;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.entity.admin.RoleEntity;
import se.metria.markkoll.entity.admin.UserEntity;
import se.metria.markkoll.openapi.model.MarkkollUserDto;
import se.metria.markkoll.openapi.model.RoleTypeDto;
import se.metria.markkoll.openapi.model.UserInfoDto;
import se.metria.markkoll.repository.admin.KundRepository;
import se.metria.markkoll.repository.admin.UserRepository;
import se.metria.markkoll.security.MkPermission;
import se.metria.markkoll.util.CollectionUtil;
import se.metria.markkoll.util.RoleUtil;
import se.metria.xplore.geoserveradmin.service.GeoserverAdminService;
import se.metria.xplore.keycloak.service.KeyCloakService;

import javax.persistence.EntityExistsException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.testdata.TestData.mockUUID;

class UserServiceTest {
    UserService userService;

    AclService mockAclService;
    GeoserverAdminService mockGeoserverAdminService;
    KeyCloakService mockKeyCloakService;
    KundRepository mockKundRepository;
    KundService mockKundService;
    ModelMapper mockModelMapper;
    RoleAclService mockRoleAclService;
    UserRepository mockUserRepository;

    @BeforeEach
    void beforeEach() {
        mockAclService = mock(AclService.class);
        mockGeoserverAdminService = mock(GeoserverAdminService.class);
        mockKeyCloakService = mock(KeyCloakService.class);
        mockKundRepository = mock(KundRepository.class);
        mockKundService = mock(KundService.class);
        mockModelMapper = mock(ModelMapper.class);
        mockRoleAclService = mock(RoleAclService.class);
        mockUserRepository = mock(UserRepository.class);

        userService = new UserService(mockAclService, mockGeoserverAdminService, mockKeyCloakService, mockKundRepository,
            mockKundService, mockModelMapper, mockRoleAclService, mockUserRepository);
    }

    @Test
    void så_ska_det_gå_att_lägga_till_en_ny_användare() throws Exception {
        // Given
        var userInfo = new UserInfoDto()
            .efternamn("efternamn")
            .fornamn("förnamn")
            .email("E.Mail@Mail.se");

        var kundId = "kundId";

        var kundEntity = new KundEntity();
        when(mockKundRepository.findById(eq(kundId))).thenReturn(Optional.of(kundEntity));

        // When
        var userId = userService.create(userInfo, kundId);

        // Then
        assertEquals(userId, userInfo.getEmail().toLowerCase());

        verify(mockAclService).createObject(eq(userId), eq(UserEntity.class),
            eq(kundId), eq(KundEntity.class));

        verify(mockUserRepository).save(eq(new UserEntity(userId, userInfo.getEfternamn(), userInfo.getEmail(),
            userInfo.getFornamn(), kundEntity)));

        verify(mockKeyCloakService).createUser(eq(userId), eq(userInfo.getFornamn()),
            eq(userInfo.getEfternamn()), any());
        verify(mockKeyCloakService).addRealmRoleToUser(eq(userId), eq("markkoll"));

        verify(mockGeoserverAdminService).createUser(eq("e_mail@mail_se"), any(), eq(true));
        verify(mockGeoserverAdminService).addRoleToUser(eq(kundId), eq("e_mail@mail_se"));
    }

    @Test
    void så_ska_exception_kastas_om_användaren_redan_existerar() throws Exception {
        // Given
        var userId = "e.mail@mail.se";
        var userInfo = new UserInfoDto().email(userId);
        var kundId = "kundId";

        when(mockUserRepository.existsById(eq(userId))).thenReturn(true);

        // When / then
        assertThrows(EntityExistsException.class, () -> userService.create(userInfo, kundId));

        verify(mockUserRepository, never()).save(any());

        verify(mockKeyCloakService, never()).createUser(anyString(), anyString(), anyString(), anyString());
        verify(mockKeyCloakService, never()).addRealmRoleToUser(anyString(), anyString());

        verify(mockGeoserverAdminService, never()).createUser(any(), any(), anyBoolean());
        verify(mockGeoserverAdminService, never()).addRoleToUser(any(), any());
    }

    @Test
    void ska_inte_anropa_externa_tjänster_om_det_är_inaktiverat() throws Exception {
        // Given
        var userInfo = new UserInfoDto()
            .efternamn("efternamn")
            .fornamn("förnamn")
            .email("e.mail@mail.se");

        var kundId = "kundId";

        var kundEntity = new KundEntity();
        when(mockKundRepository.findById(eq(kundId))).thenReturn(Optional.of(kundEntity));

        userService.disableExternalServicesCreate = true;

        // When
        var userId = userService.create(userInfo, kundId);

        // Then
        assertEquals(userId, userInfo.getEmail());

        verify(mockUserRepository).save(eq(new UserEntity(userId, userInfo.getEfternamn(), userInfo.getEmail(),
            userInfo.getFornamn(), kundEntity)));

        verify(mockKeyCloakService, never()).createUser(anyString(), anyString(), anyString(), anyString());
        verify(mockKeyCloakService, never()).addRealmRoleToUser(anyString(), anyString());

        verify(mockGeoserverAdminService, never()).createUser(any(), any(), anyBoolean());
        verify(mockGeoserverAdminService, never()).addRoleToUser(any(), any());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void så_ska_det_gå_att_lägga_till_en_roll_till_en_användare(boolean hasRole) {
        // Given
        var userId = "userId";
        var roleType = RoleTypeDto.PROJEKTADMIN;
        var objectId = "objectId";

        var userEntity = new UserEntity();
        if (hasRole) {
            userEntity.addRole(new RoleEntity(objectId, roleType, userEntity));
        }
        when(mockUserRepository.findById(eq(userId))).thenReturn(Optional.of(userEntity));

        // When
        userService.addRole(userId, roleType, objectId);

        // Then
        assertEquals(1, userEntity.getRoles().size());
        assertTrue(userEntity.getRoles().contains(new RoleEntity(objectId, roleType, userEntity)));

        if (!hasRole) {
            verify(mockRoleAclService).addRole(eq(userId), eq(roleType), eq(objectId));
        }
        else {
            verify(mockRoleAclService, never()).addRole(any(), any(), any());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void så_ska_det_gå_att_ta_bort_en_roll_från_en_användare(boolean hasRole) {
        // Given
        var userId = "userId";
        var roleType = RoleTypeDto.PROJEKTADMIN;
        var objectId = "objectId";

        var userEntity = new UserEntity();
        if (hasRole) {
            userEntity.addRole(new RoleEntity(objectId, roleType, userEntity));
        }
        when(mockUserRepository.findById(eq(userId))).thenReturn(Optional.of(userEntity));

        // When
        userService.removeRole(userId, roleType, objectId);

        // Then
        assertEquals(0, userEntity.getRoles().size());

        if (hasRole) {
            verify(mockRoleAclService).removeRole(eq(userId), eq(roleType), eq(objectId));
        }
        else {
            verify(mockRoleAclService, never()).removeRole(any(), any(), any());
        }
    }

    @Test
    void så_ska_det_gå_att_hämta_en_lista_med_användare_i_ett_projekt() {
        // Given
        var projektId = mockUUID(0);
        var entity1 = new UserEntity();
        entity1.setFornamn("namn1");
        var entity2 = new UserEntity();
        entity2.setFornamn("namn2");

        var dto = new MarkkollUserDto();

        when(mockUserRepository.getUsersForRoles(eq(projektId.toString()), eq(RoleUtil.getProjektRoleTypes())))
            .thenReturn(CollectionUtil.asSet(entity1, entity2));
        when(mockModelMapper.map(any(), eq(MarkkollUserDto.class))).thenReturn(dto);

        // When
        var users = userService.getProjektUsers(projektId);

        // Then
        assertEquals(Arrays.asList(dto, dto), users);
    }

    @Test
    void så_ska_det_gå_att_uppdatera_användaruppgifter() {
        // Given
        var userId = "userId";
        var fornamn = "förnamn";
        var efternamn = "efternamn";
        var email = "email";

        var entity = new UserEntity();
        var updatedEntity = new UserEntity();
        updatedEntity.setFornamn(fornamn);
        updatedEntity.setEfternamn(efternamn);
        updatedEntity.setEmail(email);

        var userInfo = new UserInfoDto()
            .fornamn(fornamn)
            .efternamn(efternamn)
            .email(email);
        var updatedUserExpect = new MarkkollUserDto();


        when(mockUserRepository.getOne(eq(userId))).thenReturn(entity);
        when(mockModelMapper.map(eq(updatedEntity), eq(MarkkollUserDto.class))).thenReturn(updatedUserExpect);

        // When
        var updatedUser = userService.updateUserInfo(userId, userInfo);

        // Then
        assertEquals(updatedUserExpect, updatedUser);
    }

    @Test
    void så_ska_det_gå_att_ta_bort_en_användare() {
        // Given
        var userId = "userId";
        var roles = CollectionUtil.asSet(
            RoleEntity.of("role1", RoleTypeDto.PROJEKTADMIN),
            RoleEntity.of("role2", RoleTypeDto.KUNDADMIN),
            RoleEntity.of("role3", RoleTypeDto.PROJEKTHANDLAGGARE)
        );
        var userEntity = new UserEntity();
        userEntity.setRoles(roles);

        when(mockUserRepository.getOne(eq(userId))).thenReturn(userEntity);

        // When
        userService.delete(userId);

        // Then
        verify(mockUserRepository).delete(eq(userEntity));
        for (var role: roles) {
            verify(mockRoleAclService).removeRole(eq(userId), eq(role.getRoleType()), eq(role.getObjectId()));
        }

        verify(mockKeyCloakService).deleteUserByUsername(eq(userId));
        verify(mockGeoserverAdminService).deleteUser(eq(userId));
    }

    @Test
    void ska_inte_anropa_externa_tjänster_om_det_är_disablat_när_man_tar_bort_en_användare() {
        // Given
        userService.disableExternalServicesDelete = true;
        var userId = "userId";

        when(mockUserRepository.getOne(eq(userId))).thenReturn(new UserEntity());
        // When
        userService.delete(userId);

        // Then
        verify(mockKeyCloakService, never()).deleteUserByUsername(eq(userId));
        verify(mockGeoserverAdminService, never()).deleteUser(eq(userId));
    }

    @Test
    void så_ska_det_gå_att_hämta_alla_användare_med_en_viss_behörighet() {
        // Given
        var objectId = "kundId";
        var objectClazz = KundEntity.class;
        var permission = MkPermission.READ;

        var entries = new HashMap<Sid, Set<Permission>>();
        entries.put(new PrincipalSid("user1"), CollectionUtil.asSet(MkPermission.READ, MkPermission.WRITE));
        entries.put(new PrincipalSid("user2"), CollectionUtil.asSet(MkPermission.WRITE));
        entries.put(new PrincipalSid("user3"), CollectionUtil.asSet(MkPermission.READ));
        entries.put(new GrantedAuthoritySid("group"), CollectionUtil.asSet(MkPermission.READ));

        when(mockAclService.getPermissionsForObject(eq(objectId), eq(objectClazz))).thenReturn(entries);
        when(mockModelMapper.map(any(), eq(MarkkollUserDto.class))).thenReturn(new MarkkollUserDto());
        when(mockUserRepository.findAllById(eq(Arrays.asList("user1", "user3"))))
            .thenReturn(Arrays.asList(new UserEntity(), new UserEntity()));

        // When
        var users = userService.getUsersWithPermission(objectId, objectClazz, permission);

        // Then
        assertEquals(Arrays.asList(new MarkkollUserDto(), new MarkkollUserDto()), users);
    }

    @Test
    void så_ska_det_gå_att_ta_bort_alla_projektroller_för_en_användare() {
        // Given
        var projektId = mockUUID(0);
        var userId = "userId";
        var userEntity = new UserEntity();
        var kundRole = RoleEntity.of("kund", RoleTypeDto.KUNDADMIN);
        userEntity.addRole(kundRole);
        userEntity.addRole(RoleEntity.of(projektId.toString(), RoleTypeDto.PROJEKTADMIN));

        when(mockUserRepository.findById(eq(userId))).thenReturn(Optional.of(userEntity));

        // When
        userService.removeProjektRoles(userId, projektId);

        // Then
        assertEquals(CollectionUtil.asSet(kundRole), userEntity.getRoles());
        verify(mockRoleAclService).removeRole(userId, RoleTypeDto.PROJEKTADMIN, projektId);
    }
}