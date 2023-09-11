package se.metria.xplore.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import se.metria.xplore.configuration.model.ClientId;
import se.metria.xplore.configuration.model.LoginInfo;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class AppLoginInfoService {
    Logger logger = LoggerFactory.getLogger(AppLoginInfoService.class);

    private EntityManager entityManager;

    public AppLoginInfoService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional()
    public LoginInfo getLoginInfo(ClientId clientId) {
        return entityManager.find(LoginInfo.class, clientId);
    }
}
