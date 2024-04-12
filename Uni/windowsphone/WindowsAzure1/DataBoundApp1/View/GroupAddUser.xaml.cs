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

namespace DataBoundApp1.View
{
    public partial class GroupAddUser : PhoneApplicationPage
    {
        public GroupAddUser()
        {
            InitializeComponent();
            groupAddUserViewModel = new GroupAddUserViewModel();
            DataContext = groupAddUserViewModel;
        }

        int groupId;
        GroupAddUserViewModel groupAddUserViewModel;

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            string selectedIndex = "";
            if (NavigationContext.QueryString.TryGetValue("groupId", out selectedIndex))
            {
                int index = int.Parse(selectedIndex);
                groupId = index;
            }
            groupAddUserViewModel.LoadData();
        }

        private void MainListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            // If selected index is -1 (no selection) do nothing
            if (MainListBox.SelectedIndex == -1)
                return;

            groupAddUserViewModel.AddUser(groupId, MainListBox.SelectedIndex);
            NavigationService.GoBack();

            // Reset selected index to -1 (no selection)
            MainListBox.SelectedIndex = -1;
        }
    }
}