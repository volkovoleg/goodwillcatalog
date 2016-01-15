select * from (
    select
        row_number() over() as rownum,

        (HEFR_MITKZ * 10000 + HEFR_MITNR) as BRANDID,
        HEFR_MITPP as NAME,
        HEFR_MITPD as SEARCHNAME,

        HEFR_MITKZ as MITKZ,
        HEFR_MPDSL as MPDSL,
        HEFR_LFDNR as LFDNR
    from SDAETOKHEFR
) as t1
where rownum > ? and rownum <= ?
