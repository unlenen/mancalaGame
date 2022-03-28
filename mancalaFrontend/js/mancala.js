URL_MANCALA_API = "http://localhost:8080/mancala/v1"


mancalaGameInfo = {
    sessionId: "",
    mancalaBoard: {}
}

function startGame() {
    $.get(URL_MANCALA_API + "/start", function (result) {
        mancalaGameInfo.sessionId = result.sessionId;
        initBoard();
        getBoard();
    });
}

function getBoard() {
    $.get(URL_MANCALA_API + "/board/" + mancalaGameInfo.sessionId, function (result) {
        mancalaGameInfo.mancalaBoard = result.mancalaBoard;
        drawBoard(mancalaGameInfo.mancalaBoard);
    });
}

function sendPlayerMove(pitId) {
    request = $.get(URL_MANCALA_API + "/move/" + mancalaGameInfo.sessionId + "/" + pitId);
    request.done(function (result) {
        mancalaGameInfo.mancalaBoard = result.mancalaBoard;
        drawBoard(mancalaGameInfo.mancalaBoard);
    });
    request.fail(function (xhr, statusText, errorThrown) {
        alert(JSON.parse(xhr.responseText).message);
    });
}

function initBoard() {
    $(".playerPanel").show();
    $(".board table").show();
    $(".winnerPanel").hide();
}


function completeGame(mancalaBoard) {
    $(".winnerPanel").show();
    $(".winnerName").html("Player " + mancalaBoard.winnerPlayer);
}

function drawBoard(mancalaBoard) {
    $(".playerName").html("Player " + mancalaBoard.currentPlayer);
    for (const player in mancalaBoard.boards) {
        pits = mancalaBoard.boards[player].pits;
        for (const pit in pits) {
            $("#player" + player + "Pit" + pit).html(pits[pit]);
        }
        $("#player" + player + "Treasure").html(mancalaBoard.boards[player].treasure);
    }
    if (mancalaBoard.gameState === 'COMPLETED') {
        completeGame(mancalaBoard);
    }
}

function bindButtons() {
    $(".startButton").click(function () {
        startGame();
    });

    $(".pitButton").click(function () {
        id = $(this).find(".pitText").attr("id");
        pitId = id.substr(-1);
        playerName = id.substr(6, 3);
        if (mancalaGameInfo.mancalaBoard.currentPlayer != playerName) {
            alert("It is not your turn");
            return;
        }
        sendPlayerMove(pitId);
    });
}

function setBoardSize() {
    ratio = $(".board").width() / 992;
    $(".board").height(ratio * 314);
    $(".board table").height(ratio * 314);
}

function createBoardButtons() {
    pitSize = 6;
    players = ['ONE', 'TWO']
    $.each(players, function (index, player) {
        rowData = "<tr>";
        treasure = '<td rowspan=2 class="treasure">' +
                '<a class="button " href="#" >' +
                '<span id="player{PLAYERNAME}Treasure" class="text" >1</span>' +
                '</a>' +
                '</td>';
        for (pit = 0; pit < pitSize; pit++) {
            if (player === 'ONE' && pit === 0) {
                rowData = rowData + treasure.replace("{PLAYERNAME}", "ONE");
            }
            rowData = rowData + '<td class="pit">' +
                    '<a class="button pitButton" href="#" >' +
                    '<span id="player' + player + 'Pit' + pit + '" class="text pitText">1</span>' +
                    '</a>' +
                    '</td>';

            if (pit === 2) {
                rowData = rowData + '<td class="boardCenter ">&nbsp;</td>';
            }
            if (player === 'ONE' && pit === 5) {
                rowData = rowData + treasure.replace("{PLAYERNAME}", "TWO");
            }
        }
        rowData = rowData + "</tr>";
        $('.board table> tbody:last').append($(rowData));
    });
}


function setWindowSizeListener() {
    $(window).resize(function () {
        setBoardSize();
    });
}

function initApplication() {
    setWindowSizeListener();
    setBoardSize();
    createBoardButtons();
    bindButtons();
}

