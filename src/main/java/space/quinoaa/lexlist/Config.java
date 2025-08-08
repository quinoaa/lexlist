package space.quinoaa.lexlist;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Properties prop = loadConfig("application.properties");
    private static Properties sql = loadConfig("sql.properties");


    public static String getConfig(String key){
        return prop.getProperty(key);
    }

    public static String getSQL(String key){
        return sql.getProperty(key);
    }


    @SneakyThrows
    private static Properties loadConfig(String name) {
        Properties properties = new Properties();

        var resource = Config.class.getClassLoader().getResourceAsStream(name);
        properties.load(resource);

        var file = new File(name);
        if(file.exists()) properties.load(new FileInputStream(file));

        return properties;
    }

}
