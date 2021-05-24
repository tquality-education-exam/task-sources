<%@ tag description="Fail reason combo box for test" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="failReasons" required="true" rtexprvalue="true" description="List with FaileReason objects" type="java.util.List" %>
<%@ attribute name="index" required="true" rtexprvalue="true" description="List with FaileReason objects" type="java.lang.Integer" %>
<%@ attribute name="test" required="true" rtexprvalue="true" description="Test object" type="com.a1qa.model.domain.Test" %>
<select id="failReason${index}" class="failReason" session-id="${session.getId()}">
            <option value="-1" unchangeable="false">None</option>
<c:forEach items="${failReasons}" var="reason">
    <c:choose>
        <c:when test="${test.getFailReasonToTest().getFailReason().getName() == reason.getName()}">
            <option value="${reason.getId()}" selected="selected" unchangeable="${reason.isUnchangeable()}">${reason.getName()}</option>
        </c:when>
        <c:otherwise>
            <option value="${reason.getId()}" unchangeable="${reason.isUnchangeable()}">${reason.getName()}</option>
        </c:otherwise>
    </c:choose>
</c:forEach>
</select>
<script type="text/javascript">
    $(document).ready(function () {
        $('#failReason${index}').chosen();
        if ('${test.getFailReasonToTest().getFailReason().isUnchangeable()}' == 'true') {
            $('#failReason${index}').prop('disabled', true).trigger("chosen:updated");
        }
    });
</script>