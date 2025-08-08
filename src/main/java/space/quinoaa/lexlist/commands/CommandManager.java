package space.quinoaa.lexlist.commands;

import lombok.SneakyThrows;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import space.quinoaa.lexlist.repository.Repositories;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private static final Logger LOG = LoggerFactory.getLogger(CommandManager.class);

    public static CommandManager INSTANCE;

    public final Repositories repos;

    private final LineReader reader;
    private final Map<String, CommandLine> commands = new HashMap<>();

    @SneakyThrows
    public CommandManager(Repositories repos) {
        this.repos = repos;
        INSTANCE = this;

        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();

        var commands = new HashMap<String, Object>();

        commands.put("user", new UserCommand());
        commands.put("help", new HelpCommand(commands, LOG));

        for(var key : commands.keySet()){
            this.commands.put(key, new CommandLine(commands.get(key)));
        }
    }

    @SneakyThrows
    public void loop(){

        while(true){
            var line = reader.readLine();

            var space = line.indexOf(" ");
            String cmdName;
            if(space == -1){
                cmdName = line;
                line = "";
            }else{
                cmdName = line.substring(0, space);
                line =  line.substring(space + 1);
            }

            if(commands.containsKey(cmdName)){
                var args = new String[0];

                if(!line.isBlank()){
                    args = line.split(" +");
                }

                commands.get(cmdName).execute(args);
            }else{
                LOG.error("Invalid command {}", cmdName);
                LOG.error("For command list, run > help");
            }
        }
    }

}
