using System.Windows;
using Microsoft.Phone.Controls;
using DataBoundApp1.ServiceReference1;
using System.Windows.Navigation;

namespace DataBoundApp1.View
{
    public partial class NewGroup : PhoneApplicationPage
    {
        public NewGroup()
        {
            InitializeComponent();
        }

        int userId;

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            string selectedIndex = "";
            if (NavigationContext.QueryString.TryGetValue("userId", out selectedIndex))
            {
                int index = int.Parse(selectedIndex);
                userId = index;
            }
        }

        private void button1_Click(object sender, RoutedEventArgs e)
        {
            Service1Client client = new Service1Client();
            Group group = new Group { Name = textBox1.Text };
            client.NewGroupAsync(group, userId);
            //client.CloseAsync();
            NavigationService.GoBack();
        }
    }
}