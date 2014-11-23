<%-- 
    Document   : layout
    Created on : 22.11.2014, 12:10:23
    Author     : Jan Klimeš [374259]
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
</head>
<body>
    
<h1><c:out value="${title}"/></h1>

<div id="navigation">
    <ul>
        <li><a href="${pageContext.request.contextPath}/"><f:message key="index.title" /></a></li>
        <li><a href="${pageContext.request.contextPath}/customers"><f:message key="customer.title" /></a></li>
        <li><a href="${pageContext.request.contextPath}/about"><f:message key="about.title" /></a></li>
    </ul>
</div>
    
<div id="content">
    <jsp:invoke fragment="content" />
</div>

</body>
</html>

