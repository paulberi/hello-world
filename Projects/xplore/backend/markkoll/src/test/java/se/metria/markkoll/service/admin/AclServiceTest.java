package se.metria.markkoll.service.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.security.MkPermission;
import se.metria.markkoll.util.CollectionUtil;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static se.metria.markkoll.security.MkPermission.*;

class AclServiceTest {
    AclService aclService;

    MutableAclService mockMutableAclService;

    @BeforeEach
    void beforeEach() {
        mockMutableAclService = mock(MutableAclService.class);

        aclService = new AclService(mockMutableAclService);
    }

    @Test
    void så_ska_det_gå_att_lägga_till_behörigheter() {
        // Given
        var sid = new PrincipalSid("SID");
        var entries = Arrays.asList(
            AclEntry.of("id1", ProjektEntity.class, READ),
            AclEntry.of("id1", KundEntity.class, MkPermission.WRITE, MkPermission.CREATE)
        );
        var mockAclProjekt = setupMockAclInsert(entries.get(0));
        var mockAclKund = setupMockAclInsert(entries.get(1));

        // When
        aclService.addAclEntries(sid, entries);

        // Then
        verify(mockAclProjekt).insertAce(eq(0), eq(READ), eq(sid), eq(true));
        verify(mockMutableAclService).updateAcl(eq(mockAclProjekt));

        verify(mockAclKund).insertAce(eq(0), eq(MkPermission.WRITE), eq(sid), eq(true));
        verify(mockAclKund).insertAce(eq(0), eq(MkPermission.CREATE), eq(sid), eq(true));
        verify(mockMutableAclService).updateAcl(eq(mockAclKund));
    }

    @Test
    void ska_inte_lägga_till_behörigheter_om_behörigheter_är_null() {
        // Given
        var sid = new PrincipalSid("SID");

        // When
        aclService.addAclEntries(sid, null);

        // Then
        verify(mockMutableAclService, never()).updateAcl(any());
    }

    @Test
    void ska_inte_lägga_till_behörigheter_om_inga_behörigheter_anges() {
        // Given
        var sid = new PrincipalSid("SID");

        // When
        aclService.addAclEntries(sid, new ArrayList<>());

        // Then
        verify(mockMutableAclService, never()).updateAcl(any());
    }

    @Test
    void så_ska_det_gå_att_ta_bort_behörigheter() {
        // Given
        var sid = new PrincipalSid("SID");
        var entries = Arrays.asList(
            AclEntry.of("id1", ProjektEntity.class, READ),
            AclEntry.of("id1", KundEntity.class, MkPermission.WRITE, MkPermission.CREATE)
        );
        var mockAclProjekt = setupMockAclDelete(sid, entries.get(0));
        var mockAclKund = setupMockAclDelete(sid, entries.get(1));

        // When
        aclService.removeAclEntries(sid, entries);

        // Then
        verify(mockAclProjekt).deleteAce(0);
        verify(mockMutableAclService).updateAcl(eq(mockAclProjekt));

        verify(mockAclKund).deleteAce(0);
        verify(mockAclKund).deleteAce(1);
        verify(mockMutableAclService).updateAcl(eq(mockAclKund));
    }

    @Test
    void ska_inte_ta_bort_behörigheter_om_behörigheter_är_null() {
        // Given
        var sid = new PrincipalSid("SID");

        // When
        aclService.removeAclEntries(sid, null);

        // Then
        verify(mockMutableAclService, never()).updateAcl(any());
    }

    @Test
    void ska_inte_ta_bort_behörigheter_om_inga_behörigheter_anges() {
        // Given
        var sid = new PrincipalSid("SID");

        // When
        aclService.removeAclEntries(sid, new ArrayList<>());

        // Then
        verify(mockMutableAclService, never()).updateAcl(any());
    }

    @Test
    void så_ska_det_gå_att_skapa_nya_objekt() {
        // Given
        var clazz = ProjektEntity.class;
        var id = "abc";

        // When
        aclService.createObject(id, clazz);

        // Then
        verify(mockMutableAclService).createAcl(new ObjectIdentityImpl(clazz, id));
    }

