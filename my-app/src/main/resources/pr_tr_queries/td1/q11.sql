SELECT ps_partkey,
     SUM(ps_supplycost * ps_availqty) AS value
FROM
     mdb1.mdb.mdb_sf1_partsupp,
     pg2.public.pgdb1_sf1_supplier,
     pg2.public.pgdb1_sf1_nation
WHERE
     ps_suppkey = s_suppkey
     AND s_nationkey = n_nationkey
     AND n_name = 'GERMANY'
GROUP BY
     ps_partkey HAVING
		SUM(ps_supplycost * ps_availqty) > (
			SELECT
				SUM(ps_supplycost * ps_availqty) * 0.0001
			FROM
				mdb1.mdb.mdb_sf1_partsupp,
				pg2.public.pgdb1_sf1_supplier,
				pg2.public.pgdb1_sf1_nation
			WHERE
				ps_suppkey = s_suppkey
				AND s_nationkey = n_nationkey
				AND n_name = 'GERMANY'
		)
ORDER BY
     value DESC
LIMIT 10