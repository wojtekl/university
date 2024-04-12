using System;
using System.Windows;
using System.Windows.Controls;
using Microsoft.Phone.Controls;
using DataBoundApp1.ServiceReference1;

namespace DataBoundApp1
{
    public partial class MainPage : PhoneApplicationPage
    {
        // Constructor
        public MainPage()
        {
            InitializeComponent();

            // Set the data context of the listbox control to the sample data
            DataContext = App.ViewModel;
            this.Loaded += new RoutedEventHandler(MainPage_Loaded);
        }

        // Handle selection changed on ListBox
        private void MainListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            // If selected index is -1 (no selection) do nothing
            if (MainListBox.SelectedIndex == -1)
                return;

            // Navigate to the new page
            NavigationService.Navigate(new Uri("/DetailsPage.xaml?selectedItem=" + MainListBox.SelectedIndex, UriKind.Relative));

            // Reset selected index to -1 (no selection)
            MainListBox.SelectedIndex = -1;
        }

        // Load data for the ViewModel Items
        private void MainPage_Loaded(object sender, RoutedEventArgs e)
        {
            if (!App.ViewModel.IsDataLoaded)
            {
                App.ViewModel.LoadData();
            }
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/NewNote.xaml?user.Id=" + App.ViewModel.user.Id, UriKind.Relative));
        }

        private void button2_Click(object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/View/UserGroups.xaml?userId=" + App.ViewModel.user.Id, UriKind.Relative));
        }

        private void button3_Click(object sender, RoutedEventArgs e)
        {
            App.ViewModel.Przelacz();
            App.ViewModel.ShowList = Visibility.Collapsed;
            App.ViewModel.ShowRejestracja = Visibility.Visible;
        }

    }
}