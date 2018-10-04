// $('#document').ready(function() {
//
//     NinaRow = new NinaRow('#NinaRow')
//
//     NinaRow.onPlayerMove = function() {
//         $('#player').text(NinaRow.player);
//     }
//
//     $('#restart').click(function() {
//         NinaRow.restart();
//     })
// });
var REFRESH_RATE = 500;
var REFRESH_USER_RATE = 200;
var RESTARTGAME_URL = buildUrlWithContextPath("RestartGame");
var CHECKPLAYER_URL = buildUrlWithContextPath("CheckPlayer");
var COMPUTERTURN_URL = buildUrlWithContextPath("ComputerTurn");
var GAME_USERS_LIST_URL = buildUrlWithContextPath("GamePlayersList");
var GAMEDETAILS_URL = buildUrlWithContextPath("GameDetails");
var GAMESTATUS_URL = buildUrlWithContextPath("GameStatus");
var PLAYTURN_URL = buildUrlWithContextPath("PlayTurn");
var FINISHGAME_URL = buildUrlWithContextPath("FinishGame");
var CURRENT_PLAYER_INFO_URL = buildUrlWithContextPath("PlayerInfo");
var ACTIVE_PLAYER_INFO_URL = buildUrlWithContextPath("ActivePlayerTurn");
var STATUS_URL = buildUrlWithContextPath("Status");
var LEAVE_GAME_URL = buildUrlWithContextPath("LeaveGame");
var gameStarted = 0;
var gameFinished = 0;
var intervalTimer = 200;
var isFirstStatus = true;



window.onload = function()
{
    checkLoginStatus();
    refreshUserList();
    createUserInfoGrid();
    setInterval(refreshUserList, REFRESH_USER_RATE);
    setInterval(checkGameStatus, REFRESH_RATE);
};

window.onunload = function () {
    onLeaveGameClick();
};

function checkGameStatus() {

        $.ajax({

            url: GAMESTATUS_URL,
            type: 'GET',
            success: checkGameStatusCallback

        });
}

function checkGameStatusCallback(json) {


    if(json.status === "WaitingForPlayers")
    {
        $(".gameStatus").text("Waiting For Players");
    }
    else
    {
        $(".gameStatus").text(json.status);
    }

    if (json.status === "Started") {
        if (gameStarted === 0)
        {
            alert("The Game Has Started!");
            gameStarted = 1;
            setInterval(updatePlayerTurn, REFRESH_USER_RATE);

            CheckIfItsAComputerAndExecuteTurn()

        }
    }
    else if (json.status === "Finished") {

        if (gameFinished === 0) {
            printBoard();
            alert(json.message);
            gameFinished = 1;

            finishingTheGame();
        }
    }
}

function finishingTheGame() {

    $.ajax({

        url: FINISHGAME_URL,
        type: 'GET',
        success: finisingTheGameCallback
        });

}

function finisingTheGameCallback(json) {

    if (json.numberOfRegisteredPlayers === 0)
    {
        restartTheGame(json.title)
    }
    window.location = "/NinaRow/Pages/GamesHubPage/GamesHubPage.html"

}

function restartTheGame(title) {

    $.ajax({

        url: RESTARTGAME_URL,
        data: {
            title: title
        },
        type: 'GET'
    });
}

function CheckIfItsAComputerAndExecuteTurn() {

    $.ajax({

        url: GAMEDETAILS_URL,
        data: {
            title: ""
        },
        type: 'GET',
        success: CheckIfComputerCallback
    })
}

function CheckIfComputerCallback(json) {

    var index = json.theGame.activePlayerIndex;

    if (json.players[index].isComputer) {

        doComputerTurn();
    }
}

function checkLoginStatus() {

    if(isFirstStatus)
    {
        isFirstStatus = false;
        IntializePage();
    }
}

function printBoard() {

    $.ajax({
       url: GAMEDETAILS_URL,
       data: {

           title: ""
       },
       type: 'GET',
       success: printBoardcallback
    });
}

function onLeaveGameClick()
{
    if ($(".gameStatus").text() !== "Waiting For Players") {
        $.ajax({
            url: LEAVE_GAME_URL,
            type:'POST',
            success: leaveGameCallback
        });
    }
    else {
        $.ajax({
            url: FINISHGAME_URL,
            type:'POST',
            success: leaveGameCallback
        });
    }

}

function leaveGameCallback(json)
{
    window.location = "/NinaRow/Pages/GamesHubPage/GamesHubPage.html";
}

function onButtonClick(col, moveType) {

    if ($(".gameStatus").text() !== "Waiting For Players") {
        checkPlayer(col, moveType);
    }
    else {
        alert($(".gameStatus").text());
    }

}

function printBoardcallback(json) {

    for(let i=0; i<json.rows;i++)
    {
        for (let j=0; j<json.columns; j++)
        {
            if (json.Discs[i][j]!==null)
            {

                var color = json.Discs[i][j];
                $(`.col[data-row='${i+1}'][data-col='${j}']`).css("backgroundColor",color);
            }
            else if (json.Discs[i][j]===null) {

                $(`.col[data-row='${i+1}'][data-col='${j}']`).css("backgroundColor","white");
            }
        }
    }
}

