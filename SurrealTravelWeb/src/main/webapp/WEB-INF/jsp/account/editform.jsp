<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:hidden path="customer"/>
<table border="0" cellpadding="0" cellspacing="0" class="tableForm">
    <tr>
        <td class="left"><f:message key="account.username"/>:</td>
        <td>
            <form:input path="account.username" cssClass="text" readonly="true"/>
        </td>
    </tr>
    <tr>
        <td class="left">* <f:message key="account.password.old"/>:</td>
        <td>
            <form:password path="account.plainPassword" cssClass="text"/>
            <form:errors path="account.plainPassword"   cssClass="formError"/>
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
        <td class="left">* <f:message key="account.retype"/>:</td>
        <td>
            <form:password path="passwd2" cssClass="text"/>
            <form:errors path="passwd2" cssClass="formError"/>
        </td>
    </tr>
</table>

<c:if test="${editWrapper.customer}">
    <br/>
    <h3><f:message key="account.customerdata"/>:</h3>
    <form:hidden path="account.customer.id"/>
    <table border="0" cellpadding="0" cellspacing="0" class="tableForm">
        <tr>
            <td class="left"><f:message key="customer.name"/>:</td>
            <td>
                <form:input path="account.customer.name" cssClass="text" readonly="true"/>
            </td>
        </tr>
        <tr>
            <td class="left"><f:message key="customer.address"/>:</td>
            <td>
                <form:input path="account.customer.address" cssClass="text" readonly="true"/>
            </td>
        </tr>
    </table>    
</c:if>
        
<br/>

<button type="button" class="submit" onclick="javascript:this.form.submit();"><f:message key="basic.submit" /></button>
<button type="button" class="cancel" onclick="javascript:redirect('${pageContext.request.contextPath}/accounts');"><f:message key="basic.cancel" /></button>

