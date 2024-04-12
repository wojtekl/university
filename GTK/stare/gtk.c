#include <gtk/gtk.h>

GtkWidget *okno;
GtkWidget *kontener;
int koniec;
int yy;

int sx, sy;

static gboolean button_press_event(GtkWidget *widget, GdkEventButton *event)
{
	if(event->button == 1) printf("ok");
	sx = (int) event->x;
	sy = (int) event->y;
	return TRUE;
}

static gboolean motion_notify_event(GtkWidget *widget, GdkEventMotion *event)
{
	int x, y;
	GdkModifierType state;
	gdk_window_get_pointer(gtk_widget_get_window(GTK_WIDGET(okno)), &x, &y, &state);
	if(state&GDK_BUTTON1_MASK)
	{
		gtk_fixed_move((GtkFixed*)kontener, (GtkWidget*)widget, x-sx, y-sy);
	}
	return TRUE;
}

gboolean przesun_widget(GtkWidget *w, GdkEvent *e, gpointer d)
{
    GdkEventMotion *event = (GdkEventMotion*)e;
    if (event->state == GDK_BUTTON1_MASK)
 //   if(koniec)
   {
        char polozenie[10];
        
        sprintf(polozenie, "%d, %d", (guint)event->x, (guint)event->y);
        gtk_window_set_title((GtkWindow*)okno, polozenie);
        gtk_fixed_move((GtkFixed*)kontener, w, (guint)event->x, (guint)event->y);
    }
    if (event->state == GDK_BUTTON_RELEASE_MASK)
        {
        gtk_window_set_title((GtkWindow*)okno, "test");
        g_signal_handler_disconnect(okno, yy);
        yy = 0 ;
    }
    return TRUE;
}

void wez_widget(GtkWidget *w, GdkEvent *e, gpointer d)
{
    //GdkEventMotion *event = (GdkEventMotion*)e;
    //gtk_fixed_move((GtkFixed*)kontener, d, (guint)e->x, (guint)e->y);
    koniec = 1;
    yy = g_signal_connect(G_OBJECT(okno), "motion_notify_event", G_CALLBACK(motion_notify_event), w);
}

void pusc_widget(GtkWidget *w, gpointer d)
{
    koniec = 0;
   //g_signal_handler_disconnect(okno, yy);
}

void dodaj_widget(GtkWidget *w, GdkEvent *e, gpointer d)
{
    GtkWidget *widget = gtk_button_new_with_label("Przycisk");
    gtk_widget_set_size_request(widget, 180, 35);
    gtk_fixed_put(GTK_FIXED(d), widget, 200, 100);
    g_signal_connect(G_OBJECT(widget), "button_press_event", G_CALLBACK(button_press_event), d);
gtk_widget_set_events(widget, GDK_EXPOSURE_MASK
			 | GDK_LEAVE_NOTIFY_MASK
			 | GDK_BUTTON_PRESS_MASK
			 | GDK_POINTER_MOTION_MASK
			 | GDK_POINTER_MOTION_HINT_MASK); 
    //g_signal_connect(G_OBJECT(widget), "button_release_event", G_CALLBACK(pusc_widget), d);
	g_signal_connect(G_OBJECT(widget), "motion_notify_event", G_CALLBACK(motion_notify_event), w);
printf("ok");
    gtk_widget_show(widget);
}

int main(int argc, char *argv[])
{
    gtk_init(&argc, &argv);
    
    okno = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_default_size(GTK_WINDOW(okno), 400, 400);
    gtk_window_set_position(GTK_WINDOW(okno), GTK_WIN_POS_CENTER);
    gtk_window_set_title(GTK_WINDOW(okno), "Projektor Gtk");
    g_signal_connect(G_OBJECT(okno), "destroy", G_CALLBACK(gtk_main_quit), NULL);
    
    kontener = gtk_fixed_new();
    gtk_container_add(GTK_CONTAINER(okno), kontener);
    
    GtkWidget *przycisk = gtk_button_new_with_label("Przycisk");
    gtk_widget_set_size_request(przycisk, 180, 35);
    gtk_fixed_put(GTK_FIXED(kontener), przycisk, 10, 10);
    g_signal_connect(G_OBJECT(przycisk), "clicked", G_CALLBACK(dodaj_widget), kontener);
    gtk_widget_set_events(okno, GDK_BUTTON1_MOTION_MASK|GDK_BUTTON_RELEASE_MASK);
	
	gtk_widget_set_events(okno, GDK_EXPOSURE_MASK
			 | GDK_LEAVE_NOTIFY_MASK
			 | GDK_BUTTON_PRESS_MASK
			 | GDK_POINTER_MOTION_MASK
			 | GDK_POINTER_MOTION_HINT_MASK); 
    
    gtk_widget_show_all(okno);
    gtk_main();
    
    return 0;
}
