package com.rhcloud.javawojtekel.pinezki;

import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

@Path("pinezki/uzytkownikgrupa")
public class UzytkownikGrupa
{
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String uzytkownikGrupaPobierz(@QueryParam("uzytkownik") final int uzytkownik)
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration().configure();
      configuration.addClass(GrupaModel.class);
      configuration.addClass(ModelUzytkownik.class);
      configuration.addClass(UzytkownikGrupaModel.class);
      final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties());
      final Session session = configuration.buildSessionFactory(builder.build()).openSession();
      final Query query = session
        .createQuery("SELECT g FROM UzytkownikGrupaModel ug JOIN ug.grupa g WHERE ug.uzytkownik = :uzytkownik");
      query.setParameter("uzytkownik", session.load(ModelUzytkownik.class, uzytkownik));
      final List<GrupaModel> grupy = query.list();
      session.close();
      final StringBuilder stringBuilder = new StringBuilder()
        .append("[")
        .append(grupy.get(0).toJSON());
      final int l = grupy.size();
      for(int i = 1; i < l; ++i)
      {
        stringBuilder
          .append(",")
          .append(grupy.get(i).toJSON());
      }
      return stringBuilder
        .append("]")
        .toString();
    }
    catch(final Exception exception)
    {
      return "null";
    }
  }
  
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public String uzytkownikGrupaUaktualnij(@FormParam("grupa") final int grupa, 
    @FormParam("uzytkownik") final int uzytkownik)
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration().configure();
      configuration.addClass(ModelUzytkownik.class);
      configuration.addClass(GrupaModel.class);
      configuration.addClass(UzytkownikGrupaModel.class);
      final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties());
      final Session session = configuration.buildSessionFactory(builder.build()).openSession();
      final Query query = session
        .createQuery("SELECT u FROM UzytkownikGrupaModel ug JOIN ug.uzytkownik u WHERE ug.grupa = :grupa and ug.uzytkownik != :uzytkownik");
      query.setParameter("grupa", session.load(GrupaModel.class, grupa));
      query.setParameter("uzytkownik", session.load(ModelUzytkownik.class, uzytkownik));
      final List<ModelUzytkownik> uzytkownicy = query.list();
      session.close();
      final StringBuilder stringBuilder = new StringBuilder()
        .append("[")
        .append(uzytkownicy.get(0).toJSON());
      final int l = uzytkownicy.size();
      for(int i = 1; i < l; ++i)
      {
        stringBuilder
          .append(",")
          .append(uzytkownicy.get(i).toJSON());
      }
      return stringBuilder
        .append("]")
        .toString();
    }
    catch(final Exception exception)
    {
      return "null";
    }
  }
  
  @PUT
  @Produces(MediaType.TEXT_PLAIN)
  public String wiadomoscStworz(@FormParam("uzytkownik") final int uzytkownik, 
    @FormParam("grupa") final int grupa)
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration().configure();
      configuration.addClass(UzytkownikGrupaModel.class);
      configuration.addClass(ModelUzytkownik.class);
      configuration.addClass(GrupaModel.class);
      final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties());
      final Session session = configuration.buildSessionFactory(builder.build()).openSession();
      final UzytkownikGrupaModel model = new UzytkownikGrupaModel();
      model.setUzytkownik(session.load(ModelUzytkownik.class, uzytkownik));
      model.setGrupa(session.load(GrupaModel.class, grupa));
      final Transaction transaction = session.beginTransaction();
      session.save(model);
      transaction.commit();
      session.close();
      return "1";
    }
    catch(Exception exception)
    {
      return "null";
    }
  }
  
  @DELETE
  @Produces(MediaType.TEXT_PLAIN)
  public String uzytkownikUsun(@FormParam("grupa") final int grupa, 
    @FormParam("uzytkownik") final int uzytkownik)
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration().configure();
      configuration.addClass(UzytkownikGrupaModel.class);
      final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties());
      final Session session = configuration.buildSessionFactory(builder.build()).openSession();
      final Query query = session
        .createQuery("DELETE UzytkownikGrupaModel WHERE grupa = :grupa AND uzytkownik = :uzytkownik");
      query.setParameter("grupa", grupa);
      query.setParameter("uzytkownik", uzytkownik);
      final Transaction transaction = session.beginTransaction();
      query.executeUpdate();
      transaction.commit();
      session.close();
    }
    catch(final Exception exception)
    {
      return "false";
    }
    return "true";
  }
}

