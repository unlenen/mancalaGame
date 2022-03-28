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
package unlenen.mancala.be.model.move;

import unlenen.mancala.be.constant.Player;
import unlenen.mancala.be.model.MancalaBoard;
import unlenen.mancala.be.model.PlayerBoard;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
public class BackwardMove extends AbstractMove {

    public BackwardMove(MancalaBoard mancalaBoard, int pitId) {
        super(mancalaBoard, pitId);
    }

    @Override
    public int getDirection() {
        return -1;
    }

    @Override
    public PlayerBoard getPlayerBoard() {
        return mancalaBoard.getBoards().get(Player.ONE);
    }

    @Override
    public PlayerBoard getOtherPlayerBoard() {
        return mancalaBoard.getBoards().get(Player.TWO);
    }

    @Override
    public boolean isBoardEnd() {
        return getPitId() < 0;
    }

    @Override
    public boolean isNextPitTreasure() {
        return isBoardEnd() && mancalaBoard.getCurrentPlayer() == Player.ONE;
    }

    @Override
    public AbstractMove nextMove() {
        int nextPitId = 0;
        if (isBoardEnd()) {
            if (isNextPitTreasure()) {
                nextPitId = -1;
            }
            return new ForwardMove(mancalaBoard, nextPitId);
        }
        setPitId(getPitId() + getDirection());
        return this;
    }

}
