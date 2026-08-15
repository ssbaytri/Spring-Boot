<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In</title>
</head>
<body>

<div class="container">
    <h1>🎬 Sign In</h1>

    <#if loginError>
        <div class="error-message">Incorrect email or password.</div>
    </#if>

    <form method="POST" action="/signIn">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

        <label>Email:
            <input type="email" name="email" required>
        </label>

        <label>Password:
            <input type="password" name="password" required>
        </label>

        <label class="checkbox-label">
            <input type="checkbox" name="remember-me">
            Remember me
        </label>

        <button type="submit">Sign In</button>
    </form>

    <p>Don't have an account? <a href="/signUp">Sign Up</a></p>
</div>

</body>
</html>