    @Test
    void så_ska_det_gå_att_skapa_nya_objekt_med_parentobjekt() {
        var objectId = "objectId";
        var objectClass = ProjektEntity.class;
        var parentId = "parentId";
        var parentClass = KundEntity.class;

        var oi = new ObjectIdentityImpl(objectClass, objectId);
        var parentOi = new ObjectIdentityImpl(parentClass, parentId);

        var mockAcl = mock(MutableAcl.class);
        var mockParentAcl = mock(MutableAcl.class);
        when(mockMutableAclService.createAcl(eq(oi))).thenReturn(mockAcl);
        when(mockMutableAclService.readAclById(eq(parentOi))).thenReturn(mockParentAcl);

        // When
        aclService.createObject(objectId, objectClass, parentId, parentClass);

        // Then
        verify(mockMutableAclService).createAcl(eq(oi));
        verify(mockAcl).setParent(eq(mockParentAcl));
        verify(mockMutableAclService).updateAcl(eq(mockAcl));
    }

    @Test
    void så_ska_det_gå_att_hämta_behörigheter_för_ett_objekt() {
        // Given
        var objectId = "objectId";
        var objectClass = Object.class;

        var acl = mock(Acl.class);
        when(mockMutableAclService.readAclById(eq(new ObjectIdentityImpl(objectClass, objectId))))
            .thenReturn(acl);
        when(acl.getEntries()).thenReturn(
            Arrays.asList(
                new AccessControlEntryImpl(objectId, acl, new PrincipalSid("user1"), READ, true, true, true),
                new AccessControlEntryImpl(objectId, acl, new PrincipalSid("user1"), READ, true, true, true),
                new AccessControlEntryImpl(objectId, acl, new PrincipalSid("user1"), WRITE, true, true, true),
                new AccessControlEntryImpl(objectId, acl, new PrincipalSid("user1"), ADMINISTRATION, true, true, true),
                new AccessControlEntryImpl(objectId, acl, new PrincipalSid("user1"), CREATE, false, true, true),
                new AccessControlEntryImpl(objectId, acl, new PrincipalSid("user2"), CREATE, true, true, true)
            )
        );

        // When
        var permissions = aclService.getPermissionsForObject(objectId, objectClass);

        // Then
        var permissionsExpect = new HashMap<Sid, Set<Permission>>();
        permissionsExpect.put(new PrincipalSid("user1"), CollectionUtil.asSet(READ, WRITE, ADMINISTRATION));
        permissionsExpect.put(new PrincipalSid("user2"), CollectionUtil.asSet(CREATE));

        assertEquals(permissionsExpect, permissions);
    }

    @Test
    void så_ska_det_gå_att_hämta_childobjekt_till_ett_objekt() {
        // Given
        var parentObjectId = "parentObjectId";
        var parentObjectClass = KundEntity.class;
        var childClass = ProjektEntity.class;

        when(mockMutableAclService.findChildren(eq(new ObjectIdentityImpl(parentObjectClass, parentObjectId))))
            .thenReturn(Arrays.asList(
                new ObjectIdentityImpl(childClass, "childobjekt"),
                new ObjectIdentityImpl(childClass, "childobjekt2"),
                new ObjectIdentityImpl(AvtalEntity.class, "inteChildobjekt")
            )
        );

        // When
        var children = aclService.getChildObjects(parentObjectId, parentObjectClass, childClass);

        // Then
        var childrenExpect = CollectionUtil.asSet("childobjekt", "childobjekt2");

        assertEquals(childrenExpect, children);
    }

    private MutableAcl setupMockAclDelete(Sid sid, AclEntry aclEntry) {
        var mockAcl = mock(MutableAcl.class);

        List<AccessControlEntry> ace = aclEntry.getPermissions().stream()
            .map(p -> new AccessControlEntryImpl(aclEntry.getObjectId(), mockAcl, sid, p, true, true, true))
            .collect(Collectors.toList());
        when(mockAcl.getEntries()).thenReturn(ace);

        var oi = new ObjectIdentityImpl(aclEntry.getObjectClass(), aclEntry.getObjectId());
        when(mockMutableAclService.readAclById(eq(oi))).thenReturn(mockAcl);

        return mockAcl;
    }

    private MutableAcl setupMockAclInsert(AclEntry aclEntry) {
        var oi = new ObjectIdentityImpl(aclEntry.getObjectClass(), aclEntry.getObjectId());
        var mockAcl = mock(MutableAcl.class);
        when(mockAcl.getEntries()).thenReturn(new ArrayList<>());
        when(mockMutableAclService.readAclById(eq(oi))).thenReturn(mockAcl);

        return mockAcl;
    }
}