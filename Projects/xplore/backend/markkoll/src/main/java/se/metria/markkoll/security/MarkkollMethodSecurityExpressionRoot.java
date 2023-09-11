package se.metria.markkoll.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import se.metria.markkoll.entity.DokumentmallEntity;
import se.metria.markkoll.entity.FilEntity;
import se.metria.markkoll.entity.ImportVersionEntity;
import se.metria.markkoll.entity.InfobrevsjobbEntity;
import se.metria.markkoll.entity.admin.KundEntity;
import se.metria.markkoll.entity.admin.UserEntity;
import se.metria.markkoll.entity.avtal.AvtalEntity;
import se.metria.markkoll.entity.avtal.AvtalsjobbEntity;
import se.metria.markkoll.entity.markagare.AvtalspartEntity;
import se.metria.markkoll.entity.projekt.ProjektEntity;
import se.metria.markkoll.entity.vardering.bilaga.BilagaEntity;
import se.metria.markkoll.entity.vardering.elnat.varderingsprotokoll.ElnatVarderingsprotokollEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberIntrangAkerOchSkogsmarkEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberMarkledningEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberOvrigIntrangsersattningEntity;
import se.metria.markkoll.entity.vardering.fiber.FiberPunktersattningEntity;
import se.metria.markkoll.entity.vardering.fiber.varderingsprotokoll.FiberVarderingsprotokollEntity;

public class MarkkollMethodSecurityExpressionRoot
    extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;

    public MarkkollMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean hasAvtalPermission(Object avtalId, Object permission) {
        return hasPermission(avtalId, AvtalEntity.class, permission);
    }

    public boolean hasAvtalsjobbPermission(Object avtalsjobbId, Object permission) {
        return hasPermission(avtalsjobbId, AvtalsjobbEntity.class, permission);
    }



    public boolean hasElnatVpPermission(Object elnatVPId, Object permission) {
        return hasPermission(elnatVPId, ElnatVarderingsprotokollEntity.class, permission);
    }

    public boolean hasElnatBilagaPermission(Object id, Object permission) {
        return hasPermission(id, BilagaEntity.class, permission);
    }



    public boolean hasFiberVpPermission(Object fiberVPId, Object permission) {
        return hasPermission(fiberVPId, FiberVarderingsprotokollEntity.class, permission);
    }

    public boolean hasFiberAkerOchSkogsmarkPermission(Object id, Object permission) {
        return hasPermission(id, FiberIntrangAkerOchSkogsmarkEntity.class, permission);
    }

    public boolean hasFiberMarkledningPermission(Object id, Object permission) {
        return hasPermission(id, FiberMarkledningEntity.class, permission);
    }

    public boolean hasFiberOvrigtIntrangPermission(Object id, Object permission) {
        return hasPermission(id, FiberOvrigIntrangsersattningEntity.class, permission);
    }

    public boolean hasFiberPunktersattningPermission(Object id, Object permission) {
        return hasPermission(id, FiberPunktersattningEntity.class, permission);
    }





    public boolean hasInfobrevsjobbPermission(Object infobrevsjobbId, Object permission) {
        return hasPermission(infobrevsjobbId, classToTargetType(InfobrevsjobbEntity.class), permission);
    }

    public boolean hasDokumentPermission(Object dokumentId, Object permission) {
        return hasPermission(dokumentId, DokumentmallEntity.class, permission);
    }

    public boolean hasKundPermission(Object kundId, Object permission) {
        return hasPermission(kundId, KundEntity.class, permission);
    }

    public boolean hasMarkagarePermission(Object markagareId, Object permission) {
        return hasPermission(markagareId, classToTargetType(AvtalspartEntity.class), permission);
    }

    public boolean hasProjektPermission(Object projektId, Object permission) {
        return hasPermission(projektId, ProjektEntity.class, permission);
    }

    public boolean hasFilPermission(Object filId, Object permission) {
        return hasPermission(filId, FilEntity.class, permission);
    }

    public boolean hasVersionPermission(Object versionId, Object permission) {
        return hasPermission(versionId, classToTargetType(ImportVersionEntity.class), permission);
    }

    public boolean hasUserPermission(Object userId, Object permission) {
        return hasPermission(userId, classToTargetType(UserEntity.class), permission);
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    @Override
    public void setFilterObject(Object obj) {
        this.filterObject = obj;
    }

    @Override
    public void setReturnObject(Object obj) {
        this.returnObject = obj;
    }

    private boolean hasPermission(Object objectId, Class<?> clazz, Object permission) {
        return hasPermission(objectId, classToTargetType(clazz), permission);
    }

    private String classToTargetType(Class<?> clazz) {
        // toString() prefixar klassnamnet med "class ", vilket vi inte vill ha
        return clazz.toString().substring(6);
    }
}
