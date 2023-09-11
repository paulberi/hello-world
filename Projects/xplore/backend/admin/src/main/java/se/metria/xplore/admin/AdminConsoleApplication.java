package se.metria.xplore.admin;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import se.metria.xplore.admin.geoserver.GeoServerException;
import se.metria.xplore.admin.geoserver.GeoServerService;
import se.metria.xplore.admin.keycloak.KeycloakService;
import se.metria.xplore.admin.model.KeycloakCreateUserResponse;
import se.metria.xplore.admin.model.KeycloakRole;
import se.metria.xplore.admin.model.KeycloakUser;
import se.metria.xplore.admin.model.KeycloakUserIssue;
import se.metria.xplore.admin.util.ExcelReader;
import se.metria.xplore.admin.util.WorkbookFactoryWrapper;

import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "admin.console", name = "enabled", havingValue="true")
public class AdminConsoleApplication implements ApplicationRunner {
    private KeycloakService keycloakService;
    private GeoServerService geoserverService;

    public AdminConsoleApplication(KeycloakService keycloakService,
                                   GeoServerService geoserverService) {
        this.keycloakService = keycloakService;
        this.geoserverService = geoserverService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            switch (args.getNonOptionArgs().get(0)) {
                case "keycloak":
                    this.keycloak(args);
                    break;
                case "geoserver":
                    var argsList = new ArrayList<>(args.getNonOptionArgs());
                    consume(argsList);
                    this.geoserver(argsList);
                    break;
                default:
                    throw new Exception("Function " + args.getNonOptionArgs().get(0) + " not recognised.");
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            throw e;
        }
    }

    private void keycloak(ApplicationArguments args) throws Exception {
        switch (args.getNonOptionArgs().get(1)) {
            case "spreadsheet":
                this.keycloakUserSpreadsheet(args);
                break;
            case "list-users":
                this.listKeycloakUsers(args);
                break;
            default:
                throw new Exception("Function " + args.getNonOptionArgs().get(1) + " not recognised.");
        }
    }

    private void listKeycloakUsers(ApplicationArguments args) {
        String realm = args.getNonOptionArgs().get(2);
        Console console = System.console();
        String username = console.readLine("KeyCloak username: ");
        String password = new String(console.readPassword("KeyCloak password: "));
        ArrayList<KeycloakUser> users = this.keycloakService.listUsers(realm, username, password);
        this.printKeycloakUsers(users, false);
    }

    private void keycloakUserSpreadsheet(ApplicationArguments args) throws Exception {
        int skipFirstRows = 0;
        int numberOfSheets = -1;
        String filename;
        String realm;
        if (args.containsOption("skip-first-rows")) {
            skipFirstRows = Integer.parseInt(args.getOptionValues("skip-first-rows").get(0));
        }
        if (args.containsOption("number-of-sheets")) {
            numberOfSheets = Integer.parseInt(args.getOptionValues("number-of-sheets").get(0));
        }
        switch (args.getNonOptionArgs().get(2)) {
            case "print-as-users":
                filename = args.getNonOptionArgs().get(3);
                printFileAsKeycloakUsers(filename, skipFirstRows, numberOfSheets);
                break;
            case "create-users":
                realm = args.getNonOptionArgs().get(3);
                filename = args.getNonOptionArgs().get(4);
                this.usersToKeycloakServer(realm, filename, skipFirstRows, numberOfSheets);
                break;
            default:
                throw new Exception("Function " + args.getNonOptionArgs().get(2) + " not recognised.");
        }
    }

    private void printFileAsKeycloakUsers(String filename, int skipFirstRows, int numberOfSheets) throws Exception {
        ExcelReader reader = new ExcelReader(new WorkbookFactoryWrapper());
        ArrayList<KeycloakUser> users = reader.excelToObjects(new FileInputStream(filename), skipFirstRows, numberOfSheets, KeycloakUser.class);
        this.printKeycloakUsers(users, false);
    }

