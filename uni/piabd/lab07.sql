--SELECT fulltextserviceproperty('IsFulltextInstalled');
--EXEC sp_help_fulltext_system_components 'wordbreaker';
--drop table Ksiazki;
/*CREATE TABLE [dbo].[Ksiazki]
(
	[Id] INT NOT NULL IDENTITY (1, 1), 
    [Tytul] VARCHAR(MAX) NOT NULL, 
    [Autor] VARCHAR(MAX) NOT NULL, 
    [Opis] VARCHAR(MAX) NOT NULL, 
	CONSTRAINT PK_Ksiazki PRIMARY KEY (Id)
);*/
--CREATE FULLTEXT CATALOG [ksiazki_catalog] AS DEFAULT;
/*CREATE FULLTEXT INDEX ON [dbo].[Ksiazki]
(
    Opis LANGUAGE 1045
) KEY INDEX [PK_Ksiazki]*/
--exec sp_help_fulltext_catalogs
--exec sp_help_fulltext_tables
--exec sp_help_fulltext_columns
--insert into Ksiazki (Tytul, Autor, Opis) values ('Dziesięć kroków lepszego życia', 'Krzysztof Kanciarz', 'Porady biznesowe dla początkujących.');
--insert into Ksiazki (Tytul, Autor, Opis) values ('Twoja własna siła', 'Krzysztof Kanciarz', 'Matywacja w biznesie.');
--insert into Ksiazki (Tytul, Autor, Opis) values ('Przedsiębiorcy na start', 'Krzysztof Kanciarz', 'Porady biznesowe dla początkujących przedsiębiorców.');
--SELECT * FROM Ksiazki WHERE FREETEXT (Opis, 'porady biznes')
--SELECT * FROM Ksiazki WHERE CONTAINS (Opis, 'motywacja OR "przedsiębiorc*"')
--SELECT * FROM Ksiazki WHERE CONTAINS (Opis, 'FORMSOF(INFLECTIONAL, "biznes")')
--SELECT * FROM Ksiazki WHERE CONTAINS(Opis, 'ISABOUT (porady WEIGHT (.2), motywacja WEIGHT (.8) )')
/*SELECT Id, FREETEXTTABLE_ALIAS.RANK, Opis
FROM Ksiazki k INNER JOIN FREETEXTTABLE (Ksiazki, Opis, 'porady biznesowe') 
    AS FREETEXTTABLE_ALIAS ON k.Id = FREETEXTTABLE_ALIAS.[KEY]
ORDER BY 2 DESC*/
/*SELECT Id, CONTAINSTABLE_ALIAS.RANK, Opis
FROM Ksiazki k INNER JOIN CONTAINSTABLE (Ksiazki, Opis, 'motywacja OR "przedsiębiorc*"', 3) 
    AS CONTAINSTABLE_ALIAS ON k.Id = CONTAINSTABLE_ALIAS.[KEY]*/
/*SELECT Id, CONTAINSTABLE_ALIAS.RANK, Opis
FROM Ksiazki k INNER JOIN CONTAINSTABLE (Ksiazki, Opis, 'FORMSOF(INFLECTIONAL, "biznes")') 
    AS CONTAINSTABLE_ALIAS ON k.Id = CONTAINSTABLE_ALIAS.[KEY]*/