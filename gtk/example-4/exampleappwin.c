#include <gtk/gtk.h>

#include "exampleapp.h"
#include "exampleappwin.h"

struct _ExampleAppWindow
{
  GtkApplicationWindow parent;
};

typedef struct _ExampleAppWindowPrivate 
  ExampleAppWindowPrivate;

struct _ExampleAppWindowPrivate
{
  GSettings *settings;
  GtkWidget *stack;
  GtkWidget *gears;
  GtkWidget *lines;
  GtkWidget *lines_label;
};

G_DEFINE_TYPE_WITH_PRIVATE(ExampleAppWindow, 
  example_app_window, GTK_TYPE_APPLICATION_WINDOW);

static void search_text_changed(GtkEntry *entry, 
  ExampleAppWindow *win)
{
  ExampleAppWindowPrivate *priv;
  const gchar *text;
  GtkWidget *tab;
  GtkWidget *view;
  GtkTextBuffer *buffer;
  GtkTextIter start, match_start, match_end;
  
  text = gtk_entry_get_text(entry);
  
  if('\0' == text[0])
  {
    return;
  }
  
  priv = example_app_window_get_instance_private(win);
  
  tab - gtk_stack_get_visible_child(GTK_STACK(priv
    ->stack));
  view = gtk_bin_get_child(GTK_BIN(tab));
  buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(view));
  
  /* Very simple-minded search implementation */
  gtk_text_buffer_get_start_iter(buffer, &start);
  if(gtk_text_iter_forward_search(&start, text, 
    GTK_TEXT_SEARCH_CASE_INSENSITIVE, &match_start, 
    &match_end, NULL))
  {
    gtk_text_buffer_select_range(buffer, &match_start, 
      &match_end);
    gtk_text_view_scroll_to_iter(GTK_TEXT_VIEW(view), 
      &match_start, 0.0, FALSE, 0.0, 0.0);
  }
}

static void example_app_window_init(ExampleAppWindow *win)
{
  ExampleAppWindowPrivate *priv;
  GtkBuilder *builder;
  GMenuModel *menu;
  GAction *action;
  
  priv = example_app_window_get_instance_private(win);
  gtk_widget_init_template(GTK_WIDGET(win));
  priv->settings = g_settings_new("org.gtk.exampleapp");
  
  g_settings_bind(priv->settings, "transition", 
    priv->stack, "transition-type", 
    G_SETTINGS_BIND_DEFAULT);
  
  builder = gtk_builder_new_from_resource(
    "/org/gtk/exampleapp/gears-menu.ui");
  menu = G_MENU_MODEL(gtk_builder_get_object(builder, 
    "menu"));
  gtk_menu_button_set_menu_model(GTK_MENU_BUTTON(priv
    ->gears), menu);
  g_object_unref(builder);
  
  action = g_settings_create_action(priv->settings, 
    "show-words");
  g_action_map_add_action(G_ACTION_MAP(win), action);
  g_object_unref(action);
  action = (GAction*)g_property_action_new("show-lines", 
    priv->lines, "visible");
  g_action_map_add_action(G_ACTION_MAP(win), action);
  g_object_unref(action);
  
  g_object_bind_property(priv->lines, "visible", 
    priv->lines_label, "visible", G_BINDING_DEFAULT);
}

static void example_app_window_class_init(ExampleAppWindowClass *class)
{
  gtk_widget_class_set_template_from_resource(
    GTK_WIDGET_CLASS(class), 
    "/org/gtk/exampleapp/window.ui");
  
  gtk_widget_class_bind_template_child_private(
    GTK_WIDGET_CLASS(class), ExampleAppWindow, stack);
  
  gtk_widget_class_bind_template_callback(GTK_WIDGET_CLASS(
    class), search_text_changed);
  /*gtk_widget_class_bind_template_callback(GTK_WIDGET_CLASS(
    class), visible_child_changed);*/
}

ExampleAppWindow* example_app_window_new(ExampleApp *app)
{
  return g_object_new(EXAMPLE_APP_WINDOW_TYPE, 
    "application", app, NULL);
}

void example_app_window_open(ExampleAppWindow *win, 
  GFile *file)
{
  ExampleAppWindowPrivate *priv;
  gchar *basename;
  GtkWidget *scrolled, *view;
  gchar *contents;
  gsize length;
  
  priv = example_app_window_get_instance_private(win);
  basename = g_file_get_basename(file);
  scrolled = gtk_scrolled_window_new(NULL, NULL);
  gtk_widget_show(scrolled);
  gtk_widget_set_hexpand(scrolled, TRUE);
  gtk_widget_set_vexpand(scrolled, TRUE);
  view = gtk_text_view_new();
  gtk_text_view_set_editable(GTK_TEXT_VIEW(view), FALSE);
  gtk_text_view_set_cursor_visible(GTK_TEXT_VIEW(view), 
    FALSE);
  gtk_widget_show(view);
  gtk_container_add(GTK_CONTAINER(scrolled), view);
  gtk_stack_add_titled(GTK_STACK(priv->stack), scrolled, 
    basename, basename);
  
  if(g_file_load_contents(file, NULL, &contents, &length, 
    NULL, NULL))
  {
    GtkTextBuffer *buffer;
    
    buffer = gtk_text_view_get_buffer(GTK_TEXT_VIEW(view));
    gtk_text_buffer_set_text(buffer, contents, length);
    g_free(contents);
  }
  
  g_free(basename);
}

