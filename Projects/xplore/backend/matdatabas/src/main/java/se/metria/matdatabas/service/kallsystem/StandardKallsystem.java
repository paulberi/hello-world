package se.metria.matdatabas.service.kallsystem;

public enum StandardKallsystem {
    MiljöKoll("MiljöKoll"),
    RH2000_GRANSKAT("Annat (RH2000, granskade mätningar)"),
    RH2000_EJ_GRANSKAT("Annat (RH2000, ej granskade mätningar)"),
    SMHImetobs("SMHI metobs"),
    StockholmsHamnar("Stockholms Hamnar"),
    Kolibri("KOLIBRI"),
    RH00Stockholm("Annat (RH00, granskade mätningar)");


    private String namn;

    StandardKallsystem(String namn) {
        this.namn = namn;
    }

    public String getNamn() {
        return namn;
    }
}
