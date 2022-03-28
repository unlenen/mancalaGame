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

    @GetMapping("/start")
    public ResponseEntity<MancalaResponse> createNewGame() {
        String sessionId = mancalaService.createNewGame();
        return new ResponseEntity<>(new MancalaResponse(sessionId), HttpStatus.OK);
    }

    @GetMapping("/board/{sessionId}")
    public ResponseEntity<MancalaResponse> getBoard(@PathVariable String sessionId) throws GameException {
        try {
            MancalaBoard mancalaBoard = mancalaService.getBoard(sessionId);
            return new ResponseEntity<>(new MancalaResponse(sessionId, mancalaBoard), HttpStatus.OK);
        } catch (GameException ex) {
            logger.error("[getBoard][FAIL] sessionId:" + sessionId + ", msg:" + ex.getMessage());
            return new ResponseEntity<>(new MancalaResponse(sessionId, ex), HttpStatus.UNAUTHORIZED);
        }
    }

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
