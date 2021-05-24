<%@ tag description="Vertical common test info block" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="test" required="true" rtexprvalue="true" description="Test object" type="com.a1qa.model.domain.Test" %>
<div class="panel panel-default">
    <div class="panel-heading">Common info</div>
    <div class="list-group">
        <div class="list-group-item">
            <h4 class="list-group-item-heading">Project name</h4>
            <p class="list-group-item-text">${test.getProject().getName()}</p>
        </div>
        <div class="list-group-item">
            <h4 class="list-group-item-heading">Test name</h4>
            <p class="list-group-item-text">${test.getName()}</p>
        </div>
        <div class="list-group-item">
            <h4 class="list-group-item-heading">Test method name</h4>
            <p class="list-group-item-text">${test.getMethodName()}</p>
        </div>
        <div class="list-group-item">
            <h4 class="list-group-item-heading">Status</h4>
            <p class="list-group-item-text"><tags:status status="${test.getStatus()}" /></p>
        </div>
        <div class="list-group-item">
            <h4 class="list-group-item-heading">Time info</h4>
            <p class="list-group-item-text">Start time: ${test.getStartTime()}</p>
            <p class="list-group-item-text">End time: ${test.getEndTime()}</p>
            <p class="list-group-item-text">Duration (H:m:s.S): ${test.getDuration()}</p>
        </div>
        <div class="list-group-item">
            <h4 class="list-group-item-heading">Environment</h4>
            <p class="list-group-item-text">${test.getEnvName()}</p>
        </div>
<c:choose>
    <c:when test="${not empty testInfo.getBrowser()}">
        <div class="list-group-item">
            <h4 class="list-group-item-heading">Browser</h4>
            <p class="list-group-item-text">${test.getBrowser()}</p>
        </div>
    </c:when>
</c:choose>
<c:choose>
    <c:when test="${not empty testInfo.getAuthor()}">
        <div class="list-group-item">
            <h4 class="list-group-item-heading">Developer info</h4>
            <p class="list-group-item-text">Name: ${test.getAuthor().getName()}</p>
            <p class="list-group-item-text">Login: ${test.getAuthor().getLogin()}</p>
            <p class="list-group-item-text">Email: ${test.getAuthor().getEmail()}</p>
        </div>
    </c:when>
</c:choose>
<c:choose>
    <c:when test="${not empty testInfo.getDevInfo() and testInfo.getDevInfo().getDevTime() != 0}">
        <div class="list-group-item">
            <h4 class="list-group-item-heading">Development info</h4>
            <p class="list-group-item-text">Development time: ${test.getDevInfo().getDevTime()}</p>
        </div>
    </c:when>
</c:choose>
    </div>
</div>