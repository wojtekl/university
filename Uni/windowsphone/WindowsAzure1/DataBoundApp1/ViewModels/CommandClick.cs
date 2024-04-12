using System;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Ink;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;

namespace DataBoundApp1.ViewModels
{
    public class CommandClick: ICommand
    {
        private CommandDelegate _command;
        public CommandClick(CommandDelegate command)
        {
            _command = command;
        }
        public bool CanExecute(object parameter)
        {
            return true;
        }

        public event EventHandler CanExecuteChanged;

        public void Execute(object parameter)
        {
            _command(parameter);
        }
    }
    public delegate void CommandDelegate(object parameter);
}
