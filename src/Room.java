import java.util.ArrayList;
import java.util.HashMap;

/**
 * A room describes the current environment.  Only one
 * room can be displayed at one time.
 * 
 */
public class Room
{
    // Must have these fields for Env to reconize this room
    private double width, height, depth;
    private String textureNorth, textureSouth, textureEast, textureWest;

    // Some additional fields for the game
    private String description;
    private HashMap<String, Room> exits;
        
    
    /**
     * Constructor for objects of class Room
     */
    public Room(double w, double h, double d, String desc)
    {
        width = w;
        height = h;
        depth = d;
        description = desc;
        
        textureNorth = "textures/marble.png"; 
        textureEast = "textures/marble.png";
        textureSouth = "textures/marble.png";
        textureWest =  "textures/marble.png";
        
        exits = new HashMap<String, Room>();
    }
    
    /**
     * Get the description of the room
     * 
     * @return The description string
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Mutator for the wall texture
     */
    public void setTextureNorth(String fileName)
    {
        textureNorth = fileName;
    }

    /**
     * Mutator for the wall texture
     */
    public void setTextureEast(String fileName)
    {
        textureEast = fileName;
    }

    /**
     * Mutator for the wall texture
     */
    public void setTextureSouth(String fileName)
    {
        textureSouth = fileName;
    }

    /**
     * Mutator for the wall texture
     */
    public void setTextureWest(String fileName)
    {
        textureWest = fileName;
    }
        
    /**
     * Create an exit to a room
     * 
     * @param direction the direction of the exit
     * @param room the room that this direction exits to
     */
    public void setExit(String direction, Room room)
    {
        exits.put(direction, room);
    }
    
    /**
     * Get the room a direction exits to.  
     * 
     * @param direction a string indicating a direction
     * @return the room that the direction exits to.  null if no exit
     * in that direction
     */
    public Room getExit(String direction)
    {
        return exits.get(direction);
    }
    
    /**
     * Accessor for the room's dimension
     */
    public double getWidth()
    {
        return width;
    }
    
    /**
     * Accessor for room's dimension
     */
    public double getDepth()
    {
        return depth;
    }
}

