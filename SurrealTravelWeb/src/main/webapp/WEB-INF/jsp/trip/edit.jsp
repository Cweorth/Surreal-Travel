<%--
    Document   : edit
    Created on : Nov 26 2014, 4:08:14 PM
    Author     : Roman Lacko [396157]
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<f:message var="title" key="trip.title.edit"/>

<t:layout title="${title}">
<jsp:attribute name="content">

    <jsp:include page="/WEB-INF/include/requiredNotification.jsp" />
    <jsp:include page="/WEB-INF/include/dateFormatNotification.jsp" />

    <form:form action="${pageContext.request.contextPath}/trips/edit" modelAttribute="tripdata">
        <form:hidden path="id"/>
        <jsp:include page="form.jsp"/>
    </form:form>

</jsp:attribute>
</t:layout>

