package fr.uvsq.cprog;

import java.io.IOException;
import java.io.File;
import java.nio.file.Paths;
import java.util.stream.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Notes class.
 */
public class NotesTest {
  String testDirname = "test";
  String subTestDirname = "test/subtest";
  String emptyDirname = "test/emptydir";


  /**
   * Constructor Test when the given dirname contains the file 'notes'. 
   */
  @Test
  public void ConstructorTest() {
    Notes notes = new Notes(subTestDirname);
    assertTrue(!notes.notes.isEmpty());
  }

  /**
   * Constructor Test when the given dirname does not contain the file 'notes'. 
   */
  @Test
  public void ConstructorEmptyTest() {
    Notes notes = new Notes("directory_without_notes");
    assertTrue(notes.notes.isEmpty());
  }

  /**
   * Test when adding multiple notes to the same file. 
   */
  @Test
  public void addNoteTest() {
    Notes notes = new Notes(testDirname);

    notes.addNote("filename", "a note");
    assertEquals(notes.getNote("filename"), "a note");
    
    notes.addNote("filename", "another note");
    assertEquals(notes.getNote("filename"), "a note\nanother note");
  }

  /**
   * Test if the method removeNote deletes the note of the given filename.
   */
  @Test
  public void removeNoteTest() {
    Notes notes = new Notes(testDirname);

    notes.addNote("filename", "a note");
    assertEquals(notes.getNote("filename"), "a note");
    
    notes.removeNote("filename");
    assertEquals(notes.getNote("filename"), "");

    // remove note on nothing does throw error
    notes.removeNote("filename");
  }

  /**
   * Add a note and save it then reload the notes to see if it was successfully saved. 
   */
  @Test
  public void saveNoteTest() throws IOException {
    Notes notes = new Notes(testDirname);

    notes.addNote("filename", "a note");
    notes.saveNotes(testDirname);

    notes = new Notes(testDirname);

    assertEquals(notes.getNote("filename"), "a note");

    // clean up
    notes.removeNote("filename");
    notes.saveNotes(testDirname);
  }

  /**
   * Fail to save notes.
   */
  @Test
  public void failSaveNoteTest() throws IOException {
    Notes notes = new Notes(testDirname);

    Exception exception = assertThrows(IOException.class, () -> {
      notes.saveNotes("doesntexists");
    });

    String expectedMessage = "Could not save the notes";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  /**
   * Save empty notes.
   */
  @Test
  public void emptyNotesTest() throws IOException {
    new File(emptyDirname).mkdir();
    Notes notes = new Notes(emptyDirname);
    
    notes.addNote("1", "note");
    notes.saveNotes(emptyDirname);

    notes.removeNote("1");
    notes.saveNotes(emptyDirname);
  }
}
