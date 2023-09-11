--
-- PostgreSQL database dump
--

-- Dumped from database version 10.5
-- Dumped by pg_dump version 11.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: anebyext; Type: SCHEMA; Schema: -; Owner: geovis
--

CREATE SCHEMA anebyext;


ALTER SCHEMA anebyext OWNER TO geovis;

--
-- Name: tranasext; Type: SCHEMA; Schema: -; Owner: xplore-admin
--

CREATE SCHEMA tranasext;


ALTER SCHEMA tranasext OWNER TO "xplore-admin";

--
-- Name: box2d; Type: SHELL TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.box2d;


--
-- Name: box2d_in(cstring); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.box2d_in(cstring) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX2D_in';


ALTER FUNCTION public.box2d_in(cstring) OWNER TO postgres;

--
-- Name: box2d_out(public.box2d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.box2d_out(public.box2d) RETURNS cstring
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX2D_out';


ALTER FUNCTION public.box2d_out(public.box2d) OWNER TO postgres;

--
-- Name: box2d; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.box2d (
    INTERNALLENGTH = 65,
    INPUT = public.box2d_in,
    OUTPUT = public.box2d_out,
    ALIGNMENT = int4,
    STORAGE = plain
);


ALTER TYPE public.box2d OWNER TO postgres;

--
-- Name: box2df; Type: SHELL TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.box2df;


--
-- Name: box2df_in(cstring); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.box2df_in(cstring) RETURNS public.box2df
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'box2df_in';


ALTER FUNCTION public.box2df_in(cstring) OWNER TO postgres;

--
-- Name: box2df_out(public.box2df); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.box2df_out(public.box2df) RETURNS cstring
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'box2df_out';


ALTER FUNCTION public.box2df_out(public.box2df) OWNER TO postgres;

--
-- Name: box2df; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.box2df (
    INTERNALLENGTH = 16,
    INPUT = public.box2df_in,
    OUTPUT = public.box2df_out,
    ALIGNMENT = double,
    STORAGE = plain
);


ALTER TYPE public.box2df OWNER TO postgres;

--
-- Name: box3d; Type: SHELL TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.box3d;


--
-- Name: box3d_in(cstring); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.box3d_in(cstring) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_in';


ALTER FUNCTION public.box3d_in(cstring) OWNER TO postgres;

--
-- Name: box3d_out(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.box3d_out(public.box3d) RETURNS cstring
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_out';


ALTER FUNCTION public.box3d_out(public.box3d) OWNER TO postgres;

--
-- Name: box3d; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.box3d (
    INTERNALLENGTH = 52,
    INPUT = public.box3d_in,
    OUTPUT = public.box3d_out,
    ALIGNMENT = double,
    STORAGE = plain
);


ALTER TYPE public.box3d OWNER TO postgres;

--
-- Name: geography; Type: SHELL TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.geography;


--
-- Name: geography_analyze(internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_analyze(internal) RETURNS boolean
    LANGUAGE c STRICT
    AS '$libdir/postgis-2.4', 'gserialized_analyze_nd';


ALTER FUNCTION public.geography_analyze(internal) OWNER TO postgres;

--
-- Name: geography_in(cstring, oid, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_in(cstring, oid, integer) RETURNS public.geography
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_in';


ALTER FUNCTION public.geography_in(cstring, oid, integer) OWNER TO postgres;

--
-- Name: geography_out(public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_out(public.geography) RETURNS cstring
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_out';


ALTER FUNCTION public.geography_out(public.geography) OWNER TO postgres;

--
-- Name: geography_recv(internal, oid, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_recv(internal, oid, integer) RETURNS public.geography
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_recv';


ALTER FUNCTION public.geography_recv(internal, oid, integer) OWNER TO postgres;

--
-- Name: geography_send(public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_send(public.geography) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_send';


ALTER FUNCTION public.geography_send(public.geography) OWNER TO postgres;

--
-- Name: geography_typmod_in(cstring[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_typmod_in(cstring[]) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_typmod_in';


ALTER FUNCTION public.geography_typmod_in(cstring[]) OWNER TO postgres;

--
-- Name: geography_typmod_out(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_typmod_out(integer) RETURNS cstring
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'postgis_typmod_out';


ALTER FUNCTION public.geography_typmod_out(integer) OWNER TO postgres;

--
-- Name: geography; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.geography (
    INTERNALLENGTH = variable,
    INPUT = public.geography_in,
    OUTPUT = public.geography_out,
    RECEIVE = public.geography_recv,
    SEND = public.geography_send,
    TYPMOD_IN = public.geography_typmod_in,
    TYPMOD_OUT = public.geography_typmod_out,
    ANALYZE = public.geography_analyze,
    DELIMITER = ':',
    ALIGNMENT = double,
    STORAGE = main
);


ALTER TYPE public.geography OWNER TO postgres;

--
-- Name: geometry; Type: SHELL TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.geometry;


--
-- Name: geometry_analyze(internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_analyze(internal) RETURNS boolean
    LANGUAGE c STRICT
    AS '$libdir/postgis-2.4', 'gserialized_analyze_nd';


ALTER FUNCTION public.geometry_analyze(internal) OWNER TO postgres;

--
-- Name: geometry_in(cstring); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_in(cstring) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_in';


ALTER FUNCTION public.geometry_in(cstring) OWNER TO postgres;

--
-- Name: geometry_out(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_out(public.geometry) RETURNS cstring
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_out';


ALTER FUNCTION public.geometry_out(public.geometry) OWNER TO postgres;

--
-- Name: geometry_recv(internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_recv(internal) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_recv';


ALTER FUNCTION public.geometry_recv(internal) OWNER TO postgres;

--
-- Name: geometry_send(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_send(public.geometry) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_send';


ALTER FUNCTION public.geometry_send(public.geometry) OWNER TO postgres;

--
-- Name: geometry_typmod_in(cstring[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_typmod_in(cstring[]) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geometry_typmod_in';


ALTER FUNCTION public.geometry_typmod_in(cstring[]) OWNER TO postgres;

--
-- Name: geometry_typmod_out(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_typmod_out(integer) RETURNS cstring
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'postgis_typmod_out';


ALTER FUNCTION public.geometry_typmod_out(integer) OWNER TO postgres;

--
-- Name: geometry; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.geometry (
    INTERNALLENGTH = variable,
    INPUT = public.geometry_in,
    OUTPUT = public.geometry_out,
    RECEIVE = public.geometry_recv,
    SEND = public.geometry_send,
    TYPMOD_IN = public.geometry_typmod_in,
    TYPMOD_OUT = public.geometry_typmod_out,
    ANALYZE = public.geometry_analyze,
    DELIMITER = ':',
    ALIGNMENT = double,
    STORAGE = main
);


ALTER TYPE public.geometry OWNER TO postgres;

--
-- Name: geometry_dump; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.geometry_dump AS (
	path integer[],
	geom public.geometry
);


ALTER TYPE public.geometry_dump OWNER TO postgres;

--
-- Name: gidx; Type: SHELL TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.gidx;


--
-- Name: gidx_in(cstring); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.gidx_in(cstring) RETURNS public.gidx
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gidx_in';


ALTER FUNCTION public.gidx_in(cstring) OWNER TO postgres;

--
-- Name: gidx_out(public.gidx); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.gidx_out(public.gidx) RETURNS cstring
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gidx_out';


ALTER FUNCTION public.gidx_out(public.gidx) OWNER TO postgres;

--
-- Name: gidx; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.gidx (
    INTERNALLENGTH = variable,
    INPUT = public.gidx_in,
    OUTPUT = public.gidx_out,
    ALIGNMENT = double,
    STORAGE = plain
);


ALTER TYPE public.gidx OWNER TO postgres;

--
-- Name: pgis_abs; Type: SHELL TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.pgis_abs;


--
-- Name: pgis_abs_in(cstring); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_abs_in(cstring) RETURNS public.pgis_abs
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_abs_in';


ALTER FUNCTION public.pgis_abs_in(cstring) OWNER TO postgres;

--
-- Name: pgis_abs_out(public.pgis_abs); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_abs_out(public.pgis_abs) RETURNS cstring
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_abs_out';


ALTER FUNCTION public.pgis_abs_out(public.pgis_abs) OWNER TO postgres;

--
-- Name: pgis_abs; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.pgis_abs (
    INTERNALLENGTH = 16,
    INPUT = public.pgis_abs_in,
    OUTPUT = public.pgis_abs_out,
    ALIGNMENT = double,
    STORAGE = plain
);


ALTER TYPE public.pgis_abs OWNER TO postgres;

--
-- Name: spheroid; Type: SHELL TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.spheroid;


--
-- Name: spheroid_in(cstring); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.spheroid_in(cstring) RETURNS public.spheroid
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ellipsoid_in';


ALTER FUNCTION public.spheroid_in(cstring) OWNER TO postgres;

--
-- Name: spheroid_out(public.spheroid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.spheroid_out(public.spheroid) RETURNS cstring
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ellipsoid_out';


ALTER FUNCTION public.spheroid_out(public.spheroid) OWNER TO postgres;

--
-- Name: spheroid; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.spheroid (
    INTERNALLENGTH = 65,
    INPUT = public.spheroid_in,
    OUTPUT = public.spheroid_out,
    ALIGNMENT = double,
    STORAGE = plain
);


ALTER TYPE public.spheroid OWNER TO postgres;

--
-- Name: valid_detail; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.valid_detail AS (
	valid boolean,
	reason character varying,
	location public.geometry
);


ALTER TYPE public.valid_detail OWNER TO postgres;

--
-- Name: _postgis_deprecate(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._postgis_deprecate(oldname text, newname text, version text) RETURNS void
    LANGUAGE plpgsql IMMUTABLE STRICT
    AS $$
DECLARE
  curver_text text;
BEGIN
  --
  -- Raises a NOTICE if it was deprecated in this version,
  -- a WARNING if in a previous version (only up to minor version checked)
  --
    curver_text := '2.4.4';
    IF split_part(curver_text,'.',1)::int > split_part(version,'.',1)::int OR
       ( split_part(curver_text,'.',1) = split_part(version,'.',1) AND
         split_part(curver_text,'.',2) != split_part(version,'.',2) )
    THEN
      RAISE WARNING '% signature was deprecated in %. Please use %', oldname, version, newname;
    ELSE
      RAISE DEBUG '% signature was deprecated in %. Please use %', oldname, version, newname;
    END IF;
END;
$$;


ALTER FUNCTION public._postgis_deprecate(oldname text, newname text, version text) OWNER TO postgres;

--
-- Name: _postgis_join_selectivity(regclass, text, regclass, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._postgis_join_selectivity(regclass, text, regclass, text, text DEFAULT '2'::text) RETURNS double precision
    LANGUAGE c STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', '_postgis_gserialized_joinsel';


ALTER FUNCTION public._postgis_join_selectivity(regclass, text, regclass, text, text) OWNER TO postgres;

--
-- Name: _postgis_pgsql_version(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._postgis_pgsql_version() RETURNS text
    LANGUAGE sql STABLE
    AS $$
	SELECT CASE WHEN split_part(s,'.',1)::integer > 9 THEN split_part(s,'.',1) || '0' ELSE split_part(s,'.', 1) || split_part(s,'.', 2) END AS v
	FROM substring(version(), 'PostgreSQL ([0-9\.]+)') AS s;
$$;


ALTER FUNCTION public._postgis_pgsql_version() OWNER TO postgres;

--
-- Name: _postgis_scripts_pgsql_version(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._postgis_scripts_pgsql_version() RETURNS text
    LANGUAGE sql IMMUTABLE
    AS $$SELECT '100'::text AS version$$;


ALTER FUNCTION public._postgis_scripts_pgsql_version() OWNER TO postgres;

--
-- Name: _postgis_selectivity(regclass, text, public.geometry, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._postgis_selectivity(tbl regclass, att_name text, geom public.geometry, mode text DEFAULT '2'::text) RETURNS double precision
    LANGUAGE c STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', '_postgis_gserialized_sel';


ALTER FUNCTION public._postgis_selectivity(tbl regclass, att_name text, geom public.geometry, mode text) OWNER TO postgres;

--
-- Name: _postgis_stats(regclass, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._postgis_stats(tbl regclass, att_name text, text DEFAULT '2'::text) RETURNS text
    LANGUAGE c STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', '_postgis_gserialized_stats';


ALTER FUNCTION public._postgis_stats(tbl regclass, att_name text, text) OWNER TO postgres;

--
-- Name: _st_3ddfullywithin(public.geometry, public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_3ddfullywithin(geom1 public.geometry, geom2 public.geometry, double precision) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_dfullywithin3d';


ALTER FUNCTION public._st_3ddfullywithin(geom1 public.geometry, geom2 public.geometry, double precision) OWNER TO postgres;

--
-- Name: _st_3ddwithin(public.geometry, public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_3ddwithin(geom1 public.geometry, geom2 public.geometry, double precision) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_dwithin3d';


ALTER FUNCTION public._st_3ddwithin(geom1 public.geometry, geom2 public.geometry, double precision) OWNER TO postgres;

--
-- Name: _st_3dintersects(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_3dintersects(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'intersects3d';


ALTER FUNCTION public._st_3dintersects(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: _st_asgeojson(integer, public.geography, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_asgeojson(integer, public.geography, integer, integer) RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_as_geojson';


ALTER FUNCTION public._st_asgeojson(integer, public.geography, integer, integer) OWNER TO postgres;

--
-- Name: _st_asgeojson(integer, public.geometry, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_asgeojson(integer, public.geometry, integer, integer) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_AsGeoJson($2::geometry, $3::int4, $4::int4); $_$;


ALTER FUNCTION public._st_asgeojson(integer, public.geometry, integer, integer) OWNER TO postgres;

--
-- Name: _st_asgml(integer, public.geography, integer, integer, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_asgml(integer, public.geography, integer, integer, text, text) RETURNS text
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_as_gml';


ALTER FUNCTION public._st_asgml(integer, public.geography, integer, integer, text, text) OWNER TO postgres;

--
-- Name: _st_asgml(integer, public.geometry, integer, integer, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_asgml(integer, public.geometry, integer, integer, text, text) RETURNS text
    LANGUAGE c IMMUTABLE COST 2500 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asGML';


ALTER FUNCTION public._st_asgml(integer, public.geometry, integer, integer, text, text) OWNER TO postgres;

--
-- Name: _st_askml(integer, public.geography, integer, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_askml(integer, public.geography, integer, text) RETURNS text
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_as_kml';


ALTER FUNCTION public._st_askml(integer, public.geography, integer, text) OWNER TO postgres;

--
-- Name: _st_askml(integer, public.geometry, integer, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_askml(integer, public.geometry, integer, text) RETURNS text
    LANGUAGE c IMMUTABLE COST 5000 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asKML';


ALTER FUNCTION public._st_askml(integer, public.geometry, integer, text) OWNER TO postgres;

--
-- Name: _st_asx3d(integer, public.geometry, integer, integer, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_asx3d(integer, public.geometry, integer, integer, text) RETURNS text
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asX3D';


ALTER FUNCTION public._st_asx3d(integer, public.geometry, integer, integer, text) OWNER TO postgres;

--
-- Name: _st_bestsrid(public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_bestsrid(public.geography) RETURNS integer
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT _ST_BestSRID($1,$1)$_$;


ALTER FUNCTION public._st_bestsrid(public.geography) OWNER TO postgres;

--
-- Name: _st_bestsrid(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_bestsrid(public.geography, public.geography) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_bestsrid';


ALTER FUNCTION public._st_bestsrid(public.geography, public.geography) OWNER TO postgres;

--
-- Name: _st_buffer(public.geometry, double precision, cstring); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_buffer(public.geometry, double precision, cstring) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'buffer';


ALTER FUNCTION public._st_buffer(public.geometry, double precision, cstring) OWNER TO postgres;

--
-- Name: _st_concavehull(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_concavehull(param_inputgeom public.geometry) RETURNS public.geometry
    LANGUAGE plpgsql IMMUTABLE STRICT PARALLEL SAFE
    AS $$
	DECLARE
	vexhull GEOMETRY;
	var_resultgeom geometry;
	var_inputgeom geometry;
	vexring GEOMETRY;
	cavering GEOMETRY;
	cavept geometry[];
	seglength double precision;
	var_tempgeom geometry;
	scale_factor float := 1;
	i integer;

	BEGIN

		-- First compute the ConvexHull of the geometry
		vexhull := ST_ConvexHull(param_inputgeom);
		var_inputgeom := param_inputgeom;
		--A point really has no concave hull
		IF ST_GeometryType(vexhull) = 'ST_Point' OR ST_GeometryType(vexHull) = 'ST_LineString' THEN
			RETURN vexhull;
		END IF;

		-- convert the hull perimeter to a linestring so we can manipulate individual points
		vexring := CASE WHEN ST_GeometryType(vexhull) = 'ST_LineString' THEN vexhull ELSE ST_ExteriorRing(vexhull) END;
		IF abs(ST_X(ST_PointN(vexring,1))) < 1 THEN --scale the geometry to prevent stupid precision errors - not sure it works so make low for now
			scale_factor := 100;
			vexring := ST_Scale(vexring, scale_factor,scale_factor);
			var_inputgeom := ST_Scale(var_inputgeom, scale_factor, scale_factor);
			--RAISE NOTICE 'Scaling';
		END IF;
		seglength := ST_Length(vexring)/least(ST_NPoints(vexring)*2,1000) ;

		vexring := ST_Segmentize(vexring, seglength);
		-- find the point on the original geom that is closest to each point of the convex hull and make a new linestring out of it.
		cavering := ST_Collect(
			ARRAY(

				SELECT
					ST_ClosestPoint(var_inputgeom, pt ) As the_geom
					FROM (
						SELECT  ST_PointN(vexring, n ) As pt, n
							FROM
							generate_series(1, ST_NPoints(vexring) ) As n
						) As pt

				)
			)
		;


		var_resultgeom := ST_MakeLine(geom)
			FROM ST_Dump(cavering) As foo;

		IF ST_IsSimple(var_resultgeom) THEN
			var_resultgeom := ST_MakePolygon(var_resultgeom);
			--RAISE NOTICE 'is Simple: %', var_resultgeom;
		ELSE
			--RAISE NOTICE 'is not Simple: %', var_resultgeom;
			var_resultgeom := ST_ConvexHull(var_resultgeom);
		END IF;

		IF scale_factor > 1 THEN -- scale the result back
			var_resultgeom := ST_Scale(var_resultgeom, 1/scale_factor, 1/scale_factor);
		END IF;
		RETURN var_resultgeom;

	END;
$$;


ALTER FUNCTION public._st_concavehull(param_inputgeom public.geometry) OWNER TO postgres;

--
-- Name: _st_contains(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_contains(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'contains';


ALTER FUNCTION public._st_contains(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: _st_containsproperly(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_containsproperly(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'containsproperly';


ALTER FUNCTION public._st_containsproperly(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: _st_coveredby(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_coveredby(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'coveredby';


ALTER FUNCTION public._st_coveredby(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: _st_covers(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_covers(public.geography, public.geography) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_covers';


ALTER FUNCTION public._st_covers(public.geography, public.geography) OWNER TO postgres;

--
-- Name: _st_covers(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_covers(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'covers';


ALTER FUNCTION public._st_covers(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: _st_crosses(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_crosses(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'crosses';


ALTER FUNCTION public._st_crosses(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: _st_dfullywithin(public.geometry, public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_dfullywithin(geom1 public.geometry, geom2 public.geometry, double precision) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_dfullywithin';


ALTER FUNCTION public._st_dfullywithin(geom1 public.geometry, geom2 public.geometry, double precision) OWNER TO postgres;

--
-- Name: _st_distance(public.geography, public.geography, double precision, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_distance(public.geography, public.geography, double precision, boolean) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_distance';


ALTER FUNCTION public._st_distance(public.geography, public.geography, double precision, boolean) OWNER TO postgres;

--
-- Name: _st_distancetree(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_distancetree(public.geography, public.geography) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT _ST_DistanceTree($1, $2, 0.0, true)$_$;


ALTER FUNCTION public._st_distancetree(public.geography, public.geography) OWNER TO postgres;

--
-- Name: _st_distancetree(public.geography, public.geography, double precision, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_distancetree(public.geography, public.geography, double precision, boolean) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.4', 'geography_distance_tree';


ALTER FUNCTION public._st_distancetree(public.geography, public.geography, double precision, boolean) OWNER TO postgres;

--
-- Name: _st_distanceuncached(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_distanceuncached(public.geography, public.geography) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT _ST_DistanceUnCached($1, $2, 0.0, true)$_$;


ALTER FUNCTION public._st_distanceuncached(public.geography, public.geography) OWNER TO postgres;

--
-- Name: _st_distanceuncached(public.geography, public.geography, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_distanceuncached(public.geography, public.geography, boolean) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT _ST_DistanceUnCached($1, $2, 0.0, $3)$_$;


ALTER FUNCTION public._st_distanceuncached(public.geography, public.geography, boolean) OWNER TO postgres;

--
-- Name: _st_distanceuncached(public.geography, public.geography, double precision, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_distanceuncached(public.geography, public.geography, double precision, boolean) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.4', 'geography_distance_uncached';


ALTER FUNCTION public._st_distanceuncached(public.geography, public.geography, double precision, boolean) OWNER TO postgres;

--
-- Name: _st_dwithin(public.geometry, public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_dwithin(geom1 public.geometry, geom2 public.geometry, double precision) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_dwithin';


ALTER FUNCTION public._st_dwithin(geom1 public.geometry, geom2 public.geometry, double precision) OWNER TO postgres;

--
-- Name: _st_dwithin(public.geography, public.geography, double precision, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_dwithin(public.geography, public.geography, double precision, boolean) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_dwithin';


ALTER FUNCTION public._st_dwithin(public.geography, public.geography, double precision, boolean) OWNER TO postgres;

--
-- Name: _st_dwithinuncached(public.geography, public.geography, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_dwithinuncached(public.geography, public.geography, double precision) RETURNS boolean
    LANGUAGE sql IMMUTABLE
    AS $_$SELECT $1 OPERATOR(&&) _ST_Expand($2,$3) AND $2 OPERATOR(&&) _ST_Expand($1,$3) AND _ST_DWithinUnCached($1, $2, $3, true)$_$;


ALTER FUNCTION public._st_dwithinuncached(public.geography, public.geography, double precision) OWNER TO postgres;

--
-- Name: _st_dwithinuncached(public.geography, public.geography, double precision, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_dwithinuncached(public.geography, public.geography, double precision, boolean) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100
    AS '$libdir/postgis-2.4', 'geography_dwithin_uncached';


ALTER FUNCTION public._st_dwithinuncached(public.geography, public.geography, double precision, boolean) OWNER TO postgres;

--
-- Name: _st_equals(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_equals(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_Equals';


ALTER FUNCTION public._st_equals(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: _st_expand(public.geography, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_expand(public.geography, double precision) RETURNS public.geography
    LANGUAGE c IMMUTABLE STRICT COST 50 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_expand';


ALTER FUNCTION public._st_expand(public.geography, double precision) OWNER TO postgres;

--
-- Name: _st_geomfromgml(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_geomfromgml(text, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geom_from_gml';


ALTER FUNCTION public._st_geomfromgml(text, integer) OWNER TO postgres;

--
-- Name: _st_intersects(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_intersects(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'intersects';


ALTER FUNCTION public._st_intersects(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: _st_linecrossingdirection(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_linecrossingdirection(geom1 public.geometry, geom2 public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_LineCrossingDirection';


ALTER FUNCTION public._st_linecrossingdirection(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: _st_longestline(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_longestline(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_longestline2d';


ALTER FUNCTION public._st_longestline(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: _st_maxdistance(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_maxdistance(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_maxdistance2d_linestring';


ALTER FUNCTION public._st_maxdistance(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: _st_orderingequals(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_orderingequals(geometrya public.geometry, geometryb public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_same';


ALTER FUNCTION public._st_orderingequals(geometrya public.geometry, geometryb public.geometry) OWNER TO postgres;

--
-- Name: _st_overlaps(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_overlaps(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'overlaps';


ALTER FUNCTION public._st_overlaps(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: _st_pointoutside(public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_pointoutside(public.geography) RETURNS public.geography
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.4', 'geography_point_outside';


ALTER FUNCTION public._st_pointoutside(public.geography) OWNER TO postgres;

--
-- Name: _st_touches(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_touches(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'touches';


ALTER FUNCTION public._st_touches(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: _st_voronoi(public.geometry, public.geometry, double precision, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_voronoi(g1 public.geometry, clip public.geometry DEFAULT NULL::public.geometry, tolerance double precision DEFAULT 0.0, return_polygons boolean DEFAULT true) RETURNS public.geometry
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_Voronoi';


ALTER FUNCTION public._st_voronoi(g1 public.geometry, clip public.geometry, tolerance double precision, return_polygons boolean) OWNER TO postgres;

--
-- Name: _st_within(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public._st_within(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT _ST_Contains($2,$1)$_$;


ALTER FUNCTION public._st_within(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: addauth(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.addauth(text) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$
DECLARE
	lockid alias for $1;
	okay boolean;
	myrec record;
BEGIN
	-- check to see if table exists
	--  if not, CREATE TEMP TABLE mylock (transid xid, lockcode text)
	okay := 'f';
	FOR myrec IN SELECT * FROM pg_class WHERE relname = 'temp_lock_have_table' LOOP
		okay := 't';
	END LOOP;
	IF (okay <> 't') THEN
		CREATE TEMP TABLE temp_lock_have_table (transid xid, lockcode text);
			-- this will only work from pgsql7.4 up
			-- ON COMMIT DELETE ROWS;
	END IF;

	--  INSERT INTO mylock VALUES ( $1)
--	EXECUTE 'INSERT INTO temp_lock_have_table VALUES ( '||
--		quote_literal(getTransactionID()) || ',' ||
--		quote_literal(lockid) ||')';

	INSERT INTO temp_lock_have_table VALUES (getTransactionID(), lockid);

	RETURN true::boolean;
END;
$_$;


ALTER FUNCTION public.addauth(text) OWNER TO postgres;

--
-- Name: addgeometrycolumn(character varying, character varying, integer, character varying, integer, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.addgeometrycolumn(table_name character varying, column_name character varying, new_srid integer, new_type character varying, new_dim integer, use_typmod boolean DEFAULT true) RETURNS text
    LANGUAGE plpgsql STRICT
    AS $_$
DECLARE
	ret  text;
BEGIN
	SELECT AddGeometryColumn('','',$1,$2,$3,$4,$5, $6) into ret;
	RETURN ret;
END;
$_$;


ALTER FUNCTION public.addgeometrycolumn(table_name character varying, column_name character varying, new_srid integer, new_type character varying, new_dim integer, use_typmod boolean) OWNER TO postgres;

--
-- Name: addgeometrycolumn(character varying, character varying, character varying, integer, character varying, integer, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.addgeometrycolumn(schema_name character varying, table_name character varying, column_name character varying, new_srid integer, new_type character varying, new_dim integer, use_typmod boolean DEFAULT true) RETURNS text
    LANGUAGE plpgsql STABLE STRICT
    AS $_$
DECLARE
	ret  text;
BEGIN
	SELECT AddGeometryColumn('',$1,$2,$3,$4,$5,$6,$7) into ret;
	RETURN ret;
END;
$_$;


ALTER FUNCTION public.addgeometrycolumn(schema_name character varying, table_name character varying, column_name character varying, new_srid integer, new_type character varying, new_dim integer, use_typmod boolean) OWNER TO postgres;

--
-- Name: addgeometrycolumn(character varying, character varying, character varying, character varying, integer, character varying, integer, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.addgeometrycolumn(catalog_name character varying, schema_name character varying, table_name character varying, column_name character varying, new_srid_in integer, new_type character varying, new_dim integer, use_typmod boolean DEFAULT true) RETURNS text
    LANGUAGE plpgsql STRICT
    AS $$
DECLARE
	rec RECORD;
	sr varchar;
	real_schema name;
	sql text;
	new_srid integer;

BEGIN

	-- Verify geometry type
	IF (postgis_type_name(new_type,new_dim) IS NULL )
	THEN
		RAISE EXCEPTION 'Invalid type name "%(%)" - valid ones are:
	POINT, MULTIPOINT,
	LINESTRING, MULTILINESTRING,
	POLYGON, MULTIPOLYGON,
	CIRCULARSTRING, COMPOUNDCURVE, MULTICURVE,
	CURVEPOLYGON, MULTISURFACE,
	GEOMETRY, GEOMETRYCOLLECTION,
	POINTM, MULTIPOINTM,
	LINESTRINGM, MULTILINESTRINGM,
	POLYGONM, MULTIPOLYGONM,
	CIRCULARSTRINGM, COMPOUNDCURVEM, MULTICURVEM
	CURVEPOLYGONM, MULTISURFACEM, TRIANGLE, TRIANGLEM,
	POLYHEDRALSURFACE, POLYHEDRALSURFACEM, TIN, TINM
	or GEOMETRYCOLLECTIONM', new_type, new_dim;
		RETURN 'fail';
	END IF;


	-- Verify dimension
	IF ( (new_dim >4) OR (new_dim <2) ) THEN
		RAISE EXCEPTION 'invalid dimension';
		RETURN 'fail';
	END IF;

	IF ( (new_type LIKE '%M') AND (new_dim!=3) ) THEN
		RAISE EXCEPTION 'TypeM needs 3 dimensions';
		RETURN 'fail';
	END IF;


	-- Verify SRID
	IF ( new_srid_in > 0 ) THEN
		IF new_srid_in > 998999 THEN
			RAISE EXCEPTION 'AddGeometryColumn() - SRID must be <= %', 998999;
		END IF;
		new_srid := new_srid_in;
		SELECT SRID INTO sr FROM spatial_ref_sys WHERE SRID = new_srid;
		IF NOT FOUND THEN
			RAISE EXCEPTION 'AddGeometryColumn() - invalid SRID';
			RETURN 'fail';
		END IF;
	ELSE
		new_srid := ST_SRID('POINT EMPTY'::geometry);
		IF ( new_srid_in != new_srid ) THEN
			RAISE NOTICE 'SRID value % converted to the officially unknown SRID value %', new_srid_in, new_srid;
		END IF;
	END IF;


	-- Verify schema
	IF ( schema_name IS NOT NULL AND schema_name != '' ) THEN
		sql := 'SELECT nspname FROM pg_namespace ' ||
			'WHERE text(nspname) = ' || quote_literal(schema_name) ||
			'LIMIT 1';
		RAISE DEBUG '%', sql;
		EXECUTE sql INTO real_schema;

		IF ( real_schema IS NULL ) THEN
			RAISE EXCEPTION 'Schema % is not a valid schemaname', quote_literal(schema_name);
			RETURN 'fail';
		END IF;
	END IF;

	IF ( real_schema IS NULL ) THEN
		RAISE DEBUG 'Detecting schema';
		sql := 'SELECT n.nspname AS schemaname ' ||
			'FROM pg_catalog.pg_class c ' ||
			  'JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace ' ||
			'WHERE c.relkind = ' || quote_literal('r') ||
			' AND n.nspname NOT IN (' || quote_literal('pg_catalog') || ', ' || quote_literal('pg_toast') || ')' ||
			' AND pg_catalog.pg_table_is_visible(c.oid)' ||
			' AND c.relname = ' || quote_literal(table_name);
		RAISE DEBUG '%', sql;
		EXECUTE sql INTO real_schema;

		IF ( real_schema IS NULL ) THEN
			RAISE EXCEPTION 'Table % does not occur in the search_path', quote_literal(table_name);
			RETURN 'fail';
		END IF;
	END IF;


	-- Add geometry column to table
	IF use_typmod THEN
	     sql := 'ALTER TABLE ' ||
            quote_ident(real_schema) || '.' || quote_ident(table_name)
            || ' ADD COLUMN ' || quote_ident(column_name) ||
            ' geometry(' || postgis_type_name(new_type, new_dim) || ', ' || new_srid::text || ')';
        RAISE DEBUG '%', sql;
	ELSE
        sql := 'ALTER TABLE ' ||
            quote_ident(real_schema) || '.' || quote_ident(table_name)
            || ' ADD COLUMN ' || quote_ident(column_name) ||
            ' geometry ';
        RAISE DEBUG '%', sql;
    END IF;
	EXECUTE sql;

	IF NOT use_typmod THEN
        -- Add table CHECKs
        sql := 'ALTER TABLE ' ||
            quote_ident(real_schema) || '.' || quote_ident(table_name)
            || ' ADD CONSTRAINT '
            || quote_ident('enforce_srid_' || column_name)
            || ' CHECK (st_srid(' || quote_ident(column_name) ||
            ') = ' || new_srid::text || ')' ;
        RAISE DEBUG '%', sql;
        EXECUTE sql;

        sql := 'ALTER TABLE ' ||
            quote_ident(real_schema) || '.' || quote_ident(table_name)
            || ' ADD CONSTRAINT '
            || quote_ident('enforce_dims_' || column_name)
            || ' CHECK (st_ndims(' || quote_ident(column_name) ||
            ') = ' || new_dim::text || ')' ;
        RAISE DEBUG '%', sql;
        EXECUTE sql;

        IF ( NOT (new_type = 'GEOMETRY')) THEN
            sql := 'ALTER TABLE ' ||
                quote_ident(real_schema) || '.' || quote_ident(table_name) || ' ADD CONSTRAINT ' ||
                quote_ident('enforce_geotype_' || column_name) ||
                ' CHECK (GeometryType(' ||
                quote_ident(column_name) || ')=' ||
                quote_literal(new_type) || ' OR (' ||
                quote_ident(column_name) || ') is null)';
            RAISE DEBUG '%', sql;
            EXECUTE sql;
        END IF;
    END IF;

	RETURN
		real_schema || '.' ||
		table_name || '.' || column_name ||
		' SRID:' || new_srid::text ||
		' TYPE:' || new_type ||
		' DIMS:' || new_dim::text || ' ';
END;
$$;


ALTER FUNCTION public.addgeometrycolumn(catalog_name character varying, schema_name character varying, table_name character varying, column_name character varying, new_srid_in integer, new_type character varying, new_dim integer, use_typmod boolean) OWNER TO postgres;

--
-- Name: box(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.box(public.box3d) RETURNS box
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_to_BOX';


ALTER FUNCTION public.box(public.box3d) OWNER TO postgres;

--
-- Name: box(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.box(public.geometry) RETURNS box
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_to_BOX';


ALTER FUNCTION public.box(public.geometry) OWNER TO postgres;

--
-- Name: box2d(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.box2d(public.box3d) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_to_BOX2D';


ALTER FUNCTION public.box2d(public.box3d) OWNER TO postgres;

--
-- Name: box2d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.box2d(public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_to_BOX2D';


ALTER FUNCTION public.box2d(public.geometry) OWNER TO postgres;

--
-- Name: box3d(public.box2d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.box3d(public.box2d) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX2D_to_BOX3D';


ALTER FUNCTION public.box3d(public.box2d) OWNER TO postgres;

--
-- Name: box3d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.box3d(public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_to_BOX3D';


ALTER FUNCTION public.box3d(public.geometry) OWNER TO postgres;

--
-- Name: box3dtobox(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.box3dtobox(public.box3d) RETURNS box
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_to_BOX';


ALTER FUNCTION public.box3dtobox(public.box3d) OWNER TO postgres;

--
-- Name: bytea(public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.bytea(public.geography) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_to_bytea';


ALTER FUNCTION public.bytea(public.geography) OWNER TO postgres;

--
-- Name: bytea(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.bytea(public.geometry) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_to_bytea';


ALTER FUNCTION public.bytea(public.geometry) OWNER TO postgres;

--
-- Name: checkauth(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.checkauth(text, text) RETURNS integer
    LANGUAGE sql
    AS $_$ SELECT CheckAuth('', $1, $2) $_$;


ALTER FUNCTION public.checkauth(text, text) OWNER TO postgres;

--
-- Name: checkauth(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.checkauth(text, text, text) RETURNS integer
    LANGUAGE plpgsql
    AS $_$
DECLARE
	schema text;
BEGIN
	IF NOT LongTransactionsEnabled() THEN
		RAISE EXCEPTION 'Long transaction support disabled, use EnableLongTransaction() to enable.';
	END IF;

	if ( $1 != '' ) THEN
		schema = $1;
	ELSE
		SELECT current_schema() into schema;
	END IF;

	-- TODO: check for an already existing trigger ?

	EXECUTE 'CREATE TRIGGER check_auth BEFORE UPDATE OR DELETE ON '
		|| quote_ident(schema) || '.' || quote_ident($2)
		||' FOR EACH ROW EXECUTE PROCEDURE CheckAuthTrigger('
		|| quote_literal($3) || ')';

	RETURN 0;
END;
$_$;


ALTER FUNCTION public.checkauth(text, text, text) OWNER TO postgres;

--
-- Name: checkauthtrigger(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.checkauthtrigger() RETURNS trigger
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'check_authorization';


ALTER FUNCTION public.checkauthtrigger() OWNER TO postgres;

--
-- Name: contains_2d(public.box2df, public.box2df); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.contains_2d(public.box2df, public.box2df) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.4', 'gserialized_contains_box2df_box2df_2d';


ALTER FUNCTION public.contains_2d(public.box2df, public.box2df) OWNER TO postgres;

--
-- Name: contains_2d(public.box2df, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.contains_2d(public.box2df, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.4', 'gserialized_contains_box2df_geom_2d';


ALTER FUNCTION public.contains_2d(public.box2df, public.geometry) OWNER TO postgres;

--
-- Name: contains_2d(public.geometry, public.box2df); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.contains_2d(public.geometry, public.box2df) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT $2 OPERATOR(@) $1;$_$;


ALTER FUNCTION public.contains_2d(public.geometry, public.box2df) OWNER TO postgres;

--
-- Name: disablelongtransactions(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.disablelongtransactions() RETURNS text
    LANGUAGE plpgsql
    AS $$
DECLARE
	rec RECORD;

BEGIN

	--
	-- Drop all triggers applied by CheckAuth()
	--
	FOR rec IN
		SELECT c.relname, t.tgname, t.tgargs FROM pg_trigger t, pg_class c, pg_proc p
		WHERE p.proname = 'checkauthtrigger' and t.tgfoid = p.oid and t.tgrelid = c.oid
	LOOP
		EXECUTE 'DROP TRIGGER ' || quote_ident(rec.tgname) ||
			' ON ' || quote_ident(rec.relname);
	END LOOP;

	--
	-- Drop the authorization_table table
	--
	FOR rec IN SELECT * FROM pg_class WHERE relname = 'authorization_table' LOOP
		DROP TABLE authorization_table;
	END LOOP;

	--
	-- Drop the authorized_tables view
	--
	FOR rec IN SELECT * FROM pg_class WHERE relname = 'authorized_tables' LOOP
		DROP VIEW authorized_tables;
	END LOOP;

	RETURN 'Long transactions support disabled';
END;
$$;


ALTER FUNCTION public.disablelongtransactions() OWNER TO postgres;

--
-- Name: dropgeometrycolumn(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.dropgeometrycolumn(table_name character varying, column_name character varying) RETURNS text
    LANGUAGE plpgsql STRICT
    AS $_$
DECLARE
	ret text;
BEGIN
	SELECT DropGeometryColumn('','',$1,$2) into ret;
	RETURN ret;
END;
$_$;


ALTER FUNCTION public.dropgeometrycolumn(table_name character varying, column_name character varying) OWNER TO postgres;

--
-- Name: dropgeometrycolumn(character varying, character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.dropgeometrycolumn(schema_name character varying, table_name character varying, column_name character varying) RETURNS text
    LANGUAGE plpgsql STRICT
    AS $_$
DECLARE
	ret text;
BEGIN
	SELECT DropGeometryColumn('',$1,$2,$3) into ret;
	RETURN ret;
END;
$_$;


ALTER FUNCTION public.dropgeometrycolumn(schema_name character varying, table_name character varying, column_name character varying) OWNER TO postgres;

--
-- Name: dropgeometrycolumn(character varying, character varying, character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.dropgeometrycolumn(catalog_name character varying, schema_name character varying, table_name character varying, column_name character varying) RETURNS text
    LANGUAGE plpgsql STRICT
    AS $$
DECLARE
	myrec RECORD;
	okay boolean;
	real_schema name;

BEGIN


	-- Find, check or fix schema_name
	IF ( schema_name != '' ) THEN
		okay = false;

		FOR myrec IN SELECT nspname FROM pg_namespace WHERE text(nspname) = schema_name LOOP
			okay := true;
		END LOOP;

		IF ( okay <>  true ) THEN
			RAISE NOTICE 'Invalid schema name - using current_schema()';
			SELECT current_schema() into real_schema;
		ELSE
			real_schema = schema_name;
		END IF;
	ELSE
		SELECT current_schema() into real_schema;
	END IF;

	-- Find out if the column is in the geometry_columns table
	okay = false;
	FOR myrec IN SELECT * from geometry_columns where f_table_schema = text(real_schema) and f_table_name = table_name and f_geometry_column = column_name LOOP
		okay := true;
	END LOOP;
	IF (okay <> true) THEN
		RAISE EXCEPTION 'column not found in geometry_columns table';
		RETURN false;
	END IF;

	-- Remove table column
	EXECUTE 'ALTER TABLE ' || quote_ident(real_schema) || '.' ||
		quote_ident(table_name) || ' DROP COLUMN ' ||
		quote_ident(column_name);

	RETURN real_schema || '.' || table_name || '.' || column_name ||' effectively removed.';

END;
$$;


ALTER FUNCTION public.dropgeometrycolumn(catalog_name character varying, schema_name character varying, table_name character varying, column_name character varying) OWNER TO postgres;

--
-- Name: dropgeometrytable(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.dropgeometrytable(table_name character varying) RETURNS text
    LANGUAGE sql STRICT
    AS $_$ SELECT DropGeometryTable('','',$1) $_$;


ALTER FUNCTION public.dropgeometrytable(table_name character varying) OWNER TO postgres;

--
-- Name: dropgeometrytable(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.dropgeometrytable(schema_name character varying, table_name character varying) RETURNS text
    LANGUAGE sql STRICT
    AS $_$ SELECT DropGeometryTable('',$1,$2) $_$;


ALTER FUNCTION public.dropgeometrytable(schema_name character varying, table_name character varying) OWNER TO postgres;

--
-- Name: dropgeometrytable(character varying, character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.dropgeometrytable(catalog_name character varying, schema_name character varying, table_name character varying) RETURNS text
    LANGUAGE plpgsql STRICT
    AS $$
DECLARE
	real_schema name;

BEGIN

	IF ( schema_name = '' ) THEN
		SELECT current_schema() into real_schema;
	ELSE
		real_schema = schema_name;
	END IF;

	-- TODO: Should we warn if table doesn't exist probably instead just saying dropped
	-- Remove table
	EXECUTE 'DROP TABLE IF EXISTS '
		|| quote_ident(real_schema) || '.' ||
		quote_ident(table_name) || ' RESTRICT';

	RETURN
		real_schema || '.' ||
		table_name ||' dropped.';

END;
$$;


ALTER FUNCTION public.dropgeometrytable(catalog_name character varying, schema_name character varying, table_name character varying) OWNER TO postgres;

--
-- Name: enablelongtransactions(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.enablelongtransactions() RETURNS text
    LANGUAGE plpgsql
    AS $$
DECLARE
	"query" text;
	exists bool;
	rec RECORD;

BEGIN

	exists = 'f';
	FOR rec IN SELECT * FROM pg_class WHERE relname = 'authorization_table'
	LOOP
		exists = 't';
	END LOOP;

	IF NOT exists
	THEN
		"query" = 'CREATE TABLE authorization_table (
			toid oid, -- table oid
			rid text, -- row id
			expires timestamp,
			authid text
		)';
		EXECUTE "query";
	END IF;

	exists = 'f';
	FOR rec IN SELECT * FROM pg_class WHERE relname = 'authorized_tables'
	LOOP
		exists = 't';
	END LOOP;

	IF NOT exists THEN
		"query" = 'CREATE VIEW authorized_tables AS ' ||
			'SELECT ' ||
			'n.nspname as schema, ' ||
			'c.relname as table, trim(' ||
			quote_literal(chr(92) || '000') ||
			' from t.tgargs) as id_column ' ||
			'FROM pg_trigger t, pg_class c, pg_proc p ' ||
			', pg_namespace n ' ||
			'WHERE p.proname = ' || quote_literal('checkauthtrigger') ||
			' AND c.relnamespace = n.oid' ||
			' AND t.tgfoid = p.oid and t.tgrelid = c.oid';
		EXECUTE "query";
	END IF;

	RETURN 'Long transactions support enabled';
END;
$$;


ALTER FUNCTION public.enablelongtransactions() OWNER TO postgres;

--
-- Name: equals(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.equals(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_Equals';


ALTER FUNCTION public.equals(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: find_srid(character varying, character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.find_srid(character varying, character varying, character varying) RETURNS integer
    LANGUAGE plpgsql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
DECLARE
	schem varchar =  $1;
	tabl varchar = $2;
	sr int4;
BEGIN
-- if the table contains a . and the schema is empty
-- split the table into a schema and a table
-- otherwise drop through to default behavior
	IF ( schem = '' and strpos(tabl,'.') > 0 ) THEN
	 schem = substr(tabl,1,strpos(tabl,'.')-1);
	 tabl = substr(tabl,length(schem)+2);
	END IF;

	select SRID into sr from geometry_columns where (f_table_schema = schem or schem = '') and f_table_name = tabl and f_geometry_column = $3;
	IF NOT FOUND THEN
	   RAISE EXCEPTION 'find_srid() - could not find the corresponding SRID - is the geometry registered in the GEOMETRY_COLUMNS table?  Is there an uppercase/lowercase mismatch?';
	END IF;
	return sr;
END;
$_$;


ALTER FUNCTION public.find_srid(character varying, character varying, character varying) OWNER TO postgres;

--
-- Name: geog_brin_inclusion_add_value(internal, internal, internal, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geog_brin_inclusion_add_value(internal, internal, internal, internal) RETURNS boolean
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'geog_brin_inclusion_add_value';


ALTER FUNCTION public.geog_brin_inclusion_add_value(internal, internal, internal, internal) OWNER TO postgres;

--
-- Name: geography(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography(bytea) RETURNS public.geography
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_from_binary';


ALTER FUNCTION public.geography(bytea) OWNER TO postgres;

--
-- Name: geography(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography(public.geometry) RETURNS public.geography
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_from_geometry';


ALTER FUNCTION public.geography(public.geometry) OWNER TO postgres;

--
-- Name: geography(public.geography, integer, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography(public.geography, integer, boolean) RETURNS public.geography
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_enforce_typmod';


ALTER FUNCTION public.geography(public.geography, integer, boolean) OWNER TO postgres;

--
-- Name: geography_cmp(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_cmp(public.geography, public.geography) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_cmp';


ALTER FUNCTION public.geography_cmp(public.geography, public.geography) OWNER TO postgres;

--
-- Name: geography_distance_knn(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_distance_knn(public.geography, public.geography) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_distance_knn';


ALTER FUNCTION public.geography_distance_knn(public.geography, public.geography) OWNER TO postgres;

--
-- Name: geography_eq(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_eq(public.geography, public.geography) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_eq';


ALTER FUNCTION public.geography_eq(public.geography, public.geography) OWNER TO postgres;

--
-- Name: geography_ge(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_ge(public.geography, public.geography) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_ge';


ALTER FUNCTION public.geography_ge(public.geography, public.geography) OWNER TO postgres;

--
-- Name: geography_gist_compress(internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_gist_compress(internal) RETURNS internal
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'gserialized_gist_compress';


ALTER FUNCTION public.geography_gist_compress(internal) OWNER TO postgres;

--
-- Name: geography_gist_consistent(internal, public.geography, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_gist_consistent(internal, public.geography, integer) RETURNS boolean
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'gserialized_gist_consistent';


ALTER FUNCTION public.geography_gist_consistent(internal, public.geography, integer) OWNER TO postgres;

--
-- Name: geography_gist_decompress(internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_gist_decompress(internal) RETURNS internal
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'gserialized_gist_decompress';


ALTER FUNCTION public.geography_gist_decompress(internal) OWNER TO postgres;

--
-- Name: geography_gist_distance(internal, public.geography, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_gist_distance(internal, public.geography, integer) RETURNS double precision
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'gserialized_gist_geog_distance';


ALTER FUNCTION public.geography_gist_distance(internal, public.geography, integer) OWNER TO postgres;

--
-- Name: geography_gist_penalty(internal, internal, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_gist_penalty(internal, internal, internal) RETURNS internal
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'gserialized_gist_penalty';


ALTER FUNCTION public.geography_gist_penalty(internal, internal, internal) OWNER TO postgres;

--
-- Name: geography_gist_picksplit(internal, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_gist_picksplit(internal, internal) RETURNS internal
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'gserialized_gist_picksplit';


ALTER FUNCTION public.geography_gist_picksplit(internal, internal) OWNER TO postgres;

--
-- Name: geography_gist_same(public.box2d, public.box2d, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_gist_same(public.box2d, public.box2d, internal) RETURNS internal
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'gserialized_gist_same';


ALTER FUNCTION public.geography_gist_same(public.box2d, public.box2d, internal) OWNER TO postgres;

--
-- Name: geography_gist_union(bytea, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_gist_union(bytea, internal) RETURNS internal
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'gserialized_gist_union';


ALTER FUNCTION public.geography_gist_union(bytea, internal) OWNER TO postgres;

--
-- Name: geography_gt(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_gt(public.geography, public.geography) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_gt';


ALTER FUNCTION public.geography_gt(public.geography, public.geography) OWNER TO postgres;

--
-- Name: geography_le(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_le(public.geography, public.geography) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_le';


ALTER FUNCTION public.geography_le(public.geography, public.geography) OWNER TO postgres;

--
-- Name: geography_lt(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_lt(public.geography, public.geography) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_lt';


ALTER FUNCTION public.geography_lt(public.geography, public.geography) OWNER TO postgres;

--
-- Name: geography_overlaps(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geography_overlaps(public.geography, public.geography) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_overlaps';


ALTER FUNCTION public.geography_overlaps(public.geography, public.geography) OWNER TO postgres;

--
-- Name: geom2d_brin_inclusion_add_value(internal, internal, internal, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geom2d_brin_inclusion_add_value(internal, internal, internal, internal) RETURNS boolean
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'geom2d_brin_inclusion_add_value';


ALTER FUNCTION public.geom2d_brin_inclusion_add_value(internal, internal, internal, internal) OWNER TO postgres;

--
-- Name: geom3d_brin_inclusion_add_value(internal, internal, internal, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geom3d_brin_inclusion_add_value(internal, internal, internal, internal) RETURNS boolean
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'geom3d_brin_inclusion_add_value';


ALTER FUNCTION public.geom3d_brin_inclusion_add_value(internal, internal, internal, internal) OWNER TO postgres;

--
-- Name: geom4d_brin_inclusion_add_value(internal, internal, internal, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geom4d_brin_inclusion_add_value(internal, internal, internal, internal) RETURNS boolean
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'geom4d_brin_inclusion_add_value';


ALTER FUNCTION public.geom4d_brin_inclusion_add_value(internal, internal, internal, internal) OWNER TO postgres;

--
-- Name: geometry(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry(bytea) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_from_bytea';


ALTER FUNCTION public.geometry(bytea) OWNER TO postgres;

--
-- Name: geometry(path); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry(path) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'path_to_geometry';


ALTER FUNCTION public.geometry(path) OWNER TO postgres;

--
-- Name: geometry(point); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry(point) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'point_to_geometry';


ALTER FUNCTION public.geometry(point) OWNER TO postgres;

--
-- Name: geometry(polygon); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry(polygon) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'polygon_to_geometry';


ALTER FUNCTION public.geometry(polygon) OWNER TO postgres;

--
-- Name: geometry(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'parse_WKT_lwgeom';


ALTER FUNCTION public.geometry(text) OWNER TO postgres;

--
-- Name: geometry(public.box2d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry(public.box2d) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX2D_to_LWGEOM';


ALTER FUNCTION public.geometry(public.box2d) OWNER TO postgres;

--
-- Name: geometry(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry(public.box3d) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_to_LWGEOM';


ALTER FUNCTION public.geometry(public.box3d) OWNER TO postgres;

--
-- Name: geometry(public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry(public.geography) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geometry_from_geography';


ALTER FUNCTION public.geometry(public.geography) OWNER TO postgres;

--
-- Name: geometry(public.geometry, integer, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry(public.geometry, integer, boolean) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geometry_enforce_typmod';


ALTER FUNCTION public.geometry(public.geometry, integer, boolean) OWNER TO postgres;

--
-- Name: geometry_above(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_above(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_above_2d';


ALTER FUNCTION public.geometry_above(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_below(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_below(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_below_2d';


ALTER FUNCTION public.geometry_below(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_cmp(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_cmp(geom1 public.geometry, geom2 public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'lwgeom_cmp';


ALTER FUNCTION public.geometry_cmp(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_contains(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_contains(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_contains_2d';


ALTER FUNCTION public.geometry_contains(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_distance_box(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_distance_box(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_distance_box_2d';


ALTER FUNCTION public.geometry_distance_box(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_distance_centroid(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_distance_centroid(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'distance';


ALTER FUNCTION public.geometry_distance_centroid(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_distance_centroid_nd(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_distance_centroid_nd(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_distance_nd';


ALTER FUNCTION public.geometry_distance_centroid_nd(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: geometry_distance_cpa(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_distance_cpa(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_DistanceCPA';


ALTER FUNCTION public.geometry_distance_cpa(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: geometry_eq(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_eq(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'lwgeom_eq';


ALTER FUNCTION public.geometry_eq(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_ge(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_ge(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'lwgeom_ge';


ALTER FUNCTION public.geometry_ge(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_gist_compress_2d(internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_compress_2d(internal) RETURNS internal
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_compress_2d';


ALTER FUNCTION public.geometry_gist_compress_2d(internal) OWNER TO postgres;

--
-- Name: geometry_gist_compress_nd(internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_compress_nd(internal) RETURNS internal
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_compress';


ALTER FUNCTION public.geometry_gist_compress_nd(internal) OWNER TO postgres;

--
-- Name: geometry_gist_consistent_2d(internal, public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_consistent_2d(internal, public.geometry, integer) RETURNS boolean
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_consistent_2d';


ALTER FUNCTION public.geometry_gist_consistent_2d(internal, public.geometry, integer) OWNER TO postgres;

--
-- Name: geometry_gist_consistent_nd(internal, public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_consistent_nd(internal, public.geometry, integer) RETURNS boolean
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_consistent';


ALTER FUNCTION public.geometry_gist_consistent_nd(internal, public.geometry, integer) OWNER TO postgres;

--
-- Name: geometry_gist_decompress_2d(internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_decompress_2d(internal) RETURNS internal
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_decompress_2d';


ALTER FUNCTION public.geometry_gist_decompress_2d(internal) OWNER TO postgres;

--
-- Name: geometry_gist_decompress_nd(internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_decompress_nd(internal) RETURNS internal
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_decompress';


ALTER FUNCTION public.geometry_gist_decompress_nd(internal) OWNER TO postgres;

--
-- Name: geometry_gist_distance_2d(internal, public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_distance_2d(internal, public.geometry, integer) RETURNS double precision
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_distance_2d';


ALTER FUNCTION public.geometry_gist_distance_2d(internal, public.geometry, integer) OWNER TO postgres;

--
-- Name: geometry_gist_distance_nd(internal, public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_distance_nd(internal, public.geometry, integer) RETURNS double precision
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_distance';


ALTER FUNCTION public.geometry_gist_distance_nd(internal, public.geometry, integer) OWNER TO postgres;

--
-- Name: geometry_gist_penalty_2d(internal, internal, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_penalty_2d(internal, internal, internal) RETURNS internal
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_penalty_2d';


ALTER FUNCTION public.geometry_gist_penalty_2d(internal, internal, internal) OWNER TO postgres;

--
-- Name: geometry_gist_penalty_nd(internal, internal, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_penalty_nd(internal, internal, internal) RETURNS internal
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_penalty';


ALTER FUNCTION public.geometry_gist_penalty_nd(internal, internal, internal) OWNER TO postgres;

--
-- Name: geometry_gist_picksplit_2d(internal, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_picksplit_2d(internal, internal) RETURNS internal
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_picksplit_2d';


ALTER FUNCTION public.geometry_gist_picksplit_2d(internal, internal) OWNER TO postgres;

--
-- Name: geometry_gist_picksplit_nd(internal, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_picksplit_nd(internal, internal) RETURNS internal
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_picksplit';


ALTER FUNCTION public.geometry_gist_picksplit_nd(internal, internal) OWNER TO postgres;

--
-- Name: geometry_gist_same_2d(public.geometry, public.geometry, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_same_2d(geom1 public.geometry, geom2 public.geometry, internal) RETURNS internal
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_same_2d';


ALTER FUNCTION public.geometry_gist_same_2d(geom1 public.geometry, geom2 public.geometry, internal) OWNER TO postgres;

--
-- Name: geometry_gist_same_nd(public.geometry, public.geometry, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_same_nd(public.geometry, public.geometry, internal) RETURNS internal
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_same';


ALTER FUNCTION public.geometry_gist_same_nd(public.geometry, public.geometry, internal) OWNER TO postgres;

--
-- Name: geometry_gist_union_2d(bytea, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_union_2d(bytea, internal) RETURNS internal
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_union_2d';


ALTER FUNCTION public.geometry_gist_union_2d(bytea, internal) OWNER TO postgres;

--
-- Name: geometry_gist_union_nd(bytea, internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gist_union_nd(bytea, internal) RETURNS internal
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_union';


ALTER FUNCTION public.geometry_gist_union_nd(bytea, internal) OWNER TO postgres;

--
-- Name: geometry_gt(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_gt(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'lwgeom_gt';


ALTER FUNCTION public.geometry_gt(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_le(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_le(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'lwgeom_le';


ALTER FUNCTION public.geometry_le(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_left(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_left(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_left_2d';


ALTER FUNCTION public.geometry_left(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_lt(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_lt(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'lwgeom_lt';


ALTER FUNCTION public.geometry_lt(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_overabove(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_overabove(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_overabove_2d';


ALTER FUNCTION public.geometry_overabove(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_overbelow(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_overbelow(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_overbelow_2d';


ALTER FUNCTION public.geometry_overbelow(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_overlaps(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_overlaps(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_overlaps_2d';


ALTER FUNCTION public.geometry_overlaps(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_overlaps_nd(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_overlaps_nd(public.geometry, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_overlaps';


ALTER FUNCTION public.geometry_overlaps_nd(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: geometry_overleft(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_overleft(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_overleft_2d';


ALTER FUNCTION public.geometry_overleft(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_overright(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_overright(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_overright_2d';


ALTER FUNCTION public.geometry_overright(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_right(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_right(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_right_2d';


ALTER FUNCTION public.geometry_right(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_same(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_same(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_same_2d';


ALTER FUNCTION public.geometry_same(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometry_within(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometry_within(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_within_2d';


ALTER FUNCTION public.geometry_within(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: geometrytype(public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometrytype(public.geography) RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_getTYPE';


ALTER FUNCTION public.geometrytype(public.geography) OWNER TO postgres;

--
-- Name: geometrytype(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geometrytype(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_getTYPE';


ALTER FUNCTION public.geometrytype(public.geometry) OWNER TO postgres;

--
-- Name: geomfromewkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geomfromewkb(bytea) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOMFromEWKB';


ALTER FUNCTION public.geomfromewkb(bytea) OWNER TO postgres;

--
-- Name: geomfromewkt(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.geomfromewkt(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'parse_WKT_lwgeom';


ALTER FUNCTION public.geomfromewkt(text) OWNER TO postgres;

--
-- Name: geovis_clone_schema(text, text); Type: FUNCTION; Schema: public; Owner: xplore-admin
--

CREATE FUNCTION public.geovis_clone_schema(source_schema text, dest_schema text) RETURNS void
    LANGUAGE plpgsql
    AS $$

DECLARE
  object text;
  source_table text;
  dest_table text;
  default_ text;
  column_ text;
BEGIN
  FOR object IN
    SELECT sequence_name::text FROM information_schema.SEQUENCES WHERE sequence_schema = source_schema
  LOOP
    EXECUTE 'CREATE SEQUENCE ' || dest_schema || '.' || '"' || object || '"';
  END LOOP;

  FOR object IN
    SELECT TABLE_NAME::text FROM information_schema.TABLES WHERE table_schema = source_schema
  LOOP
    source_table := source_schema || '.' || '"' || object || '"';
    dest_table := dest_schema || '.' || '"' || object || '"';
    EXECUTE 'CREATE TABLE ' || dest_table || ' (LIKE ' || source_table || ' INCLUDING CONSTRAINTS INCLUDING INDEXES INCLUDING DEFAULTS)';

    FOR column_, default_ IN
      SELECT column_name::text, REPLACE(column_default::text, source_schema, dest_schema) FROM information_schema.COLUMNS WHERE table_schema = dest_schema AND TABLE_NAME = object AND column_default LIKE 'nextval(%' || source_schema || '%::regclass)'
    LOOP
      EXECUTE 'ALTER TABLE ' || dest_table || ' ALTER COLUMN ' || column_ || ' SET DEFAULT ' || default_;
    END LOOP;
  END LOOP;

END;

$$;


ALTER FUNCTION public.geovis_clone_schema(source_schema text, dest_schema text) OWNER TO "xplore-admin";

--
-- Name: get_proj4_from_srid(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.get_proj4_from_srid(integer) RETURNS text
    LANGUAGE plpgsql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
BEGIN
	RETURN proj4text::text FROM spatial_ref_sys WHERE srid= $1;
END;
$_$;


ALTER FUNCTION public.get_proj4_from_srid(integer) OWNER TO postgres;

--
-- Name: gettransactionid(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.gettransactionid() RETURNS xid
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'getTransactionID';


ALTER FUNCTION public.gettransactionid() OWNER TO postgres;

--
-- Name: gserialized_gist_joinsel_2d(internal, oid, internal, smallint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.gserialized_gist_joinsel_2d(internal, oid, internal, smallint) RETURNS double precision
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_joinsel_2d';


ALTER FUNCTION public.gserialized_gist_joinsel_2d(internal, oid, internal, smallint) OWNER TO postgres;

--
-- Name: gserialized_gist_joinsel_nd(internal, oid, internal, smallint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.gserialized_gist_joinsel_nd(internal, oid, internal, smallint) RETURNS double precision
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_joinsel_nd';


ALTER FUNCTION public.gserialized_gist_joinsel_nd(internal, oid, internal, smallint) OWNER TO postgres;

--
-- Name: gserialized_gist_sel_2d(internal, oid, internal, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.gserialized_gist_sel_2d(internal, oid, internal, integer) RETURNS double precision
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_sel_2d';


ALTER FUNCTION public.gserialized_gist_sel_2d(internal, oid, internal, integer) OWNER TO postgres;

--
-- Name: gserialized_gist_sel_nd(internal, oid, internal, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.gserialized_gist_sel_nd(internal, oid, internal, integer) RETURNS double precision
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'gserialized_gist_sel_nd';


ALTER FUNCTION public.gserialized_gist_sel_nd(internal, oid, internal, integer) OWNER TO postgres;

--
-- Name: is_contained_2d(public.box2df, public.box2df); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.is_contained_2d(public.box2df, public.box2df) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.4', 'gserialized_contains_box2df_box2df_2d';


ALTER FUNCTION public.is_contained_2d(public.box2df, public.box2df) OWNER TO postgres;

--
-- Name: is_contained_2d(public.box2df, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.is_contained_2d(public.box2df, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.4', 'gserialized_within_box2df_geom_2d';


ALTER FUNCTION public.is_contained_2d(public.box2df, public.geometry) OWNER TO postgres;

--
-- Name: is_contained_2d(public.geometry, public.box2df); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.is_contained_2d(public.geometry, public.box2df) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT $2 OPERATOR(~) $1;$_$;


ALTER FUNCTION public.is_contained_2d(public.geometry, public.box2df) OWNER TO postgres;

--
-- Name: lockrow(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.lockrow(text, text, text) RETURNS integer
    LANGUAGE sql STRICT
    AS $_$ SELECT LockRow(current_schema(), $1, $2, $3, now()::timestamp+'1:00'); $_$;


ALTER FUNCTION public.lockrow(text, text, text) OWNER TO postgres;

--
-- Name: lockrow(text, text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.lockrow(text, text, text, text) RETURNS integer
    LANGUAGE sql STRICT
    AS $_$ SELECT LockRow($1, $2, $3, $4, now()::timestamp+'1:00'); $_$;


ALTER FUNCTION public.lockrow(text, text, text, text) OWNER TO postgres;

--
-- Name: lockrow(text, text, text, timestamp without time zone); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.lockrow(text, text, text, timestamp without time zone) RETURNS integer
    LANGUAGE sql STRICT
    AS $_$ SELECT LockRow(current_schema(), $1, $2, $3, $4); $_$;


ALTER FUNCTION public.lockrow(text, text, text, timestamp without time zone) OWNER TO postgres;

--
-- Name: lockrow(text, text, text, text, timestamp without time zone); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.lockrow(text, text, text, text, timestamp without time zone) RETURNS integer
    LANGUAGE plpgsql STRICT
    AS $_$
DECLARE
	myschema alias for $1;
	mytable alias for $2;
	myrid   alias for $3;
	authid alias for $4;
	expires alias for $5;
	ret int;
	mytoid oid;
	myrec RECORD;

BEGIN

	IF NOT LongTransactionsEnabled() THEN
		RAISE EXCEPTION 'Long transaction support disabled, use EnableLongTransaction() to enable.';
	END IF;

	EXECUTE 'DELETE FROM authorization_table WHERE expires < now()';

	SELECT c.oid INTO mytoid FROM pg_class c, pg_namespace n
		WHERE c.relname = mytable
		AND c.relnamespace = n.oid
		AND n.nspname = myschema;

	-- RAISE NOTICE 'toid: %', mytoid;

	FOR myrec IN SELECT * FROM authorization_table WHERE
		toid = mytoid AND rid = myrid
	LOOP
		IF myrec.authid != authid THEN
			RETURN 0;
		ELSE
			RETURN 1;
		END IF;
	END LOOP;

	EXECUTE 'INSERT INTO authorization_table VALUES ('||
		quote_literal(mytoid::text)||','||quote_literal(myrid)||
		','||quote_literal(expires::text)||
		','||quote_literal(authid) ||')';

	GET DIAGNOSTICS ret = ROW_COUNT;

	RETURN ret;
END;
$_$;


ALTER FUNCTION public.lockrow(text, text, text, text, timestamp without time zone) OWNER TO postgres;

--
-- Name: longtransactionsenabled(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.longtransactionsenabled() RETURNS boolean
    LANGUAGE plpgsql
    AS $$
DECLARE
	rec RECORD;
BEGIN
	FOR rec IN SELECT oid FROM pg_class WHERE relname = 'authorized_tables'
	LOOP
		return 't';
	END LOOP;
	return 'f';
END;
$$;


ALTER FUNCTION public.longtransactionsenabled() OWNER TO postgres;

--
-- Name: overlaps_2d(public.box2df, public.box2df); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.overlaps_2d(public.box2df, public.box2df) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.4', 'gserialized_contains_box2df_box2df_2d';


ALTER FUNCTION public.overlaps_2d(public.box2df, public.box2df) OWNER TO postgres;

--
-- Name: overlaps_2d(public.box2df, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.overlaps_2d(public.box2df, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.4', 'gserialized_overlaps_box2df_geom_2d';


ALTER FUNCTION public.overlaps_2d(public.box2df, public.geometry) OWNER TO postgres;

--
-- Name: overlaps_2d(public.geometry, public.box2df); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.overlaps_2d(public.geometry, public.box2df) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT $2 OPERATOR(&&) $1;$_$;


ALTER FUNCTION public.overlaps_2d(public.geometry, public.box2df) OWNER TO postgres;

--
-- Name: overlaps_geog(public.geography, public.gidx); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.overlaps_geog(public.geography, public.gidx) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT $2 OPERATOR(&&) $1;$_$;


ALTER FUNCTION public.overlaps_geog(public.geography, public.gidx) OWNER TO postgres;

--
-- Name: overlaps_geog(public.gidx, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.overlaps_geog(public.gidx, public.geography) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.4', 'gserialized_gidx_geog_overlaps';


ALTER FUNCTION public.overlaps_geog(public.gidx, public.geography) OWNER TO postgres;

--
-- Name: overlaps_geog(public.gidx, public.gidx); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.overlaps_geog(public.gidx, public.gidx) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.4', 'gserialized_gidx_gidx_overlaps';


ALTER FUNCTION public.overlaps_geog(public.gidx, public.gidx) OWNER TO postgres;

--
-- Name: overlaps_nd(public.geometry, public.gidx); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.overlaps_nd(public.geometry, public.gidx) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT $2 OPERATOR(&&&) $1;$_$;


ALTER FUNCTION public.overlaps_nd(public.geometry, public.gidx) OWNER TO postgres;

--
-- Name: overlaps_nd(public.gidx, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.overlaps_nd(public.gidx, public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.4', 'gserialized_gidx_geom_overlaps';


ALTER FUNCTION public.overlaps_nd(public.gidx, public.geometry) OWNER TO postgres;

--
-- Name: overlaps_nd(public.gidx, public.gidx); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.overlaps_nd(public.gidx, public.gidx) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.4', 'gserialized_gidx_gidx_overlaps';


ALTER FUNCTION public.overlaps_nd(public.gidx, public.gidx) OWNER TO postgres;

--
-- Name: path(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.path(public.geometry) RETURNS path
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geometry_to_path';


ALTER FUNCTION public.path(public.geometry) OWNER TO postgres;

--
-- Name: pgis_asgeobuf_finalfn(internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_asgeobuf_finalfn(internal) RETURNS bytea
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_asgeobuf_finalfn';


ALTER FUNCTION public.pgis_asgeobuf_finalfn(internal) OWNER TO postgres;

--
-- Name: pgis_asgeobuf_transfn(internal, anyelement); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_asgeobuf_transfn(internal, anyelement) RETURNS internal
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_asgeobuf_transfn';


ALTER FUNCTION public.pgis_asgeobuf_transfn(internal, anyelement) OWNER TO postgres;

--
-- Name: pgis_asgeobuf_transfn(internal, anyelement, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_asgeobuf_transfn(internal, anyelement, text) RETURNS internal
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_asgeobuf_transfn';


ALTER FUNCTION public.pgis_asgeobuf_transfn(internal, anyelement, text) OWNER TO postgres;

--
-- Name: pgis_asmvt_finalfn(internal); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_asmvt_finalfn(internal) RETURNS bytea
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_asmvt_finalfn';


ALTER FUNCTION public.pgis_asmvt_finalfn(internal) OWNER TO postgres;

--
-- Name: pgis_asmvt_transfn(internal, anyelement); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_asmvt_transfn(internal, anyelement) RETURNS internal
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_asmvt_transfn';


ALTER FUNCTION public.pgis_asmvt_transfn(internal, anyelement) OWNER TO postgres;

--
-- Name: pgis_asmvt_transfn(internal, anyelement, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_asmvt_transfn(internal, anyelement, text) RETURNS internal
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_asmvt_transfn';


ALTER FUNCTION public.pgis_asmvt_transfn(internal, anyelement, text) OWNER TO postgres;

--
-- Name: pgis_asmvt_transfn(internal, anyelement, text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_asmvt_transfn(internal, anyelement, text, integer) RETURNS internal
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_asmvt_transfn';


ALTER FUNCTION public.pgis_asmvt_transfn(internal, anyelement, text, integer) OWNER TO postgres;

--
-- Name: pgis_asmvt_transfn(internal, anyelement, text, integer, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_asmvt_transfn(internal, anyelement, text, integer, text) RETURNS internal
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_asmvt_transfn';


ALTER FUNCTION public.pgis_asmvt_transfn(internal, anyelement, text, integer, text) OWNER TO postgres;

--
-- Name: pgis_geometry_accum_finalfn(public.pgis_abs); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_geometry_accum_finalfn(public.pgis_abs) RETURNS public.geometry[]
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_geometry_accum_finalfn';


ALTER FUNCTION public.pgis_geometry_accum_finalfn(public.pgis_abs) OWNER TO postgres;

--
-- Name: pgis_geometry_accum_transfn(public.pgis_abs, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_geometry_accum_transfn(public.pgis_abs, public.geometry) RETURNS public.pgis_abs
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_geometry_accum_transfn';


ALTER FUNCTION public.pgis_geometry_accum_transfn(public.pgis_abs, public.geometry) OWNER TO postgres;

--
-- Name: pgis_geometry_accum_transfn(public.pgis_abs, public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_geometry_accum_transfn(public.pgis_abs, public.geometry, double precision) RETURNS public.pgis_abs
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_geometry_accum_transfn';


ALTER FUNCTION public.pgis_geometry_accum_transfn(public.pgis_abs, public.geometry, double precision) OWNER TO postgres;

--
-- Name: pgis_geometry_accum_transfn(public.pgis_abs, public.geometry, double precision, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_geometry_accum_transfn(public.pgis_abs, public.geometry, double precision, integer) RETURNS public.pgis_abs
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_geometry_accum_transfn';


ALTER FUNCTION public.pgis_geometry_accum_transfn(public.pgis_abs, public.geometry, double precision, integer) OWNER TO postgres;

--
-- Name: pgis_geometry_clusterintersecting_finalfn(public.pgis_abs); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_geometry_clusterintersecting_finalfn(public.pgis_abs) RETURNS public.geometry[]
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_geometry_clusterintersecting_finalfn';


ALTER FUNCTION public.pgis_geometry_clusterintersecting_finalfn(public.pgis_abs) OWNER TO postgres;

--
-- Name: pgis_geometry_clusterwithin_finalfn(public.pgis_abs); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_geometry_clusterwithin_finalfn(public.pgis_abs) RETURNS public.geometry[]
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_geometry_clusterwithin_finalfn';


ALTER FUNCTION public.pgis_geometry_clusterwithin_finalfn(public.pgis_abs) OWNER TO postgres;

--
-- Name: pgis_geometry_collect_finalfn(public.pgis_abs); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_geometry_collect_finalfn(public.pgis_abs) RETURNS public.geometry
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_geometry_collect_finalfn';


ALTER FUNCTION public.pgis_geometry_collect_finalfn(public.pgis_abs) OWNER TO postgres;

--
-- Name: pgis_geometry_makeline_finalfn(public.pgis_abs); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_geometry_makeline_finalfn(public.pgis_abs) RETURNS public.geometry
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_geometry_makeline_finalfn';


ALTER FUNCTION public.pgis_geometry_makeline_finalfn(public.pgis_abs) OWNER TO postgres;

--
-- Name: pgis_geometry_polygonize_finalfn(public.pgis_abs); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_geometry_polygonize_finalfn(public.pgis_abs) RETURNS public.geometry
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_geometry_polygonize_finalfn';


ALTER FUNCTION public.pgis_geometry_polygonize_finalfn(public.pgis_abs) OWNER TO postgres;

--
-- Name: pgis_geometry_union_finalfn(public.pgis_abs); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.pgis_geometry_union_finalfn(public.pgis_abs) RETURNS public.geometry
    LANGUAGE c PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_geometry_union_finalfn';


ALTER FUNCTION public.pgis_geometry_union_finalfn(public.pgis_abs) OWNER TO postgres;

--
-- Name: point(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.point(public.geometry) RETURNS point
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geometry_to_point';


ALTER FUNCTION public.point(public.geometry) OWNER TO postgres;

--
-- Name: polygon(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.polygon(public.geometry) RETURNS polygon
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geometry_to_polygon';


ALTER FUNCTION public.polygon(public.geometry) OWNER TO postgres;

--
-- Name: populate_geometry_columns(boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.populate_geometry_columns(use_typmod boolean DEFAULT true) RETURNS text
    LANGUAGE plpgsql
    AS $$
DECLARE
	inserted    integer;
	oldcount    integer;
	probed      integer;
	stale       integer;
	gcs         RECORD;
	gc          RECORD;
	gsrid       integer;
	gndims      integer;
	gtype       text;
	query       text;
	gc_is_valid boolean;

BEGIN
	SELECT count(*) INTO oldcount FROM geometry_columns;
	inserted := 0;

	-- Count the number of geometry columns in all tables and views
	SELECT count(DISTINCT c.oid) INTO probed
	FROM pg_class c,
		 pg_attribute a,
		 pg_type t,
		 pg_namespace n
	WHERE c.relkind IN('r','v','f')
		AND t.typname = 'geometry'
		AND a.attisdropped = false
		AND a.atttypid = t.oid
		AND a.attrelid = c.oid
		AND c.relnamespace = n.oid
		AND n.nspname NOT ILIKE 'pg_temp%' AND c.relname != 'raster_columns' ;

	-- Iterate through all non-dropped geometry columns
	RAISE DEBUG 'Processing Tables.....';

	FOR gcs IN
	SELECT DISTINCT ON (c.oid) c.oid, n.nspname, c.relname
		FROM pg_class c,
			 pg_attribute a,
			 pg_type t,
			 pg_namespace n
		WHERE c.relkind IN( 'r', 'f')
		AND t.typname = 'geometry'
		AND a.attisdropped = false
		AND a.atttypid = t.oid
		AND a.attrelid = c.oid
		AND c.relnamespace = n.oid
		AND n.nspname NOT ILIKE 'pg_temp%' AND c.relname != 'raster_columns'
	LOOP

		inserted := inserted + populate_geometry_columns(gcs.oid, use_typmod);
	END LOOP;

	IF oldcount > inserted THEN
	    stale = oldcount-inserted;
	ELSE
	    stale = 0;
	END IF;

	RETURN 'probed:' ||probed|| ' inserted:'||inserted;
END

$$;


ALTER FUNCTION public.populate_geometry_columns(use_typmod boolean) OWNER TO postgres;

--
-- Name: populate_geometry_columns(oid, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.populate_geometry_columns(tbl_oid oid, use_typmod boolean DEFAULT true) RETURNS integer
    LANGUAGE plpgsql
    AS $$
DECLARE
	gcs         RECORD;
	gc          RECORD;
	gc_old      RECORD;
	gsrid       integer;
	gndims      integer;
	gtype       text;
	query       text;
	gc_is_valid boolean;
	inserted    integer;
	constraint_successful boolean := false;

BEGIN
	inserted := 0;

	-- Iterate through all geometry columns in this table
	FOR gcs IN
	SELECT n.nspname, c.relname, a.attname
		FROM pg_class c,
			 pg_attribute a,
			 pg_type t,
			 pg_namespace n
		WHERE c.relkind IN('r', 'f')
		AND t.typname = 'geometry'
		AND a.attisdropped = false
		AND a.atttypid = t.oid
		AND a.attrelid = c.oid
		AND c.relnamespace = n.oid
		AND n.nspname NOT ILIKE 'pg_temp%'
		AND c.oid = tbl_oid
	LOOP

        RAISE DEBUG 'Processing column %.%.%', gcs.nspname, gcs.relname, gcs.attname;

        gc_is_valid := true;
        -- Find the srid, coord_dimension, and type of current geometry
        -- in geometry_columns -- which is now a view

        SELECT type, srid, coord_dimension INTO gc_old
            FROM geometry_columns
            WHERE f_table_schema = gcs.nspname AND f_table_name = gcs.relname AND f_geometry_column = gcs.attname;

        IF upper(gc_old.type) = 'GEOMETRY' THEN
        -- This is an unconstrained geometry we need to do something
        -- We need to figure out what to set the type by inspecting the data
            EXECUTE 'SELECT ST_srid(' || quote_ident(gcs.attname) || ') As srid, GeometryType(' || quote_ident(gcs.attname) || ') As type, ST_NDims(' || quote_ident(gcs.attname) || ') As dims ' ||
                     ' FROM ONLY ' || quote_ident(gcs.nspname) || '.' || quote_ident(gcs.relname) ||
                     ' WHERE ' || quote_ident(gcs.attname) || ' IS NOT NULL LIMIT 1;'
                INTO gc;
            IF gc IS NULL THEN -- there is no data so we can not determine geometry type
            	RAISE WARNING 'No data in table %.%, so no information to determine geometry type and srid', gcs.nspname, gcs.relname;
            	RETURN 0;
            END IF;
            gsrid := gc.srid; gtype := gc.type; gndims := gc.dims;

            IF use_typmod THEN
                BEGIN
                    EXECUTE 'ALTER TABLE ' || quote_ident(gcs.nspname) || '.' || quote_ident(gcs.relname) || ' ALTER COLUMN ' || quote_ident(gcs.attname) ||
                        ' TYPE geometry(' || postgis_type_name(gtype, gndims, true) || ', ' || gsrid::text  || ') ';
                    inserted := inserted + 1;
                EXCEPTION
                        WHEN invalid_parameter_value OR feature_not_supported THEN
                        RAISE WARNING 'Could not convert ''%'' in ''%.%'' to use typmod with srid %, type %: %', quote_ident(gcs.attname), quote_ident(gcs.nspname), quote_ident(gcs.relname), gsrid, postgis_type_name(gtype, gndims, true), SQLERRM;
                            gc_is_valid := false;
                END;

            ELSE
                -- Try to apply srid check to column
            	constraint_successful = false;
                IF (gsrid > 0 AND postgis_constraint_srid(gcs.nspname, gcs.relname,gcs.attname) IS NULL ) THEN
                    BEGIN
                        EXECUTE 'ALTER TABLE ONLY ' || quote_ident(gcs.nspname) || '.' || quote_ident(gcs.relname) ||
                                 ' ADD CONSTRAINT ' || quote_ident('enforce_srid_' || gcs.attname) ||
                                 ' CHECK (ST_srid(' || quote_ident(gcs.attname) || ') = ' || gsrid || ')';
                        constraint_successful := true;
                    EXCEPTION
                        WHEN check_violation THEN
                            RAISE WARNING 'Not inserting ''%'' in ''%.%'' into geometry_columns: could not apply constraint CHECK (st_srid(%) = %)', quote_ident(gcs.attname), quote_ident(gcs.nspname), quote_ident(gcs.relname), quote_ident(gcs.attname), gsrid;
                            gc_is_valid := false;
                    END;
                END IF;

                -- Try to apply ndims check to column
                IF (gndims IS NOT NULL AND postgis_constraint_dims(gcs.nspname, gcs.relname,gcs.attname) IS NULL ) THEN
                    BEGIN
                        EXECUTE 'ALTER TABLE ONLY ' || quote_ident(gcs.nspname) || '.' || quote_ident(gcs.relname) || '
                                 ADD CONSTRAINT ' || quote_ident('enforce_dims_' || gcs.attname) || '
                                 CHECK (st_ndims(' || quote_ident(gcs.attname) || ') = '||gndims||')';
                        constraint_successful := true;
                    EXCEPTION
                        WHEN check_violation THEN
                            RAISE WARNING 'Not inserting ''%'' in ''%.%'' into geometry_columns: could not apply constraint CHECK (st_ndims(%) = %)', quote_ident(gcs.attname), quote_ident(gcs.nspname), quote_ident(gcs.relname), quote_ident(gcs.attname), gndims;
                            gc_is_valid := false;
                    END;
                END IF;

                -- Try to apply geometrytype check to column
                IF (gtype IS NOT NULL AND postgis_constraint_type(gcs.nspname, gcs.relname,gcs.attname) IS NULL ) THEN
                    BEGIN
                        EXECUTE 'ALTER TABLE ONLY ' || quote_ident(gcs.nspname) || '.' || quote_ident(gcs.relname) || '
                        ADD CONSTRAINT ' || quote_ident('enforce_geotype_' || gcs.attname) || '
                        CHECK (geometrytype(' || quote_ident(gcs.attname) || ') = ' || quote_literal(gtype) || ')';
                        constraint_successful := true;
                    EXCEPTION
                        WHEN check_violation THEN
                            -- No geometry check can be applied. This column contains a number of geometry types.
                            RAISE WARNING 'Could not add geometry type check (%) to table column: %.%.%', gtype, quote_ident(gcs.nspname),quote_ident(gcs.relname),quote_ident(gcs.attname);
                    END;
                END IF;
                 --only count if we were successful in applying at least one constraint
                IF constraint_successful THEN
                	inserted := inserted + 1;
                END IF;
            END IF;
	    END IF;

	END LOOP;

	RETURN inserted;
END

$$;


ALTER FUNCTION public.populate_geometry_columns(tbl_oid oid, use_typmod boolean) OWNER TO postgres;

--
-- Name: postgis_addbbox(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_addbbox(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_addBBOX';


ALTER FUNCTION public.postgis_addbbox(public.geometry) OWNER TO postgres;

--
-- Name: postgis_cache_bbox(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_cache_bbox() RETURNS trigger
    LANGUAGE c
    AS '$libdir/postgis-2.4', 'cache_bbox';


ALTER FUNCTION public.postgis_cache_bbox() OWNER TO postgres;

--
-- Name: postgis_constraint_dims(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_constraint_dims(geomschema text, geomtable text, geomcolumn text) RETURNS integer
    LANGUAGE sql STABLE STRICT PARALLEL SAFE
    AS $_$
SELECT  replace(split_part(s.consrc, ' = ', 2), ')', '')::integer
		 FROM pg_class c, pg_namespace n, pg_attribute a, pg_constraint s
		 WHERE n.nspname = $1
		 AND c.relname = $2
		 AND a.attname = $3
		 AND a.attrelid = c.oid
		 AND s.connamespace = n.oid
		 AND s.conrelid = c.oid
		 AND a.attnum = ANY (s.conkey)
		 AND s.consrc LIKE '%ndims(% = %';
$_$;


ALTER FUNCTION public.postgis_constraint_dims(geomschema text, geomtable text, geomcolumn text) OWNER TO postgres;

--
-- Name: postgis_constraint_srid(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_constraint_srid(geomschema text, geomtable text, geomcolumn text) RETURNS integer
    LANGUAGE sql STABLE STRICT PARALLEL SAFE
    AS $_$
SELECT replace(replace(split_part(s.consrc, ' = ', 2), ')', ''), '(', '')::integer
		 FROM pg_class c, pg_namespace n, pg_attribute a, pg_constraint s
		 WHERE n.nspname = $1
		 AND c.relname = $2
		 AND a.attname = $3
		 AND a.attrelid = c.oid
		 AND s.connamespace = n.oid
		 AND s.conrelid = c.oid
		 AND a.attnum = ANY (s.conkey)
		 AND s.consrc LIKE '%srid(% = %';
$_$;


ALTER FUNCTION public.postgis_constraint_srid(geomschema text, geomtable text, geomcolumn text) OWNER TO postgres;

--
-- Name: postgis_constraint_type(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_constraint_type(geomschema text, geomtable text, geomcolumn text) RETURNS character varying
    LANGUAGE sql STABLE STRICT PARALLEL SAFE
    AS $_$
SELECT  replace(split_part(s.consrc, '''', 2), ')', '')::varchar
		 FROM pg_class c, pg_namespace n, pg_attribute a, pg_constraint s
		 WHERE n.nspname = $1
		 AND c.relname = $2
		 AND a.attname = $3
		 AND a.attrelid = c.oid
		 AND s.connamespace = n.oid
		 AND s.conrelid = c.oid
		 AND a.attnum = ANY (s.conkey)
		 AND s.consrc LIKE '%geometrytype(% = %';
$_$;


ALTER FUNCTION public.postgis_constraint_type(geomschema text, geomtable text, geomcolumn text) OWNER TO postgres;

--
-- Name: postgis_dropbbox(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_dropbbox(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_dropBBOX';


ALTER FUNCTION public.postgis_dropbbox(public.geometry) OWNER TO postgres;

--
-- Name: postgis_full_version(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_full_version() RETURNS text
    LANGUAGE plpgsql IMMUTABLE
    AS $$
DECLARE
	libver text;
	svnver text;
	projver text;
	geosver text;
	sfcgalver text;
	cgalver text;
	gdalver text;
	libxmlver text;
	liblwgeomver text;
	dbproc text;
	relproc text;
	fullver text;
	rast_lib_ver text;
	rast_scr_ver text;
	topo_scr_ver text;
	json_lib_ver text;
	protobuf_lib_ver text;
	sfcgal_lib_ver text;
	sfcgal_scr_ver text;
	pgsql_scr_ver text;
	pgsql_ver text;
BEGIN
	SELECT postgis_lib_version() INTO libver;
	SELECT postgis_proj_version() INTO projver;
	SELECT postgis_geos_version() INTO geosver;
	SELECT postgis_libjson_version() INTO json_lib_ver;
	SELECT postgis_libprotobuf_version() INTO protobuf_lib_ver;
	SELECT _postgis_scripts_pgsql_version() INTO pgsql_scr_ver;
	SELECT _postgis_pgsql_version() INTO pgsql_ver;
	BEGIN
		SELECT postgis_gdal_version() INTO gdalver;
	EXCEPTION
		WHEN undefined_function THEN
			gdalver := NULL;
			RAISE NOTICE 'Function postgis_gdal_version() not found.  Is raster support enabled and rtpostgis.sql installed?';
	END;
	BEGIN
		SELECT postgis_sfcgal_version() INTO sfcgalver;
    BEGIN
      SELECT postgis_sfcgal_scripts_installed() INTO sfcgal_scr_ver;
    EXCEPTION
      WHEN undefined_function THEN
        sfcgal_scr_ver := 'missing';
    END;
	EXCEPTION
		WHEN undefined_function THEN
			sfcgalver := NULL;
	END;
	SELECT postgis_liblwgeom_version() INTO liblwgeomver;
	SELECT postgis_libxml_version() INTO libxmlver;
	SELECT postgis_scripts_installed() INTO dbproc;
	SELECT postgis_scripts_released() INTO relproc;
	select postgis_svn_version() INTO svnver;
	BEGIN
		SELECT topology.postgis_topology_scripts_installed() INTO topo_scr_ver;
	EXCEPTION
		WHEN undefined_function OR invalid_schema_name THEN
			topo_scr_ver := NULL;
			RAISE DEBUG 'Function postgis_topology_scripts_installed() not found. Is topology support enabled and topology.sql installed?';
		WHEN insufficient_privilege THEN
			RAISE NOTICE 'Topology support cannot be inspected. Is current user granted USAGE on schema "topology" ?';
		WHEN OTHERS THEN
			RAISE NOTICE 'Function postgis_topology_scripts_installed() could not be called: % (%)', SQLERRM, SQLSTATE;
	END;

	BEGIN
		SELECT postgis_raster_scripts_installed() INTO rast_scr_ver;
	EXCEPTION
		WHEN undefined_function THEN
			rast_scr_ver := NULL;
			RAISE NOTICE 'Function postgis_raster_scripts_installed() not found. Is raster support enabled and rtpostgis.sql installed?';
	END;

	BEGIN
		SELECT postgis_raster_lib_version() INTO rast_lib_ver;
	EXCEPTION
		WHEN undefined_function THEN
			rast_lib_ver := NULL;
			RAISE NOTICE 'Function postgis_raster_lib_version() not found. Is raster support enabled and rtpostgis.sql installed?';
	END;

	fullver = 'POSTGIS="' || libver;

	IF  svnver IS NOT NULL THEN
		fullver = fullver || ' r' || svnver;
	END IF;

	fullver = fullver || '"';

	IF liblwgeomver != relproc THEN
		fullver = fullver || ' (liblwgeom version mismatch: "' || liblwgeomver || '")';
	END IF;

	fullver = fullver || ' PGSQL="' || pgsql_scr_ver || '"';
	IF pgsql_scr_ver != pgsql_ver THEN
		fullver = fullver || ' (procs need upgrade for use with "' || pgsql_ver || '")';
	END IF;

	IF  geosver IS NOT NULL THEN
		fullver = fullver || ' GEOS="' || geosver || '"';
	END IF;

	IF  sfcgalver IS NOT NULL THEN
		fullver = fullver || ' SFCGAL="' || sfcgalver || '"';
	END IF;

	IF  projver IS NOT NULL THEN
		fullver = fullver || ' PROJ="' || projver || '"';
	END IF;

	IF  gdalver IS NOT NULL THEN
		fullver = fullver || ' GDAL="' || gdalver || '"';
	END IF;

	IF  libxmlver IS NOT NULL THEN
		fullver = fullver || ' LIBXML="' || libxmlver || '"';
	END IF;

	IF json_lib_ver IS NOT NULL THEN
		fullver = fullver || ' LIBJSON="' || json_lib_ver || '"';
	END IF;

	IF protobuf_lib_ver IS NOT NULL THEN
		fullver = fullver || ' LIBPROTOBUF="' || protobuf_lib_ver || '"';
	END IF;

	-- fullver = fullver || ' DBPROC="' || dbproc || '"';
	-- fullver = fullver || ' RELPROC="' || relproc || '"';

	IF dbproc != relproc THEN
		fullver = fullver || ' (core procs from "' || dbproc || '" need upgrade)';
	END IF;

	IF topo_scr_ver IS NOT NULL THEN
		fullver = fullver || ' TOPOLOGY';
		IF topo_scr_ver != relproc THEN
			fullver = fullver || ' (topology procs from "' || topo_scr_ver || '" need upgrade)';
		END IF;
	END IF;

	IF rast_lib_ver IS NOT NULL THEN
		fullver = fullver || ' RASTER';
		IF rast_lib_ver != relproc THEN
			fullver = fullver || ' (raster lib from "' || rast_lib_ver || '" need upgrade)';
		END IF;
	END IF;

	IF rast_scr_ver IS NOT NULL AND rast_scr_ver != relproc THEN
		fullver = fullver || ' (raster procs from "' || rast_scr_ver || '" need upgrade)';
	END IF;

	IF sfcgal_scr_ver IS NOT NULL AND sfcgal_scr_ver != relproc THEN
    fullver = fullver || ' (sfcgal procs from "' || sfcgal_scr_ver || '" need upgrade)';
	END IF;

	RETURN fullver;
END
$$;


ALTER FUNCTION public.postgis_full_version() OWNER TO postgres;

--
-- Name: postgis_geos_version(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_geos_version() RETURNS text
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.4', 'postgis_geos_version';


ALTER FUNCTION public.postgis_geos_version() OWNER TO postgres;

--
-- Name: postgis_getbbox(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_getbbox(public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_to_BOX2DF';


ALTER FUNCTION public.postgis_getbbox(public.geometry) OWNER TO postgres;

--
-- Name: postgis_hasbbox(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_hasbbox(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_hasBBOX';


ALTER FUNCTION public.postgis_hasbbox(public.geometry) OWNER TO postgres;

--
-- Name: postgis_lib_build_date(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_lib_build_date() RETURNS text
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.4', 'postgis_lib_build_date';


ALTER FUNCTION public.postgis_lib_build_date() OWNER TO postgres;

--
-- Name: postgis_lib_version(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_lib_version() RETURNS text
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.4', 'postgis_lib_version';


ALTER FUNCTION public.postgis_lib_version() OWNER TO postgres;

--
-- Name: postgis_libjson_version(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_libjson_version() RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'postgis_libjson_version';


ALTER FUNCTION public.postgis_libjson_version() OWNER TO postgres;

--
-- Name: postgis_liblwgeom_version(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_liblwgeom_version() RETURNS text
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.4', 'postgis_liblwgeom_version';


ALTER FUNCTION public.postgis_liblwgeom_version() OWNER TO postgres;

--
-- Name: postgis_libprotobuf_version(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_libprotobuf_version() RETURNS text
    LANGUAGE c IMMUTABLE STRICT
    AS '$libdir/postgis-2.4', 'postgis_libprotobuf_version';


ALTER FUNCTION public.postgis_libprotobuf_version() OWNER TO postgres;

--
-- Name: postgis_libxml_version(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_libxml_version() RETURNS text
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.4', 'postgis_libxml_version';


ALTER FUNCTION public.postgis_libxml_version() OWNER TO postgres;

--
-- Name: postgis_noop(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_noop(public.geometry) RETURNS public.geometry
    LANGUAGE c STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_noop';


ALTER FUNCTION public.postgis_noop(public.geometry) OWNER TO postgres;

--
-- Name: postgis_proj_version(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_proj_version() RETURNS text
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.4', 'postgis_proj_version';


ALTER FUNCTION public.postgis_proj_version() OWNER TO postgres;

--
-- Name: postgis_scripts_build_date(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_scripts_build_date() RETURNS text
    LANGUAGE sql IMMUTABLE
    AS $$SELECT '2018-04-12 11:00:43'::text AS version$$;


ALTER FUNCTION public.postgis_scripts_build_date() OWNER TO postgres;

--
-- Name: postgis_scripts_installed(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_scripts_installed() RETURNS text
    LANGUAGE sql IMMUTABLE
    AS $$ SELECT '2.4.4'::text || ' r' || 16526::text AS version $$;


ALTER FUNCTION public.postgis_scripts_installed() OWNER TO postgres;

--
-- Name: postgis_scripts_released(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_scripts_released() RETURNS text
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.4', 'postgis_scripts_released';


ALTER FUNCTION public.postgis_scripts_released() OWNER TO postgres;

--
-- Name: postgis_svn_version(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_svn_version() RETURNS text
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.4', 'postgis_svn_version';


ALTER FUNCTION public.postgis_svn_version() OWNER TO postgres;

--
-- Name: postgis_transform_geometry(public.geometry, text, text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_transform_geometry(public.geometry, text, text, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'transform_geom';


ALTER FUNCTION public.postgis_transform_geometry(public.geometry, text, text, integer) OWNER TO postgres;

--
-- Name: postgis_type_name(character varying, integer, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_type_name(geomname character varying, coord_dimension integer, use_new_name boolean DEFAULT true) RETURNS character varying
    LANGUAGE sql IMMUTABLE STRICT COST 200 PARALLEL SAFE
    AS $_$
 SELECT CASE WHEN $3 THEN new_name ELSE old_name END As geomname
	FROM
 	( VALUES
 		 ('GEOMETRY', 'Geometry', 2) ,
 		 	('GEOMETRY', 'GeometryZ', 3) ,
 		 	('GEOMETRY', 'GeometryZM', 4) ,
			('GEOMETRYCOLLECTION', 'GeometryCollection', 2) ,
			('GEOMETRYCOLLECTION', 'GeometryCollectionZ', 3) ,
			('GEOMETRYCOLLECTIONM', 'GeometryCollectionM', 3) ,
			('GEOMETRYCOLLECTION', 'GeometryCollectionZM', 4) ,

			('POINT', 'Point',2) ,
			('POINTM','PointM',3) ,
			('POINT', 'PointZ',3) ,
			('POINT', 'PointZM',4) ,

			('MULTIPOINT','MultiPoint',2) ,
			('MULTIPOINT','MultiPointZ',3) ,
			('MULTIPOINTM','MultiPointM',3) ,
			('MULTIPOINT','MultiPointZM',4) ,

			('POLYGON', 'Polygon',2) ,
			('POLYGON', 'PolygonZ',3) ,
			('POLYGONM', 'PolygonM',3) ,
			('POLYGON', 'PolygonZM',4) ,

			('MULTIPOLYGON', 'MultiPolygon',2) ,
			('MULTIPOLYGON', 'MultiPolygonZ',3) ,
			('MULTIPOLYGONM', 'MultiPolygonM',3) ,
			('MULTIPOLYGON', 'MultiPolygonZM',4) ,

			('MULTILINESTRING', 'MultiLineString',2) ,
			('MULTILINESTRING', 'MultiLineStringZ',3) ,
			('MULTILINESTRINGM', 'MultiLineStringM',3) ,
			('MULTILINESTRING', 'MultiLineStringZM',4) ,

			('LINESTRING', 'LineString',2) ,
			('LINESTRING', 'LineStringZ',3) ,
			('LINESTRINGM', 'LineStringM',3) ,
			('LINESTRING', 'LineStringZM',4) ,

			('CIRCULARSTRING', 'CircularString',2) ,
			('CIRCULARSTRING', 'CircularStringZ',3) ,
			('CIRCULARSTRINGM', 'CircularStringM',3) ,
			('CIRCULARSTRING', 'CircularStringZM',4) ,

			('COMPOUNDCURVE', 'CompoundCurve',2) ,
			('COMPOUNDCURVE', 'CompoundCurveZ',3) ,
			('COMPOUNDCURVEM', 'CompoundCurveM',3) ,
			('COMPOUNDCURVE', 'CompoundCurveZM',4) ,

			('CURVEPOLYGON', 'CurvePolygon',2) ,
			('CURVEPOLYGON', 'CurvePolygonZ',3) ,
			('CURVEPOLYGONM', 'CurvePolygonM',3) ,
			('CURVEPOLYGON', 'CurvePolygonZM',4) ,

			('MULTICURVE', 'MultiCurve',2 ) ,
			('MULTICURVE', 'MultiCurveZ',3 ) ,
			('MULTICURVEM', 'MultiCurveM',3 ) ,
			('MULTICURVE', 'MultiCurveZM',4 ) ,

			('MULTISURFACE', 'MultiSurface', 2) ,
			('MULTISURFACE', 'MultiSurfaceZ', 3) ,
			('MULTISURFACEM', 'MultiSurfaceM', 3) ,
			('MULTISURFACE', 'MultiSurfaceZM', 4) ,

			('POLYHEDRALSURFACE', 'PolyhedralSurface',2) ,
			('POLYHEDRALSURFACE', 'PolyhedralSurfaceZ',3) ,
			('POLYHEDRALSURFACEM', 'PolyhedralSurfaceM',3) ,
			('POLYHEDRALSURFACE', 'PolyhedralSurfaceZM',4) ,

			('TRIANGLE', 'Triangle',2) ,
			('TRIANGLE', 'TriangleZ',3) ,
			('TRIANGLEM', 'TriangleM',3) ,
			('TRIANGLE', 'TriangleZM',4) ,

			('TIN', 'Tin', 2),
			('TIN', 'TinZ', 3),
			('TIN', 'TinM', 3),
			('TIN', 'TinZM', 4) )
			 As g(old_name, new_name, coord_dimension)
		WHERE (upper(old_name) = upper($1) OR upper(new_name) = upper($1))
			AND coord_dimension = $2;
$_$;


ALTER FUNCTION public.postgis_type_name(geomname character varying, coord_dimension integer, use_new_name boolean) OWNER TO postgres;

--
-- Name: postgis_typmod_dims(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_typmod_dims(integer) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'postgis_typmod_dims';


ALTER FUNCTION public.postgis_typmod_dims(integer) OWNER TO postgres;

--
-- Name: postgis_typmod_srid(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_typmod_srid(integer) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'postgis_typmod_srid';


ALTER FUNCTION public.postgis_typmod_srid(integer) OWNER TO postgres;

--
-- Name: postgis_typmod_type(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_typmod_type(integer) RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'postgis_typmod_type';


ALTER FUNCTION public.postgis_typmod_type(integer) OWNER TO postgres;

--
-- Name: postgis_version(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.postgis_version() RETURNS text
    LANGUAGE c IMMUTABLE
    AS '$libdir/postgis-2.4', 'postgis_version';


ALTER FUNCTION public.postgis_version() OWNER TO postgres;

--
-- Name: st_3dclosestpoint(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_3dclosestpoint(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_closestpoint3d';


ALTER FUNCTION public.st_3dclosestpoint(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_3ddfullywithin(public.geometry, public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_3ddfullywithin(geom1 public.geometry, geom2 public.geometry, double precision) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(&&) ST_Expand($2,$3) AND $2 OPERATOR(&&) ST_Expand($1,$3) AND _ST_3DDFullyWithin($1, $2, $3)$_$;


ALTER FUNCTION public.st_3ddfullywithin(geom1 public.geometry, geom2 public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_3ddistance(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_3ddistance(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'distance3d';


ALTER FUNCTION public.st_3ddistance(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_3ddwithin(public.geometry, public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_3ddwithin(geom1 public.geometry, geom2 public.geometry, double precision) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(&&) ST_Expand($2,$3) AND $2 OPERATOR(&&) ST_Expand($1,$3) AND _ST_3DDWithin($1, $2, $3)$_$;


ALTER FUNCTION public.st_3ddwithin(geom1 public.geometry, geom2 public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_3dintersects(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_3dintersects(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(&&) $2 AND _ST_3DIntersects($1, $2)$_$;


ALTER FUNCTION public.st_3dintersects(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_3dlength(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_3dlength(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 20 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_length_linestring';


ALTER FUNCTION public.st_3dlength(public.geometry) OWNER TO postgres;

--
-- Name: st_3dlength_spheroid(public.geometry, public.spheroid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_3dlength_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT _postgis_deprecate('ST_3DLength_Spheroid', 'ST_LengthSpheroid', '2.2.0');
    SELECT ST_LengthSpheroid($1,$2);
  $_$;


ALTER FUNCTION public.st_3dlength_spheroid(public.geometry, public.spheroid) OWNER TO postgres;

--
-- Name: st_3dlongestline(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_3dlongestline(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_longestline3d';


ALTER FUNCTION public.st_3dlongestline(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_3dmakebox(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_3dmakebox(geom1 public.geometry, geom2 public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_construct';


ALTER FUNCTION public.st_3dmakebox(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_3dmaxdistance(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_3dmaxdistance(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_maxdistance3d';


ALTER FUNCTION public.st_3dmaxdistance(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_3dperimeter(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_3dperimeter(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_perimeter_poly';


ALTER FUNCTION public.st_3dperimeter(public.geometry) OWNER TO postgres;

--
-- Name: st_3dshortestline(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_3dshortestline(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_shortestline3d';


ALTER FUNCTION public.st_3dshortestline(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_addmeasure(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_addmeasure(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_AddMeasure';


ALTER FUNCTION public.st_addmeasure(public.geometry, double precision, double precision) OWNER TO postgres;

--
-- Name: st_addpoint(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_addpoint(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_addpoint';


ALTER FUNCTION public.st_addpoint(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_addpoint(public.geometry, public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_addpoint(geom1 public.geometry, geom2 public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_addpoint';


ALTER FUNCTION public.st_addpoint(geom1 public.geometry, geom2 public.geometry, integer) OWNER TO postgres;

--
-- Name: st_affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_Affine($1,  $2, $3, 0,  $4, $5, 0,  0, 0, 1,  $6, $7, 0)$_$;


ALTER FUNCTION public.st_affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: st_affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_affine';


ALTER FUNCTION public.st_affine(public.geometry, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: st_area(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_area(text) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_Area($1::geometry);  $_$;


ALTER FUNCTION public.st_area(text) OWNER TO postgres;

--
-- Name: st_area(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_area(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'area';


ALTER FUNCTION public.st_area(public.geometry) OWNER TO postgres;

--
-- Name: st_area(public.geography, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_area(geog public.geography, use_spheroid boolean DEFAULT true) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_area';


ALTER FUNCTION public.st_area(geog public.geography, use_spheroid boolean) OWNER TO postgres;

--
-- Name: st_area2d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_area2d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_area_polygon';


ALTER FUNCTION public.st_area2d(public.geometry) OWNER TO postgres;

--
-- Name: st_asbinary(public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asbinary(public.geography) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asBinary';


ALTER FUNCTION public.st_asbinary(public.geography) OWNER TO postgres;

--
-- Name: st_asbinary(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asbinary(public.geometry) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asBinary';


ALTER FUNCTION public.st_asbinary(public.geometry) OWNER TO postgres;

--
-- Name: st_asbinary(public.geography, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asbinary(public.geography, text) RETURNS bytea
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_AsBinary($1::geometry, $2);  $_$;


ALTER FUNCTION public.st_asbinary(public.geography, text) OWNER TO postgres;

--
-- Name: st_asbinary(public.geometry, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asbinary(public.geometry, text) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asBinary';


ALTER FUNCTION public.st_asbinary(public.geometry, text) OWNER TO postgres;

--
-- Name: st_asencodedpolyline(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asencodedpolyline(geom public.geometry, integer DEFAULT 5) RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asEncodedPolyline';


ALTER FUNCTION public.st_asencodedpolyline(geom public.geometry, integer) OWNER TO postgres;

--
-- Name: st_asewkb(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asewkb(public.geometry) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'WKBFromLWGEOM';


ALTER FUNCTION public.st_asewkb(public.geometry) OWNER TO postgres;

--
-- Name: st_asewkb(public.geometry, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asewkb(public.geometry, text) RETURNS bytea
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'WKBFromLWGEOM';


ALTER FUNCTION public.st_asewkb(public.geometry, text) OWNER TO postgres;

--
-- Name: st_asewkt(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asewkt(text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_AsEWKT($1::geometry);  $_$;


ALTER FUNCTION public.st_asewkt(text) OWNER TO postgres;

--
-- Name: st_asewkt(public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asewkt(public.geography) RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asEWKT';


ALTER FUNCTION public.st_asewkt(public.geography) OWNER TO postgres;

--
-- Name: st_asewkt(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asewkt(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT COST 750 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asEWKT';


ALTER FUNCTION public.st_asewkt(public.geometry) OWNER TO postgres;

--
-- Name: st_asgeojson(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asgeojson(text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _ST_AsGeoJson(1, $1::geometry,15,0);  $_$;


ALTER FUNCTION public.st_asgeojson(text) OWNER TO postgres;

--
-- Name: st_asgeojson(public.geography, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asgeojson(geog public.geography, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _ST_AsGeoJson(1, $1, $2, $3); $_$;


ALTER FUNCTION public.st_asgeojson(geog public.geography, maxdecimaldigits integer, options integer) OWNER TO postgres;

--
-- Name: st_asgeojson(public.geometry, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asgeojson(geom public.geometry, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0) RETURNS text
    LANGUAGE c IMMUTABLE STRICT COST 1000 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asGeoJson';


ALTER FUNCTION public.st_asgeojson(geom public.geometry, maxdecimaldigits integer, options integer) OWNER TO postgres;

--
-- Name: st_asgeojson(integer, public.geography, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asgeojson(gj_version integer, geog public.geography, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _ST_AsGeoJson($1, $2, $3, $4); $_$;


ALTER FUNCTION public.st_asgeojson(gj_version integer, geog public.geography, maxdecimaldigits integer, options integer) OWNER TO postgres;

--
-- Name: st_asgeojson(integer, public.geometry, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asgeojson(gj_version integer, geom public.geometry, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_AsGeoJson($2::geometry, $3::int4, $4::int4); $_$;


ALTER FUNCTION public.st_asgeojson(gj_version integer, geom public.geometry, maxdecimaldigits integer, options integer) OWNER TO postgres;

--
-- Name: st_asgml(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asgml(text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _ST_AsGML(2,$1::geometry,15,0, NULL, NULL);  $_$;


ALTER FUNCTION public.st_asgml(text) OWNER TO postgres;

--
-- Name: st_asgml(public.geography, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asgml(geog public.geography, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT _ST_AsGML(2, $1, $2, $3, null, null)$_$;


ALTER FUNCTION public.st_asgml(geog public.geography, maxdecimaldigits integer, options integer) OWNER TO postgres;

--
-- Name: st_asgml(public.geometry, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asgml(geom public.geometry, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _ST_AsGML(2, $1, $2, $3, null, null); $_$;


ALTER FUNCTION public.st_asgml(geom public.geometry, maxdecimaldigits integer, options integer) OWNER TO postgres;

--
-- Name: st_asgml(integer, public.geography, integer, integer, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asgml(version integer, geog public.geography, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0, nprefix text DEFAULT NULL::text, id text DEFAULT NULL::text) RETURNS text
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$ SELECT _ST_AsGML($1, $2, $3, $4, $5, $6);$_$;


ALTER FUNCTION public.st_asgml(version integer, geog public.geography, maxdecimaldigits integer, options integer, nprefix text, id text) OWNER TO postgres;

--
-- Name: st_asgml(integer, public.geometry, integer, integer, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asgml(version integer, geom public.geometry, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0, nprefix text DEFAULT NULL::text, id text DEFAULT NULL::text) RETURNS text
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$ SELECT _ST_AsGML($1, $2, $3, $4, $5, $6); $_$;


ALTER FUNCTION public.st_asgml(version integer, geom public.geometry, maxdecimaldigits integer, options integer, nprefix text, id text) OWNER TO postgres;

--
-- Name: st_ashexewkb(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_ashexewkb(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT COST 25 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asHEXEWKB';


ALTER FUNCTION public.st_ashexewkb(public.geometry) OWNER TO postgres;

--
-- Name: st_ashexewkb(public.geometry, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_ashexewkb(public.geometry, text) RETURNS text
    LANGUAGE c IMMUTABLE STRICT COST 25 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asHEXEWKB';


ALTER FUNCTION public.st_ashexewkb(public.geometry, text) OWNER TO postgres;

--
-- Name: st_askml(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_askml(text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _ST_AsKML(2, $1::geometry, 15, null);  $_$;


ALTER FUNCTION public.st_askml(text) OWNER TO postgres;

--
-- Name: st_askml(public.geography, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_askml(geog public.geography, maxdecimaldigits integer DEFAULT 15) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT _ST_AsKML(2, $1, $2, null)$_$;


ALTER FUNCTION public.st_askml(geog public.geography, maxdecimaldigits integer) OWNER TO postgres;

--
-- Name: st_askml(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_askml(geom public.geometry, maxdecimaldigits integer DEFAULT 15) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _ST_AsKML(2, ST_Transform($1,4326), $2, null); $_$;


ALTER FUNCTION public.st_askml(geom public.geometry, maxdecimaldigits integer) OWNER TO postgres;

--
-- Name: st_askml(integer, public.geography, integer, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_askml(version integer, geog public.geography, maxdecimaldigits integer DEFAULT 15, nprefix text DEFAULT NULL::text) RETURNS text
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT _ST_AsKML($1, $2, $3, $4)$_$;


ALTER FUNCTION public.st_askml(version integer, geog public.geography, maxdecimaldigits integer, nprefix text) OWNER TO postgres;

--
-- Name: st_askml(integer, public.geometry, integer, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_askml(version integer, geom public.geometry, maxdecimaldigits integer DEFAULT 15, nprefix text DEFAULT NULL::text) RETURNS text
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$ SELECT _ST_AsKML($1, ST_Transform($2,4326), $3, $4); $_$;


ALTER FUNCTION public.st_askml(version integer, geom public.geometry, maxdecimaldigits integer, nprefix text) OWNER TO postgres;

--
-- Name: st_aslatlontext(public.geometry, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_aslatlontext(geom public.geometry, tmpl text DEFAULT ''::text) RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_to_latlon';


ALTER FUNCTION public.st_aslatlontext(geom public.geometry, tmpl text) OWNER TO postgres;

--
-- Name: st_asmvtgeom(public.geometry, public.box2d, integer, integer, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asmvtgeom(geom public.geometry, bounds public.box2d, extent integer DEFAULT 4096, buffer integer DEFAULT 256, clip_geom boolean DEFAULT true) RETURNS public.geometry
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_AsMVTGeom';


ALTER FUNCTION public.st_asmvtgeom(geom public.geometry, bounds public.box2d, extent integer, buffer integer, clip_geom boolean) OWNER TO postgres;

--
-- Name: st_assvg(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_assvg(text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_AsSVG($1::geometry,0,15);  $_$;


ALTER FUNCTION public.st_assvg(text) OWNER TO postgres;

--
-- Name: st_assvg(public.geography, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_assvg(geog public.geography, rel integer DEFAULT 0, maxdecimaldigits integer DEFAULT 15) RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_as_svg';


ALTER FUNCTION public.st_assvg(geog public.geography, rel integer, maxdecimaldigits integer) OWNER TO postgres;

--
-- Name: st_assvg(public.geometry, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_assvg(geom public.geometry, rel integer DEFAULT 0, maxdecimaldigits integer DEFAULT 15) RETURNS text
    LANGUAGE c IMMUTABLE STRICT COST 1000 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asSVG';


ALTER FUNCTION public.st_assvg(geom public.geometry, rel integer, maxdecimaldigits integer) OWNER TO postgres;

--
-- Name: st_astext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_astext(text) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_AsText($1::geometry);  $_$;


ALTER FUNCTION public.st_astext(text) OWNER TO postgres;

--
-- Name: st_astext(public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_astext(public.geography) RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asText';


ALTER FUNCTION public.st_astext(public.geography) OWNER TO postgres;

--
-- Name: st_astext(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_astext(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT COST 750 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_asText';


ALTER FUNCTION public.st_astext(public.geometry) OWNER TO postgres;

--
-- Name: st_astwkb(public.geometry, integer, integer, integer, boolean, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_astwkb(geom public.geometry, prec integer DEFAULT NULL::integer, prec_z integer DEFAULT NULL::integer, prec_m integer DEFAULT NULL::integer, with_sizes boolean DEFAULT NULL::boolean, with_boxes boolean DEFAULT NULL::boolean) RETURNS bytea
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'TWKBFromLWGEOM';


ALTER FUNCTION public.st_astwkb(geom public.geometry, prec integer, prec_z integer, prec_m integer, with_sizes boolean, with_boxes boolean) OWNER TO postgres;

--
-- Name: st_astwkb(public.geometry[], bigint[], integer, integer, integer, boolean, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_astwkb(geom public.geometry[], ids bigint[], prec integer DEFAULT NULL::integer, prec_z integer DEFAULT NULL::integer, prec_m integer DEFAULT NULL::integer, with_sizes boolean DEFAULT NULL::boolean, with_boxes boolean DEFAULT NULL::boolean) RETURNS bytea
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'TWKBFromLWGEOMArray';


ALTER FUNCTION public.st_astwkb(geom public.geometry[], ids bigint[], prec integer, prec_z integer, prec_m integer, with_sizes boolean, with_boxes boolean) OWNER TO postgres;

--
-- Name: st_asx3d(public.geometry, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_asx3d(geom public.geometry, maxdecimaldigits integer DEFAULT 15, options integer DEFAULT 0) RETURNS text
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT _ST_AsX3D(3,$1,$2,$3,'');$_$;


ALTER FUNCTION public.st_asx3d(geom public.geometry, maxdecimaldigits integer, options integer) OWNER TO postgres;

--
-- Name: st_azimuth(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_azimuth(geog1 public.geography, geog2 public.geography) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_azimuth';


ALTER FUNCTION public.st_azimuth(geog1 public.geography, geog2 public.geography) OWNER TO postgres;

--
-- Name: st_azimuth(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_azimuth(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_azimuth';


ALTER FUNCTION public.st_azimuth(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_bdmpolyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_bdmpolyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE plpgsql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
DECLARE
	geomtext alias for $1;
	srid alias for $2;
	mline geometry;
	geom geometry;
BEGIN
	mline := ST_MultiLineStringFromText(geomtext, srid);

	IF mline IS NULL
	THEN
		RAISE EXCEPTION 'Input is not a MultiLinestring';
	END IF;

	geom := ST_Multi(ST_BuildArea(mline));

	RETURN geom;
END;
$_$;


ALTER FUNCTION public.st_bdmpolyfromtext(text, integer) OWNER TO postgres;

--
-- Name: st_bdpolyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_bdpolyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE plpgsql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
DECLARE
	geomtext alias for $1;
	srid alias for $2;
	mline geometry;
	geom geometry;
BEGIN
	mline := ST_MultiLineStringFromText(geomtext, srid);

	IF mline IS NULL
	THEN
		RAISE EXCEPTION 'Input is not a MultiLinestring';
	END IF;

	geom := ST_BuildArea(mline);

	IF GeometryType(geom) != 'POLYGON'
	THEN
		RAISE EXCEPTION 'Input returns more then a single polygon, try using BdMPolyFromText instead';
	END IF;

	RETURN geom;
END;
$_$;


ALTER FUNCTION public.st_bdpolyfromtext(text, integer) OWNER TO postgres;

--
-- Name: st_boundary(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_boundary(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'boundary';


ALTER FUNCTION public.st_boundary(public.geometry) OWNER TO postgres;

--
-- Name: st_boundingdiagonal(public.geometry, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_boundingdiagonal(geom public.geometry, fits boolean DEFAULT false) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_BoundingDiagonal';


ALTER FUNCTION public.st_boundingdiagonal(geom public.geometry, fits boolean) OWNER TO postgres;

--
-- Name: st_box2dfromgeohash(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_box2dfromgeohash(text, integer DEFAULT NULL::integer) RETURNS public.box2d
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'box2d_from_geohash';


ALTER FUNCTION public.st_box2dfromgeohash(text, integer) OWNER TO postgres;

--
-- Name: st_buffer(text, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_buffer(text, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_Buffer($1::geometry, $2);  $_$;


ALTER FUNCTION public.st_buffer(text, double precision) OWNER TO postgres;

--
-- Name: st_buffer(public.geography, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_buffer(public.geography, double precision) RETURNS public.geography
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT geography(ST_Transform(ST_Buffer(ST_Transform(geometry($1), _ST_BestSRID($1)), $2), 4326))$_$;


ALTER FUNCTION public.st_buffer(public.geography, double precision) OWNER TO postgres;

--
-- Name: st_buffer(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_buffer(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'buffer';


ALTER FUNCTION public.st_buffer(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_buffer(text, double precision, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_buffer(text, double precision, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_Buffer($1::geometry, $2, $3);  $_$;


ALTER FUNCTION public.st_buffer(text, double precision, integer) OWNER TO postgres;

--
-- Name: st_buffer(text, double precision, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_buffer(text, double precision, text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_Buffer($1::geometry, $2, $3);  $_$;


ALTER FUNCTION public.st_buffer(text, double precision, text) OWNER TO postgres;

--
-- Name: st_buffer(public.geography, double precision, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_buffer(public.geography, double precision, integer) RETURNS public.geography
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT geography(ST_Transform(ST_Buffer(ST_Transform(geometry($1), _ST_BestSRID($1)), $2, $3), 4326))$_$;


ALTER FUNCTION public.st_buffer(public.geography, double precision, integer) OWNER TO postgres;

--
-- Name: st_buffer(public.geography, double precision, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_buffer(public.geography, double precision, text) RETURNS public.geography
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT geography(ST_Transform(ST_Buffer(ST_Transform(geometry($1), _ST_BestSRID($1)), $2, $3), 4326))$_$;


ALTER FUNCTION public.st_buffer(public.geography, double precision, text) OWNER TO postgres;

--
-- Name: st_buffer(public.geometry, double precision, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_buffer(public.geometry, double precision, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _ST_Buffer($1, $2,
		CAST('quad_segs='||CAST($3 AS text) as cstring))
	   $_$;


ALTER FUNCTION public.st_buffer(public.geometry, double precision, integer) OWNER TO postgres;

--
-- Name: st_buffer(public.geometry, double precision, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_buffer(public.geometry, double precision, text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _ST_Buffer($1, $2,
		CAST( regexp_replace($3, '^[0123456789]+$',
			'quad_segs='||$3) AS cstring)
		)
	   $_$;


ALTER FUNCTION public.st_buffer(public.geometry, double precision, text) OWNER TO postgres;

--
-- Name: st_buildarea(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_buildarea(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_BuildArea';


ALTER FUNCTION public.st_buildarea(public.geometry) OWNER TO postgres;

--
-- Name: st_centroid(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_centroid(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_Centroid($1::geometry);  $_$;


ALTER FUNCTION public.st_centroid(text) OWNER TO postgres;

--
-- Name: st_centroid(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_centroid(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'centroid';


ALTER FUNCTION public.st_centroid(public.geometry) OWNER TO postgres;

--
-- Name: st_centroid(public.geography, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_centroid(public.geography, use_spheroid boolean DEFAULT true) RETURNS public.geography
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_centroid';


ALTER FUNCTION public.st_centroid(public.geography, use_spheroid boolean) OWNER TO postgres;

--
-- Name: st_cleangeometry(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_cleangeometry(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_CleanGeometry';


ALTER FUNCTION public.st_cleangeometry(public.geometry) OWNER TO postgres;

--
-- Name: st_clipbybox2d(public.geometry, public.box2d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_clipbybox2d(geom public.geometry, box public.box2d) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 50 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_ClipByBox2d';


ALTER FUNCTION public.st_clipbybox2d(geom public.geometry, box public.box2d) OWNER TO postgres;

--
-- Name: st_closestpoint(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_closestpoint(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_closestpoint';


ALTER FUNCTION public.st_closestpoint(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_closestpointofapproach(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_closestpointofapproach(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_ClosestPointOfApproach';


ALTER FUNCTION public.st_closestpointofapproach(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: st_clusterdbscan(public.geometry, double precision, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_clusterdbscan(public.geometry, eps double precision, minpoints integer) RETURNS integer
    LANGUAGE c WINDOW IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_ClusterDBSCAN';


ALTER FUNCTION public.st_clusterdbscan(public.geometry, eps double precision, minpoints integer) OWNER TO postgres;

--
-- Name: st_clusterintersecting(public.geometry[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_clusterintersecting(public.geometry[]) RETURNS public.geometry[]
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'clusterintersecting_garray';


ALTER FUNCTION public.st_clusterintersecting(public.geometry[]) OWNER TO postgres;

--
-- Name: st_clusterkmeans(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_clusterkmeans(geom public.geometry, k integer) RETURNS integer
    LANGUAGE c WINDOW STRICT
    AS '$libdir/postgis-2.4', 'ST_ClusterKMeans';


ALTER FUNCTION public.st_clusterkmeans(geom public.geometry, k integer) OWNER TO postgres;

--
-- Name: st_clusterwithin(public.geometry[], double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_clusterwithin(public.geometry[], double precision) RETURNS public.geometry[]
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'cluster_within_distance_garray';


ALTER FUNCTION public.st_clusterwithin(public.geometry[], double precision) OWNER TO postgres;

--
-- Name: st_collect(public.geometry[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_collect(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_collect_garray';


ALTER FUNCTION public.st_collect(public.geometry[]) OWNER TO postgres;

--
-- Name: st_collect(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_collect(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_collect';


ALTER FUNCTION public.st_collect(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_collectionextract(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_collectionextract(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_CollectionExtract';


ALTER FUNCTION public.st_collectionextract(public.geometry, integer) OWNER TO postgres;

--
-- Name: st_collectionhomogenize(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_collectionhomogenize(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_CollectionHomogenize';


ALTER FUNCTION public.st_collectionhomogenize(public.geometry) OWNER TO postgres;

--
-- Name: st_combine_bbox(public.box2d, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_combine_bbox(public.box2d, public.geometry) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE
    AS $_$ SELECT _postgis_deprecate('ST_Combine_BBox', 'ST_CombineBbox', '2.2.0');
    SELECT ST_CombineBbox($1,$2);
  $_$;


ALTER FUNCTION public.st_combine_bbox(public.box2d, public.geometry) OWNER TO postgres;

--
-- Name: st_combine_bbox(public.box3d, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_combine_bbox(public.box3d, public.geometry) RETURNS public.box3d
    LANGUAGE sql IMMUTABLE
    AS $_$ SELECT _postgis_deprecate('ST_Combine_BBox', 'ST_CombineBbox', '2.2.0');
    SELECT ST_CombineBbox($1,$2);
  $_$;


ALTER FUNCTION public.st_combine_bbox(public.box3d, public.geometry) OWNER TO postgres;

--
-- Name: st_combinebbox(public.box2d, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_combinebbox(public.box2d, public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX2D_combine';


ALTER FUNCTION public.st_combinebbox(public.box2d, public.geometry) OWNER TO postgres;

--
-- Name: st_combinebbox(public.box3d, public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_combinebbox(public.box3d, public.box3d) RETURNS public.box3d
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_combine_BOX3D';


ALTER FUNCTION public.st_combinebbox(public.box3d, public.box3d) OWNER TO postgres;

--
-- Name: st_combinebbox(public.box3d, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_combinebbox(public.box3d, public.geometry) RETURNS public.box3d
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_combine';


ALTER FUNCTION public.st_combinebbox(public.box3d, public.geometry) OWNER TO postgres;

--
-- Name: st_concavehull(public.geometry, double precision, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_concavehull(param_geom public.geometry, param_pctconvex double precision, param_allow_holes boolean DEFAULT false) RETURNS public.geometry
    LANGUAGE plpgsql IMMUTABLE STRICT PARALLEL SAFE
    AS $$
	DECLARE
		var_convhull geometry := ST_ConvexHull(param_geom);
		var_param_geom geometry := param_geom;
		var_initarea float := ST_Area(var_convhull);
		var_newarea float := var_initarea;
		var_div integer := 6;
		var_tempgeom geometry;
		var_tempgeom2 geometry;
		var_cent geometry;
		var_geoms geometry[4];
		var_enline geometry;
		var_resultgeom geometry;
		var_atempgeoms geometry[];
		var_buf float := 1;
	BEGIN
		-- We start with convex hull as our base
		var_resultgeom := var_convhull;

		IF param_pctconvex = 1 THEN
			return var_resultgeom;
		ELSIF ST_GeometryType(var_param_geom) = 'ST_Polygon' THEN -- it is as concave as it is going to get
			IF param_allow_holes THEN -- leave the holes
				RETURN var_param_geom;
			ELSE -- remove the holes
				var_resultgeom := ST_MakePolygon(ST_ExteriorRing(var_param_geom));
				RETURN var_resultgeom;
			END IF;
		END IF;
		IF ST_Dimension(var_resultgeom) > 1 AND param_pctconvex BETWEEN 0 and 0.98 THEN
		-- get linestring that forms envelope of geometry
			var_enline := ST_Boundary(ST_Envelope(var_param_geom));
			var_buf := ST_Length(var_enline)/1000.0;
			IF ST_GeometryType(var_param_geom) = 'ST_MultiPoint' AND ST_NumGeometries(var_param_geom) BETWEEN 4 and 200 THEN
			-- we make polygons out of points since they are easier to cave in.
			-- Note we limit to between 4 and 200 points because this process is slow and gets quadratically slow
				var_buf := sqrt(ST_Area(var_convhull)*0.8/(ST_NumGeometries(var_param_geom)*ST_NumGeometries(var_param_geom)));
				var_atempgeoms := ARRAY(SELECT geom FROM ST_DumpPoints(var_param_geom));
				-- 5 and 10 and just fudge factors
				var_tempgeom := ST_Union(ARRAY(SELECT geom
						FROM (
						-- fuse near neighbors together
						SELECT DISTINCT ON (i) i,  ST_Distance(var_atempgeoms[i],var_atempgeoms[j]), ST_Buffer(ST_MakeLine(var_atempgeoms[i], var_atempgeoms[j]) , var_buf*5, 'quad_segs=3') As geom
								FROM generate_series(1,array_upper(var_atempgeoms, 1)) As i
									INNER JOIN generate_series(1,array_upper(var_atempgeoms, 1)) As j
										ON (
								 NOT ST_Intersects(var_atempgeoms[i],var_atempgeoms[j])
									AND ST_DWithin(var_atempgeoms[i],var_atempgeoms[j], var_buf*10)
									)
								UNION ALL
						-- catch the ones with no near neighbors
								SELECT i, 0, ST_Buffer(var_atempgeoms[i] , var_buf*10, 'quad_segs=3') As geom
								FROM generate_series(1,array_upper(var_atempgeoms, 1)) As i
									LEFT JOIN generate_series(ceiling(array_upper(var_atempgeoms,1)/2)::integer,array_upper(var_atempgeoms, 1)) As j
										ON (
								 NOT ST_Intersects(var_atempgeoms[i],var_atempgeoms[j])
									AND ST_DWithin(var_atempgeoms[i],var_atempgeoms[j], var_buf*10)
									)
									WHERE j IS NULL
								ORDER BY 1, 2
							) As foo	) );
				IF ST_IsValid(var_tempgeom) AND ST_GeometryType(var_tempgeom) = 'ST_Polygon' THEN
					var_tempgeom := ST_ForceSFS(ST_Intersection(var_tempgeom, var_convhull));
					IF param_allow_holes THEN
						var_param_geom := var_tempgeom;
					ELSIF ST_GeometryType(var_tempgeom) = 'ST_Polygon' THEN
						var_param_geom := ST_MakePolygon(ST_ExteriorRing(var_tempgeom));
					ELSE
						var_param_geom := ST_ConvexHull(var_param_geom);
					END IF;
					return var_param_geom;
				ELSIF ST_IsValid(var_tempgeom) THEN
					var_param_geom := ST_ForceSFS(ST_Intersection(var_tempgeom, var_convhull));
				END IF;
			END IF;

			IF ST_GeometryType(var_param_geom) = 'ST_Polygon' THEN
				IF NOT param_allow_holes THEN
					var_param_geom := ST_MakePolygon(ST_ExteriorRing(var_param_geom));
				END IF;
				return var_param_geom;
			END IF;
            var_cent := ST_Centroid(var_param_geom);
            IF (ST_XMax(var_enline) - ST_XMin(var_enline) ) > var_buf AND (ST_YMax(var_enline) - ST_YMin(var_enline) ) > var_buf THEN
                    IF ST_Dwithin(ST_Centroid(var_convhull) , ST_Centroid(ST_Envelope(var_param_geom)), var_buf/2) THEN
                -- If the geometric dimension is > 1 and the object is symettric (cutting at centroid will not work -- offset a bit)
                        var_cent := ST_Translate(var_cent, (ST_XMax(var_enline) - ST_XMin(var_enline))/1000,  (ST_YMAX(var_enline) - ST_YMin(var_enline))/1000);
                    ELSE
                        -- uses closest point on geometry to centroid. I can't explain why we are doing this
                        var_cent := ST_ClosestPoint(var_param_geom,var_cent);
                    END IF;
                    IF ST_DWithin(var_cent, var_enline,var_buf) THEN
                        var_cent := ST_centroid(ST_Envelope(var_param_geom));
                    END IF;
                    -- break envelope into 4 triangles about the centroid of the geometry and returned the clipped geometry in each quadrant
                    FOR i in 1 .. 4 LOOP
                       var_geoms[i] := ST_MakePolygon(ST_MakeLine(ARRAY[ST_PointN(var_enline,i), ST_PointN(var_enline,i+1), var_cent, ST_PointN(var_enline,i)]));
                       var_geoms[i] := ST_ForceSFS(ST_Intersection(var_param_geom, ST_Buffer(var_geoms[i],var_buf)));
                       IF ST_IsValid(var_geoms[i]) THEN

                       ELSE
                            var_geoms[i] := ST_BuildArea(ST_MakeLine(ARRAY[ST_PointN(var_enline,i), ST_PointN(var_enline,i+1), var_cent, ST_PointN(var_enline,i)]));
                       END IF;
                    END LOOP;
                    var_tempgeom := ST_Union(ARRAY[ST_ConvexHull(var_geoms[1]), ST_ConvexHull(var_geoms[2]) , ST_ConvexHull(var_geoms[3]), ST_ConvexHull(var_geoms[4])]);
                    --RAISE NOTICE 'Curr vex % ', ST_AsText(var_tempgeom);
                    IF ST_Area(var_tempgeom) <= var_newarea AND ST_IsValid(var_tempgeom)  THEN --AND ST_GeometryType(var_tempgeom) ILIKE '%Polygon'

                        var_tempgeom := ST_Buffer(ST_ConcaveHull(var_geoms[1],least(param_pctconvex + param_pctconvex/var_div),true),var_buf, 'quad_segs=2');
                        FOR i IN 1 .. 4 LOOP
                            var_geoms[i] := ST_Buffer(ST_ConcaveHull(var_geoms[i],least(param_pctconvex + param_pctconvex/var_div),true), var_buf, 'quad_segs=2');
                            IF ST_IsValid(var_geoms[i]) Then
                                var_tempgeom := ST_Union(var_tempgeom, var_geoms[i]);
                            ELSE
                                RAISE NOTICE 'Not valid % %', i, ST_AsText(var_tempgeom);
                                var_tempgeom := ST_Union(var_tempgeom, ST_ConvexHull(var_geoms[i]));
                            END IF;
                        END LOOP;

                        --RAISE NOTICE 'Curr concave % ', ST_AsText(var_tempgeom);
                        IF ST_IsValid(var_tempgeom) THEN
                            var_resultgeom := var_tempgeom;
                        END IF;
                        var_newarea := ST_Area(var_resultgeom);
                    ELSIF ST_IsValid(var_tempgeom) THEN
                        var_resultgeom := var_tempgeom;
                    END IF;

                    IF ST_NumGeometries(var_resultgeom) > 1  THEN
                        var_tempgeom := _ST_ConcaveHull(var_resultgeom);
                        IF ST_IsValid(var_tempgeom) AND ST_GeometryType(var_tempgeom) ILIKE 'ST_Polygon' THEN
                            var_resultgeom := var_tempgeom;
                        ELSE
                            var_resultgeom := ST_Buffer(var_tempgeom,var_buf, 'quad_segs=2');
                        END IF;
                    END IF;
                    IF param_allow_holes = false THEN
                    -- only keep exterior ring since we do not want holes
                        var_resultgeom := ST_MakePolygon(ST_ExteriorRing(var_resultgeom));
                    END IF;
                ELSE
                    var_resultgeom := ST_Buffer(var_resultgeom,var_buf);
                END IF;
                var_resultgeom := ST_ForceSFS(ST_Intersection(var_resultgeom, ST_ConvexHull(var_param_geom)));
            ELSE
                -- dimensions are too small to cut
                var_resultgeom := _ST_ConcaveHull(var_param_geom);
            END IF;
            RETURN var_resultgeom;
	END;
$$;


ALTER FUNCTION public.st_concavehull(param_geom public.geometry, param_pctconvex double precision, param_allow_holes boolean) OWNER TO postgres;

--
-- Name: st_contains(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_contains(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(~) $2 AND _ST_Contains($1,$2)$_$;


ALTER FUNCTION public.st_contains(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_containsproperly(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_containsproperly(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(~) $2 AND _ST_ContainsProperly($1,$2)$_$;


ALTER FUNCTION public.st_containsproperly(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_convexhull(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_convexhull(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'convexhull';


ALTER FUNCTION public.st_convexhull(public.geometry) OWNER TO postgres;

--
-- Name: st_coorddim(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_coorddim(geometry public.geometry) RETURNS smallint
    LANGUAGE c IMMUTABLE STRICT COST 5 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_ndims';


ALTER FUNCTION public.st_coorddim(geometry public.geometry) OWNER TO postgres;

--
-- Name: st_coveredby(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_coveredby(text, text) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$ SELECT ST_CoveredBy($1::geometry, $2::geometry);  $_$;


ALTER FUNCTION public.st_coveredby(text, text) OWNER TO postgres;

--
-- Name: st_coveredby(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_coveredby(public.geography, public.geography) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(&&) $2 AND _ST_Covers($2, $1)$_$;


ALTER FUNCTION public.st_coveredby(public.geography, public.geography) OWNER TO postgres;

--
-- Name: st_coveredby(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_coveredby(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(@) $2 AND _ST_CoveredBy($1,$2)$_$;


ALTER FUNCTION public.st_coveredby(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_covers(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_covers(text, text) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$ SELECT ST_Covers($1::geometry, $2::geometry);  $_$;


ALTER FUNCTION public.st_covers(text, text) OWNER TO postgres;

--
-- Name: st_covers(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_covers(public.geography, public.geography) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(&&) $2 AND _ST_Covers($1, $2)$_$;


ALTER FUNCTION public.st_covers(public.geography, public.geography) OWNER TO postgres;

--
-- Name: st_covers(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_covers(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(~) $2 AND _ST_Covers($1,$2)$_$;


ALTER FUNCTION public.st_covers(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_cpawithin(public.geometry, public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_cpawithin(public.geometry, public.geometry, double precision) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_CPAWithin';


ALTER FUNCTION public.st_cpawithin(public.geometry, public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_crosses(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_crosses(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(&&) $2 AND _ST_Crosses($1,$2)$_$;


ALTER FUNCTION public.st_crosses(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_curvetoline(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_curvetoline(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_CurveToLine($1, 32::integer)$_$;


ALTER FUNCTION public.st_curvetoline(public.geometry) OWNER TO postgres;

--
-- Name: st_curvetoline(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_curvetoline(public.geometry, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_CurveToLine($1, $2::float8, 0, 0)$_$;


ALTER FUNCTION public.st_curvetoline(public.geometry, integer) OWNER TO postgres;

--
-- Name: st_curvetoline(public.geometry, double precision, integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_curvetoline(geom public.geometry, tol double precision, toltype integer, flags integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_CurveToLine';


ALTER FUNCTION public.st_curvetoline(geom public.geometry, tol double precision, toltype integer, flags integer) OWNER TO postgres;

--
-- Name: st_delaunaytriangles(public.geometry, double precision, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_delaunaytriangles(g1 public.geometry, tolerance double precision DEFAULT 0.0, flags integer DEFAULT 0) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_DelaunayTriangles';


ALTER FUNCTION public.st_delaunaytriangles(g1 public.geometry, tolerance double precision, flags integer) OWNER TO postgres;

--
-- Name: st_dfullywithin(public.geometry, public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_dfullywithin(geom1 public.geometry, geom2 public.geometry, double precision) RETURNS boolean
    LANGUAGE sql IMMUTABLE
    AS $_$SELECT $1 OPERATOR(&&) ST_Expand($2,$3) AND $2 OPERATOR(&&) ST_Expand($1,$3) AND _ST_DFullyWithin(ST_ConvexHull($1), ST_ConvexHull($2), $3)$_$;


ALTER FUNCTION public.st_dfullywithin(geom1 public.geometry, geom2 public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_difference(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_difference(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'difference';


ALTER FUNCTION public.st_difference(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_dimension(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_dimension(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_dimension';


ALTER FUNCTION public.st_dimension(public.geometry) OWNER TO postgres;

--
-- Name: st_disjoint(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_disjoint(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'disjoint';


ALTER FUNCTION public.st_disjoint(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_distance(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_distance(text, text) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_Distance($1::geometry, $2::geometry);  $_$;


ALTER FUNCTION public.st_distance(text, text) OWNER TO postgres;

--
-- Name: st_distance(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_distance(public.geography, public.geography) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT _ST_Distance($1, $2, 0.0, true)$_$;


ALTER FUNCTION public.st_distance(public.geography, public.geography) OWNER TO postgres;

--
-- Name: st_distance(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_distance(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 25 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'distance';


ALTER FUNCTION public.st_distance(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_distance(public.geography, public.geography, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_distance(public.geography, public.geography, boolean) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT _ST_Distance($1, $2, 0.0, $3)$_$;


ALTER FUNCTION public.st_distance(public.geography, public.geography, boolean) OWNER TO postgres;

--
-- Name: st_distance_sphere(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_distance_sphere(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT COST 300 PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Distance_Sphere', 'ST_DistanceSphere', '2.2.0');
    SELECT ST_DistanceSphere($1,$2);
  $_$;


ALTER FUNCTION public.st_distance_sphere(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_distance_spheroid(public.geometry, public.geometry, public.spheroid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_distance_spheroid(geom1 public.geometry, geom2 public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Distance_Spheroid', 'ST_DistanceSpheroid', '2.2.0');
    SELECT ST_DistanceSpheroid($1,$2,$3);
  $_$;


ALTER FUNCTION public.st_distance_spheroid(geom1 public.geometry, geom2 public.geometry, public.spheroid) OWNER TO postgres;

--
-- Name: st_distancecpa(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_distancecpa(public.geometry, public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_DistanceCPA';


ALTER FUNCTION public.st_distancecpa(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: st_distancesphere(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_distancesphere(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT COST 300 PARALLEL SAFE
    AS $_$
	select ST_distance( geography($1), geography($2),false)
	$_$;


ALTER FUNCTION public.st_distancesphere(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_distancespheroid(public.geometry, public.geometry, public.spheroid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_distancespheroid(geom1 public.geometry, geom2 public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 200 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_distance_ellipsoid';


ALTER FUNCTION public.st_distancespheroid(geom1 public.geometry, geom2 public.geometry, public.spheroid) OWNER TO postgres;

--
-- Name: st_dump(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_dump(public.geometry) RETURNS SETOF public.geometry_dump
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_dump';


ALTER FUNCTION public.st_dump(public.geometry) OWNER TO postgres;

--
-- Name: st_dumppoints(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_dumppoints(public.geometry) RETURNS SETOF public.geometry_dump
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_dumppoints';


ALTER FUNCTION public.st_dumppoints(public.geometry) OWNER TO postgres;

--
-- Name: st_dumprings(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_dumprings(public.geometry) RETURNS SETOF public.geometry_dump
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_dump_rings';


ALTER FUNCTION public.st_dumprings(public.geometry) OWNER TO postgres;

--
-- Name: st_dwithin(text, text, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_dwithin(text, text, double precision) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$ SELECT ST_DWithin($1::geometry, $2::geometry, $3);  $_$;


ALTER FUNCTION public.st_dwithin(text, text, double precision) OWNER TO postgres;

--
-- Name: st_dwithin(public.geography, public.geography, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_dwithin(public.geography, public.geography, double precision) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(&&) _ST_Expand($2,$3) AND $2 OPERATOR(&&) _ST_Expand($1,$3) AND _ST_DWithin($1, $2, $3, true)$_$;


ALTER FUNCTION public.st_dwithin(public.geography, public.geography, double precision) OWNER TO postgres;

--
-- Name: st_dwithin(public.geometry, public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_dwithin(geom1 public.geometry, geom2 public.geometry, double precision) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(&&) ST_Expand($2,$3) AND $2 OPERATOR(&&) ST_Expand($1,$3) AND _ST_DWithin($1, $2, $3)$_$;


ALTER FUNCTION public.st_dwithin(geom1 public.geometry, geom2 public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_dwithin(public.geography, public.geography, double precision, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_dwithin(public.geography, public.geography, double precision, boolean) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(&&) _ST_Expand($2,$3) AND $2 OPERATOR(&&) _ST_Expand($1,$3) AND _ST_DWithin($1, $2, $3, $4)$_$;


ALTER FUNCTION public.st_dwithin(public.geography, public.geography, double precision, boolean) OWNER TO postgres;

--
-- Name: st_endpoint(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_endpoint(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_endpoint_linestring';


ALTER FUNCTION public.st_endpoint(public.geometry) OWNER TO postgres;

--
-- Name: st_envelope(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_envelope(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_envelope';


ALTER FUNCTION public.st_envelope(public.geometry) OWNER TO postgres;

--
-- Name: st_equals(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_equals(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(~=) $2 AND _ST_Equals($1,$2)$_$;


ALTER FUNCTION public.st_equals(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_estimated_extent(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_estimated_extent(text, text) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT _postgis_deprecate('ST_Estimated_Extent', 'ST_EstimatedExtent', '2.1.0');
    -- We use security invoker instead of security definer
    -- to prevent malicious injection of a same named different function
    -- that would be run under elevated permissions
    SELECT ST_EstimatedExtent($1, $2);
  $_$;


ALTER FUNCTION public.st_estimated_extent(text, text) OWNER TO postgres;

--
-- Name: st_estimated_extent(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_estimated_extent(text, text, text) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT _postgis_deprecate('ST_Estimated_Extent', 'ST_EstimatedExtent', '2.1.0');
    -- We use security invoker instead of security definer
    -- to prevent malicious injection of a different same named function
    SELECT ST_EstimatedExtent($1, $2, $3);
  $_$;


ALTER FUNCTION public.st_estimated_extent(text, text, text) OWNER TO postgres;

--
-- Name: st_estimatedextent(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_estimatedextent(text, text) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT SECURITY DEFINER
    AS '$libdir/postgis-2.4', 'gserialized_estimated_extent';


ALTER FUNCTION public.st_estimatedextent(text, text) OWNER TO postgres;

--
-- Name: st_estimatedextent(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_estimatedextent(text, text, text) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT SECURITY DEFINER
    AS '$libdir/postgis-2.4', 'gserialized_estimated_extent';


ALTER FUNCTION public.st_estimatedextent(text, text, text) OWNER TO postgres;

--
-- Name: st_estimatedextent(text, text, text, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_estimatedextent(text, text, text, boolean) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT SECURITY DEFINER
    AS '$libdir/postgis-2.4', 'gserialized_estimated_extent';


ALTER FUNCTION public.st_estimatedextent(text, text, text, boolean) OWNER TO postgres;

--
-- Name: st_expand(public.box2d, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_expand(public.box2d, double precision) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX2D_expand';


ALTER FUNCTION public.st_expand(public.box2d, double precision) OWNER TO postgres;

--
-- Name: st_expand(public.box3d, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_expand(public.box3d, double precision) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_expand';


ALTER FUNCTION public.st_expand(public.box3d, double precision) OWNER TO postgres;

--
-- Name: st_expand(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_expand(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_expand';


ALTER FUNCTION public.st_expand(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_expand(public.box2d, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_expand(box public.box2d, dx double precision, dy double precision) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX2D_expand';


ALTER FUNCTION public.st_expand(box public.box2d, dx double precision, dy double precision) OWNER TO postgres;

--
-- Name: st_expand(public.box3d, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_expand(box public.box3d, dx double precision, dy double precision, dz double precision DEFAULT 0) RETURNS public.box3d
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_expand';


ALTER FUNCTION public.st_expand(box public.box3d, dx double precision, dy double precision, dz double precision) OWNER TO postgres;

--
-- Name: st_expand(public.geometry, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_expand(geom public.geometry, dx double precision, dy double precision, dz double precision DEFAULT 0, dm double precision DEFAULT 0) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_expand';


ALTER FUNCTION public.st_expand(geom public.geometry, dx double precision, dy double precision, dz double precision, dm double precision) OWNER TO postgres;

--
-- Name: st_exteriorring(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_exteriorring(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_exteriorring_polygon';


ALTER FUNCTION public.st_exteriorring(public.geometry) OWNER TO postgres;

--
-- Name: st_find_extent(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_find_extent(text, text) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Find_Extent', 'ST_FindExtent', '2.2.0');
    SELECT ST_FindExtent($1,$2);
  $_$;


ALTER FUNCTION public.st_find_extent(text, text) OWNER TO postgres;

--
-- Name: st_find_extent(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_find_extent(text, text, text) RETURNS public.box2d
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Find_Extent', 'ST_FindExtent', '2.2.0');
    SELECT ST_FindExtent($1,$2,$3);
  $_$;


ALTER FUNCTION public.st_find_extent(text, text, text) OWNER TO postgres;

--
-- Name: st_findextent(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_findextent(text, text) RETURNS public.box2d
    LANGUAGE plpgsql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
DECLARE
	tablename alias for $1;
	columnname alias for $2;
	myrec RECORD;

BEGIN
	FOR myrec IN EXECUTE 'SELECT ST_Extent("' || columnname || '") As extent FROM "' || tablename || '"' LOOP
		return myrec.extent;
	END LOOP;
END;
$_$;


ALTER FUNCTION public.st_findextent(text, text) OWNER TO postgres;

--
-- Name: st_findextent(text, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_findextent(text, text, text) RETURNS public.box2d
    LANGUAGE plpgsql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
DECLARE
	schemaname alias for $1;
	tablename alias for $2;
	columnname alias for $3;
	myrec RECORD;
BEGIN
	FOR myrec IN EXECUTE 'SELECT ST_Extent("' || columnname || '") As extent FROM "' || schemaname || '"."' || tablename || '"' LOOP
		return myrec.extent;
	END LOOP;
END;
$_$;


ALTER FUNCTION public.st_findextent(text, text, text) OWNER TO postgres;

--
-- Name: st_flipcoordinates(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_flipcoordinates(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_FlipCoordinates';


ALTER FUNCTION public.st_flipcoordinates(public.geometry) OWNER TO postgres;

--
-- Name: st_force2d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_force2d(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_force_2d';


ALTER FUNCTION public.st_force2d(public.geometry) OWNER TO postgres;

--
-- Name: st_force3d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_force3d(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_force_3dz';


ALTER FUNCTION public.st_force3d(public.geometry) OWNER TO postgres;

--
-- Name: st_force3dm(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_force3dm(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_force_3dm';


ALTER FUNCTION public.st_force3dm(public.geometry) OWNER TO postgres;

--
-- Name: st_force3dz(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_force3dz(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_force_3dz';


ALTER FUNCTION public.st_force3dz(public.geometry) OWNER TO postgres;

--
-- Name: st_force4d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_force4d(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_force_4d';


ALTER FUNCTION public.st_force4d(public.geometry) OWNER TO postgres;

--
-- Name: st_force_2d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_force_2d(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Force_2d', 'ST_Force2D', '2.1.0');
    SELECT ST_Force2D($1);
  $_$;


ALTER FUNCTION public.st_force_2d(public.geometry) OWNER TO postgres;

--
-- Name: st_force_3d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_force_3d(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Force_3d', 'ST_Force3D', '2.1.0');
    SELECT ST_Force3D($1);
  $_$;


ALTER FUNCTION public.st_force_3d(public.geometry) OWNER TO postgres;

--
-- Name: st_force_3dm(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_force_3dm(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Force_3dm', 'ST_Force3DM', '2.1.0');
    SELECT ST_Force3DM($1);
  $_$;


ALTER FUNCTION public.st_force_3dm(public.geometry) OWNER TO postgres;

--
-- Name: st_force_3dz(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_force_3dz(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Force_3dz', 'ST_Force3DZ', '2.1.0');
    SELECT ST_Force3DZ($1);
  $_$;


ALTER FUNCTION public.st_force_3dz(public.geometry) OWNER TO postgres;

--
-- Name: st_force_4d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_force_4d(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Force_4d', 'ST_Force4D', '2.1.0');
    SELECT ST_Force4D($1);
  $_$;


ALTER FUNCTION public.st_force_4d(public.geometry) OWNER TO postgres;

--
-- Name: st_force_collection(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_force_collection(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Force_Collection', 'ST_ForceCollection', '2.1.0');
    SELECT ST_ForceCollection($1);
  $_$;


ALTER FUNCTION public.st_force_collection(public.geometry) OWNER TO postgres;

--
-- Name: st_forcecollection(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_forcecollection(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_force_collection';


ALTER FUNCTION public.st_forcecollection(public.geometry) OWNER TO postgres;

--
-- Name: st_forcecurve(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_forcecurve(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_force_curve';


ALTER FUNCTION public.st_forcecurve(public.geometry) OWNER TO postgres;

--
-- Name: st_forcepolygonccw(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_forcepolygonccw(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT COST 15 PARALLEL SAFE
    AS $_$ SELECT ST_Reverse(ST_ForcePolygonCW($1)) $_$;


ALTER FUNCTION public.st_forcepolygonccw(public.geometry) OWNER TO postgres;

--
-- Name: st_forcepolygoncw(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_forcepolygoncw(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 15 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_force_clockwise_poly';


ALTER FUNCTION public.st_forcepolygoncw(public.geometry) OWNER TO postgres;

--
-- Name: st_forcerhr(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_forcerhr(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_force_clockwise_poly';


ALTER FUNCTION public.st_forcerhr(public.geometry) OWNER TO postgres;

--
-- Name: st_forcesfs(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_forcesfs(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_force_sfs';


ALTER FUNCTION public.st_forcesfs(public.geometry) OWNER TO postgres;

--
-- Name: st_forcesfs(public.geometry, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_forcesfs(public.geometry, version text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_force_sfs';


ALTER FUNCTION public.st_forcesfs(public.geometry, version text) OWNER TO postgres;

--
-- Name: st_frechetdistance(public.geometry, public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_frechetdistance(geom1 public.geometry, geom2 public.geometry, double precision DEFAULT '-1'::integer) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_FrechetDistance';


ALTER FUNCTION public.st_frechetdistance(geom1 public.geometry, geom2 public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_generatepoints(public.geometry, numeric); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_generatepoints(area public.geometry, npoints numeric) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_GeneratePoints';


ALTER FUNCTION public.st_generatepoints(area public.geometry, npoints numeric) OWNER TO postgres;

--
-- Name: st_geogfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geogfromtext(text) RETURNS public.geography
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_from_text';


ALTER FUNCTION public.st_geogfromtext(text) OWNER TO postgres;

--
-- Name: st_geogfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geogfromwkb(bytea) RETURNS public.geography
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_from_binary';


ALTER FUNCTION public.st_geogfromwkb(bytea) OWNER TO postgres;

--
-- Name: st_geographyfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geographyfromtext(text) RETURNS public.geography
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_from_text';


ALTER FUNCTION public.st_geographyfromtext(text) OWNER TO postgres;

--
-- Name: st_geohash(public.geography, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geohash(geog public.geography, maxchars integer DEFAULT 0) RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_GeoHash';


ALTER FUNCTION public.st_geohash(geog public.geography, maxchars integer) OWNER TO postgres;

--
-- Name: st_geohash(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geohash(geom public.geometry, maxchars integer DEFAULT 0) RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_GeoHash';


ALTER FUNCTION public.st_geohash(geom public.geometry, maxchars integer) OWNER TO postgres;

--
-- Name: st_geomcollfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomcollfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE
	WHEN geometrytype(ST_GeomFromText($1)) = 'GEOMETRYCOLLECTION'
	THEN ST_GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_geomcollfromtext(text) OWNER TO postgres;

--
-- Name: st_geomcollfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomcollfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE
	WHEN geometrytype(ST_GeomFromText($1, $2)) = 'GEOMETRYCOLLECTION'
	THEN ST_GeomFromText($1,$2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_geomcollfromtext(text, integer) OWNER TO postgres;

--
-- Name: st_geomcollfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomcollfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE
	WHEN geometrytype(ST_GeomFromWKB($1)) = 'GEOMETRYCOLLECTION'
	THEN ST_GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_geomcollfromwkb(bytea) OWNER TO postgres;

--
-- Name: st_geomcollfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomcollfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE
	WHEN geometrytype(ST_GeomFromWKB($1, $2)) = 'GEOMETRYCOLLECTION'
	THEN ST_GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_geomcollfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: st_geometricmedian(public.geometry, double precision, integer, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometricmedian(g public.geometry, tolerance double precision DEFAULT NULL::double precision, max_iter integer DEFAULT 10000, fail_if_not_converged boolean DEFAULT false) RETURNS public.geometry
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_GeometricMedian';


ALTER FUNCTION public.st_geometricmedian(g public.geometry, tolerance double precision, max_iter integer, fail_if_not_converged boolean) OWNER TO postgres;

--
-- Name: st_geometryfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometryfromtext(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_from_text';


ALTER FUNCTION public.st_geometryfromtext(text) OWNER TO postgres;

--
-- Name: st_geometryfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometryfromtext(text, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_from_text';


ALTER FUNCTION public.st_geometryfromtext(text, integer) OWNER TO postgres;

--
-- Name: st_geometryn(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometryn(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_geometryn_collection';


ALTER FUNCTION public.st_geometryn(public.geometry, integer) OWNER TO postgres;

--
-- Name: st_geometrytype(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geometrytype(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geometry_geometrytype';


ALTER FUNCTION public.st_geometrytype(public.geometry) OWNER TO postgres;

--
-- Name: st_geomfromewkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomfromewkb(bytea) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOMFromEWKB';


ALTER FUNCTION public.st_geomfromewkb(bytea) OWNER TO postgres;

--
-- Name: st_geomfromewkt(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomfromewkt(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'parse_WKT_lwgeom';


ALTER FUNCTION public.st_geomfromewkt(text) OWNER TO postgres;

--
-- Name: st_geomfromgeohash(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomfromgeohash(text, integer DEFAULT NULL::integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$ SELECT CAST(ST_Box2dFromGeoHash($1, $2) AS geometry); $_$;


ALTER FUNCTION public.st_geomfromgeohash(text, integer) OWNER TO postgres;

--
-- Name: st_geomfromgeojson(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomfromgeojson(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geom_from_geojson';


ALTER FUNCTION public.st_geomfromgeojson(text) OWNER TO postgres;

--
-- Name: st_geomfromgml(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomfromgml(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT _ST_GeomFromGML($1, 0)$_$;


ALTER FUNCTION public.st_geomfromgml(text) OWNER TO postgres;

--
-- Name: st_geomfromgml(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomfromgml(text, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geom_from_gml';


ALTER FUNCTION public.st_geomfromgml(text, integer) OWNER TO postgres;

--
-- Name: st_geomfromkml(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomfromkml(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geom_from_kml';


ALTER FUNCTION public.st_geomfromkml(text) OWNER TO postgres;

--
-- Name: st_geomfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomfromtext(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_from_text';


ALTER FUNCTION public.st_geomfromtext(text) OWNER TO postgres;

--
-- Name: st_geomfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomfromtext(text, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_from_text';


ALTER FUNCTION public.st_geomfromtext(text, integer) OWNER TO postgres;

--
-- Name: st_geomfromtwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomfromtwkb(bytea) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOMFromTWKB';


ALTER FUNCTION public.st_geomfromtwkb(bytea) OWNER TO postgres;

--
-- Name: st_geomfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomfromwkb(bytea) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_from_WKB';


ALTER FUNCTION public.st_geomfromwkb(bytea) OWNER TO postgres;

--
-- Name: st_geomfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_geomfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_SetSRID(ST_GeomFromWKB($1), $2)$_$;


ALTER FUNCTION public.st_geomfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: st_gmltosql(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_gmltosql(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT _ST_GeomFromGML($1, 0)$_$;


ALTER FUNCTION public.st_gmltosql(text) OWNER TO postgres;

--
-- Name: st_gmltosql(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_gmltosql(text, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geom_from_gml';


ALTER FUNCTION public.st_gmltosql(text, integer) OWNER TO postgres;

--
-- Name: st_hasarc(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_hasarc(geometry public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_has_arc';


ALTER FUNCTION public.st_hasarc(geometry public.geometry) OWNER TO postgres;

--
-- Name: st_hausdorffdistance(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_hausdorffdistance(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'hausdorffdistance';


ALTER FUNCTION public.st_hausdorffdistance(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_hausdorffdistance(public.geometry, public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_hausdorffdistance(geom1 public.geometry, geom2 public.geometry, double precision) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'hausdorffdistancedensify';


ALTER FUNCTION public.st_hausdorffdistance(geom1 public.geometry, geom2 public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_interiorringn(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_interiorringn(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_interiorringn_polygon';


ALTER FUNCTION public.st_interiorringn(public.geometry, integer) OWNER TO postgres;

--
-- Name: st_interpolatepoint(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_interpolatepoint(line public.geometry, point public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_InterpolatePoint';


ALTER FUNCTION public.st_interpolatepoint(line public.geometry, point public.geometry) OWNER TO postgres;

--
-- Name: st_intersection(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_intersection(text, text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_Intersection($1::geometry, $2::geometry);  $_$;


ALTER FUNCTION public.st_intersection(text, text) OWNER TO postgres;

--
-- Name: st_intersection(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_intersection(public.geography, public.geography) RETURNS public.geography
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT geography(ST_Transform(ST_Intersection(ST_Transform(geometry($1), _ST_BestSRID($1, $2)), ST_Transform(geometry($2), _ST_BestSRID($1, $2))), 4326))$_$;


ALTER FUNCTION public.st_intersection(public.geography, public.geography) OWNER TO postgres;

--
-- Name: st_intersection(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_intersection(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'intersection';


ALTER FUNCTION public.st_intersection(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_intersects(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_intersects(text, text) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$ SELECT ST_Intersects($1::geometry, $2::geometry);  $_$;


ALTER FUNCTION public.st_intersects(text, text) OWNER TO postgres;

--
-- Name: st_intersects(public.geography, public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_intersects(public.geography, public.geography) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(&&) $2 AND _ST_Distance($1, $2, 0.0, false) < 0.00001$_$;


ALTER FUNCTION public.st_intersects(public.geography, public.geography) OWNER TO postgres;

--
-- Name: st_intersects(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_intersects(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(&&) $2 AND _ST_Intersects($1,$2)$_$;


ALTER FUNCTION public.st_intersects(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_isclosed(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_isclosed(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_isclosed';


ALTER FUNCTION public.st_isclosed(public.geometry) OWNER TO postgres;

--
-- Name: st_iscollection(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_iscollection(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 5 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_IsCollection';


ALTER FUNCTION public.st_iscollection(public.geometry) OWNER TO postgres;

--
-- Name: st_isempty(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_isempty(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_isempty';


ALTER FUNCTION public.st_isempty(public.geometry) OWNER TO postgres;

--
-- Name: st_ispolygonccw(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_ispolygonccw(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_IsPolygonCCW';


ALTER FUNCTION public.st_ispolygonccw(public.geometry) OWNER TO postgres;

--
-- Name: st_ispolygoncw(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_ispolygoncw(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_IsPolygonCW';


ALTER FUNCTION public.st_ispolygoncw(public.geometry) OWNER TO postgres;

--
-- Name: st_isring(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_isring(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'isring';


ALTER FUNCTION public.st_isring(public.geometry) OWNER TO postgres;

--
-- Name: st_issimple(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_issimple(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 25 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'issimple';


ALTER FUNCTION public.st_issimple(public.geometry) OWNER TO postgres;

--
-- Name: st_isvalid(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_isvalid(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 1000 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'isvalid';


ALTER FUNCTION public.st_isvalid(public.geometry) OWNER TO postgres;

--
-- Name: st_isvalid(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_isvalid(public.geometry, integer) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT (ST_isValidDetail($1, $2)).valid$_$;


ALTER FUNCTION public.st_isvalid(public.geometry, integer) OWNER TO postgres;

--
-- Name: st_isvaliddetail(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_isvaliddetail(public.geometry) RETURNS public.valid_detail
    LANGUAGE c IMMUTABLE STRICT COST 1000 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'isvaliddetail';


ALTER FUNCTION public.st_isvaliddetail(public.geometry) OWNER TO postgres;

--
-- Name: st_isvaliddetail(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_isvaliddetail(public.geometry, integer) RETURNS public.valid_detail
    LANGUAGE c IMMUTABLE STRICT COST 1000 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'isvaliddetail';


ALTER FUNCTION public.st_isvaliddetail(public.geometry, integer) OWNER TO postgres;

--
-- Name: st_isvalidreason(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_isvalidreason(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT COST 1000 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'isvalidreason';


ALTER FUNCTION public.st_isvalidreason(public.geometry) OWNER TO postgres;

--
-- Name: st_isvalidreason(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_isvalidreason(public.geometry, integer) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
SELECT CASE WHEN valid THEN 'Valid Geometry' ELSE reason END FROM (
	SELECT (ST_isValidDetail($1, $2)).*
) foo
	$_$;


ALTER FUNCTION public.st_isvalidreason(public.geometry, integer) OWNER TO postgres;

--
-- Name: st_isvalidtrajectory(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_isvalidtrajectory(public.geometry) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_IsValidTrajectory';


ALTER FUNCTION public.st_isvalidtrajectory(public.geometry) OWNER TO postgres;

--
-- Name: st_length(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_length(text) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_Length($1::geometry);  $_$;


ALTER FUNCTION public.st_length(text) OWNER TO postgres;

--
-- Name: st_length(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_length(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_length2d_linestring';


ALTER FUNCTION public.st_length(public.geometry) OWNER TO postgres;

--
-- Name: st_length(public.geography, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_length(geog public.geography, use_spheroid boolean DEFAULT true) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_length';


ALTER FUNCTION public.st_length(geog public.geography, use_spheroid boolean) OWNER TO postgres;

--
-- Name: st_length2d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_length2d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_length2d_linestring';


ALTER FUNCTION public.st_length2d(public.geometry) OWNER TO postgres;

--
-- Name: st_length2d_spheroid(public.geometry, public.spheroid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_length2d_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Length2D_Spheroid', 'ST_Length2DSpheroid', '2.2.0');
    SELECT ST_Length2DSpheroid($1,$2);
  $_$;


ALTER FUNCTION public.st_length2d_spheroid(public.geometry, public.spheroid) OWNER TO postgres;

--
-- Name: st_length2dspheroid(public.geometry, public.spheroid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_length2dspheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 500 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_length2d_ellipsoid';


ALTER FUNCTION public.st_length2dspheroid(public.geometry, public.spheroid) OWNER TO postgres;

--
-- Name: st_length_spheroid(public.geometry, public.spheroid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_length_spheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Length_Spheroid', 'ST_LengthSpheroid', '2.2.0');
    SELECT ST_LengthSpheroid($1,$2);
  $_$;


ALTER FUNCTION public.st_length_spheroid(public.geometry, public.spheroid) OWNER TO postgres;

--
-- Name: st_lengthspheroid(public.geometry, public.spheroid); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_lengthspheroid(public.geometry, public.spheroid) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 500 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_length_ellipsoid_linestring';


ALTER FUNCTION public.st_lengthspheroid(public.geometry, public.spheroid) OWNER TO postgres;

--
-- Name: st_line_interpolate_point(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_line_interpolate_point(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Line_Interpolate_Point', 'ST_LineInterpolatePoint', '2.1.0');
    SELECT ST_LineInterpolatePoint($1, $2);
  $_$;


ALTER FUNCTION public.st_line_interpolate_point(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_line_locate_point(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_line_locate_point(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Line_Locate_Point', 'ST_LineLocatePoint', '2.1.0');
     SELECT ST_LineLocatePoint($1, $2);
  $_$;


ALTER FUNCTION public.st_line_locate_point(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_line_substring(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_line_substring(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Line_Substring', 'ST_LineSubstring', '2.1.0');
     SELECT ST_LineSubstring($1, $2, $3);
  $_$;


ALTER FUNCTION public.st_line_substring(public.geometry, double precision, double precision) OWNER TO postgres;

--
-- Name: st_linecrossingdirection(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_linecrossingdirection(geom1 public.geometry, geom2 public.geometry) RETURNS integer
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$ SELECT CASE WHEN NOT $1 OPERATOR(&&) $2 THEN 0 ELSE _ST_LineCrossingDirection($1,$2) END $_$;


ALTER FUNCTION public.st_linecrossingdirection(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_linefromencodedpolyline(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_linefromencodedpolyline(text, integer DEFAULT 5) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'line_from_encoded_polyline';


ALTER FUNCTION public.st_linefromencodedpolyline(text, integer) OWNER TO postgres;

--
-- Name: st_linefrommultipoint(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_linefrommultipoint(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_line_from_mpoint';


ALTER FUNCTION public.st_linefrommultipoint(public.geometry) OWNER TO postgres;

--
-- Name: st_linefromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_linefromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromText($1)) = 'LINESTRING'
	THEN ST_GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_linefromtext(text) OWNER TO postgres;

--
-- Name: st_linefromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_linefromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromText($1, $2)) = 'LINESTRING'
	THEN ST_GeomFromText($1,$2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_linefromtext(text, integer) OWNER TO postgres;

--
-- Name: st_linefromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_linefromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1)) = 'LINESTRING'
	THEN ST_GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_linefromwkb(bytea) OWNER TO postgres;

--
-- Name: st_linefromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_linefromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1, $2)) = 'LINESTRING'
	THEN ST_GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_linefromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: st_lineinterpolatepoint(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_lineinterpolatepoint(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_line_interpolate_point';


ALTER FUNCTION public.st_lineinterpolatepoint(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_linelocatepoint(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_linelocatepoint(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_line_locate_point';


ALTER FUNCTION public.st_linelocatepoint(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_linemerge(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_linemerge(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'linemerge';


ALTER FUNCTION public.st_linemerge(public.geometry) OWNER TO postgres;

--
-- Name: st_linestringfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_linestringfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1)) = 'LINESTRING'
	THEN ST_GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_linestringfromwkb(bytea) OWNER TO postgres;

--
-- Name: st_linestringfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_linestringfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1, $2)) = 'LINESTRING'
	THEN ST_GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_linestringfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: st_linesubstring(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_linesubstring(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_line_substring';


ALTER FUNCTION public.st_linesubstring(public.geometry, double precision, double precision) OWNER TO postgres;

--
-- Name: st_linetocurve(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_linetocurve(geometry public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_line_desegmentize';


ALTER FUNCTION public.st_linetocurve(geometry public.geometry) OWNER TO postgres;

--
-- Name: st_locate_along_measure(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_locate_along_measure(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT ST_locate_between_measures($1, $2, $2) $_$;


ALTER FUNCTION public.st_locate_along_measure(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_locate_between_measures(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_locate_between_measures(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_locate_between_m';


ALTER FUNCTION public.st_locate_between_measures(public.geometry, double precision, double precision) OWNER TO postgres;

--
-- Name: st_locatealong(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_locatealong(geometry public.geometry, measure double precision, leftrightoffset double precision DEFAULT 0.0) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_LocateAlong';


ALTER FUNCTION public.st_locatealong(geometry public.geometry, measure double precision, leftrightoffset double precision) OWNER TO postgres;

--
-- Name: st_locatebetween(public.geometry, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_locatebetween(geometry public.geometry, frommeasure double precision, tomeasure double precision, leftrightoffset double precision DEFAULT 0.0) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_LocateBetween';


ALTER FUNCTION public.st_locatebetween(geometry public.geometry, frommeasure double precision, tomeasure double precision, leftrightoffset double precision) OWNER TO postgres;

--
-- Name: st_locatebetweenelevations(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_locatebetweenelevations(geometry public.geometry, fromelevation double precision, toelevation double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_LocateBetweenElevations';


ALTER FUNCTION public.st_locatebetweenelevations(geometry public.geometry, fromelevation double precision, toelevation double precision) OWNER TO postgres;

--
-- Name: st_longestline(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_longestline(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT _ST_LongestLine(ST_ConvexHull($1), ST_ConvexHull($2))$_$;


ALTER FUNCTION public.st_longestline(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_m(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_m(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_m_point';


ALTER FUNCTION public.st_m(public.geometry) OWNER TO postgres;

--
-- Name: st_makebox2d(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_makebox2d(geom1 public.geometry, geom2 public.geometry) RETURNS public.box2d
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX2D_construct';


ALTER FUNCTION public.st_makebox2d(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_makeenvelope(double precision, double precision, double precision, double precision, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_makeenvelope(double precision, double precision, double precision, double precision, integer DEFAULT 0) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_MakeEnvelope';


ALTER FUNCTION public.st_makeenvelope(double precision, double precision, double precision, double precision, integer) OWNER TO postgres;

--
-- Name: st_makeline(public.geometry[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_makeline(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_makeline_garray';


ALTER FUNCTION public.st_makeline(public.geometry[]) OWNER TO postgres;

--
-- Name: st_makeline(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_makeline(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_makeline';


ALTER FUNCTION public.st_makeline(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_makepoint(double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_makepoint(double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_makepoint';


ALTER FUNCTION public.st_makepoint(double precision, double precision) OWNER TO postgres;

--
-- Name: st_makepoint(double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_makepoint(double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_makepoint';


ALTER FUNCTION public.st_makepoint(double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: st_makepoint(double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_makepoint(double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_makepoint';


ALTER FUNCTION public.st_makepoint(double precision, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: st_makepointm(double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_makepointm(double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_makepoint3dm';


ALTER FUNCTION public.st_makepointm(double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: st_makepolygon(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_makepolygon(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_makepoly';


ALTER FUNCTION public.st_makepolygon(public.geometry) OWNER TO postgres;

--
-- Name: st_makepolygon(public.geometry, public.geometry[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_makepolygon(public.geometry, public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_makepoly';


ALTER FUNCTION public.st_makepolygon(public.geometry, public.geometry[]) OWNER TO postgres;

--
-- Name: st_makevalid(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_makevalid(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_MakeValid';


ALTER FUNCTION public.st_makevalid(public.geometry) OWNER TO postgres;

--
-- Name: st_maxdistance(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_maxdistance(geom1 public.geometry, geom2 public.geometry) RETURNS double precision
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT _ST_MaxDistance(ST_ConvexHull($1), ST_ConvexHull($2))$_$;


ALTER FUNCTION public.st_maxdistance(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_mem_size(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_mem_size(public.geometry) RETURNS integer
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$ SELECT _postgis_deprecate('ST_Mem_Size', 'ST_MemSize', '2.2.0');
    SELECT ST_MemSize($1);
  $_$;


ALTER FUNCTION public.st_mem_size(public.geometry) OWNER TO postgres;

--
-- Name: st_memsize(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_memsize(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT COST 5 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_mem_size';


ALTER FUNCTION public.st_memsize(public.geometry) OWNER TO postgres;

--
-- Name: st_minimumboundingcircle(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_minimumboundingcircle(inputgeom public.geometry, segs_per_quarter integer DEFAULT 48) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_MinimumBoundingCircle';


ALTER FUNCTION public.st_minimumboundingcircle(inputgeom public.geometry, segs_per_quarter integer) OWNER TO postgres;

--
-- Name: st_minimumboundingradius(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_minimumboundingradius(public.geometry, OUT center public.geometry, OUT radius double precision) RETURNS record
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_MinimumBoundingRadius';


ALTER FUNCTION public.st_minimumboundingradius(public.geometry, OUT center public.geometry, OUT radius double precision) OWNER TO postgres;

--
-- Name: st_minimumclearance(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_minimumclearance(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_MinimumClearance';


ALTER FUNCTION public.st_minimumclearance(public.geometry) OWNER TO postgres;

--
-- Name: st_minimumclearanceline(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_minimumclearanceline(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_MinimumClearanceLine';


ALTER FUNCTION public.st_minimumclearanceline(public.geometry) OWNER TO postgres;

--
-- Name: st_mlinefromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_mlinefromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromText($1)) = 'MULTILINESTRING'
	THEN ST_GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_mlinefromtext(text) OWNER TO postgres;

--
-- Name: st_mlinefromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_mlinefromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE
	WHEN geometrytype(ST_GeomFromText($1, $2)) = 'MULTILINESTRING'
	THEN ST_GeomFromText($1,$2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_mlinefromtext(text, integer) OWNER TO postgres;

--
-- Name: st_mlinefromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_mlinefromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1)) = 'MULTILINESTRING'
	THEN ST_GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_mlinefromwkb(bytea) OWNER TO postgres;

--
-- Name: st_mlinefromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_mlinefromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1, $2)) = 'MULTILINESTRING'
	THEN ST_GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_mlinefromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: st_mpointfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_mpointfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromText($1)) = 'MULTIPOINT'
	THEN ST_GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_mpointfromtext(text) OWNER TO postgres;

--
-- Name: st_mpointfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_mpointfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromText($1, $2)) = 'MULTIPOINT'
	THEN ST_GeomFromText($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_mpointfromtext(text, integer) OWNER TO postgres;

--
-- Name: st_mpointfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_mpointfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1)) = 'MULTIPOINT'
	THEN ST_GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_mpointfromwkb(bytea) OWNER TO postgres;

--
-- Name: st_mpointfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_mpointfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1, $2)) = 'MULTIPOINT'
	THEN ST_GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_mpointfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: st_mpolyfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_mpolyfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromText($1)) = 'MULTIPOLYGON'
	THEN ST_GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_mpolyfromtext(text) OWNER TO postgres;

--
-- Name: st_mpolyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_mpolyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromText($1, $2)) = 'MULTIPOLYGON'
	THEN ST_GeomFromText($1,$2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_mpolyfromtext(text, integer) OWNER TO postgres;

--
-- Name: st_mpolyfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_mpolyfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1)) = 'MULTIPOLYGON'
	THEN ST_GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_mpolyfromwkb(bytea) OWNER TO postgres;

--
-- Name: st_mpolyfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_mpolyfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1, $2)) = 'MULTIPOLYGON'
	THEN ST_GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_mpolyfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: st_multi(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_multi(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_force_multi';


ALTER FUNCTION public.st_multi(public.geometry) OWNER TO postgres;

--
-- Name: st_multilinefromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_multilinefromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1)) = 'MULTILINESTRING'
	THEN ST_GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_multilinefromwkb(bytea) OWNER TO postgres;

--
-- Name: st_multilinestringfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_multilinestringfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_MLineFromText($1)$_$;


ALTER FUNCTION public.st_multilinestringfromtext(text) OWNER TO postgres;

--
-- Name: st_multilinestringfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_multilinestringfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_MLineFromText($1, $2)$_$;


ALTER FUNCTION public.st_multilinestringfromtext(text, integer) OWNER TO postgres;

--
-- Name: st_multipointfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_multipointfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_MPointFromText($1)$_$;


ALTER FUNCTION public.st_multipointfromtext(text) OWNER TO postgres;

--
-- Name: st_multipointfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_multipointfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1)) = 'MULTIPOINT'
	THEN ST_GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_multipointfromwkb(bytea) OWNER TO postgres;

--
-- Name: st_multipointfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_multipointfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1,$2)) = 'MULTIPOINT'
	THEN ST_GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_multipointfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: st_multipolyfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_multipolyfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1)) = 'MULTIPOLYGON'
	THEN ST_GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_multipolyfromwkb(bytea) OWNER TO postgres;

--
-- Name: st_multipolyfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_multipolyfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1, $2)) = 'MULTIPOLYGON'
	THEN ST_GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_multipolyfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: st_multipolygonfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_multipolygonfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_MPolyFromText($1)$_$;


ALTER FUNCTION public.st_multipolygonfromtext(text) OWNER TO postgres;

--
-- Name: st_multipolygonfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_multipolygonfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_MPolyFromText($1, $2)$_$;


ALTER FUNCTION public.st_multipolygonfromtext(text, integer) OWNER TO postgres;

--
-- Name: st_ndims(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_ndims(public.geometry) RETURNS smallint
    LANGUAGE c IMMUTABLE STRICT COST 5 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_ndims';


ALTER FUNCTION public.st_ndims(public.geometry) OWNER TO postgres;

--
-- Name: st_node(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_node(g public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_Node';


ALTER FUNCTION public.st_node(g public.geometry) OWNER TO postgres;

--
-- Name: st_normalize(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_normalize(geom public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_Normalize';


ALTER FUNCTION public.st_normalize(geom public.geometry) OWNER TO postgres;

--
-- Name: st_npoints(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_npoints(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_npoints';


ALTER FUNCTION public.st_npoints(public.geometry) OWNER TO postgres;

--
-- Name: st_nrings(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_nrings(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_nrings';


ALTER FUNCTION public.st_nrings(public.geometry) OWNER TO postgres;

--
-- Name: st_numgeometries(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_numgeometries(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_numgeometries_collection';


ALTER FUNCTION public.st_numgeometries(public.geometry) OWNER TO postgres;

--
-- Name: st_numinteriorring(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_numinteriorring(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_numinteriorrings_polygon';


ALTER FUNCTION public.st_numinteriorring(public.geometry) OWNER TO postgres;

--
-- Name: st_numinteriorrings(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_numinteriorrings(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_numinteriorrings_polygon';


ALTER FUNCTION public.st_numinteriorrings(public.geometry) OWNER TO postgres;

--
-- Name: st_numpatches(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_numpatches(public.geometry) RETURNS integer
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN ST_GeometryType($1) = 'ST_PolyhedralSurface'
	THEN ST_NumGeometries($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_numpatches(public.geometry) OWNER TO postgres;

--
-- Name: st_numpoints(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_numpoints(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_numpoints_linestring';


ALTER FUNCTION public.st_numpoints(public.geometry) OWNER TO postgres;

--
-- Name: st_offsetcurve(public.geometry, double precision, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_offsetcurve(line public.geometry, distance double precision, params text DEFAULT ''::text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_OffsetCurve';


ALTER FUNCTION public.st_offsetcurve(line public.geometry, distance double precision, params text) OWNER TO postgres;

--
-- Name: st_orderingequals(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_orderingequals(geometrya public.geometry, geometryb public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$
	SELECT $1 OPERATOR(~=) $2 AND _ST_OrderingEquals($1, $2)
	$_$;


ALTER FUNCTION public.st_orderingequals(geometrya public.geometry, geometryb public.geometry) OWNER TO postgres;

--
-- Name: st_overlaps(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_overlaps(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(&&) $2 AND _ST_Overlaps($1,$2)$_$;


ALTER FUNCTION public.st_overlaps(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_patchn(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_patchn(public.geometry, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN ST_GeometryType($1) = 'ST_PolyhedralSurface'
	THEN ST_GeometryN($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_patchn(public.geometry, integer) OWNER TO postgres;

--
-- Name: st_perimeter(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_perimeter(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_perimeter2d_poly';


ALTER FUNCTION public.st_perimeter(public.geometry) OWNER TO postgres;

--
-- Name: st_perimeter(public.geography, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_perimeter(geog public.geography, use_spheroid boolean DEFAULT true) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_perimeter';


ALTER FUNCTION public.st_perimeter(geog public.geography, use_spheroid boolean) OWNER TO postgres;

--
-- Name: st_perimeter2d(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_perimeter2d(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT COST 10 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_perimeter2d_poly';


ALTER FUNCTION public.st_perimeter2d(public.geometry) OWNER TO postgres;

--
-- Name: st_point(double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_point(double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_makepoint';


ALTER FUNCTION public.st_point(double precision, double precision) OWNER TO postgres;

--
-- Name: st_point_inside_circle(public.geometry, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_point_inside_circle(public.geometry, double precision, double precision, double precision) RETURNS boolean
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Point_Inside_Circle', 'ST_PointInsideCircle', '2.2.0');
    SELECT ST_PointInsideCircle($1,$2,$3,$4);
  $_$;


ALTER FUNCTION public.st_point_inside_circle(public.geometry, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: st_pointfromgeohash(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_pointfromgeohash(text, integer DEFAULT NULL::integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'point_from_geohash';


ALTER FUNCTION public.st_pointfromgeohash(text, integer) OWNER TO postgres;

--
-- Name: st_pointfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_pointfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromText($1)) = 'POINT'
	THEN ST_GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_pointfromtext(text) OWNER TO postgres;

--
-- Name: st_pointfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_pointfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromText($1, $2)) = 'POINT'
	THEN ST_GeomFromText($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_pointfromtext(text, integer) OWNER TO postgres;

--
-- Name: st_pointfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_pointfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1)) = 'POINT'
	THEN ST_GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_pointfromwkb(bytea) OWNER TO postgres;

--
-- Name: st_pointfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_pointfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1, $2)) = 'POINT'
	THEN ST_GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_pointfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: st_pointinsidecircle(public.geometry, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_pointinsidecircle(public.geometry, double precision, double precision, double precision) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_inside_circle_point';


ALTER FUNCTION public.st_pointinsidecircle(public.geometry, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: st_pointn(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_pointn(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_pointn_linestring';


ALTER FUNCTION public.st_pointn(public.geometry, integer) OWNER TO postgres;

--
-- Name: st_pointonsurface(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_pointonsurface(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pointonsurface';


ALTER FUNCTION public.st_pointonsurface(public.geometry) OWNER TO postgres;

--
-- Name: st_points(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_points(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_Points';


ALTER FUNCTION public.st_points(public.geometry) OWNER TO postgres;

--
-- Name: st_polyfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_polyfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromText($1)) = 'POLYGON'
	THEN ST_GeomFromText($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_polyfromtext(text) OWNER TO postgres;

--
-- Name: st_polyfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_polyfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromText($1, $2)) = 'POLYGON'
	THEN ST_GeomFromText($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_polyfromtext(text, integer) OWNER TO postgres;

--
-- Name: st_polyfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_polyfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1)) = 'POLYGON'
	THEN ST_GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_polyfromwkb(bytea) OWNER TO postgres;

--
-- Name: st_polyfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_polyfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1, $2)) = 'POLYGON'
	THEN ST_GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_polyfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: st_polygon(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_polygon(public.geometry, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT ST_SetSRID(ST_MakePolygon($1), $2)
	$_$;


ALTER FUNCTION public.st_polygon(public.geometry, integer) OWNER TO postgres;

--
-- Name: st_polygonfromtext(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_polygonfromtext(text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_PolyFromText($1)$_$;


ALTER FUNCTION public.st_polygonfromtext(text) OWNER TO postgres;

--
-- Name: st_polygonfromtext(text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_polygonfromtext(text, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_PolyFromText($1, $2)$_$;


ALTER FUNCTION public.st_polygonfromtext(text, integer) OWNER TO postgres;

--
-- Name: st_polygonfromwkb(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_polygonfromwkb(bytea) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1)) = 'POLYGON'
	THEN ST_GeomFromWKB($1)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_polygonfromwkb(bytea) OWNER TO postgres;

--
-- Name: st_polygonfromwkb(bytea, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_polygonfromwkb(bytea, integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$
	SELECT CASE WHEN geometrytype(ST_GeomFromWKB($1,$2)) = 'POLYGON'
	THEN ST_GeomFromWKB($1, $2)
	ELSE NULL END
	$_$;


ALTER FUNCTION public.st_polygonfromwkb(bytea, integer) OWNER TO postgres;

--
-- Name: st_polygonize(public.geometry[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_polygonize(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'polygonize_garray';


ALTER FUNCTION public.st_polygonize(public.geometry[]) OWNER TO postgres;

--
-- Name: st_project(public.geography, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_project(geog public.geography, distance double precision, azimuth double precision) RETURNS public.geography
    LANGUAGE c IMMUTABLE COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_project';


ALTER FUNCTION public.st_project(geog public.geography, distance double precision, azimuth double precision) OWNER TO postgres;

--
-- Name: st_relate(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_relate(geom1 public.geometry, geom2 public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'relate_full';


ALTER FUNCTION public.st_relate(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_relate(public.geometry, public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_relate(geom1 public.geometry, geom2 public.geometry, integer) RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'relate_full';


ALTER FUNCTION public.st_relate(geom1 public.geometry, geom2 public.geometry, integer) OWNER TO postgres;

--
-- Name: st_relate(public.geometry, public.geometry, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_relate(geom1 public.geometry, geom2 public.geometry, text) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'relate_pattern';


ALTER FUNCTION public.st_relate(geom1 public.geometry, geom2 public.geometry, text) OWNER TO postgres;

--
-- Name: st_relatematch(text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_relatematch(text, text) RETURNS boolean
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_RelateMatch';


ALTER FUNCTION public.st_relatematch(text, text) OWNER TO postgres;

--
-- Name: st_removepoint(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_removepoint(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_removepoint';


ALTER FUNCTION public.st_removepoint(public.geometry, integer) OWNER TO postgres;

--
-- Name: st_removerepeatedpoints(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_removerepeatedpoints(geom public.geometry, tolerance double precision DEFAULT 0.0) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_RemoveRepeatedPoints';


ALTER FUNCTION public.st_removerepeatedpoints(geom public.geometry, tolerance double precision) OWNER TO postgres;

--
-- Name: st_reverse(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_reverse(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_reverse';


ALTER FUNCTION public.st_reverse(public.geometry) OWNER TO postgres;

--
-- Name: st_rotate(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_rotate(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_Affine($1,  cos($2), -sin($2), 0,  sin($2), cos($2), 0,  0, 0, 1,  0, 0, 0)$_$;


ALTER FUNCTION public.st_rotate(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_rotate(public.geometry, double precision, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_rotate(public.geometry, double precision, public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_Affine($1,  cos($2), -sin($2), 0,  sin($2),  cos($2), 0, 0, 0, 1, ST_X($3) - cos($2) * ST_X($3) + sin($2) * ST_Y($3), ST_Y($3) - sin($2) * ST_X($3) - cos($2) * ST_Y($3), 0)$_$;


ALTER FUNCTION public.st_rotate(public.geometry, double precision, public.geometry) OWNER TO postgres;

--
-- Name: st_rotate(public.geometry, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_rotate(public.geometry, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_Affine($1,  cos($2), -sin($2), 0,  sin($2),  cos($2), 0, 0, 0, 1,	$3 - cos($2) * $3 + sin($2) * $4, $4 - sin($2) * $3 - cos($2) * $4, 0)$_$;


ALTER FUNCTION public.st_rotate(public.geometry, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: st_rotatex(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_rotatex(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_Affine($1, 1, 0, 0, 0, cos($2), -sin($2), 0, sin($2), cos($2), 0, 0, 0)$_$;


ALTER FUNCTION public.st_rotatex(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_rotatey(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_rotatey(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_Affine($1,  cos($2), 0, sin($2),  0, 1, 0,  -sin($2), 0, cos($2), 0,  0, 0)$_$;


ALTER FUNCTION public.st_rotatey(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_rotatez(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_rotatez(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_Rotate($1, $2)$_$;


ALTER FUNCTION public.st_rotatez(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_scale(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_scale(public.geometry, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_Scale';


ALTER FUNCTION public.st_scale(public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: st_scale(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_scale(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_Scale($1, $2, $3, 1)$_$;


ALTER FUNCTION public.st_scale(public.geometry, double precision, double precision) OWNER TO postgres;

--
-- Name: st_scale(public.geometry, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_scale(public.geometry, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_Scale($1, ST_MakePoint($2, $3, $4))$_$;


ALTER FUNCTION public.st_scale(public.geometry, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: st_segmentize(public.geography, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_segmentize(geog public.geography, max_segment_length double precision) RETURNS public.geography
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geography_segmentize';


ALTER FUNCTION public.st_segmentize(geog public.geography, max_segment_length double precision) OWNER TO postgres;

--
-- Name: st_segmentize(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_segmentize(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_segmentize2d';


ALTER FUNCTION public.st_segmentize(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_seteffectivearea(public.geometry, double precision, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_seteffectivearea(public.geometry, double precision DEFAULT '-1'::integer, integer DEFAULT 1) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_SetEffectiveArea';


ALTER FUNCTION public.st_seteffectivearea(public.geometry, double precision, integer) OWNER TO postgres;

--
-- Name: st_setpoint(public.geometry, integer, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_setpoint(public.geometry, integer, public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_setpoint_linestring';


ALTER FUNCTION public.st_setpoint(public.geometry, integer, public.geometry) OWNER TO postgres;

--
-- Name: st_setsrid(public.geography, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_setsrid(geog public.geography, srid integer) RETURNS public.geography
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_set_srid';


ALTER FUNCTION public.st_setsrid(geog public.geography, srid integer) OWNER TO postgres;

--
-- Name: st_setsrid(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_setsrid(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_set_srid';


ALTER FUNCTION public.st_setsrid(public.geometry, integer) OWNER TO postgres;

--
-- Name: st_sharedpaths(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_sharedpaths(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_SharedPaths';


ALTER FUNCTION public.st_sharedpaths(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_shift_longitude(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_shift_longitude(public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$ SELECT _postgis_deprecate('ST_Shift_Longitude', 'ST_ShiftLongitude', '2.2.0');
    SELECT ST_ShiftLongitude($1);
  $_$;


ALTER FUNCTION public.st_shift_longitude(public.geometry) OWNER TO postgres;

--
-- Name: st_shiftlongitude(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_shiftlongitude(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_longitude_shift';


ALTER FUNCTION public.st_shiftlongitude(public.geometry) OWNER TO postgres;

--
-- Name: st_shortestline(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_shortestline(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_shortestline2d';


ALTER FUNCTION public.st_shortestline(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_simplify(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_simplify(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_simplify2d';


ALTER FUNCTION public.st_simplify(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_simplify(public.geometry, double precision, boolean); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_simplify(public.geometry, double precision, boolean) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_simplify2d';


ALTER FUNCTION public.st_simplify(public.geometry, double precision, boolean) OWNER TO postgres;

--
-- Name: st_simplifypreservetopology(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_simplifypreservetopology(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'topologypreservesimplify';


ALTER FUNCTION public.st_simplifypreservetopology(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_simplifyvw(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_simplifyvw(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_SetEffectiveArea';


ALTER FUNCTION public.st_simplifyvw(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_snap(public.geometry, public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_snap(geom1 public.geometry, geom2 public.geometry, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_Snap';


ALTER FUNCTION public.st_snap(geom1 public.geometry, geom2 public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_snaptogrid(public.geometry, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_snaptogrid(public.geometry, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_SnapToGrid($1, 0, 0, $2, $2)$_$;


ALTER FUNCTION public.st_snaptogrid(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_snaptogrid(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_snaptogrid(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT COST 1 PARALLEL SAFE
    AS $_$SELECT ST_SnapToGrid($1, 0, 0, $2, $3)$_$;


ALTER FUNCTION public.st_snaptogrid(public.geometry, double precision, double precision) OWNER TO postgres;

--
-- Name: st_snaptogrid(public.geometry, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_snaptogrid(public.geometry, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_snaptogrid';


ALTER FUNCTION public.st_snaptogrid(public.geometry, double precision, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: st_snaptogrid(public.geometry, public.geometry, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_snaptogrid(geom1 public.geometry, geom2 public.geometry, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_snaptogrid_pointoff';


ALTER FUNCTION public.st_snaptogrid(geom1 public.geometry, geom2 public.geometry, double precision, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: st_split(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_split(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_Split';


ALTER FUNCTION public.st_split(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_srid(public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_srid(geog public.geography) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_get_srid';


ALTER FUNCTION public.st_srid(geog public.geography) OWNER TO postgres;

--
-- Name: st_srid(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_srid(public.geometry) RETURNS integer
    LANGUAGE c IMMUTABLE STRICT COST 5 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_get_srid';


ALTER FUNCTION public.st_srid(public.geometry) OWNER TO postgres;

--
-- Name: st_startpoint(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_startpoint(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_startpoint_linestring';


ALTER FUNCTION public.st_startpoint(public.geometry) OWNER TO postgres;

--
-- Name: st_subdivide(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_subdivide(geom public.geometry, maxvertices integer DEFAULT 256) RETURNS SETOF public.geometry
    LANGUAGE c IMMUTABLE STRICT COST 100 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_Subdivide';


ALTER FUNCTION public.st_subdivide(geom public.geometry, maxvertices integer) OWNER TO postgres;

--
-- Name: st_summary(public.geography); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_summary(public.geography) RETURNS text
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_summary';


ALTER FUNCTION public.st_summary(public.geography) OWNER TO postgres;

--
-- Name: st_summary(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_summary(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT COST 25 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_summary';


ALTER FUNCTION public.st_summary(public.geometry) OWNER TO postgres;

--
-- Name: st_swapordinates(public.geometry, cstring); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_swapordinates(geom public.geometry, ords cstring) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_SwapOrdinates';


ALTER FUNCTION public.st_swapordinates(geom public.geometry, ords cstring) OWNER TO postgres;

--
-- Name: st_symdifference(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_symdifference(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'symdifference';


ALTER FUNCTION public.st_symdifference(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_symmetricdifference(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_symmetricdifference(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'symdifference';


ALTER FUNCTION public.st_symmetricdifference(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_touches(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_touches(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $1 OPERATOR(&&) $2 AND _ST_Touches($1,$2)$_$;


ALTER FUNCTION public.st_touches(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_transform(public.geometry, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_transform(public.geometry, integer) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'transform';


ALTER FUNCTION public.st_transform(public.geometry, integer) OWNER TO postgres;

--
-- Name: st_transform(public.geometry, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_transform(geom public.geometry, to_proj text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT postgis_transform_geometry($1, proj4text, $2, 0)
FROM spatial_ref_sys WHERE srid=ST_SRID($1);$_$;


ALTER FUNCTION public.st_transform(geom public.geometry, to_proj text) OWNER TO postgres;

--
-- Name: st_transform(public.geometry, text, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_transform(geom public.geometry, from_proj text, to_srid integer) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT postgis_transform_geometry($1, $2, proj4text, $3)
FROM spatial_ref_sys WHERE srid=$3;$_$;


ALTER FUNCTION public.st_transform(geom public.geometry, from_proj text, to_srid integer) OWNER TO postgres;

--
-- Name: st_transform(public.geometry, text, text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_transform(geom public.geometry, from_proj text, to_proj text) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT postgis_transform_geometry($1, $2, $3, 0)$_$;


ALTER FUNCTION public.st_transform(geom public.geometry, from_proj text, to_proj text) OWNER TO postgres;

--
-- Name: st_translate(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_translate(public.geometry, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_Translate($1, $2, $3, 0)$_$;


ALTER FUNCTION public.st_translate(public.geometry, double precision, double precision) OWNER TO postgres;

--
-- Name: st_translate(public.geometry, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_translate(public.geometry, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_Affine($1, 1, 0, 0, 0, 1, 0, 0, 0, 1, $2, $3, $4)$_$;


ALTER FUNCTION public.st_translate(public.geometry, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: st_transscale(public.geometry, double precision, double precision, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_transscale(public.geometry, double precision, double precision, double precision, double precision) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE STRICT PARALLEL SAFE
    AS $_$SELECT ST_Affine($1,  $4, 0, 0,  0, $5, 0,
		0, 0, 1,  $2 * $4, $3 * $5, 0)$_$;


ALTER FUNCTION public.st_transscale(public.geometry, double precision, double precision, double precision, double precision) OWNER TO postgres;

--
-- Name: st_unaryunion(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_unaryunion(public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_UnaryUnion';


ALTER FUNCTION public.st_unaryunion(public.geometry) OWNER TO postgres;

--
-- Name: st_union(public.geometry[]); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_union(public.geometry[]) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'pgis_union_geometry_array';


ALTER FUNCTION public.st_union(public.geometry[]) OWNER TO postgres;

--
-- Name: st_union(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_union(geom1 public.geometry, geom2 public.geometry) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'geomunion';


ALTER FUNCTION public.st_union(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_voronoilines(public.geometry, double precision, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_voronoilines(g1 public.geometry, tolerance double precision DEFAULT 0.0, extend_to public.geometry DEFAULT NULL::public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE COST 1 PARALLEL SAFE
    AS $$ SELECT _ST_Voronoi(g1, extend_to, tolerance, false) $$;


ALTER FUNCTION public.st_voronoilines(g1 public.geometry, tolerance double precision, extend_to public.geometry) OWNER TO postgres;

--
-- Name: st_voronoipolygons(public.geometry, double precision, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_voronoipolygons(g1 public.geometry, tolerance double precision DEFAULT 0.0, extend_to public.geometry DEFAULT NULL::public.geometry) RETURNS public.geometry
    LANGUAGE sql IMMUTABLE COST 1 PARALLEL SAFE
    AS $$ SELECT _ST_Voronoi(g1, extend_to, tolerance, true) $$;


ALTER FUNCTION public.st_voronoipolygons(g1 public.geometry, tolerance double precision, extend_to public.geometry) OWNER TO postgres;

--
-- Name: st_within(public.geometry, public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_within(geom1 public.geometry, geom2 public.geometry) RETURNS boolean
    LANGUAGE sql IMMUTABLE PARALLEL SAFE
    AS $_$SELECT $2 OPERATOR(~) $1 AND _ST_Contains($2,$1)$_$;


ALTER FUNCTION public.st_within(geom1 public.geometry, geom2 public.geometry) OWNER TO postgres;

--
-- Name: st_wkbtosql(bytea); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_wkbtosql(wkb bytea) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_from_WKB';


ALTER FUNCTION public.st_wkbtosql(wkb bytea) OWNER TO postgres;

--
-- Name: st_wkttosql(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_wkttosql(text) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_from_text';


ALTER FUNCTION public.st_wkttosql(text) OWNER TO postgres;

--
-- Name: st_wrapx(public.geometry, double precision, double precision); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_wrapx(geom public.geometry, wrap double precision, move double precision) RETURNS public.geometry
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'ST_WrapX';


ALTER FUNCTION public.st_wrapx(geom public.geometry, wrap double precision, move double precision) OWNER TO postgres;

--
-- Name: st_x(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_x(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_x_point';


ALTER FUNCTION public.st_x(public.geometry) OWNER TO postgres;

--
-- Name: st_xmax(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_xmax(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_xmax';


ALTER FUNCTION public.st_xmax(public.box3d) OWNER TO postgres;

--
-- Name: st_xmin(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_xmin(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_xmin';


ALTER FUNCTION public.st_xmin(public.box3d) OWNER TO postgres;

--
-- Name: st_y(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_y(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_y_point';


ALTER FUNCTION public.st_y(public.geometry) OWNER TO postgres;

--
-- Name: st_ymax(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_ymax(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_ymax';


ALTER FUNCTION public.st_ymax(public.box3d) OWNER TO postgres;

--
-- Name: st_ymin(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_ymin(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_ymin';


ALTER FUNCTION public.st_ymin(public.box3d) OWNER TO postgres;

--
-- Name: st_z(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_z(public.geometry) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_z_point';


ALTER FUNCTION public.st_z(public.geometry) OWNER TO postgres;

--
-- Name: st_zmax(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_zmax(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_zmax';


ALTER FUNCTION public.st_zmax(public.box3d) OWNER TO postgres;

--
-- Name: st_zmflag(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_zmflag(public.geometry) RETURNS smallint
    LANGUAGE c IMMUTABLE STRICT COST 5 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_zmflag';


ALTER FUNCTION public.st_zmflag(public.geometry) OWNER TO postgres;

--
-- Name: st_zmin(public.box3d); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.st_zmin(public.box3d) RETURNS double precision
    LANGUAGE c IMMUTABLE STRICT PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'BOX3D_zmin';


ALTER FUNCTION public.st_zmin(public.box3d) OWNER TO postgres;

--
-- Name: text(public.geometry); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.text(public.geometry) RETURNS text
    LANGUAGE c IMMUTABLE STRICT COST 25 PARALLEL SAFE
    AS '$libdir/postgis-2.4', 'LWGEOM_to_text';


ALTER FUNCTION public.text(public.geometry) OWNER TO postgres;

--
-- Name: unlockrows(text); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.unlockrows(text) RETURNS integer
    LANGUAGE plpgsql STRICT
    AS $_$
DECLARE
	ret int;
BEGIN

	IF NOT LongTransactionsEnabled() THEN
		RAISE EXCEPTION 'Long transaction support disabled, use EnableLongTransaction() to enable.';
	END IF;

	EXECUTE 'DELETE FROM authorization_table where authid = ' ||
		quote_literal($1);

	GET DIAGNOSTICS ret = ROW_COUNT;

	RETURN ret;
END;
$_$;


ALTER FUNCTION public.unlockrows(text) OWNER TO postgres;

--
-- Name: updategeometrysrid(character varying, character varying, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.updategeometrysrid(character varying, character varying, integer) RETURNS text
    LANGUAGE plpgsql STRICT
    AS $_$
DECLARE
	ret  text;
BEGIN
	SELECT UpdateGeometrySRID('','',$1,$2,$3) into ret;
	RETURN ret;
END;
$_$;


ALTER FUNCTION public.updategeometrysrid(character varying, character varying, integer) OWNER TO postgres;

--
-- Name: updategeometrysrid(character varying, character varying, character varying, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.updategeometrysrid(character varying, character varying, character varying, integer) RETURNS text
    LANGUAGE plpgsql STRICT
    AS $_$
DECLARE
	ret  text;
BEGIN
	SELECT UpdateGeometrySRID('',$1,$2,$3,$4) into ret;
	RETURN ret;
END;
$_$;


ALTER FUNCTION public.updategeometrysrid(character varying, character varying, character varying, integer) OWNER TO postgres;

--
-- Name: updategeometrysrid(character varying, character varying, character varying, character varying, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.updategeometrysrid(catalogn_name character varying, schema_name character varying, table_name character varying, column_name character varying, new_srid_in integer) RETURNS text
    LANGUAGE plpgsql STRICT
    AS $$
DECLARE
	myrec RECORD;
	okay boolean;
	cname varchar;
	real_schema name;
	unknown_srid integer;
	new_srid integer := new_srid_in;

BEGIN


	-- Find, check or fix schema_name
	IF ( schema_name != '' ) THEN
		okay = false;

		FOR myrec IN SELECT nspname FROM pg_namespace WHERE text(nspname) = schema_name LOOP
			okay := true;
		END LOOP;

		IF ( okay <> true ) THEN
			RAISE EXCEPTION 'Invalid schema name';
		ELSE
			real_schema = schema_name;
		END IF;
	ELSE
		SELECT INTO real_schema current_schema()::text;
	END IF;

	-- Ensure that column_name is in geometry_columns
	okay = false;
	FOR myrec IN SELECT type, coord_dimension FROM geometry_columns WHERE f_table_schema = text(real_schema) and f_table_name = table_name and f_geometry_column = column_name LOOP
		okay := true;
	END LOOP;
	IF (NOT okay) THEN
		RAISE EXCEPTION 'column not found in geometry_columns table';
		RETURN false;
	END IF;

	-- Ensure that new_srid is valid
	IF ( new_srid > 0 ) THEN
		IF ( SELECT count(*) = 0 from spatial_ref_sys where srid = new_srid ) THEN
			RAISE EXCEPTION 'invalid SRID: % not found in spatial_ref_sys', new_srid;
			RETURN false;
		END IF;
	ELSE
		unknown_srid := ST_SRID('POINT EMPTY'::geometry);
		IF ( new_srid != unknown_srid ) THEN
			new_srid := unknown_srid;
			RAISE NOTICE 'SRID value % converted to the officially unknown SRID value %', new_srid_in, new_srid;
		END IF;
	END IF;

	IF postgis_constraint_srid(real_schema, table_name, column_name) IS NOT NULL THEN
	-- srid was enforced with constraints before, keep it that way.
        -- Make up constraint name
        cname = 'enforce_srid_'  || column_name;

        -- Drop enforce_srid constraint
        EXECUTE 'ALTER TABLE ' || quote_ident(real_schema) ||
            '.' || quote_ident(table_name) ||
            ' DROP constraint ' || quote_ident(cname);

        -- Update geometries SRID
        EXECUTE 'UPDATE ' || quote_ident(real_schema) ||
            '.' || quote_ident(table_name) ||
            ' SET ' || quote_ident(column_name) ||
            ' = ST_SetSRID(' || quote_ident(column_name) ||
            ', ' || new_srid::text || ')';

        -- Reset enforce_srid constraint
        EXECUTE 'ALTER TABLE ' || quote_ident(real_schema) ||
            '.' || quote_ident(table_name) ||
            ' ADD constraint ' || quote_ident(cname) ||
            ' CHECK (st_srid(' || quote_ident(column_name) ||
            ') = ' || new_srid::text || ')';
    ELSE
        -- We will use typmod to enforce if no srid constraints
        -- We are using postgis_type_name to lookup the new name
        -- (in case Paul changes his mind and flips geometry_columns to return old upper case name)
        EXECUTE 'ALTER TABLE ' || quote_ident(real_schema) || '.' || quote_ident(table_name) ||
        ' ALTER COLUMN ' || quote_ident(column_name) || ' TYPE  geometry(' || postgis_type_name(myrec.type, myrec.coord_dimension, true) || ', ' || new_srid::text || ') USING ST_SetSRID(' || quote_ident(column_name) || ',' || new_srid::text || ');' ;
    END IF;

	RETURN real_schema || '.' || table_name || '.' || column_name ||' SRID changed to ' || new_srid::text;

END;
$$;


ALTER FUNCTION public.updategeometrysrid(catalogn_name character varying, schema_name character varying, table_name character varying, column_name character varying, new_srid_in integer) OWNER TO postgres;

--
-- Name: st_3dextent(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_3dextent(public.geometry) (
    SFUNC = public.st_combinebbox,
    STYPE = public.box3d,
    COMBINEFUNC = public.st_combinebbox,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_3dextent(public.geometry) OWNER TO postgres;

--
-- Name: st_accum(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_accum(public.geometry) (
    SFUNC = public.pgis_geometry_accum_transfn,
    STYPE = public.pgis_abs,
    FINALFUNC = public.pgis_geometry_accum_finalfn,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_accum(public.geometry) OWNER TO postgres;

--
-- Name: st_asgeobuf(anyelement); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_asgeobuf(anyelement) (
    SFUNC = public.pgis_asgeobuf_transfn,
    STYPE = internal,
    FINALFUNC = public.pgis_asgeobuf_finalfn,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_asgeobuf(anyelement) OWNER TO postgres;

--
-- Name: st_asgeobuf(anyelement, text); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_asgeobuf(anyelement, text) (
    SFUNC = public.pgis_asgeobuf_transfn,
    STYPE = internal,
    FINALFUNC = public.pgis_asgeobuf_finalfn,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_asgeobuf(anyelement, text) OWNER TO postgres;

--
-- Name: st_asmvt(anyelement); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_asmvt(anyelement) (
    SFUNC = public.pgis_asmvt_transfn,
    STYPE = internal,
    FINALFUNC = public.pgis_asmvt_finalfn,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_asmvt(anyelement) OWNER TO postgres;

--
-- Name: st_asmvt(anyelement, text); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_asmvt(anyelement, text) (
    SFUNC = public.pgis_asmvt_transfn,
    STYPE = internal,
    FINALFUNC = public.pgis_asmvt_finalfn,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_asmvt(anyelement, text) OWNER TO postgres;

--
-- Name: st_asmvt(anyelement, text, integer); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_asmvt(anyelement, text, integer) (
    SFUNC = public.pgis_asmvt_transfn,
    STYPE = internal,
    FINALFUNC = public.pgis_asmvt_finalfn,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_asmvt(anyelement, text, integer) OWNER TO postgres;

--
-- Name: st_asmvt(anyelement, text, integer, text); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_asmvt(anyelement, text, integer, text) (
    SFUNC = public.pgis_asmvt_transfn,
    STYPE = internal,
    FINALFUNC = public.pgis_asmvt_finalfn,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_asmvt(anyelement, text, integer, text) OWNER TO postgres;

--
-- Name: st_clusterintersecting(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_clusterintersecting(public.geometry) (
    SFUNC = public.pgis_geometry_accum_transfn,
    STYPE = public.pgis_abs,
    FINALFUNC = public.pgis_geometry_clusterintersecting_finalfn,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_clusterintersecting(public.geometry) OWNER TO postgres;

--
-- Name: st_clusterwithin(public.geometry, double precision); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_clusterwithin(public.geometry, double precision) (
    SFUNC = public.pgis_geometry_accum_transfn,
    STYPE = public.pgis_abs,
    FINALFUNC = public.pgis_geometry_clusterwithin_finalfn,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_clusterwithin(public.geometry, double precision) OWNER TO postgres;

--
-- Name: st_collect(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_collect(public.geometry) (
    SFUNC = public.pgis_geometry_accum_transfn,
    STYPE = public.pgis_abs,
    FINALFUNC = public.pgis_geometry_collect_finalfn,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_collect(public.geometry) OWNER TO postgres;

--
-- Name: st_extent(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_extent(public.geometry) (
    SFUNC = public.st_combinebbox,
    STYPE = public.box3d,
    FINALFUNC = public.box2d,
    COMBINEFUNC = public.st_combinebbox,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_extent(public.geometry) OWNER TO postgres;

--
-- Name: st_makeline(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_makeline(public.geometry) (
    SFUNC = public.pgis_geometry_accum_transfn,
    STYPE = public.pgis_abs,
    FINALFUNC = public.pgis_geometry_makeline_finalfn,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_makeline(public.geometry) OWNER TO postgres;

--
-- Name: st_memcollect(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_memcollect(public.geometry) (
    SFUNC = public.st_collect,
    STYPE = public.geometry,
    COMBINEFUNC = public.st_collect,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_memcollect(public.geometry) OWNER TO postgres;

--
-- Name: st_memunion(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_memunion(public.geometry) (
    SFUNC = public.st_union,
    STYPE = public.geometry,
    COMBINEFUNC = public.st_union,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_memunion(public.geometry) OWNER TO postgres;

--
-- Name: st_polygonize(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_polygonize(public.geometry) (
    SFUNC = public.pgis_geometry_accum_transfn,
    STYPE = public.pgis_abs,
    FINALFUNC = public.pgis_geometry_polygonize_finalfn,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_polygonize(public.geometry) OWNER TO postgres;

--
-- Name: st_union(public.geometry); Type: AGGREGATE; Schema: public; Owner: postgres
--

CREATE AGGREGATE public.st_union(public.geometry) (
    SFUNC = public.pgis_geometry_accum_transfn,
    STYPE = public.pgis_abs,
    FINALFUNC = public.pgis_geometry_union_finalfn,
    PARALLEL = safe
);


ALTER AGGREGATE public.st_union(public.geometry) OWNER TO postgres;

--
-- Name: &&; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&& (
    PROCEDURE = public.geometry_overlaps,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.&&),
    RESTRICT = public.gserialized_gist_sel_2d,
    JOIN = public.gserialized_gist_joinsel_2d
);


ALTER OPERATOR public.&& (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: &&; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&& (
    PROCEDURE = public.geography_overlaps,
    LEFTARG = public.geography,
    RIGHTARG = public.geography,
    COMMUTATOR = OPERATOR(public.&&),
    RESTRICT = public.gserialized_gist_sel_nd,
    JOIN = public.gserialized_gist_joinsel_nd
);


ALTER OPERATOR public.&& (public.geography, public.geography) OWNER TO postgres;

--
-- Name: &&; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&& (
    PROCEDURE = public.overlaps_geog,
    LEFTARG = public.geography,
    RIGHTARG = public.gidx,
    COMMUTATOR = OPERATOR(public.&&)
);


ALTER OPERATOR public.&& (public.geography, public.gidx) OWNER TO postgres;

--
-- Name: &&; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&& (
    PROCEDURE = public.overlaps_geog,
    LEFTARG = public.gidx,
    RIGHTARG = public.geography,
    COMMUTATOR = OPERATOR(public.&&)
);


ALTER OPERATOR public.&& (public.gidx, public.geography) OWNER TO postgres;

--
-- Name: &&; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&& (
    PROCEDURE = public.overlaps_geog,
    LEFTARG = public.gidx,
    RIGHTARG = public.gidx,
    COMMUTATOR = OPERATOR(public.&&)
);


ALTER OPERATOR public.&& (public.gidx, public.gidx) OWNER TO postgres;

--
-- Name: &&; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&& (
    PROCEDURE = public.overlaps_2d,
    LEFTARG = public.geometry,
    RIGHTARG = public.box2df,
    COMMUTATOR = OPERATOR(public.&&)
);


ALTER OPERATOR public.&& (public.geometry, public.box2df) OWNER TO postgres;

--
-- Name: &&; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&& (
    PROCEDURE = public.overlaps_2d,
    LEFTARG = public.box2df,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.&&)
);


ALTER OPERATOR public.&& (public.box2df, public.geometry) OWNER TO postgres;

--
-- Name: &&; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&& (
    PROCEDURE = public.overlaps_2d,
    LEFTARG = public.box2df,
    RIGHTARG = public.box2df,
    COMMUTATOR = OPERATOR(public.&&)
);


ALTER OPERATOR public.&& (public.box2df, public.box2df) OWNER TO postgres;

--
-- Name: &&&; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&&& (
    PROCEDURE = public.geometry_overlaps_nd,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.&&&),
    RESTRICT = public.gserialized_gist_sel_nd,
    JOIN = public.gserialized_gist_joinsel_nd
);


ALTER OPERATOR public.&&& (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: &&&; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&&& (
    PROCEDURE = public.overlaps_nd,
    LEFTARG = public.geometry,
    RIGHTARG = public.gidx,
    COMMUTATOR = OPERATOR(public.&&&)
);


ALTER OPERATOR public.&&& (public.geometry, public.gidx) OWNER TO postgres;

--
-- Name: &&&; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&&& (
    PROCEDURE = public.overlaps_nd,
    LEFTARG = public.gidx,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.&&&)
);


ALTER OPERATOR public.&&& (public.gidx, public.geometry) OWNER TO postgres;

--
-- Name: &&&; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&&& (
    PROCEDURE = public.overlaps_nd,
    LEFTARG = public.gidx,
    RIGHTARG = public.gidx,
    COMMUTATOR = OPERATOR(public.&&&)
);


ALTER OPERATOR public.&&& (public.gidx, public.gidx) OWNER TO postgres;

--
-- Name: &<; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&< (
    PROCEDURE = public.geometry_overleft,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.&>),
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.&< (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: &<|; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&<| (
    PROCEDURE = public.geometry_overbelow,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.|&>),
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.&<| (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: &>; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.&> (
    PROCEDURE = public.geometry_overright,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.&<),
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.&> (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: <; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.< (
    PROCEDURE = public.geometry_lt,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.>),
    NEGATOR = OPERATOR(public.>=),
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.< (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: <; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.< (
    PROCEDURE = public.geography_lt,
    LEFTARG = public.geography,
    RIGHTARG = public.geography,
    COMMUTATOR = OPERATOR(public.>),
    NEGATOR = OPERATOR(public.>=),
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.< (public.geography, public.geography) OWNER TO postgres;

--
-- Name: <#>; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.<#> (
    PROCEDURE = public.geometry_distance_box,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.<#>)
);


ALTER OPERATOR public.<#> (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: <->; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.<-> (
    PROCEDURE = public.geometry_distance_centroid,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.<->)
);


ALTER OPERATOR public.<-> (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: <->; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.<-> (
    PROCEDURE = public.geography_distance_knn,
    LEFTARG = public.geography,
    RIGHTARG = public.geography,
    COMMUTATOR = OPERATOR(public.<->)
);


ALTER OPERATOR public.<-> (public.geography, public.geography) OWNER TO postgres;

--
-- Name: <<; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.<< (
    PROCEDURE = public.geometry_left,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.>>),
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.<< (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: <<->>; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.<<->> (
    PROCEDURE = public.geometry_distance_centroid_nd,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.<<->>)
);


ALTER OPERATOR public.<<->> (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: <<|; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.<<| (
    PROCEDURE = public.geometry_below,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.|>>),
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.<<| (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: <=; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.<= (
    PROCEDURE = public.geometry_le,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.>=),
    NEGATOR = OPERATOR(public.>),
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.<= (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: <=; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.<= (
    PROCEDURE = public.geography_le,
    LEFTARG = public.geography,
    RIGHTARG = public.geography,
    COMMUTATOR = OPERATOR(public.>=),
    NEGATOR = OPERATOR(public.>),
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.<= (public.geography, public.geography) OWNER TO postgres;

--
-- Name: =; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.= (
    PROCEDURE = public.geometry_eq,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.=),
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.= (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: =; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.= (
    PROCEDURE = public.geography_eq,
    LEFTARG = public.geography,
    RIGHTARG = public.geography,
    COMMUTATOR = OPERATOR(public.=),
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.= (public.geography, public.geography) OWNER TO postgres;

--
-- Name: >; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.> (
    PROCEDURE = public.geometry_gt,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.<),
    NEGATOR = OPERATOR(public.<=),
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.> (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: >; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.> (
    PROCEDURE = public.geography_gt,
    LEFTARG = public.geography,
    RIGHTARG = public.geography,
    COMMUTATOR = OPERATOR(public.<),
    NEGATOR = OPERATOR(public.<=),
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.> (public.geography, public.geography) OWNER TO postgres;

--
-- Name: >=; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.>= (
    PROCEDURE = public.geometry_ge,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.<=),
    NEGATOR = OPERATOR(public.<),
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.>= (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: >=; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.>= (
    PROCEDURE = public.geography_ge,
    LEFTARG = public.geography,
    RIGHTARG = public.geography,
    COMMUTATOR = OPERATOR(public.<=),
    NEGATOR = OPERATOR(public.<),
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.>= (public.geography, public.geography) OWNER TO postgres;

--
-- Name: >>; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.>> (
    PROCEDURE = public.geometry_right,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.<<),
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.>> (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: @; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.@ (
    PROCEDURE = public.geometry_within,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.~),
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.@ (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: @; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.@ (
    PROCEDURE = public.is_contained_2d,
    LEFTARG = public.geometry,
    RIGHTARG = public.box2df,
    COMMUTATOR = OPERATOR(public.~)
);


ALTER OPERATOR public.@ (public.geometry, public.box2df) OWNER TO postgres;

--
-- Name: @; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.@ (
    PROCEDURE = public.is_contained_2d,
    LEFTARG = public.box2df,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.~)
);


ALTER OPERATOR public.@ (public.box2df, public.geometry) OWNER TO postgres;

--
-- Name: @; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.@ (
    PROCEDURE = public.is_contained_2d,
    LEFTARG = public.box2df,
    RIGHTARG = public.box2df,
    COMMUTATOR = OPERATOR(public.~)
);


ALTER OPERATOR public.@ (public.box2df, public.box2df) OWNER TO postgres;

--
-- Name: |&>; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.|&> (
    PROCEDURE = public.geometry_overabove,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.&<|),
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.|&> (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: |=|; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.|=| (
    PROCEDURE = public.geometry_distance_cpa,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.|=|)
);


ALTER OPERATOR public.|=| (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: |>>; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.|>> (
    PROCEDURE = public.geometry_above,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.<<|),
    RESTRICT = positionsel,
    JOIN = positionjoinsel
);


ALTER OPERATOR public.|>> (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: ~; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.~ (
    PROCEDURE = public.geometry_contains,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.@),
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.~ (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: ~; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.~ (
    PROCEDURE = public.contains_2d,
    LEFTARG = public.box2df,
    RIGHTARG = public.geometry,
    COMMUTATOR = OPERATOR(public.@)
);


ALTER OPERATOR public.~ (public.box2df, public.geometry) OWNER TO postgres;

--
-- Name: ~; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.~ (
    PROCEDURE = public.contains_2d,
    LEFTARG = public.geometry,
    RIGHTARG = public.box2df,
    COMMUTATOR = OPERATOR(public.@)
);


ALTER OPERATOR public.~ (public.geometry, public.box2df) OWNER TO postgres;

--
-- Name: ~; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.~ (
    PROCEDURE = public.contains_2d,
    LEFTARG = public.box2df,
    RIGHTARG = public.box2df,
    COMMUTATOR = OPERATOR(public.@)
);


ALTER OPERATOR public.~ (public.box2df, public.box2df) OWNER TO postgres;

--
-- Name: ~=; Type: OPERATOR; Schema: public; Owner: postgres
--

CREATE OPERATOR public.~= (
    PROCEDURE = public.geometry_same,
    LEFTARG = public.geometry,
    RIGHTARG = public.geometry,
    RESTRICT = contsel,
    JOIN = contjoinsel
);


ALTER OPERATOR public.~= (public.geometry, public.geometry) OWNER TO postgres;

--
-- Name: brin_geography_inclusion_ops; Type: OPERATOR FAMILY; Schema: public; Owner: postgres
--

CREATE OPERATOR FAMILY public.brin_geography_inclusion_ops USING brin;


ALTER OPERATOR FAMILY public.brin_geography_inclusion_ops USING brin OWNER TO postgres;

--
-- Name: brin_geography_inclusion_ops; Type: OPERATOR CLASS; Schema: public; Owner: postgres
--

CREATE OPERATOR CLASS public.brin_geography_inclusion_ops
    DEFAULT FOR TYPE public.geography USING brin FAMILY public.brin_geography_inclusion_ops AS
    STORAGE public.gidx ,
    OPERATOR 3 public.&&(public.geography,public.geography) ,
    OPERATOR 3 public.&&(public.geography,public.gidx) ,
    OPERATOR 3 public.&&(public.gidx,public.geography) ,
    OPERATOR 3 public.&&(public.gidx,public.gidx) ,
    FUNCTION 1 (public.geography, public.geography) brin_inclusion_opcinfo(internal) ,
    FUNCTION 2 (public.geography, public.geography) public.geog_brin_inclusion_add_value(internal,internal,internal,internal) ,
    FUNCTION 3 (public.geography, public.geography) brin_inclusion_consistent(internal,internal,internal) ,
    FUNCTION 4 (public.geography, public.geography) brin_inclusion_union(internal,internal,internal);


ALTER OPERATOR CLASS public.brin_geography_inclusion_ops USING brin OWNER TO postgres;

--
-- Name: brin_geometry_inclusion_ops_2d; Type: OPERATOR FAMILY; Schema: public; Owner: postgres
--

CREATE OPERATOR FAMILY public.brin_geometry_inclusion_ops_2d USING brin;


ALTER OPERATOR FAMILY public.brin_geometry_inclusion_ops_2d USING brin OWNER TO postgres;

--
-- Name: brin_geometry_inclusion_ops_2d; Type: OPERATOR CLASS; Schema: public; Owner: postgres
--

CREATE OPERATOR CLASS public.brin_geometry_inclusion_ops_2d
    DEFAULT FOR TYPE public.geometry USING brin FAMILY public.brin_geometry_inclusion_ops_2d AS
    STORAGE public.box2df ,
    OPERATOR 3 public.&&(public.box2df,public.box2df) ,
    OPERATOR 3 public.&&(public.box2df,public.geometry) ,
    OPERATOR 3 public.&&(public.geometry,public.box2df) ,
    OPERATOR 3 public.&&(public.geometry,public.geometry) ,
    OPERATOR 7 public.~(public.box2df,public.box2df) ,
    OPERATOR 7 public.~(public.box2df,public.geometry) ,
    OPERATOR 7 public.~(public.geometry,public.box2df) ,
    OPERATOR 7 public.~(public.geometry,public.geometry) ,
    OPERATOR 8 public.@(public.box2df,public.box2df) ,
    OPERATOR 8 public.@(public.box2df,public.geometry) ,
    OPERATOR 8 public.@(public.geometry,public.box2df) ,
    OPERATOR 8 public.@(public.geometry,public.geometry) ,
    FUNCTION 1 (public.geometry, public.geometry) brin_inclusion_opcinfo(internal) ,
    FUNCTION 2 (public.geometry, public.geometry) public.geom2d_brin_inclusion_add_value(internal,internal,internal,internal) ,
    FUNCTION 3 (public.geometry, public.geometry) brin_inclusion_consistent(internal,internal,internal) ,
    FUNCTION 4 (public.geometry, public.geometry) brin_inclusion_union(internal,internal,internal);


ALTER OPERATOR CLASS public.brin_geometry_inclusion_ops_2d USING brin OWNER TO postgres;

--
-- Name: brin_geometry_inclusion_ops_3d; Type: OPERATOR FAMILY; Schema: public; Owner: postgres
--

CREATE OPERATOR FAMILY public.brin_geometry_inclusion_ops_3d USING brin;


ALTER OPERATOR FAMILY public.brin_geometry_inclusion_ops_3d USING brin OWNER TO postgres;

--
-- Name: brin_geometry_inclusion_ops_3d; Type: OPERATOR CLASS; Schema: public; Owner: postgres
--

CREATE OPERATOR CLASS public.brin_geometry_inclusion_ops_3d
    FOR TYPE public.geometry USING brin FAMILY public.brin_geometry_inclusion_ops_3d AS
    STORAGE public.gidx ,
    OPERATOR 3 public.&&&(public.geometry,public.geometry) ,
    OPERATOR 3 public.&&&(public.geometry,public.gidx) ,
    OPERATOR 3 public.&&&(public.gidx,public.geometry) ,
    OPERATOR 3 public.&&&(public.gidx,public.gidx) ,
    FUNCTION 1 (public.geometry, public.geometry) brin_inclusion_opcinfo(internal) ,
    FUNCTION 2 (public.geometry, public.geometry) public.geom3d_brin_inclusion_add_value(internal,internal,internal,internal) ,
    FUNCTION 3 (public.geometry, public.geometry) brin_inclusion_consistent(internal,internal,internal) ,
    FUNCTION 4 (public.geometry, public.geometry) brin_inclusion_union(internal,internal,internal);


ALTER OPERATOR CLASS public.brin_geometry_inclusion_ops_3d USING brin OWNER TO postgres;

--
-- Name: brin_geometry_inclusion_ops_4d; Type: OPERATOR FAMILY; Schema: public; Owner: postgres
--

CREATE OPERATOR FAMILY public.brin_geometry_inclusion_ops_4d USING brin;


ALTER OPERATOR FAMILY public.brin_geometry_inclusion_ops_4d USING brin OWNER TO postgres;

--
-- Name: brin_geometry_inclusion_ops_4d; Type: OPERATOR CLASS; Schema: public; Owner: postgres
--

CREATE OPERATOR CLASS public.brin_geometry_inclusion_ops_4d
    FOR TYPE public.geometry USING brin FAMILY public.brin_geometry_inclusion_ops_4d AS
    STORAGE public.gidx ,
    OPERATOR 3 public.&&&(public.geometry,public.geometry) ,
    OPERATOR 3 public.&&&(public.geometry,public.gidx) ,
    OPERATOR 3 public.&&&(public.gidx,public.geometry) ,
    OPERATOR 3 public.&&&(public.gidx,public.gidx) ,
    FUNCTION 1 (public.geometry, public.geometry) brin_inclusion_opcinfo(internal) ,
    FUNCTION 2 (public.geometry, public.geometry) public.geom4d_brin_inclusion_add_value(internal,internal,internal,internal) ,
    FUNCTION 3 (public.geometry, public.geometry) brin_inclusion_consistent(internal,internal,internal) ,
    FUNCTION 4 (public.geometry, public.geometry) brin_inclusion_union(internal,internal,internal);


ALTER OPERATOR CLASS public.brin_geometry_inclusion_ops_4d USING brin OWNER TO postgres;

--
-- Name: btree_geography_ops; Type: OPERATOR FAMILY; Schema: public; Owner: postgres
--

CREATE OPERATOR FAMILY public.btree_geography_ops USING btree;


ALTER OPERATOR FAMILY public.btree_geography_ops USING btree OWNER TO postgres;

--
-- Name: btree_geography_ops; Type: OPERATOR CLASS; Schema: public; Owner: postgres
--

CREATE OPERATOR CLASS public.btree_geography_ops
    DEFAULT FOR TYPE public.geography USING btree FAMILY public.btree_geography_ops AS
    OPERATOR 1 public.<(public.geography,public.geography) ,
    OPERATOR 2 public.<=(public.geography,public.geography) ,
    OPERATOR 3 public.=(public.geography,public.geography) ,
    OPERATOR 4 public.>=(public.geography,public.geography) ,
    OPERATOR 5 public.>(public.geography,public.geography) ,
    FUNCTION 1 (public.geography, public.geography) public.geography_cmp(public.geography,public.geography);


ALTER OPERATOR CLASS public.btree_geography_ops USING btree OWNER TO postgres;

--
-- Name: btree_geometry_ops; Type: OPERATOR FAMILY; Schema: public; Owner: postgres
--

CREATE OPERATOR FAMILY public.btree_geometry_ops USING btree;


ALTER OPERATOR FAMILY public.btree_geometry_ops USING btree OWNER TO postgres;

--
-- Name: btree_geometry_ops; Type: OPERATOR CLASS; Schema: public; Owner: postgres
--

CREATE OPERATOR CLASS public.btree_geometry_ops
    DEFAULT FOR TYPE public.geometry USING btree FAMILY public.btree_geometry_ops AS
    OPERATOR 1 public.<(public.geometry,public.geometry) ,
    OPERATOR 2 public.<=(public.geometry,public.geometry) ,
    OPERATOR 3 public.=(public.geometry,public.geometry) ,
    OPERATOR 4 public.>=(public.geometry,public.geometry) ,
    OPERATOR 5 public.>(public.geometry,public.geometry) ,
    FUNCTION 1 (public.geometry, public.geometry) public.geometry_cmp(public.geometry,public.geometry);


ALTER OPERATOR CLASS public.btree_geometry_ops USING btree OWNER TO postgres;

--
-- Name: gist_geography_ops; Type: OPERATOR FAMILY; Schema: public; Owner: postgres
--

CREATE OPERATOR FAMILY public.gist_geography_ops USING gist;


ALTER OPERATOR FAMILY public.gist_geography_ops USING gist OWNER TO postgres;

--
-- Name: gist_geography_ops; Type: OPERATOR CLASS; Schema: public; Owner: postgres
--

CREATE OPERATOR CLASS public.gist_geography_ops
    DEFAULT FOR TYPE public.geography USING gist FAMILY public.gist_geography_ops AS
    STORAGE public.gidx ,
    OPERATOR 3 public.&&(public.geography,public.geography) ,
    OPERATOR 13 public.<->(public.geography,public.geography) FOR ORDER BY pg_catalog.float_ops ,
    FUNCTION 1 (public.geography, public.geography) public.geography_gist_consistent(internal,public.geography,integer) ,
    FUNCTION 2 (public.geography, public.geography) public.geography_gist_union(bytea,internal) ,
    FUNCTION 3 (public.geography, public.geography) public.geography_gist_compress(internal) ,
    FUNCTION 4 (public.geography, public.geography) public.geography_gist_decompress(internal) ,
    FUNCTION 5 (public.geography, public.geography) public.geography_gist_penalty(internal,internal,internal) ,
    FUNCTION 6 (public.geography, public.geography) public.geography_gist_picksplit(internal,internal) ,
    FUNCTION 7 (public.geography, public.geography) public.geography_gist_same(public.box2d,public.box2d,internal) ,
    FUNCTION 8 (public.geography, public.geography) public.geography_gist_distance(internal,public.geography,integer);


ALTER OPERATOR CLASS public.gist_geography_ops USING gist OWNER TO postgres;

--
-- Name: gist_geometry_ops_2d; Type: OPERATOR FAMILY; Schema: public; Owner: postgres
--

CREATE OPERATOR FAMILY public.gist_geometry_ops_2d USING gist;


ALTER OPERATOR FAMILY public.gist_geometry_ops_2d USING gist OWNER TO postgres;

--
-- Name: gist_geometry_ops_2d; Type: OPERATOR CLASS; Schema: public; Owner: postgres
--

CREATE OPERATOR CLASS public.gist_geometry_ops_2d
    DEFAULT FOR TYPE public.geometry USING gist FAMILY public.gist_geometry_ops_2d AS
    STORAGE public.box2df ,
    OPERATOR 1 public.<<(public.geometry,public.geometry) ,
    OPERATOR 2 public.&<(public.geometry,public.geometry) ,
    OPERATOR 3 public.&&(public.geometry,public.geometry) ,
    OPERATOR 4 public.&>(public.geometry,public.geometry) ,
    OPERATOR 5 public.>>(public.geometry,public.geometry) ,
    OPERATOR 6 public.~=(public.geometry,public.geometry) ,
    OPERATOR 7 public.~(public.geometry,public.geometry) ,
    OPERATOR 8 public.@(public.geometry,public.geometry) ,
    OPERATOR 9 public.&<|(public.geometry,public.geometry) ,
    OPERATOR 10 public.<<|(public.geometry,public.geometry) ,
    OPERATOR 11 public.|>>(public.geometry,public.geometry) ,
    OPERATOR 12 public.|&>(public.geometry,public.geometry) ,
    OPERATOR 13 public.<->(public.geometry,public.geometry) FOR ORDER BY pg_catalog.float_ops ,
    OPERATOR 14 public.<#>(public.geometry,public.geometry) FOR ORDER BY pg_catalog.float_ops ,
    FUNCTION 1 (public.geometry, public.geometry) public.geometry_gist_consistent_2d(internal,public.geometry,integer) ,
    FUNCTION 2 (public.geometry, public.geometry) public.geometry_gist_union_2d(bytea,internal) ,
    FUNCTION 3 (public.geometry, public.geometry) public.geometry_gist_compress_2d(internal) ,
    FUNCTION 4 (public.geometry, public.geometry) public.geometry_gist_decompress_2d(internal) ,
    FUNCTION 5 (public.geometry, public.geometry) public.geometry_gist_penalty_2d(internal,internal,internal) ,
    FUNCTION 6 (public.geometry, public.geometry) public.geometry_gist_picksplit_2d(internal,internal) ,
    FUNCTION 7 (public.geometry, public.geometry) public.geometry_gist_same_2d(public.geometry,public.geometry,internal) ,
    FUNCTION 8 (public.geometry, public.geometry) public.geometry_gist_distance_2d(internal,public.geometry,integer);


ALTER OPERATOR CLASS public.gist_geometry_ops_2d USING gist OWNER TO postgres;

--
-- Name: gist_geometry_ops_nd; Type: OPERATOR FAMILY; Schema: public; Owner: postgres
--

CREATE OPERATOR FAMILY public.gist_geometry_ops_nd USING gist;


ALTER OPERATOR FAMILY public.gist_geometry_ops_nd USING gist OWNER TO postgres;

--
-- Name: gist_geometry_ops_nd; Type: OPERATOR CLASS; Schema: public; Owner: postgres
--

CREATE OPERATOR CLASS public.gist_geometry_ops_nd
    FOR TYPE public.geometry USING gist FAMILY public.gist_geometry_ops_nd AS
    STORAGE public.gidx ,
    OPERATOR 3 public.&&&(public.geometry,public.geometry) ,
    OPERATOR 13 public.<<->>(public.geometry,public.geometry) FOR ORDER BY pg_catalog.float_ops ,
    OPERATOR 20 public.|=|(public.geometry,public.geometry) FOR ORDER BY pg_catalog.float_ops ,
    FUNCTION 1 (public.geometry, public.geometry) public.geometry_gist_consistent_nd(internal,public.geometry,integer) ,
    FUNCTION 2 (public.geometry, public.geometry) public.geometry_gist_union_nd(bytea,internal) ,
    FUNCTION 3 (public.geometry, public.geometry) public.geometry_gist_compress_nd(internal) ,
    FUNCTION 4 (public.geometry, public.geometry) public.geometry_gist_decompress_nd(internal) ,
    FUNCTION 5 (public.geometry, public.geometry) public.geometry_gist_penalty_nd(internal,internal,internal) ,
    FUNCTION 6 (public.geometry, public.geometry) public.geometry_gist_picksplit_nd(internal,internal) ,
    FUNCTION 7 (public.geometry, public.geometry) public.geometry_gist_same_nd(public.geometry,public.geometry,internal) ,
    FUNCTION 8 (public.geometry, public.geometry) public.geometry_gist_distance_nd(internal,public.geometry,integer);


ALTER OPERATOR CLASS public.gist_geometry_ops_nd USING gist OWNER TO postgres;

--
-- Name: CAST (public.box2d AS public.box3d); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.box2d AS public.box3d) WITH FUNCTION public.box3d(public.box2d) AS IMPLICIT;


--
-- Name: CAST (public.box2d AS public.geometry); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.box2d AS public.geometry) WITH FUNCTION public.geometry(public.box2d) AS IMPLICIT;


--
-- Name: CAST (public.box3d AS box); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.box3d AS box) WITH FUNCTION public.box(public.box3d) AS IMPLICIT;


--
-- Name: CAST (public.box3d AS public.box2d); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.box3d AS public.box2d) WITH FUNCTION public.box2d(public.box3d) AS IMPLICIT;


--
-- Name: CAST (public.box3d AS public.geometry); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.box3d AS public.geometry) WITH FUNCTION public.geometry(public.box3d) AS IMPLICIT;


--
-- Name: CAST (bytea AS public.geography); Type: CAST; Schema: -; Owner:
--

CREATE CAST (bytea AS public.geography) WITH FUNCTION public.geography(bytea) AS IMPLICIT;


--
-- Name: CAST (bytea AS public.geometry); Type: CAST; Schema: -; Owner:
--

CREATE CAST (bytea AS public.geometry) WITH FUNCTION public.geometry(bytea) AS IMPLICIT;


--
-- Name: CAST (public.geography AS bytea); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.geography AS bytea) WITH FUNCTION public.bytea(public.geography) AS IMPLICIT;


--
-- Name: CAST (public.geography AS public.geography); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.geography AS public.geography) WITH FUNCTION public.geography(public.geography, integer, boolean) AS IMPLICIT;


--
-- Name: CAST (public.geography AS public.geometry); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.geography AS public.geometry) WITH FUNCTION public.geometry(public.geography);


--
-- Name: CAST (public.geometry AS box); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.geometry AS box) WITH FUNCTION public.box(public.geometry) AS ASSIGNMENT;


--
-- Name: CAST (public.geometry AS public.box2d); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.geometry AS public.box2d) WITH FUNCTION public.box2d(public.geometry) AS IMPLICIT;


--
-- Name: CAST (public.geometry AS public.box3d); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.geometry AS public.box3d) WITH FUNCTION public.box3d(public.geometry) AS IMPLICIT;


--
-- Name: CAST (public.geometry AS bytea); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.geometry AS bytea) WITH FUNCTION public.bytea(public.geometry) AS IMPLICIT;


--
-- Name: CAST (public.geometry AS public.geography); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.geometry AS public.geography) WITH FUNCTION public.geography(public.geometry) AS IMPLICIT;


--
-- Name: CAST (public.geometry AS public.geometry); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.geometry AS public.geometry) WITH FUNCTION public.geometry(public.geometry, integer, boolean) AS IMPLICIT;


--
-- Name: CAST (public.geometry AS path); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.geometry AS path) WITH FUNCTION public.path(public.geometry);


--
-- Name: CAST (public.geometry AS point); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.geometry AS point) WITH FUNCTION public.point(public.geometry);


--
-- Name: CAST (public.geometry AS polygon); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.geometry AS polygon) WITH FUNCTION public.polygon(public.geometry);


--
-- Name: CAST (public.geometry AS text); Type: CAST; Schema: -; Owner:
--

CREATE CAST (public.geometry AS text) WITH FUNCTION public.text(public.geometry) AS IMPLICIT;


--
-- Name: CAST (path AS public.geometry); Type: CAST; Schema: -; Owner:
--

CREATE CAST (path AS public.geometry) WITH FUNCTION public.geometry(path);


--
-- Name: CAST (point AS public.geometry); Type: CAST; Schema: -; Owner:
--

CREATE CAST (point AS public.geometry) WITH FUNCTION public.geometry(point);


--
-- Name: CAST (polygon AS public.geometry); Type: CAST; Schema: -; Owner:
--

CREATE CAST (polygon AS public.geometry) WITH FUNCTION public.geometry(polygon);


--
-- Name: CAST (text AS public.geometry); Type: CAST; Schema: -; Owner:
--

CREATE CAST (text AS public.geometry) WITH FUNCTION public.geometry(text) AS IMPLICIT;


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: karta_batplatser; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_batplatser (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    beskr1 character varying(254),
    beskr2 character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_batplatser OWNER TO geovis;

--
-- Name: batplatser_aneby_id_seq; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.batplatser_aneby_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.batplatser_aneby_id_seq OWNER TO geovis;

--
-- Name: batplatser_aneby_id_seq; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.batplatser_aneby_id_seq OWNED BY anebyext.karta_batplatser.id;


--
-- Name: karta_apotek; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_apotek (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_apotek OWNER TO geovis;

--
-- Name: karta_atervinningscentral; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_atervinningscentral (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_atervinningscentral OWNER TO geovis;

--
-- Name: karta_atervinningsstation; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_atervinningsstation (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_atervinningsstation OWNER TO geovis;

--
-- Name: karta_badplats; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_badplats (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_badplats OWNER TO geovis;

--
-- Name: karta_bankomat; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_bankomat (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_bankomat OWNER TO geovis;

--
-- Name: karta_basketplan; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_basketplan (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_basketplan OWNER TO geovis;

--
-- Name: karta_beachvolley; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_beachvolley (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_beachvolley OWNER TO geovis;

--
-- Name: karta_begravningsplats; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_begravningsplats (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_begravningsplats OWNER TO geovis;

--
-- Name: karta_belaggningsarbete; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_belaggningsarbete (
    id integer NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_belaggningsarbete OWNER TO geovis;

--
-- Name: karta_bibliotek; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_bibliotek (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_bibliotek OWNER TO geovis;

--
-- Name: karta_biograf; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_biograf (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_biograf OWNER TO geovis;

--
-- Name: karta_boende; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_boende (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_boende OWNER TO geovis;

--
-- Name: karta_bostadstomter_ejbebyggda; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_bostadstomter_ejbebyggda (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_bostadstomter_ejbebyggda OWNER TO geovis;

--
-- Name: karta_boulebanor; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_boulebanor (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_boulebanor OWNER TO geovis;

--
-- Name: karta_bowling; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_bowling (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_bowling OWNER TO geovis;

--
-- Name: karta_busshallplats; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_busshallplats (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_busshallplats OWNER TO geovis;

--
-- Name: karta_busskort; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_busskort (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_busskort OWNER TO geovis;

--
-- Name: karta_busstation; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_busstation (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_busstation OWNER TO geovis;

--
-- Name: karta_bygdegardar; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_bygdegardar (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_bygdegardar OWNER TO geovis;

--
-- Name: karta_cafe; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_cafe (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_cafe OWNER TO geovis;

--
-- Name: karta_cykelleder; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_cykelleder (
    id integer NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_cykelleder OWNER TO geovis;

--
-- Name: karta_cykelvagar; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_cykelvagar (
    id integer NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_cykelvagar OWNER TO geovis;

--
-- Name: karta_daglig-verksamhet; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_daglig-verksamhet" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_daglig-verksamhet" OWNER TO geovis;

--
-- Name: karta_detaljplaner-gallande; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_detaljplaner-gallande" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(254),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_detaljplaner-gallande" OWNER TO geovis;

--
-- Name: karta_detaljplaner-pagaende; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_detaljplaner-pagaende" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(254),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_detaljplaner-pagaende" OWNER TO geovis;

--
-- Name: karta_drivmedelsstation; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_drivmedelsstation (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_drivmedelsstation OWNER TO geovis;

--
-- Name: karta_fiske; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_fiske (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_fiske OWNER TO geovis;

--
-- Name: karta_fiskekort; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_fiskekort (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_fiskekort OWNER TO geovis;

--
-- Name: karta_folkets-park; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_folkets-park" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_folkets-park" OWNER TO geovis;

--
-- Name: karta_folktandvard; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_folktandvard (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_folktandvard OWNER TO geovis;

--
-- Name: karta_forskola; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_forskola (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_forskola OWNER TO geovis;

--
-- Name: karta_fotbollsplaner; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_fotbollsplaner (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_fotbollsplaner OWNER TO geovis;

--
-- Name: karta_friidrottsanl; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_friidrottsanl (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_friidrottsanl OWNER TO geovis;

--
-- Name: karta_fritidsgard; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_fritidsgard (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_fritidsgard OWNER TO geovis;

--
-- Name: karta_fritidshem; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_fritidshem (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_fritidshem OWNER TO geovis;

--
-- Name: karta_golfbanor; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_golfbanor (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_golfbanor OWNER TO geovis;

--
-- Name: karta_grav-uppstallningstillstand; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_grav-uppstallningstillstand" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_grav-uppstallningstillstand" OWNER TO geovis;

--
-- Name: karta_gym; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_gym (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_gym OWNER TO geovis;

--
-- Name: karta_hamn; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_hamn (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_hamn OWNER TO geovis;

--
-- Name: karta_hembygdsforening; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_hembygdsforening (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_hembygdsforening OWNER TO geovis;

--
-- Name: karta_hjartstartare; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_hjartstartare (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_hjartstartare OWNER TO geovis;

--
-- Name: karta_hundklubb; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_hundklubb (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_hundklubb OWNER TO geovis;

--
-- Name: karta_hundpasar; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_hundpasar (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_hundpasar OWNER TO geovis;

--
-- Name: karta_idrottshall; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_idrottshall (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_idrottshall OWNER TO geovis;

--
-- Name: karta_isbanor; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_isbanor (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_isbanor OWNER TO geovis;

--
-- Name: karta_kommunagd-mark; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_kommunagd-mark" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_kommunagd-mark" OWNER TO geovis;

--
-- Name: karta_kommunkontor; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_kommunkontor (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_kommunkontor OWNER TO geovis;

--
-- Name: karta_konsumentradgivning; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_konsumentradgivning (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_konsumentradgivning OWNER TO geovis;

--
-- Name: karta_kulturforening; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_kulturforening (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_kulturforening OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq78; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq78
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq78 OWNER TO geovis;

--
-- Name: karta_kulturskolan; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_kulturskolan (
    id integer DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq78'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_kulturskolan OWNER TO geovis;

--
-- Name: karta_laddstationer; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_laddstationer (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_laddstationer OWNER TO geovis;

--
-- Name: karta_langdskidspar; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_langdskidspar (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_langdskidspar OWNER TO geovis;

--
-- Name: karta_ledig-industrimark; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_ledig-industrimark" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_ledig-industrimark" OWNER TO geovis;

--
-- Name: karta_ledig-industrimark_privatagd; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_ledig-industrimark_privatagd" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_ledig-industrimark_privatagd" OWNER TO geovis;

--
-- Name: karta_lediga-bostadstomter; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_lediga-bostadstomter" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_lediga-bostadstomter" OWNER TO geovis;

--
-- Name: karta_lekplats; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_lekplats (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_lekplats OWNER TO geovis;

--
-- Name: karta_livsmedelsbutik; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_livsmedelsbutik (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_livsmedelsbutik OWNER TO geovis;

--
-- Name: karta_motionsanl; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_motionsanl (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_motionsanl OWNER TO geovis;

--
-- Name: karta_motionsspar_elljusspar; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_motionsspar_elljusspar (
    id integer NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_motionsspar_elljusspar OWNER TO geovis;

--
-- Name: karta_motionsspar_uppmarkta; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_motionsspar_uppmarkta (
    id integer NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_motionsspar_uppmarkta OWNER TO geovis;

--
-- Name: karta_motorcrossbana; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_motorcrossbana (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_motorcrossbana OWNER TO geovis;

--
-- Name: karta_naturreservat; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_naturreservat (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_naturreservat OWNER TO geovis;

--
-- Name: karta_naturvardsprogram-objekt; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_naturvardsprogram-objekt" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_naturvardsprogram-objekt" OWNER TO geovis;

--
-- Name: karta_naturvardsprogram-omraden; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_naturvardsprogram-omraden" (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_naturvardsprogram-omraden" OWNER TO geovis;

--
-- Name: karta_obo; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_obo (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_obo OWNER TO geovis;

--
-- Name: karta_offentligatoaletter; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_offentligatoaletter (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_offentligatoaletter OWNER TO geovis;

--
-- Name: karta_okad-sysselsattning; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_okad-sysselsattning" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_okad-sysselsattning" OWNER TO geovis;

--
-- Name: karta_parkering-pendlar; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_parkering-pendlar" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_parkering-pendlar" OWNER TO geovis;

--
-- Name: karta_parkering_rorelsehindrade; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_parkering_rorelsehindrade (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_parkering_rorelsehindrade OWNER TO geovis;

--
-- Name: karta_parkeringsplatser; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_parkeringsplatser (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_parkeringsplatser OWNER TO geovis;

--
-- Name: karta_polis; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_polis (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_polis OWNER TO geovis;

--
-- Name: karta_postombud; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_postombud (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_postombud OWNER TO geovis;

--
-- Name: karta_racketanl; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_racketanl (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_racketanl OWNER TO geovis;

--
-- Name: karta_raddningstjanst; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_raddningstjanst (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_raddningstjanst OWNER TO geovis;

--
-- Name: karta_restauranger; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_restauranger (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_restauranger OWNER TO geovis;

--
-- Name: karta_ridanl; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_ridanl (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_ridanl OWNER TO geovis;

--
-- Name: karta_samlingslokal; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_samlingslokal (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_samlingslokal OWNER TO geovis;

--
-- Name: karta_sevardheter_besoksmal; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_sevardheter_besoksmal (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_sevardheter_besoksmal OWNER TO geovis;

--
-- Name: karta_sevardheter_kultur; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_sevardheter_kultur (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_sevardheter_kultur OWNER TO geovis;

--
-- Name: karta_sevardheter_natur; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_sevardheter_natur (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_sevardheter_natur OWNER TO geovis;

--
-- Name: karta_skatepark; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_skatepark (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_skatepark OWNER TO geovis;

--
-- Name: karta_skjutbana; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_skjutbana (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_skjutbana OWNER TO geovis;

--
-- Name: karta_skola; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_skola (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_skola OWNER TO geovis;

--
-- Name: karta_socialkontoret; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_socialkontoret (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_socialkontoret OWNER TO geovis;

--
-- Name: karta_sophamtning; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_sophamtning (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_sophamtning OWNER TO geovis;

--
-- Name: karta_spontanidrottsplats; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_spontanidrottsplats (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_spontanidrottsplats OWNER TO geovis;

--
-- Name: karta_systembolaget; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_systembolaget (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_systembolaget OWNER TO geovis;

--
-- Name: karta_torghandel; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_torghandel (
    id integer NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_torghandel OWNER TO geovis;

--
-- Name: karta_traffpunkt-aktivitetshuset; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_traffpunkt-aktivitetshuset" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_traffpunkt-aktivitetshuset" OWNER TO geovis;

--
-- Name: karta_traffpunkt-aldre; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_traffpunkt-aldre" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_traffpunkt-aldre" OWNER TO geovis;

--
-- Name: karta_traffpunkt-funktionsnedsatta; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_traffpunkt-funktionsnedsatta" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_traffpunkt-funktionsnedsatta" OWNER TO geovis;

--
-- Name: karta_turistinformation; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_turistinformation (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_turistinformation OWNER TO geovis;

--
-- Name: karta_uppstallningsplats-husbilar; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_uppstallningsplats-husbilar" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_uppstallningsplats-husbilar" OWNER TO geovis;

--
-- Name: karta_utomhusgym; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_utomhusgym (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_utomhusgym OWNER TO geovis;

--
-- Name: karta_vandringsleder; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_vandringsleder (
    id integer NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_vandringsleder OWNER TO geovis;

--
-- Name: karta_vardcentral; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext.karta_vardcentral (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext.karta_vardcentral OWNER TO geovis;

--
-- Name: karta_viktigt-meddelande; Type: TABLE; Schema: anebyext; Owner: geovis
--

CREATE TABLE anebyext."karta_viktigt-meddelande" (
    id integer NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE anebyext."karta_viktigt-meddelande" OWNER TO geovis;

--
-- Name: standard_kommunkarta_linje_id_seq; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_linje_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_linje_id_seq OWNER TO geovis;

--
-- Name: standard_kommunkarta_linje_id_seq; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_linje_id_seq OWNED BY anebyext.karta_belaggningsarbete.id;


--
-- Name: standard_kommunkarta_linje_id_seq1; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_linje_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_linje_id_seq1 OWNER TO geovis;

--
-- Name: standard_kommunkarta_linje_id_seq1; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_linje_id_seq1 OWNED BY anebyext.karta_vandringsleder.id;


--
-- Name: standard_kommunkarta_linje_id_seq2; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_linje_id_seq2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_linje_id_seq2 OWNER TO geovis;

--
-- Name: standard_kommunkarta_linje_id_seq2; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_linje_id_seq2 OWNED BY anebyext.karta_cykelleder.id;


--
-- Name: standard_kommunkarta_linje_id_seq3; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_linje_id_seq3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_linje_id_seq3 OWNER TO geovis;

--
-- Name: standard_kommunkarta_linje_id_seq3; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_linje_id_seq3 OWNED BY anebyext.karta_cykelvagar.id;


--
-- Name: standard_kommunkarta_linje_id_seq4; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_linje_id_seq4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_linje_id_seq4 OWNER TO geovis;

--
-- Name: standard_kommunkarta_linje_id_seq4; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_linje_id_seq4 OWNED BY anebyext.karta_motionsspar_elljusspar.id;


--
-- Name: standard_kommunkarta_linje_id_seq5; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_linje_id_seq5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_linje_id_seq5 OWNER TO geovis;

--
-- Name: standard_kommunkarta_linje_id_seq5; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_linje_id_seq5 OWNED BY anebyext.karta_motionsspar_uppmarkta.id;


--
-- Name: standard_kommunkarta_punkt_id_seq; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq OWNED BY anebyext.karta_skola.id;


--
-- Name: standard_kommunkarta_punkt_id_seq1; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq1 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq1; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq1 OWNED BY anebyext.karta_forskola.id;


--
-- Name: standard_kommunkarta_punkt_id_seq10; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq10 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq10; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq10 OWNED BY anebyext."karta_okad-sysselsattning".id;


--
-- Name: standard_kommunkarta_punkt_id_seq11; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq11 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq11; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq11 OWNED BY anebyext."karta_traffpunkt-aldre".id;


--
-- Name: standard_kommunkarta_punkt_id_seq12; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq12 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq12; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq12 OWNED BY anebyext."karta_traffpunkt-aktivitetshuset".id;


--
-- Name: standard_kommunkarta_punkt_id_seq13; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq13
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq13 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq13; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq13 OWNED BY anebyext.karta_bibliotek.id;


--
-- Name: standard_kommunkarta_punkt_id_seq14; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq14 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq14; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq14 OWNED BY anebyext.karta_biograf.id;


--
-- Name: standard_kommunkarta_punkt_id_seq15; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq15 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq15; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq15 OWNED BY anebyext."karta_folkets-park".id;


--
-- Name: standard_kommunkarta_punkt_id_seq16; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq16 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq16; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq16 OWNED BY anebyext.karta_golfbanor.id;


--
-- Name: standard_kommunkarta_punkt_id_seq17; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq17 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq17; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq17 OWNED BY anebyext.karta_fotbollsplaner.id;


--
-- Name: standard_kommunkarta_punkt_id_seq18; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq18 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq18; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq18 OWNED BY anebyext.karta_friidrottsanl.id;


--
-- Name: standard_kommunkarta_punkt_id_seq19; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq19 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq19; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq19 OWNED BY anebyext.karta_idrottshall.id;


--
-- Name: standard_kommunkarta_punkt_id_seq2; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq2 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq2; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq2 OWNED BY anebyext.karta_isbanor.id;


--
-- Name: standard_kommunkarta_punkt_id_seq20; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq20 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq20; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq20 OWNED BY anebyext.karta_badplats.id;


--
-- Name: standard_kommunkarta_punkt_id_seq21; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq21
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq21 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq21; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq21 OWNED BY anebyext.karta_fritidsgard.id;


--
-- Name: standard_kommunkarta_punkt_id_seq22; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq22
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq22 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq22; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq22 OWNED BY anebyext.karta_motionsanl.id;


--
-- Name: standard_kommunkarta_punkt_id_seq23; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq23
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq23 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq23; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq23 OWNED BY anebyext.karta_racketanl.id;


--
-- Name: standard_kommunkarta_punkt_id_seq24; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq24
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq24 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq24; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq24 OWNED BY anebyext.karta_skatepark.id;


--
-- Name: standard_kommunkarta_punkt_id_seq25; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq25
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq25 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq25; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq25 OWNED BY anebyext.karta_motorcrossbana.id;


--
-- Name: standard_kommunkarta_punkt_id_seq26; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq26
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq26 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq26; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq26 OWNED BY anebyext.karta_ridanl.id;


--
-- Name: standard_kommunkarta_punkt_id_seq27; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq27
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq27 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq27; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq27 OWNED BY anebyext.karta_atervinningscentral.id;


--
-- Name: standard_kommunkarta_punkt_id_seq28; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq28
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq28 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq28; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq28 OWNED BY anebyext.karta_atervinningsstation.id;


--
-- Name: standard_kommunkarta_punkt_id_seq29; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq29
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq29 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq29; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq29 OWNED BY anebyext."karta_parkering-pendlar".id;


--
-- Name: standard_kommunkarta_punkt_id_seq3; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq3 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq3; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq3 OWNED BY anebyext.karta_polis.id;


--
-- Name: standard_kommunkarta_punkt_id_seq30; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq30
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq30 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq30; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq30 OWNED BY anebyext.karta_parkeringsplatser.id;


--
-- Name: standard_kommunkarta_punkt_id_seq31; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq31
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq31 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq31; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq31 OWNED BY anebyext.karta_busstation.id;


--
-- Name: standard_kommunkarta_punkt_id_seq32; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq32
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq32 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq32; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq32 OWNED BY anebyext."karta_uppstallningsplats-husbilar".id;


--
-- Name: standard_kommunkarta_punkt_id_seq33; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq33
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq33 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq33; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq33 OWNED BY anebyext.karta_apotek.id;


--
-- Name: standard_kommunkarta_punkt_id_seq34; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq34
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq34 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq34; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq34 OWNED BY anebyext.karta_laddstationer.id;


--
-- Name: standard_kommunkarta_punkt_id_seq35; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq35
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq35 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq35; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq35 OWNED BY anebyext.karta_lekplats.id;


--
-- Name: standard_kommunkarta_punkt_id_seq36; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq36
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq36 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq36; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq36 OWNED BY anebyext.karta_folktandvard.id;


--
-- Name: standard_kommunkarta_punkt_id_seq37; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq37
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq37 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq37; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq37 OWNED BY anebyext.karta_socialkontoret.id;


--
-- Name: standard_kommunkarta_punkt_id_seq38; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq38
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq38 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq38; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq38 OWNED BY anebyext.karta_obo.id;


--
-- Name: standard_kommunkarta_punkt_id_seq39; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq39
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq39 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq39; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq39 OWNED BY anebyext.karta_hamn.id;


--
-- Name: standard_kommunkarta_punkt_id_seq4; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq4 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq4; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq4 OWNED BY anebyext.karta_konsumentradgivning.id;


--
-- Name: standard_kommunkarta_punkt_id_seq40; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq40
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq40 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq40; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq40 OWNED BY anebyext.karta_fritidshem.id;


--
-- Name: standard_kommunkarta_punkt_id_seq41; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq41
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq41 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq41; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq41 OWNED BY anebyext.karta_bankomat.id;


--
-- Name: standard_kommunkarta_punkt_id_seq42; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq42
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq42 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq42; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq42 OWNED BY anebyext."karta_traffpunkt-funktionsnedsatta".id;


--
-- Name: standard_kommunkarta_punkt_id_seq43; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq43
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq43 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq43; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq43 OWNED BY anebyext.karta_hjartstartare.id;


--
-- Name: standard_kommunkarta_punkt_id_seq44; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq44
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq44 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq44; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq44 OWNED BY anebyext.karta_turistinformation.id;


--
-- Name: standard_kommunkarta_punkt_id_seq45; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq45
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq45 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq45; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq45 OWNED BY anebyext.karta_boende.id;


--
-- Name: standard_kommunkarta_punkt_id_seq46; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq46
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq46 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq46; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq46 OWNED BY anebyext.karta_sevardheter_natur.id;


--
-- Name: standard_kommunkarta_punkt_id_seq47; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq47
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq47 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq47; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq47 OWNED BY anebyext.karta_sevardheter_kultur.id;


--
-- Name: standard_kommunkarta_punkt_id_seq48; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq48
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq48 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq48; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq48 OWNED BY anebyext.karta_sevardheter_besoksmal.id;


--
-- Name: standard_kommunkarta_punkt_id_seq49; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq49
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq49 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq49; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq49 OWNED BY anebyext.karta_restauranger.id;


--
-- Name: standard_kommunkarta_punkt_id_seq5; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq5 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq5; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq5 OWNED BY anebyext.karta_raddningstjanst.id;


--
-- Name: standard_kommunkarta_punkt_id_seq50; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq50
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq50 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq50; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq50 OWNED BY anebyext.karta_cafe.id;


--
-- Name: standard_kommunkarta_punkt_id_seq51; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq51
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq51 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq51; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq51 OWNED BY anebyext.karta_livsmedelsbutik.id;


--
-- Name: standard_kommunkarta_punkt_id_seq52; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq52
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq52 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq52; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq52 OWNED BY anebyext.karta_drivmedelsstation.id;


--
-- Name: standard_kommunkarta_punkt_id_seq53; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq53
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq53 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq53; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq53 OWNED BY anebyext.karta_busshallplats.id;


--
-- Name: standard_kommunkarta_punkt_id_seq54; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq54
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq54 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq54; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq54 OWNED BY anebyext.karta_fiske.id;


--
-- Name: standard_kommunkarta_punkt_id_seq55; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq55
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq55 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq55; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq55 OWNED BY anebyext.karta_fiskekort.id;


--
-- Name: standard_kommunkarta_punkt_id_seq56; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq56
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq56 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq56; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq56 OWNED BY anebyext.karta_basketplan.id;


--
-- Name: standard_kommunkarta_punkt_id_seq57; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq57
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq57 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq57; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq57 OWNED BY anebyext.karta_beachvolley.id;


--
-- Name: standard_kommunkarta_punkt_id_seq58; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq58
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq58 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq58; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq58 OWNED BY anebyext.karta_begravningsplats.id;


--
-- Name: standard_kommunkarta_punkt_id_seq59; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq59
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq59 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq59; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq59 OWNED BY anebyext.karta_boulebanor.id;


--
-- Name: standard_kommunkarta_punkt_id_seq6; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq6 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq6; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq6 OWNED BY anebyext.karta_vardcentral.id;


--
-- Name: standard_kommunkarta_punkt_id_seq60; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq60
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq60 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq60; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq60 OWNED BY anebyext.karta_busskort.id;


--
-- Name: standard_kommunkarta_punkt_id_seq61; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq61
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq61 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq61; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq61 OWNED BY anebyext.karta_langdskidspar.id;


--
-- Name: standard_kommunkarta_punkt_id_seq62; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq62
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq62 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq62; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq62 OWNED BY anebyext.karta_parkering_rorelsehindrade.id;


--
-- Name: standard_kommunkarta_punkt_id_seq63; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq63
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq63 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq63; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq63 OWNED BY anebyext.karta_postombud.id;


--
-- Name: standard_kommunkarta_punkt_id_seq64; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq64
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq64 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq64; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq64 OWNED BY anebyext.karta_skjutbana.id;


--
-- Name: standard_kommunkarta_punkt_id_seq65; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq65
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq65 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq65; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq65 OWNED BY anebyext.karta_systembolaget.id;


--
-- Name: standard_kommunkarta_punkt_id_seq66; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq66
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq66 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq66; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq66 OWNED BY anebyext.karta_utomhusgym.id;


--
-- Name: standard_kommunkarta_punkt_id_seq67; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq67
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq67 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq67; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq67 OWNED BY anebyext.karta_bowling.id;


--
-- Name: standard_kommunkarta_punkt_id_seq68; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq68
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq68 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq68; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq68 OWNED BY anebyext.karta_offentligatoaletter.id;


--
-- Name: standard_kommunkarta_punkt_id_seq69; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq69
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq69 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq69; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq69 OWNED BY anebyext.karta_kulturforening.id;


--
-- Name: standard_kommunkarta_punkt_id_seq7; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq7 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq7; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq7 OWNED BY anebyext.karta_kommunkontor.id;


--
-- Name: standard_kommunkarta_punkt_id_seq70; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq70
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq70 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq70; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq70 OWNED BY anebyext.karta_bygdegardar.id;


--
-- Name: standard_kommunkarta_punkt_id_seq71; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq71
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq71 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq71; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq71 OWNED BY anebyext.karta_hembygdsforening.id;


--
-- Name: standard_kommunkarta_punkt_id_seq72; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq72
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq72 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq72; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq72 OWNED BY anebyext.karta_spontanidrottsplats.id;


--
-- Name: standard_kommunkarta_punkt_id_seq73; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq73
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq73 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq73; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq73 OWNED BY anebyext.karta_hundklubb.id;


--
-- Name: standard_kommunkarta_punkt_id_seq74; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq74
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq74 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq74; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq74 OWNED BY anebyext.karta_gym.id;


--
-- Name: standard_kommunkarta_punkt_id_seq75; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq75
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq75 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq75; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq75 OWNED BY anebyext."karta_lediga-bostadstomter".id;


--
-- Name: standard_kommunkarta_punkt_id_seq76; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq76
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq76 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq76; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq76 OWNED BY anebyext.karta_hundpasar.id;


--
-- Name: standard_kommunkarta_punkt_id_seq77; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq77
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq77 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq77; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq77 OWNED BY anebyext.karta_samlingslokal.id;


--
-- Name: standard_kommunkarta_punkt_id_seq8; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq8 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq8; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq8 OWNED BY anebyext."karta_viktigt-meddelande".id;


--
-- Name: standard_kommunkarta_punkt_id_seq9; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_punkt_id_seq9 OWNER TO geovis;

--
-- Name: standard_kommunkarta_punkt_id_seq9; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_punkt_id_seq9 OWNED BY anebyext."karta_daglig-verksamhet".id;


--
-- Name: standard_kommunkarta_yta_id_seq; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_yta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_yta_id_seq OWNER TO geovis;

--
-- Name: standard_kommunkarta_yta_id_seq; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_yta_id_seq OWNED BY anebyext."karta_detaljplaner-gallande".id;


--
-- Name: standard_kommunkarta_yta_id_seq1; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_yta_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_yta_id_seq1 OWNER TO geovis;

--
-- Name: standard_kommunkarta_yta_id_seq1; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_yta_id_seq1 OWNED BY anebyext."karta_detaljplaner-pagaende".id;


--
-- Name: standard_kommunkarta_yta_id_seq10; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_yta_id_seq10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_yta_id_seq10 OWNER TO geovis;

--
-- Name: standard_kommunkarta_yta_id_seq10; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_yta_id_seq10 OWNED BY anebyext."karta_naturvardsprogram-omraden".id;


--
-- Name: standard_kommunkarta_yta_id_seq11; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_yta_id_seq11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_yta_id_seq11 OWNER TO geovis;

--
-- Name: standard_kommunkarta_yta_id_seq11; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_yta_id_seq11 OWNED BY anebyext.karta_sophamtning.id;


--
-- Name: standard_kommunkarta_yta_id_seq12; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_yta_id_seq12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_yta_id_seq12 OWNER TO geovis;

--
-- Name: standard_kommunkarta_yta_id_seq12; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_yta_id_seq12 OWNED BY anebyext.karta_naturreservat.id;


--
-- Name: standard_kommunkarta_yta_id_seq2; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_yta_id_seq2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_yta_id_seq2 OWNER TO geovis;

--
-- Name: standard_kommunkarta_yta_id_seq2; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_yta_id_seq2 OWNED BY anebyext."karta_grav-uppstallningstillstand".id;


--
-- Name: standard_kommunkarta_yta_id_seq3; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_yta_id_seq3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_yta_id_seq3 OWNER TO geovis;

--
-- Name: standard_kommunkarta_yta_id_seq3; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_yta_id_seq3 OWNED BY anebyext."karta_naturvardsprogram-objekt".id;


--
-- Name: standard_kommunkarta_yta_id_seq4; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_yta_id_seq4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_yta_id_seq4 OWNER TO geovis;

--
-- Name: standard_kommunkarta_yta_id_seq4; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_yta_id_seq4 OWNED BY anebyext.karta_torghandel.id;


--
-- Name: standard_kommunkarta_yta_id_seq6; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_yta_id_seq6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_yta_id_seq6 OWNER TO geovis;

--
-- Name: standard_kommunkarta_yta_id_seq6; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_yta_id_seq6 OWNED BY anebyext."karta_ledig-industrimark".id;


--
-- Name: standard_kommunkarta_yta_id_seq7; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_yta_id_seq7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_yta_id_seq7 OWNER TO geovis;

--
-- Name: standard_kommunkarta_yta_id_seq7; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_yta_id_seq7 OWNED BY anebyext."karta_kommunagd-mark".id;


--
-- Name: standard_kommunkarta_yta_id_seq8; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_yta_id_seq8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_yta_id_seq8 OWNER TO geovis;

--
-- Name: standard_kommunkarta_yta_id_seq8; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_yta_id_seq8 OWNED BY anebyext.karta_bostadstomter_ejbebyggda.id;


--
-- Name: standard_kommunkarta_yta_id_seq9; Type: SEQUENCE; Schema: anebyext; Owner: geovis
--

CREATE SEQUENCE anebyext.standard_kommunkarta_yta_id_seq9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE anebyext.standard_kommunkarta_yta_id_seq9 OWNER TO geovis;

--
-- Name: standard_kommunkarta_yta_id_seq9; Type: SEQUENCE OWNED BY; Schema: anebyext; Owner: geovis
--

ALTER SEQUENCE anebyext.standard_kommunkarta_yta_id_seq9 OWNED BY anebyext."karta_ledig-industrimark_privatagd".id;


--
-- Name: geography_columns; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.geography_columns AS
 SELECT current_database() AS f_table_catalog,
    n.nspname AS f_table_schema,
    c.relname AS f_table_name,
    a.attname AS f_geography_column,
    public.postgis_typmod_dims(a.atttypmod) AS coord_dimension,
    public.postgis_typmod_srid(a.atttypmod) AS srid,
    public.postgis_typmod_type(a.atttypmod) AS type
   FROM pg_class c,
    pg_attribute a,
    pg_type t,
    pg_namespace n
  WHERE ((t.typname = 'geography'::name) AND (a.attisdropped = false) AND (a.atttypid = t.oid) AND (a.attrelid = c.oid) AND (c.relnamespace = n.oid) AND (c.relkind = ANY (ARRAY['r'::"char", 'v'::"char", 'm'::"char", 'f'::"char", 'p'::"char"])) AND (NOT pg_is_other_temp_schema(c.relnamespace)) AND has_table_privilege(c.oid, 'SELECT'::text));


ALTER TABLE public.geography_columns OWNER TO postgres;

--
-- Name: geometry_columns; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.geometry_columns AS
 SELECT (current_database())::character varying(256) AS f_table_catalog,
    n.nspname AS f_table_schema,
    c.relname AS f_table_name,
    a.attname AS f_geometry_column,
    COALESCE(public.postgis_typmod_dims(a.atttypmod), sn.ndims, 2) AS coord_dimension,
    COALESCE(NULLIF(public.postgis_typmod_srid(a.atttypmod), 0), sr.srid, 0) AS srid,
    (replace(replace(COALESCE(NULLIF(upper(public.postgis_typmod_type(a.atttypmod)), 'GEOMETRY'::text), st.type, 'GEOMETRY'::text), 'ZM'::text, ''::text), 'Z'::text, ''::text))::character varying(30) AS type
   FROM ((((((pg_class c
     JOIN pg_attribute a ON (((a.attrelid = c.oid) AND (NOT a.attisdropped))))
     JOIN pg_namespace n ON ((c.relnamespace = n.oid)))
     JOIN pg_type t ON ((a.atttypid = t.oid)))
     LEFT JOIN ( SELECT s.connamespace,
            s.conrelid,
            s.conkey,
            replace(split_part(s.consrc, ''''::text, 2), ')'::text, ''::text) AS type
           FROM pg_constraint s
          WHERE (s.consrc ~~* '%geometrytype(% = %'::text)) st ON (((st.connamespace = n.oid) AND (st.conrelid = c.oid) AND (a.attnum = ANY (st.conkey)))))
     LEFT JOIN ( SELECT s.connamespace,
            s.conrelid,
            s.conkey,
            (replace(split_part(s.consrc, ' = '::text, 2), ')'::text, ''::text))::integer AS ndims
           FROM pg_constraint s
          WHERE (s.consrc ~~* '%ndims(% = %'::text)) sn ON (((sn.connamespace = n.oid) AND (sn.conrelid = c.oid) AND (a.attnum = ANY (sn.conkey)))))
     LEFT JOIN ( SELECT s.connamespace,
            s.conrelid,
            s.conkey,
            (replace(replace(split_part(s.consrc, ' = '::text, 2), ')'::text, ''::text), '('::text, ''::text))::integer AS srid
           FROM pg_constraint s
          WHERE (s.consrc ~~* '%srid(% = %'::text)) sr ON (((sr.connamespace = n.oid) AND (sr.conrelid = c.oid) AND (a.attnum = ANY (sr.conkey)))))
  WHERE ((c.relkind = ANY (ARRAY['r'::"char", 'v'::"char", 'm'::"char", 'f'::"char", 'p'::"char"])) AND (NOT (c.relname = 'raster_columns'::name)) AND (t.typname = 'geometry'::name) AND (NOT pg_is_other_temp_schema(c.relnamespace)) AND has_table_privilege(c.oid, 'SELECT'::text));


ALTER TABLE public.geometry_columns OWNER TO postgres;

--
-- Name: spatial_ref_sys; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.spatial_ref_sys (
    srid integer NOT NULL,
    auth_name character varying(256),
    auth_srid integer,
    srtext character varying(2048),
    proj4text character varying(2048),
    CONSTRAINT spatial_ref_sys_srid_check CHECK (((srid > 0) AND (srid <= 998999)))
);


ALTER TABLE public.spatial_ref_sys OWNER TO postgres;

--
-- Name: batplatser_aneby_id_seq; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.batplatser_aneby_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.batplatser_aneby_id_seq OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq33; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq33
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq33 OWNER TO "xplore-admin";

--
-- Name: karta_apotek; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_apotek (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq33'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_apotek OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq27; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq27
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq27 OWNER TO "xplore-admin";

--
-- Name: karta_atervinningscentral; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_atervinningscentral (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq27'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_atervinningscentral OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq28; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq28
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq28 OWNER TO "xplore-admin";

--
-- Name: karta_atervinningsstation; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_atervinningsstation (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq28'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_atervinningsstation OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq20; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq20 OWNER TO "xplore-admin";

--
-- Name: karta_badplats; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_badplats (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq20'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_badplats OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq41; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq41
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq41 OWNER TO "xplore-admin";

--
-- Name: karta_bankomat; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_bankomat (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq41'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_bankomat OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq56; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq56
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq56 OWNER TO "xplore-admin";

--
-- Name: karta_basketplan; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_basketplan (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq56'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_basketplan OWNER TO "xplore-admin";

--
-- Name: karta_batplatser; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_batplatser (
    id integer DEFAULT nextval('tranasext.batplatser_aneby_id_seq'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    beskr1 character varying(254),
    beskr2 character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_batplatser OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq57; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq57
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq57 OWNER TO "xplore-admin";

--
-- Name: karta_beachvolley; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_beachvolley (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq57'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_beachvolley OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq58; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq58
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq58 OWNER TO "xplore-admin";

--
-- Name: karta_begravningsplats; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_begravningsplats (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq58'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_begravningsplats OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_linje_id_seq; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_linje_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_linje_id_seq OWNER TO "xplore-admin";

--
-- Name: karta_belaggningsarbete; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_belaggningsarbete (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_linje_id_seq'::regclass) NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_belaggningsarbete OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq13; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq13
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq13 OWNER TO "xplore-admin";

--
-- Name: karta_bibliotek; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_bibliotek (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq13'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_bibliotek OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq14; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq14 OWNER TO "xplore-admin";

--
-- Name: karta_biograf; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_biograf (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq14'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_biograf OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq45; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq45
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq45 OWNER TO "xplore-admin";

--
-- Name: karta_boende; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_boende (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq45'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_boende OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_yta_id_seq8; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_yta_id_seq8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_yta_id_seq8 OWNER TO "xplore-admin";

--
-- Name: karta_bostadstomter_ejbebyggda; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_bostadstomter_ejbebyggda (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_yta_id_seq8'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_bostadstomter_ejbebyggda OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq59; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq59
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq59 OWNER TO "xplore-admin";

--
-- Name: karta_boulebanor; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_boulebanor (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq59'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_boulebanor OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq67; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq67
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq67 OWNER TO "xplore-admin";

--
-- Name: karta_bowling; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_bowling (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq67'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_bowling OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq53; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq53
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq53 OWNER TO "xplore-admin";

--
-- Name: karta_busshallplats; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_busshallplats (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq53'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_busshallplats OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq60; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq60
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq60 OWNER TO "xplore-admin";

--
-- Name: karta_busskort; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_busskort (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq60'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_busskort OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq31; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq31
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq31 OWNER TO "xplore-admin";

--
-- Name: karta_busstation; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_busstation (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq31'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_busstation OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq70; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq70
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq70 OWNER TO "xplore-admin";

--
-- Name: karta_bygdegardar; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_bygdegardar (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq70'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_bygdegardar OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq50; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq50
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq50 OWNER TO "xplore-admin";

--
-- Name: karta_cafe; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_cafe (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq50'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_cafe OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_linje_id_seq2; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_linje_id_seq2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_linje_id_seq2 OWNER TO "xplore-admin";

--
-- Name: karta_cykelleder; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_cykelleder (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_linje_id_seq2'::regclass) NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_cykelleder OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_linje_id_seq3; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_linje_id_seq3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_linje_id_seq3 OWNER TO "xplore-admin";

--
-- Name: karta_cykelvagar; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_cykelvagar (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_linje_id_seq3'::regclass) NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_cykelvagar OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq9; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq9 OWNER TO "xplore-admin";

--
-- Name: karta_daglig-verksamhet; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_daglig-verksamhet" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq9'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_daglig-verksamhet" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_yta_id_seq; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_yta_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_yta_id_seq OWNER TO "xplore-admin";

--
-- Name: karta_detaljplaner-gallande; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_detaljplaner-gallande" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_yta_id_seq'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(254),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_detaljplaner-gallande" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_yta_id_seq1; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_yta_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_yta_id_seq1 OWNER TO "xplore-admin";

--
-- Name: karta_detaljplaner-pagaende; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_detaljplaner-pagaende" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_yta_id_seq1'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(254),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_detaljplaner-pagaende" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq52; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq52
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq52 OWNER TO "xplore-admin";

--
-- Name: karta_drivmedelsstation; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_drivmedelsstation (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq52'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_drivmedelsstation OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq54; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq54
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq54 OWNER TO "xplore-admin";

--
-- Name: karta_fiske; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_fiske (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq54'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_fiske OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq55; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq55
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq55 OWNER TO "xplore-admin";

--
-- Name: karta_fiskekort; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_fiskekort (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq55'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_fiskekort OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq15; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq15 OWNER TO "xplore-admin";

--
-- Name: karta_folkets-park; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_folkets-park" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq15'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_folkets-park" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq36; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq36
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq36 OWNER TO "xplore-admin";

--
-- Name: karta_folktandvard; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_folktandvard (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq36'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_folktandvard OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq1; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq1 OWNER TO "xplore-admin";

--
-- Name: karta_forskola; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_forskola (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq1'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_forskola OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq17; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq17 OWNER TO "xplore-admin";

--
-- Name: karta_fotbollsplaner; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_fotbollsplaner (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq17'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_fotbollsplaner OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq18; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq18 OWNER TO "xplore-admin";

--
-- Name: karta_friidrottsanl; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_friidrottsanl (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq18'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_friidrottsanl OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq21; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq21
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq21 OWNER TO "xplore-admin";

--
-- Name: karta_fritidsgard; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_fritidsgard (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq21'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_fritidsgard OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq40; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq40
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq40 OWNER TO "xplore-admin";

--
-- Name: karta_fritidshem; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_fritidshem (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq40'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_fritidshem OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq16; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq16 OWNER TO "xplore-admin";

--
-- Name: karta_golfbanor; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_golfbanor (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq16'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_golfbanor OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_yta_id_seq2; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_yta_id_seq2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_yta_id_seq2 OWNER TO "xplore-admin";

--
-- Name: karta_grav-uppstallningstillstand; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_grav-uppstallningstillstand" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_yta_id_seq2'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_grav-uppstallningstillstand" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq74; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq74
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq74 OWNER TO "xplore-admin";

--
-- Name: karta_gym; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_gym (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq74'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_gym OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq39; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq39
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq39 OWNER TO "xplore-admin";

--
-- Name: karta_hamn; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_hamn (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq39'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_hamn OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq71; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq71
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq71 OWNER TO "xplore-admin";

--
-- Name: karta_hembygdsforening; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_hembygdsforening (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq71'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_hembygdsforening OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq43; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq43
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq43 OWNER TO "xplore-admin";

--
-- Name: karta_hjartstartare; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_hjartstartare (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq43'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_hjartstartare OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq73; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq73
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq73 OWNER TO "xplore-admin";

--
-- Name: karta_hundklubb; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_hundklubb (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq73'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_hundklubb OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq76; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq76
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq76 OWNER TO "xplore-admin";

--
-- Name: karta_hundpasar; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_hundpasar (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq76'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_hundpasar OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq19; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq19 OWNER TO "xplore-admin";

--
-- Name: karta_idrottshall; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_idrottshall (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq19'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_idrottshall OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq2; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq2 OWNER TO "xplore-admin";

--
-- Name: karta_isbanor; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_isbanor (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq2'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_isbanor OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq82; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq82
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq82 OWNER TO "xplore-admin";

--
-- Name: karta_kolonilotter; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_kolonilotter (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq82'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_kolonilotter OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_yta_id_seq7; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_yta_id_seq7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_yta_id_seq7 OWNER TO "xplore-admin";

--
-- Name: karta_kommunagd-mark; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_kommunagd-mark" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_yta_id_seq7'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_kommunagd-mark" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq7; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq7 OWNER TO "xplore-admin";

--
-- Name: karta_kommunkontor; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_kommunkontor (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq7'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_kommunkontor OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq4; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq4 OWNER TO "xplore-admin";

--
-- Name: karta_konsumentradgivning; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_konsumentradgivning (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq4'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_konsumentradgivning OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq69; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq69
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq69 OWNER TO "xplore-admin";

--
-- Name: karta_kulturforening; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_kulturforening (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq69'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_kulturforening OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq78; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq78
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq78 OWNER TO "xplore-admin";

--
-- Name: karta_kulturskolan; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_kulturskolan (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq78'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_kulturskolan OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq34; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq34
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq34 OWNER TO "xplore-admin";

--
-- Name: karta_laddstationer; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_laddstationer (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq34'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_laddstationer OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq61; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq61
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq61 OWNER TO "xplore-admin";

--
-- Name: karta_langdskidspar; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_langdskidspar (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq61'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_langdskidspar OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq79; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq79
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq79 OWNER TO "xplore-admin";

--
-- Name: karta_latrinkarl; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_latrinkarl (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq79'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_latrinkarl OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_yta_id_seq6; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_yta_id_seq6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_yta_id_seq6 OWNER TO "xplore-admin";

--
-- Name: karta_ledig-industrimark; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_ledig-industrimark" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_yta_id_seq6'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_ledig-industrimark" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_yta_id_seq9; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_yta_id_seq9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_yta_id_seq9 OWNER TO "xplore-admin";

--
-- Name: karta_ledig-industrimark_privatagd; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_ledig-industrimark_privatagd" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_yta_id_seq9'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_ledig-industrimark_privatagd" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq75; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq75
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq75 OWNER TO "xplore-admin";

--
-- Name: karta_lediga-bostadstomter; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_lediga-bostadstomter" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq75'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_lediga-bostadstomter" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq35; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq35
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq35 OWNER TO "xplore-admin";

--
-- Name: karta_lekplats; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_lekplats (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq35'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_lekplats OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq51; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq51
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq51 OWNER TO "xplore-admin";

--
-- Name: karta_livsmedelsbutik; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_livsmedelsbutik (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq51'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_livsmedelsbutik OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq22; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq22
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq22 OWNER TO "xplore-admin";

--
-- Name: karta_motionsanl; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_motionsanl (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq22'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_motionsanl OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_linje_id_seq4; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_linje_id_seq4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_linje_id_seq4 OWNER TO "xplore-admin";

--
-- Name: karta_motionsspar_elljusspar; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_motionsspar_elljusspar (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_linje_id_seq4'::regclass) NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_motionsspar_elljusspar OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_linje_id_seq5; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_linje_id_seq5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_linje_id_seq5 OWNER TO "xplore-admin";

--
-- Name: karta_motionsspar_uppmarkta; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_motionsspar_uppmarkta (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_linje_id_seq5'::regclass) NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_motionsspar_uppmarkta OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq25; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq25
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq25 OWNER TO "xplore-admin";

--
-- Name: karta_motorcrossbana; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_motorcrossbana (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq25'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_motorcrossbana OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_yta_id_seq12; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_yta_id_seq12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_yta_id_seq12 OWNER TO "xplore-admin";

--
-- Name: karta_naturreservat; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_naturreservat (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_yta_id_seq12'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_naturreservat OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_yta_id_seq3; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_yta_id_seq3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_yta_id_seq3 OWNER TO "xplore-admin";

--
-- Name: karta_naturvardsprogram-objekt; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_naturvardsprogram-objekt" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_yta_id_seq3'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_naturvardsprogram-objekt" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_yta_id_seq10; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_yta_id_seq10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_yta_id_seq10 OWNER TO "xplore-admin";

--
-- Name: karta_naturvardsprogram-omraden; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_naturvardsprogram-omraden" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_yta_id_seq10'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_naturvardsprogram-omraden" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq38; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq38
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq38 OWNER TO "xplore-admin";

--
-- Name: karta_obo; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_obo (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq38'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_obo OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq68; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq68
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq68 OWNER TO "xplore-admin";

--
-- Name: karta_offentligatoaletter; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_offentligatoaletter (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq68'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_offentligatoaletter OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq10; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq10 OWNER TO "xplore-admin";

--
-- Name: karta_okad-sysselsattning; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_okad-sysselsattning" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq10'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_okad-sysselsattning" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq81; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq81
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq81 OWNER TO "xplore-admin";

--
-- Name: karta_park; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_park (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq81'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_park OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq29; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq29
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq29 OWNER TO "xplore-admin";

--
-- Name: karta_parkering-pendlar; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_parkering-pendlar" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq29'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_parkering-pendlar" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq62; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq62
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq62 OWNER TO "xplore-admin";

--
-- Name: karta_parkering_rorelsehindrade; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_parkering_rorelsehindrade (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq62'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_parkering_rorelsehindrade OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq30; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq30
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq30 OWNER TO "xplore-admin";

--
-- Name: karta_parkeringsplatser; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_parkeringsplatser (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq30'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_parkeringsplatser OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq3; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq3 OWNER TO "xplore-admin";

--
-- Name: karta_polis; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_polis (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq3'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_polis OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq63; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq63
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq63 OWNER TO "xplore-admin";

--
-- Name: karta_postombud; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_postombud (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq63'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_postombud OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq23; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq23
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq23 OWNER TO "xplore-admin";

--
-- Name: karta_racketanl; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_racketanl (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq23'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_racketanl OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq5; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq5 OWNER TO "xplore-admin";

--
-- Name: karta_raddningstjanst; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_raddningstjanst (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq5'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_raddningstjanst OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq49; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq49
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq49 OWNER TO "xplore-admin";

--
-- Name: karta_restauranger; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_restauranger (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq49'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_restauranger OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq26; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq26
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq26 OWNER TO "xplore-admin";

--
-- Name: karta_ridanl; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_ridanl (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq26'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_ridanl OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq77; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq77
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq77 OWNER TO "xplore-admin";

--
-- Name: karta_samlingslokal; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_samlingslokal (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq77'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_samlingslokal OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq48; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq48
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq48 OWNER TO "xplore-admin";

--
-- Name: karta_sevardheter_besoksmal; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_sevardheter_besoksmal (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq48'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_sevardheter_besoksmal OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq47; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq47
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq47 OWNER TO "xplore-admin";

--
-- Name: karta_sevardheter_kultur; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_sevardheter_kultur (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq47'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_sevardheter_kultur OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq46; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq46
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq46 OWNER TO "xplore-admin";

--
-- Name: karta_sevardheter_natur; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_sevardheter_natur (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq46'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_sevardheter_natur OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq24; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq24
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq24 OWNER TO "xplore-admin";

--
-- Name: karta_skatepark; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_skatepark (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq24'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_skatepark OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq64; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq64
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq64 OWNER TO "xplore-admin";

--
-- Name: karta_skjutbana; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_skjutbana (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq64'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_skjutbana OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq OWNER TO "xplore-admin";

--
-- Name: karta_skola; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_skola (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_skola OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq37; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq37
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq37 OWNER TO "xplore-admin";

--
-- Name: karta_socialkontoret; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_socialkontoret (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq37'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_socialkontoret OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_yta_id_seq11; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_yta_id_seq11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_yta_id_seq11 OWNER TO "xplore-admin";

--
-- Name: karta_sophamtning; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_sophamtning (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_yta_id_seq11'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_sophamtning OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq72; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq72
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq72 OWNER TO "xplore-admin";

--
-- Name: karta_spontanidrottsplats; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_spontanidrottsplats (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq72'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_spontanidrottsplats OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq80; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq80
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq80 OWNER TO "xplore-admin";

--
-- Name: karta_station; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_station (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq80'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_station OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq65; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq65
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq65 OWNER TO "xplore-admin";

--
-- Name: karta_systembolaget; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_systembolaget (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq65'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_systembolaget OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_yta_id_seq4; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_yta_id_seq4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_yta_id_seq4 OWNER TO "xplore-admin";

--
-- Name: karta_torghandel; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_torghandel (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_yta_id_seq4'::regclass) NOT NULL,
    geom public.geometry(MultiPolygon,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_torghandel OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq12; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq12 OWNER TO "xplore-admin";

--
-- Name: karta_traffpunkt-aktivitetshuset; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_traffpunkt-aktivitetshuset" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq12'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_traffpunkt-aktivitetshuset" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq11; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq11 OWNER TO "xplore-admin";

--
-- Name: karta_traffpunkt-aldre; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_traffpunkt-aldre" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq11'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_traffpunkt-aldre" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq42; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq42
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq42 OWNER TO "xplore-admin";

--
-- Name: karta_traffpunkt-funktionsnedsatta; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_traffpunkt-funktionsnedsatta" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq42'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_traffpunkt-funktionsnedsatta" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq44; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq44
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq44 OWNER TO "xplore-admin";

--
-- Name: karta_turistinformation; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_turistinformation (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq44'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_turistinformation OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq32; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq32
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq32 OWNER TO "xplore-admin";

--
-- Name: karta_uppstallningsplats-husbilar; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_uppstallningsplats-husbilar" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq32'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_uppstallningsplats-husbilar" OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq66; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq66
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq66 OWNER TO "xplore-admin";

--
-- Name: karta_utomhusgym; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_utomhusgym (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq66'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_utomhusgym OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_linje_id_seq1; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_linje_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_linje_id_seq1 OWNER TO "xplore-admin";

--
-- Name: karta_vandringsleder; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_vandringsleder (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_linje_id_seq1'::regclass) NOT NULL,
    geom public.geometry(MultiLineString,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_vandringsleder OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq6; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq6 OWNER TO "xplore-admin";

--
-- Name: karta_vardcentral; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext.karta_vardcentral (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq6'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext.karta_vardcentral OWNER TO "xplore-admin";

--
-- Name: standard_kommunkarta_punkt_id_seq8; Type: SEQUENCE; Schema: tranasext; Owner: xplore-admin
--

CREATE SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE tranasext.standard_kommunkarta_punkt_id_seq8 OWNER TO "xplore-admin";

--
-- Name: karta_viktigt-meddelande; Type: TABLE; Schema: tranasext; Owner: xplore-admin
--

CREATE TABLE tranasext."karta_viktigt-meddelande" (
    id integer DEFAULT nextval('tranasext.standard_kommunkarta_punkt_id_seq8'::regclass) NOT NULL,
    geom public.geometry(MultiPoint,3009),
    namn character varying(80),
    beskr character varying(254),
    link character varying(254),
    adress character varying(254),
    kontakt character varying(254)
);


ALTER TABLE tranasext."karta_viktigt-meddelande" OWNER TO "xplore-admin";

--
-- Name: karta_apotek id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_apotek ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq33'::regclass);


--
-- Name: karta_atervinningscentral id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_atervinningscentral ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq27'::regclass);


--
-- Name: karta_atervinningsstation id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_atervinningsstation ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq28'::regclass);


--
-- Name: karta_badplats id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_badplats ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq20'::regclass);


--
-- Name: karta_bankomat id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_bankomat ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq41'::regclass);


--
-- Name: karta_basketplan id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_basketplan ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq56'::regclass);


--
-- Name: karta_batplatser id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_batplatser ALTER COLUMN id SET DEFAULT nextval('anebyext.batplatser_aneby_id_seq'::regclass);


--
-- Name: karta_beachvolley id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_beachvolley ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq57'::regclass);


--
-- Name: karta_begravningsplats id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_begravningsplats ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq58'::regclass);


--
-- Name: karta_belaggningsarbete id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_belaggningsarbete ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_linje_id_seq'::regclass);


--
-- Name: karta_bibliotek id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_bibliotek ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq13'::regclass);


--
-- Name: karta_biograf id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_biograf ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq14'::regclass);


--
-- Name: karta_boende id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_boende ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq45'::regclass);


--
-- Name: karta_bostadstomter_ejbebyggda id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_bostadstomter_ejbebyggda ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_yta_id_seq8'::regclass);


--
-- Name: karta_boulebanor id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_boulebanor ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq59'::regclass);


--
-- Name: karta_bowling id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_bowling ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq67'::regclass);


--
-- Name: karta_busshallplats id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_busshallplats ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq53'::regclass);


--
-- Name: karta_busskort id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_busskort ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq60'::regclass);


--
-- Name: karta_busstation id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_busstation ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq31'::regclass);


--
-- Name: karta_bygdegardar id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_bygdegardar ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq70'::regclass);


--
-- Name: karta_cafe id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_cafe ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq50'::regclass);


--
-- Name: karta_cykelleder id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_cykelleder ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_linje_id_seq2'::regclass);


--
-- Name: karta_cykelvagar id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_cykelvagar ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_linje_id_seq3'::regclass);


--
-- Name: karta_daglig-verksamhet id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_daglig-verksamhet" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq9'::regclass);


--
-- Name: karta_detaljplaner-gallande id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_detaljplaner-gallande" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_yta_id_seq'::regclass);


--
-- Name: karta_detaljplaner-pagaende id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_detaljplaner-pagaende" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_yta_id_seq1'::regclass);


--
-- Name: karta_drivmedelsstation id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_drivmedelsstation ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq52'::regclass);


--
-- Name: karta_fiske id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_fiske ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq54'::regclass);


--
-- Name: karta_fiskekort id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_fiskekort ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq55'::regclass);


--
-- Name: karta_folkets-park id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_folkets-park" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq15'::regclass);


--
-- Name: karta_folktandvard id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_folktandvard ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq36'::regclass);


--
-- Name: karta_forskola id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_forskola ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq1'::regclass);


--
-- Name: karta_fotbollsplaner id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_fotbollsplaner ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq17'::regclass);


--
-- Name: karta_friidrottsanl id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_friidrottsanl ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq18'::regclass);


--
-- Name: karta_fritidsgard id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_fritidsgard ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq21'::regclass);


--
-- Name: karta_fritidshem id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_fritidshem ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq40'::regclass);


--
-- Name: karta_golfbanor id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_golfbanor ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq16'::regclass);


--
-- Name: karta_grav-uppstallningstillstand id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_grav-uppstallningstillstand" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_yta_id_seq2'::regclass);


--
-- Name: karta_gym id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_gym ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq74'::regclass);


--
-- Name: karta_hamn id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_hamn ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq39'::regclass);


--
-- Name: karta_hembygdsforening id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_hembygdsforening ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq71'::regclass);


--
-- Name: karta_hjartstartare id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_hjartstartare ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq43'::regclass);


--
-- Name: karta_hundklubb id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_hundklubb ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq73'::regclass);


--
-- Name: karta_hundpasar id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_hundpasar ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq76'::regclass);


--
-- Name: karta_idrottshall id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_idrottshall ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq19'::regclass);


--
-- Name: karta_isbanor id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_isbanor ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq2'::regclass);


--
-- Name: karta_kommunagd-mark id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_kommunagd-mark" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_yta_id_seq7'::regclass);


--
-- Name: karta_kommunkontor id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_kommunkontor ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq7'::regclass);


--
-- Name: karta_konsumentradgivning id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_konsumentradgivning ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq4'::regclass);


--
-- Name: karta_kulturforening id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_kulturforening ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq69'::regclass);


--
-- Name: karta_laddstationer id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_laddstationer ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq34'::regclass);


--
-- Name: karta_langdskidspar id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_langdskidspar ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq61'::regclass);


--
-- Name: karta_ledig-industrimark id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_ledig-industrimark" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_yta_id_seq6'::regclass);


--
-- Name: karta_ledig-industrimark_privatagd id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_ledig-industrimark_privatagd" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_yta_id_seq9'::regclass);


--
-- Name: karta_lediga-bostadstomter id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_lediga-bostadstomter" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq75'::regclass);


--
-- Name: karta_lekplats id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_lekplats ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq35'::regclass);


--
-- Name: karta_livsmedelsbutik id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_livsmedelsbutik ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq51'::regclass);


--
-- Name: karta_motionsanl id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_motionsanl ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq22'::regclass);


--
-- Name: karta_motionsspar_elljusspar id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_motionsspar_elljusspar ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_linje_id_seq4'::regclass);


--
-- Name: karta_motionsspar_uppmarkta id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_motionsspar_uppmarkta ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_linje_id_seq5'::regclass);


--
-- Name: karta_motorcrossbana id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_motorcrossbana ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq25'::regclass);


--
-- Name: karta_naturreservat id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_naturreservat ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_yta_id_seq12'::regclass);


--
-- Name: karta_naturvardsprogram-objekt id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_naturvardsprogram-objekt" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_yta_id_seq3'::regclass);


--
-- Name: karta_naturvardsprogram-omraden id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_naturvardsprogram-omraden" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_yta_id_seq10'::regclass);


--
-- Name: karta_obo id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_obo ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq38'::regclass);


--
-- Name: karta_offentligatoaletter id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_offentligatoaletter ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq68'::regclass);


--
-- Name: karta_okad-sysselsattning id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_okad-sysselsattning" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq10'::regclass);


--
-- Name: karta_parkering-pendlar id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_parkering-pendlar" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq29'::regclass);


--
-- Name: karta_parkering_rorelsehindrade id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_parkering_rorelsehindrade ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq62'::regclass);


--
-- Name: karta_parkeringsplatser id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_parkeringsplatser ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq30'::regclass);


--
-- Name: karta_polis id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_polis ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq3'::regclass);


--
-- Name: karta_postombud id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_postombud ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq63'::regclass);


--
-- Name: karta_racketanl id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_racketanl ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq23'::regclass);


--
-- Name: karta_raddningstjanst id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_raddningstjanst ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq5'::regclass);


--
-- Name: karta_restauranger id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_restauranger ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq49'::regclass);


--
-- Name: karta_ridanl id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_ridanl ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq26'::regclass);


--
-- Name: karta_samlingslokal id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_samlingslokal ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq77'::regclass);


--
-- Name: karta_sevardheter_besoksmal id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_sevardheter_besoksmal ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq48'::regclass);


--
-- Name: karta_sevardheter_kultur id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_sevardheter_kultur ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq47'::regclass);


--
-- Name: karta_sevardheter_natur id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_sevardheter_natur ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq46'::regclass);


--
-- Name: karta_skatepark id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_skatepark ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq24'::regclass);


--
-- Name: karta_skjutbana id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_skjutbana ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq64'::regclass);


--
-- Name: karta_skola id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_skola ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq'::regclass);


--
-- Name: karta_socialkontoret id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_socialkontoret ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq37'::regclass);


--
-- Name: karta_sophamtning id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_sophamtning ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_yta_id_seq11'::regclass);


--
-- Name: karta_spontanidrottsplats id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_spontanidrottsplats ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq72'::regclass);


--
-- Name: karta_systembolaget id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_systembolaget ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq65'::regclass);


--
-- Name: karta_torghandel id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_torghandel ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_yta_id_seq4'::regclass);


--
-- Name: karta_traffpunkt-aktivitetshuset id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_traffpunkt-aktivitetshuset" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq12'::regclass);


--
-- Name: karta_traffpunkt-aldre id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_traffpunkt-aldre" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq11'::regclass);


--
-- Name: karta_traffpunkt-funktionsnedsatta id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_traffpunkt-funktionsnedsatta" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq42'::regclass);


--
-- Name: karta_turistinformation id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_turistinformation ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq44'::regclass);


--
-- Name: karta_uppstallningsplats-husbilar id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_uppstallningsplats-husbilar" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq32'::regclass);


--
-- Name: karta_utomhusgym id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_utomhusgym ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq66'::regclass);


--
-- Name: karta_vandringsleder id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_vandringsleder ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_linje_id_seq1'::regclass);


--
-- Name: karta_vardcentral id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_vardcentral ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq6'::regclass);


--
-- Name: karta_viktigt-meddelande id; Type: DEFAULT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_viktigt-meddelande" ALTER COLUMN id SET DEFAULT nextval('anebyext.standard_kommunkarta_punkt_id_seq8'::regclass);


--
-- Name: karta_batplatser Batplatser_aneby_pkey; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_batplatser
    ADD CONSTRAINT "Batplatser_aneby_pkey" PRIMARY KEY (id);


--
-- Name: karta_belaggningsarbete standard_kommunkarta_linje_pkey; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_belaggningsarbete
    ADD CONSTRAINT standard_kommunkarta_linje_pkey PRIMARY KEY (id);


--
-- Name: karta_vandringsleder standard_kommunkarta_linje_pkey1; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_vandringsleder
    ADD CONSTRAINT standard_kommunkarta_linje_pkey1 PRIMARY KEY (id);


--
-- Name: karta_cykelleder standard_kommunkarta_linje_pkey2; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_cykelleder
    ADD CONSTRAINT standard_kommunkarta_linje_pkey2 PRIMARY KEY (id);


--
-- Name: karta_cykelvagar standard_kommunkarta_linje_pkey3; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_cykelvagar
    ADD CONSTRAINT standard_kommunkarta_linje_pkey3 PRIMARY KEY (id);


--
-- Name: karta_motionsspar_elljusspar standard_kommunkarta_linje_pkey4; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_motionsspar_elljusspar
    ADD CONSTRAINT standard_kommunkarta_linje_pkey4 PRIMARY KEY (id);


--
-- Name: karta_motionsspar_uppmarkta standard_kommunkarta_linje_pkey5; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_motionsspar_uppmarkta
    ADD CONSTRAINT standard_kommunkarta_linje_pkey5 PRIMARY KEY (id);


--
-- Name: karta_skola standard_kommunkarta_punkt_pkey; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_skola
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey PRIMARY KEY (id);


--
-- Name: karta_forskola standard_kommunkarta_punkt_pkey1; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_forskola
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey1 PRIMARY KEY (id);


--
-- Name: karta_okad-sysselsattning standard_kommunkarta_punkt_pkey10; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_okad-sysselsattning"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey10 PRIMARY KEY (id);


--
-- Name: karta_traffpunkt-aldre standard_kommunkarta_punkt_pkey11; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_traffpunkt-aldre"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey11 PRIMARY KEY (id);


--
-- Name: karta_traffpunkt-aktivitetshuset standard_kommunkarta_punkt_pkey12; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_traffpunkt-aktivitetshuset"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey12 PRIMARY KEY (id);


--
-- Name: karta_bibliotek standard_kommunkarta_punkt_pkey13; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_bibliotek
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey13 PRIMARY KEY (id);


--
-- Name: karta_biograf standard_kommunkarta_punkt_pkey14; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_biograf
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey14 PRIMARY KEY (id);


--
-- Name: karta_folkets-park standard_kommunkarta_punkt_pkey15; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_folkets-park"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey15 PRIMARY KEY (id);


--
-- Name: karta_golfbanor standard_kommunkarta_punkt_pkey16; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_golfbanor
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey16 PRIMARY KEY (id);


--
-- Name: karta_fotbollsplaner standard_kommunkarta_punkt_pkey17; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_fotbollsplaner
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey17 PRIMARY KEY (id);


--
-- Name: karta_friidrottsanl standard_kommunkarta_punkt_pkey18; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_friidrottsanl
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey18 PRIMARY KEY (id);


--
-- Name: karta_idrottshall standard_kommunkarta_punkt_pkey19; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_idrottshall
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey19 PRIMARY KEY (id);


--
-- Name: karta_isbanor standard_kommunkarta_punkt_pkey2; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_isbanor
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey2 PRIMARY KEY (id);


--
-- Name: karta_badplats standard_kommunkarta_punkt_pkey20; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_badplats
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey20 PRIMARY KEY (id);


--
-- Name: karta_fritidsgard standard_kommunkarta_punkt_pkey21; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_fritidsgard
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey21 PRIMARY KEY (id);


--
-- Name: karta_motionsanl standard_kommunkarta_punkt_pkey22; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_motionsanl
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey22 PRIMARY KEY (id);


--
-- Name: karta_racketanl standard_kommunkarta_punkt_pkey23; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_racketanl
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey23 PRIMARY KEY (id);


--
-- Name: karta_skatepark standard_kommunkarta_punkt_pkey24; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_skatepark
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey24 PRIMARY KEY (id);


--
-- Name: karta_motorcrossbana standard_kommunkarta_punkt_pkey25; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_motorcrossbana
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey25 PRIMARY KEY (id);


--
-- Name: karta_ridanl standard_kommunkarta_punkt_pkey26; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_ridanl
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey26 PRIMARY KEY (id);


--
-- Name: karta_atervinningscentral standard_kommunkarta_punkt_pkey27; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_atervinningscentral
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey27 PRIMARY KEY (id);


--
-- Name: karta_atervinningsstation standard_kommunkarta_punkt_pkey28; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_atervinningsstation
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey28 PRIMARY KEY (id);


--
-- Name: karta_parkering-pendlar standard_kommunkarta_punkt_pkey29; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_parkering-pendlar"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey29 PRIMARY KEY (id);


--
-- Name: karta_polis standard_kommunkarta_punkt_pkey3; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_polis
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey3 PRIMARY KEY (id);


--
-- Name: karta_parkeringsplatser standard_kommunkarta_punkt_pkey30; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_parkeringsplatser
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey30 PRIMARY KEY (id);


--
-- Name: karta_busstation standard_kommunkarta_punkt_pkey31; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_busstation
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey31 PRIMARY KEY (id);


--
-- Name: karta_uppstallningsplats-husbilar standard_kommunkarta_punkt_pkey32; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_uppstallningsplats-husbilar"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey32 PRIMARY KEY (id);


--
-- Name: karta_apotek standard_kommunkarta_punkt_pkey33; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_apotek
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey33 PRIMARY KEY (id);


--
-- Name: karta_laddstationer standard_kommunkarta_punkt_pkey34; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_laddstationer
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey34 PRIMARY KEY (id);


--
-- Name: karta_lekplats standard_kommunkarta_punkt_pkey35; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_lekplats
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey35 PRIMARY KEY (id);


--
-- Name: karta_folktandvard standard_kommunkarta_punkt_pkey36; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_folktandvard
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey36 PRIMARY KEY (id);


--
-- Name: karta_socialkontoret standard_kommunkarta_punkt_pkey37; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_socialkontoret
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey37 PRIMARY KEY (id);


--
-- Name: karta_obo standard_kommunkarta_punkt_pkey38; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_obo
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey38 PRIMARY KEY (id);


--
-- Name: karta_hamn standard_kommunkarta_punkt_pkey39; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_hamn
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey39 PRIMARY KEY (id);


--
-- Name: karta_konsumentradgivning standard_kommunkarta_punkt_pkey4; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_konsumentradgivning
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey4 PRIMARY KEY (id);


--
-- Name: karta_fritidshem standard_kommunkarta_punkt_pkey40; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_fritidshem
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey40 PRIMARY KEY (id);


--
-- Name: karta_bankomat standard_kommunkarta_punkt_pkey41; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_bankomat
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey41 PRIMARY KEY (id);


--
-- Name: karta_traffpunkt-funktionsnedsatta standard_kommunkarta_punkt_pkey42; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_traffpunkt-funktionsnedsatta"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey42 PRIMARY KEY (id);


--
-- Name: karta_hjartstartare standard_kommunkarta_punkt_pkey43; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_hjartstartare
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey43 PRIMARY KEY (id);


--
-- Name: karta_turistinformation standard_kommunkarta_punkt_pkey44; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_turistinformation
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey44 PRIMARY KEY (id);


--
-- Name: karta_boende standard_kommunkarta_punkt_pkey45; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_boende
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey45 PRIMARY KEY (id);


--
-- Name: karta_sevardheter_natur standard_kommunkarta_punkt_pkey46; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_sevardheter_natur
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey46 PRIMARY KEY (id);


--
-- Name: karta_sevardheter_kultur standard_kommunkarta_punkt_pkey47; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_sevardheter_kultur
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey47 PRIMARY KEY (id);


--
-- Name: karta_sevardheter_besoksmal standard_kommunkarta_punkt_pkey48; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_sevardheter_besoksmal
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey48 PRIMARY KEY (id);


--
-- Name: karta_restauranger standard_kommunkarta_punkt_pkey49; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_restauranger
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey49 PRIMARY KEY (id);


--
-- Name: karta_raddningstjanst standard_kommunkarta_punkt_pkey5; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_raddningstjanst
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey5 PRIMARY KEY (id);


--
-- Name: karta_cafe standard_kommunkarta_punkt_pkey50; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_cafe
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey50 PRIMARY KEY (id);


--
-- Name: karta_livsmedelsbutik standard_kommunkarta_punkt_pkey51; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_livsmedelsbutik
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey51 PRIMARY KEY (id);


--
-- Name: karta_drivmedelsstation standard_kommunkarta_punkt_pkey52; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_drivmedelsstation
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey52 PRIMARY KEY (id);


--
-- Name: karta_busshallplats standard_kommunkarta_punkt_pkey53; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_busshallplats
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey53 PRIMARY KEY (id);


--
-- Name: karta_fiske standard_kommunkarta_punkt_pkey54; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_fiske
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey54 PRIMARY KEY (id);


--
-- Name: karta_fiskekort standard_kommunkarta_punkt_pkey55; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_fiskekort
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey55 PRIMARY KEY (id);


--
-- Name: karta_basketplan standard_kommunkarta_punkt_pkey56; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_basketplan
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey56 PRIMARY KEY (id);


--
-- Name: karta_beachvolley standard_kommunkarta_punkt_pkey57; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_beachvolley
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey57 PRIMARY KEY (id);


--
-- Name: karta_begravningsplats standard_kommunkarta_punkt_pkey58; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_begravningsplats
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey58 PRIMARY KEY (id);


--
-- Name: karta_boulebanor standard_kommunkarta_punkt_pkey59; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_boulebanor
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey59 PRIMARY KEY (id);


--
-- Name: karta_vardcentral standard_kommunkarta_punkt_pkey6; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_vardcentral
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey6 PRIMARY KEY (id);


--
-- Name: karta_busskort standard_kommunkarta_punkt_pkey60; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_busskort
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey60 PRIMARY KEY (id);


--
-- Name: karta_langdskidspar standard_kommunkarta_punkt_pkey61; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_langdskidspar
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey61 PRIMARY KEY (id);


--
-- Name: karta_parkering_rorelsehindrade standard_kommunkarta_punkt_pkey62; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_parkering_rorelsehindrade
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey62 PRIMARY KEY (id);


--
-- Name: karta_postombud standard_kommunkarta_punkt_pkey63; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_postombud
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey63 PRIMARY KEY (id);


--
-- Name: karta_skjutbana standard_kommunkarta_punkt_pkey64; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_skjutbana
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey64 PRIMARY KEY (id);


--
-- Name: karta_systembolaget standard_kommunkarta_punkt_pkey65; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_systembolaget
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey65 PRIMARY KEY (id);


--
-- Name: karta_utomhusgym standard_kommunkarta_punkt_pkey66; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_utomhusgym
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey66 PRIMARY KEY (id);


--
-- Name: karta_bowling standard_kommunkarta_punkt_pkey67; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_bowling
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey67 PRIMARY KEY (id);


--
-- Name: karta_offentligatoaletter standard_kommunkarta_punkt_pkey68; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_offentligatoaletter
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey68 PRIMARY KEY (id);


--
-- Name: karta_kulturforening standard_kommunkarta_punkt_pkey69; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_kulturforening
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey69 PRIMARY KEY (id);


--
-- Name: karta_kommunkontor standard_kommunkarta_punkt_pkey7; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_kommunkontor
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey7 PRIMARY KEY (id);


--
-- Name: karta_bygdegardar standard_kommunkarta_punkt_pkey70; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_bygdegardar
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey70 PRIMARY KEY (id);


--
-- Name: karta_hembygdsforening standard_kommunkarta_punkt_pkey71; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_hembygdsforening
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey71 PRIMARY KEY (id);


--
-- Name: karta_spontanidrottsplats standard_kommunkarta_punkt_pkey72; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_spontanidrottsplats
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey72 PRIMARY KEY (id);


--
-- Name: karta_hundklubb standard_kommunkarta_punkt_pkey73; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_hundklubb
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey73 PRIMARY KEY (id);


--
-- Name: karta_gym standard_kommunkarta_punkt_pkey74; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_gym
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey74 PRIMARY KEY (id);


--
-- Name: karta_lediga-bostadstomter standard_kommunkarta_punkt_pkey75; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_lediga-bostadstomter"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey75 PRIMARY KEY (id);


--
-- Name: karta_hundpasar standard_kommunkarta_punkt_pkey76; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_hundpasar
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey76 PRIMARY KEY (id);


--
-- Name: karta_samlingslokal standard_kommunkarta_punkt_pkey77; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_samlingslokal
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey77 PRIMARY KEY (id);


--
-- Name: karta_kulturskolan standard_kommunkarta_punkt_pkey78; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_kulturskolan
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey78 PRIMARY KEY (id);


--
-- Name: karta_viktigt-meddelande standard_kommunkarta_punkt_pkey8; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_viktigt-meddelande"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey8 PRIMARY KEY (id);


--
-- Name: karta_daglig-verksamhet standard_kommunkarta_punkt_pkey9; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_daglig-verksamhet"
    ADD CONSTRAINT standard_kommunkarta_punkt_pkey9 PRIMARY KEY (id);


--
-- Name: karta_detaljplaner-gallande standard_kommunkarta_yta_pkey; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_detaljplaner-gallande"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey PRIMARY KEY (id);


--
-- Name: karta_detaljplaner-pagaende standard_kommunkarta_yta_pkey1; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_detaljplaner-pagaende"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey1 PRIMARY KEY (id);


--
-- Name: karta_naturvardsprogram-omraden standard_kommunkarta_yta_pkey10; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_naturvardsprogram-omraden"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey10 PRIMARY KEY (id);


--
-- Name: karta_sophamtning standard_kommunkarta_yta_pkey11; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_sophamtning
    ADD CONSTRAINT standard_kommunkarta_yta_pkey11 PRIMARY KEY (id);


--
-- Name: karta_naturreservat standard_kommunkarta_yta_pkey12; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_naturreservat
    ADD CONSTRAINT standard_kommunkarta_yta_pkey12 PRIMARY KEY (id);


--
-- Name: karta_grav-uppstallningstillstand standard_kommunkarta_yta_pkey2; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_grav-uppstallningstillstand"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey2 PRIMARY KEY (id);


--
-- Name: karta_naturvardsprogram-objekt standard_kommunkarta_yta_pkey3; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_naturvardsprogram-objekt"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey3 PRIMARY KEY (id);


--
-- Name: karta_torghandel standard_kommunkarta_yta_pkey4; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_torghandel
    ADD CONSTRAINT standard_kommunkarta_yta_pkey4 PRIMARY KEY (id);


--
-- Name: karta_ledig-industrimark standard_kommunkarta_yta_pkey6; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_ledig-industrimark"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey6 PRIMARY KEY (id);


--
-- Name: karta_kommunagd-mark standard_kommunkarta_yta_pkey7; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_kommunagd-mark"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey7 PRIMARY KEY (id);


--
-- Name: karta_bostadstomter_ejbebyggda standard_kommunkarta_yta_pkey8; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext.karta_bostadstomter_ejbebyggda
    ADD CONSTRAINT standard_kommunkarta_yta_pkey8 PRIMARY KEY (id);


--
-- Name: karta_ledig-industrimark_privatagd standard_kommunkarta_yta_pkey9; Type: CONSTRAINT; Schema: anebyext; Owner: geovis
--

ALTER TABLE ONLY anebyext."karta_ledig-industrimark_privatagd"
    ADD CONSTRAINT standard_kommunkarta_yta_pkey9 PRIMARY KEY (id);


--
-- Name: spatial_ref_sys spatial_ref_sys_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.spatial_ref_sys
    ADD CONSTRAINT spatial_ref_sys_pkey PRIMARY KEY (srid);


--
-- Name: karta_apotek karta_apotek_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_apotek
    ADD CONSTRAINT karta_apotek_pkey PRIMARY KEY (id);


--
-- Name: karta_atervinningscentral karta_atervinningscentral_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_atervinningscentral
    ADD CONSTRAINT karta_atervinningscentral_pkey PRIMARY KEY (id);


--
-- Name: karta_atervinningsstation karta_atervinningsstation_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_atervinningsstation
    ADD CONSTRAINT karta_atervinningsstation_pkey PRIMARY KEY (id);


--
-- Name: karta_badplats karta_badplats_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_badplats
    ADD CONSTRAINT karta_badplats_pkey PRIMARY KEY (id);


--
-- Name: karta_bankomat karta_bankomat_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_bankomat
    ADD CONSTRAINT karta_bankomat_pkey PRIMARY KEY (id);


--
-- Name: karta_basketplan karta_basketplan_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_basketplan
    ADD CONSTRAINT karta_basketplan_pkey PRIMARY KEY (id);


--
-- Name: karta_batplatser karta_batplatser_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_batplatser
    ADD CONSTRAINT karta_batplatser_pkey PRIMARY KEY (id);


--
-- Name: karta_beachvolley karta_beachvolley_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_beachvolley
    ADD CONSTRAINT karta_beachvolley_pkey PRIMARY KEY (id);


--
-- Name: karta_begravningsplats karta_begravningsplats_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_begravningsplats
    ADD CONSTRAINT karta_begravningsplats_pkey PRIMARY KEY (id);


--
-- Name: karta_belaggningsarbete karta_belaggningsarbete_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_belaggningsarbete
    ADD CONSTRAINT karta_belaggningsarbete_pkey PRIMARY KEY (id);


--
-- Name: karta_bibliotek karta_bibliotek_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_bibliotek
    ADD CONSTRAINT karta_bibliotek_pkey PRIMARY KEY (id);


--
-- Name: karta_biograf karta_biograf_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_biograf
    ADD CONSTRAINT karta_biograf_pkey PRIMARY KEY (id);


--
-- Name: karta_boende karta_boende_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_boende
    ADD CONSTRAINT karta_boende_pkey PRIMARY KEY (id);


--
-- Name: karta_bostadstomter_ejbebyggda karta_bostadstomter_ejbebyggda_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_bostadstomter_ejbebyggda
    ADD CONSTRAINT karta_bostadstomter_ejbebyggda_pkey PRIMARY KEY (id);


--
-- Name: karta_boulebanor karta_boulebanor_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_boulebanor
    ADD CONSTRAINT karta_boulebanor_pkey PRIMARY KEY (id);


--
-- Name: karta_bowling karta_bowling_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_bowling
    ADD CONSTRAINT karta_bowling_pkey PRIMARY KEY (id);


--
-- Name: karta_busshallplats karta_busshallplats_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_busshallplats
    ADD CONSTRAINT karta_busshallplats_pkey PRIMARY KEY (id);


--
-- Name: karta_busskort karta_busskort_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_busskort
    ADD CONSTRAINT karta_busskort_pkey PRIMARY KEY (id);


--
-- Name: karta_busstation karta_busstation_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_busstation
    ADD CONSTRAINT karta_busstation_pkey PRIMARY KEY (id);


--
-- Name: karta_bygdegardar karta_bygdegardar_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_bygdegardar
    ADD CONSTRAINT karta_bygdegardar_pkey PRIMARY KEY (id);


--
-- Name: karta_cafe karta_cafe_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_cafe
    ADD CONSTRAINT karta_cafe_pkey PRIMARY KEY (id);


--
-- Name: karta_cykelleder karta_cykelleder_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_cykelleder
    ADD CONSTRAINT karta_cykelleder_pkey PRIMARY KEY (id);


--
-- Name: karta_cykelvagar karta_cykelvagar_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_cykelvagar
    ADD CONSTRAINT karta_cykelvagar_pkey PRIMARY KEY (id);


--
-- Name: karta_daglig-verksamhet karta_daglig-verksamhet_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_daglig-verksamhet"
    ADD CONSTRAINT "karta_daglig-verksamhet_pkey" PRIMARY KEY (id);


--
-- Name: karta_detaljplaner-gallande karta_detaljplaner-gallande_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_detaljplaner-gallande"
    ADD CONSTRAINT "karta_detaljplaner-gallande_pkey" PRIMARY KEY (id);


--
-- Name: karta_detaljplaner-pagaende karta_detaljplaner-pagaende_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_detaljplaner-pagaende"
    ADD CONSTRAINT "karta_detaljplaner-pagaende_pkey" PRIMARY KEY (id);


--
-- Name: karta_drivmedelsstation karta_drivmedelsstation_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_drivmedelsstation
    ADD CONSTRAINT karta_drivmedelsstation_pkey PRIMARY KEY (id);


--
-- Name: karta_fiske karta_fiske_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_fiske
    ADD CONSTRAINT karta_fiske_pkey PRIMARY KEY (id);


--
-- Name: karta_fiskekort karta_fiskekort_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_fiskekort
    ADD CONSTRAINT karta_fiskekort_pkey PRIMARY KEY (id);


--
-- Name: karta_folkets-park karta_folkets-park_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_folkets-park"
    ADD CONSTRAINT "karta_folkets-park_pkey" PRIMARY KEY (id);


--
-- Name: karta_folktandvard karta_folktandvard_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_folktandvard
    ADD CONSTRAINT karta_folktandvard_pkey PRIMARY KEY (id);


--
-- Name: karta_forskola karta_forskola_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_forskola
    ADD CONSTRAINT karta_forskola_pkey PRIMARY KEY (id);


--
-- Name: karta_fotbollsplaner karta_fotbollsplaner_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_fotbollsplaner
    ADD CONSTRAINT karta_fotbollsplaner_pkey PRIMARY KEY (id);


--
-- Name: karta_friidrottsanl karta_friidrottsanl_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_friidrottsanl
    ADD CONSTRAINT karta_friidrottsanl_pkey PRIMARY KEY (id);


--
-- Name: karta_fritidsgard karta_fritidsgard_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_fritidsgard
    ADD CONSTRAINT karta_fritidsgard_pkey PRIMARY KEY (id);


--
-- Name: karta_fritidshem karta_fritidshem_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_fritidshem
    ADD CONSTRAINT karta_fritidshem_pkey PRIMARY KEY (id);


--
-- Name: karta_golfbanor karta_golfbanor_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_golfbanor
    ADD CONSTRAINT karta_golfbanor_pkey PRIMARY KEY (id);


--
-- Name: karta_grav-uppstallningstillstand karta_grav-uppstallningstillstand_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_grav-uppstallningstillstand"
    ADD CONSTRAINT "karta_grav-uppstallningstillstand_pkey" PRIMARY KEY (id);


--
-- Name: karta_gym karta_gym_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_gym
    ADD CONSTRAINT karta_gym_pkey PRIMARY KEY (id);


--
-- Name: karta_hamn karta_hamn_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_hamn
    ADD CONSTRAINT karta_hamn_pkey PRIMARY KEY (id);


--
-- Name: karta_hembygdsforening karta_hembygdsforening_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_hembygdsforening
    ADD CONSTRAINT karta_hembygdsforening_pkey PRIMARY KEY (id);


--
-- Name: karta_hjartstartare karta_hjartstartare_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_hjartstartare
    ADD CONSTRAINT karta_hjartstartare_pkey PRIMARY KEY (id);


--
-- Name: karta_hundklubb karta_hundklubb_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_hundklubb
    ADD CONSTRAINT karta_hundklubb_pkey PRIMARY KEY (id);


--
-- Name: karta_hundpasar karta_hundpasar_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_hundpasar
    ADD CONSTRAINT karta_hundpasar_pkey PRIMARY KEY (id);


--
-- Name: karta_idrottshall karta_idrottshall_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_idrottshall
    ADD CONSTRAINT karta_idrottshall_pkey PRIMARY KEY (id);


--
-- Name: karta_isbanor karta_isbanor_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_isbanor
    ADD CONSTRAINT karta_isbanor_pkey PRIMARY KEY (id);


--
-- Name: karta_kolonilotter karta_kolonilotter_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_kolonilotter
    ADD CONSTRAINT karta_kolonilotter_pkey PRIMARY KEY (id);


--
-- Name: karta_kommunagd-mark karta_kommunagd-mark_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_kommunagd-mark"
    ADD CONSTRAINT "karta_kommunagd-mark_pkey" PRIMARY KEY (id);


--
-- Name: karta_kommunkontor karta_kommunkontor_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_kommunkontor
    ADD CONSTRAINT karta_kommunkontor_pkey PRIMARY KEY (id);


--
-- Name: karta_konsumentradgivning karta_konsumentradgivning_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_konsumentradgivning
    ADD CONSTRAINT karta_konsumentradgivning_pkey PRIMARY KEY (id);


--
-- Name: karta_kulturforening karta_kulturforening_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_kulturforening
    ADD CONSTRAINT karta_kulturforening_pkey PRIMARY KEY (id);


--
-- Name: karta_kulturskolan karta_kulturskolan_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_kulturskolan
    ADD CONSTRAINT karta_kulturskolan_pkey PRIMARY KEY (id);


--
-- Name: karta_laddstationer karta_laddstationer_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_laddstationer
    ADD CONSTRAINT karta_laddstationer_pkey PRIMARY KEY (id);


--
-- Name: karta_langdskidspar karta_langdskidspar_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_langdskidspar
    ADD CONSTRAINT karta_langdskidspar_pkey PRIMARY KEY (id);


--
-- Name: karta_latrinkarl karta_latrinkarl_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_latrinkarl
    ADD CONSTRAINT karta_latrinkarl_pkey PRIMARY KEY (id);


--
-- Name: karta_ledig-industrimark karta_ledig-industrimark_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_ledig-industrimark"
    ADD CONSTRAINT "karta_ledig-industrimark_pkey" PRIMARY KEY (id);


--
-- Name: karta_ledig-industrimark_privatagd karta_ledig-industrimark_privatagd_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_ledig-industrimark_privatagd"
    ADD CONSTRAINT "karta_ledig-industrimark_privatagd_pkey" PRIMARY KEY (id);


--
-- Name: karta_lediga-bostadstomter karta_lediga-bostadstomter_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_lediga-bostadstomter"
    ADD CONSTRAINT "karta_lediga-bostadstomter_pkey" PRIMARY KEY (id);


--
-- Name: karta_lekplats karta_lekplats_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_lekplats
    ADD CONSTRAINT karta_lekplats_pkey PRIMARY KEY (id);


--
-- Name: karta_livsmedelsbutik karta_livsmedelsbutik_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_livsmedelsbutik
    ADD CONSTRAINT karta_livsmedelsbutik_pkey PRIMARY KEY (id);


--
-- Name: karta_motionsanl karta_motionsanl_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_motionsanl
    ADD CONSTRAINT karta_motionsanl_pkey PRIMARY KEY (id);


--
-- Name: karta_motionsspar_elljusspar karta_motionsspar_elljusspar_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_motionsspar_elljusspar
    ADD CONSTRAINT karta_motionsspar_elljusspar_pkey PRIMARY KEY (id);


--
-- Name: karta_motionsspar_uppmarkta karta_motionsspar_uppmarkta_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_motionsspar_uppmarkta
    ADD CONSTRAINT karta_motionsspar_uppmarkta_pkey PRIMARY KEY (id);


--
-- Name: karta_motorcrossbana karta_motorcrossbana_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_motorcrossbana
    ADD CONSTRAINT karta_motorcrossbana_pkey PRIMARY KEY (id);


--
-- Name: karta_naturreservat karta_naturreservat_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_naturreservat
    ADD CONSTRAINT karta_naturreservat_pkey PRIMARY KEY (id);


--
-- Name: karta_naturvardsprogram-objekt karta_naturvardsprogram-objekt_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_naturvardsprogram-objekt"
    ADD CONSTRAINT "karta_naturvardsprogram-objekt_pkey" PRIMARY KEY (id);


--
-- Name: karta_naturvardsprogram-omraden karta_naturvardsprogram-omraden_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_naturvardsprogram-omraden"
    ADD CONSTRAINT "karta_naturvardsprogram-omraden_pkey" PRIMARY KEY (id);


--
-- Name: karta_obo karta_obo_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_obo
    ADD CONSTRAINT karta_obo_pkey PRIMARY KEY (id);


--
-- Name: karta_offentligatoaletter karta_offentligatoaletter_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_offentligatoaletter
    ADD CONSTRAINT karta_offentligatoaletter_pkey PRIMARY KEY (id);


--
-- Name: karta_okad-sysselsattning karta_okad-sysselsattning_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_okad-sysselsattning"
    ADD CONSTRAINT "karta_okad-sysselsattning_pkey" PRIMARY KEY (id);


--
-- Name: karta_park karta_park_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_park
    ADD CONSTRAINT karta_park_pkey PRIMARY KEY (id);


--
-- Name: karta_parkering-pendlar karta_parkering-pendlar_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_parkering-pendlar"
    ADD CONSTRAINT "karta_parkering-pendlar_pkey" PRIMARY KEY (id);


--
-- Name: karta_parkering_rorelsehindrade karta_parkering_rorelsehindrade_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_parkering_rorelsehindrade
    ADD CONSTRAINT karta_parkering_rorelsehindrade_pkey PRIMARY KEY (id);


--
-- Name: karta_parkeringsplatser karta_parkeringsplatser_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_parkeringsplatser
    ADD CONSTRAINT karta_parkeringsplatser_pkey PRIMARY KEY (id);


--
-- Name: karta_polis karta_polis_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_polis
    ADD CONSTRAINT karta_polis_pkey PRIMARY KEY (id);


--
-- Name: karta_postombud karta_postombud_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_postombud
    ADD CONSTRAINT karta_postombud_pkey PRIMARY KEY (id);


--
-- Name: karta_racketanl karta_racketanl_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_racketanl
    ADD CONSTRAINT karta_racketanl_pkey PRIMARY KEY (id);


--
-- Name: karta_raddningstjanst karta_raddningstjanst_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_raddningstjanst
    ADD CONSTRAINT karta_raddningstjanst_pkey PRIMARY KEY (id);


--
-- Name: karta_restauranger karta_restauranger_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_restauranger
    ADD CONSTRAINT karta_restauranger_pkey PRIMARY KEY (id);


--
-- Name: karta_ridanl karta_ridanl_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_ridanl
    ADD CONSTRAINT karta_ridanl_pkey PRIMARY KEY (id);


--
-- Name: karta_samlingslokal karta_samlingslokal_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_samlingslokal
    ADD CONSTRAINT karta_samlingslokal_pkey PRIMARY KEY (id);


--
-- Name: karta_sevardheter_besoksmal karta_sevardheter_besoksmal_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_sevardheter_besoksmal
    ADD CONSTRAINT karta_sevardheter_besoksmal_pkey PRIMARY KEY (id);


--
-- Name: karta_sevardheter_kultur karta_sevardheter_kultur_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_sevardheter_kultur
    ADD CONSTRAINT karta_sevardheter_kultur_pkey PRIMARY KEY (id);


--
-- Name: karta_sevardheter_natur karta_sevardheter_natur_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_sevardheter_natur
    ADD CONSTRAINT karta_sevardheter_natur_pkey PRIMARY KEY (id);


--
-- Name: karta_skatepark karta_skatepark_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_skatepark
    ADD CONSTRAINT karta_skatepark_pkey PRIMARY KEY (id);


--
-- Name: karta_skjutbana karta_skjutbana_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_skjutbana
    ADD CONSTRAINT karta_skjutbana_pkey PRIMARY KEY (id);


--
-- Name: karta_skola karta_skola_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_skola
    ADD CONSTRAINT karta_skola_pkey PRIMARY KEY (id);


--
-- Name: karta_socialkontoret karta_socialkontoret_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_socialkontoret
    ADD CONSTRAINT karta_socialkontoret_pkey PRIMARY KEY (id);


--
-- Name: karta_sophamtning karta_sophamtning_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_sophamtning
    ADD CONSTRAINT karta_sophamtning_pkey PRIMARY KEY (id);


--
-- Name: karta_spontanidrottsplats karta_spontanidrottsplats_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_spontanidrottsplats
    ADD CONSTRAINT karta_spontanidrottsplats_pkey PRIMARY KEY (id);


--
-- Name: karta_station karta_station_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_station
    ADD CONSTRAINT karta_station_pkey PRIMARY KEY (id);


--
-- Name: karta_systembolaget karta_systembolaget_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_systembolaget
    ADD CONSTRAINT karta_systembolaget_pkey PRIMARY KEY (id);


--
-- Name: karta_torghandel karta_torghandel_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_torghandel
    ADD CONSTRAINT karta_torghandel_pkey PRIMARY KEY (id);


--
-- Name: karta_traffpunkt-aktivitetshuset karta_traffpunkt-aktivitetshuset_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_traffpunkt-aktivitetshuset"
    ADD CONSTRAINT "karta_traffpunkt-aktivitetshuset_pkey" PRIMARY KEY (id);


--
-- Name: karta_traffpunkt-aldre karta_traffpunkt-aldre_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_traffpunkt-aldre"
    ADD CONSTRAINT "karta_traffpunkt-aldre_pkey" PRIMARY KEY (id);


--
-- Name: karta_traffpunkt-funktionsnedsatta karta_traffpunkt-funktionsnedsatta_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_traffpunkt-funktionsnedsatta"
    ADD CONSTRAINT "karta_traffpunkt-funktionsnedsatta_pkey" PRIMARY KEY (id);


--
-- Name: karta_turistinformation karta_turistinformation_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_turistinformation
    ADD CONSTRAINT karta_turistinformation_pkey PRIMARY KEY (id);


--
-- Name: karta_uppstallningsplats-husbilar karta_uppstallningsplats-husbilar_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_uppstallningsplats-husbilar"
    ADD CONSTRAINT "karta_uppstallningsplats-husbilar_pkey" PRIMARY KEY (id);


--
-- Name: karta_utomhusgym karta_utomhusgym_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_utomhusgym
    ADD CONSTRAINT karta_utomhusgym_pkey PRIMARY KEY (id);


--
-- Name: karta_vandringsleder karta_vandringsleder_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_vandringsleder
    ADD CONSTRAINT karta_vandringsleder_pkey PRIMARY KEY (id);


--
-- Name: karta_vardcentral karta_vardcentral_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext.karta_vardcentral
    ADD CONSTRAINT karta_vardcentral_pkey PRIMARY KEY (id);


--
-- Name: karta_viktigt-meddelande karta_viktigt-meddelande_pkey; Type: CONSTRAINT; Schema: tranasext; Owner: xplore-admin
--

ALTER TABLE ONLY tranasext."karta_viktigt-meddelande"
    ADD CONSTRAINT "karta_viktigt-meddelande_pkey" PRIMARY KEY (id);


--
-- Name: geometry_columns geometry_columns_delete; Type: RULE; Schema: public; Owner: postgres
--

CREATE RULE geometry_columns_delete AS
    ON DELETE TO public.geometry_columns DO INSTEAD NOTHING;


--
-- Name: geometry_columns geometry_columns_insert; Type: RULE; Schema: public; Owner: postgres
--

CREATE RULE geometry_columns_insert AS
    ON INSERT TO public.geometry_columns DO INSTEAD NOTHING;


--
-- Name: geometry_columns geometry_columns_update; Type: RULE; Schema: public; Owner: postgres
--

CREATE RULE geometry_columns_update AS
    ON UPDATE TO public.geometry_columns DO INSTEAD NOTHING;


--
-- Name: SCHEMA anebyext; Type: ACL; Schema: -; Owner: geovis
--

GRANT USAGE ON SCHEMA anebyext TO "aneby-read";
GRANT ALL ON SCHEMA anebyext TO "xplore-admin";


--
-- Name: SCHEMA tranasext; Type: ACL; Schema: -; Owner: xplore-admin
--

GRANT USAGE ON SCHEMA tranasext TO tranas_read;
GRANT ALL ON SCHEMA tranasext TO geovis;


--
-- Name: TABLE geography_columns; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.geography_columns TO PUBLIC;


--
-- Name: TABLE geometry_columns; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.geometry_columns TO PUBLIC;


--
-- Name: TABLE spatial_ref_sys; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE public.spatial_ref_sys TO PUBLIC;


--
-- Name: SEQUENCE batplatser_aneby_id_seq; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.batplatser_aneby_id_seq TO geovis;
GRANT SELECT ON SEQUENCE tranasext.batplatser_aneby_id_seq TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq33; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq33 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq33 TO tranas_read;


--
-- Name: TABLE karta_apotek; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_apotek TO geovis;
GRANT SELECT ON TABLE tranasext.karta_apotek TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq27; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq27 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq27 TO tranas_read;


--
-- Name: TABLE karta_atervinningscentral; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_atervinningscentral TO geovis;
GRANT SELECT ON TABLE tranasext.karta_atervinningscentral TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq28; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq28 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq28 TO tranas_read;


--
-- Name: TABLE karta_atervinningsstation; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_atervinningsstation TO geovis;
GRANT SELECT ON TABLE tranasext.karta_atervinningsstation TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq20; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq20 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq20 TO tranas_read;


--
-- Name: TABLE karta_badplats; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_badplats TO geovis;
GRANT SELECT ON TABLE tranasext.karta_badplats TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq41; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq41 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq41 TO tranas_read;


--
-- Name: TABLE karta_bankomat; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_bankomat TO geovis;
GRANT SELECT ON TABLE tranasext.karta_bankomat TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq56; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq56 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq56 TO tranas_read;


--
-- Name: TABLE karta_basketplan; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_basketplan TO geovis;
GRANT SELECT ON TABLE tranasext.karta_basketplan TO tranas_read;


--
-- Name: TABLE karta_batplatser; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_batplatser TO geovis;
GRANT SELECT ON TABLE tranasext.karta_batplatser TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq57; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq57 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq57 TO tranas_read;


--
-- Name: TABLE karta_beachvolley; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_beachvolley TO geovis;
GRANT SELECT ON TABLE tranasext.karta_beachvolley TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq58; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq58 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq58 TO tranas_read;


--
-- Name: TABLE karta_begravningsplats; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_begravningsplats TO geovis;
GRANT SELECT ON TABLE tranasext.karta_begravningsplats TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_linje_id_seq; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_linje_id_seq TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_linje_id_seq TO tranas_read;


--
-- Name: TABLE karta_belaggningsarbete; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_belaggningsarbete TO geovis;
GRANT SELECT ON TABLE tranasext.karta_belaggningsarbete TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq13; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq13 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq13 TO tranas_read;


--
-- Name: TABLE karta_bibliotek; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_bibliotek TO geovis;
GRANT SELECT ON TABLE tranasext.karta_bibliotek TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq14; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq14 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq14 TO tranas_read;


--
-- Name: TABLE karta_biograf; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_biograf TO geovis;
GRANT SELECT ON TABLE tranasext.karta_biograf TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq45; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq45 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq45 TO tranas_read;


--
-- Name: TABLE karta_boende; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_boende TO geovis;
GRANT SELECT ON TABLE tranasext.karta_boende TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq8; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq8 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq8 TO tranas_read;


--
-- Name: TABLE karta_bostadstomter_ejbebyggda; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_bostadstomter_ejbebyggda TO geovis;
GRANT SELECT ON TABLE tranasext.karta_bostadstomter_ejbebyggda TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq59; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq59 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq59 TO tranas_read;


--
-- Name: TABLE karta_boulebanor; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_boulebanor TO geovis;
GRANT SELECT ON TABLE tranasext.karta_boulebanor TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq67; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq67 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq67 TO tranas_read;


--
-- Name: TABLE karta_bowling; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_bowling TO geovis;
GRANT SELECT ON TABLE tranasext.karta_bowling TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq53; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq53 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq53 TO tranas_read;


--
-- Name: TABLE karta_busshallplats; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_busshallplats TO geovis;
GRANT SELECT ON TABLE tranasext.karta_busshallplats TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq60; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq60 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq60 TO tranas_read;


--
-- Name: TABLE karta_busskort; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_busskort TO geovis;
GRANT SELECT ON TABLE tranasext.karta_busskort TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq31; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq31 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq31 TO tranas_read;


--
-- Name: TABLE karta_busstation; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_busstation TO geovis;
GRANT SELECT ON TABLE tranasext.karta_busstation TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq70; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq70 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq70 TO tranas_read;


--
-- Name: TABLE karta_bygdegardar; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_bygdegardar TO geovis;
GRANT SELECT ON TABLE tranasext.karta_bygdegardar TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq50; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq50 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq50 TO tranas_read;


--
-- Name: TABLE karta_cafe; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_cafe TO geovis;
GRANT SELECT ON TABLE tranasext.karta_cafe TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_linje_id_seq2; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_linje_id_seq2 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_linje_id_seq2 TO tranas_read;


--
-- Name: TABLE karta_cykelleder; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_cykelleder TO geovis;
GRANT SELECT ON TABLE tranasext.karta_cykelleder TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_linje_id_seq3; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_linje_id_seq3 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_linje_id_seq3 TO tranas_read;


--
-- Name: TABLE karta_cykelvagar; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_cykelvagar TO geovis;
GRANT SELECT ON TABLE tranasext.karta_cykelvagar TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq9; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq9 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq9 TO tranas_read;


--
-- Name: TABLE "karta_daglig-verksamhet"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_daglig-verksamhet" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_daglig-verksamhet" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq TO tranas_read;


--
-- Name: TABLE "karta_detaljplaner-gallande"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_detaljplaner-gallande" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_detaljplaner-gallande" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq1; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq1 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq1 TO tranas_read;


--
-- Name: TABLE "karta_detaljplaner-pagaende"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_detaljplaner-pagaende" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_detaljplaner-pagaende" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq52; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq52 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq52 TO tranas_read;


--
-- Name: TABLE karta_drivmedelsstation; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_drivmedelsstation TO geovis;
GRANT SELECT ON TABLE tranasext.karta_drivmedelsstation TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq54; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq54 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq54 TO tranas_read;


--
-- Name: TABLE karta_fiske; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_fiske TO geovis;
GRANT SELECT ON TABLE tranasext.karta_fiske TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq55; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq55 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq55 TO tranas_read;


--
-- Name: TABLE karta_fiskekort; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_fiskekort TO geovis;
GRANT SELECT ON TABLE tranasext.karta_fiskekort TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq15; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq15 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq15 TO tranas_read;


--
-- Name: TABLE "karta_folkets-park"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_folkets-park" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_folkets-park" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq36; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq36 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq36 TO tranas_read;


--
-- Name: TABLE karta_folktandvard; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_folktandvard TO geovis;
GRANT SELECT ON TABLE tranasext.karta_folktandvard TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq1; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq1 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq1 TO tranas_read;


--
-- Name: TABLE karta_forskola; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_forskola TO geovis;
GRANT SELECT ON TABLE tranasext.karta_forskola TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq17; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq17 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq17 TO tranas_read;


--
-- Name: TABLE karta_fotbollsplaner; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_fotbollsplaner TO geovis;
GRANT SELECT ON TABLE tranasext.karta_fotbollsplaner TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq18; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq18 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq18 TO tranas_read;


--
-- Name: TABLE karta_friidrottsanl; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_friidrottsanl TO geovis;
GRANT SELECT ON TABLE tranasext.karta_friidrottsanl TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq21; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq21 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq21 TO tranas_read;


--
-- Name: TABLE karta_fritidsgard; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_fritidsgard TO geovis;
GRANT SELECT ON TABLE tranasext.karta_fritidsgard TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq40; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq40 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq40 TO tranas_read;


--
-- Name: TABLE karta_fritidshem; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_fritidshem TO geovis;
GRANT SELECT ON TABLE tranasext.karta_fritidshem TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq16; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq16 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq16 TO tranas_read;


--
-- Name: TABLE karta_golfbanor; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_golfbanor TO geovis;
GRANT SELECT ON TABLE tranasext.karta_golfbanor TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq2; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq2 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq2 TO tranas_read;


--
-- Name: TABLE "karta_grav-uppstallningstillstand"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_grav-uppstallningstillstand" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_grav-uppstallningstillstand" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq74; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq74 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq74 TO tranas_read;


--
-- Name: TABLE karta_gym; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_gym TO geovis;
GRANT SELECT ON TABLE tranasext.karta_gym TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq39; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq39 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq39 TO tranas_read;


--
-- Name: TABLE karta_hamn; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_hamn TO geovis;
GRANT SELECT ON TABLE tranasext.karta_hamn TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq71; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq71 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq71 TO tranas_read;


--
-- Name: TABLE karta_hembygdsforening; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_hembygdsforening TO geovis;
GRANT SELECT ON TABLE tranasext.karta_hembygdsforening TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq43; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq43 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq43 TO tranas_read;


--
-- Name: TABLE karta_hjartstartare; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_hjartstartare TO geovis;
GRANT SELECT ON TABLE tranasext.karta_hjartstartare TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq73; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq73 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq73 TO tranas_read;


--
-- Name: TABLE karta_hundklubb; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_hundklubb TO geovis;
GRANT SELECT ON TABLE tranasext.karta_hundklubb TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq76; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq76 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq76 TO tranas_read;


--
-- Name: TABLE karta_hundpasar; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_hundpasar TO geovis;
GRANT SELECT ON TABLE tranasext.karta_hundpasar TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq19; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq19 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq19 TO tranas_read;


--
-- Name: TABLE karta_idrottshall; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_idrottshall TO geovis;
GRANT SELECT ON TABLE tranasext.karta_idrottshall TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq2; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq2 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq2 TO tranas_read;


--
-- Name: TABLE karta_isbanor; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_isbanor TO geovis;
GRANT SELECT ON TABLE tranasext.karta_isbanor TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq82; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq82 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq82 TO tranas_read;


--
-- Name: TABLE karta_kolonilotter; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_kolonilotter TO geovis;
GRANT SELECT ON TABLE tranasext.karta_kolonilotter TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq7; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq7 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq7 TO tranas_read;


--
-- Name: TABLE "karta_kommunagd-mark"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_kommunagd-mark" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_kommunagd-mark" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq7; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq7 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq7 TO tranas_read;


--
-- Name: TABLE karta_kommunkontor; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_kommunkontor TO geovis;
GRANT SELECT ON TABLE tranasext.karta_kommunkontor TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq4; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq4 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq4 TO tranas_read;


--
-- Name: TABLE karta_konsumentradgivning; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_konsumentradgivning TO geovis;
GRANT SELECT ON TABLE tranasext.karta_konsumentradgivning TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq69; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq69 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq69 TO tranas_read;


--
-- Name: TABLE karta_kulturforening; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_kulturforening TO geovis;
GRANT SELECT ON TABLE tranasext.karta_kulturforening TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq78; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq78 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq78 TO tranas_read;


--
-- Name: TABLE karta_kulturskolan; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_kulturskolan TO geovis;
GRANT SELECT ON TABLE tranasext.karta_kulturskolan TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq34; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq34 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq34 TO tranas_read;


--
-- Name: TABLE karta_laddstationer; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_laddstationer TO geovis;
GRANT SELECT ON TABLE tranasext.karta_laddstationer TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq61; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq61 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq61 TO tranas_read;


--
-- Name: TABLE karta_langdskidspar; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_langdskidspar TO geovis;
GRANT SELECT ON TABLE tranasext.karta_langdskidspar TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq79; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq79 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq79 TO tranas_read;


--
-- Name: TABLE karta_latrinkarl; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_latrinkarl TO geovis;
GRANT SELECT ON TABLE tranasext.karta_latrinkarl TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq6; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq6 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq6 TO tranas_read;


--
-- Name: TABLE "karta_ledig-industrimark"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_ledig-industrimark" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_ledig-industrimark" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq9; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq9 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq9 TO tranas_read;


--
-- Name: TABLE "karta_ledig-industrimark_privatagd"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_ledig-industrimark_privatagd" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_ledig-industrimark_privatagd" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq75; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq75 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq75 TO tranas_read;


--
-- Name: TABLE "karta_lediga-bostadstomter"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_lediga-bostadstomter" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_lediga-bostadstomter" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq35; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq35 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq35 TO tranas_read;


--
-- Name: TABLE karta_lekplats; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_lekplats TO geovis;
GRANT SELECT ON TABLE tranasext.karta_lekplats TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq51; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq51 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq51 TO tranas_read;


--
-- Name: TABLE karta_livsmedelsbutik; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_livsmedelsbutik TO geovis;
GRANT SELECT ON TABLE tranasext.karta_livsmedelsbutik TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq22; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq22 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq22 TO tranas_read;


--
-- Name: TABLE karta_motionsanl; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_motionsanl TO geovis;
GRANT SELECT ON TABLE tranasext.karta_motionsanl TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_linje_id_seq4; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_linje_id_seq4 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_linje_id_seq4 TO tranas_read;


--
-- Name: TABLE karta_motionsspar_elljusspar; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_motionsspar_elljusspar TO geovis;
GRANT SELECT ON TABLE tranasext.karta_motionsspar_elljusspar TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_linje_id_seq5; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_linje_id_seq5 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_linje_id_seq5 TO tranas_read;


--
-- Name: TABLE karta_motionsspar_uppmarkta; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_motionsspar_uppmarkta TO geovis;
GRANT SELECT ON TABLE tranasext.karta_motionsspar_uppmarkta TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq25; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq25 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq25 TO tranas_read;


--
-- Name: TABLE karta_motorcrossbana; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_motorcrossbana TO geovis;
GRANT SELECT ON TABLE tranasext.karta_motorcrossbana TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq12; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq12 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq12 TO tranas_read;


--
-- Name: TABLE karta_naturreservat; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_naturreservat TO geovis;
GRANT SELECT ON TABLE tranasext.karta_naturreservat TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq3; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq3 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq3 TO tranas_read;


--
-- Name: TABLE "karta_naturvardsprogram-objekt"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_naturvardsprogram-objekt" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_naturvardsprogram-objekt" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq10; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq10 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq10 TO tranas_read;


--
-- Name: TABLE "karta_naturvardsprogram-omraden"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_naturvardsprogram-omraden" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_naturvardsprogram-omraden" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq38; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq38 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq38 TO tranas_read;


--
-- Name: TABLE karta_obo; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_obo TO geovis;
GRANT SELECT ON TABLE tranasext.karta_obo TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq68; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq68 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq68 TO tranas_read;


--
-- Name: TABLE karta_offentligatoaletter; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_offentligatoaletter TO geovis;
GRANT SELECT ON TABLE tranasext.karta_offentligatoaletter TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq10; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq10 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq10 TO tranas_read;


--
-- Name: TABLE "karta_okad-sysselsattning"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_okad-sysselsattning" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_okad-sysselsattning" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq81; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq81 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq81 TO tranas_read;


--
-- Name: TABLE karta_park; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_park TO geovis;
GRANT SELECT ON TABLE tranasext.karta_park TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq29; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq29 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq29 TO tranas_read;


--
-- Name: TABLE "karta_parkering-pendlar"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_parkering-pendlar" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_parkering-pendlar" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq62; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq62 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq62 TO tranas_read;


--
-- Name: TABLE karta_parkering_rorelsehindrade; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_parkering_rorelsehindrade TO geovis;
GRANT SELECT ON TABLE tranasext.karta_parkering_rorelsehindrade TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq30; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq30 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq30 TO tranas_read;


--
-- Name: TABLE karta_parkeringsplatser; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_parkeringsplatser TO geovis;
GRANT SELECT ON TABLE tranasext.karta_parkeringsplatser TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq3; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq3 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq3 TO tranas_read;


--
-- Name: TABLE karta_polis; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_polis TO geovis;
GRANT SELECT ON TABLE tranasext.karta_polis TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq63; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq63 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq63 TO tranas_read;


--
-- Name: TABLE karta_postombud; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_postombud TO geovis;
GRANT SELECT ON TABLE tranasext.karta_postombud TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq23; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq23 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq23 TO tranas_read;


--
-- Name: TABLE karta_racketanl; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_racketanl TO geovis;
GRANT SELECT ON TABLE tranasext.karta_racketanl TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq5; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq5 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq5 TO tranas_read;


--
-- Name: TABLE karta_raddningstjanst; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_raddningstjanst TO geovis;
GRANT SELECT ON TABLE tranasext.karta_raddningstjanst TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq49; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq49 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq49 TO tranas_read;


--
-- Name: TABLE karta_restauranger; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_restauranger TO geovis;
GRANT SELECT ON TABLE tranasext.karta_restauranger TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq26; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq26 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq26 TO tranas_read;


--
-- Name: TABLE karta_ridanl; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_ridanl TO geovis;
GRANT SELECT ON TABLE tranasext.karta_ridanl TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq77; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq77 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq77 TO tranas_read;


--
-- Name: TABLE karta_samlingslokal; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_samlingslokal TO geovis;
GRANT SELECT ON TABLE tranasext.karta_samlingslokal TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq48; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq48 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq48 TO tranas_read;


--
-- Name: TABLE karta_sevardheter_besoksmal; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_sevardheter_besoksmal TO geovis;
GRANT SELECT ON TABLE tranasext.karta_sevardheter_besoksmal TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq47; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq47 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq47 TO tranas_read;


--
-- Name: TABLE karta_sevardheter_kultur; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_sevardheter_kultur TO geovis;
GRANT SELECT ON TABLE tranasext.karta_sevardheter_kultur TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq46; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq46 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq46 TO tranas_read;


--
-- Name: TABLE karta_sevardheter_natur; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_sevardheter_natur TO geovis;
GRANT SELECT ON TABLE tranasext.karta_sevardheter_natur TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq24; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq24 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq24 TO tranas_read;


--
-- Name: TABLE karta_skatepark; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_skatepark TO geovis;
GRANT SELECT ON TABLE tranasext.karta_skatepark TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq64; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq64 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq64 TO tranas_read;


--
-- Name: TABLE karta_skjutbana; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_skjutbana TO geovis;
GRANT SELECT ON TABLE tranasext.karta_skjutbana TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq TO tranas_read;


--
-- Name: TABLE karta_skola; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_skola TO geovis;
GRANT SELECT ON TABLE tranasext.karta_skola TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq37; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq37 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq37 TO tranas_read;


--
-- Name: TABLE karta_socialkontoret; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_socialkontoret TO geovis;
GRANT SELECT ON TABLE tranasext.karta_socialkontoret TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq11; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq11 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq11 TO tranas_read;


--
-- Name: TABLE karta_sophamtning; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_sophamtning TO geovis;
GRANT SELECT ON TABLE tranasext.karta_sophamtning TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq72; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq72 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq72 TO tranas_read;


--
-- Name: TABLE karta_spontanidrottsplats; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_spontanidrottsplats TO geovis;
GRANT SELECT ON TABLE tranasext.karta_spontanidrottsplats TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq80; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq80 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq80 TO tranas_read;


--
-- Name: TABLE karta_station; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_station TO geovis;
GRANT SELECT ON TABLE tranasext.karta_station TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq65; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq65 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq65 TO tranas_read;


--
-- Name: TABLE karta_systembolaget; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_systembolaget TO geovis;
GRANT SELECT ON TABLE tranasext.karta_systembolaget TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_yta_id_seq4; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq4 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_yta_id_seq4 TO tranas_read;


--
-- Name: TABLE karta_torghandel; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_torghandel TO geovis;
GRANT SELECT ON TABLE tranasext.karta_torghandel TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq12; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq12 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq12 TO tranas_read;


--
-- Name: TABLE "karta_traffpunkt-aktivitetshuset"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_traffpunkt-aktivitetshuset" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_traffpunkt-aktivitetshuset" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq11; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq11 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq11 TO tranas_read;


--
-- Name: TABLE "karta_traffpunkt-aldre"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_traffpunkt-aldre" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_traffpunkt-aldre" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq42; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq42 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq42 TO tranas_read;


--
-- Name: TABLE "karta_traffpunkt-funktionsnedsatta"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_traffpunkt-funktionsnedsatta" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_traffpunkt-funktionsnedsatta" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq44; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq44 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq44 TO tranas_read;


--
-- Name: TABLE karta_turistinformation; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_turistinformation TO geovis;
GRANT SELECT ON TABLE tranasext.karta_turistinformation TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq32; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq32 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq32 TO tranas_read;


--
-- Name: TABLE "karta_uppstallningsplats-husbilar"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_uppstallningsplats-husbilar" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_uppstallningsplats-husbilar" TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq66; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq66 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq66 TO tranas_read;


--
-- Name: TABLE karta_utomhusgym; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_utomhusgym TO geovis;
GRANT SELECT ON TABLE tranasext.karta_utomhusgym TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_linje_id_seq1; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_linje_id_seq1 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_linje_id_seq1 TO tranas_read;


--
-- Name: TABLE karta_vandringsleder; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_vandringsleder TO geovis;
GRANT SELECT ON TABLE tranasext.karta_vandringsleder TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq6; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq6 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq6 TO tranas_read;


--
-- Name: TABLE karta_vardcentral; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext.karta_vardcentral TO geovis;
GRANT SELECT ON TABLE tranasext.karta_vardcentral TO tranas_read;


--
-- Name: SEQUENCE standard_kommunkarta_punkt_id_seq8; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT SELECT,USAGE ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq8 TO geovis;
GRANT SELECT ON SEQUENCE tranasext.standard_kommunkarta_punkt_id_seq8 TO tranas_read;


--
-- Name: TABLE "karta_viktigt-meddelande"; Type: ACL; Schema: tranasext; Owner: xplore-admin
--

GRANT ALL ON TABLE tranasext."karta_viktigt-meddelande" TO geovis;
GRANT SELECT ON TABLE tranasext."karta_viktigt-meddelande" TO tranas_read;


--
-- Name: DEFAULT PRIVILEGES FOR SEQUENCES; Type: DEFAULT ACL; Schema: tranasext; Owner: xplore-admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE "xplore-admin" IN SCHEMA tranasext REVOKE ALL ON SEQUENCES  FROM "xplore-admin";
ALTER DEFAULT PRIVILEGES FOR ROLE "xplore-admin" IN SCHEMA tranasext GRANT SELECT,USAGE ON SEQUENCES  TO geovis;
ALTER DEFAULT PRIVILEGES FOR ROLE "xplore-admin" IN SCHEMA tranasext GRANT SELECT ON SEQUENCES  TO tranas_read;


--
-- Name: DEFAULT PRIVILEGES FOR TABLES; Type: DEFAULT ACL; Schema: tranasext; Owner: xplore-admin
--

ALTER DEFAULT PRIVILEGES FOR ROLE "xplore-admin" IN SCHEMA tranasext REVOKE ALL ON TABLES  FROM "xplore-admin";
ALTER DEFAULT PRIVILEGES FOR ROLE "xplore-admin" IN SCHEMA tranasext GRANT ALL ON TABLES  TO geovis;
ALTER DEFAULT PRIVILEGES FOR ROLE "xplore-admin" IN SCHEMA tranasext GRANT SELECT ON TABLES  TO tranas_read;


--
-- PostgreSQL database dump complete
--

