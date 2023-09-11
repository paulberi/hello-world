package InlämningsUoogift;

public class snitthastighet {
	
	public static void main(String args[]) {
		
		double distansInMiles=24;
		double distansInKm=distansInMiles*1.6;
		double TidInSeconds=(((1*60)+40)*60)+35;
		double TidInMinutes=TidInSeconds/60;
		double TidITimme=TidInMinutes/60;
		System.out.println("24 mile är värde "+distansInKm+"Km");
		System.out.println("1 timme 40 minuter och 35 secunder är värde "+TidITimme+"timmar");
		
		
		double snittHastighet=myMethod(TidITimme,distansInKm); //hur avgör man vilken ordning man skriver argement variabler på.
		
		System.out.println("Snitthastighet per Timme är "+snittHastighet+ "km/timme");
	}

	private static Double myMethod(double timme, double distans) {
		// TODO Auto-generated method stub
		double speed;
		return speed=distans/timme;
	}

}
