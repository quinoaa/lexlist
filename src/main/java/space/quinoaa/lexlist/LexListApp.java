package space.quinoaa.lexlist;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import space.quinoaa.lexlist.commands.CommandManager;
import space.quinoaa.lexlist.controller.DictController;
import space.quinoaa.lexlist.controller.UserController;
import space.quinoaa.lexlist.repository.Repositories;
import space.quinoaa.lexlist.schedule.ScheduledTasks;

public class LexListApp {

    public static void main(String[] args) {
        Repositories repos = Repositories.load();

        Javalin.create(cfg->{
            cfg.useVirtualThreads = true;

            cfg.router.apiBuilder(()->{
                ApiBuilder.path("user", new UserController(repos));
                ApiBuilder.path("dict", new DictController(repos));
            });


            cfg.bundledPlugins.enableCors(cors->{
                cors.addRule(it->{
                    it.allowHost(Config.getConfig("http.cors.domain"));
                    it.allowCredentials = true;
                });
            });

            cfg.staticFiles.add("/public");
        }).start(Integer.parseInt(Config.getConfig("http.port")));

        new ScheduledTasks(repos).initialize();

        CommandManager cmd = new CommandManager(repos);
        cmd.loop();
    }

}
