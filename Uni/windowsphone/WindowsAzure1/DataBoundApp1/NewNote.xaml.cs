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
using DataBoundApp1.ServiceReference1;

namespace DataBoundApp1
{
    public partial class NewNote : PhoneApplicationPage
    {
        public NewNote()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            Note note = new Note();
            note.Description = textBox1.Text;
            note.Content = textBox2.Text;
            App.ViewModel.NewNote(note);
            NavigationService.GoBack();
        }

        private void button2_Click(object sender, RoutedEventArgs e)
        {
            NavigationService.GoBack();
        }
    }
}