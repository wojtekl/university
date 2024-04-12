using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using WCFServiceWebRole1.Models;

namespace WCFServiceWebRole1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService1" in both code and config file together.
    [ServiceContract]
    public interface IService1
    {

        [OperationContract]
        int Login(User user);

        [OperationContract]
        int Register(User user);

        [OperationContract]
        List<Note> GetUserNotes(int UserId);

        [OperationContract]
        void NewNote(Note note);

        [OperationContract]
        void UpdateNote(Note note);

        [OperationContract]
        void RemoveNote(int noteId);

        [OperationContract]
        int NewGroup(Group group, int userId);

        [OperationContract]
        void RemoveGroup(int groupId);

        [OperationContract]
        void GroupAddNote(int groupId, int noteId);

        [OperationContract]
        void GroupRemoveNote(int groupId, int noteId);

        [OperationContract]
        void GroupAddUser(int groupId, int userId);

        [OperationContract]
        void GroupRemoveUser(int groupId, int userId);

        [OperationContract]
        List<Group> GetUserGroups(int sserId);

        [OperationContract]
        List<Note> GetGroupNotes(int groupId);

        [OperationContract]
        List<User> GetGroupUsers(int groupId);

        [OperationContract]
        List<User> GetUsers();

        /*[OperationContract]
        string GetData(int value);

        [OperationContract]
        CompositeType GetDataUsingDataContract(CompositeType composite);*/

        // TODO: Add your service operations here
    }


    // Use a data contract as illustrated in the sample below to add composite types to service operations.
    /*[DataContract]
    public class CompositeType
    {
        bool boolValue = true;
        string stringValue = "Hello ";

        [DataMember]
        public bool BoolValue
        {
            get { return boolValue; }
            set { boolValue = value; }
        }

        [DataMember]
        public string StringValue
        {
            get { return stringValue; }
            set { stringValue = value; }
        }
    }*/
}
