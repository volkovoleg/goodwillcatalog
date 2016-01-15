select * from (
  select
      row_number() over() as rownum,

      (
      (FZMA_FZASL * 1000000 * 1000 * 10000) +
      (FZMA_MRKSL * 1000000 * 1000)
      ) as ID,

      FZMA_MARKD as NAME,
      FZMA_FZASL as VECHICLETYPEID,

      FZMA_FZASL as FZASL,
      FZMA_MRKSL as MRKSL
  from SDAETOKFZMA
) as t1
where rownum > ? and rownum <= ?