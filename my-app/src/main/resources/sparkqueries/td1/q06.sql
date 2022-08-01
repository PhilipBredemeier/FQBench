SELECT
     SUM(l_extendedprice * l_discount) AS revenue
FROM
     pgdb1_sf1_lineitem
WHERE
     l_shipdate >= date '1994-01-01'
     AND l_shipdate < date '1995-01-01'
     AND l_discount BETWEEN .06 - 0.01 AND .06 + 0.010001
     AND l_quantity < 24