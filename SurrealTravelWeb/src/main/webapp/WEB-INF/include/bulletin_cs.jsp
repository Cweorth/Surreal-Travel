<%--
    Document   : bulletin_cs
    Author     : Jan Klimeš
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:bulletin>
    <jsp:attribute name="title">
        <f:message key="basic.introduction.board"/>
    </jsp:attribute>
    <jsp:attribute name="body">
        <h4>Cestovní kancelář Surreal Travel přijímá nové řidiče autobusů!</h4>
        <div>
            <p>Na interview si přineste nejvýše 1 den starý test na drogy. Řidičský průkaz výhodou (pokud nemáte, nevadí, můžete být pilotem/kou*!)</p>
            <p>* pilotní průkaz na létající oře nazgulů je možné získat v našem školícím centru; obří orly jsme byli nuceni odstavit, protože jsou prý "ohrožení" a "na pokraji vyhynutí". Děkujeme hnutí Greenpeace.</p>
        </div>
        <h4>Výkonný ředitel společnosti Surreal-Travel zahynul při tragické nehodě</h4>
        <div>
            <p>S velkým smutkem v našich srdcích jsme nuceni vám oznámit, že zakladatel naší společnosti, Olo Bramble of Willowbottom, zemřel při pokusu pokořit kbelíkovou výzvu. Obsahem kbelíku
                se ukázal být velmi agresivní <em>morgulský dryák</em>, který rozpustil pana Willowbottoma na mastný flek.</p>
            <p>Uctěte jeho vzpomínku zakoupením některého z našich zájezdů do zemí fantasy! Prvních dvacet truchlících získá 10% slevu*!</p>
            <p style="font-size: 8px;">* sleva je uplatnitelná pouze pokud jste hobbití rasy a navštívíte pohřeb p. Willobottma (písemné stvrzenky bude vydávat notář hned vedle rakve)</p>
        </div>
        <h4>Zimní akce - jízdenka do neznáma</h4>
        <div>
            <p>To pravé pro opravdové dobrodruhy! Zakupte si speciální zájezd <strong>Wherever the wind takes me</strong> za akční cenu 9 999,- a my vám náhodně vybereme destinaci!
            Cíl zájezdu vám neoznámíme před odjezdem - zjistíte to až na místě. V rámci zachování překvapení po vás může steward/ka požadovat, abyste během cesty přes oči neprůhlednou pásku.</p>
            <p>Tak nepřemýšlejte (opravdu, radši to nezkoušejte) a vrhněte se s námi do neznáma.</p>
            <p style="font-size: 8px;">Cena neobsahuje zpáteční jízdenku.</p>
        </div>
    </jsp:attribute>
</t:bulletin>
