<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Profile — Cinema</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="page">
    <div class="page-header">
        <h1>Profile</h1>
        <form class="signout-form" action="/signOut" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <button type="submit" class="btn">Log out</button>
        </form>
    </div>

    <div class="card profile-summary">
        <img class="avatar" src="${avatarUrl}" alt="Profile picture">
        <div>
            <h2>${user.firstName} ${user.lastName}</h2>
            <p>${user.email}</p>
        </div>
    </div>

    <div class="card">
        <h3>Login history</h3>
        <#if logHistory?has_content>
            <table>
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Time</th>
                    <th>IP</th>
                </tr>
                </thead>
                <tbody>
                <#list logHistory as entry>
                    <tr>
                        <td>${entry.date}</td>
                        <td>${entry.time}</td>
                        <td>${entry.ipAddress}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        <#else>
            <p class="empty-state">No previous logins recorded.</p>
        </#if>
    </div>

    <div class="card">
        <h3>Uploaded files</h3>

        <form class="upload-row" method="post" action="/images" enctype="multipart/form-data">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <input type="file" name="avatar" required>
            <button type="submit" class="btn">Upload</button>
        </form>

        <#if uploadedFiles?has_content>
            <table>
                <thead>
                <tr>
                    <th>File name</th>
                    <th>Size</th>
                    <th>MIME</th>
                </tr>
                </thead>
                <tbody>
                <#list uploadedFiles as file>
                    <tr>
                        <td>
                            <a href="/images/${file.storedName}" target="_blank">${file.originalName}</a>
                        </td>
                        <td>${file.readableSize}</td>
                        <td>${file.mimeType}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        <#else>
            <p class="empty-state">No files uploaded yet.</p>
        </#if>
    </div>
</div>
</body>
</html>