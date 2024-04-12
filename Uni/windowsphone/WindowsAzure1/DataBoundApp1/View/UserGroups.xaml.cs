using System;
using System.Windows;
using System.Windows.Controls;
using Microsoft.Phone.Controls;
using System.Windows.Navigation;

namespace DataBoundApp1
{
    public partial class UserGroups : PhoneApplicationPage
    {
        public UserGroups()
        {
            InitializeComponent();
            // Set the data context of the listbox control to the sample data
            userGroupsViewModel = new UserGroupsViewModel();
            DataContext = userGroupsViewModel;
        }

        int userId;
        UserGroupsViewModel userGroupsViewModel;

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            string selectedIndex = "";
            if (NavigationContext.QueryString.TryGetValue("userId", out selectedIndex))
            {
                int index = int.Parse(selectedIndex);
                userId = index;
            }
            userGroupsViewModel.LoadGroups(userId);
        }

        private void MainListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            // If selected index is -1 (no selection) do nothing
            if (MainListBox.SelectedIndex == -1)
                return;

            // Navigate to the new page
            NavigationService.Navigate(new Uri("/View/GroupNotes.xaml?groupId=" + userGroupsViewModel.groups[MainListBox.SelectedIndex].Id, UriKind.Relative));

            // Reset selected index to -1 (no selection)
            MainListBox.SelectedIndex = -1;
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/View/NewGroup.xaml?userId="+userId, UriKind.Relative));
        }
    }
}