<%-- 
    Document   : 403_cs
    Created on : Jan 25, 2015, 11:10:20 AM
    Author     : Roman Lacko [396157]
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<p style="text-align: center">
    <span style="font-size: 200%">
        Bílý Čaroděj má <b><span style="font-size: 150%">403</span></b> důvodů proč Vám nezobrazit tuto stránku.
    </span>
    <br/>
    Ten darebák má zřejmě něco za lubem.
</p>

<br/>
<t:bulletin>
    <jsp:attribute name="title">
        Co dál?
    </jsp:attribute>
    <jsp:attribute name="body">
        <h4>Jste ze světa bez čarodějů?</h4>
        <div>
            <ul>
                <li>Můžete se vrátit na <a href="/">hlavní stránku</a> a zkusit něco jiné</li>
                <li>Pokud nejste přihlášen, zkuste se <a href="/auth/login">přihlásit</a></li>
                <li>Zkontrolujte své přihlašovací údaje</li>
                <li>Možná nemáte dostatečná oprávnění</li>
            </ul>
        </div>
        <h4>Jste ze Středozemi?</h4>
        <div>
            <ul>
                <li><i>Pedo mellon a minno!</i> (<small>Řekni 'přítel' a vejdi</small>)</li>
                <li>Zavolejte orly, ať Vás odnesou kamkoli budete chtít</li>
            </ul>
        </div>
    </jsp:attribute>
</t:bulletin>

<br/>
<small>
    Surreal-Travel není vlastníkem grafického materiálu použitého na této stránce.<br/>
    Obsah z <i>The Lord of the Rings: The Fellowship of the Ring</i> vlastní společnost Tolkien Enterprises,
    záznam vlastní New Line Cinema.
</small>