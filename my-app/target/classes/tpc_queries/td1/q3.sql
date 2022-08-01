SELECT FIRST 10
     l_orderkey,
     SUM(l_extendedprice * (1 - l_discount)) AS revenue,
     o_orderdate,
     o_shippriority
 FROM pg1.public.pgdb1_sf1_customer, pg1.public.pgdb1_sf1_orders, pg1.public.pgdb1_sf1_lineitem
WHERE c_mktsegment = 'BUILDING'
  AND c_custkey = o_custkey
  AND l_orderkey = o_orderkey
  AND o_orderdate < MDY(3, 15, 1995)
  AND l_shipdate > MDY(3, 15, 1995)
GROUP BY l_orderkey, o_orderdate, o_shippriority
ORDER BY revenue DESC,  o_orderdate