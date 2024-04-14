package com.rhcloud.javawojtekel.pinezki;

import java.io.IOException;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@ISecured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class CRequestFilterPinezki 
  implements ContainerRequestFilter
{
  @Override
  public void filter(final ContainerRequestContext requestContext) 
    throws IOException
  {
    System.out.println("To działa!");
    final String cookie = requestContext.getHeaderString(HttpHeaders.COOKIE);
    if(null == cookie)
    {
      requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
        .build());
      return ;
    }
  }
}

