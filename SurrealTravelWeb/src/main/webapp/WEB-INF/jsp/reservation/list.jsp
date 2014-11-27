<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<f:message var="title" key="reservation.title" />

<t:layout title="${title}">
<jsp:attribute name="content">

    <button class="new" onclick="javascript:redirect('${pageContext.request.contextPath}/reservations/new');"><f:message key="reservation.add"/></button>
    
    <br><br>
    
    <table cellspacing="0" cellpadding="3" border="0" class="tableEntry">
        <tr class="head">
            <td width="25">#</td>
            <td><f:message key="reservation.name"/></td>
            <td><f:message key="reservation.address"/></td>
            <td width="200" align="right"><f:message key="basic.action"/></td>
        </tr>
        <c:choose>
            <c:when test="${not empty reservations}">
                <c:forEach items="${reservations}" var="reservation" varStatus="count">
                    <tr>
                        <td class="number">${count.index + 1}</td>
                        <td><c:out value="${reservation.customer}"/></td>
                        <td><c:out value="${customer.trip}"/></td>
                        <td>
                            <ul class="rowMenu">
                                <jsp:include page="/WEB-INF/include/entryEditButton.jsp">
                                    <jsp:param name="url" value="${pageContext.request.contextPath}/customers/edit/${reservation.id}" />
                                </jsp:include>
                                <jsp:include page="/WEB-INF/include/entryDeleteButton.jsp">
                                    <jsp:param name="id" value="${customer.id}" />
                                    <jsp:param name="url" value="${pageContext.request.contextPath}/customers/delete/${reservation.id}" />
                                </jsp:include>
                            </ul>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="4" class="noresult"><f:message key="basic.noresult"/></td>
                </tr>
            </c:otherwise>
        </c:choose>
    </table>
            
</jsp:attribute>
</t:layout>
