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
using System.Windows.Navigation;
using DataBoundApp1.ServiceReference1;

namespace DataBoundApp1
{
    public partial class Edit : PhoneApplicationPage
    {
        public Edit()
        {
            InitializeComponent();
        }

        int NoteIndex;

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            string selectedIndex = "";
            if (NavigationContext.QueryString.TryGetValue("noteIndex", out selectedIndex))
            {
                int index = int.Parse(selectedIndex);
                DataContext = App.ViewModel.Items[index];
                NoteIndex = index;
            }
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            Note note = App.ViewModel.notes[NoteIndex];
            note.Description = textBox1.Text;
            note.Content = textBox2.Text;
            ((ItemViewModel)DataContext).LineOne = note.Description;
            ((ItemViewModel)DataContext).LineThree = note.Content;
            App.ViewModel.UpdateNote(note);
            NavigationService.GoBack();
        }

        private void button2_Click(object sender, RoutedEventArgs e)
        {
            NavigationService.GoBack();
        }
    }
}