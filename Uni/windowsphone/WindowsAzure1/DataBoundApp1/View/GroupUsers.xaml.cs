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

namespace DataBoundApp1
{
    public partial class GroupUsers : PhoneApplicationPage
    {
        public GroupUsers()
        {
            InitializeComponent();
            groupUsersViewModel = new GroupUsersViewModel();
            DataContext = groupUsersViewModel;
        }

        int groupId;
        GroupUsersViewModel groupUsersViewModel;

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            string selectedIndex = "";
            if (NavigationContext.QueryString.TryGetValue("groupId", out selectedIndex))
            {
                int index = int.Parse(selectedIndex);
                groupId = index;
            }
            groupUsersViewModel.LoadUsers(groupId);
        }

        private void MainListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            // If selected index is -1 (no selection) do nothing
            if (MainListBox.SelectedIndex == -1)
                return;

            // Navigate to the new page
            NavigationService.Navigate(new Uri("/View/GroupRemoveUser.xaml?groupId=" + groupId + "&userId=" + groupUsersViewModel.users[MainListBox.SelectedIndex].Id, UriKind.Relative));

            // Reset selected index to -1 (no selection)
            MainListBox.SelectedIndex = -1;
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/View/GroupAddUser.xaml?groupId=" + groupId, UriKind.Relative));
        }

        private void button2_Click(object sender, RoutedEventArgs e)
        {
        }
    }
}