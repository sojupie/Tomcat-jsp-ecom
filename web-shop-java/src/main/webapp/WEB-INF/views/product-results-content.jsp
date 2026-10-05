<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<fmt:setLocale value="sv_SE" />
<p><a href="${pageContext.request.contextPath}/cart">Visa varukorg</a></p>
<c:choose>
    <c:when test="${requestScope.catalogError}">
        <p>The catalogue could not be loaded. Please try again later.</p>
    </c:when>
    <c:otherwise>
        <p>Visar <c:out value="${fn:length(requestScope.products)}"/> produkter.</p>
        <c:choose>
            <c:when test="${empty requestScope.products}">
                <p>Inga produkter hittades.</p>
            </c:when>
            <c:otherwise>
                <ul class="grid product-list">
                    <c:forEach items="${requestScope.products}" var="product">
                        <li class="card">
                            <p><c:out value="${product.categoryName}" default="Ingen kategori"/></p>
                            <h2><c:out value="${product.name}"/></h2>
                            <p><c:out value="${product.description}"/></p>
                            <p><fmt:formatNumber value="${product.price}" minFractionDigits="2" maxFractionDigits="2"/> kr</p>
                            <c:choose>
                                <c:when test="${product.inStock}"><p><c:out value="${product.stock}"/> i lager</p></c:when>
                                <c:otherwise><p>Slut i lager</p></c:otherwise>
                            </c:choose>
                            <form method="post" action="${pageContext.request.contextPath}/cart">
                                <input type="hidden" name="action" value="add">
                                <input type="hidden" name="productId" value="${product.id}">
                                <label class="form-field">
                                    Antal
                                    <input type="number" name="quantity" value="1" min="1" required>
                                </label>
                                <button type="submit">Lägg i varukorg</button>
                            </form>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>
