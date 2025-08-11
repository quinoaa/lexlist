package space.quinoaa.lexlist.repository;

import lombok.SneakyThrows;
import space.quinoaa.lexlist.Config;

import java.sql.Connection;
import java.sql.DriverManager;

public class Repositories {
    public final UserRepository users;
    public final DictionaryRepository dict;

    private Repositories(Connection conn) {
        users = new UserRepository(conn);
        dict = new DictionaryRepository(conn);
    }

    @SneakyThrows
    @SuppressWarnings("all")
    public static Repositories load() {
        var conn = DriverManager.getConnection(Config.getConfig("database.url"));
        var init = new String(Repositories.class.getClassLoader().getResourceAsStream("init.sql").readAllBytes());
        for (String s : init.split(";")) {
            if(!s.isBlank()) conn.prepareStatement(s).execute();
        }
        return new Repositories(conn);
    }
}
