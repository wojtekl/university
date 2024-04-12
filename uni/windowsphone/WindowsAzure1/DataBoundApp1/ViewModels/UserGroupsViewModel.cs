using System;
using System.ComponentModel;
using System.Collections.ObjectModel;
using DataBoundApp1.ServiceReference1;


namespace DataBoundApp1
{
    public class UserGroupsViewModel : INotifyPropertyChanged
    {

        Service1Client client;

        public ObservableCollection<DataBoundApp1.ServiceReference1.Group> groups { get; private set; }

        public UserGroupsViewModel()
        {
            this.Items = new ObservableCollection<ItemViewModel>();
            client = new Service1Client();
            client.GetUserGroupsCompleted += client_GetUserGroupsCompleted;
        }

        public void LoadGroups(int userId)
        {
            client.GetUserGroupsAsync(userId);
        }

        private void client_GetUserGroupsCompleted(object sender, GetUserGroupsCompletedEventArgs e)
        {
            if (e.Error == null)
            {
                Items.Clear();
                groups = e.Result;
                foreach (DataBoundApp1.ServiceReference1.Group g in groups)
                {
                    this.Items.Add(new ItemViewModel() { LineOne = g.Name, LineTwo = "", LineThree = g.Description });
                }
                this.IsDataLoaded = true;
            }
            else
            {
                groups = new ObservableCollection<DataBoundApp1.ServiceReference1.Group>();
            }
        }

        /// <summary>
        /// A collection for ItemViewModel objects.
        /// </summary>
        public ObservableCollection<ItemViewModel> Items { get; private set; }

        public bool IsDataLoaded
        {
            get;
            private set;
        }

        /// <summary>
        /// Creates and adds a few ItemViewModel objects into the Items collection.
        /// </summary>
        public void LoadData()
        {
            // Sample data; replace with real data
        }

        public event PropertyChangedEventHandler PropertyChanged;
        private void NotifyPropertyChanged(String propertyName)
        {
            PropertyChangedEventHandler handler = PropertyChanged;
            if (null != handler)
            {
                handler(this, new PropertyChangedEventArgs(propertyName));
            }
        }
    }
}