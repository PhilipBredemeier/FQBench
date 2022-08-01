SELECT 
     p_brand,
     p_type,
     p_size,
     COUNT(DISTINCT ps_suppkey) AS supplier_cnt
FROM
     mdb1.mdb.mdb_sf1_partsupp,
     mdb1.mdb.mdb_sf1_part
WHERE
     p_partkey = ps_partkey
     AND p_brand <> 'Brand#45'
     AND p_type NOT LIKE 'MEDIUM POLISHED%'
     AND p_size IN (49, 14, 23, 45, 19, 3, 36, 9)
     AND ps_suppkey NOT IN (
		SELECT
			s_suppkey
		FROM
			pg2.public.pgdb1_sf1_supplier
		WHERE
			s_comment LIKE '%Customer%Complaints%'
     )
GROUP BY
     p_brand,
     p_type,
     p_size
ORDER BY
     supplier_cnt DESC,
     p_brand,
     p_type,
     p_size