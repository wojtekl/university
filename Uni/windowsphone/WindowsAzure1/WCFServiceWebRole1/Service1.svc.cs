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
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in code, svc and config file together.
    public class Service1 : IService1
    {

        private NotepadModel model;

        public Service1()
        {
            model = new NotepadModel();
        }

        public int Login(User user)
        {
            try
            {
                var userLogin = model.users.Single(w => w.Name == user.Name);
                if (null != userLogin)
                {
                    return userLogin.Id;
                }
                return -1;
            }
            catch(System.ArgumentNullException a)
            {
                return -1;
            }
        }

        public int Register(User user)
        {
            model.users.Add(user);
            model.SaveChanges();
            return model.users.Where(u => u.Name == user.Name).Single().Id;
        }

        public List<Note> GetUserNotes(int UserId)
        {
            return model.notes.Where(n => n.Id_User == UserId).ToList();
        }

        public void NewNote(Note note)
        {
            model.notes.Add(note);
            model.SaveChanges();
        }

        public void UpdateNote(Note note)
        {
            var notes = model.notes.Where(n => n.Id == note.Id).Single();
            notes.Description = note.Description;
            notes.Content = note.Content;
            notes.Id_User = note.Id_User;
            model.SaveChanges();
        }

        public void RemoveNote(int noteId)
        {
            List<GroupNote> groupsNotes = model.groupsNotes.Where(g => g.Id_Note == noteId).ToList();
            if (0 < groupsNotes.Count)
            {
                model.groupsNotes.RemoveRange(groupsNotes);
            }
            model.notes.Remove(model.notes.Where(n => n.Id == noteId).Single());
            model.SaveChanges();
        }

        public int NewGroup(Group group, int userId)
        {
            model.groups.Add(group);
            model.SaveChanges();
            model.groupsUsers.Add(new GroupUser { Id_Group = model.groups.Where(g => g.Name == group.Name).Single().Id, Id_User = userId });
            model.SaveChanges();
            return model.groups.Where(g => g.Name == group.Name).Single().Id;
        }

        public void RemoveGroup(int groupId)
        {
            List<GroupNote> groupsNotes = model.groupsNotes.Where(g => g.Id_Group == groupId).ToList();
            if (0 < groupsNotes.Count)
            {
                model.groupsNotes.RemoveRange(groupsNotes);
                model.SaveChanges();
            }
            List<GroupUser> groupsUsers = model.groupsUsers.Where(g => g.Id_Group == groupId).ToList();
            if (0 < groupsUsers.Count)
            {
                model.groupsUsers.RemoveRange(groupsUsers);
                model.SaveChanges();
            }
            model.groups.Remove(model.groups.Where(g => g.Id == groupId).Single());
            model.SaveChanges();
        }

        public void GroupAddNote(int groupId, int noteId)
        {
            model.groupsNotes.Add(new GroupNote { Id_Group = groupId, Id_Note = noteId });
            model.SaveChanges();
        }

        public void GroupRemoveNote(int groupId, int noteId)
        {
            model.groupsNotes.Remove(model.groupsNotes.Where(gn => gn.Id_Group == groupId && gn.Id_Note == noteId).Single());
            model.SaveChanges();
        }

        public void GroupAddUser(int groupId, int userId)
        {
            model.groupsUsers.Add(new GroupUser {Id_Group=groupId, Id_User=userId });
            model.SaveChanges();
        }

        public void GroupRemoveUser(int groupId, int userId)
        {
            model.groupsUsers.Remove(model.groupsUsers.Where(gu => gu.Id_Group == groupId && gu.Id_User == userId).Single());
            model.SaveChanges();
        }

        public List<Group> GetUserGroups(int userId)
        {
            return model.groups.Where( g => (model.groupsUsers.Where(gu => gu.Id_User == userId).Select(gu => gu.Id_Group).ToList()).Contains(g.Id) ).ToList();
        }

        public List<User> GetGroupUsers(int groupId)
        {
            return model.users.Where(u => (model.groupsUsers.Where(gu => gu.Id_Group == groupId).Select(gu => gu.Id_User).ToList()).Contains(u.Id)).ToList();
        }

        public List<Note> GetGroupNotes(int groupId)
        {
            return model.notes.Where(n => (model.groupsNotes.Where(g => g.Id_Group == groupId).Select(g => g.Id_Note).ToList()).Contains(n.Id)).ToList();
        }

        public List<User> GetUsers()
        {
            return model.users.ToList();
        }

    }
}
