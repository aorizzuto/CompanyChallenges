
import java.io.*;
import java.nio.file.*;
import java.util.*;

/*
Author: Alejandro Rizzuto
Date: 2021/04/17

This should be created in different packages. 
Some functions could be split in modules or little functions.

TODO:
1 - Create main class           DONE
2 - shell class                 DONE
3 - Use relative path           DONE
4 - Create command class with commands to implement               DONE
5 - Implement each command: pwd, quit, cd, ls, mkdir, touch       DONE
6 - ls with "-r"                      
7 - try-catch to handle exceptions    DONE

 */
public class A001_shell_console {

    public static void main(String[] args) {
        startShell();               // Start new shell
    }

    public static void startShell(){
        Shell shell = new Shell();  // Create a new instance of shell
        shell.start();              // Start instance
    }
}

/********************************************/

class Shell {
    // Class Shell where we handled all shell's commands.

    final int _FILE_MAX_CHARACTERS_ = 100;  // Max characters for filename
    CommandsImpl commands;                  // Commands implementation object

    /* Constructor */
    public Shell() {
        commands = new CommandsImpl();      // New object for commands
    }

    /* Start method */
    public void start(){
        printInitialMessage();            // Print initial message
        readCommands();                     // Start reading commands
    }

    /* Initial message when shell begin */
    public void printInitialMessage(){
        System.out.println("\nSSH Terminal v1.0");
        System.out.println("Author: Alejandro O. Rizzuto \t\t2021/04/17\n");
    }

    /* Method where we read the commands */
    public int readCommands(){
        Scanner scan = new Scanner(System.in);  // Scanner to get the input commands
        boolean done = true;                    // Flag to check if the command is "quit"
        int retCode = 0;                        // Return code. 0:success, 1:fail
        
        while(done){
            printPrefix();                      // Prefix before each command. Usually ">>". This has been commented due the Hackerrank tests.
            String command = scan.nextLine();   // Get command
            if(!checkCommand(command))          // Check if "quit"
                executeCommand(command);        // Execute command if not
            else
                done = false;                   // Exit if it is.     
        }

        scan.close();

        return retCode;
    }

    /* Prefix */
    public void printPrefix(){
        System.out.print(">>");             // Print ">>" before each command
    }

    /* Check command */
    public boolean checkCommand(String command){
        return command.contains("quit");    // check if the command contain quit.
                                            // I could do "command.equals("quit");" also.
    }

    // Create enum of commands. This should be part of another class.
    public enum Cmd { pwd, ls, mkdir, cd, touch } 

    /* Execute Command */
    public void executeCommand(String command){
      
        String comm = command.split(" ")[0];

        try{
            switch(Cmd.valueOf(comm).ordinal()){
                case 0:     executeCommandPWD(); break;
                case 1:      executeCommandLS(command); break;
                case 2:   executeCommandMKDIR(command); break;
                case 3:      executeCommandCD(command); break;
                case 4:   executeCommandTOUCH(command); break;
                default:  commands.printUnrecognized(); break;   // If another command -> Unrecognized.        
            }
        }catch(Exception e){
            if(!command.isEmpty()) commands.printUnrecognized();
        }
    }

    /************** COMMANDS **************/
    /* PWD */
    public void executeCommandPWD(){ commands.executePWD(); }
    /* LS */
    public void executeCommandLS(String command){ commands.executeLS(command); }
    /* MKDIR */
    public void executeCommandMKDIR(String command){ commands.executeMKDIR(command); }
    /* CD */
    public void executeCommandCD(String command){ commands.executeCD(command, _FILE_MAX_CHARACTERS_); }
    /* TOUCH */
    public void executeCommandTOUCH(String command){ commands.executeTOUCH(command, _FILE_MAX_CHARACTERS_); }
}

/********************************************/

interface Commands {
    /* Use this interface to add commands to implement*/
    public boolean executePWD();    
    public boolean executeLS(String cmd);    
    public boolean executeMKDIR(String cmd);
    public boolean executeCD(String cmd, int maxChar);
    public boolean executeTOUCH(String cmd, int maxChar);
}

/********************************************/

class CommandsImpl implements Commands{
    /* Command implementation */

    private String relativePath = "/";  // Path where I' start

    /* Constructor */
    public CommandsImpl(){
        homeDirectory();      // When the object is created I'll find the home directory and save it
    }

