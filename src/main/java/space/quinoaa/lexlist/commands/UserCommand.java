package space.quinoaa.lexlist.commands;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import space.quinoaa.lexlist.Hash;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "user", subcommands = {
        UserCommand.Add.class,
        UserCommand.Delete.class,
})
@AllArgsConstructor
public class UserCommand {
    private static Logger LOG = LoggerFactory.getLogger(UserCommand.class);

    @CommandLine.Command(name = "add")
    public static class Add implements Callable<Integer> {

        @CommandLine.Parameters(index = "0")
        String username;

        @CommandLine.Parameters(index = "1", hidden = true)
        String password;

        @Override
        public Integer call() {
            var res = CommandManager.INSTANCE.repos.users.createUser(username, Hash.hash(password));

            switch (res.type()){
                case CREATE -> LOG.info("User added");
                case EXISTS -> LOG.info("User already exists");
                case INCORRECT_USERNAME -> LOG.info("Incorrect username {}", username);
            }

            return 0;
        }
    }

    @CommandLine.Command(name = "delete", aliases = "del")
    public static class Delete implements Callable<Integer> {

        @CommandLine.Parameters
        String username;

        @Override
        public Integer call() {
            var res = CommandManager.INSTANCE.repos.users.removeUser(username);

            System.out.println(res);
            return 0;
        }
    }
}
