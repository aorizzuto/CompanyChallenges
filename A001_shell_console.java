
import java.io.*;
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

    CommandFactory commandFactory = new CommandFactory();
    final int _FILE_MAX_CHARACTERS_ = 100;  // Max characters for filename

    /* Constructor */
    public Shell() {}

    /* Start method */
    public void start(){
        printInitialMessage();            // Print initial message
        readCommands();                     // Start reading commands
    }

    /* Initial message when shell begin */
    public void printInitialMessage(){
        System.out.println("\nSSH Terminal v1.0\nAuthor: Alejandro O. Rizzuto \t\t2021/04/17\n");
    }

    /* Method where we read the commands */
    public void readCommands(){
        Scanner scan = new Scanner(System.in);  // Scanner to get the input commands
        boolean done = true;                    // Flag to check if the command is "quit"
        
        while(done){
            printPrefix();                      // Prefix before each command. Usually ">>". This has been commented due the Hackerrank tests.
            String command = scan.nextLine();   // Get command
            if(!checkCommand(command))          // Check if "quit"
                executeCommand(command);        // Execute command if not
            else
                done = false;                   // Exit if it is.     
        }
        scan.close();
    }

    /* Prefix */
    public void printPrefix(){
        System.out.print(">>");             // Print ">>" before each command
    }

    /* Check command */
    public boolean checkCommand(String command){
        return command.contains("quit");    // check if the command contain quit. I could do "command.equals("quit");" also.
    }

    /* Execute Command */
    public void executeCommand(String command){
        Command cmd = commandFactory.getCommand(command);   // Get command from factory
        if (cmd != null) cmd.execute();                     // Execute command
    }
}

/********************************************/

class CommandFactory {
    final int _FILE_MAX_CHARACTERS_ = 100;  // Max characters for filename

    public CommandFactory(){
        Command.homeDirectoryIntoStack();   // Add home directory to the stack of directories
    }

    public Command getCommand(String command){
        String comm = command.split(" ")[0];        // Get first part of command and create an object of that command.

        if(comm.equals("pwd"))      return new CommandPWD();
        if(comm.equals("ls"))       return new CommandLS(command);
        if(comm.equals("mkdir"))    return new CommandMKDIR(command);
        if(comm.equals("cd"))       return new CommandCD(command, _FILE_MAX_CHARACTERS_);
        if(comm.equals("touch"))    return new CommandTOUCH(command, _FILE_MAX_CHARACTERS_);
        if(!command.equals(""))     System.out.println("Unrecognized Command");
       
        return null;    // If we press ENTER we add a new line waiting for another command
    }
 }

/********************************************/

abstract class Command {    /*   */
    protected static Stack<String> stackPaths = new Stack<>();

    public Command(){ }

    public static void homeDirectoryIntoStack(){
        stackPaths.add("/home"); // "/root" for challenge
    }

    /* Set relative path */
    public void addPathToRelativePath(String relPath){
        stackPaths.add(relPath);
    }
    
    /* Get relative path */
    public String getRelativePath(){
        return String.join("",stackPaths);
    }

    abstract void execute();
}

/********************************************/

class CommandPWD extends Command {

    @Override
    public void execute() {
        System.out.println(getRelativePath());  // Get the relative path where I am
    }
 }

/********************************************/

class CommandLS extends Command {

    private String cmd;
    List<String> listOfFiles = new ArrayList<String>();

    public CommandLS(String command){
        this.cmd = command;
    }

    @Override
    void execute() {

        if(wrongNumberOfParameters()) return;
        
        if (cmdContainFlag_R()) printRecursive(); 
        else                    printNotRecursive("");   
    }

    private boolean wrongNumberOfParameters() {
        String[] parameters = cmd.split(" ");
        if (parameters.length > 3){
            System.out.println("Unknown command");
            return true;
        }
        return false;
    }

    private boolean cmdContainFlag_R() {
        return cmd.contains(" -r"); // I add the "space" because we also call "ls" like this "ls /dir1/ale-rizzuto/" and this is not recursive
    }

