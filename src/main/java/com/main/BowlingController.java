package com.main;

import com.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BowlingController {

    private BowlingRepository bowlingRepository;
    public BowlingController(){
        bowlingRepository = new BowlingRepository();
    }
    /*Rolls a score for game gameId, returns whether roll is valid or not*/
    @PostMapping("/bowling/roll/{gameName}/{roll}")
    Response roll(@PathVariable String gameName, @PathVariable int roll){

        Response response = new Response();
        Game game = bowlingRepository.findByGameName(gameName);
        if(game==null){
            response.setStatus(Status.GAMEID_INVALID);
            response.setMessage("Invalid game name");
            return response;
        }
        Game nGame = new Game(game);
        game=makeRoll(nGame, roll);
        if(game==null){
            response.setStatus(Status.ROLL_INVALID);
            return response;
        }else{
            bowlingRepository.save(game);
        }
        response.setGameName(gameName);
        response.setStatus(Status.SUCCESS);
        return response;
    }

    /*Returns the current scoreboard for game gameId
    * note: if frameScore is -1, then frame's score isn't finalized yet, for example
    in the case of a strike, but the next frame hasn't finished.*/
    @GetMapping("/bowling/scoreboard/{gameName}")
    ScoreboardResponse getScoreboard(@PathVariable String gameName){

        Game game = bowlingRepository.findByGameName(gameName);

        ScoreboardResponse response = new ScoreboardResponse();
        if(game==null){
            response.setStatus(Status.GAMEID_INVALID);
            response.setMessage("Invalid game name");
            return response;
        }
        Scoreboard scoreBoard = buildScoreBoardString(game);
        int rollsRemaining = rollsRemaining(game);
        int currentFrame = getCurrentFrameNumber(game);

        response.setScoreboard(scoreBoard);
        response.setCurrentFrame(currentFrame);
        response.setRollsRemaining(rollsRemaining);
        response.setStatus(Status.SUCCESS);
        if(game.getGameStatus().equals(GameStatus.DONE)){
            response.setGameDone(true);
        }else{
            response.setGameDone(false);
        }
        return response;
    }

    /*creates new game, returns gameId*/
    @PostMapping("/bowling/newGame/{gameName}")
    Response newGame(@PathVariable String gameName){
        Response response = new Response();
        Game game=bowlingRepository.findByGameName(gameName);
        if(game!=null){
            response.setStatus(Status.ERROR);
            response.setMessage("gameName already in use");
            return response;
        }else {
            Frame frame = new Frame();
            game = new Game();
            game.getFrames().add(frame);
            game.setGameName(gameName);
            game.setGameStatus(GameStatus.ACTIVE);
            response.setStatus(Status.SUCCESS);
            response.setGameName(gameName);
            response.setMessage("New Game Created");
            bowlingRepository.save(game);
            return response;
        }
    }

    /*precondition: game is a valid game with valid frames i.e.
    * gameStatus is updated properly, and has the right number of frames*/
    private int rollsRemaining(Game game){
        if(game.getGameStatus().equals(GameStatus.DONE)){
            return 0;
        }
        int currFrameNum = getCurrentFrameNumber(game);
        if(currFrameNum==0)return Constants.ROLLS_PER_FRAME;
        Frame currFrame = game.getFrames().get(currFrameNum-1);

        if(currFrameNum==Constants.NUM_OF_FRAMES_IN_GAME){
            if(currFrame.getRolls().size()==0){
                return Constants.ROLLS_PER_FRAME;
            } else if(currFrame.getRolls().size()==Constants.ROLLS_PER_FRAME){
                return 1;
            } else {
                if(currFrame.getRolls().get(0).getScore()==Constants.NUM_OF_PINS){
                    return Constants.ROLLS_PER_FRAME;
                } else{
                    return Constants.ROLLS_PER_FRAME-1;
                }
            }
        } else {
            return Constants.ROLLS_PER_FRAME-currFrame.getRolls().size();
        }
    }

    /*returns the current frame number in the game*/
    private int getCurrentFrameNumber(Game game){
        if(game.getGameStatus().equals(GameStatus.DONE)) return -1;
        return game.getFrames().size();
    }

    /*makes a roll. return null if roll is invalid*/
    private Game makeRoll(Game game, int roll){
        if(roll>10||roll<0) return null;
        if(game.getGameStatus().equals(GameStatus.DONE)) return null;

            int currFrameNum = getCurrentFrameNumber(game);

            //add role to frame
            Roll newRoll = new Roll();
            newRoll.setScore(roll);
            game.getFrames().get(currFrameNum-1).getRolls().add(newRoll);
            int pinsKnockedOver=0;
            for(Roll a:game.getFrames().get(currFrameNum-1).getRolls()){
                pinsKnockedOver+=a.getScore();
            }
            /*handle two scenarios, last frame or regular frame*/
            if(currFrameNum!=Constants.NUM_OF_FRAMES_IN_GAME){
                //roll was too high.
                if(pinsKnockedOver>Constants.NUM_OF_PINS){
                    return null;
                }
                //add next frame if two rolls, or first roll is a strike.
                if(game.getFrames().get(currFrameNum-1).getRolls().size()==Constants.ROLLS_PER_FRAME ||
                        game.getFrames().get(currFrameNum-1).getRolls().get(0).getScore()==Constants.NUM_OF_PINS){
                    Frame frame = new Frame();
                    game.getFrames().add(frame);
                }
            } else{
                boolean firstRollStrike=false;
                if(game.getFrames().get(currFrameNum-1).getRolls().get(0).getScore()==Constants.NUM_OF_PINS){
                    firstRollStrike=true;
                }
                if(game.getFrames().get(currFrameNum-1).getRolls().size()==Constants.ROLLS_PER_FRAME+1){
                    if(firstRollStrike){
                        //roll was too high, for example in this frame: Strike, 3, 9
                        if(pinsKnockedOver>2*Constants.NUM_OF_PINS){
                            return null;
                        }
                    }
                    game.setGameStatus(GameStatus.DONE);
                } else if(game.getFrames().get(currFrameNum-1).getRolls().size()==Constants.ROLLS_PER_FRAME){
                    //roll was too high
                    if(!firstRollStrike&&pinsKnockedOver>Constants.NUM_OF_PINS) {
                        return null;
                    }
                    if(pinsKnockedOver<Constants.NUM_OF_PINS || (firstRollStrike&&pinsKnockedOver==2*Constants.NUM_OF_PINS)){
                        game.setGameStatus(GameStatus.DONE);
                    }
                }
            }
        return game;
    }

    /*returns a representation of the scoreboard, with
        X to represent a strike
        / to represent a spare
        - to represent no pins knocked down
        and two rolls per frame
        
        precondition: game is a valid Game
     */
    private Scoreboard buildScoreBoardString(Game game){
        Scoreboard result=new Scoreboard();
        int count=0;
        int cumulativeScore=0;
        for(count=0;count<game.getFrames().size();count++){
            Frame cFrame = game.getFrames().get(count);
            //empty frame
            if(cFrame.getRolls().size()==0){
                break;
            }
            int frameScore=0;
            String moves="";
            if(count==Constants.NUM_OF_FRAMES_IN_GAME-1){
                for(int i=0;i<cFrame.getRolls().size();i++) {
                    Roll cRoll = cFrame.getRolls().get(i);
                    if(frameScore>=0)
                    frameScore += cRoll.getScore();
                    if(cRoll.getScore()==Constants.NUM_OF_PINS){
                        moves+="X";
                        if(cFrame.getRolls().size()<Constants.ROLLS_PER_FRAME+1){
                            frameScore=-1;
                        }
                    } else if((frameScore==Constants.NUM_OF_PINS&&i==1)){
                        moves+="/";
                        if(cFrame.getRolls().size()<Constants.ROLLS_PER_FRAME+1){
                            frameScore=-1;
                        }
                    } else if((frameScore==2*Constants.NUM_OF_PINS&&i==Constants.ROLLS_PER_FRAME)){
                        moves+="/";
                    } else{
                        moves+=cRoll.getScore();
                    }
                }
                if(frameScore<0 || cumulativeScore<0){
                    result.addFrame(count + 1, moves);
                }else {
                    cumulativeScore += frameScore;
                    result.addFrame(count + 1, cumulativeScore, moves);
                }
                return result;
            }
            for(int i=0;i<cFrame.getRolls().size();i++){
                Roll cRoll = cFrame.getRolls().get(i);
                frameScore+=cRoll.getScore();
                if(frameScore==Constants.NUM_OF_PINS){
                    if(i==0){
                        moves+="X";
                        if(count<game.getFrames().size()-1){
                            Frame nextFrame = game.getFrames().get(count+1);
                            if(nextFrame.getRolls().size()>1){
                                frameScore+=nextFrame.getRolls().get(0).getScore();
                                frameScore+=nextFrame.getRolls().get(1).getScore();
                                break;
                            } else if(nextFrame.getRolls().size()==1){
                                frameScore+=nextFrame.getRolls().get(0).getScore();
                                if(count<game.getFrames().size()-2){
                                    Frame twoFrames = game.getFrames().get(count+2);
                                    if(twoFrames.getRolls().size()>0){
                                        frameScore+=twoFrames.getRolls().get(0).getScore();
                                        break;
                                    }
                                }
                            }
                            frameScore=-1;
                        }

                    }else{
                        moves+="/";
                        if(count<game.getFrames().size()-1){
                            Frame nextFrame = game.getFrames().get(count+1);
                            if(nextFrame.getRolls().size()>0){
                                frameScore+=nextFrame.getRolls().get(0).getScore();
                            } else{
                                frameScore=-1;
                            }
                        }
                    }
                    break;
                } else {
                    if(cRoll.getScore()==0){
                        moves+="-";
                    } else {
                        moves += cRoll.getScore();
                    }
                }
            }
            if(frameScore<0 || cumulativeScore<0){
                cumulativeScore=-1;
                result.addFrame(count + 1, moves);
            }else {
                cumulativeScore += frameScore;
                result.addFrame(count + 1, cumulativeScore, moves);
            }
        }
        for(count=count+1;count<=Constants.NUM_OF_FRAMES_IN_GAME;count++){
            result.addFrame(count, "");
        }
        return result;
    }

}
