   # Technical Manual
 
 ## Content
The program is composed of 6 classes which will be described in the "Description" section. For each of these classes (except main), a file with tests is also present.


 ## Librairies

 ### Natives

   * **java . io** and **java . nio** for input/output with the file system
   * **java . awt** to open an image with the default viewer
   * **java . util** to use collections to store data (hashmap, arrays and stream)
  
 ### Dependencies

   * **Junit 5.9.1** to test the program
   * **Commons-io** 1.3.2 to manipulate directories recursively
   * **Lanterna 3.1.1** to build an interactive terminal user interface (TUI)

 ## Compile the program
 
To compile the executable jar required to start the program, execute the 'package' cycle of maven.
 
 On linux : 
 ```
 ./mvnw package
 ```
On windows :
```
mvnw.cmd package
```

This cycle will do the following:

 * Launch the checkstyle
 * Prepare the code coverage
 * Compile the program
 * Run the tests
 * Report the code coverage
 * Build the jar
 * Build the jar with dependencies

The jar executable file can then be found under the 'target' folder (see User Manual).

 ## Description  
As mentioned in the "Content" section, the program consists of 6 classes.

![interface](https://github.com/ensYeh/miniprojet-grp-56_04/blob/main/ressources/class_tree.png?raw=true)
 
 ### ExplorerApp
    
  This is the main class used to start the program.
    
  It manages the arguments given by the user, creates a new session and starts the latter.
    
  ### Session
    
  This class keeps track of the state of the program during its usage.
    
  It implements the method onIput to intercept keystrokes.  
  Particularly, when the user presses \<Enter\>, the session will do the following:  
    
   * Read user input with the class Interface
   * Process user input with the class CommandParser
   * Execute the command by calling the appropriates methods of the class Directory itself calling the methods of the class Notes
   * Refresh the interface with the class Interface
     
   > Any error encountered during the process will be showned to the user through the class Interface
    
  ### Interface
    
  Builds and manages the user interface.
    
  ### Directory
    
  Manages a directory and its notes.
  Most of the commands will be executed through this class.
    
  ### Notes
    
 Manages the notes of the elements in a directory.
This class allows you to add a note, delete it, save it, load it (when changing the directory or when you restart the program).
  
  ### Command Parser
   
This class will parse the input entered by the user.
To do so, it will split the input on blocks of spaces into at most two parts.
Each part will then be verified.
Depending on the number of arguments given, the class will allow, thanks to its different methods, to define whether the NER entered is valid, whether the given command exists and is usable, if the order in which the arguments are entered is respected.

  
 ## Example of a command being parsed
    
  The given string input is splitted on the first 3 spaces occurences (or less if there are not enough space character). Each part will then be parsed and translated to the wanted type.
  For example,   
    ```3 + ceci est un texte```

  will be splitted like so,
    
  ```{"3", "+", "ceci est un texte"}```
    
   The parser will then check that:  
    
   * "3" is a valid number and translate it to int
   * "+" is a valid command and translate it to an enum variant of Commands

 "ceci est un texte" will not be checked and stay as a String.
 
  ## Code Coverage
  
 The library JaCoCo is used for the code coverage.  
 The report can be found in the folder target/site/jacoco and viewed with the file index.hmtl in your favorite browser.
    
   > The report is generated after executing the tests.

![interface](https://github.com/ensYeh/miniprojet-grp-56_04/blob/main/ressources/code_coverage.png?raw=true)
    
Our code coverage is around 90% mostly because the interface cannot be tested fully automatically.

The class ExplorerApp is not included as it is the main class that start the program and only contains the method main.

  ## Checkstyle
    
The checkstyle runs during the "validate" phase of the Maven lifecycle. When the program is compiled and the jar is generated, it is possible to view the result of this check. At the moment, our program follows the conventions and we don't have any checkstyle error.

## Javadoc

The documentation can be built with the following command:
> ./mvnw javadoc:javadoc

It can then be found in the folder **target/site/apidocs** with the file **index.html**

