<#import "/spring.ftl" as spring>
<!DOCTYPE html>
<html lang="${springMacroRequestContext.locale.language}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><@spring.message "signup.title"/></title>
    <link rel="stylesheet" href="/css/style.css">
</head>
<body>

<div class="container">
    <div class="lang-selector">
        <a href="?lang=en"><@spring.message "common.english"/></a>
        <a href="?lang=es"><@spring.message "common.spanish"/></a>
    </div>

    <h1><@spring.message "signup.heading"/></h1>

    <#if signUpError>
        <div class="error-message"><@spring.message "signup.error"/></div>
    </#if>

    <form method="POST" action="/signUp">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

        <label><@spring.message "signup.firstName"/>:
            <input type="text" name="firstName" required>
        </label>

        <label><@spring.message "signup.lastName"/>:
            <input type="text" name="lastName" required>
        </label>

        <label><@spring.message "signup.phone"/>:
            <input type="text" name="phoneNumber" required>
        </label>

        <label><@spring.message "signup.email"/>:
            <input type="email" name="email" required>
        </label>

        <label><@spring.message "signup.password"/>:
            <input type="password" name="password" required>
        </label>

        <button type="submit"><@spring.message "signup.submit"/></button>
    </form>

    <p><@spring.message "signup.signinLink"/></p>
</div>

</body>
</html>