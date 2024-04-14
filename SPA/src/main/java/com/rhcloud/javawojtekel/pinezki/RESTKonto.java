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
import org.mindrot.jbcrypt.BCrypt;

@Path(Serwer.APLIKACJA_NAZWA + "/konto")
public class RESTKonto
{
  /*
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String get(@QueryParam("parametr") final int parametr)
  {
    return "";
  }
  
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public String post(@FormParam("parametr") final String parametr)
  {
  }
  */
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  public String wejdz(@Context final HttpServletRequest request, 
    @FormParam("email") final String eMail, 
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
        "FROM ModelUzytkownik WHERE email = :email");
      query.setParameter("email", eMail);
      final List<ModelUzytkownik> uzytkownicy = query.list();
      session.close();
      final ModelUzytkownik model = uzytkownicy.get(0);
      if(
        (1 != uzytkownicy.size()) 
        || (!BCrypt.checkpw(haslo, model.getHaslo())) 
        || (model.getCzasWaznosci() < Calendar.getInstance().getTimeInMillis())
      )
      {
        return "[]";
      }
      final HttpSession httpSession = request.getSession();
      if(null == httpSession)
      {
        return "[]";
      }
      httpSession.setAttribute("uzytkownik", model.getIdentyfikator());
      return new StringBuilder()
        .append("[")
        .append(model.getIdentyfikator())
        .append(",\"")
        .append(httpSession.getId())
        .append("\",")
        .append(1800000)
        .append("]")
        .toString();
    }
    catch(final Exception exception)
    {
      return "[]";
    }
  }
  
  @ISecured
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  public String wyjdz(@Context final HttpServletRequest request, 
    @FormParam("czaswyjscia") final long czasWyjscia)
  {
    int identyfikator = -1;
    final HttpSession httpSession = request.getSession(false);
    if(null != httpSession)
    {
      final Integer u = (Integer)httpSession.getAttribute("uzytkownik");
      if(null != u)
      {
        identyfikator = u;
      }
    }
    if(0 > identyfikator)
    {
      return "[false]";
    }
    httpSession.invalidate();
    return "[true]";
  }
}