    private void printRecursive() {
        try{
            File f = new File(getRelativePath());

            List<String> listOfDirectories = Arrays.asList(getDirectories(f));
            listOfFiles = Arrays.asList(f.list());    // With "list" I got every file/directory

            for (String file : listOfFiles) {
                if (listOfDirectories.contains(file)){
                    System.out.println(getRelativePath()+'/'+file);
                    printNotRecursive("/"+file);
                }else{
                    System.out.println(file); // Print the names of file
                }            
            }
        }catch(Exception e){
            e.printStackTrace();
        }  
    }

    private void printNotRecursive(String extraPath){
        try{
            File f = new File(getRelativePath()+extraPath);

            listOfFiles = Arrays.asList(f.list());    // With "list" I got every file/directory

            for (String file : listOfFiles)
                System.out.println(file); // Print the names of file
        }catch(Exception e){
            e.printStackTrace();
        }  
    }

    private String[] getDirectories(File f) {
        return f.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) { return new File(current, name).isDirectory(); }});
    }
}

/********************************************/

class CommandMKDIR extends Command {

    private String cmd;

    public CommandMKDIR(String command){
        this.cmd = command;
    }

    @Override
    void execute() {
        try{
            if ( cmd.split(" ").length == 1){
                System.out.println("Invalid Command");
                return;
            }

            List<String> directories = new LinkedList<String>(Arrays.asList(cmd.split(" ")));
            directories.remove(0); // First string is "mkdir"
            
            // This allow to create multiple directories at once, like a normal shell.
            for (String directory : directories){
                File file = new File(getRelativePath() +"/"+ directory);
                
                if(file.exists()){  // Check if directory exists 
                    System.out.println("Directory already exists");
                    break;
                }

                file.mkdir();    //Creating the directory
            }
        }catch(Exception e){
            e.printStackTrace();
        }        
    }
}

/********************************************/

class CommandCD extends Command {

    private String cmd;
    private int maxLength;

    public CommandCD(String command, int maxLength) {
        this.cmd = command;
        this.maxLength = maxLength;
    }

    @Override
    void execute() {
        List<String> list = new ArrayList<String>();
        list = Arrays.asList(cmd.split(" "));

        if (list.size() > 2){   // If more than 2 parameters
            System.out.println("Invalid Command");
            return;
        }

        if (list.size() == 1){  // If only was called "cd"
            String first = stackPaths.firstElement();
            stackPaths.clear();
            stackPaths.add(first); // Keeps only first element
            return;
        }

        //If 2nd parameters is ".." and is not root path
        if (list.get(1).equals("..")){
            if (stackPaths.size() != 1){
                stackPaths.pop(); // Remove last element of stack
            }else{
                System.out.println("Directory not found");
            }
            return;
        }

        if (list.get(1).length() > maxLength){
            System.out.printf("Invalid File or Folder Name");
            return;
        }

        File file = new File(getRelativePath() +"/"+ list.get(1));
        
        if(!file.isDirectory()){             // Checking if directory exists
            System.out.println("Invalid path");
            return;
        }

        String[] dirs = list.get(1).split("/");
        for (String dir : dirs) {
            addPathToRelativePath("/" + dir);   // I add the new directory to the relative path.
        }
    }
}

/********************************************/

class CommandTOUCH extends Command {

    private String cmd;
    private int maxLength;

    public CommandTOUCH(String command, int maxLength) {
        this.cmd = command;
        this.maxLength = maxLength;
    }
    
    @Override
    public void execute () {        
        try {
            if ( cmd.split(" ").length == 1){
                System.out.println("Invalid Command");
                return;
            }

            List<String> files = new LinkedList<String>(Arrays.asList(cmd.split(" ")));
            files.remove(0);
            
            // This allow to create multiple files at once, like a normal shell.
            // Use: touch <file1.txt> <file2.txt> ...
            for (String file : files){
                if (file.length() > maxLength){
                    System.out.printf("Invalid File or Folder Name");
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
    }
}
