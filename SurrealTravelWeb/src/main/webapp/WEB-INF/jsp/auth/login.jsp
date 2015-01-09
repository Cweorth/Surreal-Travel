<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<f:message var="title" key="auth.login.title"/>

<t:layout title="${title}">
<jsp:attribute name="content">
    <jsp:include page="/WEB-INF/include/loginForm.jsp" />
</jsp:attribute>
</t:layout>
