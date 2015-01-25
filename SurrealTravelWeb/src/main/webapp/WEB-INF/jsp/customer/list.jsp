<%--
    Document   : list
    Author     : Jan Klimeš
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<f:message var="title" key="customer.title" />

<t:layout title="${title}">
<jsp:attribute name="content">

    <sec:authorize access="hasRole('ROLE_ADMIN')">
    <button class="new" onclick="javascript:redirect('${pageContext.request.contextPath}/customers/new');"><f:message key="customer.add"/></button>
    <br/>
    <c:if test="${customersOccurence.contains(customersOccurenceCheck)}">
        <br/>
        <jsp:include page="/WEB-INF/include/note.jsp">
            <jsp:param name="key" value="customer.message.undeletable"/>
        </jsp:include>
    </c:if>
    <br/>
    </sec:authorize>

    <table cellspacing="0" cellpadding="3" border="0" class="tableEntry">
        <tr class="head">
            <td width="25">#</td>
            <td><f:message key="customer.name"/></td>
            <td><f:message key="customer.address"/></td>
            <td width="200" align="right"><f:message key="basic.action"/></td>
        </tr>
        <c:choose>
            <c:when test="${not empty customers}">
                <c:forEach items="${customers}" var="customer" varStatus="count">
                    <tr>
                        <td class="number">${count.count}</td>
                        <td><c:out value="${customer.name}"/></td>
                        <td><c:out value="${customer.address}"/></td>
                        <td>
                            <ul class="rowMenu">
                                <jsp:include page="/WEB-INF/include/entryEditButton.jsp">
                                    <jsp:param name="url" value="${pageContext.request.contextPath}/customers/edit/${customer.id}" />
                                </jsp:include>
                                <sec:authorize access="hasRole('ROLE_ADMIN')">
                                    <c:choose>
                                        <c:when test="${customersOccurence.get(count.index) == 0}">
                                            <jsp:include page="/WEB-INF/include/entryDeleteButton.jsp">
                                                <jsp:param name="id" value="${customer.id}" />
                                                <jsp:param name="url" value="${pageContext.request.contextPath}/customers/delete/${customer.id}" />
                                            </jsp:include>
                                        </c:when>
                                        <c:otherwise>
                                            <jsp:include page="/WEB-INF/include/entryDeleteButton.jsp">
                                                <jsp:param name="inactive" value="true" />
                                            </jsp:include>
                                        </c:otherwise>
                                    </c:choose>
                                </sec:authorize>
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
