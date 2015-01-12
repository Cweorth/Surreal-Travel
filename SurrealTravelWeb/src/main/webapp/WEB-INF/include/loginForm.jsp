<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access="not isAuthenticated()">

    <form name="loginForm" action="<c:url value='/j_spring_security_check' />" method="POST">
        <div class="label"><f:message key="auth.username"/>:</div>
        <input type="text" name="username" class="text">
        <br>
        <div class="label"><f:message key="auth.password"/>:</div>
        <input type="password" name="password" class="text">

        <button type="button" class="submit" onclick="javascript:this.form.submit();"><f:message key="basic.submit" /></button>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    </form>
    
</sec:authorize>
