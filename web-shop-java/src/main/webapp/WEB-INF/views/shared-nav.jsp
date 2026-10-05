<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav aria-label="Huvudmeny">
    <a href="${pageContext.request.contextPath}/products">Produkter</a> ·
    <a href="${pageContext.request.contextPath}/cart">Varukorg</a>
    <c:choose>
        <c:when test="${not empty sessionScope.authenticatedUser}">
            <c:if test="${sessionScope.authenticatedUser.admin}">
                · <a href="${pageContext.request.contextPath}/admin/products">Hantera produkter</a>
                · <a href="${pageContext.request.contextPath}/admin/categories">Hantera kategorier</a>
                · <a href="${pageContext.request.contextPath}/admin/users">Hantera användare</a>
            </c:if>
            <c:if test="${sessionScope.authenticatedUser.warehouse}">
                · <a href="${pageContext.request.contextPath}/warehouse/orders">Beställningar</a>
            </c:if>
            <p>Inloggad som <c:out value="${sessionScope.authenticatedUser.fullName}"/>.</p>
            <form method="post" action="${pageContext.request.contextPath}/logout">
                <button type="submit">Logga ut</button>
            </form>
        </c:when>
        <c:otherwise>
            · <a href="${pageContext.request.contextPath}/login">Logga in</a>
            · <a href="${pageContext.request.contextPath}/register">Skapa konto</a>
        </c:otherwise>
    </c:choose>
</nav>