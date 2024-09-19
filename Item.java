
/**
 * This class represents an item which may be put
 * in a room in the game of Zuul.
 * 
 * @author Ahmed Rashid 101256081
 * @version 2024-03-16
 */
public class Item
{
    // description of the item
    private String description;
    
    // weight of the item in kilo grams 
    private double weight;
    
    // name of each item
    private String name;

    /**
     * Constructor for objects of class Item.
     * 
     * @param description The description of the item
     * @param weight The weight of the item
     * @param name The name of the item
     */
    public Item(String description, double weight ,String name)
    {
        this.description = description;
        this.weight = weight;
        this.name = name;
    }
    /**
     * A getter for the name of the specific item.
     * 
     * @return  the name of the item
     */
    public String getItemName(){
        return name; // to get access of the items 'name'.
    }
    /**
     * Returns a description of the item, including its
     * description, name, and weight.
     * 
     * @return  A description of the item
     */
    public String getDescription()
    {
        return name + ": " + description + " that weighs " + weight + "kg.";
    }
}
