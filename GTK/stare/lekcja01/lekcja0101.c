#include <gtk/gtk.h>

int show_popup(GtkWidget *widget, GdkEvent *event)
{

	const gint RIGHT_CLICK = 3;

	if(event->type == GDK_BUTTON_PRESS)
	{
		GdkEventButton *bevent = (GdkEventButton*)event;

		if(bevent->button == RIGHT_CLICK)
		{
			gtk_menu_popup(GTK_MENU(widget), NULL, NULL, NULL, NULL, bevent->button, bevent->time);
		}
		return TRUE;
	}
	return TRUE;

}

void undo_redo(GtkWidget *widget, gpointer item)
{
	static gint count = 2;
	const gchar *name = gtk_widget_get_name(widget);

	if(g_strcmp0(name, "undo"))
	{
		count++;
	}
	else
	{
		count--;
	}

	if(count < 0)
	{
		gtk_widget_set_sensitive(widget, FALSE);
		gtk_widget_set_sensitive(item, TRUE);
	}

	if(count > 5)
	{
		gtk_widget_set_sensitive(widget, FALSE);
		gtk_widget_set_sensitive(item, TRUE);
	}
}

int main(int argc, char *argv[])
{
	GtkWidget *window;
	GtkWidget *ebox;
	GtkWidget *pmenu;
	GtkWidget *hideMi;
	GtkWidget *quitMi;

	GtkWidget *vbox;
	GtkWidget *toolbar;
	GtkToolItem *newTb;
	GtkToolItem *openTb;
	GtkToolItem *saveTb;
	GtkToolItem *sep;
	GtkToolItem *exitTb;

	GtkToolItem *undo;
	GtkToolItem *redo;

	gtk_init(&argc, &argv);

	window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	gtk_window_set_position(GTK_WINDOW(window), GTK_WIN_POS_CENTER);
	gtk_window_set_default_size(GTK_WINDOW(window), 300, 200);
	gtk_window_set_title(GTK_WINDOW(window), "Popup menu");

	ebox = gtk_event_box_new();
	//gtk_container_add(GTK_CONTAINER(window), ebox);

	pmenu = gtk_menu_new();

	hideMi = gtk_menu_item_new_with_label("Minimize");
	gtk_widget_show(hideMi);
	gtk_menu_shell_append(GTK_MENU_SHELL(pmenu), hideMi);

	quitMi = gtk_menu_item_new_with_label("Quit");
	gtk_widget_show(quitMi);
	gtk_menu_shell_append(GTK_MENU_SHELL(pmenu), quitMi);

	vbox = gtk_vbox_new(FALSE, 0);
	gtk_container_add(GTK_CONTAINER(window), vbox);

	toolbar = gtk_toolbar_new();
	gtk_toolbar_set_style(GTK_TOOLBAR(toolbar), GTK_TOOLBAR_ICONS);

	gtk_container_set_border_width(GTK_CONTAINER(toolbar), 2);

	newTb = gtk_tool_button_new_from_stock(GTK_STOCK_NEW);
	gtk_toolbar_insert(GTK_TOOLBAR(toolbar), newTb, -1);

	openTb = gtk_tool_button_new_from_stock(GTK_STOCK_OPEN);
	gtk_toolbar_insert(GTK_TOOLBAR(toolbar), openTb, -1);

	saveTb = gtk_tool_button_new_from_stock(GTK_STOCK_SAVE);
	gtk_toolbar_insert(GTK_TOOLBAR(toolbar), saveTb, -1);

	sep = gtk_separator_tool_item_new();
	gtk_toolbar_insert(GTK_TOOLBAR(toolbar), sep, -1);

	exitTb = gtk_tool_button_new_from_stock(GTK_STOCK_QUIT);
	gtk_toolbar_insert(GTK_TOOLBAR(toolbar), exitTb, -1);

	undo = gtk_tool_button_new_from_stock(GTK_STOCK_UNDO);
	gtk_widget_set_name(GTK_WIDGET(undo), "undo");
	gtk_toolbar_insert(GTK_TOOLBAR(toolbar), undo, -1);

	redo = gtk_tool_button_new_from_stock(GTK_STOCK_REDO);
	gtk_toolbar_insert(GTK_TOOLBAR(toolbar), redo, -1);

	gtk_box_pack_start(GTK_BOX(vbox), toolbar, FALSE, FALSE, 5);

	g_signal_connect(G_OBJECT(exitTb), "clicked", G_CALLBACK(gtk_main_quit), NULL);

	g_signal_connect_swapped(G_OBJECT(hideMi), "activate", G_CALLBACK(gtk_window_iconify), GTK_WINDOW(window));

	g_signal_connect(G_OBJECT(quitMi), "activate", G_CALLBACK(gtk_main_quit), NULL);

	g_signal_connect(G_OBJECT(window), "destroy", G_CALLBACK(gtk_main_quit), NULL);

	g_signal_connect_swapped(G_OBJECT(ebox), "button-press-event", G_CALLBACK(show_popup), pmenu);

	g_signal_connect(G_OBJECT(undo), "clicked", G_CALLBACK(undo_redo), undo);

	g_signal_connect(G_OBJECT(redo), "clicked", G_CALLBACK(undo_redo), redo);

	gtk_widget_show_all(window);

	gtk_main();

	return 0;
}
