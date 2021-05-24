<%@ page import="com.a1qa.model.domain.Variant" %>
<%@ page import="com.a1qa.model.domain.Token" %>
<%@ page import="com.a1qa.common.utils.HibernateUtil" %>
<%@ page import="com.a1qa.common.QueryParam" %>
<%@ page import="com.a1qa.common.NamedParam" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    Cookie[] cookies = request.getCookies();
    Variant variant = new Variant();
    variant.setId(0);
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                variant = ((Token) HibernateUtil.getEntities(Token.TOKEN_BY_VALUE,
                        new QueryParam(NamedParam.TOKEN_VALUE, cookie.getValue())).get(0)).getVariant();
            }
        }
    }
    request.setAttribute("variant", variant);
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Union Reporting Web</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta charset="utf-8">
        <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/resources/css/reporting.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/resources/css/chosen.min.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/resources/css/messi.min.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/resources/css/footer.css" rel="stylesheet" type="text/css">
        <link href="${pageContext.request.contextPath}/resources/css/header.css" rel="stylesheet" type="text/css">
        <script src="${pageContext.request.contextPath}/resources/js/jquery-1.11.3.min.js"></script>
    </head>
    <body>
        <div class="container main-container">
            <ol class="breadcrumb">
                <li><a href="${pageContext.request.contextPath}/projects">Home</a></li>
                <c:choose>
                    <c:when test="${not empty breadCrumbs}">
                        <c:set var="breadCrumbsSize" value="${fn:length(breadCrumbs)}" />
                        <c:forEach items="${breadCrumbs}" var="breadCrumb" varStatus="count">
                            <c:choose>
                                <c:when test="${count.index lt breadCrumbsSize - 1}">
                <li><a href="${pageContext.request.contextPath}${breadCrumb.getUri()}">${breadCrumb.getName()}</a></li>
                                </c:when>
                                <c:otherwise>
                <li>${breadCrumb.getName()}</li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </ol>
