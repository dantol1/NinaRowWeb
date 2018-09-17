$(document).ready(function() {
    NinaRow = new NinaRow('#NinaRow')

    NinaRow.onPlayerMove = function() {
        $('#player').text(NinaRow.player);
    }

    $('#restart').click(function() {
        NinaRow.restart();
    })
});