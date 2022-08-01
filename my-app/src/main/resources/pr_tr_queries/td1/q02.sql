SELECT s_acctbal,
     s_name,
     n_name,
     p_partkey,
     p_mfgr,
     s_address,
     s_phone,
     s_comment
 FROM mdb1.mdb.mdb_sf1_part, pg2.public.pgdb1_sf1_supplier, mdb1.mdb.mdb_sf1_partsupp, pg2.public.pgdb1_sf1_nation, pg2.public.pgdb1_sf1_region
WHERE
     p_partkey = ps_partkey
     AND s_suppkey = ps_suppkey
     AND p_size = 15
     AND p_type LIKE '%BRASS'
     AND s_nationkey = n_nationkey
     AND n_regionkey = r_regionkey
     AND r_name = 'EUROPE'
     AND ps_supplycost = (
		SELECT
			MIN(ps_supplycost)
		FROM
			mdb1.mdb.mdb_sf1_partsupp,
			pg2.public.pgdb1_sf1_supplier,
			pg2.public.pgdb1_sf1_nation,
			pg2.public.pgdb1_sf1_region
		WHERE
			p_partkey = ps_partkey
			AND s_suppkey = ps_suppkey
			AND s_nationkey = n_nationkey
			AND n_regionkey = r_regionkey
			AND r_name = 'EUROPE'
     )
ORDER BY s_acctbal DESC, n_name, s_name, p_partkey
limit 100