package se.metria.xplore.sok.fastighet;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import se.metria.xplore.sok.SokControllerBaseE2E;

import static io.restassured.RestAssured.given;

class FastighetGeometryE2E extends SokControllerBaseE2E {

	private final String fastighetGeometryQuery = "/fastighet/geometry";
	private final String fastighetGeometryKommunQuery = "/fastighet/geometry?k={k}";

	private static final String WKT = "POLYGON((479361.2636895094 6408036.790270894,479125.0371141922 6407534.051661885,479561.1477147778 6407709.7073204545,479361.2636895094 6408036.790270894))";
	private static final String INVALID_WKT = "POLYGON((479361.2636895094 6408036.790270894,479125.0371141922 6407534.051661885,479561.1477147778 6407709.7073204545,479361.2636895094 6408036.790270894";

	private static final String WKT2 = "POLYGON((618180.5078853423 6743280.577095148,618159.7123588421 6743298.117260788,618150.5923588421 6743308.003260788,618142.0420415394 6743326.72016802,617984.4322008161 6743549.809143461,617983.4082008161 6743551.796143461,617914.2241766802 6743723.414009442,617912.1391766802 6743730.387009442,617895.0588413907 6743795.394882755,617893.4738413906 6743802.373882755,617868.7170975762 6744011.507496141,617868.6800975862 6744014.506496142,617916.1321155633 6744331.469507646,617917.0951155532 6744334.480507646,618153.8490074624 6744728.243400556,618165.6980074624 6744740.386400556,618304.287137641 6744811.575944646,618466.5662155108 6744950.151459138,618666.116415425 6744997.438005334,618730.5926514135 6745030.557730082,618774.0652578674 6745023.01817477,618815.8857036722 6745032.9281846,618815.7817816628 6745034.315634584,618824.4948456609 6745034.968255672,618923.581650944 6745058.448426563,619043.562650944 6745057.923426564,619110.8757625391 6745037.332969271,619123.063294804 6745039.526127085,619237.1009451967 6744998.721855163,619375.3093956782 6744956.445170432,619376.5381477694 6744959.62990818,619386.5841477694 6744955.753908181,619385.6325158519 6744953.287423598,619594.9903487259 6744889.246801874,619779.8221582673 6744664.838473509,619943.1191330005 6744473.12051101,619949.2761330005 6744460.19951101,619949.6620267364 6744458.632187549,619961.5999217072 6744444.138129079,619967.7627376216 6744385.115397298,620045.8007594707 6744068.160725172,620018.169733253 6743902.355791238,620021.4847747401 6743870.606862298,620008.8404145716 6743846.373539424,620005.5390373379 6743826.563032575,620010.2908557402 6743825.437570725,620009.3408557402 6743821.426570725,620001.3547245101 6743801.454312163,619979.432521334 6743669.906195903,619977.493521334 6743664.883195903,619929.1846174987 6743578.443617938,619914.7877812606 6743528.661431876,619912.8387812605 6743524.638431876,619845.6073564382 6743428.89803524,619840.1507689132 6743419.134511638,619827.8537689133 6743402.987511628,619826.1809240385 6743401.233972668,619728.5019248833 6743262.134940529,619723.5639248834 6743257.075940529,619602.1375414081 6743151.245731779,619589.751541408 6743142.09573178,619576.7748037119 6743159.661838131,619357.2272134832 6743011.475551464,619341.3062134832 6743005.280551463,618876.5434790985 6742942.445813976,618806.0767782975 6742967.294538955,618786.6475159946 6742969.827023048,618787.1857166672 6742973.956108033,618608.6615916779 6743036.909202666,618600.6006435486 6743016.015069827,618593.0656435486 6743018.922069827,618563.4679105671 6743030.887066171,618547.3879105671 6743037.688066171,618555.0537939037 6743055.812960165,618434.246378309 6743098.413368747,618416.106378309 6743110.187368747,618441.5177943507 6743149.338301904,618378.6886339474 6743174.465613373,618353.3312165758 6743135.395545718,618350.3082165759 6743137.357545718,618241.0517955325 6743229.510813503,618208.7260908097 6743242.438855925,618205.6890908097 6743245.400855925,618180.5078853423 6743280.577095148))";
	private static final String WKT3 = "POLYGON((619729.7872278928 6744183.164972256,619729.2905309984 6744183.019276641,619728.7875293309 6744183.141441856,619728.4129946505 6744183.498735558,619707.6049946506 6744217.654735559,619707.4592766863 6744218.151478357,619707.5814717113 6744218.654524585,619707.9388330312 6744219.029064328,619728.8698330312 6744231.777064328,619729.3665564694 6744231.922724912,619729.8695616345 6744231.80050796,619730.2440676992 6744231.443161647,619751.0470676991 6744197.286161647,619751.1927246714 6744196.789465071,619751.0705319731 6744196.286481584,619750.7132278928 6744195.911972256,619729.7872278928 6744183.164972256))";
	private static final String WKT4 = "POLYGON((619729.7872278928 6744183.164972256,619729.2905309984 6744183.019276641,619728.7875293309 6744183.141441856,619728.4129946505 6744183.498735558,619707.6049946506 6744217.654735559,619707.4592766863 6744218.151478357,619707.5814717113 6744218.654524585,619707.9388330312 6744219.029064328,619728.8698330312 6744231.777064328,619729.3665564694 6744231.922724912,619729.8695616345 6744231.80050796,619730.2440676992 6744231.443161647,619751.0470676991 6744197.286161647,619751.1927246714 6744196.789465071,619751.0705319731 6744196.286481584,619750.7132278928 6744195.911972256,619729.7872278928 6744183.164972256))";

