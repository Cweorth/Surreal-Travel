<%--
    Document   : bulletin
    Created on : 24.1.2015, 17:24:24
    Author     : Jan Klimeš [374259]
--%>

<%@ tag pageEncoding="utf-8" dynamic-attributes="dynattrs" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute  name="title" required="false" %>
<%@ attribute name="body"  required="true"  %>

<h3>${title}</h3>

<div class="bulletinBoard">
    ${body}
</div>
