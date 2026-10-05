<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Logga in</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/base.css">
</head>
<body>
<main class="container stack">
    <h1>Logga in</h1>
    <p><a href="${pageContext.request.contextPath}/products">Till produkterna</a></p>
    <c:if test="${not empty requestScope.accountError}">
        <p role="alert"><c:out value="${requestScope.accountError}"/></p>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <label class="form-field">
            Användarnamn
            <input type="text" name="username" value="${fn:escapeXml(requestScope.username)}" maxlength="80" autocomplete="username" required>
        </label>
        <label class="form-field">
            Lösenord
            <input type="password" name="password" autocomplete="current-password" required>
        </label>
        <button type="submit">Logga in</button>
    </form>
    <p>Har du inget konto? <a href="${pageContext.request.contextPath}/register">Skapa konto</a></p>
</main>
</body>
</html>
