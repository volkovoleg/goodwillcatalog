select * from (
    select
        row_number() over() as rownum,
        mhfi1.MHFI_PRDSL as ID,
        mhfi2.MHFI2_FIFOG as FILTERFORMID,
        mhfi2.MHFI2_PDART as FILTERTYPECODE,
        mhfi2.MHFI2_M1 as APARAM,
        mhfi2.MHFI2_M2 as BPARAM,
        mhfi2.MHFI2_M3 as CPARAM,
        mhfi2.MHFI2_M4 as DPARAM,
        mhfi2.MHFI2_M5 as EPARAM,
        mhfi2.MHFI2_M6 as FPARAM,
        mhfi2.MHFI2_M7 as GPARAM,
        mhfi2.MHFI2_M8 as HPARAM,
        mhfi2.MHFI2_M9 as NRPARAM,
        mhfi2.MHFI2_VENTI_NUM as PBPARAM,
        mhfi1.MHFI_EANNR as EAN,
        mhfi1.MHFI_PRDBD as NAME,
        mhfi1.MHFI_PRDBZ as SEARCHNAME
    from SDAETOKMHFI as mhfi1
        left outer join SDAETOKMHFI2 as mhfi2 ON mhfi1.MHFI_PRDSL = mhfi2.MHFI2_PRDSL
) as t1
where rownum > ? and rownum <= ?