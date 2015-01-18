<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<f:message var="title" key="account.title.delete"/>

<t:layout title="${title}">
<jsp:attribute name="content">
    
    <jsp:include page="/WEB-INF/include/requiredNotification.jsp" />

    <form:form action="${pageContext.request.contextPath}/accounts/delete" modelAttribute="deleteWrapper">
        <form:hidden path="reqpasswd"/>
        <form:hidden path="account.id" />
        <form:hidden path="account.password"/>
        <form:hidden path="account.customer.id"/>
        <form:hidden path="account.customer.name"/>
        <form:hidden path="account.customer.address"/>
        <jsp:include page="deleteform.jsp" />
    </form:form>
    
</jsp:attribute>
</t:layout>

