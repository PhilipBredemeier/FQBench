SELECT s_acctbal,
     s_name,
     n_name,
     p_partkey,
     p_mfgr,
     s_address,
     s_phone,
     s_comment
 FROM pgdb1_sf1_part, pgdb1_sf1_supplier, pgdb1_sf1_partsupp, mdb_sf1_nation, mdb_sf1_region
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
			pgdb1_sf1_partsupp,
			pgdb1_sf1_supplier,
			mdb_sf1_nation,
			mdb_sf1_region
		WHERE
			p_partkey = ps_partkey
			AND s_suppkey = ps_suppkey
			AND s_nationkey = n_nationkey
			AND n_regionkey = r_regionkey
			AND r_name = 'EUROPE'
     )
ORDER BY s_acctbal DESC, n_name, s_name, p_partkey