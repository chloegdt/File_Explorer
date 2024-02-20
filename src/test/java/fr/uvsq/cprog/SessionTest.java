package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Session class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SessionTest {
  static Session session;

  /**
   * Constructor Test when the commandline is empty. 
   */
  @Test
  @BeforeAll
  public static void ConstructorTest() throws IOException {
    session = new Session("test/subtest");
  }

  /**
   * Arguments for the Constructor Test with a command containing no argument (ConstructorSize0Test). 
   */
  static Stream<Arguments> commands() {
    return  Stream.of(
    Arguments.of("mkdir dir"),
    Arguments.of("help"),
    Arguments.of("ner"),
    Arguments.of("copy"),
    Arguments.of("cut"),
    Arguments.of("."),
    Arguments.of(".."),
    Arguments.of("1 visu"),
    Arguments.of("help"),
    Arguments.of("0 visu"),
    Arguments.of("help"),
    Arguments.of("find dir"),
    Arguments.of("1 + une note"),
    Arguments.of("1 -"),
    Arguments.of("paste")
    );
  }
  
  /**
   * Constructor Test when the commandline is empty. 
   */
  @ParameterizedTest
  @MethodSource("commands")
  public void CommandsTest(String commandline) throws Exception {
    if (commandline.equals("ner")) {
      if (session.currentDirectory.getFilename(0).equals("dir")) {
        commandline = "0";
      } else {
        commandline = "1";
      }
    }
    session.ui.inputTextBox.setText(commandline);
    session.onInput(null, new KeyStroke(KeyType.Enter));
  }

  /**
   * Test onInput with a key different than Enter.
   */
  @Test
  public void onInputTest() {
    session.onInput(null, new KeyStroke(KeyType.Escape));
  }

  /**
   * Clean up.
   */
  @AfterAll
  public static void cleanUp() throws IOException {
    Directory.removeFile(Paths.get("test/subtest/dir"));
    Directory.removeFile(Paths.get("test/subtest/dir-copy"));
  }
}
