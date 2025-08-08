package space.quinoaa.lexlist.controller;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;
import space.quinoaa.lexlist.data.User;
import space.quinoaa.lexlist.repository.Repositories;

@AllArgsConstructor
public abstract class Controller implements EndpointGroup {
    protected final Repositories repo;

    User getUser(Context ctx){
        var token = ctx.cookie("token");
        if(token == null) return null;

        var user = repo.users.getUser(token);
        if(user == null) return null;
        repo.users.updateToken(token);

        return user;
    }
}
