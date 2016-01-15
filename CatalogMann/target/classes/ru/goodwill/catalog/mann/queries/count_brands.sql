select count(*) from (
	select		
		(HEFR_MITKZ * 10000 + HEFR_MITNR) as ID,
		HEFR_MITBZ as NAME,
		HEFR_MITKZ as MITKZ,
		HEFR_MITNR as MITNR
	from (
		select
	        HEFR_MITNR,
	        HEFR_MITKZ,
	        HEFR_MITBZ        
	    from SDAETOKHEFR
	    group by HEFR_MITNR, HEFR_MITKZ, HEFR_MITBZ
	    ) as t1
    ) as t2