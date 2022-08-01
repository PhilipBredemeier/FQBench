SELECT
 	s_name,
     s_address
FROM
     pgdb1_sf1_supplier,
     mdb_sf1_nation
WHERE
     s_suppkey IN (
		SELECT
			ps_suppkey
		FROM
			pgdb1_sf1_partsupp
		WHERE
			ps_partkey IN (
				SELECT
					p_partkey
				FROM
					pgdb1_sf1_part
				WHERE
					p_name LIKE 'forest%'
			)
			AND ps_availqty > (
				SELECT
					0.5 * SUM(l_quantity)
				FROM
					pgdb1_sf1_lineitem
				WHERE
					l_partkey = ps_partkey
					AND l_suppkey = ps_suppkey
					AND l_shipdate >= date '1994-01-01'
					AND l_shipdate < date '1995-01-01'
			)
     )
     AND s_nationkey = n_nationkey
     AND n_name = 'CANADA'
ORDER BY
     s_name