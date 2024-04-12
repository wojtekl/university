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
    public partial class GroupAddNote : PhoneApplicationPage
    {
        public GroupAddNote()
        {
            InitializeComponent();
            groupAddNoteViewModel = new GroupAddNoteViewModel();
            DataContext = groupAddNoteViewModel;
        }

        int groupId;
        GroupAddNoteViewModel groupAddNoteViewModel;

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            string selectedIndex = "";
            if (NavigationContext.QueryString.TryGetValue("groupId", out selectedIndex))
            {
                int index = int.Parse(selectedIndex);
                groupId = index;
            }
            groupAddNoteViewModel.LoadData();
        }

        private void MainListBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            // If selected index is -1 (no selection) do nothing
            if (MainListBox.SelectedIndex == -1)
                return;

            groupAddNoteViewModel.AddNote(groupId, MainListBox.SelectedIndex);
            NavigationService.GoBack();

            // Reset selected index to -1 (no selection)
            MainListBox.SelectedIndex = -1;
        }
    }
}