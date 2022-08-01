SELECT FIRST 20
     c_custkey,
     c_name,
     SUM(l_extendedprice * (1 - l_discount)) AS revenue,
     c_acctbal,
     n_name,
     c_address,
     c_phone,
     c_comment
FROM
     mdb1.mdb.mdb_sf1_customer,
     pg1.public.pgdb1_sf1_orders,
     pg2.public.pgdb1_sf1_lineitem,
     mdb1.mdb.mdb_sf1_nation
WHERE
     c_custkey = o_custkey
     AND l_orderkey = o_orderkey
     AND o_orderdate >= MDY  (10,1,1993)
     AND o_orderdate < MDY(10,1,1993) + 3 UNITS MONTH
     AND l_returnflag = 'R'
     AND c_nationkey = n_nationkey
GROUP BY
     c_custkey,
     c_name,
     c_acctbal,
     c_phone,
     n_name,
     c_address,
     c_comment
ORDER BY
     revenue DESC