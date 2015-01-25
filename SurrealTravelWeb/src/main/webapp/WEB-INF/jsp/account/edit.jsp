<%--
    Document   : edit
    Author     : Roman Lacko [396157]
--%>

<%@page import="java.util.EnumSet"%>
<%@ page import="cz.muni.pa165.surrealtravel.dto.UserRole"         %>
<%@ page import="cz.muni.pa165.surrealtravel.utils.AccountWrapper" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec"  uri="http://www.springframework.org/security/tags" %>

<%
    AccountWrapper wrapper = (AccountWrapper) request.getAttribute("editWrapper");
    if (wrapper.getAccount().getRoles() == null) {
        wrapper.getAccount().setRoles(EnumSet.noneOf(UserRole.class));
    }

    request.setAttribute("accountIsAdmin", wrapper.getAccount().getRoles().contains(UserRole.ROLE_ADMIN));
%>

<sec:authorize      var="isAdmin" access="hasRole('ROLE_ADMIN')"/>
<sec:authentication var="username" property="principal.username"/>
<f:message var="title" key="account.title.edit"/>

<t:layout title="${title}">
<jsp:attribute name="content">
    <jsp:include page="/WEB-INF/include/requiredNotification.jsp" />
    <c:if test="${isAdmin && accountIsAdmin}">
        <br/>
        <c:choose>
            <c:when test="${editWrapper.account.username eq 'root'}"/>
            <c:when test="${editWrapper.account.username eq username}">
                <jsp:include page="/WEB-INF/include/note.jsp">
                    <jsp:param name="key" value="account.message.editAdminSelf"/>
                </jsp:include>
            </c:when>
            <c:otherwise>
                <jsp:include page="/WEB-INF/include/note.jsp">
                    <jsp:param name="key" value="account.message.editByAdmin"/>
                </jsp:include>
            </c:otherwise>
        </c:choose>
        <br/>
    </c:if>

    <form:form action="${pageContext.request.contextPath}/accounts/edit" modelAttribute="editWrapper">
        <c:if test="${not empty editWrapper.account.customer}">
            <form:hidden path="account.customer.id"/>
            <form:hidden path="account.customer.name"/>
            <c:if test="${not empty editWrapper.account.customer.address}">
                <form:hidden path="account.customer.address"/>
            </c:if>
        </c:if>
        <form:hidden path="account.id" />
        <form:hidden path="account.password"/>
        <form:hidden path="reqpasswd"/>
        <form:hidden path="modperm"/>
        <jsp:include page="editform.jsp" />
    </form:form>

</jsp:attribute>
</t:layout>

