<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<table border="0" cellpadding="0" cellspacing="0" class="tableForm">
    <tr>
        <td class="left">* <f:message key="customer.name"/>:</td>
        <td><form:input path="name" cssClass="text" /><form:errors path="name" cssClass="formError" /></td>
    </tr>
    <tr>
        <td class="left"><f:message key="customer.address"/>:</td>
        <td><form:input path="address" cssClass="text" /></td>
    </tr>
</table>

<br>

<button type="button" class="submit" onclick="javascript:this.form.submit();"><f:message key="basic.submit" /></button>
<button type="button" class="cancel" onclick="javascript:redirect('${pageContext.request.contextPath}/customers');"><f:message key="basic.cancel" /></button>

