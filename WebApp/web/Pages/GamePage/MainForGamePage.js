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
var CHECKPLAYER_URL = buildUrlWithContextPath("CheckPlayer");
var GAME_USERS_LIST_URL = buildUrlWithContextPath("GamePlayersList");
var GAMEDETAILS_URL = buildUrlWithContextPath("GameDetails");
var GAMESTATUS_URL = buildUrlWithContextPath("GameStatus");
var PLAYTURN_URL = buildUrlWithContextPath("PlayTurn");
var CURRENT_PLAYER_INFO_URL = buildUrlWithContextPath("PlayerInfo");
var ACTIVE_PLAYER_INFO_URL = buildUrlWithContextPath("ActivePlayerTurn");
var gameStarted = 0;
var intervalTimer = 500;
var isFirstStatus = true;
var STATUS_URL = buildUrlWithContextPath("Status");

window.onload = function()
{
    checkLoginStatus();
    refreshUserList();
    createUserInfoGrid();
    setInterval(refreshUserList, REFRESH_USER_RATE);
    setInterval(checkGameStatus, REFRESH_RATE);
};

function checkGameStatus() {

        $.ajax({

            url: GAMESTATUS_URL,
            type: 'GET',
            success: checkGameStatusCallback

        });
}

function checkGameStatusCallback(json) {

    $(".gameStatus").text(json.status);

    if (json.status === "Started") {
        if (gameStarted === 0)
        {
            alert("The Game Has Started!");
            gameStarted = 1;
            setInterval(updatePlayerTurn, REFRESH_USER_RATE);
        }
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

    // NinaRow = new NinaRow('#NinaRow',json);

    createGrid(json);
    setInterval(printBoard,intervalTimer);
}

function SetupOnMouseClick() {

    const $board = $("#NinaRow");

    $board.on('click', '.col.empty', function() {
        if ($(".gameStatus").text() !== "Waiting For Players") {
            checkPlayer(col);
        }
        else {
            alert($(".gameStatus").text());
        }
    });

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

    if (json !== null) {
        printBoardcallback(json);
        createUserInfoGrid();
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
    const playerName = $("#activePlayerName");
    playerName.empty();
    playerName.text(json);
}

function SetupOnMouseLeave() {

    const $board = $("#NinaRow");

    $board.on('mouseleave', '.col', function() {
        $('.col').removeClass(`next-stam`);
    });
}

function SetupOnMouseEnter() {

    const $board = $("#NinaRow");

    $board.on('mouseenter', '.col.empty', function() {
        const col = $(this).data('col');
        const $lastEmptyCell = findLastEmptyCell(col);
        $lastEmptyCell.addClass(`next-stam`);
    });
}

function findLastEmptyCell(col) {

    const cells = $(`.col[data-col='${col}']`);
    for (let i = cells.length - 1; i >= 0; i--) {
        const $cell = $(cells[i]);
        if ($cell.hasClass('empty')) {
            return $cell;
        }
    }
    return null;
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