select * from (
    select
        row_number() over() as rownum,

	    MFBA_PRDSL as FILTERID,

	    (
        (MFBA_FZASL * 1000000 * 1000 * 10000) +
        (MFBA_MRKSL * 1000000 * 1000) +
        (MFBA_MDRSL * 1000000) +
        (MFBA_MODSL)
        ) as MOTORID

from SDAETOKMFBA
) as t1
where rownum > ? and rownum <= ?