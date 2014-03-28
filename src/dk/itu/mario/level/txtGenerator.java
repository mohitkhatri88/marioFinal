package dk.itu.mario.level;

import dk.itu.*;
import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.engine.*;
import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.scene.*;

public class txtGenerator {
	public static void main(String arg[]){
        GamePlay gpm = new GamePlay();
		gpm.completionTime = 60;
		gpm.totalTime = 0;////sums all the time, including from previous games if player died
		gpm.jumpsNumber = 50;
		gpm.timeSpentDucking = 0;
		gpm.duckNumber = 0;
		gpm.timeSpentRunning = 0;
		gpm.timesPressedRun = 0;
		gpm.timeRunningRight = 0;
		gpm.timeRunningLeft = 0;
		gpm.coinsCollected = 0;
		//System.out.println("Coins Collected: " + gpm.coinsCollected);
		gpm.totalCoins = 50;
		gpm.emptyBlocksDestroyed = 0;
		gpm.totalEmptyBlocks = 0;
		gpm.coinBlocksDestroyed = 0;
		gpm.totalCoinBlocks = 0;
		gpm.powerBlocksDestroyed = 0;
		gpm.totalpowerBlocks = 0;
		gpm.kickedShells = 0; //kicked
		gpm.enemyKillByFire = 0;//Number of Kills by Shooting Enemy
		gpm.enemyKillByKickingShell  = 0;//Number of Kills by Kicking Shell on Enemy
		gpm.totalEnemies  = 50;

		gpm.totalTimeLittleMode  = 0;
		gpm.totalTimeLargeMode  = 0;//Time Spent Being Large Mario
		gpm.totalTimeFireMode  = 0;//Time Spent Being Fire Mario
		gpm.timesSwichingPower  = 0;//Number of Times Switched Between Little, Large or Fire Mario
		gpm.aimlessJumps  = 0;//aimless jumps
		gpm.percentageBlocksDestroyed  = 1;//percentage of all blocks destroyed
		gpm.percentageCoinBlocksDestroyed  = 0;//percentage of coin blocks destroyed
		gpm.percentageEmptyBlockesDestroyed  = 0;//percentage of empty blocks destroyed
		gpm.percentagePowerBlockDestroyed  = 0;//percentage of power blocks destroyed
		gpm.timesOfDeathByFallingIntoGap  = 0;//number of death by falling into a gap
		gpm.timesOfDeathByRedTurtle  = 0;
		gpm.timesOfDeathByGreenTurtle  = 0;
		gpm.timesOfDeathByGoomba  = 0;
		gpm.timesOfDeathByArmoredTurtle  = 0;
		gpm.timesOfDeathByJumpFlower  = 0;
		gpm.timesOfDeathByCannonBall  = 0;
		gpm.timesOfDeathByChompFlower  = 0;

		gpm.RedTurtlesKilled  = 0;
		gpm.GreenTurtlesKilled  = 0;
		gpm.GoombasKilled  = 0;
		gpm.ArmoredTurtlesKilled  = 0;
		gpm.JumpFlowersKilled  = 0;
		gpm.CannonBallKilled  = 0;
		gpm.ChompFlowersKilled  = 0;
		gpm.WinOrLose = true;
		gpm.write("player4.txt");
	}

}
