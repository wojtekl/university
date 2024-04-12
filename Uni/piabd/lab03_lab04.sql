/*
CREATE procedure T1
  @total int
AS
  CREATE table #T (n int, s varchar(128))
  INSERT into #T select n,s from NUM
    WHERE n%100>0 and n<=@total
  DECLARE @res varchar(128)
  SELECT @res=max(s) from NUM
    WHERE n<=@total and
      NOT exists(select * from #T
      WHERE #T.n=NUM.n)
GO

DECLARE @t1 datetime, @n int
SET @t1=getdate()
SET @n=100
WHILE @n>0 begin
  EXEC T1 1000
  SET @n=@n-1 end
SELECT datediff(ms,@t1,getdate())
GO
*/

/*
CREATE procedure T2
  @total int
AS
  CREATE table #T (n int primary key, s varchar(128))
  INSERT into #T select n,s from NUM
    WHERE n%100>0 and n<=@total
  DECLARE @res varchar(128)
  SELECT @res=max(s) from NUM
    WHERE n<=@total and
      NOT exists(select * from #T
      WHERE #T.n=NUM.n)
GO

DECLARE @t2 datetime, @n int
SET @t2=getdate()
SET @n=100
WHILE @n>0 begin
  EXEC T2 1000
  SET @n=@n-1 end
SELECT datediff(ms,@t2,getdate())
GO
*/

/*
CREATE procedure T3
  @total int
AS
  CREATE table #T (n int, s varchar(128))
  INSERT into #T select n,s from NUM
    WHERE n%100>0 and n<=@total
  CREATE clustered index Tind on #T (n)
  DECLARE @res varchar(128)
  SELECT @res=max(s) from NUM
    WHERE n<=@total and
      NOT exists(select * from #T
      WHERE #T.n=NUM.n)
GO

DECLARE @t3 datetime, @n int
SET @t3=getdate()
SET @n=100 – (**)
WHILE @n>0 begin
  EXEC T3 1000 – (*)
  SET @n=@n-1 end
SELECT datediff(ms,@t3,getdate())
GO
*/

/*
CREATE procedure V1
  @total int
AS
  DECLARE @V table (n int, s varchar(128))
  INSERT into @V select n,s from NUM
    WHERE n%100>0 and n<=@total
  DECLARE @res varchar(128)
  SELECT @res=max(s) from NUM
    WHERE n<=@total and
      NOT exists(select * from @V V
      WHERE V.n=NUM.n)
GO

DECLARE @v1 datetime, @n int
SET @v1=getdate()
SET @n=100 – (**)
WHILE @n>0 begin
  EXEC V1 1000 – (*)
  SET @n=@n-1 end
SELECT datediff(ms,@v1,getdate())
GO
*/

/*
CREATE procedure V2
  @total int
AS
  DECLARE @V table (n int primary key, s varchar(128))
  INSERT into @V select n,s from NUM
    WHERE n%100>0 and n<=@total
  DECLARE @res varchar(128)
  SELECT @res=max(s) from NUM
    WHERE n<=@total and
      NOT exists(select * from @V V
      WHERE V.n=NUM.n)
GO

DECLARE @v2 datetime, @n int
SET @v2=getdate()
SET @n=100
WHILE @n>0 begin
  EXEC V2 1000
  SET @n=@n-1 end
SELECT datediff(ms,@v2,getdate())
GO
*/