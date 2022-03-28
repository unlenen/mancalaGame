/*
# Copyright © 2022 Nebi Volkan UNLENEN
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
 */
package unlenen.mancala.be.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import unlenen.mancala.be.constant.Player;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import unlenen.mancala.be.constant.GameState;
import unlenen.mancala.be.exception.GameEndedInADrawException;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
@Setter
public class MancalaBoard {

    String sessionId;
    int pitSize;
    @JsonIgnore
    Player nextPlayer;
    Player currentPlayer;
    Player winnerPlayer;
    GameState gameState;
    Map<Player, PlayerBoard> boards = new HashMap<>();

    public MancalaBoard(String sessionId, int pitSize, int stoneSize) {
        this.sessionId = sessionId;
        initializeBoard(pitSize, stoneSize);
    }

    @JsonIgnore
    public Player findWinner() throws GameEndedInADrawException {
        int playerOneScore = boards.get(Player.ONE).getTotalStone();
        int playerTwoScore = boards.get(Player.TWO).getTotalStone();
        if (playerOneScore > playerTwoScore) {
            return Player.ONE;
        } else if (playerOneScore < playerTwoScore) {
            return Player.TWO;
        } else {
            setGameState(GameState.COMPLETED);
            throw new GameEndedInADrawException(sessionId);
        }
    }

    @JsonIgnore
    public PlayerBoard getCurrentPlayerBoard() {
        return boards.get(currentPlayer);
    }

    @JsonIgnore
    public int getCurrentStoneSize(int pitId) {
        return getCurrentPlayerBoard().getPits()[pitId];
    }

    @JsonIgnore
    public PlayerBoard getNextBoard() {
        return boards.get(currentPlayer.getOtherPlayer());
    }

    @JsonIgnore
    public boolean isGameCompleted() {
        for (PlayerBoard playerBoard : boards.values()) {
            if (playerBoard.isCompleted()) {
                return true;
            }
        }
        return false;
    }

    public void onGameCompleted(Player winner) {
        addAllStonesToTreasure();
        setWinnerPlayer(winner);
        setGameState(GameState.COMPLETED);
    }

    public void onMoveCompleted() {
        currentPlayer = nextPlayer;
    }

    @Override
    public String toString() {
        return "[MancalaBoard] currentPlayer : " + getCurrentPlayer() + " , " + boards + " , state:" + gameState;
    }

    private void addAllStonesToTreasure() {
        for (Player player : Player.values()) {
            boards.get(player).addAllStoneToTreasure();
        }
    }

    private void initializeBoard(int pitSize, int stoneSize) {
        this.pitSize = pitSize;
        boards.put(Player.ONE, new PlayerBoard(Player.ONE, pitSize, stoneSize));
        boards.put(Player.TWO, new PlayerBoard(Player.TWO, pitSize, stoneSize));
        currentPlayer = Player.ONE;
        nextPlayer = Player.TWO;
        gameState = GameState.ACTIVE;
    }

}
