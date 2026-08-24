<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Movie Sessions</title>
    <link rel="stylesheet" href="/css/style.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
</head>
<body>
<header>
    <div class="container">
        <h1>🎬 Search Movie Sessions</h1>
    </div>
</header>

<div class="container">
    <div class="search-bar">
        <input type="text" id="filmSearch" placeholder="Search by film title..." autocomplete="off">
    </div>
    <div id="results" class="sessions-grid"></div>
</div>

<script>
    function renderResults(sessions) {
        const container = $('#results');
        container.empty();

        if (sessions.length === 0) {
            container.append('<div class="no-results"><p>No sessions found.</p></div>');
            return;
        }

        sessions.forEach(function (session) {
            const posterSrc = session.film.posterUrl ? session.film.posterUrl : '';

            const card = $('<div class="session-card"></div>');

            if (posterSrc) {
                card.append('<img src="' + posterSrc + '" alt="poster">');
            } else {
                card.append('<div style="width: 100%; height: 200px; background: var(--bg);"></div>');
            }

            const content = $('<div class="session-card-content"></div>');
            content.append('<h3>' + session.film.name + '</h3>');
            content.append('<p class="meta"><span>' + session.dateTime + '</span></p>');
            content.append($('<a class="btn"></a>')
                .attr('href', '/sessions/' + session.id)
                .text('View Details'));

            card.append(content);
            container.append(card);
        });
    }

    function fetchSessions(query) {
        $.get('/sessions/search', { filmName: query }, function (data) {
            renderResults(data.sessions);
        });
    }

    $(document).ready(function () {
        fetchSessions('');

        $('#filmSearch').on('input', function () {
            const query = $(this).val();
            fetchSessions(query);
        });
    });
</script>

</body>
</html>