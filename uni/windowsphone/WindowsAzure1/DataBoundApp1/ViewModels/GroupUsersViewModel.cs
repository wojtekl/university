using System;
using System.ComponentModel;
using System.Collections.ObjectModel;
using DataBoundApp1.ServiceReference1;


namespace DataBoundApp1
{
    public class GroupUsersViewModel : INotifyPropertyChanged
    {

        Service1Client client;

        public ObservableCollection<DataBoundApp1.ServiceReference1.User> users { get; private set; }

        public GroupUsersViewModel()
        {
            this.Items = new ObservableCollection<ItemViewModel>();
            client = new Service1Client();

            client.GetGroupUsersCompleted += client_GetGroupUsersCompleted;
        }

        public void LoadUsers(int groupId)
        {
            client.GetGroupUsersAsync(groupId);
        }

        public void RemoveUser(int groupId, int userIndex)
        {
            client.GroupRemoveUserAsync(groupId, users[userIndex].Id);
        }

        private void client_GetGroupUsersCompleted(object sender, GetGroupUsersCompletedEventArgs e)
        {
            if (e.Error == null)
            {
                Items.Clear();
                users = e.Result;
                foreach (DataBoundApp1.ServiceReference1.User n in users)
                {
                    this.Items.Add(new ItemViewModel() { LineOne = n.Name, LineTwo = "", LineThree = "" });
                }
                this.IsDataLoaded = true;
            }
            else
            {
                users = new ObservableCollection<DataBoundApp1.ServiceReference1.User>();
            }
        }

        /// <summary>
        /// A collection for ItemViewModel objects.
        /// </summary>
        public ObservableCollection<ItemViewModel> Items { get; private set; }

        private string _sampleProperty = "Sample Runtime Property Value";
        /// <summary>
        /// Sample ViewModel property; this property is used in the view to display its value using a Binding
        /// </summary>
        /// <returns></returns>
        public string SampleProperty
        {
            get
            {
                return _sampleProperty;
            }
            set
            {
                if (value != _sampleProperty)
                {
                    _sampleProperty = value;
                    NotifyPropertyChanged("SampleProperty");
                }
            }
        }

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