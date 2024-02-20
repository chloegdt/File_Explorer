package fr.uvsq.cprog;

/**
 * Class to decompose, process and check the given command line.
 */
public class CommandParser {
  /**
   * Enum of all implemented commands.
   */
  enum Commands {
    CUT,
    COPY,
    PASTE,
    VISU,
    MKDIR,
    FIND,
    ADDNOTE,
    REMOVENOTE,
    GOTO,
    PARENT,
    EXIT,
    SETNER,
    HELP,
    NOTHING
  }

  private int ner = -1;
  private Commands command = Commands.NOTHING;
  private String name = null;

  /**
   * Parse the given command according to its size.
   *
   * @param commandLine the command to parse
   * @throws IllegalArgumentException if the command is invalid
   */ 
  public CommandParser(String commandLine) throws IllegalArgumentException {
    commandLine = commandLine.trim();
    if (commandLine.isEmpty()) {
      command = Commands.NOTHING;
      return;
    }

    String[] arrOfCommand = commandLine.split("[ ]+", 3);

    switch (arrOfCommand.length) {
      case 1:
        processSize1(arrOfCommand);
        break;
      case 2:
        processSize2(arrOfCommand);
        break;
      default:
        processSize3(arrOfCommand, commandLine);
        break;
    }
  } 


  /**
   * Checks if the given word is a NER or a command.
   *
   * @param arrOfCommand array of String, representing the components of the command
   * @throws IllegalArgumentException if the command is invalid. 
   */
  private void processSize1(String[] arrOfCommand) throws IllegalArgumentException {
    if (checkNer(arrOfCommand[0])) {
      command = Commands.SETNER;
    } else if (!checkCommand(arrOfCommand[0])) {
      throw new IllegalArgumentException("Invalid command with one argument. See help.");
    }
  } 


  /**
   * Checks the validity and the order of the two arguments.
   *
   * @param arrOfCommand array of String, representing the components of the command
   * @throws IllegalArgumentException if the command is invalid. 
   */
  private void processSize2(String[] arrOfCommand) throws IllegalArgumentException {
    if (checkNer(arrOfCommand[0])) {
      if (!checkCommand(arrOfCommand[1])) {
        throw new IllegalArgumentException("Invalid command with two arguments. See help.");
      }  
    } else if (checkCommand(arrOfCommand[0])) {
      name = arrOfCommand[1];
    } else {
      throw new IllegalArgumentException("Invalid command with two arguments. See help.");
    }
  }


  /**
   * Checks the validity and the order of the given command.
   *
   * @param arrOfCommand array of String, representing the components of the command
   * @throws IllegalArgumentException if the command is invalid. 
   */
  private void processSize3(String[] arrOfCommand, String commandLine)
      throws IllegalArgumentException {
    if (!checkNer(arrOfCommand[0])) {
      arrOfCommand = commandLine.split("[ ]+", 2);
      processSize2(arrOfCommand);
    } else if (!checkCommand(arrOfCommand[1])) {
      throw new IllegalArgumentException("Invalid command with three arguments. See help.");  
    } else {
      name = arrOfCommand[2];
    }
  }


  /**
   * Verify if the given string (inputCommand) is a valid command.  
   *
   * @param inputCommand (string) to be checked
   * @return true if the command is valid or false if not
   */
  private boolean checkCommand(String inputCommand) {
    // Check special characters
    if (inputCommand.equals("+")) {
      command = Commands.ADDNOTE;
      return true;
    } else if (inputCommand.equals("-")) {
      command = Commands.REMOVENOTE;
      return true;
    } else if (inputCommand.equals("..")) {
      command = Commands.PARENT;
      return true;
    } else if (inputCommand.equals(".")) {
      command = Commands.GOTO;
      return true;
    }

    // Check with enum
    inputCommand = inputCommand.toUpperCase();
    for (Commands enumCommand : Commands.values()) {
      if (enumCommand.toString().equals(inputCommand)) {
        command = enumCommand;
        return true;
      }
    }

    return false;
  }

  /**
   * Verify if the given string (inputNer) is an int.  
   *
   * @param inputNer (string) to be checked
   *
   * @return true if the NER is an int or false if not
   */
  private boolean checkNer(String inputNer) {
    try {
      ner = Integer.parseInt(inputNer);
      return ner >= 0;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Returns the NER of the parsed command line. 
   *
   * @return the NER (int) or null if there is no NER
   */ 
  public int getNer() {
    return ner;
  }

  /**
   * Returns the command of the parsed command line. 
   *
   * @return the command (Commands)
   */ 
  public Commands getCommand() {
    return command;
  }

  /**
   * Returns the name part of the parsed command line. 
   *
   * @return the name (String) or null if there is no name
   */ 
  public String getName() {
    return name;
  }
}

