package space.quinoaa.lexlist.commands;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "help")
@AllArgsConstructor
public class HelpCommand implements Callable<Integer> {
    private HashMap<String, Object> commands;
    private Logger LOG;

    @Override
    public Integer call() throws Exception {
        LOG.info("Command list: ");
        for (String s : commands.keySet()) {
            LOG.info(" - {}", s);
        }
        return 0;
    }
}
