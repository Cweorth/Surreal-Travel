<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize var="isAdmin" access="hasRole('ROLE_ADMIN')"/>

<table border="0" cellpadding="0" cellspacing="0" class="tableForm">
    <tr>
        <td class="left"><f:message key="account.username"/>:</td>
        <td>
            <form:input path="account.username" cssClass="text" readonly="true"/>
        </td>
    </tr>
    <tr>
        <td class="left">* 
            <c:choose>
                <c:when test="${isAdmin}">
                    <f:message key="account.password.admin"/>
                </c:when>
                <c:otherwise>
                    <f:message key="account.password"/>
                </c:otherwise>
            </c:choose>:
        </td>
        <td>
            <form:password path="account.plainPassword" cssClass="text"/>
            <form:errors path="account.plainPassword"   cssClass="formError"/>
        </td>
    </tr>
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

