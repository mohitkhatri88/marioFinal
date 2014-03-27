package dk.itu.mario.level;

import java.util.Random;

import dk.itu.mario.MarioInterface.Constraints;
import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.engine.sprites.Enemy;


public class MyLevel extends Level{
	//Store information about the level
	 public   int ENEMIES = 0; //the number of enemies the level contains
	 public   int BLOCKS_EMPTY = 0; // the number of empty blocks
	 public   int BLOCKS_COINS = 0; // the number of coin blocks
	 public   int BLOCKS_POWER = 0; // the number of power blocks
	 public   int COINS = 0; //These are the coins in boxes that Mario collect
	 
	 public   boolean COINKEEPERMODE = false; 
	 public   boolean REGULARPLAYER = false;
	 public   boolean REGULAR_HARD = false;
	 public   boolean REGULAR_EASY = false;
	 public   boolean DESTROYER = false;
	 public   GamePlay gp;
	 
	 public enum Profile{COINKEEPER, KILLER, SPEEDY, JUMPER}
	 
	 public class PlayerProfile {
		 
		 /*Following four profiles will be generated. 
		  * COINKEEPER :  Generate a lot of coins in the map. 
		  * KILLER : Generate a lot of enemies in the map. 
		  * SPEEDY : Doesn't like hills. Remove them. 
		  * JUMPER : Likes to jump. Put in more jumps. 
		  * For speedy and jumper, the values of the coinkeeper and killer remain the same. 
		  * For Coinkeeper and killer, i just change the probability of the decor, and all other things are same. 
		  */
		  		 
		 public Profile profile = Profile.SPEEDY;
		 
		 //Variables which decide the probabilities of various components being present. 
		 public double hillProbability = 2.0f;
		 public double jumpProbability = 4.0f;
		 public double tubesProbability = 6.0f;
		 public double cannonsProbability = 8.0f;
		 public double straightProbability = 1.0f;
		 public double coinProbability = 0.5f;
		 public double enemiesProbability = 0.5f;
		 
		 /*
		  * Used for keeping the preferences that help decide the type of plan that will be used. 
		  */
		 public PlayerProfile(GamePlay pm) {
			 //Get the percentage of the enemies killed. 
			 int totalEnemiesKilled = pm.ArmoredTurtlesKilled + pm.CannonBallKilled + pm.ChompFlowersKilled + pm.RedTurtlesKilled + pm.GreenTurtlesKilled + pm.JumpFlowersKilled;
			 double percentageKilled  = totalEnemiesKilled/pm.totalEnemies;
			 
			 //Get the exploration rate/Coin percentage of the player 
			 double percentageCoins = pm.coinsCollected/pm.totalCoins;
			 double percentageCoinBlocks = pm.percentageBlocksDestroyed;
			 
			 /*Rules for deciding between the personalities*/
			 if (percentageKilled > 0.5) {
				 System.out.println("Killer has been selected.");
				 profile = Profile.KILLER;
				 this.enemiesProbability = 0.8;
			 } else if (percentageCoins > 0.5) {
				 System.out.println("Coinkeeper has been selected.");
				 profile = Profile.COINKEEPER;
				 this.coinProbability = 0.8;
			 } else if (pm.percentageBlocksDestroyed < 30 ) {
				 System.out.println("Speedy got selected, less value will be given to hills.");
				 profile = Profile.SPEEDY;
				 //Reduce the hill Probability
				 this.hillProbability = 1.0;
				 this.jumpProbability = 3.25;
				 this.tubesProbability = 5.5;
				 this.cannonsProbability = 7.75;
				 this.straightProbability =  10.0;
			 } else if (pm.aimlessJumps/pm.jumpsNumber > 0.5) {
				 System.out.println("Jumper has been selected.");
				 profile = Profile.JUMPER;
				 this.hillProbability = 3;
				 this.jumpProbability = 4.75;
				 this.tubesProbability = 6.5;
				 this.cannonsProbability = 8.25;
				 this.straightProbability =  10.0;
			 }
		 }
	 }
	 
	 public PlayerProfile playerProfile;
 
	private static Random levelSeedRandom = new Random();
	    public static long lastSeed;

	    Random random;

  
	    private int difficulty;
	    private int type;
		private int gaps;
		
		public MyLevel(int width, int height)
	    {
			super(width, height);
	    }


		public MyLevel(int width, int height, long seed, int difficulty, int type, GamePlay playerMetrics)
	    {
	        this(width, height);
	        verify(playerMetrics);
	        if (playerMetrics == null) {
	        	System.out.println("OH NO ITS NULL AGAIN!!!RUN FOR YOUR LIFE!!!");
	        }
	        this.playerProfile = new PlayerProfile(playerMetrics);
	        creat(seed, difficulty, type);
	        
	    }

	    public void verify(GamePlay gp){
	    	this.gp = gp;
	    	
	    }
		public void creat(long seed, int difficulty, int type)
	    {
	    	
	        this.type = type;
	        this.difficulty = difficulty;

	        lastSeed = seed;
	        random = new Random(seed);

	        //create the start location
	        int length = 0;
	        length += buildStraight(0, width, true);
	        
	        //create all of the medium sections based on the profile that we have earlier generated. 
	        while (length < width - 64)
	        {
	            length += buildZone(length, width - length);
	        }
	        

	        //set the end piece
	        int floor = height - 1 - random.nextInt(4);

	        xExit = length + 8;
	        yExit = floor;

	        // fills the end piece
	        for (int x = length; x < width; x++)
	        {
	            for (int y = 0; y < height; y++)
	            {
	                if (y >= floor)
	                {
	                    setBlock(x, y, GROUND);
	                }
	            }
	        }
	        
	        
	        
	        if (type == LevelInterface.TYPE_CASTLE || type == LevelInterface.TYPE_UNDERGROUND)
	        {
	            int ceiling = 0;
	            int run = 0;
	            for (int x = 0; x < width; x++)
	            {
	                if (run-- <= 0 && x > 4)
	                {
	                    ceiling = random.nextInt(4);
	                    run = random.nextInt(4) + 4;
	                }
	                for (int y = 0; y < height; y++)
	                {
	                    if ((x > 4 && y <= ceiling) || x < 1)
	                    {
	                        setBlock(x, y, GROUND);
	                    }
	                }
	            }
	        }
	        
	        

	        fixWalls();
	        

	    }
		
