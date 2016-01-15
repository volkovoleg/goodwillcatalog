select * from (
  select
      row_number() over() as rownum,

      (
      (FZTY_FZASL * 1000000 * 1000 * 10000) +
      (FZTY_MRKSL * 1000000 * 1000) +
      (FZTY_MDRSL * 1000000) +
      (FZTY_MODSL)
      ) as ID,

      (
      (FZTY_FZASL * 1000000 * 1000 * 10000) +
      (FZTY_MRKSL * 1000000 * 1000) +
      (FZTY_MDRSL * 1000000)
      ) as SERIAID,

      FZTY_DATEV as DATEF,
      FZTY_DATEB as DATET,
      FZTY_MOTBZ as ENGINE,
      FZTY_FZPS as HP,
      FZTY_FZKW as KW,
      FZTY_MODBZ as NAME,

      FZTY_FZASL as FZASL,
      FZTY_MRKSL as MRKSL,
      FZTY_MDRSL as MDRSL,
      FZTY_MODSL as MODSL
  from SDAETOKFZTY
) as t1
where rownum > ? and rownum <= ?
