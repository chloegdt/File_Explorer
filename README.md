# User Manual
GODET Chloé & GROSJACQUES Marwane, M1 Informatique
![-----------------------------------------------------](https://raw.githubusercontent.com/andreasbm/readme/master/assets/lines/aqua.png)
## Presentation
This program was made during the course "complément de programmation" of the first year Master in Computer Science.

The purpose of this mini-project is to develop a command-line file manager. It allows basic commands such as copy/paste, create a directory, search for a file...

## Requierements

 - Java version 17 (with JDK)
 
## How to use the program ?

### Running the jar 
To start the program, enter the following command line into your terminal:
```
java -jar target/explorer-1.0-jar-with-dependencies.jar [path]
```
*path* is optional: it allows you to run the program in the directory of your choice
Otherwise, the program will launch in the current directory.
> Note : An error will be displayed if the *path* does not point to a directory

The program's interface will then be displayed in the terminal.



### Interface Description

![interface](https://github.com/ensYeh/miniprojet-grp-56_04/blob/main/ressources/interface.png?raw=true)

The interface is divided into 4 parts:

 - The absolute path of the current directory (at the top)
 - The list of files and directories contained in the current directory (on the left)
	*each item in the directory is designated by its NER*
 
 - The information block (on the right) contains the information about the current NER and displays different results depending on the commands entered by the user (see Usage section)
 -	the command prompt, where the user enters the different commands
 
 
You can navigate through these 4 components using the TAB key.
 
You can use the arrows to read all of the content displayed.
For example, if the list of files in the current directory is too long, it is possible to display the following items using the down arrow when you are in the files block.
> Note : You can only write in the Command Prompt part


### Utilisation

When the program is opened, the information block indicates that there is no current NER.

You can enter a number of commands that require a different number of arguments. These commands are detailed below.

If a command requires a set number of arguments, and you provide more, the last arguments entered will be ignored.

---
#### CURRENT NER 
You can enter an NER (which must designate an item in the current directory) and confirm by pressing the enter key.

Command : 
```
NER
```

The information block then displays the current NER and the notes (if any) associated with it.

> If the NER you enter does not specify an item in the current directory, an error window will be displayed. To close it, press Enter.

---
#### Change directory
You can browse the file tree.
To do this, two commands are possible:

→ To go to the designated directory:
 ```
NER .
```
where *NER* is an item in the directory.

> If a current NER is already indicated, it is not necessary to give it.

> This command only works if the current item designated is a directory. If this is not the case, an error window will be displayed. To close it, press Enter.

→ to go up a directory

 ```
..
```

> This command works without NER.


---
#### Create a directory

It is possible to create a new directory from the current directory.
 ```
mkdir dirname
```
where *dirname* is the name you want to give to the new directory.
> If a directory with the same name already exists, an error window will be displayed. To close it, press Enter.

---
#### View the contents of a file

With the **Visu** command, you can potentially view the contents of a file.
```
NER visu
```
where NER is an item in the directory.
> If a current NER is already indicated, it is not necessary to give it.

Careful, if:
→ The designated item is a text file		
		The info block will display the contents of the text file.

→ The designated item is an image
		The image will be opened with the default viewer.
  We used the Desktop class from the java.awt package (https://docs.oracle.com/javase/8/docs/api/java/awt/Desktop.html).

→ The designated item is a directory or other file type
		Only the size of the designated item will be displayed.
		
---
#### Find a file from the current directory
It is possible to search through the file aborescence (starting at the current directory) for a particular file or directory.

```
find filename
```
where *filename* is the exact name of the file you want to search.
> Be careful, the **find** function is case-sensitive

---
#### Copy/Cut and Paste
→  Copy
It is possible to copy a file or directory with the following command:
```
NER copy
```
where *NER* is an item in the directory.
> If a current NER is already indicated, it is not necessary to give it.

> If a directory is copied, all of its contents are copied as well.


→ Cut
```
NER cut
```
where *NER* is an item in the directory.
> If a current NER is already indicated, it is not necessary to give it.

The file is then stored. However, it will be deleted as soon as the paste command is used.

Be careful, if you used the copy/cut command with a file, and then reuse this command without pasting, the new copy/cut file will replace the previous one 

→ Paste
```
paste
```
> This command works without NER.

> The item will be copied to the current directory.

> If nothing has been copied previously, an error window will indicate this. To close it, press Enter.

---
#### Add a note

You can annotate a file or directory with the following command :
```
NER + note
```
where *note* is the note you want to add to the element designated by the NER.
> If a current NER is already indicated, it is not necessary to give it.

> It is not necessary to fill in the note in quotation marks.

> The note can contains spaces.

This note will be displayed in the "Information" section and will be visible whenever the NER entered matches this item in the directory.
This note is retained and will be displayed at the next launch of the program.


---
#### Remove a note
It is not possible to delete a specific note.
However, it is possible to delete all notes associated with an item in the current directory.
```
NER -
```
> If a current NER is already indicated, it is not necessary to give it.


When a user enters a note for an item in the repertoire designated by its NER or deletes all of the notes associated with this NER, the annotations file (named 'notes') is updated.
This 'notes' file is saved each time the user changes directories and when the program is exited.
> If the program is interrupted in an unusual way (forced), the 'notes' file in the current directory is not saved.

---
#### View help

You can ask for help at any time.
```
help
```
> This command work without NER.

→ If there is no current NER and you don't enter one

In the information section, you will see :

 - A message indicating the absence of a current NER
 - Commands can be made by entering an NER
 - Commands possible without NER designation
 - Help to change focus
 - Redirecting to this manual if the help is not enough to answer your questions

→ If there is a current NER or if you enter one


In this case, the information displayed will be specific to the item type:

 -the current NER
 - The type of designated item (a file/directory)
 - Possible commands depending on the type of current item
 - Commands possible without NER designation
 - Help to change focus
 - Redirecting to this manual if the help is not enough to answer your questions
 ---
#### Quit the program
```
exit
```
closes the interface and exits the program.

---
### Alias
Each command with a special character has an alias that can also be used:

 - GO TO can replaces the dot in the commande`NER .`
 - PARENT can replaces the dot dot  `..`
 - ADDNOTE can replaces the add symbol in the command `NER + note`
 - REMOVENOTE can replaces the minus symbol in the command`NER -`
 
 ### Examples of possible commands
 → SET NER
```
2
```
*the current NER denotes an item in the current directory*


→ GO TO DIRECTORY
```
2 .
```
*or*
```
.
```
*if item 2 was already the current NER*

→ GO TO PARENT
```
..
```

→ CREATE A DIRECTORY
```
mkdir test
```


→ COPY
```
1 copy
```
→ CUT
```
1 CUT
```
→ PASTE
```
paste
```
→ ADD A NOTE
```
1 + this is my note
```
```
+ another note for this element
```
→ REMOVE NOTE(S)
```
-
```
*if item 1 is the current NER*
```
2 -
```
*specifies the item for which I want to remove the notes*
>If there are no notes, nothing will happen.

→ HELP
```
3 help
```
*request for specific help for element 3*
*or*
```
help
```

## Content

![tree](https://github.com/ensYeh/miniprojet-grp-56_04/blob/main/ressources/tree.png?raw=true)


## Possible improvements

If the project needs improvement, we would like to add commands such as:
  - **remove**: remove an element from the directory  
  - **grep**: display lines containing pattern(s) in a file 
  - **touch**: create a file


The planned improvements are:
 - autocompletion  
 - command history (for example being able to avoid retyping commands by using the top arrow to access the last commands used)   
 - use of the Esc key (similar to the exit command)

In addition to the keyboard improvements and new commands, we could consider being able to modify a text document directly from our program.  Additionally, we could possibly try to manage permissions for certain types of commands. 
For example, in 'home' we cannot save notes because we are not in 'sudo' mode. We could try changing modes in order to have access to more permissions. 
