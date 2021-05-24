<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:if test="${requestScope.variant.isUseFrameForNewProject() or requestScope.variant.isUseNewTabForNewProject()}">
<!DOCTYPE html>
<html>
<head>
    <title>Union Reporting Web</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/resources/css/reporting.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/resources/css/chosen.min.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/resources/css/messi.min.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/resources/css/footer.css" rel="stylesheet" type="text/css">
    <link href="${pageContext.request.contextPath}/resources/css/header.css" rel="stylesheet" type="text/css">
    <script src="${pageContext.request.contextPath}/resources/js/jquery-1.11.3.min.js"></script>
</head>
<body>
<div class="container main-container">
</c:if>
    <form id="addProjectForm"
<c:if test="${requestScope.variant.isUseFrameForNewProject() or requestScope.variant.isUseNewTabForNewProject()}">
            action="addProject"
</c:if>
    method="get">
    <div class="form-group">
        <c:if test="${not empty resultMessage}">
            <c:choose>
                <c:when test="${resultMessage.isSuccess()}">
                    <c:choose>
                        <c:when test="${variant.isUseAlertForNewProject()}">
                            <script type="text/javascript">
                                <c:set var="message" value="${resultMessage.getMessage()}"/>
                                var message = "<c:out value="${message}"/>";
                                alert(message);
                            </script>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-success">
                                <c:out value="${resultMessage.getMessage()}"/>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:when test="${not resultMessage.isSuccess()}">
                    <c:choose>
                        <c:when test="${variant.isUseAlertForNewProject()}">
                            <script type="text/javascript">
                                <c:set var="message" value="${resultMessage.getMessage()}"/>
                                var message = "<c:out value="${message}"/>";
                                alert(message);
                            </script>
                        </c:when>
                        <c:otherwise>
                    <div class="alert alert-danger">
                        <c:out value="${resultMessage.getMessage()}"/>
                    </div>
                        </c:otherwise>
                    </c:choose>
                </c:when>
            </c:choose>
        </c:if>
        <label for="projectName">Project Name</label>
        <input type="text" class="form-control" id="projectName" name="projectName" placeholder="Enter Project Name">
        <input type="hidden" name="isSubmitted" value="true">
    </div>
    <button type="submit" class="btn btn-primary">Save
        <c:choose>
        <c:when test="${variant.isGrammarMistakeOnSaveProject()}"><c:out value="Proejct"/>
        </c:when>
            <c:otherwise>
                <c:out value="Project"/>
            </c:otherwise>
        </c:choose>
    </button>
        <button type="button" class="btn btn-default">Cancel</button>
</form>
<c:if test="${requestScope.variant.isUseFrameForNewProject() or requestScope.variant.isUseNewTabForNewProject()}">
</div>

</body>
<!-- some JS imports here -->
<script src="${pageContext.request.contextPath}/resources/js/reporting.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.flot.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.flot.pie.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.flot.symbol.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/chosen.jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/messi.min.js"></script>
</html>
</c:if>