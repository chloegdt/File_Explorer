package fr.uvsq.cprog;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Class to manage notes.
 */
public class Notes {
  HashMap<String, String> notes;

  /**
  * Constructor which tries to load any existing notes in the given directory.
  *
  * @param dirname directory where to load the notes
  */
  public Notes(String dirname) {
    // Create a HashMap object
    try {
      notes = loadNotes(dirname);
    } catch (IOException exception) {
      notes = new HashMap<String, String>();
    }
  }

  /**
  * Tries to load any existing notes in the given directory.
  *
  * @param dirname directory where to load the notes
  * @return an HashMap of the loaded notes
  * @throws IOException if the file 'notes' in the given directory could not be read
  */
  private static HashMap<String, String> loadNotes(String dirname) throws IOException {
    // Create new HashMap
    HashMap<String, String> notes = new HashMap<String, String>();

    // Try to read the file 'notes'
    FileReader reader = new FileReader(dirname + File.separatorChar + "notes");

    String filename = null; // current filename to attach the note to
    int ch; // current character to process
    StringBuilder buffer = new StringBuilder(); // current string to build
    
    while ((ch = reader.read()) != -1) { // not EOF
      if (ch == 31) { // If ch is an unit separator
        // the filename is complete
        filename = buffer.toString();
        buffer = new StringBuilder();
      } else if (ch == 30 && filename != null) { // If ch is a record separator
        // the note is complete
        notes.put(filename, buffer.toString());
        buffer = new StringBuilder();
      } else { // If ch is any other character
        buffer.append((char) ch);
      }
    }

    return notes;
  }

  /**
   * Get the note of the given filename or an empty String if there is none.
   *
   * @param filename for which to get its note
   * @return the note associated to given file
   */
  public String getNote(String filename) {
    return notes.getOrDefault(filename, "");
  }

  /**
   * Add a given note to the given filename.
   *
   * @param filename for which to add the note to
   * @param note to add
   */
  public void addNote(String filename, String note) {
    if (notes.containsKey(filename)) {
      note = notes.get(filename) + '\n' + note;
    }
    notes.put(filename, note);
  }

  /**
   * Remove all notes of the given filename.
   *
   * @param filename for which to remove the notes
   */
  public void removeNote(String filename) {
    notes.remove(filename);
  }

  /**
   * Save the notes in the given Directory.
   *
   * @param dirname where to save the notes
   * @throws IOException if the notes could not be saved
   */
  public void saveNotes(String dirname) throws IOException {
    if (!notes.isEmpty()) {
      try {
        FileWriter writer = new FileWriter(dirname + File.separatorChar + "notes", false);
        for (String filename : notes.keySet()) {
          writer.write(filename + (char) 31 + notes.get(filename) + (char) 30); 
        }
        writer.close();
      } catch (IOException ioe) {
        throw new IOException("Could not save the notes : " + ioe.getMessage());
      }
    } else {
      Path filepath = Paths.get(dirname, "notes");
      if (Files.exists(filepath)) {
        Directory.removeFile(filepath);
      }
    } 
  }
}
