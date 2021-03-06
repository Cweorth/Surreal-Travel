<%--
    Document   : about
    Author     : Jan Klimeš
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<f:message var="title" key="about.title"/>

<t:layout title="${title}">
<jsp:attribute name="content">
    <p><f:message key="basic.about.content"/></p>
</jsp:attribute>
</t:layout>
