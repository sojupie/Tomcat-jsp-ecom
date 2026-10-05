<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Hantera användare</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/base.css">
</head>
<body>
<main class="container stack">
    <jsp:include page="/WEB-INF/views/shared-nav.jsp" />
    <h1>Hantera användare</h1>

    <c:if test="${not empty requestScope.userAdminError}">
        <p role="alert"><c:out value="${requestScope.userAdminError}"/></p>
    </c:if>
    <c:if test="${not empty requestScope.userAdminNotice}">
        <p role="status"><c:out value="${requestScope.userAdminNotice}"/></p>
    </c:if>

    <c:choose>
        <c:when test="${empty requestScope.users}"><p>Det finns inga användare.</p></c:when>
        <c:otherwise>
            <ul class="stack product-list">
                <c:forEach items="${requestScope.users}" var="account">
                    <li class="card stack">
                        <h2><c:out value="${account.fullName}"/></h2>
                        <p>Användarnamn: <c:out value="${account.username}"/></p>
                        <p>E-post: <c:out value="${account.email}"/></p>
                        <c:choose>
                            <c:when test="${account.id == requestScope.currentUserId}">
                                <p>Din egen roll och åtkomst kan inte ändras här.</p>
                            </c:when>
                            <c:otherwise>
                                <form method="post" action="${pageContext.request.contextPath}/admin/users" class="stack">
                                    <input type="hidden" name="userId" value="${account.id}">
                                    <label>Roll
                                        <select name="role" required>
                                            <option value="CUSTOMER" <c:if test="${account.role == 'CUSTOMER'}">selected</c:if>>Kund</option>
                                            <option value="ADMIN" <c:if test="${account.role == 'ADMIN'}">selected</c:if>>Administratör</option>
                                            <option value="WAREHOUSE" <c:if test="${account.role == 'WAREHOUSE'}">selected</c:if>>Lagerpersonal</option>
                                        </select>
                                    </label>
                                    <label>
                                        <input type="checkbox" name="active" value="true"
                                               <c:if test="${account.active}">checked</c:if>> Aktivt konto
                                    </label>
                                    <button type="submit">Spara ändringar</button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:forEach>
            </ul>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
