<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Hantera kategorier</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/base.css">
</head>
<body>
<main class="container stack">
    <nav>
        <a href="${pageContext.request.contextPath}/products">Produkter</a> ·
        <a href="${pageContext.request.contextPath}/admin/products">Hantera produkter</a>
    </nav>
    <h1>Hantera kategorier</h1>
    <c:if test="${not empty requestScope.adminError}">
        <p role="alert"><c:out value="${requestScope.adminError}"/></p>
    </c:if>
    <c:if test="${not empty requestScope.adminNotice}">
        <p role="status"><c:out value="${requestScope.adminNotice}"/></p>
    </c:if>

    <section class="card stack">
        <h2><c:choose><c:when test="${requestScope.category.id > 0}">Ändra kategori</c:when><c:otherwise>Ny kategori</c:otherwise></c:choose></h2>
        <form method="post" action="${pageContext.request.contextPath}/admin/categories" class="stack">
            <input type="hidden" name="id" value="${requestScope.category.id}">
            <label>Namn
                <input name="name" maxlength="120" required value="${fn:escapeXml(requestScope.category.name)}">
            </label>
            <label>Beskrivning
                <textarea name="description"><c:out value="${requestScope.category.description}"/></textarea>
            </label>
            <button type="submit">Spara kategori</button>
            <c:if test="${requestScope.category.id > 0}">
                <a href="${pageContext.request.contextPath}/admin/categories">Avbryt ändring</a>
            </c:if>
        </form>
    </section>

    <section class="stack">
        <h2>Kategorier</h2>
        <c:choose>
            <c:when test="${empty requestScope.categories}"><p>Det finns inga kategorier.</p></c:when>
            <c:otherwise>
                <ul class="stack product-list">
                    <c:forEach items="${requestScope.categories}" var="category">
                        <li class="card">
                            <h3><c:out value="${category.name}"/></h3>
                            <p><c:out value="${category.description}"/></p>
                            <a href="${pageContext.request.contextPath}/admin/categories?edit=${category.id}">Ändra</a>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </section>
</main>
</body>
</html>
