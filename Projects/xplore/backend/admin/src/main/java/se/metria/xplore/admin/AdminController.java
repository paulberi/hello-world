package se.metria.xplore.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api")
@RestController
@CrossOrigin(origins = "*")
public class AdminController {
    Logger logger = LoggerFactory.getLogger(AdminController.class);

    private AdminApplicationProperties adminProperties;
    private AdminService adminService;

    public AdminController(AdminApplicationProperties adminProperties, AdminService adminService) {
        this.adminProperties = adminProperties;
        this.adminService = adminService;
    }
}
