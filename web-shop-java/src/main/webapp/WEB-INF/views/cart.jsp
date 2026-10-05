<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="sv_SE" />
<!doctype html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Varukorg</title>
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
    <h1>Varukorg</h1>
    <p><a href="${pageContext.request.contextPath}/products">Till produkterna</a></p>

    <c:if test="${not empty requestScope.cartError}">
        <p role="alert"><c:out value="${requestScope.cartError}"/></p>
    </c:if>
    <c:if test="${not empty requestScope.cartNotice}">
        <p role="status"><c:out value="${requestScope.cartNotice}"/></p>
    </c:if>

    <c:choose>
        <c:when test="${empty requestScope.cart.items}">
            <p>Varukorgen är tom.</p>
        </c:when>
        <c:otherwise>
            <ul class="stack product-list">
                <c:forEach items="${requestScope.cart.items}" var="item">
                    <li class="card">
                        <h2><c:out value="${item.productName}"/></h2>
                        <p><c:out value="${item.quantity}"/> st ×
                            <fmt:formatNumber value="${item.unitPrice}" minFractionDigits="2" maxFractionDigits="2"/> kr</p>
                        <p>Summa: <fmt:formatNumber value="${item.totalSum}" minFractionDigits="2" maxFractionDigits="2"/> kr</p>
                        <form method="post" action="${pageContext.request.contextPath}/cart">
                            <input type="hidden" name="action" value="remove">
                            <input type="hidden" name="productId" value="${item.productId}">
                            <button type="submit">Ta bort</button>
                        </form>
                    </li>
                </c:forEach>
            </ul>
            <p>Totalt antal varor: <c:out value="${requestScope.cart.totalQuantity}"/></p>
            <p>Totalt: <fmt:formatNumber value="${requestScope.cart.total}" minFractionDigits="2" maxFractionDigits="2"/> kr</p>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
