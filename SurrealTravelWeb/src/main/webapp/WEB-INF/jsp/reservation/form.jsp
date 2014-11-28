<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<table border="0" cellpadding="0" cellspacing="0" class="tableForm">
    <tr>
        <td class="left">* <f:message key="customer.name"/>:</td>
        <td class="left">* <f:message key="trip.destination"/>:</td>
        <td><tr>
            <select name="custom" path="customer" >
            <c:forEach items="${customers}" var="customer" varStatus="state">
                                                                                            
                                    <option value="${customer}">${customer.name}</option>                                    
                                       
                        </c:forEach></tr></select></td>
    </tr>
    <td></td>
    <tr>
        <select name="trp" path="trip">
            <c:forEach items="${trips}" var="trip" varStatus="state">
                                                                                            
                                    <option value="${trips}">${trip.destination}</option>                                    
                                       
                        </c:forEach></tr></select></td>
    </tr>
    
</table>

<br>

<button type="button" class="submit" onclick="javascript:this.form.submit();"><f:message key="basic.submit" /></button>
<button type="button" class="cancel" onclick="javascript:redirect('${pageContext.request.contextPath}/reservations');"><f:message key="basic.cancel" /></button>