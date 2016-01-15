select * from (
  select
      row_number() over() as rownum,

      (
      (FZMO_FZASL * 1000000 * 1000 * 10000) +
      (FZMO_MRKSL * 1000000 * 1000) +
      (FZMO_MDRSL * 1000000)
      ) as ID,

      (
      (FZMO_FZASL * 1000000 * 1000 * 10000) +
      (FZMO_MRKSL * 1000000 * 1000)
      ) as MANUFACTORID,

      FZMO_MDRBZ as NAME,

      FZMO_FZASL as FZASL,
      FZMO_MRKSL as MRKSL,
      FZMO_MDRSL as MDRSL
  from SDAETOKFZMO
) as t1
where rownum > ? and rownum <= ?
