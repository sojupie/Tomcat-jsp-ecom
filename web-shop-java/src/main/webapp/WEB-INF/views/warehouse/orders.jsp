<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="sv_SE" />
<!doctype html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Beställningar</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/base.css">
</head>
<body>
<main class="container stack">
    <jsp:include page="/WEB-INF/views/shared-nav.jsp" />
    <h1>Beställningar</h1>
    <c:if test="${not empty requestScope.warehouseError}">
        <p role="alert"><c:out value="${requestScope.warehouseError}"/></p>
    </c:if>
    <c:if test="${not empty requestScope.warehouseNotice}">
        <p role="status"><c:out value="${requestScope.warehouseNotice}"/></p>
    </c:if>

    <c:choose>
        <c:when test="${empty requestScope.orders}"><p>Det finns inga beställningar.</p></c:when>
        <c:otherwise>
            <ul class="stack product-list">
                <c:forEach items="${requestScope.orders}" var="order">
                    <li class="card stack">
                        <h2>Beställning #<c:out value="${order.id}"/></h2>
                        <p>Kund: <c:out value="${order.customerName}"/></p>
                        <p>Status: <c:out value="${order.statusLabel}"/></p>
                        <p>Summa: <fmt:formatNumber value="${order.total}" minFractionDigits="2" maxFractionDigits="2"/> kr</p>
                        <ul>
                            <c:forEach items="${order.items}" var="item">
                                <li><c:out value="${item.productName}"/> — <c:out value="${item.quantity}"/> st</li>
                            </c:forEach>
                        </ul>
                        <c:if test="${order.readyForPacking}">
                            <form method="post" action="${pageContext.request.contextPath}/warehouse/orders">
                                <input type="hidden" name="action" value="pack">
                                <input type="hidden" name="orderId" value="${order.id}">
                                <button type="submit">Markera som packad</button>
                            </form>
                        </c:if>
                    </li>
                </c:forEach>
            </ul>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
