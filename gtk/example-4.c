#include <gtk/gtk.h>

/* Surface to store current scribbles */
static cairo_surface_t *surface = NULL;

static void clear surface(void)
{
  cairo_t *cr;
  
  cr = cairo_create(surface);
  
  cairo_set_source_rgb(cr, 1, 1, 1);
  cairo_paint(cr);
  
  cairo_destroy(cr);
}

/* Create a new surface of the appropriate size to store our scribbles */
static gboolean configure_event_cb(GtkWidget *widget, 
  GdkEventConfigure *event, gpointer data)
{
  if(surface)
  {
    cairo_surface_destroy(surface);
  }
  
  surface = gdk_window_create_simmilar_surface(
    gtk_widget_get_window(widget), CAIRO_CONTENT_COLOR, 
    gtk_widget_get_allocated_width(widget), 
    gtk_widget_get_allocated_height(widget));
  
  /* Initialize the surface to white */
  clear_surface();
  
  /* We've handled the configure event, no need for further processing.  */
  return TRUE;
}

