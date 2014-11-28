<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<table border="0" cellpadding="0" cellspacing="0" class="tableForm">
    <tr>
        <td class="left">* <f:message key="reservation.customer"/>:</td>
        <td>
            <form:select path="customer">
                <form:options items="${customers}" itemValue="id" itemLabel="name"/>
            </form:select>
        </td>
    </tr>
    <tr>
        <td class="left">* <f:message key="reservation.trip"/>:</td>
        <td>
            <form:select path="trip">
                <form:options items="${trips}" itemValue="id" itemLabel="destination"/>
            </form:select>
        </td>
    </tr>
</table>

<br>

<button type="button" class="submit" onclick="javascript:this.form.submit();"><f:message key="basic.submit" /></button>
<button type="button" class="cancel" onclick="javascript:redirect('${pageContext.request.contextPath}/reservations');"><f:message key="basic.cancel" /></button>