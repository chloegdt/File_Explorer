package fr.uvsq.cprog;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the CommandParser class.
 */
public class CommandParserTest {
  /**
   * Arguments for the Constructor Test with a command containing no argument (ConstructorSize0Test). 
   */
  static Stream<Arguments> argumentsSize0() {
    return  Stream.of(
    Arguments.of("", -1, CommandParser.Commands.NOTHING),
    Arguments.of(" ", -1, CommandParser.Commands.NOTHING)
    );
  }
  
  /**
   * Constructor Test when the commandline is empty. 
   */
  @ParameterizedTest
  @MethodSource("argumentsSize0")
  public void ConstructorSize0Test(String commandline, int ner, CommandParser.Commands command) throws Exception {
    CommandParser result = new CommandParser(commandline);
    assertEquals(ner, result.getNer());
    assertEquals(command, result.getCommand());
    }

  /**
   * Arguments for the Constructor Test with a command containing one argument (ConstructorSize1Test). 
   */
  static Stream<Arguments> argumentsSize1() {
    return  Stream.of(
    Arguments.of("2", 2, CommandParser.Commands.SETNER),
    Arguments.of("+10", 10, CommandParser.Commands.SETNER),
    Arguments.of("0", 0, CommandParser.Commands.SETNER),
    Arguments.of(" cut", -1, CommandParser.Commands.CUT),
    Arguments.of("+  ", -1, CommandParser.Commands.ADDNOTE),
    Arguments.of("-", -1, CommandParser.Commands.REMOVENOTE),
    Arguments.of(" . ", -1, CommandParser.Commands.GOTO),
    Arguments.of("..", -1, CommandParser.Commands.PARENT),
    Arguments.of("exit", -1, CommandParser.Commands.EXIT)
    );
  }

  /**
   * Constructor Test to check if the command is properly separated. 
   */
  @ParameterizedTest
  @MethodSource("argumentsSize1")
  public void ConstructorSize1Test(String commandline, int ner, CommandParser.Commands command) throws Exception {
    CommandParser result = new CommandParser(commandline);
    assertEquals(ner, result.getNer());
    assertEquals(command, result.getCommand());
  }


  /**
   * Arguments for the Constructor Test with a command containing two arguments (ConstructorSize2Test). 
   */
  static Stream<Arguments> argumentsSize2() {
    return  Stream.of(
    Arguments.of("2 cut ", 2, CommandParser.Commands.CUT, null),
    Arguments.of("7 .", 7, CommandParser.Commands.GOTO, null),
    Arguments.of("3 -", 3, CommandParser.Commands.REMOVENOTE, null),
    Arguments.of("find file", -1, CommandParser.Commands.FIND, "file"),
    Arguments.of("mkdir test", -1, CommandParser.Commands.MKDIR, "test"),
    Arguments.of("5 exit", 5, CommandParser.Commands.EXIT, null),
    Arguments.of("+ 9 truc", -1, CommandParser.Commands.ADDNOTE, "9 truc")
    );
  }

  /**
   * Constructor Test to check if the command is properly separated
   * and if the order of arguments is respected.
   */
  @ParameterizedTest
  @MethodSource("argumentsSize2")
  public void ConstructorSize2Test(String commandline, int ner, CommandParser.Commands command, String name) throws Exception {
    CommandParser result = new CommandParser(commandline);
    assertEquals(ner, result.getNer());
    assertEquals(command, result.getCommand());
    assertEquals(name, result.getName());
  }


  /**
   * Arguments for the Constructor Test with a command containing three arguments (ConstructorSize3Test). 
   */
  static Stream<Arguments> argumentsSize3() {
    return  Stream.of(
    Arguments.of("2 + ceci est un test ", 2, CommandParser.Commands.ADDNOTE, "ceci est un test"),
    Arguments.of("  2  + hello    world  ", 2, CommandParser.Commands.ADDNOTE, "hello    world")
    );
  }

  /**
   * Constructor Test to check if the command is properly separated
   * and if the order of arguments is respected.
   */
  @ParameterizedTest
  @MethodSource("argumentsSize3")
  public void ConstructorSize3Test(String commandline, int ner, CommandParser.Commands command, String name) throws Exception {
    CommandParser result = new CommandParser(commandline);
    assertEquals(ner, result.getNer());
    assertEquals(command, result.getCommand());
    assertEquals(name, result.getName());
  }  


  /**
   * Bad arguments for the Constructor Test. 
   */
  static Stream<Arguments> badArguments() {
    return  Stream.of(
    Arguments.of("2 cuy ", "Invalid command with two arguments. See help."),
    Arguments.of("7 test 6 +", "Invalid command with three arguments. See help."),
    Arguments.of("test 6 +", "Invalid command with two arguments. See help."),
    Arguments.of("-5 ", "Invalid command with one argument. See help.")
    );
  }

  /**
   * Constructor Test to check if the command is properly separated
   * and if the order of arguments is respected.
   */
  @ParameterizedTest
  @MethodSource("badArguments")
  public void ConstructorBadArgumentsTest(String commandline, String error) throws Exception {
    Exception exception = assertThrows(Exception.class, () -> {
      CommandParser result = new CommandParser(commandline);
    });
    
    assertEquals(error, exception.getMessage());
  }
}
