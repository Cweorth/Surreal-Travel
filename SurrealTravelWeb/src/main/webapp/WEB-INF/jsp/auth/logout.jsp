<%--
    Document   : logout
    Author     : Jan Klimeš
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<f:message var="title" key="auth.logout.title"/>

<t:layout title="${title}">
<jsp:attribute name="content">

    <p><f:message key="auth.logout.success"/></p>

</jsp:attribute>
</t:layout>

