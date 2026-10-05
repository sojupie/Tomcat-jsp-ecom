<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Skapa konto</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/base.css">
</head>
<body>
<main class="container stack">
    <h1>Skapa konto</h1>
    <p><a href="${pageContext.request.contextPath}/products">Till produkterna</a></p>
    <c:if test="${not empty requestScope.accountError}">
        <p role="alert"><c:out value="${requestScope.accountError}"/></p>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/register">
        <label class="form-field">
            Namn
            <input type="text" name="fullName" value="${fn:escapeXml(requestScope.fullName)}" maxlength="160" autocomplete="name" required>
        </label>
        <label class="form-field">
            E-post
            <input type="email" name="email" value="${fn:escapeXml(requestScope.email)}" maxlength="254" autocomplete="email" required>
        </label>
        <label class="form-field">
            Användarnamn
            <input type="text" name="username" value="${fn:escapeXml(requestScope.username)}" maxlength="80" autocomplete="username" required>
        </label>
        <label class="form-field">
            Lösenord
            <input type="password" name="password" minlength="8" maxlength="128" autocomplete="new-password" required>
        </label>
        <button type="submit">Skapa konto</button>
    </form>
    <p>Har du redan ett konto? <a href="${pageContext.request.contextPath}/login">Logga in</a></p>
</main>
</body>
</html>
