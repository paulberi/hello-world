package application.extra;

public enum Rating {
    G, PG, PG_13, R, NC_17;
    
	@Override
	public String toString() {
		return name();
	}
  
}
