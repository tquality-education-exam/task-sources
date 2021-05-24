<%@ tag description="Status badge depends on test status name from DB" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="status" required="true" rtexprvalue="true" description="Status object" type="com.a1qa.model.domain.Status" %>
<c:choose>
    <c:when test="${status.getName() == 'PASSED'}">
        <c:set var="statusClass" value="success" />
        <c:set var="statusName" value="Passed" />
    </c:when>
    <c:when test="${status.getName() == 'SKIPPED'}">
        <c:set var="statusClass" value="warning" />
        <c:set var="statusName" value="Skipped" />
    </c:when>
    <c:when test="${status.getName() == 'FAILED'}">
        <c:set var="statusClass" value="danger" />
        <c:set var="statusName" value="Failed" />
    </c:when>
    <c:otherwise>
        <c:set var="statusClass" value="default" />
        <c:set var="statusName" value="In progress" />
    </c:otherwise>
</c:choose>
<span class="label label-<c:out value="${statusClass}" />"><c:out value="${statusName}" />