	    public int buildZone(int x, int maxLength) {
	        double t = random.nextDouble()*10;
	
			if(t<=this.playerProfile.hillProbability){
				return buildHillStraight(x, maxLength);
			}
			else if(t>this.playerProfile.hillProbability && t<=this.playerProfile.jumpProbability){
				return buildJump(x, maxLength);
			}
			else if(t>this.playerProfile.jumpProbability && t<=this.playerProfile.tubesProbability){
				return buildTubes(x, maxLength);
			}
			else if(t>this.playerProfile.tubesProbability && t<=this.playerProfile.cannonsProbability){
				return buildCannons(x, maxLength);
			}
			else{
				return buildStraight(x, maxLength, false);
			}
	    }


	    private int buildJump(int xo, int maxLength)
	    {	gaps++;
	    	//jl: jump length
	    	//js: the number of blocks that are available at either side for free
	        int js = random.nextInt(4) + 2;
	        int jl = random.nextInt(2) + 2;
	        int length = js * 2 + jl;
	        if (length > maxLength) length = maxLength;

	        boolean hasStairs = random.nextInt(3) == 0;
	        //boolean hasStairs = true;                        /////////////TRY//////////////
	        int floor = height - 1 - random.nextInt(4);
	        //int floor = 13;
	       
	      //run from the start x position, for the whole length
	        for (int x = xo; x < xo + length; x++)
	        {
	        	boolean temp = false;
	            if (x < xo + js || x > xo + length - js - 1)
	            {
	            	//run for all y's since we need to paint blocks upward
	                for (int y = 0; y < height; y++)
	                {	//paint ground up until the floor
	                    if (y >= floor)
	                    {
	                        setBlock(x, y, GROUND);
	                        float shouldDecorate = random.nextFloat();
	                        if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
	                        	decorate2(x, x+1, floor);
	                        } else if (((this.playerProfile.enemiesProbability >= this.playerProfile.coinProbability) && shouldDecorate <= this.playerProfile.enemiesProbability) ) {
	                        	System.out.println("DECORATE JUMP");
	                        	addEnemyLine2(x, x+1, floor);
	                        }
	                        //decorate2(x, x+1, ifloor);
	                    }else if (hasStairs)//if it is above ground, start making stairs of rocks
	                    {	//LEFT SIDE
	                        if (x < xo + js)
	                        { //we need to max it out and level because it wont
	                          //paint ground correctly unless two bricks are side by side
	                            if (y >= floor - (x - xo) + 1)
	                            {
	                                setBlock(x, y, ROCK);
	                                float shouldDecorate = random.nextFloat();
	                                if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
	    	                        	decorate2(x, x+1, y);
	    	                        } else if (((this.playerProfile.enemiesProbability >= this.playerProfile.coinProbability) && shouldDecorate <= this.playerProfile.enemiesProbability) ) {
	    	                        	System.out.println("DECORATE JUMP");
	    	                        	addEnemyLine2(x, x+1, y);
	    	                        }
	                            }
	                        }
	                        else
	                        { //RIGHT SIDE
	                            if (y >= floor - ((xo + length) - x) + 2)
	                            {
	                                setBlock(x, y, ROCK);
	                                float shouldDecorate = random.nextFloat();
	                                if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
	    	                        	decorate2(x, x+1, y);
	    	                        } else if (((this.playerProfile.enemiesProbability >= this.playerProfile.coinProbability) && shouldDecorate <= this.playerProfile.enemiesProbability) ) {
	    	                        	System.out.println("DECORATE JUMP");
	    	                        	addEnemyLine2(x, x+1, y);
	    	                        }
	                            }
	                        }
	                    }
	                }
	            }else{
	            	float shouldDecorate = random.nextFloat();
	            	if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
                    	decorate2(x, x+1, floor);
                    } else if (((this.playerProfile.enemiesProbability >= this.playerProfile.coinProbability) && shouldDecorate <= this.playerProfile.enemiesProbability) ) {
                    	System.out.println("DECORATE JUMP");
                    	addEnemyLine2(x, x+1, floor);
                    }
	            }
	        }

	        return length;
	    }
	    
