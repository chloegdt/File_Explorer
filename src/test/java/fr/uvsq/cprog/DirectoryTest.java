package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class DirectoryTest {
  /**
   * Check if no error is thrown.
   */
  @Test
  public void constructorTest() throws IOException {
    // String argument
    new Directory("test");

    // Path argument
    new Directory(Paths.get("test"));
  }

  /**
   * Check if the constructor fails when the given path is not a directory.
   */
  @Test
  public void constructorFailTest() throws IOException {
    // String argument
    Exception exception = assertThrows(IOException.class, () -> {
      Directory dir = new Directory("nofile");
    });

    String expectedMessage = "No such directory";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    // Path argument
    exception = assertThrows(IOException.class, () -> {
      Directory dir = new Directory(Paths.get("nofile"));
    });

    expectedMessage = "No such directory";
    actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void mkdirTest() throws IOException {
    Directory dir = new Directory("test");
    dir.mkdir("new");
    dir.removeFile(Paths.get("test/new"));
  }

  @Test
  public void mkdirFailTest() throws IOException {
    Directory dir = new Directory("test");
    Exception exception = assertThrows(IOException.class, () -> {
      dir.mkdir("new");
      dir.mkdir("new");
    });
    dir.removeFile(Paths.get("test/new"));

    String expectedMessage = "Directory already exists";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  public void pasteFileTest() throws IOException, IllegalArgumentException {
    Directory dir = new Directory("test");
    Path source = Paths.get("test/doc.md");
    dir.paste(source);
    dir.removeFile(Paths.get("test/doc-copy.md"));
  }
  
  @Test
  public void pasteDirTest() throws IOException {
    Directory dir = new Directory("test");
    Path source = Paths.get("test/subtest");
    dir.paste(source);
    dir.removeFile(Paths.get("test/subtest-copy"));
  }

  /**
   * Check if getParent fails when the directory is the root.
   */
  @Test
  public void getParentFailTest() throws IOException {
    Exception exception = assertThrows(IOException.class, () -> {
      Directory dir = new Directory("");
      while (true) {
        dir = dir.getParent();
      }
    });

    String expectedMessage = "Already at the root";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  static Stream<Arguments> fileNameFailArgs() {
    return  Stream.of(
    Arguments.of(-1, "No current NER"),
    Arguments.of(9999, "Invalid NER"),
    Arguments.of(-9999, "Invalid NER")
    );
  }

  /**
   * Check if getFileName fails when the given NER is not valid.
   */
  @ParameterizedTest
  @MethodSource("fileNameFailArgs")
  public void getFileNameFailTest(int ner, String error) throws IOException {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      Directory dir = new Directory("test");
      dir.getFilename(ner);
    });

    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(error));
  }

  static Stream<Arguments> checkNerArgs() {
    return  Stream.of(
    Arguments.of(0, true),
    Arguments.of(-9, false),
    Arguments.of(-1, false),
    Arguments.of(35453, false)
    );
  }

  @ParameterizedTest
  @MethodSource("checkNerArgs")
  public void checkNerTest(int ner, boolean expectedBool) throws IOException {
    Directory dir = new Directory("test");
    
    assertEquals(dir.checkNer(ner), expectedBool);
  }
}
