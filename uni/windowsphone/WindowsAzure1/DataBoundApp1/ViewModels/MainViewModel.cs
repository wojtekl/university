using System;
using System.ComponentModel;
using System.Windows;
using System.Collections.ObjectModel;
using DataBoundApp1.ViewModels;
using DataBoundApp1.ServiceReference1;
using System.IO.IsolatedStorage;


namespace DataBoundApp1
{
    public class MainViewModel : INotifyPropertyChanged
    {

        Service1Client client;

        private Visibility _showRejestracja=Visibility.Visible;

        public Visibility ShowRejestracja
        {
            get
            {
                if (IsolatedStorageSettings.ApplicationSettings.Contains("NotepadUserName") && IsolatedStorageSettings.ApplicationSettings["NotepadUserName"] != null)
                {
                    user.Name = IsolatedStorageSettings.ApplicationSettings["NotepadUserName"].ToString();
                    //user.Password = IsolatedStorageSettings.ApplicationSettings["NotepadUserPassword"].ToString();
                    client.LoginAsync(user);
                }
                return _showRejestracja;
            }
            set
            {
                if (_showRejestracja != value)
                {
                    _showRejestracja = value;
                    NotifyPropertyChanged("ShowRejestracja");
                }
            }
        }

        private Visibility _showList = Visibility.Collapsed;

        public Visibility ShowList {
            get
            {
                return _showList;
            }
            set
            {
                if (_showList != value)
                {
                    _showList = value;
                    NotifyPropertyChanged("ShowList");
                }
            }
        }

        public User user { get; private set; }
        public CommandClick Rejestracja { get; private set; }
        public CommandClick Logowanie { get; private set; }
        public ObservableCollection<Note> notes { get; private set; }

        public MainViewModel()
        {
            this.Items = new ObservableCollection<ItemViewModel>();
            client = new Service1Client();
            user = new User();

            Rejestracja = new CommandClick(RejestracjaCommand);
            Logowanie = new CommandClick(LogowanieCommand);

            client.RegisterCompleted += client_RegisterCompleted;
            client.LoginCompleted += client_LoginCompleted;
            client.GetUserNotesCompleted += client_GetUserNotesCompleted;
        }

        public void Przelacz()
        {
            IsolatedStorageSettings.ApplicationSettings.Remove("NotepadUserName");
            //user = new User();
        }

        private void client_GetUserNotesCompleted(object sender, GetUserNotesCompletedEventArgs e)
        {
            if (e.Error == null)
            {
                Items.Clear();
                notes = e.Result;
                foreach (Note n in notes)
                {
                    this.Items.Add(new ItemViewModel() { LineOne = n.Description, LineTwo = "", LineThree = n.Content });
                }
                this.IsDataLoaded = true;
            }
            else
            {
                notes = new ObservableCollection<Note>();
            }
        }

        public void NewNote(Note note)
        {
            Items.Add(new ItemViewModel { LineOne = note.Description, LineThree = note.Content });
            note.Id_User = user.Id;
            client.NewNoteAsync(note);
            this.Items = new ObservableCollection<ItemViewModel>();
            client.GetUserNotesAsync(user.Id);
        }

        public void UpdateNote(Note note)
        {
            note.Id_User = user.Id;
            client.UpdateNoteAsync(note);
        }

        public void RemoveNote(int NoteIndex)
        {
            client.RemoveNoteAsync(notes[NoteIndex].Id);
            notes.RemoveAt(NoteIndex);
            Items.RemoveAt(NoteIndex);
        }

        private void client_RegisterCompleted(object sender, RegisterCompletedEventArgs e)
        {
            if (e.Error == null)
            {
                if (e.Result > 0)
                {
                    user.Id = e.Result;
                    SaveUserToLocalStorage();
                    ShowRejestracja = Visibility.Collapsed;
                }
                else
                {
                    ShowRejestracja = Visibility.Visible;
                }
            }
            else
            {
                ShowRejestracja = Visibility.Visible;
            }
            //NotifyPropertyChanged("ShowRejestracja");
        }

        private void SaveUserToLocalStorage()
        {
            if (IsolatedStorageSettings.ApplicationSettings.Contains("NotepadUserName"))
            {
                IsolatedStorageSettings.ApplicationSettings["NotepadUserName"] = user.Name;
            }
            else
            {
                IsolatedStorageSettings.ApplicationSettings.Add("NotepadUserName", user.Name);
            }
            if (IsolatedStorageSettings.ApplicationSettings.Contains("NotepadUserPassword"))
            {
                //IsolatedStorageSettings.ApplicationSettings["NotepadUserPassword"] = user.Password;
            }
            else
            {
                //IsolatedStorageSettings.ApplicationSettings.Add("NotepadUserPassword", user.Password);
            }
        }

        private void client_LoginCompleted(object sender, LoginCompletedEventArgs e)
        {
            if (e.Error == null)
            {
                user.Id = e.Result;
                ShowRejestracja = Visibility.Collapsed;
                if (!IsolatedStorageSettings.ApplicationSettings.Contains("NotepadUserName"))
                {
                    IsolatedStorageSettings.ApplicationSettings.Add("NotepadUserName", user.Name);
                }
                client.GetUserNotesAsync(user.Id);
                ShowList = Visibility.Visible;
            }
            else
            {
                ShowRejestracja = Visibility.Visible;
            }
            //NotifyPropertyChanged("ShowRejestracja");
            //NotifyPropertyChanged("ShowList");
        }

        private void RejestracjaCommand(object parameter)
        {
            client.RegisterAsync(user);
        }

        private void LogowanieCommand(object parameter)
        {
            client.LoginAsync(user);
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