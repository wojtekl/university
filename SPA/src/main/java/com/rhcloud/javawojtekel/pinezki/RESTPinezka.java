package com.rhcloud.javawojtekel.pinezki;

// import java.net.URLDecoder;
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
@Path(Serwer.APLIKACJA_NAZWA + "/pinezka")
public class RESTPinezka
{
  // private static final String UTF8 = "UTF-8";
  // private static final String ISO88591 = "ISO-8859-1";
  
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String pobierz(@Context final HttpServletRequest request)
  {
    final int uzytkownik = sessionAttributeUzytkownik(request);
    if(0 > uzytkownik)
    {
      return "[]";
    }
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration()
        .configure();
      configuration.addClass(ModelPinezka.class);
      configuration.addClass(ModelUzytkownik.class);
      final StandardServiceRegistryBuilder builder = 
        new StandardServiceRegistryBuilder().applySettings(
          configuration.getProperties());
      final Session session = configuration.buildSessionFactory(
        builder.build()).openSession();
      final Query query = session.createQuery(
        "FROM ModelPinezka WHERE uzytkownik.identyfikator = :uzytkownik");
      query.setParameter("uzytkownik", uzytkownik);
      final List<ModelPinezka> pinezki = query.list();
      session.close();
      final StringBuilder stringBuilder = new StringBuilder()
        .append("[");
      final int l = pinezki.size();
      if(0 < l)
      {
        stringBuilder.append(pinezki.get(0).toJSON());
      }
      for(int i = 1 ; i < l; ++i)
      {
        stringBuilder
          .append(",")
          .append(pinezki.get(i).toJSON());
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
    @FormParam("nazwa") final String nazwa, 
    @FormParam("szerokosc") final double szerokosc, 
    @FormParam("dlugosc") final double dlugosc)
  {
    final int uzytkownik = sessionAttributeUzytkownik(request);
    String nt = nazwa;
    /* try
    {
      nt = URLDecoder.decode(new String(nt.getBytes(ISO88591)), UTF8);
    }
    catch(final Exception exception)
    {
      return "[]";
    } */
    nt = nt.trim();
    if(
      (0 > uzytkownik) 
      || (0 > identyfikator) 
      || (nt.isEmpty())
    )
    {
      return "[]";
    }
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration()
        .configure();
      configuration.addClass(ModelPinezka.class);
      configuration.addClass(ModelUzytkownik.class);
      final StandardServiceRegistryBuilder builder = 
        new StandardServiceRegistryBuilder().applySettings(
          configuration.getProperties());
      final Session session = configuration.buildSessionFactory(
        builder.build()).openSession();
      final ModelPinezka model = session.load(ModelPinezka.class, 
        identyfikator);
      if(
        (model.getUzytkownik().getIdentyfikator() != uzytkownik) 
      )
      {
        session.close();
        return "[]";
      }
      if(!model.getNazwa().equals(nt))
      {
        model.setNazwa(nt);
      }
      if(model.getSzerokosc() != szerokosc)
      {
        model.setSzerokosc(szerokosc);
      }
      if(model.getDlugosc() != dlugosc)
      {
        model.setDlugosc(dlugosc);
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
    @FormParam("nazwa") final String nazwa, 
    @FormParam("szerokosc") final double szerokosc, 
    @FormParam("dlugosc") final double dlugosc)
  {
    final int uzytkownik = sessionAttributeUzytkownik(request);
    String nt = nazwa;
    /* try
    {
      nt = URLDecoder.decode(new String(nt.getBytes(ISO88591)), UTF8);
    }
    catch(final Exception exception)
    {
      return "[]";
    } */
    nt = nt.trim();
    if(
      (0 > uzytkownik) 
      || (nt.isEmpty())
    )
    {
      return "[]";
    }
    final ModelPinezka model = new ModelPinezka();
    model.setNazwa(nt);
    model.setSzerokosc(szerokosc);
    model.setDlugosc(dlugosc);
    model.setCzasAktualizacji(Calendar.getInstance().getTimeInMillis());
    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      final Configuration configuration = new Configuration()
        .configure();
      configuration.addClass(ModelPinezka.class);
      configuration.addClass(ModelUzytkownik.class);
      final StandardServiceRegistryBuilder builder = 
        new StandardServiceRegistryBuilder().applySettings(
          configuration.getProperties());
      final Session session = configuration.buildSessionFactory(
        builder.build()).openSession();
      final Query query = session.createQuery(
        "FROM ModelPinezka WHERE uzytkownik.identyfikator = :uzytkownik");
      query.setParameter("uzytkownik", uzytkownik);
      final List<ModelPinezka> pinezki = query.list();
      for(int i = 0, l = pinezki.size(); i < l; ++i)
      {
        final ModelPinezka pinezka = pinezki.get(i);
        if(
          (pinezka.getNazwa().equals(model.getNazwa())) 
          || (
            (pinezka.getSzerokosc() == model.getSzerokosc()) 
            && (pinezka.getDlugosc() == model.getDlugosc())
          )
        )
        {
          session.close();
          return "[]";
        }
      }
      model.setUzytkownik(session.load(ModelUzytkownik.class, uzytkownik));
      final Transaction transaction = session.beginTransaction();
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
      configuration.addClass(ModelPinezka.class);
      configuration.addClass(ModelUzytkownik.class);
      final StandardServiceRegistryBuilder builder = 
        new StandardServiceRegistryBuilder().applySettings(
          configuration.getProperties());
      final Session session = configuration.buildSessionFactory(
        builder.build()).openSession();
      final ModelPinezka model = session.load(ModelPinezka.class, 
        identyfikator);
      if(model.getUzytkownik().getIdentyfikator() != uzytkownik)
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

