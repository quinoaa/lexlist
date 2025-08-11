package space.quinoaa.lexlist.controller;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import space.quinoaa.lexlist.Hash;
import space.quinoaa.lexlist.repository.Repositories;
import space.quinoaa.lexlist.repository.UserRepository;

public class UserController extends Controller {

    public UserController(Repositories repo) {
        super(repo);
    }

    @Override
    public void addEndpoints() {
        ApiBuilder.post("login", this::login);
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
            context.json(new UserState(true, user.id(), user.username()));
            return;
        }

        context.json(new UserState(false, null, null));
    }

    record UserState(boolean logged, Long id, String name) {}

    private void login(@NotNull Context context) {
        var body = context.bodyAsClass(LoginData.class);
        var username = body.username;
        var password = body.password;
        if(password == null || username == null) return;

        var user = repo.users.getUserByName(username);
        if(user == null) {
            context.json(new LoginResult(false));
            Hash.hash(password); // For timing
        }else{
            if(Hash.compare(password, user.password())){
                var token = repo.users.createToken(user.id());
                context.cookie("token", token, (int) (UserRepository.TOKEN_DURATION / 1000));
                context.json(new LoginResult(true));
            }else{
                context.json(new LoginResult(false));
            }
        }
    }

    record LoginData(String username, String password) {}

    record LoginResult(
        boolean success
    ) {}
}
