
var REFRESH_RATE = 2000;
var USER_LIST_URL = buildUrlWithContextPath("UsersList");
var LOGOUT_URL = buildUrlWithContextPath("Logout");
var LOGIN_URL = buildUrlWithContextPath("Login");
var STATUS_URL = buildUrlWithContextPath("Status");
var LOADGAME_URL = buildUrlWithContextPath("LoadGame");
var GAMELIST_URL = buildUrlWithContextPath("GameList");

window.onload = function ()
{
    //refreshLoginStatus();
    refreshUserList();
    setInterval(refreshUserList, REFRESH_RATE);
    setInterval(refreshGamesList, REFRESH_RATE);
    //setInterval(refreshLoginStatus, REFRESH_RATE);
};

// window.onunload = function (event)
// {
//     if (didUserCloseWindow)
//     {
//         $.ajax
//         ({
//             async: false,
//             url: 'login',
//             data: {
//                 action: "close"
//             },
//             type: 'POST'
//         });
//     }
// }

// function checkIfuserInGame() {
//     var result;
//     $.ajax
//     ({
//         async: false,
//         url: 'login',
//         data: {
//             action: "status"
//         },
//         type: 'GET',
//         success: function (json) {
//             result = json.gameNumber != -1;
//         }
//     });
//     return result;
// }
//
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
//
// function isUserComputer() {
//     var result;
//     $.ajax
//     ({
//         async: false,
//         url: 'login',
//         data: {
//             action: "status"
//         },
//         type: 'GET',
//         success: function (json) {
//             result = json.isComputer;
//         }
//     });
//     return result;
// }
//
// function refreshLoginStatus() {
//     $.ajax
//     ({
//         url: 'login',
//         data: {
//             action: "status"
//         },
//         type: 'GET',
//         success: statusCallback
//     });
// }

function statusCallback(json)
{
    if (!json.isConnected)
    {
        window.location = "index.html";
    }
    else if (json.gameNumber != -1)
    {
        window.location = "GameRoom.html";
    }
    else
    {
        $('.userNameSpan').text("Hello " + json.userName + ", logged in as " + (json.isComputer ? "computer" : "human"));
    }
}

function onLogoutClick() {
    $.ajax(
        {
            url: LOGOUT_URL,
            type: 'GET',
            success: logoutCallback
        }
    );
}

function logoutCallback(json) {
    didUserCloseWindow = false;
    window.location = "/NinaRow";
}

function refreshUserList() {
    $.ajax(
        {
            url: USER_LIST_URL,
            type: 'GET',
            success: refreshUserListCallback
        }
    );
}

function refreshUserListCallback(users) {
    var usersTable = $("#GamesHubUsersList");
    usersTable.empty();

    $.each(users || [], function(index, username) {
        $('<tr><td>' + username + '</tr></td>').appendTo(usersTable);
    });
}

function loadGameClicked(event) {
    var file = event.target.files[0];
    var reader = new FileReader();
    var creatorName = getUserName();


    reader.onload = function () {
        var content = reader.result;
        $.ajax(
            {
                url: LOADGAME_URL,
                data: {
                    file: content,
                    creator: creatorName
                },
                type: 'POST',
                success: loadGameCallback
            }
        );
    };

    $.ajax // Getting creator's name.
    ({
        url: STATUS_URL,
        type: 'GET',
        success: function (json) {
            creatorName = json.userName;
            reader.readAsText(file);
        }
    });
}

function loadGameCallback(json) {
    if (json.isLoaded) {
        alert("Load game Success !!");
        refreshGamesList();
        clearFileInput();
    }
    else {
        clearFileInput();
        alert(json.errorMessage);
    }
}

function refreshGamesList() {
    $.ajax
    (
        {
            url: GAMELIST_URL,
            type: 'GET',
            success: refreshGamesListCallback
        }
    )
}

function refreshGamesListCallback(json) {
    var gamesTable = $('.gamesTable tbody');
    gamesTable.empty();
    var gamesList = json.games;

    gamesList.forEach(function (game) {
        var tr = $(document.createElement('tr'));
        var tdGameName = $(document.createElement('td')).text(game.title);
        var tdCreatorName = $(document.createElement('td')).text(game.uploadedBy);
        var tdBoardSize = $(document.createElement('td')).text(game.rows + " X " + game.columns);
        var tdTarget = $(document.createElement('td')).text(game.target);
        var tdGameVariant = $(document.createElement('td')).text(game.variant);
        var tdPlayerNumber = $(document.createElement('td')).text(game.numberOfRegisteredPlayers + " / " + game.numberOfPlayers);

        tdGameName.appendTo(tr);
        tdCreatorName.appendTo(tr);
        tdBoardSize.appendTo(tr);
        tdTarget.appendTo(tr);
        tdGameVariant.appendTo(tr);
        tdPlayerNumber.appendTo(tr);

        tr.appendTo(gamesTable);
    });

    // var tr = $('.tableBody tr');
    // for (var i = 0; i < tr.length; i++) {
    //     tr[i].onclick = createGameDialog;
    // }
}

