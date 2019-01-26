package sample;


/**
 * stores the powerups while they are active to keep track of their start times and powerup types
 * @author Kunal Upadya
 */
public class ActivePowerup{
    int powerupType;
    long startTime = System.currentTimeMillis();
    public ActivePowerup(int powerupType){
        this.powerupType = powerupType;
    }

    /**
     * used to get the powerup type of the active powerup, utilized while activating the powerup
     * @return
     */
    public int getPowerupType() {
        return powerupType;
    }
}
