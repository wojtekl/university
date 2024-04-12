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
    public partial class GroupNotes : PhoneApplicationPage
    {
        public GroupNotes()
        {
            InitializeComponent();
            groupNotesViewModel = new GroupNotesViewModel();
            DataContext = groupNotesViewModel;
        }

        int groupId;
        public static GroupNotesViewModel groupNotesViewModel;

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            string selectedIndex = "";
            if (NavigationContext.QueryString.TryGetValue("groupId", out selectedIndex))
            {
                int index = int.Parse(selectedIndex);
                groupId = index;
            }
            groupNotesViewModel.LoadNotes(groupId);
        }

        private void MainListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            // If selected index is -1 (no selection) do nothing
            if (MainListBox.SelectedIndex == -1)
                return;

            // Navigate to the new page
            NavigationService.Navigate(new Uri("/View/GroupNote.xaml?noteIndex=" + MainListBox.SelectedIndex + "&groupId="+groupId, UriKind.Relative));

            // Reset selected index to -1 (no selection)
            MainListBox.SelectedIndex = -1;
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            groupNotesViewModel.RemoveGroup(groupId);
            NavigationService.GoBack();
        }

        private void button2_Click(object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/View/GroupUsers.xaml?groupId=" + groupId, UriKind.Relative));
        }

        private void button3_Click(object sender, RoutedEventArgs e)
        {
            NavigationService.Navigate(new Uri("/View/GroupAddNote.xaml?groupId=" + groupId, UriKind.Relative));
        }
    }
}