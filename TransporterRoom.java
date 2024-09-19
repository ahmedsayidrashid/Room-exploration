import java.util.Random;
/**
 * Write a description of class TransporterRoom here.
 *
 * @author Ahmed Rashid
 * @version 2024-03-16
 */
public class TransporterRoom extends Room
{
    private Random randomRoom;
    /**
     * Constructor for objects of class TransporterRoom
     * @param description of the room
     */
    public TransporterRoom(String description)
    {
        super(description);
        randomRoom = new Random();
    }

    /**
     * This method overrides the getExit method in its superclass,
     *  it takes the user to a random room.
     *
     * @return a random room 
     */
    public Room getExit(String direction){
        return rooms.get(randomRoom.nextInt(rooms.size()));
    }
}
