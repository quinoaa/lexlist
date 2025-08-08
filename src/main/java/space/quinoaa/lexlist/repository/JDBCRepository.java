package space.quinoaa.lexlist.repository;

import lombok.SneakyThrows;
import space.quinoaa.lexlist.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCRepository {
    protected Connection connection;

    public JDBCRepository(Connection connection) {
        this.connection = connection;
    }

    @SneakyThrows
    protected <T> T query(String key, Transformer<T> transform, ArgSetter... setters){
        // I'm too lazy, it's sqlite anyway not a giant web app
        synchronized (connection) {
            var statement = connection.prepareStatement(Config.getSQL(key));

            for (ArgSetter setter : setters) {
                setter.set(statement);
            }

            var res = statement.executeQuery();
            var value = transform.apply(res);
            res.close();

            return value;
        }
    }

    @SneakyThrows
    protected void run(String key, ArgSetter... setters){
        synchronized (connection) {
            var statement = connection.prepareStatement(Config.getSQL(key));

            for (ArgSetter setter : setters) {
                setter.set(statement);
            }

            statement.execute();
        }
    }


    public interface ArgSetter{
        void set(PreparedStatement statement) throws SQLException;
    }

    public interface Transformer<T> {
        T apply(ResultSet result) throws SQLException;
    }
}
