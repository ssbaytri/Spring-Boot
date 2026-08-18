<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sessions — Cinema Admin</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="page">
    <div class="page-header">
        <h1>Sessions</h1>
        <div class="header-actions">
            <a class="btn" href="/profile">Profile</a>
            <form class="signout-form" action="/signOut" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <button type="submit" class="btn">Log out</button>
            </form>
        </div>
    </div>

    <#if success??>
        <div class="alert-success">${success}</div>
    </#if>

    <div class="card">
        <h3>Add session</h3>
        <form method="post" action="/admin/panel/sessions">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <label>Film:
                <select name="filmId" required>
                    <#list films as film>
                        <option value="${film.id}">${film.title}</option>
                    </#list>
                </select>
            </label>
            <label>Hall:
                <select name="hallId" required>
                    <#list halls as hall>
                        <option value="${hall.id}">${hall.serialNumber}</option>
                    </#list>
                </select>
            </label>
            <label>Date & time:
                <input type="text" name="dateTime" placeholder="dd/MM/yyyy HH:mm" required>
            </label>
            <label>Ticket price:
                <input type="number" name="ticketPrice" step="0.01" min="0" required>
            </label>
            <button type="submit" class="btn">Create</button>
        </form>
    </div>

    <div class="card">
        <h3>Existing sessions</h3>
        <#if sessions?has_content>
            <table>
                <thead>
                <tr>
                    <th>Film</th>
                    <th>Hall</th>
                    <th>Date & time</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                <#list sessions as session>
                    <tr>
                        <td>${session.film.title}</td>
                        <td>${session.hall.serialNumber}</td>
                        <td>${formatter.format(session.dateTime)}</td>
                        <td>${session.ticketPrice}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        <#else>
            <p class="empty-state">No sessions yet.</p>
        </#if>
    </div>
</div>
</body>
</html>