#ifndef EXAMPLEAPPPREFS_H
#define EXAMPLEAPPPREFS_H

#include <gtk/gtk.h>

#define EXAMPLE_APP_PREFS_TYPE (example_app_prefs_get_type())

G_DECLARE_FINAL_TYPE(ExampleAppPrefs, example_app_prefs, 
  EXAMPLE, APP_PREFS, GtkApplicationWindow)

ExampleAppPrefs* example_app_prefs_new(ExampleAppWindow 
  *win);

#endif

