<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up</title>
</head>
<body>

<div class="container">
    <h1>🎬 Sign Up</h1>

    <#if signUpError>
        <div class="error-message">All fields are required.</div>
    </#if>

    <form method="POST" action="/signUp">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

        <label>First Name:
            <input type="text" name="firstName" required>
        </label>

        <label>Last Name:
            <input type="text" name="lastName" required>
        </label>

        <label>Phone Number:
            <input type="text" name="phoneNumber" required>
        </label>

        <label>Email:
            <input type="email" name="email" required>
        </label>

        <label>Password:
            <input type="password" name="password" required>
        </label>

        <button type="submit">Sign Up</button>
    </form>

    <p>Already have an account? <a href="/signIn">Sign In</a></p>
</div>

</body>
</html>