    /* Get relative path */
    public String getRelativePath(){
        return this.relativePath;
    }

    /* Set relative path */
    public void setRelativePath(String relPath){
        this.relativePath = relPath;
    }

    /* Get start directory */
    public void homeDirectory(){
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        setRelativePath(s);
        //setRelativePath("/root"); // Used for challenge
    }

    /* PWD */
    public boolean executePWD() {
        System.out.println(getRelativePath());  // Get the relative path where I am
        return true;
    }

    /* MKDIR */
    public boolean executeMKDIR(String cmd) {
        try{
            if ( cmd.split(" ").length == 1){
                printInvalid();
                return false;
            }

            List<String> directories = new LinkedList<String>(Arrays.asList(cmd.split(" ")));
            directories.remove(0);
            
            // This allow to create multiple directories at once, like a normal shell.
            for (String directory : directories){
                File file = new File(getRelativePath() +"/"+ directory);
                boolean bool = file.exists();     // Check if directory exists 
                
                if(bool){
                  printDirectoryExists(); 
                  break;
                }

                bool = file.mkdir();    //Creating the directory
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return true;
    }

    /* CD */
    public boolean executeCD(String cmd, int maxChar){
        List<String> list = new ArrayList<String>();
        list = Arrays.asList(cmd.split(" "));

        if (list.size() > 2){
            printInvalid();
            return false;
        }
        if (list.size() == 0){
            homeDirectory();
            return true;
        }

        File file = new File(getRelativePath() +"/"+ list.get(1));
        boolean bool = file.exists();    // Checking if directory exists
        
        if(!bool){
            printDirectoryNotFound();
            return false;
        }    

        if (list.get(1).length() > maxChar){
            printInvalidFileFolder();
            return false;
        }

        String s = getRelativePath() + "/" + list.get(1); // I change relative path instead force change directory.
        setRelativePath(s);
        return true;
    }

    /* TOUCH */
    public boolean executeTOUCH(String cmd, int maxChar) {        
        try {
            if ( cmd.split(" ").length == 1){
                printInvalid();
                return false;
            }

            List<String> files = new LinkedList<String>(Arrays.asList(cmd.split(" ")));
            files.remove(0);
            
            // This allow to create multiple files at once, like a normal shell.
            // Use: touch <file1.txt> <file2.txt> ...
            for (String file : files){
                if (file.length() > maxChar){
                    printInvalidFileFolder();
                    continue; // Continue with next one
                }
            
                File myObj = new File(getRelativePath() +"/"+ file);    // Use relative path to create file
                boolean ret = myObj.createNewFile(); // Commented for Challenge

                // Normal shell doesn't print anything with "touch". In case we want to print anything we need to use the following line:
                //if (!ret) System.out.println("File already exists.");
            }
        } catch (IOException e) {   // When createNewFile is uncommented -> Uncomment try-catch
           e.printStackTrace();
        }

        return true;
    }

    /* LS */
    public boolean executeLS(String cmd) {
        try{
            boolean flag_R = false;

            if (cmd.contains("-r")){ flag_R = true; }

            List<String> listOfFiles = new ArrayList<String>();

            File f = new File(getRelativePath());

            if (flag_R){
                String[] directories = f.list(new FilenameFilter() {
                @Override
                public boolean accept(File current, String name) { return new File(current, name).isDirectory(); }});

                for (String directory : directories) {
                    System.out.println(directory);

                    f = new File(getRelativePath()+"/"+directory);
                    listOfFiles = Arrays.asList(f.list());    // With "list" I got every file/directory

                    for (String file : listOfFiles) {
                        System.out.println("\t"+file); // Print the names of files and directories
                    }
                }
            }
            else{
                listOfFiles = Arrays.asList(f.list());    // With "list" I got every file/directory

                for (String file : listOfFiles) {
                    System.out.println(file); // Print the names of files and directories
                }
            }

            return true;
        }catch(Exception e){
            return false;
        }
    }

    /*********** PRINTS *************/
    public void printInvalid(){      System.out.println("Invalid Command"); }
    public void printUnrecognized(){ System.out.println("Unrecognized Command"); }
    public void printInvalidFileFolder() { System.out.printf("Invalid File or Folder Name"); }
    public void printDirectoryNotFound() { System.out.println("Directory not found"); }
    public void printDirectoryExists() { System.out.println("Directory already exists"); }
}
