<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" trimDirectiveWhitespaces="true" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:layout title="403">
<jsp:attribute name="content">
    <p><f:message key="auth.denied"/></p>
</jsp:attribute>
</t:layout>
