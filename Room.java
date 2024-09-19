import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 * 
 * @author Ahmed Rashid 101256081
 * @version 2024-03-16
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits; // stores exits of this room.

    // the items in this room
    private ArrayList<Item> items;
    
    protected static ArrayList<Room> rooms = new ArrayList<Room>();
    
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * 
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        items = new ArrayList<Item>();
        rooms.add(this);
    }
    
    /**
     * Add an item to the room, best to check that it's not null.
     * 
     * @param item The item to add to the room
     */
    public void addItem(Item item) 
    {
        if (item!=null) { // not required, but good practice
            items.add(item);
        }
    }
    /**
     * remove an item from a room
     * 
     * @param the item to take from the room
     */
    public void takeItem(String itemName) 
    {
        if(findItem(itemName) != null){
           items.remove(findItem(itemName));
        }
    }
    /**
     * find the item in the room before deleting it
     * 
     * @param the item to search for in the room
     */
    public Item findItem(String itemName){ // a seperate method due to concurrent modification exception
        
        for(Item item: items){ // search for item in items
            if(item.getItemName().equals(itemName)){
                return item; // proceed to remove from the room if found
            }
        }
        return null;
    }
    /**
     * Return the arraylist of rooms
     * 
     * @return return the array list of rooms 
     */
    public static ArrayList<Room> returnRooms() {
        return rooms;
    }
    /**
     * Define an exit from this room.
     * 
     * @param direction The direction of the exit
     * @param neighbour The room to which the exit leads
     */
    public void setExit(String direction, Room neighbour) 
    {
        exits.put(direction, neighbour);
    }

    /**
     * Returns a short description of the room, i.e. the one that
     * was defined in the constructor
     * 
     * @return The short description of the room
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a long description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     *     Items: 
     *        a chair weighing 5 kgs.
     *        a table weighing 10 kgs.
     *     
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString()
            + "\nItems:" + getItems();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * 
     * @return Details of the room's exits
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * 
     * @param direction The exit's direction
     * @return The room in the given direction
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Return a String representing the items in the room, one per line.
     * 
     * @return A String of the items, one per line
     */
    public String getItems() 
    {
        StringBuilder s = new StringBuilder();
        for (Item i : items) {
            s.append("\n    " + i.getDescription());
        }
        return s.toString(); 
    }
    /**
     * Return a String representing the name of the items in the room.
     * 
     * @return A String of the items, one per line
     */
    public String getItemNames() 
    {
        ArrayList<String> itemNames = new ArrayList<String>();
        for (Item item: items) {
            itemNames.add(item.getItemName()); 
        }
        return itemNames.toString(); 
    }
    /**
     * Return true if a cookie is in the current room.
     * 
     * @return A boolean that tells us if a cookie is in the current room
     */
    public boolean cookieInRoom() 
    {
        return this.getItemNames().contains("cookie");
    }
    /**
     * Return true if a specific item is in the current room.
     * 
     * @return A boolean that tells us if the specific item is in the current room
     */
    public boolean itemInRoom(String item) 
    {
        return this.getItemNames().contains(item);
    }
}

