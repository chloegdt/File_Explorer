package fr.uvsq.cprog;

import com.googlecode.lanterna.gui2.InputFilter;
import com.googlecode.lanterna.gui2.Interactable;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import java.awt.Desktop;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * This class keeps track of the state of the program during its usage.
 */
public class Session implements InputFilter {
  Interface ui;
  Directory currentDirectory;
  int currentNer = -1;
  Path file2copy = null;
  boolean shouldCut = false;

  /**
   * Creates a new Directory instance from the given dirname string.
   *
   * @param dirname name of the directory
   * @throws IOException if the directory doesn't exists
   */ 
  public Session(String dirname) throws IOException {
    currentDirectory = new Directory(dirname);
    ui = new Interface();
    ui.setInputFilter(this);
    ui.refresh(currentDirectory, currentNer);
  } 

  /**
   * Implements the method onInput of the interface InputFilter
   * to intercept keystrokes from the user and run the appropriate code.
   */
  public boolean onInput(Interactable intercatable, KeyStroke keyStroke) {
    if (keyStroke.getKeyType() == KeyType.Enter) {
      // Process user input
      try {
        onEnter();
      } catch (Exception exception) {
        ui.showError(exception.getMessage());
      }
      // Refresh Interface
      ui.refresh(currentDirectory, currentNer);

      // Tell the interface to ignore the keystroke
      return false;
    }
    // Let the interface handle it
    return true;
  }

  /**
   * Start the session and the interface.
   */
  public void start() {
    ui.start();
  }

  /**
   * Close the session and the interface.
   * Saves any notes if needed.
   */
  public void close() {
    // save notes
    try {
      currentDirectory.saveNotes();
    } catch (IOException exception) {
      ui.showError(exception.getMessage());
    }

    // close
    ui.close();
  }


  /**
   * Process the command entered by the user when the Enter key is pressed.
   *
   * @throws IOException to be displayed by the interface if any encountered
   */ 
  public void onEnter() throws IOException {
    // Input
    String input = ui.inputTextBox.getText();
    ui.inputTextBox.setText("");
    ui.infoTextBox.setText("");
    
    // Parse Command
    CommandParser result;
    try {
      result = new CommandParser(input);
    } catch (Exception exception) {
      ui.showError(exception.getMessage());
      return;
    }

    // Update the NER
    if (currentDirectory.checkNer(result.getNer())) {
      currentNer = result.getNer();
    } else if (result.getNer() != -1) {
      ui.showError("Invalid NER.");
      return;
    }

    // Run Command
    try {
      runCommand(result);
    } catch (Exception exception) {
      ui.showError(exception.getMessage());
      return;
    }

  }
  
  /**
   * Call the methods of Directory according to the parsed command.
   *
   * @param result parsed command from CommandParser
   * @throws Exception to be displayed by the interface if any encountered
   */
  private void runCommand(CommandParser result) throws Exception {
    switch (result.getCommand()) {
      
      case ADDNOTE:
        if (result.getName() != null) {
          currentDirectory.addNote(currentNer, result.getName());
        } else {
          ui.showError("No note given.");
        }
        break;

      case COPY:
        file2copy = currentDirectory.getFilepath(currentNer);
        shouldCut = false;
        break;
        
      case CUT:
        file2copy = currentDirectory.getFilepath(currentNer);
        shouldCut = true;
        break;

      case EXIT:
        close();
        break;

      case FIND:
        if (result.getName() != null) {
          ui.infoTextBox.setText(
              "File(s) matching the name '" + result.getName() + "':");
          currentDirectory.find(result.getName())
            .map(path -> path.toString())
              .forEach(ui.infoTextBox::addLine);
        } else {
          ui.showError("No filename to search.");
        }
        
        break;

      case GOTO:
        try {
          Directory oldDirectory = currentDirectory;
          currentDirectory = currentDirectory.goTo(currentNer);
          currentNer = -1;
          oldDirectory.saveNotes();
        } catch (IOException exception) {
          ui.showError(exception.getMessage());
        }

        break;

      case MKDIR:
        if (result.getName() != null) {
          currentDirectory.mkdir(result.getName());
        } else {
          ui.showError("No directory name given.");
        }
        break;

      case PARENT:
        try {
          Directory oldDirectory = currentDirectory;
          currentDirectory = currentDirectory.getParent();
          currentNer = -1;
          oldDirectory.saveNotes();
        } catch (IOException exception) {
          ui.showError(exception.getMessage());
        }
        break;

      case PASTE:
        if (file2copy != null) {
          currentDirectory.paste(file2copy);
          if (shouldCut) {
            currentDirectory.removeFile(file2copy);
            shouldCut = false;
            file2copy = null;
          }
        } else {
          ui.showError("Nothing to paste.");
        }
        break;

      case REMOVENOTE:
        currentDirectory.removeNote(currentNer);
        break;

      case VISU:
        // get mime type
        Path path = currentDirectory.getFilepath(currentNer);
        String type = Files.probeContentType(path);

        if (type != null && type.contains("text")) {
          // if the file is a text
          String content = Files.readString(path);
          ui.infoTextBox.setText(
              "Content of the file " 
              + String.valueOf(currentNer) 
              + ":\n" + content);
        } else if (type != null && type.contains("image")) {
          // if the file is an image
          File image = new File(path.toString());
          Desktop desktop = Desktop.getDesktop();
          desktop.open(image);
        } else {
          // anything else
          long size = Files.size(path);
          ui.infoTextBox.setText(String.valueOf(currentNer)
              + " is not a text file but here is its size: " 
              + String.valueOf(size)
              + " bytes");
        }
        break;

      case HELP:
        ui.infoTextBox.setText(showHelp());
        break;

      default:
        // Nothing to do
        break;
    }
  }

