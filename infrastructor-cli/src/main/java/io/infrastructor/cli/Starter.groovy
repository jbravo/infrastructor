package io.infrastructor.cli

import com.beust.jcommander.JCommander
import io.infrastructor.cli.handlers.DecryptHandler
import io.infrastructor.cli.handlers.EncryptHandler
import io.infrastructor.cli.handlers.HelpHandler
import io.infrastructor.cli.handlers.RunHandler
import io.infrastructor.cli.handlers.VersionHandler

import static io.infrastructor.core.utils.ExceptionUtils.deepSanitize
import static io.infrastructor.core.logging.ConsoleLogger.*

public class Starter {
    
    def static HANDLERS = [:]
    
    static {
        HANDLERS << ['run':     new RunHandler()]
        HANDLERS << ['encrypt': new EncryptHandler()]
        HANDLERS << ['decrypt': new DecryptHandler()]
        HANDLERS << ['version': new VersionHandler()]
        HANDLERS << ['help':    new HelpHandler(handlers: HANDLERS)]
    }

    public static void main(String [] args) {
        
        // get rid of any SLF4J error output messages
        System.setErr(new PrintStream(
                new OutputStream() {
                    public void write(int b) {
                    }
                }));
        
        try {
            if (args.length == 0) {
                HANDLERS['help'].execute()
            } else {
                def handler = HANDLERS[args.head()]
                if (!handler) {
                    error "Unknown command '${args.head()}'"
                    HANDLERS['help'].execute()
                } else {
                    new JCommander(handler).parse(args.tail())
                    handler.execute()
                }
            }
        } catch (Exception ex) {
            def message = ex.toString()?.replaceAll("\n", "\n ")
            
            debug " ${bold('Uncaught exception:')}"
            debug " ${ex.class.name}: $message"
            debug " ${bold('stack trace:')}"
            debug (" - ${deepSanitize(ex)}".replaceAll("\n", "\n - "))
            
            error "application has stopped due to an error: ${bold(message)}"
            error "please check the log output. use '-l 3' to activate debug log."
        }
    }
}