//	    private int coinBuildJump(int xo, int maxLength)
//	    {	gaps++;
//	    	//jl: jump length
//	    	//js: the number of blocks that are available at either side for free
//	        int js = random.nextInt(4) + 2;
//	        int jl = random.nextInt(2) + 2;
//	        int length = js * 2 + jl;
//	        if (length > maxLength) length = maxLength;
//
//	        boolean hasStairs = random.nextInt(3) == 0;
//	        //boolean hasStairs = true;                        /////////////TRY//////////////
//	        int floor = height - 1 - random.nextInt(4);
//	        //int floor = 13;
//	        int h = floor;
//	       
//	      //run from the start x position, for the whole length
//	        for (int x = xo; x < xo + length; x++)
//	        {
//	        	boolean temp = false;
//	            if (x < xo + js || x > xo + length - js - 1)
//	            {
//	            	//run for all y's since we need to paint blocks upward
//	                for (int y = 0; y < height; y++)
//	                {	//paint ground up until the floor
//	                    if (y >= floor)
//	                    {
//	                        setBlock(x, y, GROUND);
//	                        decorate2(x, x+1, floor);
//	                    }else if (hasStairs)//if it is above ground, start making stairs of rocks
//	                    {	//LEFT SIDE
//	                        if (x < xo + js)
//	                        { //we need to max it out and level because it wont
//	                          //paint ground correctly unless two bricks are side by side
//	                            if (y >= floor - (x - xo) + 1)
//	                            {
//	                                setBlock(x, y, ROCK);
//	                                if(temp == false){
//	                                	temp = decorate2(x, x+1, y);
//	                                	h = y;
//	                                }
//	                            }
//	                        }
//	                        else
//	                        { //RIGHT SIDE
//	                            if (y >= floor - ((xo + length) - x) + 2)
//	                            {
//	                                setBlock(x, y, ROCK);
//	                                if(temp == false){
//	                                	temp = decorate2(x, x+1, y);
//	                                }
//	                            }
//	                        }
//	                    }
//	                }
//	            }else{
//	            	decorate2(x, x+1, h);
//	            }
//	        }
//
//	        return length;
//	    }

	    private int buildCannons(int xo, int maxLength)
	    {
	        int length = random.nextInt(10) + 2;
	        if (length > maxLength) length = maxLength;

	        int floor = height - 1 - random.nextInt(4);
	        int xCannon = xo + 1 + random.nextInt(4);
	        for (int x = xo; x < xo + length; x++)
	        {
	            if (x > xCannon)
	            {
	                xCannon += 2 + random.nextInt(4);
	            }
	            if (xCannon == xo + length - 1) xCannon += 10;
	            int cannonHeight = floor - random.nextInt(4) - 1;

	            for (int y = 0; y < height; y++)
	            {
	                if (y >= floor)
	                {
	                    setBlock(x, y, GROUND);
	                    float shouldDecorate = random.nextFloat();
	                    if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
	                    	decorate2(x, x+1, y);
	                    }
	                }
	                else
	                {
	                    if (x == xCannon && y >= cannonHeight)
	                    {
	                        if (y == cannonHeight)
	                        {
	                            setBlock(x, y, (byte) (14 + 0 * 16));
	                            float shouldDecorate = random.nextFloat();
	                            if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
	    	                    	decorate2(x, x+1, y);
	    	                    }
	                        }
	                        else if (y == cannonHeight + 1)
	                        {
	                            setBlock(x, y, (byte) (14 + 1 * 16));
	                            float shouldDecorate = random.nextFloat();
	                            if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
	    	                    	decorate2(x, x+1, y);
	    	                    }
	                        }
	                        else
	                        {
	                            setBlock(x, y, (byte) (14 + 2 * 16));
	                            float shouldDecorate = random.nextFloat();
	                            if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
	    	                    	decorate2(x, x+1, y);
	    	                    }
	                        }
	                    }
	                }
	            }
	        }

	        return length;
	    }
	    
