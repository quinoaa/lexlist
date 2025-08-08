package space.quinoaa.lexlist.schedule;

import space.quinoaa.lexlist.repository.Repositories;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledTasks {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final Repositories repo;

    public ScheduledTasks(Repositories repo) {
        this.repo = repo;
    }

    public void initialize(){
        scheduler.schedule(repo.users::clearExpiredTokens, 10, TimeUnit.MINUTES);
    }
}
