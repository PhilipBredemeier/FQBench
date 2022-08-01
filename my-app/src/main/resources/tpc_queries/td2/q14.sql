SELECT
     100.00 * SUM(CASE
		WHEN p_type LIKE 'PROMO%'
			THEN l_extendedprice * (1 - l_discount)
		ELSE 0
     END) / SUM(l_extendedprice * (1 - l_discount)) AS promo_revenue
FROM
     pg2.public.pgdb1_sf1_lineitem,
     pg2.public.pgdb1_sf1_part
WHERE
     l_partkey = p_partkey
     AND l_shipdate >= MDY(9,1,1995)
     AND l_shipdate < MDY(9,1,1995) + 1 UNITS MONTH