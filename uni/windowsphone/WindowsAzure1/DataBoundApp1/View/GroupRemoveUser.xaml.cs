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
using System.Windows.Navigation;

namespace DataBoundApp1.View
{
    public partial class GroupRemoveUser : PhoneApplicationPage
    {
        public GroupRemoveUser()
        {
            InitializeComponent();
        }

        int groupId;
        int userId;

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            string selectedIndex = "";
            if (NavigationContext.QueryString.TryGetValue("groupId", out selectedIndex))
            {
                int index = int.Parse(selectedIndex);
                groupId = index;
            }
            if (NavigationContext.QueryString.TryGetValue("userId", out selectedIndex))
            {
                int index = int.Parse(selectedIndex);
                userId = index;
            }
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            Service1Client client = new Service1Client();
            client.GroupRemoveUserAsync(groupId, userId);
            NavigationService.GoBack();
        }
    }
}