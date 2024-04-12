using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using Microsoft.Phone.Controls;
using System.Text.RegularExpressions;
using System.Xml.Linq;
using System.ComponentModel;
using System.Windows.Data;
using PhoneApp1.ViewModel;
using System.Text;

namespace PhoneApp1
{
    public partial class MainPage : PhoneApplicationPage
    {
        private int stan, przecinek=0;
        private List<Waluta> l;
        private double liczba1, liczba2, pamiec;
        private int operacja, kontynuacja, mc;
        private KalkulatorViewModel kalkulatorViewModel;
        // Constructor
        public MainPage()
        {
            WebClient webclient = new WebClient();
            webclient.DownloadStringCompleted += httpcompleted;
            webclient.DownloadStringAsync(new Uri("http://www.nbp.pl/kursy/xml/LastA.xml"));
            InitializeComponent();
            stan = 0;
            operacja = 0;
            kontynuacja = 0;
            przecinek = 0;
            mc = 0;
            kalkulatorViewModel = new KalkulatorViewModel();
            DataContext = kalkulatorViewModel;
        }

        private String obetnij(String wynik)
        {
            if (Regex.IsMatch(wynik, "\\A\\d+.\\d{3,}\\z"))
            {
                wynik = wynik.Substring(0, wynik.LastIndexOf(".") + 3);
            }
            return wynik;
        }
        private void httpcompleted(object sender, DownloadStringCompletedEventArgs e)
        {
            if (null == e.Error)
            {
                XDocument xdoc = XDocument.Parse(e.Result, LoadOptions.None);
                l = new List<Waluta>();
                foreach (XElement el in xdoc.Descendants("pozycja"))
                {
                    l.Add(new Waluta(
                        el.Element("nazwa_waluty").Value.ToString(),
                        el.Element("przelicznik").Value.ToString(),
                        el.Element("kod_waluty").Value.ToString(),
                        el.Element("kurs_sredni").Value.ToString())
                        );
                }
                l.Add(new Waluta(
                        "złoty polski",
                        "1",
                        "PLN",
                        "1,0"
                        ));
                var kursy = from waluta in l
                            group waluta by waluta.kod_waluty into c
                            orderby c.Key
                            select new Group<Waluta>(c.Key, c);
                longListSelector1.ItemsSource = kursy;
                longListSelector2.ItemsSource = kursy;
            }
        }

        private void l1SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            Waluta waluta1=(Waluta)longListSelector1.SelectedItem;
            kalkulatorViewModel.ItemPrzelicznik1 = Convert.ToInt32(waluta1.przelicznik, new System.Globalization.CultureInfo("pl-PL"));
            kalkulatorViewModel.ItemKurs1 = Convert.ToDouble(waluta1.kurs_sredni, new System.Globalization.CultureInfo("pl-PL"));
            textBlock1.Text = waluta1.kod_waluty;
            longListSelector1.Visibility=Visibility.Collapsed;
        }

        private void l2SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            Waluta waluta2 = (Waluta)longListSelector2.SelectedItem;
            kalkulatorViewModel.ItemPrzelicznik2 = Convert.ToInt32(waluta2.przelicznik, new System.Globalization.CultureInfo("pl-PL"));
            kalkulatorViewModel.ItemKurs2 = Convert.ToDouble(waluta2.kurs_sredni, new System.Globalization.CultureInfo("pl-PL"));
            textBlock2.Text = waluta2.kod_waluty;
            longListSelector2.Visibility = Visibility.Collapsed;
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            if (1 == kontynuacja)
            {
                operacja = 0;
                stan = 0;
                kontynuacja = 0;
            }
            if (0 == stan && 1 != przecinek)
            {
                textBoxWej.Text = "";
                stan = 1;
            }
            Button button = (Button)sender;
            textBoxWej.Text = obetnij(textBoxWej.Text + button.Content);
        }

        private void buttonC_Click(object sender, RoutedEventArgs e)
        {
            textBoxWej.Text = "0";
            stan = 0;
            przecinek = 0;
        }

        private void buttonP_Click(object sender, RoutedEventArgs e)
        {
            if (0 == przecinek)
            {
                Button button = (Button)sender;
                textBoxWej.Text += ".";
                przecinek = 1;
            }
        }

        private void buttonD_Click(object sender, RoutedEventArgs e)
        {
            liczba1 = Convert.ToDouble(textBoxWej.Text);
            String o=((Button)sender).Content.ToString();
            if (o.Equals("+"))
            {
                operacja = 1;
            }
            else if (o.Equals("-"))
            {
                operacja = 2;
            }
            else if (o.Equals("*"))
            {
                operacja = 3;
            }
            else if (o.Equals("/"))
            {
                operacja = 4;
            }
            stan = 0;
            kontynuacja = 0;
            przecinek = 0;
            liczba2 = 0.0;
            textBoxWej.Text = "0";
        }

        private void buttonR_Click(object sender, RoutedEventArgs e)
        {
            if (0 != operacja)
            {
                if (0 == kontynuacja)
                {
                    liczba2 = Convert.ToDouble(textBoxWej.Text);
                    kontynuacja = 1;
                }
                switch (operacja)
                {
                    case 1:
                        liczba1 = liczba1 + liczba2;
                        break;
                    case 2:
                        liczba1 = liczba1 - liczba2;
                        break;
                    case 3:
                        liczba1 = liczba1 * liczba2;
                        break;
                    case 4:
                        liczba1 = liczba1 / liczba2;
                        break;
                }
                textBoxWej.Text = liczba1.ToString();
            }
        }

        private void buttonMP_Click(object sender, RoutedEventArgs e)
        {
            pamiec += Convert.ToDouble(textBoxWej.Text);
            mc = 0;
        }

        private void buttonMM_Click(object sender, RoutedEventArgs e)
        {
            pamiec -= Convert.ToDouble(textBoxWej.Text);
            mc = 0;
        }

        private void buttonMC_Click(object sender, RoutedEventArgs e)
        {
            if (0 == mc)
            {
                textBoxWej.Text = pamiec.ToString();
                mc = 1;
            }
            else
            {
                pamiec = 0;
                mc = 0;
            }
        }

        private void textBlock1_Tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            longListSelector1.Visibility = Visibility.Visible;
        }
        private void textBlock2_Tap(object sender, System.Windows.Input.GestureEventArgs e)
        {
            longListSelector2.Visibility = Visibility.Visible;
        }
    }
}