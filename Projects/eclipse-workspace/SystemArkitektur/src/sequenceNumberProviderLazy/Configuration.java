package sequenceNumberProviderLazy;

public class Configuration {

	private static int number;
	private static int objectCount;

	Configuration() {
		if(objectCount>0)
			System.out.println("there is already an object, refer to it");
		number = 1;
		objectCount++;
	}

	private static Configuration config;

	public static Configuration getConfig() {
		if (config == null) {
			config = new Configuration();
		}

		return config;
	}

	public static int numbers() {
		return number++;
	}

}
