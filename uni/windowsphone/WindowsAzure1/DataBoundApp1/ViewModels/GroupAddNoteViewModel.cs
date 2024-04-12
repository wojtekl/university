using System;
using System.ComponentModel;
using System.Collections.ObjectModel;
using DataBoundApp1.ServiceReference1;


namespace DataBoundApp1
{
    public class GroupAddNoteViewModel : INotifyPropertyChanged
    {

        Service1Client client;

        public ObservableCollection<DataBoundApp1.ServiceReference1.Note> notes { get; private set; }

        public GroupAddNoteViewModel()
        {
            this.Items = new ObservableCollection<ItemViewModel>();
            client = new Service1Client();

            client.GetUserNotesCompleted += client_GetUserNotesCompleted;
        }

        private void client_GetUserNotesCompleted(object sender, GetUserNotesCompletedEventArgs e)
        {
            if (e.Error == null)
            {
                notes = e.Result;
                foreach (DataBoundApp1.ServiceReference1.Note n in notes)
                {
                    this.Items.Add(new ItemViewModel() { LineOne = n.Content, LineTwo = "", LineThree = n.Description });
                }
                this.IsDataLoaded = true;
            }
            else
            {
                notes = new ObservableCollection<DataBoundApp1.ServiceReference1.Note>();
            }
        }

        public void AddNote(int groupId, int userIndex)
        {
            client.GroupAddNoteAsync(groupId, notes[userIndex].Id);
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
            client.GetUserNotesAsync(App.ViewModel.user.Id);
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