//	    private int coinBuildCannons(int xo, int maxLength)
//	    {
//	        int length = random.nextInt(10) + 2;
//	        if (length > maxLength) length = maxLength;
//
//	        int floor = height - 1 - random.nextInt(4);
//	        int xCannon = xo + 1 + random.nextInt(4);
//	        for (int x = xo; x < xo + length; x++)
//	        {
//	        	boolean temp = false;
//	            if (x > xCannon)
//	            {
//	                xCannon += 2 + random.nextInt(4);
//	            }
//	            if (xCannon == xo + length - 1) xCannon += 10;
//	            int cannonHeight = floor - random.nextInt(4) - 1;
//
//	            for (int y = 0; y < height; y++)
//	            {
//	                if (y >= floor)
//	                {
//	                    setBlock(x, y, GROUND);
//	                    if(temp == false)
//	                    	temp = decorate2(x, x+1, y);
//	                }
//	                else
//	                {
//	                    if (x == xCannon && y >= cannonHeight)
//	                    {
//	                        if (y == cannonHeight)
//	                        {
//	                            setBlock(x, y, (byte) (14 + 0 * 16));
//	                            if(temp == false)
//	    	                    	temp = decorate2(x, x+1, y);
//	                        }
//	                        else if (y == cannonHeight + 1)
//	                        {
//	                            setBlock(x, y, (byte) (14 + 1 * 16));
//	                            if(temp == false)
//	    	                    	temp = decorate2(x, x+1, y);
//	                        }
//	                        else
//	                        {
//	                            setBlock(x, y, (byte) (14 + 2 * 16));
//	                            if(temp == false)
//	    	                    	temp = decorate2(x, x+1, y);
//	                        }
//	                    }
//	                }
//	            }
//	        }
//
//	        return length;
//	    }

	    private int buildHillStraight(int xo, int maxLength)
	    {
	        int length = random.nextInt(10) + 10;
	        if (length > maxLength) length = maxLength;

	        int floor = height - 1 - random.nextInt(4);
	        //int floor = height - 1 - 0;
	        for (int x = xo; x < xo + length; x++)
	        {
	        	boolean temp = false;
	            for (int y = 0; y < height; y++)
	            {
	                if (y >= floor)
	                {
	                	if (temp == false) {
	                		float shouldDecorate = random.nextFloat();
	    	            	if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
	                        	decorate2(x, x+1, y);
	                        } else if (((this.playerProfile.enemiesProbability >= this.playerProfile.coinProbability) && shouldDecorate <= this.playerProfile.enemiesProbability) ) {
	                        	System.out.println("DECORATE JUMP");
	                        	addEnemyLine2(x, x+1, y);
	                        }
	                	}
	                    setBlock(x, y, GROUND);
	                }
	            }
	        }

	        //addEnemyLine(xo + 1, xo + length - 1, floor - 1);

	        int h = floor;

	        boolean keepGoing = true;

	        boolean[] occupied = new boolean[length];
	        while (keepGoing)
	        {
	            h = h - 2 - random.nextInt(3);

	            if (h <= 0)
	            {
	                keepGoing = false;
	            }
	            else
	            {
	                int l = random.nextInt(5) + 3;
	                int xxo = random.nextInt(length - l - 2) + xo + 1;

	                if (occupied[xxo - xo] || occupied[xxo - xo + l] || occupied[xxo - xo - 1] || occupied[xxo - xo + l + 1])
	                {
	                    keepGoing = false;
	                }
	                else
	                {
	                    occupied[xxo - xo] = true;
	                    occupied[xxo - xo + l] = true;
	                    //addEnemyLine(xxo, xxo + l, h - 1);
	                    float shouldDecorate = random.nextFloat();
	                    if (((this.playerProfile.enemiesProbability >= this.playerProfile.coinProbability) && shouldDecorate <= this.playerProfile.enemiesProbability) ) {
                        	System.out.println("DECORATE JUMP");
                        	addEnemyLine2(xxo, xxo + l, h - 1);
                        }
	                    if (random.nextInt(4) == 0)
	                    {
	                        coinDecorate(xxo - 1, xxo + l + 1, h);
	                        keepGoing = false;
	                    }
	                    for (int x = xxo; x < xxo + l; x++)
	                    {
	                    	boolean temp = false;
	                        for (int y = h; y < floor; y++)
	                        {
	                            int xx = 5;
	                            if (x == xxo) xx = 4;
	                            if (x == xxo + l - 1) xx = 6;
	                            int yy = 9;
	                            if (y == h) yy = 8;

	                            if (getBlock(x, y) == 0 || getBlock(x, y) == COIN || getBlock(x, y) == BLOCK_POWERUP || getBlock(x, y) == BLOCK_COIN || getBlock(x, y) == BLOCK_EMPTY)
	                            {
	                                setBlock(x, y, (byte) (xx + yy * 16));
	                                if(getBlock(x, y) == COIN) COINS--;
	                                if(getBlock(x, y) == BLOCK_POWERUP) BLOCKS_POWER--;
	                                if(getBlock(x, y) == BLOCK_COIN) BLOCKS_COINS--;
	                                if(getBlock(x, y) == BLOCK_EMPTY) BLOCKS_EMPTY--;
	                                if (temp == false) {
	                                	shouldDecorate = random.nextFloat();
	                	            	if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
	                                    	decorate2(x, x+1, floor);
	                                    } else if (((this.playerProfile.enemiesProbability >= this.playerProfile.coinProbability) && shouldDecorate <= this.playerProfile.enemiesProbability) ) {
	                                    	System.out.println("DECORATE JUMP");
	                                    	addEnemyLine2(x, x+1, floor);
	                                    }
	                                }
	                            }
	                            else
	                            {
	                                if (getBlock(x, y) == HILL_TOP_LEFT) setBlock(x, y, HILL_TOP_LEFT_IN);
	                                if (getBlock(x, y) == HILL_TOP_RIGHT) setBlock(x, y, HILL_TOP_RIGHT_IN);
	                                if (temp == false) {
	                                	shouldDecorate = random.nextFloat();
	                	            	if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
	                                    	decorate2(x, x+1, floor);
	                                    } else if (((this.playerProfile.enemiesProbability >= this.playerProfile.coinProbability) && shouldDecorate <= this.playerProfile.enemiesProbability) ) {
	                                    	System.out.println("DECORATE JUMP");
	                                    	addEnemyLine2(x, x+1, floor);
	                                    }
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	        }

	        return length;
	    }
	    
//	    private int coinBuildHillStraight(int xo, int maxLength)
//	    {
//	        int length = random.nextInt(10) + 10;
//	        if (length > maxLength) length = maxLength;
//
//	        int floor = height - 1 - random.nextInt(4);
//	        //int floor = height - 1 - 0;
//	        for (int x = xo; x < xo + length; x++)
//	        {
//	        	boolean temp = false;
//	            for (int y = 0; y < height; y++)
//	            {
//	                if (y >= floor)
//	                {
//	                    setBlock(x, y, GROUND);
//	                    if(temp == false)
//	                    	temp = decorate2(x, x+1, y);
//	                }
//	            }
//	        }
//
//	        addEnemyLine(xo + 1, xo + length - 1, floor - 1);
//
//	        int h = floor;
//
//	        boolean keepGoing = true;
//
//	        boolean[] occupied = new boolean[length];
//	        while (keepGoing)
//	        {
//	            h = h - 2 - random.nextInt(3);
//
//	            if (h <= 0)
//	            {
//	                keepGoing = false;
//	            }
//	            else
//	            {
//	                int l = random.nextInt(5) + 3;
//	                int xxo = random.nextInt(length - l - 2) + xo + 1;
//
//	                if (occupied[xxo - xo] || occupied[xxo - xo + l] || occupied[xxo - xo - 1] || occupied[xxo - xo + l + 1])
//	                {
//	                    keepGoing = false;
//	                }
//	                else
//	                {
//	                    occupied[xxo - xo] = true;
//	                    occupied[xxo - xo + l] = true;
//	                    addEnemyLine(xxo, xxo + l, h - 1);
//	                    //decorate2(xxo - 1, xxo + l + 1, h);
//	                    //keepGoing = false;
//	                    if (random.nextInt(4) == 0)
//	                    {
//	                        coinDecorate(xxo - 1, xxo + l + 1, h);
//	                        keepGoing = false;
//	                    }
//	                    
//	                    for (int x = xxo; x < xxo + l; x++)
//	                    {
//	                    	boolean temp = false;
//	                        for (int y = h; y < floor; y++)
//	                        {
//	                            int xx = 5;
//	                            if (x == xxo) xx = 4;
//	                            if (x == xxo + l - 1) xx = 6;
//	                            int yy = 9;
//	                            if (y == h) yy = 8;
//
//	                            if (getBlock(x, y) == 0 || getBlock(x, y) == COIN || getBlock(x, y) == BLOCK_POWERUP || getBlock(x, y) == BLOCK_COIN || getBlock(x, y) == BLOCK_EMPTY)
//	                            {
//	                                setBlock(x, y, (byte) (xx + yy * 16));
//	                                if(getBlock(x, y) == COIN) COINS--;
//	                                if(getBlock(x, y) == BLOCK_POWERUP) BLOCKS_POWER--;
//	                                if(getBlock(x, y) == BLOCK_COIN) BLOCKS_COINS--;
//	                                if(getBlock(x, y) == BLOCK_EMPTY) BLOCKS_EMPTY--;
//	                                if(temp == false)
//		    	                    	temp = decorate2(x, x+1, y);
//	                            }
//	                            else
//	                            {
//	                                if (getBlock(x, y) == HILL_TOP_LEFT) setBlock(x, y, HILL_TOP_LEFT_IN);
//	                                if (getBlock(x, y) == HILL_TOP_RIGHT) setBlock(x, y, HILL_TOP_RIGHT_IN);
//	                                if(temp == false)
//		    	                    	temp = decorate2(x, x+1, y);
//	                            }
//	                        }
//	                    }
//	                    //decorate(xxo - 1, xxo + l + 1, h);
//	                }
//	                //decorate(xxo - 1, xxo + l + 1, h);
//	            }
//	        }
//	        //decorate(xo + 1 - 1, length - 1 - 2 + xo + 1 + 3 + 1, h);
//
//	        return length;
//	    }

	    private void addEnemyLine(int x0, int x1, int y)
	    {
	        for (int x = x0; x < x1; x++)
	        {
	            if (random.nextInt(35) < difficulty + 1)
	            {
	                int type = random.nextInt(4);
	                
	                if (difficulty < 1)
	                {
	                    type = Enemy.ENEMY_GOOMBA;
	                }
	                else if (difficulty < 3)
	                {
	                    type = random.nextInt(3);
	                }

	                setSpriteTemplate(x, y, new SpriteTemplate(type, random.nextInt(35) < difficulty));
	                ENEMIES++;
	            }
	        }
	    }
	    
	    private void addEnemyLine2(int x0, int x1, int y)
	    {
	        for (int x = x0; x < x1; x++)
	        {
	        	//float shouldCreateEnemy = random.nextFloat();
	            //if (shouldCreateEnemy <= 0.7)
	            //{
	                int type = random.nextInt(4);
	                
	                setSpriteTemplate(x, y, new SpriteTemplate(type, random.nextFloat() < 0.3));
	                ENEMIES++;
	            //}
	        }
	    }

	    private int buildTubes(int xo, int maxLength)
	    {
	        int length = random.nextInt(10) + 5;
	        if (length > maxLength) length = maxLength;

	        int floor = height - 1 - random.nextInt(4);
	        int xTube = xo + 1 + random.nextInt(4);
	        int tubeHeight = floor - random.nextInt(2) - 2;
	        for (int x = xo; x < xo + length; x++)
	        {
	        	boolean temp = false;
	            if (x > xTube + 1)
	            {
	                xTube += 3 + random.nextInt(4);
	                tubeHeight = floor - random.nextInt(2) - 2;
	            }
	            if (xTube >= xo + length - 2) xTube += 10;

	            if (x == xTube && random.nextInt(11) < difficulty + 1)
	            {
	                setSpriteTemplate(x, tubeHeight, new SpriteTemplate(Enemy.ENEMY_FLOWER, false));
	                ENEMIES++;
	            }

	            for (int y = 0; y < height; y++)
	            {
	                if (y >= floor)
	                {
	                    setBlock(x, y,GROUND);
	                    if (temp == false) {
	                    	float shouldDecorate = random.nextFloat();
        	            	if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
                            	decorate2(x, x+1, floor);
                            } else if (((this.playerProfile.enemiesProbability >= this.playerProfile.coinProbability) && shouldDecorate <= this.playerProfile.enemiesProbability) ) {
                            	System.out.println("DECORATE JUMP");
                            	addEnemyLine2(x, x+1, floor);
                            }
	                    }

	                }
	                else
	                {
	                    if ((x == xTube || x == xTube + 1) && y >= tubeHeight)
	                    {
	                        int xPic = 10 + x - xTube;

	                        if (y == tubeHeight)
	                        {
	                        	//tube top
	                            setBlock(x, y, (byte) (xPic + 0 * 16));
	                            if (temp == false) {
	    	                    	float shouldDecorate = random.nextFloat();
	            	            	if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
	                                	decorate2(x, x+1, floor);
	                                } else if (((this.playerProfile.enemiesProbability >= this.playerProfile.coinProbability) && shouldDecorate <= this.playerProfile.enemiesProbability) ) {
	                                	System.out.println("DECORATE JUMP");
	                                	addEnemyLine2(x, x+1, floor);
	                                }
	    	                    }
	                        }
	                        else
	                        {
	                        	//tube side
	                            setBlock(x, y, (byte) (xPic + 1 * 16));
	                            if (temp == false) {
	    	                    	float shouldDecorate = random.nextFloat();
	            	            	if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
	                                	decorate2(x, x+1, floor);
	                                } else if (((this.playerProfile.enemiesProbability >= this.playerProfile.coinProbability) && shouldDecorate <= this.playerProfile.enemiesProbability) ) {
	                                	System.out.println("DECORATE JUMP");
	                                	addEnemyLine2(x, x+1, floor);
	                                }
	    	                    }
	                        }
	                    }
	                }
	            }
	        }

	        return length;
	    }
	    
