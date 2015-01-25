<%--
    Document   : list
    Author     : Roman Lacko [396157]
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<f:message var="title" key="account.title" />

<t:layout title="${title}">
    <jsp:attribute name="content">

        <button class="new" onclick="javascript:redirect('${pageContext.request.contextPath}/accounts/new');"><f:message key="account.add"/></button>

        <br><br>

        <table cellspacing="0" cellpadding="3" border="0" class="tableEntry">
            <tr class="head">
                <td width="25">#</td>
                <td><f:message key="account.username"/></td>
                <td><f:message key="account.roles"/></td>
                <td><f:message key="customer.name"/></td>
                <td><f:message key="customer.address"/></td>
                <td width="200" align="right"><f:message key="basic.action"/></td>
            </tr>
            <sec:authorize var="isAdmin" access="hasRole('ROLE_ADMIN')"/>
            <sec:authorize var="isRoot"  access="hasRole('ROLE_ROOT')" />
            <c:choose>
                <c:when test="${not empty accounts}">
                    <c:forEach items="${accounts}" var="account" varStatus="count">
                        <tr>
                            <td class="number">${count.index + 1}</td>
                            <td><c:out value="${account.username}"/></td>
                            <td>
                                <c:forEach items="${account.roles}" var="role">
                                    <div class='role <c:out value="${role.toString().toLowerCase()}"/>'>
                                        <c:out value="${role.toString().substring(5)}"/>
                                    </div>
                                </c:forEach>
                            </td>
                            <c:choose>
                                <c:when test="${account.customer == null}">
                                    <td colspan="2">
                                        <i><f:message key="account.notcustomer"/></i>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td><c:out value="${account.customer.name}"/></td>
                                    <td><c:out value="${account.customer.address}"/></td>
                                </c:otherwise>
                            </c:choose>
                            <td>
                                <c:if test="${isAdmin}">
                                    <ul class="rowMenu">
                                        <c:choose>
                                            <c:when test="${isRoot || (account.username ne 'root')}">
                                                <jsp:include page="/WEB-INF/include/entryEditButton.jsp">
                                                    <jsp:param name="url" value="${pageContext.request.contextPath}/accounts/edit/${account.id}" />
                                                </jsp:include>
                                            </c:when>
                                            <c:otherwise>
                                                <jsp:include page="/WEB-INF/include/entryEditButton.jsp">
                                                    <jsp:param name="inactive" value="true" />
                                                </jsp:include>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${account.username ne 'root'}">
                                                <jsp:include page="/WEB-INF/include/entryDeleteButton.jsp">
                                                    <jsp:param name="id"  value="${account.id}" />
                                                    <jsp:param name="url" value="${pageContext.request.contextPath}/accounts/delete/${account.id}" />
                                                </jsp:include>
                                            </c:when>
                                            <c:otherwise>
                                                <jsp:include page="/WEB-INF/include/entryDeleteButton.jsp">
                                                    <jsp:param name="inactive" value="true" />
                                                </jsp:include>
                                            </c:otherwise>
                                        </c:choose>
                                    </ul>
                                </c:if>
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
