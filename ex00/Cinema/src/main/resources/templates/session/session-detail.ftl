<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${session.film.title}</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<header>
    <div class="container">
        <h1>🎬 Movie Session Details</h1>
    </div>
</header>

<div class="container">
    <div class="detail-container">
        <a href="/session/search" class="back-link">&larr; Back to search</a>

        <div class="detail-header">
            <div class="detail-poster">
                <#if session.film.posterUrl?? && session.film.posterUrl != "">
                    <img src="${session.film.posterUrl}" alt="poster">
                <#else>
                    <div style="width: 220px; height: 330px; background: #eee; border-radius: 4px;"></div>
                </#if>
            </div>

            <div class="detail-info">
                <h1>${session.film.title}</h1>
                <p><strong>Release year:</strong> ${session.film.releaseYear}</p>
                <p><strong>Age restriction:</strong> ${session.film.ageRestriction}+</p>
                <p><strong>Description:</strong> ${session.film.description!"No description available."}</p>
            </div>
        </div>

        <div class="showtime-section">
            <h2>🎫 Showtime</h2>
            <p><strong>Date/Time:</strong> ${session.dateTime.format(formatter)}</p>
            <p><strong>Hall:</strong> ${session.hall.serialNumber} (${session.hall.seatsNumber} seats)</p>
            <p><strong>Ticket price:</strong> €${session.ticketPrice}</p>
        </div>
    </div>
</div>

</body>
</html>