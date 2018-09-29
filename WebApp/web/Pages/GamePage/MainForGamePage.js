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
var CHECKPLAYER_URL = buildUrlWithContextPath("CheckPlayer");
var GAME_USERS_LIST_URL = buildUrlWithContextPath("GamePlayersList");
var GAMEDETAILS_URL = buildUrlWithContextPath("GameDetails");
var GAMESTATUS_URL = buildUrlWithContextPath("GameStatus");
var PLAYTURN_URL = buildUrlWithContextPath("PlayTurn");
var gameStarted = 0;
var intervalTimer = 500;
var isFirstStatus = true;
var STATUS_URL = buildUrlWithContextPath("Status");

window.onload = function()
{
    checkLoginStatus();
    refreshUserList();
    setInterval(refreshUserList, REFRESH_RATE);
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

function printBoardcallback(json) {

    for(let i=0; i<json.rows;i++)
    {
        for (let j=0; j<json.columns; j++)
        {
            if (json.Discs[i][j]!==null)
            {

                var color = json.Discs[i][j];
                $(`.col[data-row='${i}'][data-col='${j}']`).css("backgroundColor",color);
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
    SetupOnMouseEnter();
    SetupOnMouseLeave();
    SetupOnMouseClick();

    setInterval(printBoard,intervalTimer);
}

function SetupOnMouseClick() {

    const $board = $("#NinaRow");

    $board.on('click', '.col.empty', function() {
        if ($(".gameStatus").text() !== "Waiting For Players") {
            const col = $(this).data('col');
            checkPlayer(col);
        }
        else {
            alert($(".gameStatus").text());
        }
    });

}

function checkPlayer(col) {

    $.ajax({

        url: CHECKPLAYER_URL,
        type: 'GET',
        success: function(json) {

            checkPlayerCallback(json, col);
        }

    });
 ;
}

function checkPlayerCallback(json, col) {


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

    }


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

    const $board = $("#NinaRow");
    $board.empty();
    for (let row = 0; row < ROWS; row++) {
        const $row = $('<div>')
            .addClass('row');
        for (let col = 0; col < COLS; col++) {
            const $col = $('<div>')
                .addClass('col empty')
                .attr('data-col', col)
                .attr('data-row', row);
            $row.append($col);

            // if (row === ROWS-1)
            // {
            //     console.log("we got here!");
            //     const $overlayCol = $('<button>')
            //         .addClass('Pop')
            //         .text("Pop");
            //     $row.append($overlayCol);
            // }
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
        $('<tr><td>' + user.name + '<br/>' + user.colorName + '<br/>' + "Turns Played: " + user.howManyTurnsPlayed.value + '<br/>' + "ID: " + user.id + '<br/>').appendTo(usersTable);
        $('</tr></td>').appendTo(usersTable);
    });
}