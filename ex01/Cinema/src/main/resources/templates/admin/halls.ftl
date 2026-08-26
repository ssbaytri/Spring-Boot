<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Halls — Cinema Admin</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="page">
    <div class="page-header">
        <h1>Halls</h1>
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
        <h3>Add hall</h3>
        <form method="post" action="/admin/panel/halls">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <label>Serial number:
                <input type="text" name="serialNumber" required>
            </label>
            <label>Seats number:
                <input type="number" name="seatsNumber" min="1" required>
            </label>
            <button type="submit" class="btn">Create</button>
        </form>
    </div>

    <div class="card">
        <h3>Existing halls</h3>
        <#if halls?has_content>
            <table>
                <thead>
                <tr>
                    <th>Serial number</th>
                    <th>Seats</th>
                </tr>
                </thead>
                <tbody>
                <#list halls as hall>
                    <tr>
                        <td>${hall.serialNumber}</td>
                        <td>${hall.seatsNumber}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        <#else>
            <p class="empty-state">No halls yet.</p>
        </#if>
    </div>
</div>
</body>
</html>