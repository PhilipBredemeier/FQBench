SELECT
     nation,
     o_year,
     SUM(amount) AS sum_profit
FROM
     (
		SELECT
			n_name AS nation,
			YEAR(o_orderdate) AS o_year,
			l_extendedprice * (1 - l_discount) - ps_supplycost * l_quantity AS amount
		FROM
			mdb_sf1_part,
			pgdb1_sf1_supplier,
			pgdb1_sf1_lineitem,
			mdb_sf1_partsupp,
			pgdb1_sf1_orders,
			pgdb1_sf1_nation
		WHERE
			s_suppkey = l_suppkey
			AND ps_suppkey = l_suppkey
			AND ps_partkey = l_partkey
			AND p_partkey = l_partkey
			AND o_orderkey = l_orderkey
			AND s_nationkey = n_nationkey
			AND p_name LIKE '%green%'
     ) AS profit
GROUP BY
     nation,
     o_year
ORDER BY
     nation,
     o_year DESC