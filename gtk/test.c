#include <gtk/gtk.h>
#include <glib.h>

GtkWidget *kontener;
int koniec;

void wyswietl_polozenie(GtkWindow *okno, GdkEvent *zdarzenie, gpointer dane)
{
    int x, y;
    char polozenie[10];
    x = zdarzenie -> configure.x;
    y = zdarzenie -> configure.y;
    sprintf(polozenie, "%d, %d", x, y);
    gtk_window_set_title(okno, polozenie);
}

void test(GtkWindow *okno, GdkEvent *zdarzenie, gpointer dane)
{
    koniec = 0;
    char polozenie[10];
    sprintf(polozenie, "%d", koniec);
    gtk_window_set_title(okno, polozenie);
}

gboolean mycha(GtkWidget *okno, GdkEvent *event, gpointer dane)
{
    char polozenie2[10];
    //sprintf(polozenie2 "mycha");
    gtk_window_set_title((GtkWindow*)okno, "mycha");
    if(koniec==1){
    char polozenie[10];
    GdkEventMotion* e=(GdkEventMotion*)event;
    sprintf(polozenie, "%d, %d", (guint)e->x, (guint)e->y);
    //gtk_window_set_title((GtkWindow*)okno, polozenie);
}
    GdkEventMotion* e=(GdkEventMotion*)event;
    gtk_fixed_move((GtkFixed*)kontener, dane, (guint)e->x, (guint)e->y);
    /*if(koniec)
    {
        GdkEventMotion* e=(GdkEventMotion*)event;
    //     printf("Coordinates: (%u,%u)\n", (guint)e->x,(guint)e->y);
    gtk_fixed_move((GtkFixed*)kontener, okno, (guint)e->x, (guint)e->y);
    }*/
    return TRUE;
}

gboolean przesun(GtkWidget *okno, gpointer dane)
{
    gtk_window_set_title(dane, "przesun");
    
    int ii = g_signal_connect(G_OBJECT(dane), "motion-notify-event", G_CALLBACK(mycha), okno);
    if(koniec){
        gtk_signal_disconnect(ii);
        koniec = 0;
    }
    else
        {koniec = 1;}
    //gtk_widget_set_events(dane, GDK_POINTER_MOTION_MASK|GDK_BUTTON_PRESS_MASK);
    return TRUE;
}

void rele(GtkWidget *okno, gpointer dane)
{
    koniec = 0;
    
    gtk_window_set_title(dane, "rele");
}



gboolean mysz(GtkWidget *okno, GdkEvent *event, gpointer dane)
{   
    char polozenie[10];
    GdkEventMotion* e=(GdkEventMotion*)event;
    sprintf(polozenie, "%d, %d", (guint)e->x, (guint)e->y);
    gtk_window_set_title((GtkWindow*)okno, polozenie);
    /*if(koniec)
    {
        //GdkEventMotion* e=(GdkEventMotion*)event;
    //     printf("Coordinates: (%u,%u)\n", (guint)e->x,(guint)e->y);
    gtk_fixed_move((GtkFixed*)kontener, dane, (guint)e->x, (guint)e->y);
    }*/
    return TRUE;
}

void dodaj_przycisk(GtkWidget *okno, gpointer dane)
{
    /*GtkWidget *dialog;
    dialog = gtk_message_dialog_new(GTK_WINDOW(dane), GTK_DIALOG_DESTROY_WITH_PARENT, GTK_MESSAGE_ERROR, GTK_BUTTONS_OK, "teskt %s", "nowy");
    gtk_window_set_title(GTK_WINDOW(dialog), "tytuł");
    gtk_dialog_run(GTK_DIALOG(dialog));
    gtk_widget_destroy(dialog);*/
    GtkWidget *p = gtk_button_new_with_label("button 1");
    gtk_widget_set_size_request(p, 180, 35);
    gtk_fixed_put(GTK_FIXED(GTK_WINDOW(kontener)), p, 200, 100);
    g_signal_connect(G_OBJECT(p), "pressed", G_CALLBACK(przesun), dane);
    g_signal_connect(G_OBJECT(p), "released", G_CALLBACK(rele), dane);
    //g_signal_connect(G_OBJECT(p), "motion-notify-event", G_CALLBACK(mycha), dane);
    //gtk_widget_set_events(p, GDK_POINTER_MOTION_MASK);
    gtk_widget_show(p);
}

int main(int argc, char *argv[])
{
    GtkWidget *okno;
    
    
    GtkWidget *przycisk;
    
    gtk_init(&argc, &argv);
    okno = gtk_window_new(GTK_WINDOW_TOPLEVEL);
    gtk_window_set_default_size(GTK_WINDOW(okno), 300, 200);
    gtk_window_set_position(GTK_WINDOW(okno), GTK_WIN_POS_CENTER);
    gtk_window_set_title(GTK_WINDOW(okno), "Nasz program");
    
    kontener = gtk_fixed_new();
    gtk_container_add(GTK_CONTAINER(okno), kontener);
    
    przycisk = gtk_button_new_with_label("Twój pierwszy przycisk");
    gtk_widget_set_size_request(przycisk, 180, 35);
    gtk_fixed_put(GTK_FIXED(kontener), przycisk, 10, 10);
    
    g_signal_connect(G_OBJECT(okno), "configure-event", G_CALLBACK(wyswietl_polozenie), NULL);
    g_signal_connect(G_OBJECT(przycisk), "released", G_CALLBACK(dodaj_przycisk), (gpointer)okno);
    
    g_signal_connect(G_OBJECT(okno), "button-release-event", G_CALLBACK(test), (gpointer)okno);
    
    //g_signal_connect(G_OBJECT(okno), "motion-notify-event", G_CALLBACK(mycha), przycisk);
    
  gtk_widget_set_events(okno, GDK_POINTER_MOTION_MASK|GDK_BUTTON_PRESS_MASK|GDK_BUTTON_RELEASE_MASK);
    
    gtk_widget_show_all(okno);
    gtk_main();
    
    g_signal_connect(G_OBJECT(okno), "destroy", G_CALLBACK(gtk_main_quit), NULL);
    
    return 0;
}
