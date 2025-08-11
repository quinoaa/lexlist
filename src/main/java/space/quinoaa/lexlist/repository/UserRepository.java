package space.quinoaa.lexlist.repository;

import lombok.AllArgsConstructor;
import space.quinoaa.lexlist.Config;
import space.quinoaa.lexlist.data.User;

import java.sql.Connection;
import java.sql.ResultSet;

public class UserRepository extends JDBCRepository {
    public static final long TOKEN_DURATION = Long.parseLong(Config.getConfig("auth.expire_ms"));

    public UserRepository(Connection connection) {
        super(connection);
    }

    public boolean isUsernameCorrect(String username){
        if(username.length() > 20) return false;
        return username.matches("([A-z0-9_])+");
    }

    public CreateResult createUser(String username, String password) {
        if(!isUsernameCorrect(username)) return new CreateResult(CreateResultType.INCORRECT_USERNAME, null);

        return query("create_user",  res->{
            if(res.next()){
                return new CreateResult(
                        CreateResultType.CREATE,
                        new User(
                                res.getLong("userid"),
                                username,
                                password
                        )
                );
            }else{
                return new CreateResult(
                        CreateResultType.EXISTS,
                        null
                );
            }
        }, prep->{
            prep.setString(1, username);
            prep.setString(2, password);
        });
    }

    public boolean removeUser(String username) {
        return query("remove_user", ResultSet::next, arg->arg.setString(1, username));
    }
    public record CreateResult(CreateResultType type, User user){}

    @AllArgsConstructor
    public enum CreateResultType{
        EXISTS,
        INCORRECT_USERNAME,
        CREATE
        ;
    }

    public User getUserByName(String username){
        return query("get_user_by_name", res->{
            if(res.next()){
                return new User(
                        res.getLong("userid"),
                        username,
                        res.getString("password")
                );
            }
            return null;
        }, arg->arg.setString(1, username));
    }

    public User getUserByToken(String token){
        return query("get_user_by_token", res->{
            if(res.next()){
                return new User(
                        res.getLong("userid"),
                        res.getString("username"),
                        res.getString("password")
                );
            }
            return null;
        }, arg->{
            arg.setString(1, token);
            arg.setLong(2, System.currentTimeMillis());
        });
    }

    public void updateToken(String token){
        run("update_token", arg->{
            arg.setLong(1, System.currentTimeMillis() + TOKEN_DURATION);
            arg.setString(2, token);
        });
    }

    public void clearExpiredTokens(){
        run("clear_expired_tokens", arg->{
            arg.setLong(1, System.currentTimeMillis());
        });
    }


    public String createToken(long userid){

        return query("create_token", res->{
            return res.next() ? res.getString("token") : null;
        }, arg->{
            arg.setLong(1, userid);
            arg.setLong(2, System.currentTimeMillis() + TOKEN_DURATION);
        });
    }


    public void deleteToken(String token){
        run("delete_token", arg->arg.setString(1, token));
    }
}
