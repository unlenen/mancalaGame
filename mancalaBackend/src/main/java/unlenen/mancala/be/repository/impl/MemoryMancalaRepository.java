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
package unlenen.mancala.be.repository.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import unlenen.mancala.be.exception.GameSessionNotFoundException;
import unlenen.mancala.be.model.MancalaBoard;
import unlenen.mancala.be.repository.MancalaRepository;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
public class MemoryMancalaRepository implements MancalaRepository {

    Map<String, MancalaBoard> gameSessions = new LinkedHashMap<>();

    @Override
    public void save(String sessionId, MancalaBoard mancalaBoard) {
        gameSessions.put(sessionId, mancalaBoard);
    }

    @Override
    public MancalaBoard getBySessionId(String sessionId) throws GameSessionNotFoundException {
        MancalaBoard mancalaBoard = gameSessions.get(sessionId);
        if (mancalaBoard == null) {
            throw new GameSessionNotFoundException(sessionId);
        }
        return mancalaBoard;
    }

    @Override
    public void deleteById(String sessionId) throws GameSessionNotFoundException {
        MancalaBoard mancalaBoard = gameSessions.remove(sessionId);
        if (mancalaBoard == null) {
            throw new GameSessionNotFoundException(sessionId);
        }
    }

}
