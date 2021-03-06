<%--
    Document   : edit
    Author     : Jan Klimeš
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<f:message var="title" key="customer.title.edit"/>

<t:layout title="${title}">
<jsp:attribute name="content">

    <jsp:include page="/WEB-INF/include/requiredNotification.jsp" />

    <form:form action="${pageContext.request.contextPath}/customers/edit" modelAttribute="customerDTO">
        <form:hidden path="id" />
        <jsp:include page="form.jsp" />
    </form:form>

</jsp:attribute>
</t:layout>

