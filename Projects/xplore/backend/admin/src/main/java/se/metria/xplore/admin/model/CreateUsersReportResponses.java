package se.metria.xplore.admin.model;

public class CreateUsersReportResponses {
    /**
     * Statusar en körning kan ha.
     *
     * SUCCESS: Lyckad körning.
     * FAIL: Misslyckad körning.
     * WARNING: Delvis lyckad, men ett eller flera steg kan ha gått fel.
     * UNKNOWN: Ett okänt fel har skett och vi vet inte vilket state Keycloak har efter körningen.
     *
     * WARNING och UNKNOWN finns bara i skarp körning, SUCCESS och FAIL finns alltid.
     */
    public enum Status {
        SUCCESS,
        FAIL,
        WARNING,
        UNKNOWN
    }

    /**
     * Actions som anger om en användare kommer att skapas eller uppdateras.
     * UNKNOWN kan bara ske om något gått fel under skarp körning.
     */
    public enum Action {
        CREATE,
        UPDATE,
        UNKNOWN
    }

    public static class Message {
        /**
         * Meddelande för om filen inte kunde läsas eller konverteras till en representation av användare.
         */
        public static final String READ_FILE_ERROR = "Misslyckades att läsa uppladdad fil. Vänligen kontrollera att filen följer angivet format.";

        /**
         * Meddelanden tillhörande olika resultat av validering (dry-run).
         */
        public static final String VALIDATE_SUCCESS = "Validering av uppladdad fil lyckades.";
        public static final String VALIDATE_FAIL = "Validering av uppladdad fil misslyckades. Vänligen kontrollera felrapporten och åtgärda felen.";
        public static final String VALIDATE_EMAIL_CONFLICT = "Epost-addressen är upptagen.";
        public static final String VALIDATE_ROLES_MISSING = "Roller saknas på KeyCloak servern: ";
        public static final String VALIDATE_DUPLICATE_EMAIL_FILE_CONFLICT = "Epost-addressen finns redan angiven hos en annan användare i filen.";
        public static final String VALIDATE_DUPLICATE_USERNAME_FILE_CONFLICT = "Användarnamnet finns redan angivet hos en annan användare i filen.";

        /**
         * Meddelanden för det övergripande resultatet av en skarp körning.
         */
        public static final String CREATE_SUCCESS = "Användare har skapats.";
        public static final String CREATE_ERROR = "Fel uppstod under skapande av en eller fera användare. Vänligen kontrollera felrapporten.";

        /**
         * Meddelanden för resultatet av individuella operationer.
         */
        public static final String USER_CREATE_ERROR = "Fel uppstod vid skapande av användare.";
        public static final String USER_ADD_ROLES_ERROR = "Användaren kan vara skapad men fel uppstod med att lägga till roller.";
        public static final String USER_UPDATE_ERROR = "Fel uppstod vid uppdatering av användare.";
        public static final String USER_RESET_PASSWORD_ERROR = "Användaren kan vara skapad men fel uppstod med att återställa lösenord.";
        public static final String USER_UNKNOWN_ERROR = "Ett okänt fel uppstod vid skapande/uppdatering av användare. Vänligen kontakta support om felet kvarstår.";
    }
}
