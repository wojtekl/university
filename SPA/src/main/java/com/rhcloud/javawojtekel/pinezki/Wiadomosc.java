package com.rhcloud.javawojtekel.pinezki;

import java.util.Calendar;
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

@Path("pinezki/wiadomosc")
public class Wiadomosc
{
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String wiadomoscPobierz(@QueryParam("grupa") final int grupa)
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration().configure();
      configuration.addClass(WiadomoscModel.class);
      configuration.addClass(ModelUzytkownik.class);
      configuration.addClass(GrupaModel.class);
      final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties());
      final Session session = configuration.buildSessionFactory(builder.build()).openSession();
      final Query query = session.createQuery("FROM WiadomoscModel WHERE grupa = :grupa");
      query.setParameter("grupa", session.load(GrupaModel.class, grupa));
      final List<WiadomoscModel> wiadomosci = query.list();
      session.close();
      final StringBuilder stringBuilder = new StringBuilder()
        .append("[")
        .append(wiadomosci.get(0).toJSON());
      final int l = wiadomosci.size();
      for(int i = 1; i < l; ++i)
      {
        stringBuilder
          .append(",")
          .append(wiadomosci.get(i).toJSON());
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
  public String wiadomoscUaktualnij(@FormParam("identyfikator") final int identyfikator, 
    @FormParam("uzytkownik") final int uzytkownik, @FormParam("grupa") final int grupa, 
    @FormParam("tresc") final String tresc)
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration().configure();
      configuration.addClass(WiadomoscModel.class);
      configuration.addClass(ModelUzytkownik.class);
      configuration.addClass(GrupaModel.class);
      final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties());
      final Session session = configuration.buildSessionFactory(builder.build()).openSession();
      final WiadomoscModel model = session.load(WiadomoscModel.class, identyfikator);
      if(-1 < uzytkownik)
      {
        model.setUzytkownik(session.load(ModelUzytkownik.class, uzytkownik));
      }
      if(-1 < grupa)
      {
        model.setGrupa(session.load(GrupaModel.class, grupa));
      }
      if(!tresc.isEmpty())
      {
        model.setTresc(tresc);
      }
      model.setCzas(Calendar.getInstance().getTime());
      final Transaction transaction = session.beginTransaction();
      session.update(model);
      transaction.commit();
      session.close();
      return model.toJSON();
    }
    catch(final Exception exception)
    {
      return "null";
    }
  }
  
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  public String wiadomoscStworz(@FormParam("uzytkownik") final int uzytkownik, 
    @FormParam("grupa") final int grupa, @FormParam("tresc") final String tresc)
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration().configure();
      configuration.addClass(WiadomoscModel.class);
      configuration.addClass(ModelUzytkownik.class);
      configuration.addClass(GrupaModel.class);
      final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties());
      final Session session = configuration.buildSessionFactory(builder.build()).openSession();
      final WiadomoscModel model = new WiadomoscModel();
      model.setUzytkownik(session.load(ModelUzytkownik.class, uzytkownik));
      model.setGrupa(session.load(GrupaModel.class, grupa));
      model.setTresc(tresc);
      model.setCzas(Calendar.getInstance().getTime());
      Transaction transaction = session.beginTransaction();
      session.save(model);
      transaction.commit();
      session.close();
      return model.toJSON();
    }
    catch(final Exception exception)
    {
      return "null";
    }
  }
  
  @DELETE
  @Produces(MediaType.TEXT_PLAIN)
  public String wiadomoscUsun(@FormParam("identyfikator") final int identyfikator)
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration().configure();
      configuration.addClass(WiadomoscModel.class);
      final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties());
      final Session session = configuration.buildSessionFactory(builder.build()).openSession();
      final Transaction transaction = session.beginTransaction();
      session.delete(session.load(WiadomoscModel.class, identyfikator));
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

