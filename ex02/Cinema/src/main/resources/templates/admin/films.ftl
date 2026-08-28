<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Films — Cinema Admin</title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="page">
    <div class="page-header">
        <h1>Films</h1>
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
        <h3>Add film</h3>
        <form method="post" action="/admin/panel/films" enctype="multipart/form-data">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <label>Title:
                <input type="text" name="title" required>
            </label>
            <label>Release year:
                <input type="number" name="releaseYear" required>
            </label>
            <label>Age restriction:
                <input type="number" name="ageRestriction" min="0" required>
            </label>
            <label>Description:
                <textarea name="description" rows="3"></textarea>
            </label>
            <label>Poster:
                <input type="file" name="poster" accept="image/*">
            </label>
            <button type="submit" class="btn">Create</button>
        </form>
    </div>

    <div class="card">
        <h3>Existing films</h3>
        <#if films?has_content>
            <table>
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Year</th>
                    <th>Age</th>
                    <th>Description</th>
                    <th>Poster</th>
                </tr>
                </thead>
                <tbody>
                <#list films as film>
                    <tr>
                        <td>${film.title}</td>
                        <td>${film.releaseYear}</td>
                        <td>${film.ageRestriction}</td>
                        <td>${film.description!''}</td>
                        <td>
                            <#if film.posterUrl?? && film.posterUrl != "">
                                <a href="${film.posterUrl}" target="_blank">
                                    <img src="${film.posterUrl}" alt="Poster" width="50">
                                </a>
                            </#if>
                        </td>
                    </tr>
                </#list>
                </tbody>
            </table>
        <#else>
            <p class="empty-state">No films yet.</p>
        </#if>
    </div>
</div>
</body>
</html>