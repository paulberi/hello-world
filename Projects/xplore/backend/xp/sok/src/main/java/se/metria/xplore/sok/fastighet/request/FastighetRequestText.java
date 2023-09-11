package se.metria.xplore.sok.fastighet.request;

import se.metria.xplore.maputils.CqlEscape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.of;

/**
 * Makes a text query over the attributes KOMMUN, TRAKT, BLOCKENHET.
 */
public class FastighetRequestText extends FastighetRequest {

    private final String QUERY_TEMPLATE;

    private static final String KOMMUN = "kommunnamn";
	private static final String TRAKT = "trakt";
	private static final String BLOCK = "blockenhet";

	private List<String> queryComponents;

	public FastighetRequestText(String query, String[] kommun, String typeNames) {
		super(query, kommun);
		QUERY_TEMPLATE = String.format("service=WFS&version=2.0.0&srsName=EPSG:3006&request=GetFeature&typeNames=%s&cql_filter={cql_filter}&outputFormat=application/json&count=100&sortBy=kommunnamn,trakt,blockenhet,omrnr,objekt_id", typeNames);
	}

	public String getWfsQueryTemplate() {
        return QUERY_TEMPLATE;
    }

    public String getCqlFilter() {
        queryComponents = new ArrayList<>(Arrays.asList(this.query.toUpperCase().split("\\s+")));

		if (hasKommun()) {
			enforceKommunComponent();
		}

		switch (queryComponents.size()) {
			case 1:
				return createCqlFilter(of(
						of(KOMMUN))); // "LULEÅ"
			case 2:
				return createCqlFilter(of(
						of(KOMMUN, TRAKT),    // "LULEÅ STADSÖN"
						of(KOMMUN, KOMMUN))); // "LILLA EDET"
			case 3:
				return createCqlFilter(of(
						of(KOMMUN, TRAKT, BLOCK),    // "LULEÅ STADSÖN 1:825"
						of(KOMMUN, TRAKT, TRAKT),    // "SKELLEFTEÅ SKELLEFTEÅ PRÄSTBORD"
						of(KOMMUN, KOMMUN, TRAKT))); // "LILLA EDET AMNERÖD"
			case 4:
				return createCqlFilter(of(
						of(KOMMUN, TRAKT, TRAKT, BLOCK),   // "SKELLEFTEÅ SKELLEFTEÅ PRÄSTBORD 8:8"
						of(KOMMUN, KOMMUN, TRAKT, BLOCK),  // "LILLA EDET AMNERÖD 1:10"
						of(KOMMUN, KOMMUN, TRAKT, TRAKT),  // "LILLA EDET VÄSTRA BERG"
						of(KOMMUN, TRAKT, TRAKT, TRAKT))); // "OSKARSHAMN VIRBO MED EKÖ"
			case 5:
				return createCqlFilter(of(
						of(KOMMUN, KOMMUN, TRAKT, TRAKT, BLOCK),  // "LILLA EDET VÄSTRA BERG 2:12"
						of(KOMMUN, TRAKT, TRAKT, TRAKT, TRAKT),   // "GOTLAND NÄR BOMUNDS I BURGEN"
						of(KOMMUN, KOMMUN, TRAKT, TRAKT, TRAKT),  // "LILLA EDET SANKT PEDERS ÄLEKÄRR"
						of(KOMMUN, TRAKT, TRAKT, TRAKT, BLOCK))); // "OSKARSHAMN VIRBO MED EKÖ 1:8"
			default:
				return createCqlFilter(of(
						of(KOMMUN, KOMMUN, TRAKT, TRAKT, TRAKT, BLOCK),  // "LILLA EDET SANKT PEDERS ÄLEKÄRR 1:8"
						of(KOMMUN, TRAKT, TRAKT, TRAKT, TRAKT, BLOCK))); // "GOTLAND NÄR BOMUNDS I BURGEN 1:11"
		}
	}

	/*
	 * Creates a CQL query of form "(A) OR (B) OR (C) ...".
	 * All filters with two part KOMMUN are ignored if kommun is restricted.
	 *
	 * @param filters List of string lists containing the attributes for the filter parts
	 * @return String with complete CQL query of form "(A) OR (B) OR (C) ...".
	 */
    private String createCqlFilter(List<List<String>> filters) {
		return filters.stream()
				.filter(f -> !hasKommun() || !hasTwoPartKommunFilter(f))
				.map(this::createPartFilter).collect(
						Collectors.joining(") OR (", "(", ")"));
	}

	/*
	 * Creates a CQL query of form "A AND B AND C ...".
	 * All subsequent instances of the same attribute are grouped together, for instance if
	 * the filter is "KOMMUN, TRAKT, TRAKT, TRAKT, BLOCK" then all three TRAKT will be handled together.
	 *
	 * @param attributes String list containing the attributes for the filter
	 * @return String containing CQL query of form "A AND B AND C ...".
	 */
	private String createPartFilter(List<String> attributes) {
		List<String> result = new ArrayList<>();

		for (int i = 0; i < attributes.size(); i++) {
			List<String> values = new ArrayList<>();
			values.add(queryComponents.get(i));

			while (i + 1 < attributes.size() && attributes.get(i).equals(attributes.get(i + 1))) {
				values.add(queryComponents.get(++i));
			}

			result.add(createAttributeFilter(attributes.get(i), values));
		}

		return String.join(" AND ", result);
	}

	/*
	 * Creates a CQL query of form "attribute LIKE 'A%% B%% C%% ...'", e.g. "TRAKT LIKE 'NÄR%% BOMUNDS%% I%% BURGEN%%'"
	 *
	 * @param attribute The attribute for which the CQL filter should be created
	 * @param values The values which should restrict the selected attribute
	 * @return String with CQL query of form "A LIKE 'B%% C%% ...'".
	 */
	private String createAttributeFilter(String attribute, List<String> values) {
		if (hasKommun() && attribute.equals(KOMMUN)) {
			return String.format("%s IN (%s)", KOMMUN, getKommunCqlFormatted());
		}

		return String.format("%s LIKE '%s'",
				attribute,
				values.stream().map(CqlEscape::cqlEscape).collect(Collectors.joining("%% ", "", "%%")));
	}

	/*
	 * Ensures that the query components contains a component for the KOMMUN. This is a
	 * convenience so that it is possible to leave out the KOMMUN if all searches are always
	 * made against the same kommun.
	 *
	 * If the query contains one of the restricted kommuner then the list of kommuner is
	 * replaced by this specific kommun. This makes it possible to limit the search to only
	 * one of the allowed kommuner.
	 */
	private void enforceKommunComponent() {
		String oneWordKommun = queryComponents.get(0);
		String twoWordKommun = queryComponents.size() > 1 ? queryComponents.get(0) + " " + queryComponents.get(1) : null;

		if (getKommun().contains(oneWordKommun)) {
			setKommun(new String[]{oneWordKommun});
		} else if (getKommun().contains(twoWordKommun)) {
			setKommun(new String[]{twoWordKommun});

			// Remove one of the components since kommun filtering requires only one KOMMUN component
			queryComponents.remove(0);
		} else {
			// Since the kommun was missing we have to prepend a ("dummy") component for the KOMMUN
			queryComponents.add(0, "");
		}
	}

	/*
	 * Tests if the filter starts with two subsequent KOMMUN attributes
	 *
	 * @param filter String list with attributes
	 * @return boolean
	 */
	private boolean hasTwoPartKommunFilter(List<String> filter) {
		return filter.size() > 1 && filter.get(0).equals(KOMMUN) && filter.get(1).equals(KOMMUN);
	}
}
