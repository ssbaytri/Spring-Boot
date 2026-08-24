<!DOCTYPE html>
<html lang="en" data-film-id="${film.id}" data-current-user-id="${currentUserId}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${film.title} - Chat</title>
    <link rel="stylesheet" href="/css/style.css">
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/@stomp/stompjs@7/bundles/stomp.umd.min.js"></script>
</head>
<body>

<header>
    <div class="container">
        <h1>💬 ${film.title} — Live Chat</h1>
        <p class="back-link"><a href="/session/search">&larr; Back to sessions</a></p>
    </div>
</header>

<div class="container">
    <div id="chatBox">
        <#list messages as msg>
            <div class="message <#if msg.user.id == currentUserId>mine</#if>">
                <div class="author">${msg.user.firstName} ${msg.user.lastName}</div>
                <div>${msg.content}</div>
            </div>
        </#list>
    </div>

    <form id="chatForm">
        <input type="text" id="messageInput" placeholder="Type a message..." autocomplete="off" required>
        <button type="submit">Send</button>
    </form>

    <div class="avatar-section">
        <h2>Your Avatar</h2>
        <#if myAvatar??>
            <img src="/images/${myAvatar.storedName}" alt="avatar">
        <#else>
            <p>No avatar uploaded yet.</p>
        </#if>

        <form class="upload-form" action="/images" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <input type="hidden" name="returnTo" value="/films/${film.id}/chat">
            <input type="file" name="avatar" accept="image/*" required>
            <button type="submit">Upload Avatar</button>
        </form>
    </div>

    <div class="images-list">
        <h2>Your Uploaded Images</h2>
        <#if myImages?? && (myImages?size > 0)>
            <ul>
                <#list myImages as image>
                    <li><a href="/images/${image.storedName}" target="_blank">${image.originalName}</a></li>
                </#list>
            </ul>
        <#else>
            <p>No images uploaded yet.</p>
        </#if>
    </div>

    <div class="visits-list">
        <h2>Your Authentication Logs</h2>
        <#if authenticationLogs?? && (authenticationLogs?size > 0)>
            <ul>
                <#list authenticationLogs as log>
                    <li><strong>${log.authenticatedAt.format(formatter)}</strong> — ${log.ipAddress}</li>
                </#list>
            </ul>
        <#else>
            <p>No authentication logs recorded yet.</p>
        </#if>
    </div>
</div>

<script src="/js/chat.js"></script>

</body>
</html>