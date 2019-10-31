-- FUNCOES
CREATE OR REPLACE FUNCTION sem_acento(text)
RETURNS text
AS $$
SELECT translate($1,
'¹²³àáâãäåāăąÀÁÂÃÄÅĀĂĄÆćčç©ĆČÇĐÐèéêёëēĕėęěÈÉÊËЁĒĔĖĘĚ€ğĞıìíîïìĩīĭÌÍÎÏЇÌĨĪĬłŁńňñŃŇÑòóôõöōŏőøÒÓÔÕÖŌŎŐØŒř®ŘšşșßŠŞȘùúûüũūŭůÙÚÛÜŨŪŬŮýÿÝŸžżźŽŻŹ',
'123aaaaaaaaaAAAAAAAAAAccccCCCDDeeeeeeeeeeEEEEEEEEEEgGiiiiiiiiiIIIIIIIIIlLnnnNNNoooooooooOOOOOOOOOOrrRssssSSSuuuuuuuuUUUUUUUUyyYYzzzZZZ'
);
$$ IMMUTABLE STRICT LANGUAGE SQL;