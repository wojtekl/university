/*
CREATE TABLE [dbo].[Table]
(
	[Id] INT NOT NULL PRIMARY KEY, 
    [Nazwa] VARCHAR(50) NOT NULL, 
    [Wojewodztwo] VARCHAR(50) NOT NULL, 
    [Miejscowosc] VARCHAR(50) NOT NULL
)
*/

/*
select * from 
(select Wojewodztwo, Nazwa from Hotele) as p
pivot(
count(Nazwa)
for Wojewodztwo in ("Mazowieckie", "Warmińsko-Mazurskie")
) as piv
*/

/*
DECLARE @kolumny VARCHAR(MAX)
SET @kolumny=''
SELECT @kolumny = @kolumny + '' + Miejscowosc + ', ' FROM
(SELECT DISTINCT Miejscowosc FROM Hotele WHERE Miejscowosc IS NOT NULL) as unik
SET @kolumny= LEFT(@kolumny,LEN(@kolumny)-1)

declare @cmd varchar(max)
set @cmd ='select * from 
(select Miejscowosc, Nazwa from Hotele) as p
pivot(
count(Nazwa)
for Miejscowosc in ('+@kolumny+')) as piv'
exec (@cmd)
*/

/*
CREATE TABLE [dbo].[Wojewodztwa]
(
	[Id] INT NOT NULL PRIMARY KEY IDENTITY, 
    [Wojewodztwo] VARCHAR(50) NOT NULL, 
    [LiczbaHoteli] NUMERIC NOT NULL
)
*/

/*
CREATE TABLE [dbo].[Miejscowosci]
(
	[Id] INT NOT NULL PRIMARY KEY IDENTITY, 
    [Miejscowosc] VARCHAR(50) NOT NULL, 
    [LiczbaHoteli] NUMERIC NOT NULL
)
*/

/*
CREATE TRIGGER [Wyzwalacz]
	ON [dbo].[Miejscowosci]
	FOR DELETE, INSERT, UPDATE
	AS
	BEGIN
		SET NOCOUNT ON
		IF ((
		SELECT TRIGGER_NESTLEVEL()) > 1 )
		RETURN
		update wojewodztwa set LiczbaHoteli=(select LiczbaHoteli from Miejscowosci where Miejscowosc=(select Miejscowosc from inserted)) where Wojewodztwo=
		(select Miejscowosc from inserted);
	END
*/

/*
CREATE TRIGGER [WyzwalaczWojewodztwa]
	ON [dbo].[Wojewodztwa]
	FOR DELETE, INSERT, UPDATE
	AS
	BEGIN
		SET NOCOUNT ON
		IF ((
		SELECT TRIGGER_NESTLEVEL()) > 1 )
		RETURN
		update Miejscowosci set LiczbaHoteli=(select LiczbaHoteli from Wojewodztwa where Wojewodztwo=(select Wojewodztwo from inserted)) where Miejscowosc=
		(select Wojewodztwo from inserted);
	END
*/
