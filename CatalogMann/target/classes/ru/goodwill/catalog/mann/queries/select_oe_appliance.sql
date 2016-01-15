select * from (
    select
        row_number() over() as rownum,

        MHFR_PRDSL as FILTERID,
        MHFR_MITPD as OE
    from SDAETOKMHFR
) as t1
where rownum > ? and rownum <= ?