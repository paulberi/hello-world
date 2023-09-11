package se.metria.xplore.sok.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class LanUtil {

	public static Map<String, String> lanMap;
	public static Map<String, String> reverseLanMap;

	static {
		lanMap = Map.ofEntries(
				Map.entry("Blekinge län", "10"),
				Map.entry("Dalarnas län", "20"),
				Map.entry("Gotlands län", "9"),
				Map.entry("Gävleborgs län", "21"),
				Map.entry("Hallands län", "13"),
				Map.entry("Jämtlands län", "23"),
				Map.entry("Jönköpings län", "6"),
				Map.entry("Kalmar län", "8"),
				Map.entry("Kronobergs län", "7"),
				Map.entry("Norrbottens län", "25"),
				Map.entry("Skåne län", "12"),
				Map.entry("Stockholms län", "1"),
				Map.entry("Södermanlands län", "4"),
				Map.entry("Uppsala län", "3"),
				Map.entry("Värmlands län", "17"),
				Map.entry("Västerbottens län", "24"),
				Map.entry("Västernorrlands län", "22"),
				Map.entry("Västmanlands län", "19"),
				Map.entry("Västra Götalands län", "14"),
				Map.entry("Örebro län", "18"),
				Map.entry("Östergötlands län", "5")
		);

		reverseLanMap = lanMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
	}

	/*
	 * Maps a full länsnamn to the corresponding länskod
	 */
	public static String mapLansnamnToLanskod(String lan) {
		return lanMap.get(lan.toUpperCase());
	}

	/*
	 * Maps a full or partial länsnamn to a list of corresponding länskoder.
	 */
	public static List<String> mapLansnamnToLanskoder(String lan) {
		return lanMap.entrySet().stream()
				.filter(e -> e.getKey().toUpperCase().startsWith(lan.toUpperCase()))
				.map(Map.Entry::getValue)
				.collect(Collectors.toList());
	}

	/*
	 * Maps a länskod to the corresponding länsnamn, or null if there is no match
	 */
	public static String mapLanskodToLansnamn(String lanskod) {
		return lanMap.entrySet().stream()
				.filter(e -> e.getValue().equals(lanskod))
				.map(Map.Entry::getKey)
				.findAny().orElse(null);
	}
}
