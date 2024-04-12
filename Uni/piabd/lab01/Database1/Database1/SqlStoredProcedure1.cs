//------------------------------------------------------------------------------
// <copyright file="CSSqlStoredProcedure.cs" company="Microsoft">
//     Copyright (c) Microsoft Corporation.  All rights reserved.
// </copyright>
//------------------------------------------------------------------------------
using System;
using System.Data;
using System.Data.SqlClient;
using System.Data.SqlTypes;
using Microsoft.SqlServer.Server;

public partial class StoredProcedures
{
    [Microsoft.SqlServer.Server.SqlProcedure]
    public static void SqlStoredProcedure1 ()
    {
        // Put your code here
        SqlConnection connection = new SqlConnection("context connection=true");
        connection.Open();
        var cmd = new SqlCommand("select max(ID) from Pieniadze", connection);
        var rdr = cmd.ExecuteReader();
        int i = Convert.ToInt32(rdr);
        rdr.Close();

        SqlCommand command = new SqlCommand();
        SqlParameter parameter1 = new SqlParameter("@ID", SqlDbType.Int);
        SqlParameter parameter2 = new SqlParameter("@ImieNazwisko", SqlDbType.VarChar);
        SqlParameter parameter3 = new SqlParameter("@Kwota", SqlDbType.Int);

        Random r = new Random();
        parameter1.Value = i + 1;
        parameter2.Value = "Ziutek Kowalski";
        parameter3.Value = r.Next(1000);

        command.Parameters.Add(parameter1);
        command.Parameters.Add(parameter2);
        command.Parameters.Add(parameter3);

        command.CommandText =
            "INSERT Pieniadze (ID, ImieNazwisko, Kwota)" +
            " VALUES(@ID, @ImieNazwisko, @Kwota)";


        command.Connection = connection;


        command.ExecuteNonQuery();

        connection.Close();
    }
}
