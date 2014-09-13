package com.example;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

@Provider
public class ResponseCorsFilter
implements ContainerResponseFilter {
 
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
 
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
 
        headers.add("Access-Control-Allow-Origin", "*");
        //headers.add("Access-Control-Allow-Origin", "http://podcastpedia.org"); //allows CORS requests only coming from podcastpedia.org        
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");            
        headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
	    System.out.println("Headers after adding: " + headers);

    }
 
}

//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.ResponseBuilder;
//
//import com.sun.jersey.spi.container.ContainerRequest;
//import com.sun.jersey.spi.container.ContainerResponse;
//import com.sun.jersey.spi.container.ContainerResponseFilter;
//
//public class ResponseCorsFilter implements ContainerResponseFilter {
//
//	
//	public ContainerResponse filter(ContainerRequest req,
//			ContainerResponse contResp) {
//
//		ResponseBuilder resp = Response.fromResponse(contResp.getResponse());
//		resp.header("Access-Control-Allow-Origin", "*").header(
//				"Access-Control-Allow-Methods", "GET, POST, OPTIONS");
//
//		String reqHead = req.getHeaderValue("Access-Control-Request-Headers");
//
//		if (null != reqHead && !reqHead.equals("")) {
//			resp.header("Access-Control-Allow-Headers", reqHead);
//		}
//
//		contResp.setResponse(resp.build());
//		return contResp;
//	}
//
//}