function removeGameDialog() {
    $('.dialogDiv')[0].style.display = "none";
}

function clearFileInput() {
    document.getElementById("fileInput").value = "";
}

// function createGameDialog(event) {
//     var td = event.currentTarget.children[0];
//     var number = td.innerText;
//     $.ajax
//     (
//         {
//             url: 'games',
//             data: {
//                 action: 'gameDetails',
//                 key: number
//             },
//             type: 'GET',
//             success: createGameDialogCallback
//         }
//     )
// }

function createGameDialogCallback(json) {
    var div = $('.dialogDiv')[0];
    div.style.display = "block";
    var playersNamesDiv = $('.playersNames');

    var key = json.key;
    var creatorName = json.creatorName;
    var gameName = json.gameTitle;
    var boardSize = json.rows + " X " + json.cols;
    var moves = json.moves;
    var playerNumber = json.registeredPlayers + " / " + json.requiredPlayers

    $('.key').text("Game id: " + key + ".");
    $('.creatorName').text("Game Creator: " + creatorName + ".");
    $('.gameName').text("Game Title: " + gameName);
    $('.boardSize').text("Board size: " + boardSize);
    $('.moves').text("Moves number: " + moves);
    $('.playerNumber').text("Players : " + playerNumber);
    for (i = 0; i < json.registeredPlayers; i++) {
        var playerDiv = $(document.createElement('div'));
        playerDiv.addClass('playerDiv');
        playerDiv.appendTo(playersNamesDiv);
    }

    var playerDivs = $('.playerDiv');
    for (i = 0; i < json.registeredPlayers; i++) {
        playerDivs[i].innerHTML = (+i + 1) + '. ' + json.players[i].m_Name + '.';
    }

    createBoard(json.rows, json.cols, json.rowBlocks, json.colBlocks);
}

function createBoard(rows, cols, rowBlocks, colBlocks) {
    var board = $('.board');
    board.contents().remove();
    colBlocksDiv = $(document.createElement('div'));
    colBlocksDiv.addClass('colBlocks');
    colBlocksDiv.appendTo(board);


    for (i = 0; i < rows; i++) { // creates squares + row blocks.
        rowDiv = $(document.createElement('div'));
        rowDiv.addClass('rowDiv');
        rowSquares = $(document.createElement('div'));
        rowSquares.addClass('rowSquares');
        rowBlocksDiv = $(document.createElement('div'));
        rowBlocksDiv.addClass('rowBlocks');
        rowSquares.appendTo(rowDiv);
        rowBlocksDiv.appendTo(rowDiv);

        for (hint = 0; hint < rowBlocks[i].length; hint++) {
            rowHint = $(document.createElement('div'));
            rowHint.addClass('rowHint');
            rowHint.appendTo(rowBlocksDiv);
        }

        for (j = 0; j < cols; j++) { // add the squares.
            squareDiv = $(document.createElement('div'));
            squareDiv.addClass('square');
            squareDiv.appendTo(rowSquares);
        }
        rowDiv.appendTo(board);
    }

    for (col = 0; col < cols; col++) { // creates column blocks.
        colBlockDiv = $(document.createElement('div'));
        colBlockDiv.addClass('colBlock');
        for (hint = 0; hint < colBlocks[col].length; hint++) {
            hintDiv = $(document.createElement('div'));
            hintDiv.addClass('colHint');
            hintDiv.appendTo(colBlockDiv);
            //hintDiv.innerHTML = colBlocks[col][hint];
        }
        colBlockDiv.appendTo(colBlocksDiv);
    }

    var hints = $('.colHint');
    var i = 0;
    for (col = 0; col < cols; col++) { //add columns block numbers (the text inside the divs)
        for (hint = 0; hint < colBlocks[col].length; hint++) {
            hints[i].innerHTML = colBlocks[col][hint];
            i++;
        }
    }

    var hints = $('.rowHint');
    var i = 0;
    for (row = 0; row < rows; row++) { //add row block numbers (the text inside the divs)
        for (hint = 0; hint < rowBlocks[row].length; hint++) {
            hints[i].innerHTML = rowBlocks[row][hint];
            i++;
        }
    }
}

// function joinGameClicked() {
//     var name = getUserName();
//     var isComputer = isUserComputer();
//     var gameId = getGameId();
//     $.ajax
//     (
//         {
//             url: 'games',
//             data: {
//                 action: 'joinGame',
//                 user: name,
//                 isComputer: isComputer,
//                 gameId: gameId
//             },
//             type: 'GET',
//             success: joinGameClickedCallback
//         }
//     );
// }

function joinGameClickedCallback(json) {

    if (json.isLoaded)
    {
        didUserCloseWindow = false;
        window.location = "GameRoom.html";
    }
    else {
        alert(json.errorMessage);
    }
}

function getGameId() {
    var string = $('.key').text();
    var result = +0;
    var i = 9;
    var temp = +string[i];
    while (!isNaN(temp)) // while temp is a number..
    {
        result = result * 10 + temp;
        i++;
        temp = +string[i];
    }
    return result;
}

