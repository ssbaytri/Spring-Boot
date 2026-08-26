<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="${springMacroRequestContext.locale.language}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><@spring.message "signin.title"/></title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<div class="container">
    <div class="lang-selector">
        <a href="?lang=en"><@spring.message "common.english"/></a>
        <a href="?lang=es"><@spring.message "common.spanish"/></a>
    </div>

    <h1><@spring.message "signin.heading"/></h1>

    <#if loginError>
        <div class="error-message"><@spring.message "signin.error"/></div>
    </#if>

    <form method="POST" action="/signIn">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

        <label><@spring.message "signin.email"/>:
            <input type="email" name="email" required>
        </label>

        <label><@spring.message "signin.password"/>:
            <input type="password" name="password" required>
        </label>

        <label class="checkbox-label">
            <input type="checkbox" name="remember-me">
            <@spring.message "signin.rememberMe"/>
        </label>

        <button type="submit"><@spring.message "signin.submit"/></button>
    </form>

    <p><@spring.message "signin.signupLink"/></p>
</div>

</body>
</html>