	@Test
	void getFastighetIntersects_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				and().
				header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).
				body("q=" + WKT + "&o=INTERSECTS").
				when().
				post(fastighetGeometryQuery).
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":2"));
	}

	@Test
	void getFastighetIntersects_kommun_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				and().
				header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).
				body("q=" + WKT + "&o=INTERSECTS").
				when().
				post(fastighetGeometryKommunQuery, "aneby").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":2"));
	}

	@Test
	void getFastighetIntersects_buffer_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				and().
				header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).
				body("q=" + WKT4 + "&o=INTERSECTS&b=100").
				when().
				post(fastighetGeometryQuery).
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":18"));
	}

	@Test
	void getFastighetIntersects_kommun_empty() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				and().
				header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).
				body("q=" + WKT + "&o=INTERSECTS").
				when().
				post(fastighetGeometryKommunQuery, "ödeshög").
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":0"));
	}

	@Test
	void getFastighetIntersects_invalid_wkt_fail() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				and().
				header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).
				body("q=" + INVALID_WKT + "&o=INTERSECTS").
				when().
				post(fastighetGeometryQuery).
				then().
				assertThat().
				statusCode(400);
	}

	@Test
	void getFastighetIntersects_auth_fail() {
		given().
				header(HttpHeaders.AUTHORIZATION, "").
				and().
				header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).
				body("q=" + WKT + "&o=INTERSECTS").
				when().
				post(fastighetGeometryQuery).
				then().
				assertThat().
				statusCode(500);
	}

	@Test
	void getFastighetWithin_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				and().
				header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).
				body("q=" + WKT2 + "&o=WITHIN").
				when().
				post(fastighetGeometryQuery).
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":64"));
	}

	@Test
	void getFastighetWithin_buffer_success() {
		given().
				header(HttpHeaders.AUTHORIZATION, auth).
				and().
				header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).
				body("q=" + WKT3 + "&o=WITHIN&b=100").
				when().
				post(fastighetGeometryQuery).
				then().
				assertThat().
				statusCode(200).
				and().
				assertThat().body(CoreMatchers.containsString("\"totalFeatures\":12"));
	}
}