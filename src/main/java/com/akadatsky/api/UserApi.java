package com.akadatsky.api;

import com.akadatsky.dao.UserDao;
import com.akadatsky.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/user")
public class UserApi {

    private Gson gson = new GsonBuilder().create();

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addNewUser(@FormParam("first_name") String firstName,
                           @FormParam("second_name") String secondName,
                           @FormParam("age") int age) {
        User user = new User(firstName, secondName, age);

        UserDao.getInstance().addUser(user);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String loadFromFile() throws IOException {
        return gson.toJson(UserDao.getInstance().getUsersFromFile());
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(String jsonUser) {
        try {
            User user = gson.fromJson(jsonUser, User.class);
            if (UserDao.getInstance().update(user)) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @DELETE
    @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUser(@PathParam("name") String name) {
        if (UserDao.getInstance().remove(name)) {
            String json = "{\"result\" : \"Removed user with name: " + name + "\"}";
            return Response.status(Response.Status.OK).entity(json).build();
        } else {
            String json = "{\"result\" : \"User not found: " + name + "\"}";
            return Response.status(Response.Status.NOT_FOUND).entity(json).build();
        }
    }

}
