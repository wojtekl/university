using System;
using System.ComponentModel;
using System.Collections.ObjectModel;
using DataBoundApp1.ServiceReference1;


namespace DataBoundApp1
{
    public class GroupAddUserViewModel : INotifyPropertyChanged
    {

        Service1Client client;

        public ObservableCollection<DataBoundApp1.ServiceReference1.User> users { get; private set; }

        public GroupAddUserViewModel()
        {
            this.Items = new ObservableCollection<ItemViewModel>();
            client = new Service1Client();

            client.GetUsersCompleted += client_GetUsersCompleted;
        }

        private void client_GetUsersCompleted(object sender, GetUsersCompletedEventArgs e)
        {
            if (e.Error == null)
            {
                users = e.Result;
                foreach (DataBoundApp1.ServiceReference1.User n in users)
                {
                    this.Items.Add(new ItemViewModel() { LineOne = n.Name, LineTwo = "", LineThree = n.Description });
                }
                this.IsDataLoaded = true;
            }
            else
            {
                users = new ObservableCollection<DataBoundApp1.ServiceReference1.User>();
            }
        }

        public void AddUser(int groupId, int userIndex)
        {
            client.GroupAddUserAsync(groupId, users[userIndex].Id);
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
            client.GetUsersAsync();
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