//	    private int coinBuildTubes(int xo, int maxLength)
//	    {
//	        int length = random.nextInt(10) + 5;
//	        if (length > maxLength) length = maxLength;
//
//	        int floor = height - 1 - random.nextInt(4);
//	        int xTube = xo + 1 + random.nextInt(4);
//	        int tubeHeight = floor - random.nextInt(2) - 2;
//	        for (int x = xo; x < xo + length; x++)
//	        {
//	        	boolean temp = false;
//	            if (x > xTube + 1)
//	            {
//	                xTube += 3 + random.nextInt(4);
//	                tubeHeight = floor - random.nextInt(2) - 2;
//	            }
//	            if (xTube >= xo + length - 2) xTube += 10;
//
//	            if (x == xTube && random.nextInt(11) < difficulty + 1)
//	            {
//	                setSpriteTemplate(x, tubeHeight, new SpriteTemplate(Enemy.ENEMY_FLOWER, false));
//	                ENEMIES++;
//	            }
//
//	            for (int y = 0; y < height; y++)
//	            {
//	                if (y >= floor)
//	                {
//	                    setBlock(x, y,GROUND);
//	                    if(temp == false)
//	                    	temp = decorate2(x, x+1, y);
//
//	                }
//	                else
//	                {
//	                    if ((x == xTube || x == xTube + 1) && y >= tubeHeight)
//	                    {
//	                        int xPic = 10 + x - xTube;
//
//	                        if (y == tubeHeight)
//	                        {
//	                        	//tube top
//	                            setBlock(x, y, (byte) (xPic + 0 * 16));
//	                            if(temp == false)
//	    	                    	temp = decorate2(x, x+1, y);
//	                        }
//	                        else
//	                        {
//	                        	//tube side
//	                            setBlock(x, y, (byte) (xPic + 1 * 16));
//	                            if(temp == false)
//	    	                    	temp = decorate2(x, x+1, y);
//	                        }
//	                    }
//	                }
//	            }
//	        }
//
//	        return length;
//	    }

	    private int buildStraight(int xo, int maxLength, boolean safe)
	    {
	        int length = random.nextInt(10) + 2;

	        if (safe)
	        	length = 10 + random.nextInt(5);

	        if (length > maxLength)
	        	length = maxLength;

	        int floor = height - 1 - random.nextInt(4);

	        //runs from the specified x position to the length of the segment
	        for (int x = xo; x < xo + length; x++)
	        {
	            for (int y = 0; y < height; y++)
	            {
	                if (y >= floor)
	                {
	                    setBlock(x, y, GROUND);
	                }
	            }
	        }

	        if (!safe)
	        {
	        	for(int x = xo; x < xo + length; x++) {
	        		float shouldDecorate = random.nextFloat();
	            	if (((this.playerProfile.coinProbability >= this.playerProfile.enemiesProbability) && shouldDecorate <= this.playerProfile.coinProbability) ) {
                    	decorate2(x, x+1, floor);
                    } else if (((this.playerProfile.enemiesProbability >= this.playerProfile.coinProbability) && shouldDecorate <= this.playerProfile.enemiesProbability) ) {
                    	System.out.println("DECORATE JUMP");
                    	addEnemyLine2(x, x+1, floor);
                    }
	        	}
	            if (length > 5)
	            {
	                coinDecorate(xo, xo + length, floor);
	            }
	        }

	        return length;
	    }
	    
