<%--
    Document   : editform
    Author     : Roman Lacko [396157]
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize var="isAdmin" access="hasRole('ROLE_ADMIN')"/>

<form:hidden path="customer"/>
<table border="0" cellpadding="0" cellspacing="0" class="tableForm">
    <tr>
        <td class="left"><f:message key="account.username"/>:</td>
        <td>
            <form:input path="account.username" cssClass="text" readonly="true"/>
        </td>
    </tr>

    <c:choose>
        <c:when test="${editWrapper.reqpasswd}">
            <tr>
                <td class="left">*
                    <c:choose>
                        <c:when test="${isAdmin}">
                            <f:message key="account.password.admin"/>
                        </c:when>
                        <c:otherwise>
                            <f:message key="account.password.old"/>
                        </c:otherwise>
                    </c:choose>:
                </td>
                <td>
                    <form:password path="account.plainPassword" cssClass="text"/>
                    <form:errors path="account.plainPassword"   cssClass="formError"/>
                </td>
            </tr>
        </c:when>
        <c:otherwise>
            <form:hidden path="account.plainPassword"/>
        </c:otherwise>
    </c:choose>
    <c:if test="${editWrapper.modperm}">
        <tr>
            <td colspan="2">
                <f:message key="account.modperm"/>
            </td>
        </tr>
    </c:if>
    <tr>
        <td class="left"><c:if test="${!editWrapper.modperm}">* </c:if><f:message key="account.password.new"/>:</td>
        <td>
            <form:password path="passwd1" cssClass="text"/>
            <form:errors path="passwd1" cssClass="formError"/>
        </td>
    </tr>
    <tr>
        <td class="left"><c:if test="${!editWrapper.modperm}">* </c:if><f:message key="account.password.retype"/>:</td>
        <td>
            <form:password path="passwd2" cssClass="text"/>
            <form:errors path="passwd2" cssClass="formError"/>
        </td>
    </tr>
    <c:choose>
        <c:when test="${editWrapper.modperm}">
            <tr>
                <td class="left"/><f:message key="account.permissions"/>:</td>
                <td>
                    <form:checkboxes path="account.roles" items="${allRoles}" delimiter="<br/>"/>
                    <form:errors path="account.roles" cssClass="formError"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <f:message key="account.permissions.details"/>
                </td>
            </tr>
        </c:when>
        <c:otherwise>
            <form:hidden path="account.roles"/>
        </c:otherwise>
    </c:choose>
</table>

<br/>

<button type="button" class="submit" onclick="javascript:this.form.submit();"><f:message key="basic.submit" /></button>
<c:choose>
    <c:when test="${isAdmin}">
        <button type="button" class="cancel" onclick="javascript:redirect('${pageContext.request.contextPath}/accounts');"><f:message key="basic.cancel" /></button>
    </c:when>
    <c:otherwise>
        <button type="button" class="cancel" onclick="javascript:redirect('${pageContext.request.contextPath}/');"><f:message key="basic.cancel" /></button>
    </c:otherwise>
</c:choose>

