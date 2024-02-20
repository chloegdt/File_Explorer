package fr.uvsq.cprog;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;
import org.apache.commons.io.FileUtils;

/**
 * Class to manage a directory and its notes.
 */
public class Directory {
  Path dirpath;
  String[] files;
  Notes notes;

  /**
   * Creates a new Directory instance from the given dirname string.
   *
   * @param dirname name of the directory
   * @throws IOException if the directory doesn't exists
   */ 
  public Directory(String dirname) throws IOException {
    dirpath = Paths.get(dirname).toAbsolutePath();
    if (!Files.isDirectory(dirpath)) {
      throw new IOException("No such directory: " + dirname);
    }
    files = new File(dirname).list();
    notes = new Notes(dirname);
  } 

  /**
   * Creates a new Directory instance from the given Path.
   *
   * @param givendirpath path of the directory
   * @throws IOException if the directory doesn't exists
   */ 
  public Directory(Path givendirpath) throws IOException {
    dirpath = givendirpath.toAbsolutePath();
    if (!Files.isDirectory(dirpath)) {
      throw new IOException("No such directory");
    }
    files = new File(dirpath.toString()).list();
    notes = new Notes(dirpath.toString());
  } 


  /**
   * Copy the file at the given source path in this Directory.
   *
   * @param source path of the directory to copy
   * @throws IOException if could not copy
   */ 
  public void paste(Path source) throws IOException  {
    //TODO throws InvalidPathException
    Path destination = dirpath.resolve(source.getFileName());

    // append '-copy' to the filename if needed
    while (Files.exists(destination)) {
      String oldFilename = destination.getFileName().toString();
      int lastDotIndex = oldFilename.lastIndexOf('.');
      String newFilename;
      if (lastDotIndex > 0) {
        newFilename = oldFilename.substring(0, lastDotIndex) 
          + "-copy" + oldFilename.substring(lastDotIndex);
      } else {
        newFilename = oldFilename + "-copy";
      }
      destination = dirpath.resolve(newFilename);
    }

    // copy the file
    if (Files.isDirectory(source)) {
      // if it is a directory, copy recursively
      File dirSource = new File(source.toString());
      File dirDestination = new File(destination.toString());
      FileUtils.copyDirectory(dirSource, dirDestination);
    } else {
      Files.copy(source, destination);
    }
  }


  /**
   * Creates the directory named by the given string.
   *
   * @param dirname the name of the directory
   * @throws IOException if the directory already exists
   */
  public void mkdir(String dirname) throws IOException {
    // append dirname to dirpath
    Path dir2create = dirpath.resolve(dirname);
    // check if the directory exists
    if (Files.isDirectory(dir2create)) {
      throw new IOException("Directory already exists");
    }
    // create the directory
    new File(dir2create.toString()).mkdir();
  }


  /**
   * Search recursively for files matching the given name (no regexp).
   *
   * @param filename name of the file to find
   * @return a stream of path matching the name to find
   * @throws IOException if could not access the starting directory
   */ 
  public Stream<Path> find(String filename) throws IOException {
    return Files.find(dirpath, 128, (path, basicFileAttributes) -> {
      if (path.getFileName().toString().equals(filename)) {
        return true;
      } else {
        return false;
      }
    });
  }


  /**
   * Add a given note to the given ner.
   *
   * @param ner corresponding to the file for which to add the note
   * @param note the String to add
   * @throws IllegalArgumentException if the given NER does not point to a file
   */ 
  public void addNote(int ner, String note) throws IllegalArgumentException {
    notes.addNote(getFilename(ner), note);
  }

  /**
   * Remove the note attach to the given ner.
   *
   * @param ner of the note to remove
   * @throws IllegalArgumentException if the given NER does not point to a file
   */ 
  public void removeNote(int ner) throws IllegalArgumentException {
    notes.removeNote(getFilename(ner));
  }

  /**
   * Get the note attach to the given ner.
   *
   * @param ner of the note to get
   * @return the note
   * @throws IllegalArgumentException if the given NER does not point to a file
   */ 
  public String getNote(int ner) throws IllegalArgumentException {
    return notes.getNote(getFilename(ner));
  }

  /**
   * Check if the given ner is valid (positive and pointing to a file).
   *
   * @param ner to check
   * @return true if the ner is valid else return false
   */ 
  public boolean checkNer(int ner) {
    return 0 <= ner && ner < files.length;
  }

  /**
   * Gives the name of the file of the given NER as a String.
   *
   * @param ner of the file to get the name
   * @return String name of the file corresponding to ner
   * @throws IllegalArgumentException if the given NER does not point to a file
   */ 
  public String getFilename(int ner) throws IllegalArgumentException {
    if (ner == -1) {
      throw new IllegalArgumentException("No current NER.");
    } else if (0 <= ner && ner < files.length) {
      return files[ner];
    } else {
      throw new IllegalArgumentException("Invalid NER.");
    }
  }

  /**
   * Gives the Path of the file of the given NER.
   *
   * @param ner of the file to get the path
   * @return Path of the file corresponding to ner
   */
  public Path getFilepath(int ner) throws IllegalArgumentException {
    return dirpath.resolve(getFilename(ner));
  }

  /**
   * Delete the file at the given path.
   *
   * @param filepath path of the file to remove
   * @throws IOException if the file could not be deleted
   * @throws IllegalArgumentException if filepath does not point to a file
   */ 
  public static void removeFile(Path filepath) throws IOException, IllegalArgumentException {
    File file2remove = new File(filepath.toString());
    if (file2remove.isDirectory()) {
      FileUtils.deleteDirectory(file2remove);
    } else {
      file2remove.delete();
    }
  }

  /**
   * Return the directory of the given NER.
   *
   * @param ner of the file to go to
   * @return ner's directory
   * @throws IOException if the ner's file is not a directory
   */ 
  public Directory goTo(int ner) throws IOException, IllegalArgumentException {
    Path dir2go = dirpath.resolve(getFilename(ner));
    return new Directory(dir2go);
  }
  
  /**
   * Return the parent of the directory.
   *
   * @return the directory's parent
   * @throws IOException if the directory is the root
   */ 
  public Directory getParent() throws IOException {
    Path dir2go = dirpath.getParent();
    if (dir2go == null) {
      throw new IOException("Already at the root.");
    } else {
      return new Directory(dir2go);
    }
  }

  /**
   * Save the notes of the Directory.
   *
   * @throws IOException if the notes could not be saved
   */ 
  public void saveNotes() throws IOException {
    notes.saveNotes(dirpath.toString());
  }

  /**
   * Refresh the Directory.
   */
  public void refresh() {
    files = new File(dirpath.toString()).list();
  }
}

