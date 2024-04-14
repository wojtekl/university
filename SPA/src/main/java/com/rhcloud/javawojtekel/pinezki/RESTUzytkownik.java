package com.rhcloud.javawojtekel.pinezki;

import java.security.SecureRandom;
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
import org.mindrot.jbcrypt.BCrypt;

@Path(Serwer.APLIKACJA_NAZWA + "/uzytkownik")
public class RESTUzytkownik
{
  @ISecured
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String pobierz(@Context final HttpServletRequest request)
  {
    final int identyfikator = sessionAttributeUzytkownik(request);
    if(0 > identyfikator)
    {
      return "[]";
    }
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration()
        .configure();
      configuration.addClass(ModelUzytkownik.class);
      final StandardServiceRegistryBuilder builder = 
        new StandardServiceRegistryBuilder().applySettings(
          configuration.getProperties());
      final Session session = configuration.buildSessionFactory(
        builder.build()).openSession();
      final Query query = session.createQuery(
        "FROM ModelUzytkownik WHERE identyfikator = :identyfikator");
      query.setParameter("identyfikator", identyfikator);
      final List<ModelUzytkownik> uzytkownicy = query.list();
      session.close();
      return uzytkownicy.get(0).toJSON();
    }
    catch(final Exception exception)
    {
      return "[]";
    }
  }
  
  @ISecured
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public String uaktualnij(@Context final HttpServletRequest request, 
    @FormParam("haslo") final String haslo, 
    @FormParam("haslonowe") final String hasloNowe)
  {
    final int identyfikator = sessionAttributeUzytkownik(request);
    if(
      (0 > identyfikator) 
      || (8 > haslo.length()) 
      || (40 < haslo.length()) 
      || (haslo.matches(".*\\s+.*")) 
      || (8 > hasloNowe.length()) 
      || (40 < hasloNowe.length()) 
      || (hasloNowe.matches(".*\\s+.*"))
    )
    {
      return "[false]";
    }
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration()
        .configure();
      configuration.addClass(ModelUzytkownik.class);
      final StandardServiceRegistryBuilder builder = 
        new StandardServiceRegistryBuilder().applySettings(
          configuration.getProperties());
      final Session session = configuration.buildSessionFactory(
        builder.build()).openSession();
      final ModelUzytkownik model = session.load(ModelUzytkownik.class, 
        identyfikator);
      if(
        (null == model) 
        || (!BCrypt.checkpw(haslo, model.getHaslo())) 
      )
      {
        session.close();
        return "[false]";
      }
      model.setHaslo(BCrypt.hashpw(hasloNowe, BCrypt.gensalt()));
      final Transaction transaction = session.beginTransaction();
      session.update(model);
      transaction.commit();
      session.close();
      return "[true]";
    }
    catch(final Exception exception)
    {
      return "[false]";
    }
  }
  
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  public String stworz(@FormParam("email") final String eMail, 
    @FormParam("haslo") final String haslo)
  {
    if(
      (7 > eMail.length()) 
      || (255 < eMail.length()) 
      || (eMail.matches(".*\\s.*")) 
      || (8 > haslo.length()) 
      || (40 < haslo.length()) 
      || (haslo.matches(".*\\s+.*"))
    )
    {
      return "[false]";
    }
    final byte r[] = new byte[20];
    new SecureRandom().nextBytes(r);
    final char heks[] = "0123456789abcdef".toCharArray();
    final StringBuilder stringBuilder = new StringBuilder();
    for(int i = 0, l = r.length; i < l; ++i)
    {
      final byte b = r[i];
      stringBuilder
        .append(heks[(b >> 4) & 0x0f])
        .append(heks[b & 0x0f]);
    }
    final ModelUzytkownikNowy model = new ModelUzytkownikNowy();
    model.setEmail(eMail);
    model.setHaslo(BCrypt.hashpw(haslo, BCrypt.gensalt()));
    model.setKodAktywacyjny(stringBuilder.toString());
    model.setCzasStworzenia(Calendar.getInstance()
      .getTimeInMillis());
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration()
        .configure();
      configuration.addClass(ModelUzytkownikNowy.class);
      configuration.addClass(ModelUzytkownik.class);
      final StandardServiceRegistryBuilder builder = 
        new StandardServiceRegistryBuilder().applySettings(
          configuration.getProperties());
      final Session session = configuration.buildSessionFactory(
        builder.build()).openSession();
      final Query query = session.createQuery(
        "SELECT 1 FROM ModelUzytkownik WHERE email = :email");
      query.setParameter("email", eMail);
      if(null != query.uniqueResult())
      {
        session.close();
        return "[false]";
      }
      final Transaction transaction = session.beginTransaction();
      session.save(model);
      transaction.commit();
      session.close();
      return "[true]";
    }
    catch(final Exception exception)
    {
      return "[false]";
    }
  }
  
  @ISecured
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  public String usun(@Context final HttpServletRequest request, 
    @FormParam("haslo") final String haslo)
  {
    final int identyfikator = sessionAttributeUzytkownik(request);
    if(0 > identyfikator)
    {
      return "[false]";
    }
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration()
        .configure();
      configuration.addClass(ModelUzytkownik.class);
      final StandardServiceRegistryBuilder builder = 
        new StandardServiceRegistryBuilder().applySettings(
          configuration.getProperties());
      final Session session = configuration.buildSessionFactory(
        builder.build()).openSession();
      final ModelUzytkownik model = session.load(ModelUzytkownik.class, 
        identyfikator);
      if(
        (null == model) || 
        (!BCrypt.checkpw(haslo, model.getHaslo())) 
      )
      {
        session.close();
        return "[false]";
      }
      final Transaction transaction = session.beginTransaction();
      session.delete(model);
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

