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
using System.ComponentModel;
using PhoneApp1.DataModel;

namespace PhoneApp1.ViewModel
{
    public class KalkulatorViewModel : INotifyPropertyChanged
    {
        private Dane dane;
        public KalkulatorViewModel()
        {
            dane = new Dane { kurs1 = 1.0, przelicznik1=1, kwota1=0.0, kurs2=1.0, kwota2=0.0, przelicznik2=1 };
        }
        public event PropertyChangedEventHandler PropertyChanged;
        protected void OnPropertyChanged(PropertyChangedEventArgs args)
        {
            if (null != PropertyChanged)
            {
                PropertyChanged(this, args);
            }
        }
        public double ItemKwota1
        {
            get
            {
                return this.dane.kwota1;
            }
            set
            {
                if (value != this.dane.kwota1)
                {
                    this.dane.kwota1 = value;
                    this.dane.kwota2= Math.Round(value * (this.dane.kurs1 / (double)this.dane.przelicznik1)/(this.dane.kurs2 / (double)this.dane.przelicznik2), 2);
                    OnPropertyChanged(new PropertyChangedEventArgs("ItemKwota1"));
                    OnPropertyChanged(new PropertyChangedEventArgs("ItemKwota2"));
                }
            }
        }
        public double ItemKwota2
        {
            get
            {
                return this.dane.kwota2;
            }
            set
            {
                if (value != this.dane.kwota2)
                {
                    this.dane.kwota2 = value;
                    OnPropertyChanged(new PropertyChangedEventArgs("ItemKwota2"));
                }
            }
        }
        public double ItemKurs1
        {
            get
            {
                return this.dane.kurs1;
            }
            set
            {
                if (value != this.dane.kurs1)
                {
                    this.dane.kurs1 = value;
                    this.dane.kwota2 = Math.Round(this.dane.kwota1 * (this.dane.kurs1 / (double)this.dane.przelicznik1) / (this.dane.kurs2 / (double)this.dane.przelicznik2), 2);
                    OnPropertyChanged(new PropertyChangedEventArgs("ItemKwota2"));
                }
            }
        }
        public int ItemPrzelicznik1
        {
            get;
            set;
        }
        public double ItemKurs2
        {
            get
            {
                return this.dane.kurs2;
            }
            set
            {
                if (value != this.dane.kurs2)
                {
                    this.dane.kurs2 = value;
                    this.dane.kwota2 = Math.Round(this.dane.kwota1 * (this.dane.kurs1 / (double)this.dane.przelicznik1) / (this.dane.kurs2 / (double)this.dane.przelicznik2), 2);
                    OnPropertyChanged(new PropertyChangedEventArgs("ItemKwota2"));
                }
            }
        }
        public int ItemPrzelicznik2
        {
            get;
            set;
        }
    }
}
