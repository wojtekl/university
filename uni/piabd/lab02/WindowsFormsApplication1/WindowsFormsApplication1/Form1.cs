using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Data.SqlTypes;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApplication1
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            OpenFileDialog openFileDlg = new OpenFileDialog();

            openFileDlg.InitialDirectory = Directory.GetCurrentDirectory();

            if (openFileDlg.ShowDialog() == DialogResult.OK)
            {

                FileInfo fileinfo = new FileInfo(openFileDlg.FileName);

                FileStream filestream = new FileStream(fileinfo.FullName, FileMode.Open, FileAccess.Read);

                BinaryReader readerbinary = new BinaryReader(filestream);

                byte[] fileData = readerbinary.ReadBytes((int)filestream.Length);

                readerbinary.Close();

                filestream.Close();

                string cs = @"Data Source=.;Initial Catalog=FileStreamDatabase;Integrated Security=True";

                using (SqlConnection con = new SqlConnection(cs))
                {

                    con.Open();

                    string sql = "INSERT INTO Pliczki VALUES (NEWID(), @name, @data)";

                    SqlCommand cmd = new SqlCommand(sql, con);

                    cmd.Parameters.Add("@data", SqlDbType.Image, fileData.Length).Value = fileData;
                    cmd.Parameters.Add("@name", SqlDbType.NVarChar).Value = fileinfo.Name;
                    cmd.ExecuteNonQuery();

                    con.Close();

                }

                MessageBox.Show(fileinfo.FullName, "Plik dodany", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }

        }

        private void button2_Click(object sender, EventArgs e)
        {
            string tresc;
            string cs = @"Data Source=.;Initial Catalog=FileStreamDatabase;Integrated Security=True";
            string nazwapliku = textBox1.Text;
            using (SqlConnection con = new SqlConnection(cs))
            {

                con.Open();

                SqlTransaction txn = con.BeginTransaction();

                string sql = "SELECT data.PathName(), GET_FILESTREAM_TRANSACTION_CONTEXT(), name FROM Pliczki where name=@nazwa";

                SqlCommand cmd = new SqlCommand(sql, con, txn);
                cmd.Parameters.Add("@nazwa", SqlDbType.VarChar).Value = nazwapliku;
                SqlDataReader rdr = cmd.ExecuteReader();
                rdr.Read();
                byte[] strim;

                tresc = rdr.GetSqlString(0).Value;
                strim = rdr.GetSqlBinary(1).Value;
                rdr.Close();
                

      using (SqlFileStream sfs=new SqlFileStream(tresc, strim, FileAccess.Read))
      {
          pictureBox1.Image=Image.FromStream(sfs);
          sfs.Close();
      }
                con.Close();
        
      }
                
            


        }
    }
}
