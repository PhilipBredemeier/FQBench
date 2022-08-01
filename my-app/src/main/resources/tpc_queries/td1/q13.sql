SELECT
          c_count,
          COUNT(*) AS custdist
  FROM (
          SELECT
                  c_custkey,
                  COUNT(o_orderkey) AS c_count
          FROM
                  (SELECT * FROM pg1.public.pgdb1_sf1_customer
                  LEFT OUTER JOIN pg1.public.pgdb1_sf1_orders ON
                    c_custkey = o_custkey AND
                    o_comment NOT LIKE '%special%requests%') c_customer
          GROUP BY
                  c_custkey
          ) c_orders
  GROUP BY
          c_count
  ORDER BY
          custdist DESC,
          c_count DESC