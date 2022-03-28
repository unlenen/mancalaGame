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
package unlenen.mancala.be.service;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unlenen.mancala.be.config.GameConfig;
import unlenen.mancala.be.constant.GameState;
import unlenen.mancala.be.constant.Player;
import unlenen.mancala.be.exception.GameEndedInADrawException;
import unlenen.mancala.be.exception.GameException;
import unlenen.mancala.be.exception.GameSessionCompletedException;
import unlenen.mancala.be.exception.UnvalidMovementException;
import unlenen.mancala.be.model.MancalaBoard;
import unlenen.mancala.be.model.PlayerBoard;
import unlenen.mancala.be.model.move.AbstractMove;
import unlenen.mancala.be.repository.MancalaRepository;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Service
public class MancalaService {

    @Autowired
    MancalaRepository mancalaRepository;

    @Autowired
    GameConfig gameConfig;

    Logger logger = LoggerFactory.getLogger(MancalaService.class);

    public String createNewGame() {
        String sessionId = createSessionId();
        MancalaBoard mancalaBoard = new MancalaBoard(sessionId, gameConfig.getPitSize(), gameConfig.getStoneSize());
        mancalaRepository.save(sessionId, mancalaBoard);
        if (logger.isInfoEnabled()) {
            logger.debug("[NewGame] sessionId:" + sessionId + " , pitSize:" + gameConfig.getPitSize() + " , stoneSize:" + gameConfig.getStoneSize());
        }
        return sessionId;
    }

    public MancalaBoard getBoard(String sessionId) throws GameException {
        return mancalaRepository.getBySessionId(sessionId);
    }

    public MancalaBoard newMove(String sessionId, int pitId) throws GameException {
        MancalaBoard mancalaBoard = mancalaRepository.getBySessionId(sessionId);
        validatePit(mancalaBoard, pitId);

        int currentStoneSize = mancalaBoard.getCurrentStoneSize(pitId);
        mancalaBoard.getCurrentPlayerBoard().clearPit(pitId);
        mancalaBoard.setNextPlayer(mancalaBoard.getCurrentPlayer().getOtherPlayer());

        AbstractMove move = AbstractMove.createMove(mancalaBoard, pitId);

        if (logger.isDebugEnabled()) {
            logger.debug("[NewMove][Start] pitId:" + move + ", board:" + mancalaBoard);
        }

        for (int stone = currentStoneSize; stone > 0; stone--) {
            move = move.nextMove();
            if (move.isNextPitTreasure()) {
                mancalaBoard.getCurrentPlayerBoard().addToTreasure(1);
                if (logger.isDebugEnabled()) {
                    logger.debug("[NewMove][Action][Treasure] pitId:" + move + ", stone:" + stone + ", board:" + mancalaBoard);
                }
                if (stone == 1) {
                    mancalaBoard.setNextPlayer(mancalaBoard.getCurrentPlayer());
                    break;
                }
                move = move.nextMove();
                continue;
            }
            if (move.getPitId() >= mancalaBoard.getPitSize() || move.getPitId() < 0) {
                move = move.nextMove();
            }
            move.getPlayerBoard().addStone(move.getPitId());
            if (logger.isDebugEnabled()) {
                logger.debug("[NewMove][Action] pitId:" + move + ", stone:" + stone + ", board:" + mancalaBoard);
            }
        }

        onMoveCompleted(mancalaBoard);
        if (logger.isInfoEnabled()) {
            logger.info("[NewMove][Complete] pitId:" + move + ", board:" + mancalaBoard);
        }
        return mancalaBoard;
    }

    private String createSessionId() {
        return UUID.randomUUID().toString();
    }

    private void onMoveCompleted(MancalaBoard mancalaBoard) throws GameEndedInADrawException {
        mancalaBoard.onMoveCompleted();
        if (mancalaBoard.isGameCompleted()) {
            mancalaBoard.onGameCompleted(mancalaBoard.findWinner());
        }
    }

    private void validatePit(MancalaBoard mancalaBoard, int pitId) throws GameException {
        if (mancalaBoard.getGameState() == GameState.COMPLETED) {
            throw new GameSessionCompletedException(mancalaBoard.getSessionId(), mancalaBoard.getWinnerPlayer());
        }
        if (pitId < 0 || pitId >= mancalaBoard.getPitSize()) {
            throw new UnvalidMovementException(mancalaBoard.getCurrentPlayer(), pitId, "Pit ID is not valid");
        }
        int currentStoneSize = mancalaBoard.getCurrentStoneSize(pitId);
        if (currentStoneSize == 0) {
            throw new UnvalidMovementException(mancalaBoard.getCurrentPlayer(), pitId, "Pit is empty");
        }
    }

}
