package com.microsoft.hackathon.quarkus;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/* 
* The Demo resource should be mapped to the root path.
* 
* Create a GET operation to return the value of a key passed as query parameter in the request. 
* 
* If the key is not passed, return "key not passed".
* If the key is passed, return "hello <key>".
* 
*/
@Path("/")
public class DemoResource {

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String demo(@QueryParam("key") String key) {
        if (key == null) {
            return "key not passed";
        } else {
            return "hello " + key;
        }
    }

    //create a New operation under /diffdates that calculates the difference between two dates. The operation should receive two dates as parameter in format dd-MM-yyyy and return the difference in days
    @GET
    @Path("/diffdates")
    @Produces(MediaType.TEXT_PLAIN)
    public String diffDates(@QueryParam("date1") String date1, @QueryParam("date2") String date2) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");
        java.time.LocalDate d1 = java.time.LocalDate.parse(date1, formatter);
        java.time.LocalDate d2 = java.time.LocalDate.parse(date2, formatter);
        long diff = java.time.temporal.ChronoUnit.DAYS.between(d1, d2);
        return "Difference between " + date1 + " and " + date2 + " is " + diff + " days";
    }
}
