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

import lombok.Getter;
import lombok.Setter;
import unlenen.mancala.be.constant.Player;
import unlenen.mancala.be.model.MancalaBoard;
import unlenen.mancala.be.model.PlayerBoard;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
@Setter
public abstract class AbstractMove {

    MancalaBoard mancalaBoard;
    int pitId;
    int pitSize;

    public AbstractMove(MancalaBoard mancalaBoard, int pitId) {
        this.mancalaBoard = mancalaBoard;
        this.pitId = pitId;
        this.pitSize = mancalaBoard.getPitSize();
    }

    public abstract int getDirection();

    public abstract PlayerBoard getPlayerBoard();

    public Player getPlayer() {
        return getPlayerBoard().getPlayer();
    }

    public abstract boolean isBoardEnd();

    public abstract boolean isNextPitTreasure();

    public abstract AbstractMove nextMove();

    public static AbstractMove createMove(MancalaBoard mancalaBoard, int pitId) {
        switch (mancalaBoard.getCurrentPlayer()) {
            case ONE: {
                return new BackwardMove(mancalaBoard, pitId);
            }
            case TWO: {
                return new ForwardMove(mancalaBoard, pitId);
            }
        }
        throw new RuntimeException("Not possible");
    }

    @Override
    public String toString() {
        return "[" + getPlayer() + "/" + getPitId() + "]";
    }

}
