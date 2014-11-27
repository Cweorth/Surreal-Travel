<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<table border="0" cellpadding="0" cellspacing="0" class="tableForm">
    <tr>
        <td class="left">* <f:message key="excursion.description"/>:</td>
        <td><form:input path="description" cssClass="text" /><form:errors path="description" cssClass="formError" delimiter=" " /></td>
    </tr>
    <tr>
        <td class="left">* <f:message key="excursion.destination"/>:</td>
        <td><form:input path="destination" cssClass="text" /><form:errors path="destination" cssClass="formError" delimiter=" " /></td>
    </tr>
    <tr>
        <td class="left">* <f:message key="excursion.excursionDate"/>:</td>
        <td><form:input path="excursionDate" cssClass="text datepicker" /><form:errors path="excursionDate" cssClass="formError" delimiter=" " /></td></td>
    </tr>
    <tr>
        <td class="left">* <f:message key="excursion.duration"/>:</td>
        <td><form:input path="duration" cssClass="text" /><form:errors path="duration" cssClass="formError" delimiter=" " /></td>
    </tr>
        <tr>
        <td class="left">* <f:message key="excursion.price"/>:</td>
        <td><form:input path="price" cssClass="text" /><form:errors path="price" cssClass="formError" delimiter=" " /></td></td>
    </tr>
</table>

<br>

<button type="button" class="submit" onclick="javascript:this.form.submit();"><f:message key="basic.submit" /></button>
<button type="button" class="cancel" onclick="javascript:redirect('${pageContext.request.contextPath}/excursions');"><f:message key="basic.cancel" /></button>

