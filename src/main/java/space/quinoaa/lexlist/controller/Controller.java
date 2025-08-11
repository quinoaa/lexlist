package space.quinoaa.lexlist.controller;

import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import lombok.AllArgsConstructor;
import space.quinoaa.lexlist.data.User;
import space.quinoaa.lexlist.repository.Repositories;
import space.quinoaa.lexlist.repository.UserRepository;

@AllArgsConstructor
public abstract class Controller implements EndpointGroup {
    protected final Repositories repo;

    User getUser(Context ctx){
        var token = ctx.cookie("token");
        if(token == null) return null;

        var user = repo.users.getUserByToken(token);
        if(user == null) return null;

        ctx.cookie("token", token, (int) (UserRepository.TOKEN_DURATION / 1000));
        repo.users.updateToken(token);

        return user;
    }

    User getUserOrError(Context ctx) {
        var user = getUser(ctx);

        if(user == null) throw new UnauthorizedResponse();

        return user;
    }
}
