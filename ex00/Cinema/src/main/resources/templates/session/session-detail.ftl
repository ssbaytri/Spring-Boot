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
                    <div class="poster-placeholder"></div>
                </#if>
            </div>

            <div class="detail-info">
                <h1>${session.film.title}</h1>
                <div class="film-meta">
                    <span class="meta-badge">Release: ${session.film.releaseYear}</span>
                    <span class="meta-badge">Age: ${session.film.ageRestriction}+</span>
                </div>
                <div class="film-description">
                    <h3>Description</h3>
                    <p>${session.film.description!"No description available."}</p>
                </div>
            </div>
        </div>

        <div class="showtime-section">
            <h2>🎫 Showtime Details</h2>
            <div class="showtime-grid">
                <div class="showtime-item">
                    <span class="showtime-label">Date & Time</span>
                    <span class="showtime-value">${session.dateTime.format(formatter)}</span>
                </div>
                <div class="showtime-item">
                    <span class="showtime-label">Hall</span>
                    <span class="showtime-value">${session.hall.serialNumber} (${session.hall.seatsNumber} seats)</span>
                </div>
                <div class="showtime-item">
                    <span class="showtime-label">Ticket Price</span>
                    <span class="showtime-value">€${session.ticketPrice}</span>
                </div>
            </div>
        </div>

        <div class="action-section">
            <a href="/films/${session.film.id}/chat" class="btn btn-chat">
                💬 Join Film Chat
            </a>
        </div>
    </div>
</div>

</body>
</html>