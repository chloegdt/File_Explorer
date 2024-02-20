package fr.uvsq.cprog;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.DefaultWindowManager;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.Arrays;

/**
 * Class to build and manage the interface.
 */
public class Interface {
  Terminal terminal;
  Screen screen;

  // components
  Panel mainPanel;
  TextBox dirpathTextBox;
  TextBox filesTextBox;
  TextBox infoTextBox;
  TextBox inputTextBox;

  BasicWindow window;
  MultiWindowTextGUI gui;

  /**
   * Constructor method.
   *
   * @throws IOException if the terminal or the screen could not be created
   */
  public Interface() throws IOException {
    // Setup terminal and screen layers
    terminal = new DefaultTerminalFactory().createTerminal();
    screen = new TerminalScreen(terminal);
    screen.startScreen();

    // Create panel to hold components
    mainPanel = new Panel();
    mainPanel.setLayoutManager(new GridLayout(2));

    // DIRPATH label
    dirpathTextBox = new TextBox("");
    dirpathTextBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.BEGINNING,
                true, 
                false,
                2,
                1
    ));
    dirpathTextBox.setReadOnly(true);
    dirpathTextBox.setVerticalFocusSwitching(false);
    dirpathTextBox.setHorizontalFocusSwitching(false);
    mainPanel.addComponent(dirpathTextBox.withBorder(Borders.singleLine("Current Directory Path")));

    // FILE LIST
    filesTextBox = new TextBox();
    filesTextBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.FILL,
                false,
                true
    ));
    filesTextBox.setReadOnly(true);
    filesTextBox.setVerticalFocusSwitching(false);
    filesTextBox.setHorizontalFocusSwitching(false);
    filesTextBox.withBorder(Borders.singleLine("Files")).addTo(mainPanel);
    
    // NOTES, VISU, HELP, FIND
    infoTextBox = new TextBox();
    infoTextBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.FILL,
                true,
                true
    ));
    infoTextBox.setReadOnly(true);
    infoTextBox.setVerticalFocusSwitching(false);
    infoTextBox.setHorizontalFocusSwitching(false);
    mainPanel.addComponent(infoTextBox.withBorder(Borders.singleLine("Info")));

    // COMMAND LINE
    inputTextBox = new TextBox();
    inputTextBox.setLayoutData(GridLayout.createLayoutData(
                GridLayout.Alignment.FILL,
                GridLayout.Alignment.END,
                true,
                false,
                2,
                1
    ));
    inputTextBox.setVerticalFocusSwitching(false);
    inputTextBox.setHorizontalFocusSwitching(false);
    mainPanel.addComponent(inputTextBox.withBorder(Borders.singleLine("Command Line")));

    // Create window to hold the panel
    window = new BasicWindow();
    window.setComponent(mainPanel.withBorder(Borders.singleLine("Explorer")));
    window.setHints(Arrays.asList(
          Window.Hint.NO_POST_RENDERING,
          Window.Hint.FULL_SCREEN,
          Window.Hint.NO_DECORATIONS
    ));

    // Config theme
    SimpleTheme theme = SimpleTheme.makeTheme(
        true,
        TextColor.ANSI.WHITE,
        TextColor.ANSI.BLACK,
        TextColor.ANSI.WHITE,
        TextColor.ANSI.BLACK,
        TextColor.ANSI.CYAN,
        TextColor.ANSI.BLACK,
        TextColor.ANSI.RED
    );

    // Create tui
    gui = new MultiWindowTextGUI(
        screen,
        new DefaultWindowManager(),
        new EmptySpace(TextColor.ANSI.BLACK)
    );
    gui.setTheme(theme);
  }

  /**
   * Set the input filter with the current session to intercept inputs.
   *
   * @param session the session that should intercept the keystrokes
   */
  public void setInputFilter(Session session) {
    inputTextBox.setInputFilter(session);
  }

  /**
   * Start the interface.
   */
  public void start() {
    inputTextBox.takeFocus();
    gui.addWindowAndWait(window);
  }

  /**
   * Close the interface.
   */
  public void close() {
    window.close();
  }

  /**
   * Refresh the interface with the current state of the program.
   *
   * @param currentDirectory the current directory where the program is working on
   * @param currentNer the current NER of the program
   */
  public void refresh(Directory currentDirectory, int currentNer) {
    currentDirectory.refresh();

    //dirpath
    dirpathTextBox.setText(currentDirectory.dirpath.toString());
    
    //files
    if (currentDirectory.files.length > 0) {
      filesTextBox.setText("0 : " + currentDirectory.files[0]);
      for (int i = 1; i < currentDirectory.files.length; i++) {
        filesTextBox.addLine(String.valueOf(i) + " : " + currentDirectory.files[i]);
      }
    } else {
      filesTextBox.setText("No file in this directory.");
    }

    //notes
    if (infoTextBox.getText().isEmpty()) {
      try {
        infoTextBox.setText("Notes of the current NER "
            + String.valueOf(currentNer) + ":\n" 
            + currentDirectory.getNote(currentNer));
      } catch (Exception exception) {
        infoTextBox.setText(exception.getMessage());
      }
    }
  }

  /**
   * Display a message dialog with the given error for the user.
   *
   * @param error String describing the encountered error
   */
  public void showError(String error) {
    MessageDialog.showMessageDialog(gui, "Error", error);
  } 
}
