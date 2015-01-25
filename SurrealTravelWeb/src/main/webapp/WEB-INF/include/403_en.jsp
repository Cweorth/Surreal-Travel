<%-- 
    Document   : 403_en
    Created on : Jan 25, 2015, 11:10:09 AM
    Author     : Roman Lacko [396157]
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<p style="text-align: center">
    <span style="font-size: 200%">
        The White Wizard has <b><span style="font-size: 150%">403</span></b> reasons for not letting You see this page.
    </span>
    <br/>
    That rascal is probably up to something.
</p>

<br/>
<t:bulletin>
    <jsp:attribute name="title">
        What to do next?
    </jsp:attribute>
    <jsp:attribute name="body">
        <h4>Are You from a world without wizards?</h4>
        <div>
            <ul>
                <li>You can return to the <a href="/">main page</a></li>
                <li>If You are not logged in, try to <a href="/auth/login">log in</a></li>
                <li>Check your credentials</li>
                <li>You may not have sufficient rights to access the page</li>
            </ul>
        </div>
        <h4>Are You from Middle-Earth?</h4>
        <div>
            <ul>
                <li><i>Pedo mellon a minno!</i> (<small>Say 'friend' and enter</small>)</li>
                <li>Call the eagles to carry you wherever you want to go</li>
            </ul>
        </div>
    </jsp:attribute>
</t:bulletin>

<br/>
<small>
    The Surreal-Travel does not own rights for the image material on this page.<br/>
    The content from <i>The Lord of the Rings: The Fellowship of the Ring</i> is owned by Tolkien Enterprises,
    the footage by New Line Cinema.
</small>