<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Produkter</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/base.css">
</head>
<body>
<main class="container stack">
    <nav>
        <c:choose>
            <c:when test="${not empty sessionScope.authenticatedUser}">
                <p>Inloggad som <c:out value="${sessionScope.authenticatedUser.fullName}"/>.</p>
                <form method="post" action="${pageContext.request.contextPath}/logout">
                    <button type="submit">Logga ut</button>
                </form>
            </c:when>
            <c:otherwise>
                <p><a href="${pageContext.request.contextPath}/login">Logga in</a> ·
                    <a href="${pageContext.request.contextPath}/register">Skapa konto</a></p>
            </c:otherwise>
        </c:choose>
    </nav>
    <h1>Produkter</h1>
    <jsp:include page="product-results-content.jsp" />
</main>
</body>
</html>
