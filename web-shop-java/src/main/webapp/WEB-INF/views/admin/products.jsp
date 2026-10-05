<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<fmt:setLocale value="sv_SE" />
<!doctype html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Hantera produkter</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/base.css">
</head>
<body>
<main class="container stack">
    <jsp:include page="/WEB-INF/views/shared-nav.jsp" />
    <h1>Hantera produkter</h1>
    <c:if test="${not empty requestScope.adminError}">
        <p role="alert"><c:out value="${requestScope.adminError}"/></p>
    </c:if>
    <c:if test="${not empty requestScope.adminNotice}">
        <p role="status"><c:out value="${requestScope.adminNotice}"/></p>
    </c:if>

    <section class="card stack">
        <h2><c:choose><c:when test="${requestScope.product.id > 0}">Ändra produkt</c:when><c:otherwise>Ny produkt</c:otherwise></c:choose></h2>
        <form method="post" action="${pageContext.request.contextPath}/admin/products" class="stack">
            <input type="hidden" name="id" value="${requestScope.product.id}">
            <label>Artikelnummer
                <input name="sku" maxlength="64" required value="${fn:escapeXml(requestScope.product.sku)}">
            </label>
            <label>Namn
                <input name="name" maxlength="200" required value="${fn:escapeXml(requestScope.product.name)}">
            </label>
            <label>Beskrivning
                <textarea name="description"><c:out value="${requestScope.product.description}"/></textarea>
            </label>
            <label>Pris i kronor
                <input type="number" name="price" min="0" step="0.01" required value="${requestScope.product.price}">
            </label>
            <label>Kategori
                <select name="categoryId">
                    <option value="0">Ingen kategori</option>
                    <c:forEach items="${requestScope.categories}" var="category">
                        <option value="${category.id}" <c:if test="${category.id == requestScope.product.categoryId}">selected</c:if>>
                            <c:out value="${category.name}"/>
                        </option>
                    </c:forEach>
                </select>
            </label>
            <label>Antal i lager
                <input type="number" name="stock" min="0" step="1" required value="${requestScope.product.stock}">
            </label>
            <label>
                <input type="checkbox" name="active" value="true" <c:if test="${requestScope.product.active}">checked</c:if>> Aktiv produkt
            </label>
            <button type="submit">Spara produkt</button>
            <c:if test="${requestScope.product.id > 0}">
                <a href="${pageContext.request.contextPath}/admin/products">Avbryt ändring</a>
            </c:if>
        </form>
    </section>

    <section class="stack">
        <h2>Produkter</h2>
        <c:choose>
            <c:when test="${empty requestScope.products}"><p>Det finns inga produkter.</p></c:when>
            <c:otherwise>
                <ul class="stack product-list">
                    <c:forEach items="${requestScope.products}" var="product">
                        <li class="card">
                            <h3><c:out value="${product.name}"/></h3>
                            <p><c:out value="${product.sku}"/> ·
                                <fmt:formatNumber value="${product.price}" minFractionDigits="2" maxFractionDigits="2"/> kr ·
                                <c:out value="${product.stock}"/> i lager ·
                                <c:choose><c:when test="${product.active}">Aktiv</c:when><c:otherwise>Inaktiv</c:otherwise></c:choose>
                            </p>
                            <p><c:out value="${product.categoryName}"/></p>
                            <a href="${pageContext.request.contextPath}/admin/products?edit=${product.id}">Ändra</a>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>
