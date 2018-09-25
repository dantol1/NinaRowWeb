
var REFRESH_RATE = 2000;
var USER_LIST_URL = buildUrlWithContextPath("UsersList");
var LOGOUT_URL = buildUrlWithContextPath("Logout");
var LOGIN_URL = buildUrlWithContextPath("Login");
var STATUS_URL = buildUrlWithContextPath("Status");
var LOADGAME_URL = buildUrlWithContextPath("LoadGame");
var GAMELIST_URL = buildUrlWithContextPath("GameList");
var GAMEDETAILS_URL = buildUrlWithContextPath("GameDetails");
var JOINGAME_URL = buildUrlWithContextPath("JoinGame");

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
function isUserComputer() {
    var result;
    $.ajax
    ({
        async: false,
        url: STATUS_URL,
        type: 'GET',
        success: function (json) {
            result = json.isComputer;
        }
    });
    return result;
}
function getGameTitle() {
    var string = $('.gameName').text();

    return string;
}
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
        alert("Load game Success!!");
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

    var tr = $('.tableBody tr');
    for (var i = 0; i < tr.length; i++) {
        tr[i].onclick = createGameDialog;
    }
}

function removeGameDialog() {
    $('.dialogDiv')[0].style.display = "none";
}

function clearFileInput() {
    document.getElementById("fileInput").value = "";
}

function createGameDialog(event) {
    var td = event.currentTarget.children[0];
    var gameTitle = td.innerText;
    $.ajax
    (
        {
            url: GAMEDETAILS_URL,
            data: {
                title: gameTitle
            },
            type: 'GET',
            success: createGameDialogCallback
        }
    )
}

function createGameDialogCallback(json) {
    var div = $('.dialogDiv')[0];
    div.style.display = "block";
    var playersNamesDiv = $('.playersNames');

    var target = json.target;
    var creatorName = json.uploadedBy;
    var gameName = json.title;
    var boardSize = json.rows + " X " + json.columns;
    var variant = json.variant;
    var playerNumber = json.numberOfRegisteredPlayers + " / " + json.numberOfPlayers;

    $('.target').text("Target: " + target + ".");
    $('.creatorName').text("Game Creator: " + creatorName + ".");
    $('.gameName').text("Game Title: " + gameName);
    $('.boardSize').text("Board size: " + boardSize);
    $('.variant').text("Variant: " + variant);
    $('.playerNumber').text("Players : " + playerNumber);
    for (i = 0; i < json.registeredPlayers; i++) {
        var playerDiv = $(document.createElement('div'));
        playerDiv.addClass('playerDiv');
        playerDiv.appendTo(playersNamesDiv);
    }

}

function joinGameClicked() {
    var name = getUserName();
    var isComputer = isUserComputer();
    var gameTitle = getGameTitle();
    $.ajax
    (
        {
            url: JOINGAME_URL,
            data: {
                user: name,
                isComputer: isComputer,
                gameTitle: gameTitle
            },
            type: 'GET',
            success: joinGameClickedCallback
        }
    );
}

function joinGameClickedCallback(json) {

    if (json.isLoaded)
    {
        console.log("we are here!");
        didUserCloseWindow = false;
        window.location = "/NinaRow/Pages/GamePage/GamePage.html";
    }
    else {
        alert(json.errorMessage);
    }
}

// function getGameId() {
//     var string = $('.key').text();
//     var result = +0;
//     var i = 9;
//     var temp = +string[i];
//     while (!isNaN(temp)) // while temp is a number..
//     {
//         result = result * 10 + temp;
//         i++;
//         temp = +string[i];
//     }
//     return result;
// }

