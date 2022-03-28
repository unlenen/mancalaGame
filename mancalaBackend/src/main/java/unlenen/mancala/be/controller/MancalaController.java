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
package unlenen.mancala.be.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unlenen.mancala.be.exception.GameException;
import unlenen.mancala.be.model.MancalaBoard;
import unlenen.mancala.be.service.MancalaService;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@RestController
@RequestMapping("/mancala/v1")
public class MancalaController {

    @Autowired
    MancalaService mancalaService;

    Logger logger = LoggerFactory.getLogger(MancalaController.class);

    /**
     * Creates a new game and returns session id in MancalaResponse object
     *
     * @return MancalaResponse
     */
    @GetMapping("/start")
    public ResponseEntity<MancalaResponse> createNewGame() {
        String sessionId = mancalaService.createNewGame();
        return new ResponseEntity<>(new MancalaResponse(sessionId), HttpStatus.OK);
    }

    /**
     * Returns the Mancala Board with given session Id in a MancalaResponse
     * object with Http.200
     *
     * Returns a MancalaResponse object with Http.401 with the reason of error.
     *
     * @param sessionId : Game Session Id
     * @return : MancalaResponse
     */
    @GetMapping("/board/{sessionId}")
    public ResponseEntity<MancalaResponse> getBoard(@PathVariable String sessionId) {
        try {
            MancalaBoard mancalaBoard = mancalaService.getBoard(sessionId);
            return new ResponseEntity<>(new MancalaResponse(sessionId, mancalaBoard), HttpStatus.OK);
        } catch (GameException ex) {
            logger.error("[getBoard][FAIL] sessionId:" + sessionId + ", msg:" + ex.getMessage());
            return new ResponseEntity<>(new MancalaResponse(sessionId, ex), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Makes a move on given pitId on board which is related with sessionId
     *
     * @param sessionId : Game Session Id
     * @param pitId : Pit Id to start ( current Player knowledge is inside of
     * mancala board )
     * @return : Returns updated mancala board after move on given pit with HTTP
     * 200. If an error rises a MancalaResponse object with Http.401 with the
     * reason of error.
     *
     */
    @GetMapping("/move/{sessionId}/{pitId}")
    public ResponseEntity<MancalaResponse> newMove(@PathVariable String sessionId, @PathVariable int pitId) {
        try {
            MancalaBoard mancalaBoard = mancalaService.newMove(sessionId, pitId);
            return new ResponseEntity<>(new MancalaResponse(sessionId, mancalaBoard), HttpStatus.OK);
        } catch (GameException ex) {
            logger.error("[newMove][FAIL] sessionId:" + sessionId + ", msg:" + ex.getMessage());
            return new ResponseEntity<>(new MancalaResponse(sessionId, ex), HttpStatus.UNAUTHORIZED);
        }
    }

}
