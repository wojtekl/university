/*
alter sequence hotele_prestiz restart with -5
drop trigger zmniejsz_prestiz
drop trigger zwieksz_prestiz
drop sequence Hotele_Prestiz
drop table Hotele;
*/

/*
CREATE TABLE [dbo].[Hotele]
(
	[Id] INT NOT NULL PRIMARY KEY, 
    [Nazwa] VARCHAR(50) NOT NULL, 
    [Wojewodztwo] VARCHAR(50) NOT NULL, 
    [Miejscowosc] VARCHAR(50) NOT NULL, 
	[Prestiz] INT default -5
)

CREATE SEQUENCE [dbo].Hotele_Prestiz
    START WITH -5
    INCREMENT BY 1
    MINVALUE -5
    MAXVALUE 5
;

CREATE TRIGGER [Zwieksz_Prestiz]
	ON [dbo].[Hotele]
	FOR INSERT
	AS
	BEGIN
		SET NOCOUNT ON
		DECLARE @resetSQL nvarchar(255) = 'ALTER SEQUENCE Hotele_Prestiz RESTART WITH ' +
		(SELECT cast(max([dbo].[Hotele].Prestiz+1) as nvarchar(10)) FROM [dbo].[Hotele] 
		where nazwa = (select Nazwa from inserted));
		exec sp_executesql @resetSQL;
		alter sequence Hotele_Prestiz increment by 1
		declare @PRESTIZ int = next value for dbo.Hotele_Prestiz
		update Hotele set Prestiz=@PRESTIZ 
		where Nazwa=(select Nazwa from inserted);
	END

CREATE TRIGGER [Zmniejsz_Prestiz]
	ON [dbo].[Hotele]
	FOR DELETE
	AS
	BEGIN
		SET NOCOUNT ON
		DECLARE @resetSQL nvarchar(255) = 'ALTER SEQUENCE Hotele_Prestiz RESTART WITH ' +
		(SELECT cast(max([dbo].[Hotele].Prestiz+1) as nvarchar(10)) FROM [dbo].[Hotele] 
		where nazwa = (select Nazwa from inserted));
		exec sp_executesql @resetSQL;
		alter sequence Hotele_Prestiz increment by -1
		declare @PRESTIZ int = next value for dbo.Hotele_Prestiz
		update Hotele set Prestiz=@PRESTIZ 
		where Nazwa=(select Nazwa from deleted);
	END
*/
