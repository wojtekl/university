using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace PhoneApp1
{
    public class Waluta
    {
        public String nazwa_waluty
        {
            get;
            set;
        }
        public String przelicznik
        {
            get;
            set;
        }
        public String kod_waluty
        {
            get;
            set;
        }
        public String kurs_sredni
        {
            get;
            set;
        }
        public Waluta(String nazwa, String przel, String kod, String kurs)
        {
            nazwa_waluty = nazwa;
            przelicznik = przel;
            kod_waluty = kod;
            kurs_sredni = kurs;
        }
    }
}
