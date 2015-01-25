<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="not hasRole('ROLE_ADMIN')">
    <input type="hidden" name="customer" id="customer" value="1"/>
</sec:authorize>

<table border="0" cellpadding="0" cellspacing="0" class="tableForm">
    <tr>
        <td class="left">* <f:message key="account.username"/>:</td>
        <td>
            <form:input path="account.username" cssClass="text" />
            <form:errors path="account.username" cssClass="formError"/>
        </td>
    </tr>
    <tr>
        <td class="left">* <f:message key="account.password"/>:</td>
        <td>
            <form:password path="passwd1" cssClass="text"/>
            <form:errors path="passwd1" cssClass="formError"/>
        </td>
    </tr>
    <tr>
        <td class="left">* <f:message key="account.password.retype"/>:</td>
        <td>
            <form:password path="passwd2" cssClass="text"/>
            <form:errors path="passwd2" cssClass="formError"/>
        </td>
    </tr>
</table>
<br/>
<h3><f:message key="account.customerdata"/>:</h3>
<table border="0" cellpadding="0" cellspacing="0" class="tableForm">
    <sec:authorize access="hasRole('ROLE_ADMIN')">
    <tr>
        <td class="left">  <f:message key="account.customer"/>:</td>
        <td><form:checkbox path="customer" id="customer"/></td>
    </tr>
    </sec:authorize>
    <tr>
        <td class="left">* <f:message key="customer.name"/>:</td>
        <td>
            <form:input id="customer-name" path="account.customer.name" cssClass="text"/>
            <form:errors path="account.customer.name" cssClass="formError"/>
        </td>
    </tr>
    <tr>
        <td class="left">* <f:message key="customer.address"/>:</td>
        <td>
            <form:input id="customer-address" path="account.customer.address" cssClass="text"/>
            <form:errors path="account.customer.address" cssClass="formError"/>
        </td>
    </tr>
</table>

<br>

<button type="button" class="submit" onclick="javascript:this.form.submit();"><f:message key="basic.submit" /></button>
<button type="button" class="cancel" onclick="javascript:redirect('${pageContext.request.contextPath}/accounts');"><f:message key="basic.cancel" /></button>

