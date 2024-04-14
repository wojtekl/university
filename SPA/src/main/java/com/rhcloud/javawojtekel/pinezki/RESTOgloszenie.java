package com.rhcloud.javawojtekel.pinezki;

import java.util.Calendar;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.FormParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

@ISecured
@Path(Serwer.APLIKACJA_NAZWA + "/ogloszenie")
public class RESTOgloszenie
{
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String pobierz(@Context final HttpServletRequest request, 
    @QueryParam("liczba") final int liczba, @QueryParam("jezyk") final String jezyk)
  {
    final int uzytkownik = sessionAttributeUzytkownik(request);
    final String jt = jezyk.trim();
    if(
      (0 > uzytkownik) 
      || (1 > liczba) 
      || (jt.isEmpty())
    )
    {
      return "[]";
    }
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration()
        .configure();
      configuration.addClass(ModelOgloszenie.class);
      final StandardServiceRegistryBuilder builder = 
        new StandardServiceRegistryBuilder().applySettings(
          configuration.getProperties());
      final Session session = configuration.buildSessionFactory(
        builder.build()).openSession();
      final Query query = session.createQuery(
        "FROM ModelOgloszenie WHERE jezyk = :jezyk ORDER BY identyfikator DESC");
      query.setParameter("jezyk", jt);
      query.setMaxResults(liczba);
      final List<ModelOgloszenie> ogloszenia = query.list();
      session.close();
      final StringBuilder stringBuilder = new StringBuilder()
        .append("[");
      final int l = ogloszenia.size();
      if(0 < l)
      {
        stringBuilder.append(ogloszenia.get(0).toJSON());
      }
      for(int i = 1; i < l; ++i)
      {
        stringBuilder
          .append(",")
          .append(ogloszenia.get(i).toJSON());
      }
      return stringBuilder
        .append("]")
        .toString();
    }
    catch(final Exception exception)
    {
      return "[]";
    }
  }
  
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public String uaktualnij(@Context final HttpServletRequest request, 
    @FormParam("identyfikator") final int identyfikator, 
    @FormParam("tytul") final String tytul, 
    @FormParam("tresc") final String tresc)
  {
    final int uzytkownik = sessionAttributeUzytkownik(request);
    if(
      (0 > uzytkownik) 
      || (0 > identyfikator)
    )
    {
      return "[]";
    }
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration()
        .configure();
      configuration.addClass(ModelOgloszenie.class);
      final StandardServiceRegistryBuilder builder = 
        new StandardServiceRegistryBuilder().applySettings(
          configuration.getProperties());
      final Session session = configuration.buildSessionFactory(
        builder.build()).openSession();
      final ModelOgloszenie model = session.load(ModelOgloszenie.class, 
        identyfikator);
      if(!tytul.isEmpty())
      {
        model.setTytul(tytul);
      }
      if(!tresc.isEmpty())
      {
        model.setTresc(tresc);
      }
      model.setCzasAktualizacji(Calendar.getInstance().getTimeInMillis());
      final Transaction transaction = session.beginTransaction();
      session.update(model);
      transaction.commit();
      session.close();
      return model.toJSON();
    }
    catch(final Exception exception)
    {
      return "[]";
    }
  }
  
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  public String stworz(@Context final HttpServletRequest request, 
    @FormParam("tytul") final String tytul, 
    @FormParam("tresc") final String tresc, 
    @FormParam("jezyk") final String jezyk)
  {
    final int uzytkownik = sessionAttributeUzytkownik(request);
    final String jt = jezyk.trim();
    if(
      (0 > uzytkownik) 
      || (tytul.isEmpty()) 
      || (tresc.isEmpty()) 
      || (jt.isEmpty())
    )
    {
      return "[]";
    }
    final ModelOgloszenie model = new ModelOgloszenie();
    model.setTytul(tytul);
    model.setTresc(tresc);
    model.setJezyk(jt);
    model.setCzasAktualizacji(Calendar.getInstance().getTimeInMillis());
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration()
        .configure();
      configuration.addClass(ModelOgloszenie.class);
      final StandardServiceRegistryBuilder builder = 
        new StandardServiceRegistryBuilder().applySettings(
          configuration.getProperties());
      final Session session = configuration.buildSessionFactory(
        builder.build()).openSession();
      Transaction transaction = session.beginTransaction();
      session.save(model);
      transaction.commit();
      session.close();
      return model.toJSON();
    }
    catch(final Exception exception)
    {
      return "[]";
    }
  }
  
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  public String usun(@Context final HttpServletRequest request, 
    @FormParam("identyfikator") final int identyfikator)
  {
    final int uzytkownik = sessionAttributeUzytkownik(request);
    if(
      (0 > uzytkownik) 
      || (0 > identyfikator)
    )
    {
      return "[false]";
    }
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration()
        .configure();
      configuration.addClass(ModelOgloszenie.class);
      final StandardServiceRegistryBuilder builder = 
        new StandardServiceRegistryBuilder().applySettings(
          configuration.getProperties());
      final Session session = configuration.buildSessionFactory(
        builder.build()).openSession();
      final Transaction transaction = session.beginTransaction();
      session.delete(session.load(WiadomoscModel.class, identyfikator));
      transaction.commit();
      session.close();
      return "[true]";
    }
    catch(final Exception exception)
    {
      return "[false]";
    }
  }
  
  private int sessionAttributeUzytkownik(final HttpServletRequest request)
  {
    final HttpSession httpSession = request.getSession(false);
    if(null != httpSession)
    {
      final Integer u = (Integer)httpSession.getAttribute("uzytkownik");
      if(null != u)
      {
        return u;
      }
    }
    return -1;
  }
}

