<%@ tag description="Total progress for test from it history collection" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag import="java.lang.Math" %>
<%@ attribute name="testList" required="true" rtexprvalue="true" description="Test object" type="java.util.List" %>
<c:set var="passed" value="0" />
<c:set var="failed" value="0" />
<c:set var="skipped" value="0" />
<c:set var="inProgress" value="0" />
<c:set var="total" value="0" />
<c:forEach items="${testList}" var="test">
    <c:choose>
        <c:when test="${test.getStatus().getName() == 'PASSED'}">
            <c:set var="passed" value="${passed + 1}" />
        </c:when>
        <c:when test="${test.getStatus().getName() == 'FAILED'}">
            <c:set var="failed" value="${failed + 1}" />
        </c:when>
        <c:when test="${test.getStatus().getName() == 'SKIPPED'}">
            <c:set var="skipped" value="${skipped + 1}" />
        </c:when>
        <c:otherwise>
            <c:set var="inProgress" value="${inProgress + 1}" />
        </c:otherwise>
    </c:choose>
    <c:set var="total" value="${total + 1}" />
</c:forEach>
<c:set var="passedPc" value="${Math.round((passed * 100) / total)}" />
<c:set var="failedPc" value="${Math.round((failed * 100) / total)}" />
<c:set var="skippedPc" value="${Math.round((skipped * 100) / total)}" />
<c:set var="inProgressPc" value="${Math.round((inProgress * 100) / total)}" />
<div class="panel panel-default">
    <div class="panel-heading">Overall progress (runs: ${total})</div>
    <div class="panel-body">
        <div class="progress">
            <c:if test="${passedPc > 0}">
                <div class="progress-bar progress-bar-success progress-bar-striped" style="width: ${passedPc}%">
                    ${passedPc}% (${passed})
                </div>
            </c:if>
            <c:if test="${failedPc > 0}">
                <div class="progress-bar progress-bar-danger progress-bar-striped" style="width: ${failedPc}%">
                    ${failedPc}% (${failed})
                </div>
            </c:if>
            <c:if test="${skippedPc > 0}">
                <div class="progress-bar progress-bar-warning progress-bar-striped" style="width: ${skippedPc}%">
                    ${skippedPc}% (${skipped})
                </div>
            </c:if>
            <c:if test="${inProgressPc > 0}">
                <div class="progress-bar progress-bar-striped" style="width: ${inProgressPc}%">
                    ${inProgressPc}% (${inProgress})
                </div>
            </c:if>
        </div>
    </div>
</div>