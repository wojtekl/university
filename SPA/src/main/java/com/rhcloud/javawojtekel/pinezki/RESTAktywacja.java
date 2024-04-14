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

@Path(Serwer.APLIKACJA_NAZWA + "/aktywacja")
public class RESTAktywacja
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
  public String post(@FormParam("paramter") final String parametr)
  {
  }
  */
  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  public String aktywuj(@Context final HttpServletRequest request, 
    @FormParam("email") final String eMail, 
    @FormParam("czaswaznosci") final long czasWaznosci)
  {
    if(
      (7 > eMail.length()) 
      || (255 < eMail.length()) 
      || (eMail.matches(".*\\s.*")) 
      || (0 > czasWaznosci) 
    )
    {
      return "[false]";
    }
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
        "FROM ModelUzytkownikNowy WHERE email = :email");
      query.setParameter("email", eMail);
      final List<ModelUzytkownikNowy> uzytkownicyNowi = query.list();
      if(1 > uzytkownicyNowi.size())
      {
        return "[false]";
      }
      final ModelUzytkownikNowy modelNowy = uzytkownicyNowi.get(0);
      final ModelUzytkownik model = new ModelUzytkownik();
      model.setEmail(modelNowy.getEmail());
      model.setHaslo(modelNowy.getHaslo());
      model.setCzasStworzenia(modelNowy.getCzasStworzenia());
      model.setCzasWaznosci(Calendar.getInstance().getTimeInMillis() + czasWaznosci);
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
  /*
  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  public String delete(@FormParam("paramter") final String parametr)
  {
  }
  */
}

