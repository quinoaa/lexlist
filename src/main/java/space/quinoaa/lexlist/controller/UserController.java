package space.quinoaa.lexlist.controller;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import space.quinoaa.lexlist.Hash;
import space.quinoaa.lexlist.repository.Repositories;

public class UserController extends Controller {

    public UserController(Repositories repo) {
        super(repo);
    }

    @Override
    public void addEndpoints() {
        ApiBuilder.get("login", this::login);
        ApiBuilder.get("state", this::state);
        ApiBuilder.get("logout", this::logout);
    }

    private void logout(@NotNull Context context) {
        var token = context.cookie("token");
        if(token == null) return;

        repo.users.deleteToken(token);
        context.removeCookie("token");
    }

    private void state(@NotNull Context context) {
        var user = getUser(context);

        if(user != null){
            context.json(new UserState(true, user.id()));
            return;
        }

        context.json(new UserState(false, null));
    }

    record UserState(boolean logged, Long id) {}

    private void login(@NotNull Context context) {
        var username = context.queryParam("username");
        var password = context.queryParam("password");
        if(password == null || username == null) return;

        var user = repo.users.getUser(username);
        if(user == null) {
            context.json(new LoginResult(false));
            Hash.hash(password); // For timing
        }else{
            if(Hash.compare(password, user.password())){
                var token = repo.users.createToken(user.id());
                context.cookie("token", token);
                context.json(new LoginResult(true));
            }else{
                context.json(new LoginResult(false));
            }
        }
    }


    record LoginResult(
        boolean success
    ) {}
}
