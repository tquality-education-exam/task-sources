<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.a1qa.model.domain.Variant" %>
<%@ page pageEncoding="UTF-8" %>
        </div>
        <footer class="footer">
            <div class="container">
                <p class="text-muted text-center footer-text">Reporting test portal. <span>Version: <%=((Variant)request.getAttribute("variant")).getId()%></span></p>
            </div>
        </footer>
    </body>
    <!-- some JS imports here -->
    <script src="${pageContext.request.contextPath}/resources/js/reporting.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery.flot.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery.flot.pie.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery.flot.symbol.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/chosen.jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/messi.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/reloadPage.js"></script>
    <c:if test="${requestScope.variant.isDynamicVersionInFooter()}">
        <script src="${pageContext.request.contextPath}/resources/js/footer.js"></script>
    </c:if>
    <c:if test="${requestScope.variant.isUseFrameForNewProject() or requestScope.variant.isUseAlertForNewProject()}">
        <script src="${pageContext.request.contextPath}/resources/js/closeModal.js"></script>
    </c:if>
    <c:if test="${requestScope.variant.isUseGeolocationForNewProject()}">
        <script src="${pageContext.request.contextPath}/resources/js/geolocation.js"></script>
    </c:if>
    <c:if test="${not requestScope.variant.isUseFrameForNewProject()}">
        <c:if test="${isSubmitted}">
            <script src="${pageContext.request.contextPath}/resources/js/reopenModal.js"></script>
        </c:if>
    </c:if>
</html>