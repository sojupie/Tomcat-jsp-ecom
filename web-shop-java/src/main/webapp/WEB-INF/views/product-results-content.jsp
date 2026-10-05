<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<fmt:setLocale value="sv_SE" />
<c:choose>
    <c:when test="${requestScope.catalogError}">
        <p>Sortimentet kunde inte laddas. Försök igen senare.</p>
    </c:when>
    <c:otherwise>
        <p>Visar <c:out value="${fn:length(requestScope.products)}"/> produkter.</p>
        <c:choose>
            <c:when test="${empty requestScope.products}">
                <p>Inga produkter hittades.</p>
            </c:when>
            <c:otherwise>
                <ul>
                    <c:forEach items="${requestScope.products}" var="product">
                        <li>
                            <p><c:out value="${product.categoryName}" default="Ingen kategori"/></p>
                            <h2><c:out value="${product.name}"/></h2>
                            <p><c:out value="${product.description}"/></p>
                            <p><fmt:formatNumber value="${product.price}" minFractionDigits="2" maxFractionDigits="2"/> kr</p>
                            <c:choose>
                                <c:when test="${product.inStock}"><p><c:out value="${product.stock}"/> i lager</p></c:when>
                                <c:otherwise><p>Slut i lager</p></c:otherwise>
                            </c:choose>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>
