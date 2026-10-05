<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
    <h1>Produkter</h1>
    <jsp:include page="product-results-content.jsp" />
</main>
</body>
</html>
