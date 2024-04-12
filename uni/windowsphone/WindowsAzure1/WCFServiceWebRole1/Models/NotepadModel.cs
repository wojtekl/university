using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.Entity;
using System.ComponentModel.DataAnnotations.Schema;

namespace WCFServiceWebRole1.Models
{
    public class NotepadModel: DbContext
    {

        public NotepadModel()
            : base("name=connectionString")
        {
        }

        public virtual DbSet<User> users { get; set; }
        public virtual DbSet<Group> groups { get; set; }
        public virtual DbSet<Note> notes { get; set; }
        public virtual DbSet<GroupNote> groupsNotes { get; set; }
        public virtual DbSet<GroupUser> groupsUsers { get; set; }

    }

    public class User
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
    }
    public class Group
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
    }
    public class GroupUser
    {
        public int Id { get; set; }
        //[ForeignKey("Group")]
        public int Id_Group { get; set; }
        //public virtual Group Group { get; set; }
        //[ForeignKey("Id_User")]
        public int Id_User { get; set; }
        //public virtual User User { get; set; }
    }
    public class Note
    {
        public int Id { get; set; }
        public string Description { get; set; }
        public string Content { get; set; }
        public int Id_User { get; set; }
    }
    public class GroupNote
    {
        public int Id { get; set; }
        //[ForeignKey("Group")]
        public int Id_Group { get; set; }
        //public virtual Group Group { get; set; }
        //[ForeignKey("Note")]
        public int Id_Note { get; set; }
        //public virtual Note Note { get; set; }
    }

}