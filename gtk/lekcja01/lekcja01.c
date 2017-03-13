#include <gtk/gtk.h>
#include <gdk/gdkkeysyms.h>

void toggle_statusbar(GtkWidget *widget, gpointer statusbar)
{
	if(gtk_check_menu_item_get_active(GTK_CHECK_MENU_ITEM(widget)))
	{
		gtk_widget_show(statusbar);
	}
	else
	{
		gtk_widget_hide(statusbar);
	}
}

int main(int argc, char *argv[])
{

	GtkWidget *window;
	GtkWidget *vbox;

	GtkWidget *menubar;
	GtkWidget *fileMenu;
	GtkWidget *imprMenu;
	GtkWidget *sep;
	GtkWidget *fileMi;
	GtkWidget *imprMi;
	GtkWidget *feedMi;
	GtkWidget *bookMi;
	GtkWidget *mailMi;
	GtkWidget *quitMi;

	GtkWidget *viewmenu;
	GtkWidget *view;
	GtkWidget *tog_stat;
	GtkWidget *statusbar;

	GtkAccelGroup *accel_group = NULL;

	gtk_init(&argc, &argv);

	window = gtk_window_new(GTK_WINDOW_TOPLEVEL);
	gtk_window_set_position(GTK_WINDOW(window), GTK_WIN_POS_CENTER);
	gtk_window_set_default_size(GTK_WINDOW(window), 300, 200);
	gtk_window_set_title(GTK_WINDOW(window), "Submenu");

	vbox = gtk_vbox_new(FALSE, 0);
	gtk_container_add(GTK_CONTAINER(window), vbox);

	menubar = gtk_menu_bar_new();

	viewmenu = gtk_menu_new();

	fileMenu = gtk_menu_new();

	accel_group = gtk_accel_group_new();
	gtk_window_add_accel_group(GTK_WINDOW(window), accel_group);
	
	fileMi = gtk_menu_item_new_with_mnemonic("_File");

	view = gtk_menu_item_new_with_label("View");
	tog_stat = gtk_check_menu_item_new_with_label("View statusbar");
	gtk_check_menu_item_set_active(GTK_CHECK_MENU_ITEM(tog_stat), TRUE);

	gtk_menu_item_set_submenu(GTK_MENU_ITEM(view), viewmenu);
	gtk_menu_shell_append(GTK_MENU_SHELL(viewmenu), tog_stat);
	gtk_menu_shell_append(GTK_MENU_SHELL(menubar), view);

	imprMenu = gtk_menu_new();
	imprMi = gtk_image_menu_item_new_from_stock(GTK_STOCK_NEW, NULL);
	feedMi = gtk_image_menu_item_new_from_stock(GTK_STOCK_OPEN, NULL);
	bookMi = gtk_menu_item_new_with_label("Import bookmarks...");
	mailMi = gtk_menu_item_new_with_label("Import mail...");

	gtk_menu_item_set_submenu(GTK_MENU_ITEM(imprMi), imprMenu);
	gtk_menu_shell_append(GTK_MENU_SHELL(imprMenu), feedMi);
	gtk_menu_shell_append(GTK_MENU_SHELL(imprMenu), bookMi);
	gtk_menu_shell_append(GTK_MENU_SHELL(imprMenu), mailMi);
	sep = gtk_separator_menu_item_new();
	quitMi = gtk_image_menu_item_new_from_stock(GTK_STOCK_QUIT, accel_group);

	gtk_widget_add_accelerator(quitMi, "activate", accel_group, GDK_KEY_q, GDK_CONTROL_MASK, GTK_ACCEL_VISIBLE);

	gtk_menu_item_set_submenu(GTK_MENU_ITEM(fileMi), fileMenu);
	gtk_menu_shell_append(GTK_MENU_SHELL(fileMenu), imprMi);
	gtk_menu_shell_append(GTK_MENU_SHELL(fileMenu), sep);
	gtk_menu_shell_append(GTK_MENU_SHELL(fileMenu), quitMi);
	gtk_menu_shell_append(GTK_MENU_SHELL(menubar), fileMi);
	gtk_box_pack_start(GTK_BOX(vbox), menubar, FALSE, FALSE, 0);

	statusbar = gtk_statusbar_new();
	gtk_box_pack_start(GTK_BOX(vbox), statusbar, FALSE, TRUE, 0);

	g_signal_connect(G_OBJECT(window), "destroy", G_CALLBACK(gtk_main_quit), NULL);

	g_signal_connect(G_OBJECT(quitMi), "activate", G_CALLBACK(gtk_main_quit), NULL);

	gtk_widget_show_all(window);

	gtk_main();

	return 0;

}