function IntializePage() {

    $.ajax ({

        url: GAMEDETAILS_URL,
        data: {
           title: ""
        },
        type: 'GET',
        success: intializePagecallback
    });
}

function intializePagecallback(json){


    createGrid(json);
    setInterval(printBoard,intervalTimer);
}

function checkPlayer(col, moveType) {

    $.ajax({

        url: CHECKPLAYER_URL,
        type: 'GET',
        success: function(json) {

            checkPlayerCallback(json, col, moveType);
        }

    });
 ;
}

function checkPlayerCallback(json, col, moveType) {


    if (json === null)
    {

        alert("it's not your turn!");
    }
    else
    {
        $.ajax
        ({
            url: PLAYTURN_URL,
            data: {
                moveType: moveType,
                column: col
            },
            type: 'GET',
            success: onClickCallback
        });
    }


}

function onClickCallback(json) {

    printBoardcallback(json);
    createUserInfoGrid();



    var index = json.theGame.activePlayerIndex;

    if(json.players[index].isComputer)
    {
        doComputerTurn();
    }



}

function doComputerTurn() {

    $.ajax({

        url: COMPUTERTURN_URL,
        type: 'GET',
        success: doComputerTurnCallback
    });


}

function doComputerTurnCallback(json) {

    if (json !== null) {
        printBoardcallback(json);
        createUserInfoGrid();

        var index = json.theGame.activePlayerIndex;

        if(json.players[index].isComputer)
        {
            doComputerTurn();
        }
    }
}

function updatePlayerTurn() {
    $.ajax({
        url: ACTIVE_PLAYER_INFO_URL,
        type: 'GET',
        success: updatePlayerTurnCallback
    });

}

function updatePlayerTurnCallback(json){
    console.log(json);
    const playerName = $("#activePlayerName");
    playerName.empty();
    playerName.text(json);
}

function createGrid(json) {

    var ROWS = json.rows;
    var COLS = json.columns;
    var rowsForPop;
    if (json.variant === "Popout") {

       rowsForPop = ROWS+1;
       ROWS = ROWS+1;
    }
    ROWS = ROWS+1;

    const $board = $("#NinaRow");
    $board.empty();
    for (let row = 0; row < ROWS; row++) {
        const $row = $('<div>')
            .addClass('row');
        for (let col = 0; col < COLS; col++) {
            if (row === 0) {
                const $button = $('<button>')
                    .addClass('Button')
                    .attr('data-col', col)
                    .text("Drop!")
                    .click(function(){
                        var moveType = $(this).text();
                        var col = $(this).attr('data-col');
                        onButtonClick(col, moveType);
                    });
                $row.append($button);
            }
            else if (row === rowsForPop) {
                const $button = $('<button>')
                    .addClass('Button')
                    .attr('data-col', col)
                    .text("Pop!")
                    .click(function() {
                        var moveType = $(this).text();
                        var col = $(this).attr('data-col');
                        onButtonClick(col, moveType);
                    });
                $row.append($button);
            }
            else {
                const $col = $('<div>')
                    .addClass('col empty')
                    .attr('data-col', col)
                    .attr('data-row', row);
                $row.append($col);
            }
        }
        $board.append($row);
    }
}

function getUserName() {
    var result;
    $.ajax
    ({
        async: false,
        url: STATUS_URL,
        type: 'GET',
        success: function (json) {
            result = json.userName;
        }
    });
    return result;
}

function refreshUserList() {
    $.ajax(
        {
            url: GAME_USERS_LIST_URL,
            data:{
                username: getUserName()
            },
            type: 'GET',
            success: refreshUserListCallback
        }
    );
}

function refreshUserListCallback(users) {
    var usersTable = $("#GameUsersList");
    usersTable.empty();

    $.each(users || [], function(index, user) {
        $('<tr><td>' + user.name + '<br/>' + user.colorName + '<br/>' + "Turns Played: " + user.howManyTurnsPlayed.value + '<br/>' + "ID: " + user.id + '<br/>' + user.playerType + '<br/>').appendTo(usersTable);
        $('</tr></td>').appendTo(usersTable);
    });
}

function createUserInfoGrid() {
    $.ajax(
        {
            url: CURRENT_PLAYER_INFO_URL,
            data:{
                username: getUserName()
            },
            type: 'GET',
            success: createUserInfoGridCallback
        }
    );
}

function createUserInfoGridCallback(player) {
    var usersTable = $("#playerInfo");
    usersTable.empty();

    $('<tr><td>' + player.name + '<br/>' + player.colorName + '<br/>' + "Turns Played: " + player.howManyTurnsPlayed.value + '<br/>' + "ID: " + player.id + '<br/>').appendTo(usersTable);
    $('</tr></td>').appendTo(usersTable);
}