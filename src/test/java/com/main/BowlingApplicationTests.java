package com.main;

import com.model.ScoreboardResponse;
import com.model.Status;
import org.junit.jupiter.api.Test;

class BowlingApplicationTests {

	BowlingController controller = new BowlingController();

	@Test
	void contextLoads() {
	}

	//test game without strikes or spares
	@Test
	void test1(){
		controller.newGame("test1");
		rollTillTenth("test1");
		controller.roll("test1", 2);
		controller.roll("test1", 2);

		ScoreboardResponse response=controller.getScoreboard("test1");

	 	if(response.getScoreboard().getScores().get(9).getFrameScore()!=71){
	 		assert false;
		}else if(response.isGameDone()==false){
	 		assert false;
		}
	 	assert true;

	}
	//test that strike score isnt updated till two rolls
	@Test
	void test2(){
		controller.newGame("test2");
		controller.roll("test2", 10);
		controller.roll("test2", 2);

		ScoreboardResponse response=controller.getScoreboard("test2");
		if(response.getScoreboard().getScores().get(0).getFrameScore()!=-1){
			assert false;
		}
		controller.roll("test2", 3);
		if(controller.getScoreboard("test2").getScoreboard().getScores().get(0).getFrameScore()!=15){
			assert false;
		}
		if(controller.getScoreboard("test2").getScoreboard().getScores().get(1).getFrameScore()!=20){
			assert false;
		}
		assert true;
	}
	//test two strikes in a row
	@Test
	void test3(){
		controller.newGame("test3");
		controller.roll("test3", 10);
		controller.roll("test3", 10);

		ScoreboardResponse response=controller.getScoreboard("test3");
		if(response.getScoreboard().getScores().get(0).getFrameScore()!=-1){
			assert false;
		}
		controller.roll("test3", 3);
		if(controller.getScoreboard("test3").getScoreboard().getScores().get(0).getFrameScore()!=23){
			assert false;
		}
		if(controller.getScoreboard("test3").getScoreboard().getScores().get(1).getFrameScore()!=-1){
			assert false;
		}
		assert true;
	}
	//test spare isnt updated till next roll
	@Test
	void test4(){
		controller.newGame("test4");
		controller.roll("test4", 3);
		controller.roll("test4", 7);

		ScoreboardResponse response=controller.getScoreboard("test4");
		if(response.getScoreboard().getScores().get(0).getFrameScore()!=-1){
			assert false;
		}
		controller.roll("test4", 3);
		if(controller.getScoreboard("test4").getScoreboard().getScores().get(0).getFrameScore()!=13){
			assert false;
		}
		assert true;
	}
	//test 0 score
	@Test
	void test5(){
		controller.newGame("test5");
		controller.roll("test5", 0);

		ScoreboardResponse response=controller.getScoreboard("test5");
		assert(response.getScoreboard().getScores().get(0).getMoves().contains("-"));

	}
	//test rolling too high
	@Test
	void test6(){
		controller.newGame("test6");
		controller.roll("test6", 3);
		assert(controller.roll("test6", 8).getStatus().equals(Status.ROLL_INVALID));

		ScoreboardResponse response=controller.getScoreboard("test6");
		if(response.getScoreboard().getScores().get(0).getFrameScore()!=3){
			assert false;
		}
		controller.roll("test6", 3);
		if(controller.getScoreboard("test6").getScoreboard().getScores().get(0).getFrameScore()!=6){
			assert false;
		}
		assert true;
	}

	//test tenth frame strike
	@Test
	void test7(){
		controller.newGame("test7");
		rollTillTenth("test7");
		controller.roll("test7",10);
		assert(controller.roll("test7", 5).getStatus().equals(Status.SUCCESS));
		ScoreboardResponse response=controller.getScoreboard("test7");
		assert(response.isGameDone()==false);
		controller.roll("test7", 3);
		response=controller.getScoreboard("test7");
		assert(response.isGameDone()==true);
		assert(response.getScoreboard().getScores().get(9).getFrameScore()==85);
	}

	//test tenth frame spare
	@Test
	void test8(){
		controller.newGame("test8");
		rollTillTenth("test8");
		controller.roll("test8",3);
		assert(controller.roll("test8", 7).getStatus().equals(Status.SUCCESS));
		ScoreboardResponse response=controller.getScoreboard("test8");
		assert(response.isGameDone()==false);
		controller.roll("test8", 3);
		response=controller.getScoreboard("test8");
		assert(response.isGameDone()==true);
		assert(response.getScoreboard().getScores().get(9).getFrameScore()==80);
		assert(response.getScoreboard().getScores().get(9).getMoves().equals("3/3"));
	}



	void rollTillTenth(String gameName){
		controller.roll(gameName, 3);
		controller.roll(gameName, 3);
		controller.roll(gameName, 6);
		controller.roll(gameName, 0);
		controller.roll(gameName, 7);
		controller.roll(gameName, 2);
		controller.roll(gameName, 7);
		controller.roll(gameName, 1);
		controller.roll(gameName, 8);
		controller.roll(gameName, 1);
		controller.roll(gameName, 7);
		controller.roll(gameName, 1);
		controller.roll(gameName, 2);
		controller.roll(gameName, 5);
		controller.roll(gameName, 7);
		controller.roll(gameName, 2);
		controller.roll(gameName, 1);
		controller.roll(gameName, 4);
	}
}
