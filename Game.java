import java.util.Stack;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes 
 * @version 2006.03.30
 * 
 * @author Ahmed Rashid 101256081
 * @version 2024-03-16
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room previousRoom;
    private Stack<Room> previousRoomStack;
    private Item itemTaken; // keep track of each item that is being taken and dropped
    private int numOfPickups; // keep track of the number of times user has taken an item
    private Room chargedRoom; // keep track of the room to go to if beamer is fired
    /**
     * Create the game and initialise its internal map, as well
     * as the previous room (none) and previous room stack (empty).
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        previousRoom = null;
        previousRoomStack = new Stack<Room>();
        itemTaken = null;
        numOfPickups = 0;
        chargedRoom = null;
    }
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office;
        
        Item chair1, chair2, chair3, chair4, chair5, bar, computer1, computer2, 
        computer3, tree1, tree2, cookie1, cookie2, cookie3, cookie4;
        
        TransporterRoom transporterRoom;
        
        Beamer beam1, beam2;
        
        // create some items
        chair1 = new Item("a wooden chair",5, "chair");
        chair2 = new Item("a wooden chair",5, "chair");
        chair3 = new Item("a wooden chair",5, "chair");
        chair4 = new Item("a wooden chair",5, "chair");
        chair5 = new Item("a wooden chair",5, "chair");
        
        bar = new Item("a long bar with stools",95.67, "bar");
        
        computer1 = new Item("a PC",10, "computer");
        computer2 = new Item("a Mac",5, "computer");
        computer3 = new Item("a PC",10, "computer");
        
        tree1 = new Item("a fir tree",500.5, "tree");
        tree2 = new Item("a fir tree",500.5, "tree");
       
        cookie1 = new Item("a cookie", 0.2, "cookie");
        cookie2 = new Item("a cookie", 0.2, "cookie");
        cookie3 = new Item("a cookie", 0.2, "cookie");
        cookie4 = new Item("a cookie", 0.2, "cookie");
        
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        
        //initialize the beamers.
        beam1 = new Beamer("a wonderful beamer",1.0, "beamer"); 
        beam2 = new Beamer("a wonderful beamer",1.0, "beamer");
        
        // initialize the transporter room
        
        transporterRoom = new TransporterRoom("You are in the transporter room!");
        // put items in the rooms
        outside.addItem(tree1);
        outside.addItem(tree2);
        outside.addItem(beam1); // add a beamer outside
        theatre.addItem(chair1);
        theatre.addItem(cookie1); // add a cookie to the theatre
        pub.addItem(bar); // add a cookie to the pub
        pub.addItem(cookie2); 
        lab.addItem(chair2);
        lab.addItem(computer1);
        lab.addItem(chair3);
        lab.addItem(computer2);
        lab.addItem(beam2); // add a beamer to the lab
        office.addItem(chair4);
        office.addItem(computer3); 
        office.addItem(cookie3); // add a cookie to the office
        transporterRoom.addItem(cookie4);
        transporterRoom.addItem(chair5);
        
        outside.setExit("east", theatre); 
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);
        pub.setExit("west", transporterRoom);
        
        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);
        office.setExit("north", transporterRoom);

        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        itemsAndCarry(); // new description method to show items being carried
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }
        else if (commandWord.equals("charge")) {
            charge(command);
        }
        else if (commandWord.equals("fire")) {
            fire(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }
    /**
     * Print the description of the rooms items and the items being carried
     */
    private void itemsAndCarry(){
        System.out.println(currentRoom.getLongDescription());
        
        if(itemTaken != null){
            System.out.println("You are carrying: ");
            System.out.println("    " + itemTaken.getDescription());
        }
        else{
            System.out.println("You are not carrying anything");
        }
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * If we go to a new room, update previous room and previous room stack.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Attempt to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom; // store the previous room
            previousRoomStack.push(currentRoom); // and add to previous room stack
            currentRoom = nextRoom;
            itemsAndCarry(); // new description method
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /** 
     * "Look" was entered. Check the rest of the command to see
     * whether we really want to look.
     * 
     * @param command The command to be processed
     */
    private void look(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Look what?");
        }
        else {
            // output the long description of this room
            itemsAndCarry(); // new description method
        }
    }
    
    /** 
     * "Eat" was entered. Check the rest of the command to see
     * whether the item in hand is a cookie.
     * 
     * @param command The command to be processed
     */
    private void eat(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Eat what?");
        }
        else {
            // if something was picked up
            if(itemTaken.getItemName().equals("cookie")){ // check if item is cookie
                System.out.println("You have eaten and are no longer hungry.");  
                itemTaken = null; 
                numOfPickups += 5;
            }
            else{
                System.out.println("You must take a cookie"); 
            }   
        }
    }
    
    /** 
     * "Back" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     */
    private void back(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Back what?");
        }
        else {
            // go back to the previous room, if possible
            if (previousRoom==null) {
                System.out.println("No room to go back to.");
            } else {
                // go back and swap previous and current rooms,
                // and put current room on previous room stack
                Room temp = currentRoom;
                currentRoom = previousRoom;
                previousRoom = temp;
                previousRoomStack.push(temp);
                // and print description
                itemsAndCarry();
            }
        }
    }
    
    /** 
     * "StackBack" was entered. Check the rest of the command to see
     * whether we really want to stackBack.
     * 
     * @param command The command to be processed
     */
    private void stackBack(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("StackBack what?");
        }
        else {
            // step back one room in our stack of rooms history, if possible
            if (previousRoomStack.isEmpty()) {
                System.out.println("No room to go stack back to.");
            } else {
                // current room becomes previous room, and
                // current room is taken from the top of the stack
                previousRoom = currentRoom;
                currentRoom = previousRoomStack.pop();
                // and print description
                itemsAndCarry();
            }
        }
    }
     /** 
     * "take" was entered. Check the rest of the command to see
     * if we can take an item 
     * 
     * @param command The command to be processed
     */
    private void take(Command command){
        if(!command.hasSecondWord()) {
            System.out.println("take what?"); // only a single word
        }
        else{
            String itemPickedUp = command.getSecondWord();
            
            // check if item is in room and user has not picked something up
            if(currentRoom.itemInRoom(itemPickedUp) && itemTaken == null){ 
        
                // check if user has taken a cookie.
                if(itemPickedUp.equals("cookie")){ // trying to take a cookie
                    if(currentRoom.cookieInRoom()){ // if cookie is in the room
                        System.out.println("You picked up a cookie"); // user found and took the cookie
                        itemTaken = currentRoom.findItem(itemPickedUp);
                        currentRoom.takeItem(itemPickedUp); // take item from room
                    }
                }
    
                else{ // user wants to take something other than a cookie
                    if(numOfPickups > 0){ // check if user has ate a cookie before
                        System.out.println("You picked up a " + itemPickedUp);
                        itemTaken = currentRoom.findItem(itemPickedUp);
                        currentRoom.takeItem(itemPickedUp); // take item from room
                        numOfPickups--;
                    }
                    else{
                        System.out.println("Must eat a cookie before taking an item");
                    }
                }
                
            }
            
            else if(!currentRoom.itemInRoom(itemPickedUp)){ // if item NOT in the room
                System.out.println(itemPickedUp + " is not in the room");
            }
            else if(itemTaken != null){ // if item in hand.
                System.out.println("You have already taken " + itemTaken.getItemName());
            }
        }
    }
    private void drop(Command command){
        if(command.hasSecondWord()) {
            System.out.println("drop what?"); // only a single word
        }
        else{
            if(itemTaken != null){ // if theres an item in hand
                // add item taken to this room
                currentRoom.addItem(itemTaken); 
                System.out.println("You have dropped a " + itemTaken.getItemName());
                itemTaken = null; // nothing in users hand when item is dropped
            }
            else{
                System.out.println("You must take an item before dropping it");
            }
        }
    }
    private void charge(Command command){
        if(command.hasSecondWord()) {
            System.out.println("charge what?"); // only a single word
            return;
        }
        if(itemTaken instanceof Beamer){ // check if item taken is a beamer
            if( ((Beamer) itemTaken).charge()){ // downcast to beamer to use the charge method
                // if it charges, print that its charged 
                // now, we need to keep track of the current room
                System.out.println("Beamer successfully charged. ");
                chargedRoom = currentRoom;
            }
            else{
                System.out.println("Beamer has already been charged. ");
            }
        }
        else{
            System.out.println("You have no beamer to charge");
        }
    }
    private void fire(Command command){
        if(command.hasSecondWord()) {
            System.out.println("fire what?"); // only a single word
            return;
        }
        if(itemTaken instanceof Beamer){
            if( ((Beamer) itemTaken).fire()){
                System.out.println("Beamer successfully fired. ");
                currentRoom = chargedRoom; // teleport the room
                itemsAndCarry(); // print the description
            }
            else{
                System.out.println("Beamer is not ready to fire. ");
            }
        }
        else{
            System.out.println("You are not holding a beamer");
        }
    }
}
