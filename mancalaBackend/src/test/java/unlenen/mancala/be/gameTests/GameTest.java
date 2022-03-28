/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unlenen.mancala.be.gameTests;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import unlenen.mancala.be.Application;
import unlenen.mancala.be.constant.GameState;
import unlenen.mancala.be.constant.Player;
import unlenen.mancala.be.exception.GameEndedInADrawException;
import unlenen.mancala.be.exception.GameException;
import unlenen.mancala.be.exception.GameSessionNotFoundException;
import unlenen.mancala.be.exception.UnvalidMovementException;
import unlenen.mancala.be.model.MancalaBoard;
import unlenen.mancala.be.model.PlayerBoard;
import unlenen.mancala.be.service.MancalaService;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.properties")
public class GameTest {

    @Autowired
    MancalaService mancalaService;

    @Test
    public void test_CreateNewGame() {
        String sessionId = createNewGame();
        Assertions.assertNotNull(sessionId);
    }

    @Test
    public void test_GetBoard() throws GameException {
        String sessionId = createNewGame();
        MancalaBoard mancalaBoard = mancalaService.getBoard(sessionId);
        Assertions.assertNotNull(mancalaBoard);
    }

    @Test
    public void test_SessionNotFound() throws GameException {
        assertThrows(GameSessionNotFoundException.class, () -> {
            MancalaBoard mancalaBoard = mancalaService.getBoard("hello world");
        });
    }

    @Test
    public void test_addToTreasure() throws GameException {
        String sessionId = createNewGame();
        MancalaBoard mancalaBoard = mancalaService.newMove(sessionId, 0);
        PlayerBoard playerBoard = mancalaBoard.getBoards().get(Player.ONE);
        // my own pit is empty , my treasure has a stone , next board has correct stones
        assert playerBoard.getPits()[0] == 0 && playerBoard.getTreasure() == 1 && mancalaBoard.getCurrentStoneSize(4) == 7 && mancalaBoard.getCurrentStoneSize(5) == 6;
    }

    @Test
    public void test_captureStones() throws GameException {
        String sessionId = createNewGame();
        MancalaBoard mancalaBoard = mancalaService.getBoard(sessionId);
        PlayerBoard boardOne = mancalaBoard.getCurrentPlayerBoard();
        PlayerBoard boardTwo = mancalaBoard.getBoards().get(Player.TWO);

        int emptyPit = 4;
        int oneStonePit = 5;

        boardOne.clearPit(oneStonePit);
        boardOne.clearPit(emptyPit);
        boardOne.addStone(oneStonePit);

        mancalaBoard = mancalaService.newMove(sessionId, oneStonePit);
        assert boardOne.getTreasure() == 7
                && boardOne.getStoneSize(emptyPit) == 0
                && boardTwo.getStoneSize(emptyPit) == 0;
    }

    @Test
    public void test_turnChanges() throws GameException {
        String sessionId = createNewGame();
        MancalaBoard mancalaBoard = mancalaService.newMove(sessionId, 0);
        assert mancalaBoard.getCurrentPlayer() == Player.TWO;
    }

    @Test
    public void test_nextTurnSamePlayer() throws GameException {
        String sessionId = createNewGame();
        MancalaBoard mancalaBoard = mancalaService.newMove(sessionId, 5);
        assert mancalaBoard.getCurrentPlayer() == Player.ONE;
    }

    @Test
    public void test_pitEmtpyError() throws GameException {
        String sessionId = createNewGame();
        MancalaBoard mancalaBoard = mancalaService.newMove(sessionId, 5);  // last stone to treasure
        assert mancalaBoard.getCurrentPlayer() == Player.ONE;
        assertThrows(UnvalidMovementException.class, () -> {
            mancalaService.newMove(sessionId, 5);
        });
    }

    @Test
    public void test_WinGame() throws GameException {
        String sessionId = createNewGame();
        MancalaBoard mancalaBoard = mancalaService.getBoard(sessionId);
        PlayerBoard boardOne = mancalaBoard.getCurrentPlayerBoard();

        for (int i = 0; i < boardOne.getPits().length; i++) {
            boardOne.clearPit(i);
        }
        boardOne.addToTreasure(36);
        boardOne.addStone(0);
        mancalaBoard = mancalaService.newMove(sessionId, 0);
        assert mancalaBoard.getGameState() == GameState.COMPLETED
                && mancalaBoard.getWinnerPlayer() == Player.ONE
                && boardOne.getTreasure() == 37;
    }

    @Test
    public void test_DrawEnd() throws GameException {
        String sessionId = createNewGame();
        MancalaBoard mancalaBoard = mancalaService.getBoard(sessionId);

        for (Player player : Player.values()) {
            PlayerBoard board = mancalaBoard.getBoards().get(player);
            for (int i = 0; i < board.getPits().length; i++) {
                board.clearPit(i);
            }
            board.addToTreasure(36);
            board.addStone(0);
        }

        assertThrows(GameEndedInADrawException.class, () -> {
            mancalaService.newMove(sessionId, 0);
        });
    }

    private String createNewGame() {
        return mancalaService.createNewGame();
    }

}
