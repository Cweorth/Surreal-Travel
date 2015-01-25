<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<f:message key="basic.error403" var="title" />

<t:layout title="${title}">
<jsp:attribute name="content">
    <div align="center">
        <img src="${pageContext.request.contextPath}/image/403.gif" width="500" alt="The White Whizard"/>
    </div>
    
    <c:choose>
        <c:when test="${pageContext.response.locale eq 'cs'}">
            <jsp:include page="/WEB-INF/include/403_cs.jsp"/>
        </c:when>
        <c:otherwise>
            <jsp:include page="/WEB-INF/include/403_en.jsp"/>
        </c:otherwise>
    </c:choose>
</jsp:attribute>
</t:layout>
