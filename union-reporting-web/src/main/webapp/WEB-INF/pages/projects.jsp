<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="header.jsp" />
            <div class="panel panel-default">
                <div class="panel-heading">Available projects
                    <c:choose>
                        <c:when test="${requestScope.variant.isUseFrameForNewProject()}">
                            <button class="btn btn-xs btn-primary pull-right" data-toggle="modal" data-target="#addProject" data-backdrop="static" data-keyboard="false">
                                +Add
                            </button>
                        </c:when>
                        <c:when test="${requestScope.variant.isUseNewTabForNewProject()}">
                            <a target="_blank" href="addProject" class="btn btn-xs btn-primary pull-right" >
                                +Add
                            </a>
                        </c:when>
                        <c:when test="${requestScope.variant.isUseGeolocationForNewProject()}">
                            <button onclick="geoFindMe()" class="btn btn-xs btn-primary pull-right" >
                                +Add
                            </button>
                        </c:when>
                        <c:when test="${requestScope.variant.isUseAlertForNewProject()}">
                            <button class="btn btn-xs btn-primary pull-right"  data-toggle="modal" data-target="#addProject">
                                +Add
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-xs btn-primary pull-right"  data-toggle="modal" data-target="#addProject">
                                +Add
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>
                    <c:choose>
                        <c:when test="${empty projects}">
                <div class="panel-body">
                    <div class="alert alert-danger">
                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                        <span class="sr-only">Error:</span>
                        Sorry, there is no projects yet :(
                    </div>
                </div>
                        </c:when>
                        <c:otherwise>
                <div class="list-group">
                            <c:forEach items="${projects}" var="project">
                    <a href="allTests?projectId=${project.getId()}" class="list-group-item">${project.getName()}</a>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
<script src="${pageContext.request.contextPath}/resources/js/projects.js"></script>

<c:choose>
    <c:when test="${requestScope.variant.isUseFrameForNewProject()}">
        <jsp:include page="modalFrame.jsp" />
    </c:when>
    <c:when test="${requestScope.variant.isUseGeolocationForNewProject()}">
        <jsp:include page="defaultModal.jsp" />
    </c:when>
    <c:when test="${requestScope.variant.isUseAlertForNewProject()}">
        <jsp:include page="blockedModal.jsp" />
    </c:when>
    <c:otherwise>
        <jsp:include page="defaultModal.jsp" />
    </c:otherwise>
</c:choose>
<jsp:include page="footer.jsp" />