    private void usersToKeycloakServer(String realm, String filename, int skipFirstRows, int numberOfSheets) throws Exception {
        ExcelReader reader = new ExcelReader(new WorkbookFactoryWrapper());
        ArrayList<KeycloakUser> users = reader.excelToObjects(new FileInputStream(filename), skipFirstRows, numberOfSheets, KeycloakUser.class);

        Console console = System.console();
        String username = console.readLine("KeyCloak username: ");
        String password = new String(console.readPassword("KeyCloak password: "));

        //Check if any user already exists in realm and list hits
        ArrayList<KeycloakUserIssue> conflicts = this.keycloakService.validateUsers(users, realm, username, password);
        if (conflicts.size() > 0) {
            System.out.println("WARNING: User(s) with given username or email already exists!\n" +
                    "Username conflicts: Old user data will be replaced by file contents if proceeding (roles will be added but not deleted). " +
                    "Email conflicts: Operations will fail!" +
                    "If the password field of the affected users is set in the file the password will be reset as a temporary password. " +
                    "Remove any conflicting users and/or password entries from input file if this is not intended.\n" +
                    "Users in conflict:");
            ArrayList<KeycloakUser> conflictUsers = new ArrayList<>();
            for (KeycloakUserIssue conflict : conflicts) {
                conflictUsers.add(conflict.getUser());
            }
            this.printKeycloakUsers(conflictUsers, true);
            if (!console.readLine("Do you wish to proceed (y/n)? ").equals("y")) {
                throw new Exception("Aborted by user");
            }
        }

        //Check if any role in input file doesn't exist on server, abort if so (or maybe option to create missing roles in the future).
        ArrayList<String> missingRoles = new ArrayList<>();
        ArrayList<String> serverRoles = this.keycloakService.listRoles(realm, username, password);
        for (KeycloakUser user : users) {
            for (KeycloakRole role : user.getRoles()) {
                if (!serverRoles.contains(role.getName())) {
                    missingRoles.add(role.getName());
                }
            }
        }
        if (missingRoles.size() > 0) {
            String msg = "Some roles given in file does not exist on server. Roles missing:\n";
            for (String missingRole : missingRoles) {
                msg += missingRole + "\n";
            }
            throw new Exception(msg);
        }

        //List actions that will be taken
        System.out.println("The following actions will be taken\n-----------------------------");
        for (KeycloakUser user : users) {
            boolean update = false;
            boolean fail = false;
            for (KeycloakUserIssue conflict : conflicts) {
                if (conflict.getUser().getUsername().equals(user.getUsername()) &&
                        conflict.hasIssue(KeycloakUserIssue.USERNAME_CONFLICT)) {
                    update = true;
                }

                if (conflict.getUser().getUsername().equals(user.getUsername()) &&
                        conflict.hasIssue(KeycloakUserIssue.EMAIL_CONFLICT)) {
                    fail = true;
                }
            }
            if (update) {
                System.out.println("UPDATE");
            } else {
                System.out.println("CREATE");
            }

            if (fail) {
                System.out.println("WARNING: Operation will fail due to email conflict!");
            }

            System.out.println(user.toString());
            System.out.println("-----------------------------");
        }
        if (!console.readLine("Do you wish to proceed (y/n)? ").equals("y")) {
            throw new Exception("Aborted by user");
        }

        //Create users, update any existing with new info
        ArrayList<KeycloakCreateUserResponse> responses = this.keycloakService.createUsers(users, realm, username, password);
        System.out.println("Create user response statuses:");
        for (KeycloakCreateUserResponse response : responses) {
            System.out.println(response.getStatus().name());
        }
    }

    private void printKeycloakUsers(ArrayList<KeycloakUser> users, boolean onlyUsername) {
        System.out.println("\nNumber of users found: " + users.size() + "\n-----------------------------");
        for (KeycloakUser user : users) {
            if (onlyUsername) {
                System.out.println(user.getUsername());
            } else {
                System.out.println(user.toString());
                System.out.println("-----------------------------");
            }
        }
    }

