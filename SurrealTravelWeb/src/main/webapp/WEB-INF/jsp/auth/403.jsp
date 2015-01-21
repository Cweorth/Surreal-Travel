<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<f:message key="basic.errorCode" var="title" />

<t:layout title="${title} 403">
<jsp:attribute name="content">
    <p style="font-size: 16px">
        <f:message key="auth.denied"><f:param value="<span style='font-size: 20px; font-style: italic; font-weight: bold; color: #bc1818'>403</span>" /></f:message>&nbsp;<a href="<c:url value='/' />"><f:message key="auth.denied.mainPage" /></a>.
    </p>
</jsp:attribute>
</t:layout>
