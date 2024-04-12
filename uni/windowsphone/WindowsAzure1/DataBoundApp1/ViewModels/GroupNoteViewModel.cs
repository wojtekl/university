using System;
using System.ComponentModel;
using System.Collections.ObjectModel;
using DataBoundApp1.ServiceReference1;


namespace DataBoundApp1
{
    public class GroupNoteViewModel : INotifyPropertyChanged
    {

        Service1Client client;

        public ObservableCollection<DataBoundApp1.ServiceReference1.Note> notes { get; private set; }

        public GroupNoteViewModel()
        {
            this.Items = new ObservableCollection<ItemViewModel>();
            client = new Service1Client();

            client.GetGroupNotesCompleted += client_GetGroupNotesCompleted;
        }

        public void LoadNotes(int groupId)
        {
            client.GetGroupNotesAsync(groupId);
        }

        public void RemoveGroup(int groupId)
        {
            client.RemoveGroupAsync(groupId);
        }

        private void client_GetGroupNotesCompleted(object sender, GetGroupNotesCompletedEventArgs e)
        {
            if (e.Error == null)
            {
                Items.Clear();
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