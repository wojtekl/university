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
    public partial class GroupNote : PhoneApplicationPage
    {
        public GroupNote()
        {
            InitializeComponent();
        }

        int noteIndex;
        int groupId;

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            string selectedIndex = "";
            if (NavigationContext.QueryString.TryGetValue("noteIndex", out selectedIndex))
            {
                int index = int.Parse(selectedIndex);
                noteIndex = index;
            }
            if (NavigationContext.QueryString.TryGetValue("groupId", out selectedIndex))
            {
                int index = int.Parse(selectedIndex);
                groupId = index;
            }
            DataContext = GroupNotes.groupNotesViewModel.Items[noteIndex];
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            Service1Client client = new Service1Client();
            client.GroupRemoveNoteAsync(groupId, GroupNotes.groupNotesViewModel.notes[noteIndex].Id);
            NavigationService.GoBack();
        }
    }
}