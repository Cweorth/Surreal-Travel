<%-- 
    Document   : 500
    Created on : Jan 25, 2015, 1:07:18 PM
    Author     : Roman Lacko [396157]
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<f:message key="basic.error500" var="title" />

<t:layout title="${title}">
<jsp:attribute name="content">
    <div align="center">
        <img src="${pageContext.request.contextPath}/image/500.gif" width="500" alt="Aliens"/>
    </div>
    
    <c:choose>
        <c:when test="${pageContext.response.locale eq 'cs'}">
            <jsp:include page="/WEB-INF/include/500_cs.jsp"/>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/include/500_en.jsp"/>
        </c:otherwise>
    </c:choose>
</jsp:attribute>
</t:layout>
