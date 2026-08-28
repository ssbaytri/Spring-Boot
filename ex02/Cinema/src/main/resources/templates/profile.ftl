<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="${springMacroRequestContext.locale.language}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><@spring.message "profile.title"/></title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>
<div class="page">
    <div class="page-header">
        <h1><@spring.message "profile.heading"/></h1>
        <div class="header-actions">
            <div class="lang-selector">
                <a href="?lang=en"><@spring.message "common.english"/></a>
                <a href="?lang=es"><@spring.message "common.spanish"/></a>
            </div>
            <form class="signout-form" action="/signOut" method="post">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <button type="submit" class="btn"><@spring.message "profile.logout"/></button>
            </form>
        </div>
    </div>

    <div class="card profile-summary">
        <img class="avatar" src="${avatarUrl}" alt="<@spring.message "profile.avatarAlt"/>">
        <div>
            <h2>${user.firstName} ${user.lastName}</h2>
            <p>${user.email}</p>
        </div>
    </div>

    <div class="card">
        <h3><@spring.message "profile.loginHistory"/></h3>
        <#if logHistory?has_content>
            <table>
                <thead>
                <tr>
                    <th><@spring.message "profile.date"/></th>
                    <th><@spring.message "profile.time"/></th>
                    <th><@spring.message "profile.ip"/></th>
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
            <p class="empty-state"><@spring.message "profile.noLogins"/></p>
        </#if>
    </div>

    <div class="card">
        <h3><@spring.message "profile.uploadedFiles"/></h3>

        <form class="upload-row" method="post" action="/images" enctype="multipart/form-data">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            <input type="file" name="avatar" required>
            <button type="submit" class="btn"><@spring.message "profile.upload"/></button>
        </form>

        <#if uploadedFiles?has_content>
            <table>
                <thead>
                <tr>
                    <th><@spring.message "profile.fileName"/></th>
                    <th><@spring.message "profile.size"/></th>
                    <th><@spring.message "profile.mime"/></th>
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
            <p class="empty-state"><@spring.message "profile.noFiles"/></p>
        </#if>
    </div>
</div>
</body>
</html>