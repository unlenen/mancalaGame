/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unlenen.mancala.be.controller;

import lombok.Getter;
import lombok.Setter;
import unlenen.mancala.be.model.MancalaBoard;

/**
 *
 * @author Nebi Volkan UNLENEN(unlenen@gmail.com)
 */
@Getter
@Setter
public class MancalaResponse {

    String sessionId;
    MancalaBoard mancalaBoard;
    String message;
    Class errorClass;
    boolean failed;

    public MancalaResponse(String sessionId) {
        this.sessionId = sessionId;
        failed = false;
    }

    public MancalaResponse(String sessionId, MancalaBoard mancalaBoard) {
        this.sessionId = sessionId;
        this.mancalaBoard = mancalaBoard;
        failed = false;
    }

    public MancalaResponse(String sessionId, Exception exception) {
        this.sessionId = sessionId;
        this.message = exception.getMessage();
        this.errorClass = exception.getClass();
        failed = true;
    }

}
