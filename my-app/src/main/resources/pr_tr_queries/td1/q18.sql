select c_name,
	c_custkey,
	o_orderkey,
	o_orderdate,
	o_totalprice,
	sum(l_quantity)
from
	pg1.public.pgdb1_sf1_customer,
	pg1.public.pgdb1_sf1_orders,
	pg1.public.pgdb1_sf1_lineitem
where
	o_orderkey in (
	select
	l_orderkey
	from
	pg1.public.pgdb1_sf1_lineitem
	group by
	l_orderkey having
		sum(l_quantity) > 300
	)
	and c_custkey = o_custkey
	and o_orderkey = l_orderkey
group by
	c_name,
	c_custkey,
	o_orderkey,
	o_orderdate,
	o_totalprice
order by
	o_totalprice desc,
	o_orderdate
limit 100