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

@Path("pinezki/grupa")
public class Grupa
{
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String grupaPobierz(@QueryParam("identyfikator") final int identyfikator)
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
      final Query query = session.createQuery("FROM GrupaModel WHERE identyfikator = :identyfikator");
      query.setParameter("identyfikator", identyfikator);
      final List<GrupaModel> grupy = query.list();
      session.close();
      return grupy.get(0).toJSON();
    }
    catch(final Exception exception)
    {
      return "null";
    }
  }
  
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public String grupaUaktualnij(@FormParam("identyfikator") final int identyfikator, 
    @FormParam("nazwa") final String nazwa)
  {
    if(nazwa.isEmpty())
    {
      return "null";
    }
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration().configure();
      configuration.addClass(GrupaModel.class);
      final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties());
      final Session session = configuration.buildSessionFactory(builder.build()).openSession();
      final GrupaModel model = session.load(GrupaModel.class, identyfikator);
      model.setNazwa(nazwa);
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
  public String grupaStworz(@FormParam("nazwa") final String nazwa)
  {
    final GrupaModel model = new GrupaModel();
    model.setNazwa(nazwa);
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration().configure();
      configuration.addClass(GrupaModel.class);
      final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties());
      final Session session = configuration.buildSessionFactory(builder.build()).openSession();
      final Transaction transaction = session.beginTransaction();
      session.save(model);
      transaction.commit();
      session.close();
    }
    catch(final Exception exception)
    {
      return "null";
    }
    return model.toJSON();
  }
  
  @DELETE
  @Produces(MediaType.TEXT_PLAIN)
  public String grupaUsun(@FormParam("identyfikator") final int identyfikator)
  {
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration().configure();
      configuration.addClass(GrupaModel.class);
      final StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
        .applySettings(configuration.getProperties());
      final Session session = configuration.buildSessionFactory(builder.build()).openSession();
      final Transaction transaction = session.beginTransaction();
      session.delete(session.load(GrupaModel.class, identyfikator));
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

