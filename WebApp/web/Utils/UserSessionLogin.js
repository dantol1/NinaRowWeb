var CHECK_USER_SESSION_URL = buildUrlWithContextPath("UserSession");

window.onload = function () {
    $.ajax({
        url:CHECK_USER_SESSION_URL,
        type: 'GET',
        success: checkUserSessionCallback
    })
};

function checkUserSessionCallback(json) {
    if(json.session === 'ConnectedToHub')
    {
        window.location = '/NinaRow/Pages/GamesHubPage/GamesHubPage.html';
    }
    else if(json.session === 'ConnectedToGame')
    {
        window.location = '/NinaRow/Pages/GamePage/GamePage.html';
    }
}