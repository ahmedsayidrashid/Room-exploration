
/**
 * This class is to add a 'beamer' that is a special item in our game.
 *
 * @author Ahmed Rashid
 * @version 2024-03-16
 */
public class Beamer extends Item
{
    
    private boolean isCharged; // to keep track if the beamer is charged
    /**
     * Constructor for fields for each beamer object and its superclass objects
     * 
     * @param description The description of the item
     * @param weight The weight of the item
     * @param name The name of the item
     */
    public Beamer(String description, double weight, String name)
    {
        super(description, weight, name);
        isCharged = false; // initialize each beamer to not being charged
        
    }

    /**
     * Charge the beamer and also check if it was charged.
     *
     * @return boolean if it was charged or not
     */
    public boolean charge() {
        if(!isCharged){ // charge it if already not charged
            isCharged = true;
            return true;
        }
        return false;
    }
      /**
     * fire the beamer and also check if it was charged.
     *
     * @return boolean if it was fired or not
     */
    
    public boolean fire() {
        if(isCharged){
            isCharged = false;
            return true;
        }
        return false;
    }
}
