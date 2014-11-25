<%-- 
    Document   : layout
    Created on : 22.11.2014, 12:10:23
    Author     : Jan KlimeÅ¡ [374259]
--%>

<%@ tag pageEncoding="utf-8" dynamic-attributes="dynattrs" trimDirectiveWhitespaces="true" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="content" fragment="true" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="${pageContext.request.locale}">
<head>
    <title><c:out value="${title}"/></title>
    <meta charset="utf-8" />
    <meta name="keywords" content="travel agency, cestovní agentura, holiday, vacation, dovolená, last minute, krásy Afganistánu" />
    <meta name="description" content="<f:message key="basic.description" />" />
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css"/>
    <link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/themes/smoothness/jquery-ui.css" />
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/base.js"></script>
</head>
<body>

<div id="status" class="ui-corner-all">
    <div><img src="${pageContext.request.contextPath}/image/loading.gif" alt="loading" /></div>
    <p><f:message key="basic.processing" /></p>
</div>

<c:if test="${not empty successMessage}">
    <jsp:include page="/WEB-INF/include/successMessage.jsp" />
</c:if>

<ul id="navigation">
    <li><a href="${pageContext.request.contextPath}/"><f:message key="index.title" /></a></li>
    <li><a href="${pageContext.request.contextPath}/trips"><f:message key="trip.title" /></a></li>
    <li><a href="${pageContext.request.contextPath}/excursions"><f:message key="excursion.title" /></a></li>
    <li><a href="${pageContext.request.contextPath}/reservations"><f:message key="reservation.title" /></a></li>
    <li><a href="${pageContext.request.contextPath}/customers"><f:message key="customer.title" /></a></li>
    <li><a href="${pageContext.request.contextPath}/about"><f:message key="about.title" /></a></li>
</ul>

<br style="clear: both;"> 
    
<h1><c:out value="${title}"/></h1>
    
<div id="content">
    <jsp:invoke fragment="content" />
</div>

</body>
</html>