//	    private int coinBuildStraight(int xo, int maxLength, boolean safe)
//	    {
//	        int length = random.nextInt(10) + 2;
//
//	        if (safe)
//	        	length = 10 + random.nextInt(5);
//
//	        if (length > maxLength)
//	        	length = maxLength;
//	        
//
//	        int floor = height - 1 - random.nextInt(4);
//
//	        //runs from the specified x position to the length of the segment
//	        for (int x = xo; x < xo + length; x++)
//	        {
//	            for (int y = 0; y < height; y++)
//	            {
//	                if (y >= floor)
//	                {
//	                    setBlock(x, y, GROUND);
//	                }
//	            }
//	        }
//
//	        if (!safe)
//	        {
//	        	for(int x = xo; x < xo + length; x++)
//	        		decorate2(x, x + 1, floor);
//	        	
//	        	if(length > 5){
//	        		coinDecorate(xo, xo + length, floor);
//	        	}
//	        	
//	        }
//	        return length;
//	    }

	    private void decorate(int xStart, int xLength, int floor)
	    {
	    	//if its at the very top, just return
	        if (floor < 1)
	        	return;

	        //        boolean coins = random.nextInt(3) == 0;
	        boolean rocks = true;
	        

	        //add an enemy line above the box
	        addEnemyLine(xStart + 1, xLength - 1, floor - 1);

	       // Make whole decorate line have coins 
	        int s = 0;
	        int e = 0;
	      //int s = random.nextInt(4);
	      //int e = random.nextInt(4);

	        if (floor - 2 > 0){
	            if ((xLength - 1 - e) - (xStart + 1 + s) > 0){
	                for(int x = xStart + 1 + s; x < xLength - 1 - e; x++){
	                    setBlock(x, floor - 2, COIN);
	                    COINS++;
	                }
	            }
	        }

	        s = random.nextInt(4);
	        e = random.nextInt(4);
	        
	        //this fills the set of blocks and the hidden objects inside them
	        if (floor - 4 > 0)
	        {
	            if ((xLength - 1 - e) - (xStart + 1 + s) > 2)
	            {
	                for (int x = xStart + 1 + s; x < xLength - 1 - e; x++)
	                {
	                    if (rocks)//getSpriteTemplate(x, floor - 4) == null)
	                    {
	                        if (x != xStart + 1 && x != xLength - 2 && random.nextInt(3) == 0)
	                        {
	                            if (random.nextInt(4) == 0)
	                            {
	                                setBlock(x, floor - 4, BLOCK_POWERUP);
	                                BLOCKS_POWER++;
	                                System.out.println("BLOCK POWER : " + BLOCKS_POWER);
	                            }
	                            else
	                            {	//the fills a block with a hidden coin
	                                setBlock(x, floor - 4, BLOCK_COIN);
	                                BLOCKS_COINS++;
	                                System.out.println("BLOCK COINS : " + BLOCKS_COINS);
	                            }
	                        }
	                        else if (random.nextInt(4) == 0)
	                        {
	                            if (random.nextInt(4) == 0)
	                            {
	                                setBlock(x, floor - 4, (byte) (2 + 1 * 16));
	                            }
	                            else
	                            {
	                                setBlock(x, floor - 4, (byte) (1 + 1 * 16));
	                            }
	                        }
	                        else
	                        {
	                            setBlock(x, floor - 4, BLOCK_EMPTY);
	                            BLOCKS_EMPTY++;
	                            System.out.println("BLOCK EMPTY : " + BLOCKS_EMPTY);
	                        }
	                    }
	                }
	            }
	        }
	    }
	    
	    private boolean decorate2(int xStart, int xLength, int floor)
	    {
	    	boolean temp = false;
	    	//if its at the very top, just return
	        if (floor < 0)
	        	return temp;

	        //        boolean coins = random.nextInt(3) == 0;
	        boolean rocks = true;

	        //add an enemy line above the box
	        //addEnemyLine(xStart + 1, xLength - 1, floor - 1);

	       // Make whole decorate line have coins 
	        int s = 0;
	        int e = 0;
	      //int s = random.nextInt(4);
	      //int e = random.nextInt(4);
	        
	        if (floor - 2 > 0){
                for(int x = xStart + s; x < xLength - e; x++){
                    if(getBlock(x, floor - 2) == 0){
                    	setBlock(x, floor - 2, COIN);
                    	temp =  true;
                    }
                    COINS++;
                }
	        }else{
	        	temp = false;
	        }
	        return temp;
	    }
	    
	    private void coinDecorate(int xStart, int xLength, int floor)
	    {
	    	//if its at the very top, just return
	        if (floor < 1)
	        	return;

	        //        boolean coins = random.nextInt(3) == 0;
	        boolean rocks = true;
	        

	        //add an enemy line above the box
	        addEnemyLine(xStart + 1, xLength - 1, floor - 1);

	       // Make whole decorate line have coins 
	        int s = 0;
	        int e = 0;

	        s = random.nextInt(4);
	        e = random.nextInt(4);
	        
	        //this fills the set of blocks and the hidden objects inside them
	        if (floor - 4 > 0)
	        {
	            if ((xLength - 1 - e) - (xStart + 1 + s) > 2)
	            {
	                for (int x = xStart + 1 + s; x < xLength - 1 - e; x++)
	                {
	                    if (rocks)//getSpriteTemplate(x, floor - 4) == null)
	                    {
	                        if (x != xStart + 1 && x != xLength - 2 && random.nextInt(3) == 0)
	                        {
	                            if (random.nextInt(4) == 0)
	                            {
	                                setBlock(x, floor - 4, BLOCK_POWERUP);
	                                BLOCKS_POWER++;
	                                System.out.println("BLOCK POWER : " + BLOCKS_POWER);
	                            }
	                            else
	                            {	//the fills a block with a hidden coin
	                                setBlock(x, floor - 4, BLOCK_COIN);
	                                BLOCKS_COINS++;
	                                System.out.println("BLOCK COINS : " + BLOCKS_COINS);
	                            }
	                        }
	                        else if (random.nextInt(4) == 0)
	                        {
	                            if (random.nextInt(4) == 0)
	                            {
	                                setBlock(x, floor - 4, (byte) (2 + 1 * 16));
	                            }
	                            else
	                            {
	                                setBlock(x, floor - 4, (byte) (1 + 1 * 16));
	                            }
	                        }
	                        else
	                        {
	                            setBlock(x, floor - 4, BLOCK_EMPTY);
	                            BLOCKS_EMPTY++;
	                            System.out.println("BLOCK EMPTY : " + BLOCKS_EMPTY);
	                        }
	                    }
	                }
	            }
	        }
	    }

	    private void fixWalls()
	    {
	        boolean[][] blockMap = new boolean[width + 1][height + 1];

	        for (int x = 0; x < width + 1; x++)
	        {
	            for (int y = 0; y < height + 1; y++)
	            {
	                int blocks = 0;
	                for (int xx = x - 1; xx < x + 1; xx++)
	                {
	                    for (int yy = y - 1; yy < y + 1; yy++)
	                    {
	                        if (getBlockCapped(xx, yy) == GROUND){
	                        	blocks++;
	                        }
	                    }
	                }
	                blockMap[x][y] = blocks == 4;
	            }
	        }
	        blockify(this, blockMap, width + 1, height + 1);
	    }

	    private void blockify(Level level, boolean[][] blocks, int width, int height){
	        int to = 0;
	        if (type == LevelInterface.TYPE_CASTLE)
	        {
	            to = 4 * 2;
	        }
	        else if (type == LevelInterface.TYPE_UNDERGROUND)
	        {
	            to = 4 * 3;
	        }

	        boolean[][] b = new boolean[2][2];

	        for (int x = 0; x < width; x++)
	        {
	            for (int y = 0; y < height; y++)
	            {
	                for (int xx = x; xx <= x + 1; xx++)
	                {
	                    for (int yy = y; yy <= y + 1; yy++)
	                    {
	                        int _xx = xx;
	                        int _yy = yy;
	                        if (_xx < 0) _xx = 0;
	                        if (_yy < 0) _yy = 0;
	                        if (_xx > width - 1) _xx = width - 1;
	                        if (_yy > height - 1) _yy = height - 1;
	                        b[xx - x][yy - y] = blocks[_xx][_yy];
	                    }
	                }

	                if (b[0][0] == b[1][0] && b[0][1] == b[1][1])
	                {
	                    if (b[0][0] == b[0][1])
	                    {
	                        if (b[0][0])
	                        {
	                            level.setBlock(x, y, (byte) (1 + 9 * 16 + to));
	                        }
	                        else
	                        {
	                            // KEEP OLD BLOCK!
	                        }
	                    }
	                    else
	                    {
	                        if (b[0][0])
	                        {
	                        	//down grass top?
	                            level.setBlock(x, y, (byte) (1 + 10 * 16 + to));
	                        }
	                        else
	                        {
	                        	//up grass top
	                            level.setBlock(x, y, (byte) (1 + 8 * 16 + to));
	                        }
	                    }
	                }
	                else if (b[0][0] == b[0][1] && b[1][0] == b[1][1])
	                {
	                    if (b[0][0])
	                    {
	                    	//right grass top
	                        level.setBlock(x, y, (byte) (2 + 9 * 16 + to));
	                    }
	                    else
	                    {
	                    	//left grass top
	                        level.setBlock(x, y, (byte) (0 + 9 * 16 + to));
	                    }
	                }
	                else if (b[0][0] == b[1][1] && b[0][1] == b[1][0])
	                {
	                    level.setBlock(x, y, (byte) (1 + 9 * 16 + to));
	                }
	                else if (b[0][0] == b[1][0])
	                {
	                    if (b[0][0])
	                    {
	                        if (b[0][1])
	                        {
	                            level.setBlock(x, y, (byte) (3 + 10 * 16 + to));
	                        }
	                        else
	                        {
	                            level.setBlock(x, y, (byte) (3 + 11 * 16 + to));
	                        }
	                    }
	                    else
	                    {
	                        if (b[0][1])
	                        {
	                        	//right up grass top
	                            level.setBlock(x, y, (byte) (2 + 8 * 16 + to));
	                        }
	                        else
	                        {
	                        	//left up grass top
	                            level.setBlock(x, y, (byte) (0 + 8 * 16 + to));
	                        }
	                    }
	                }
	                else if (b[0][1] == b[1][1])
	                {
	                    if (b[0][1])
	                    {
	                        if (b[0][0])
	                        {
	                        	//left pocket grass
	                            level.setBlock(x, y, (byte) (3 + 9 * 16 + to));
	                        }
	                        else
	                        {
	                        	//right pocket grass
	                            level.setBlock(x, y, (byte) (3 + 8 * 16 + to));
	                        }
	                    }
	                    else
	                    {
	                        if (b[0][0])
	                        {
	                            level.setBlock(x, y, (byte) (2 + 10 * 16 + to));
	                        }
	                        else
	                        {
	                            level.setBlock(x, y, (byte) (0 + 10 * 16 + to));
	                        }
	                    }
	                }
	                else
	                {
	                    level.setBlock(x, y, (byte) (0 + 1 * 16 + to));
	                }
	            }
	        }
	    }
	    
	    public RandomLevel clone() throws CloneNotSupportedException {

	    	RandomLevel clone=new RandomLevel(width, height);

	    	clone.xExit = xExit;
	    	clone.yExit = yExit;
	    	byte[][] map = getMap();
	    	SpriteTemplate[][] st = getSpriteTemplate();
	    	
	    	for (int i = 0; i < map.length; i++)
	    		for (int j = 0; j < map[i].length; j++) {
	    			clone.setBlock(i, j, map[i][j]);
	    			clone.setSpriteTemplate(i, j, st[i][j]);
	    	}
	    	clone.BLOCKS_COINS = BLOCKS_COINS;
	    	clone.BLOCKS_EMPTY = BLOCKS_EMPTY;
	    	clone.BLOCKS_POWER = BLOCKS_POWER;
	    	clone.ENEMIES = ENEMIES;
	    	clone.COINS = COINS;
	    	
	        return clone;

	      }
}
