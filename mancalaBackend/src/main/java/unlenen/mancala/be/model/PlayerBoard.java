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
import java.util.Arrays;
import lombok.Getter;
import unlenen.mancala.be.constant.Player;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
public class PlayerBoard {

    @JsonIgnore
    Player player;
    int[] pits;
    int treasure;

    public PlayerBoard(Player player, int pitSize, int stoneSize) {
        this.player = player;
        initialize(pitSize, stoneSize);
    }

    public void clearPit(int pitId) {
        pits[pitId] = 0;
    }

    public void addToTreasure(int stoneSize) {
        treasure += stoneSize;
    }

    public void addStone(int pitId) {
        pits[pitId] = pits[pitId] + 1;
    }

    public int getTotalStone() {
        int pitScore = 0;
        for (int i = 0; i < pits.length; i++) {
            pitScore += pits[i];
        }
        return treasure + pitScore;
    }

    @JsonIgnore
    public boolean isCompleted() {
        for (int i = 0; i < pits.length; i++) {
            if (pits[i] > 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "[" + treasure + "]" + Arrays.toString(pits);
    }

    private void initialize(int pitSize, int stoneSize) {
        pits = new int[pitSize];
        for (int column = 0; column < pitSize; column++) {
            pits[column] = stoneSize;
        }
    }

}
