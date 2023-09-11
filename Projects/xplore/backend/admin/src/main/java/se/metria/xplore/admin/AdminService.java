package se.metria.xplore.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    Logger logger = LoggerFactory.getLogger(AdminService.class);

    private AdminApplicationProperties properties;

    public AdminService(AdminApplicationProperties properties) {
        this.properties = properties;
    }
}
