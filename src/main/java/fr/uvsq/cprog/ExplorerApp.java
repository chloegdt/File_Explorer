package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Main class.
 * Process the arguments entered by the user and launch the program.
 */
class ExplorerApp {
  /**
   * Main method called when executing the jar.
   */
  public static void main(String[] args) throws IOException {
    Session session;
    if (args.length > 0) {
      // try to start the program at the given path
      try {
        String path = Paths.get(args[0]).toRealPath().toString();
        session = new Session(path);
      } catch (IOException ioexception) {
        // show error and exit
        System.out.println("Invalid argument: " + args[0] + " is not a directory.");
        System.out.println("\nUsage: java -jar explorer-1.0-jar-with-dependencies.jar [<path>]");
        return;
      }
    } else { // no argument given, starting at working directory
      session = new Session("");
    }

    // start the program
    session.start();
  }
}