    private void geoserver(List<String> args) throws IOException, GeoServerException {
        String command = consume(args);
        switch (command) {
            case "publish":
                publishLayers(args);
                break;
            case "toggle-caching":
                toggleCaching(args);
                break;
            case "set-style":
                setStyle(args);
                break;
            default:
                throw new IOException("Function " + command + " not recognised.");
        }
    }

    private void publishLayers(List<String> args) throws GeoServerException, IOException {
        String target = consume(args);

        switch(target) {
            case "datastore":
                publishDatastore(args);
                break;
            default:
                throw new IOException("Function " + target + " not recognised.");
        }
    }

    private void publishDatastore(List<String> args) throws IOException, GeoServerException {
        String workspace = consume(args);
        String datastore = consume(args);
        String srs = consume(args);
        String defaultStyle = consume(args);

        System.out.println("Publishing layers...");

        var published = geoserverService.PublishAllLayers(workspace, datastore, srs, defaultStyle);

        printGeoserverLayers(published, "Published " + published.size() + " layers in datastore " +
                datastore + ":");
    }

    private void toggleCaching(List<String> args) throws IOException, GeoServerException {
        String target = consume(args);

        switch(target) {
            case "workspace":
                toggleCachingWorkspace(args);
                break;
            case "layer-group":
                toggleCachingLayerGroup(args);
                break;
            default:
                throw new IOException("Function " + target + " not recognised.");
        }
    }

    private void toggleCachingWorkspace(List<String> args) throws GeoServerException, IOException {
        String workspace = consume(args);
        String toggleString = consume(args);

        var configured = geoserverService.ToggleCachingWorkspace(workspace, toggle(toggleString));

        printGeoserverLayers(configured, "Caching " + toggleString + " for " + configured.size() +
                " layers in workspace " + workspace + ":");
    }

    private void toggleCachingLayerGroup(List<String> args) throws GeoServerException, IOException {
        String workspace = consume(args);
        String layerGroup = consume(args);
        String toggleString = consume(args);

        var configured = geoserverService.ToggleCachingLayerGroup(workspace, layerGroup, toggle(toggleString));

        printGeoserverLayers(configured, "Caching " + toggleString + " for " + configured.size() +
                " layers in layer group " + layerGroup + ":");
    }

    private void setStyle(List<String> args) throws IOException, GeoServerException {
        String target = consume(args);

        switch(target) {
            case "workspace":
                setStyleWorkspace(args);
                break;
            case "layer-group":
                setStyleLayerGroup(args);
                break;
            default:
                throw new IOException("Function " + target + " not recognised.");
        }
    }

    private void setStyleWorkspace(List<String> args) throws IOException, GeoServerException {
        String workspace = consume(args);
        String style = consume(args);

        geoserverService.SetStyleWorkspace(workspace, style);

        var configured = geoserverService.SetStyleWorkspace(workspace, style);

        printGeoserverLayers(configured, "Style " + style + " set for " + configured.size() +
                " layers in workspace " + workspace + ":");
    }

    private void setStyleLayerGroup(List<String> args) throws GeoServerException {
        String workspace = consume(args);
        String layerGroup = consume(args);
        String style = consume(args);

        var configured = geoserverService.SetStyleLayerGroup(workspace, layerGroup, style);

        printGeoserverLayers(configured, "Style " + style + " set for " + configured.size() +
                " layers in layer group " + layerGroup + ":");
    }

    private boolean toggle(String enabledString) throws IOException {
        switch (enabledString) {
            case "enabled":
                return true;
            case "disabled":
                return false;
            default:
                throw new IOException("Toggle " + enabledString + "Not recognised.");
        }
    }

    private void printGeoserverLayers(List<String> layers, String title) {
        System.out.println(title);
        System.out.println("-----------------------------");
        for (var layer: layers) {
            System.out.println(layer);
        }
    }

    private String consume(List<String> args) {
        return args.remove(0);
    }
}
