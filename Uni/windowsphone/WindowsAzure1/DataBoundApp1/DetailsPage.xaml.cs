using System;
using System.Windows;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;

namespace DataBoundApp1
{
    public partial class DetailsPage : PhoneApplicationPage
    {
        int NoteIndex;

        // Constructor
        public DetailsPage()
        {
            InitializeComponent();
        }

        // When page is navigated to set data context to selected item in list
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            string selectedIndex = "";
            if (NavigationContext.QueryString.TryGetValue("selectedItem", out selectedIndex))
            {
                int index = int.Parse(selectedIndex);
                DataContext = App.ViewModel.Items[index];
                NoteIndex = index;
            }
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            App.ViewModel.RemoveNote(NoteIndex);
            NavigationService.GoBack();
        }

        private void button2_Click(object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/Edit.xaml?noteIndex="+NoteIndex, UriKind.Relative));
        }
    }
}