  /**
   * Gives a String to help.
   *
   * @return String of how to use the program
   */
  private String showHelp() {
    String help;
    if (currentNer != -1) {
      // ner is set
      help = "Current NER: " + String.valueOf(currentNer) + "\n";
      if (Files.isDirectory(currentDirectory.getFilepath(currentNer))) {
        // ner points to a directory
        help += currentDirectory.getFilename(currentNer) + " is a directory.\n\n";
        help += "Commands with the current NER:\n"
          + " * help       give help for the given NER\n"
          + " * <new NER>  change the current NER (needs to point to an existing element)\n"
          + " * + <note>   add a given note to the NER\n"
          + " * -          remove all notes of the NER\n"
          + " * cut        copy file to clipboard and delete it when pasted\n"
          + " * copy       copy file to clipboard\n"
          + " * visu       show its size\n"
          + " * .          go to the given NER (needs to be a directory)\n";
      } else {
        // ner points to a file
        help += currentDirectory.getFilename(currentNer) + " is a file.\n\n";
        help += "Commands with the current NER:\n"
          + " * help       give help for the given NER\n"
          + " * <new NER>  change the current NER (needs to point to an existing element)\n"
          + " * + <note>   add a given note to the NER\n"
          + " * -          remove all notes of the NER\n"
          + " * cut        copy file to clipboard and delete it when pasted\n"
          + " * copy       copy file to clipboard\n"
          + " * visu       show content of the file if it is a text else show its size\n";
      }
    } else {
      // no ner
      help = "No current NER.\n\n" 
        + "Commands needing NER:\n"
        + " * <NER>           set the current NER (needs to point to an existing element)\n"
        + " * <NER> + <note>  add a given note to the given NER\n"
        + " * <NER> -         remove all notes of the given NER\n"
        + " * <NER> cut       copy file to clipboard and delete it when pasted\n"
        + " * <NER> copy      copy file to clipboard\n"
        + " * <NER> visu      show content of the file if it is a text else show its size\n"
        + " * <NER> .         go to the given NER (needs to be a directory)\n"
        + " * <NER> help      give help for the given NER\n";
    }

    help += "\nOther commands without NER:\n" 
      + " * ..                      go to the parent directory of the current one\n"
      + " * mkdir <directory name>  create a new directory with the given name\n"
      + " * find <name to search>   find the given name from this directory recursively\n"
      + " * paste                   paste the copied file in the current directory\n"
      + " * help                    gives general help\n"
      + " * exit                    quit the program\n\n"
      + "Press <Tab> to change the focus to the next window\n"
      + "and navigate the text with the arrows.\n\n"
      + "See the user manual for more informations.";

    return help;
  }
}
