<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:include page="header.jsp" />
        <div class="col-md-8">
            <div class="panel panel-default">
                <div class="panel-heading">Logs</div>
    <c:choose>
        <c:when test="${not empty testInfo.getLogs() and not testInfo.allLogsAreEmpty()}">
                <table class="table">
                <c:forEach items="${testInfo.getLogs()}" var="log">
                    <tr>
                        <td>${log.getContent()}</td>
                    </tr>
                </c:forEach>
                </table>
        </c:when>
        <c:otherwise>
                <div class="panel-body">
                    <div class="alert alert-success">
                        <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
                        <span class="sr-only">Info:</span>
                        Test did not produced any logs
                    </div>
                </div>
        </c:otherwise>
    </c:choose>
            </div>
            <div class="panel panel-default">
                <div class="panel-heading">Attachments</div>
    <c:choose>
        <c:when test="${not empty testInfo.getAttachments()}">
                <table class="table">
                <tr>
                    <th>Attachment</th>
                    <th>Attachment type</th>
                </tr>
                <c:forEach items="${testInfo.getAttachments()}" var="attachment">
                <tr>
                    <td>
            <c:choose>
                <c:when test="${fn:contains(attachment.getContentType(), 'image')}">
                        <a href="data:${attachment.getContentType()};base64,${attachment.toBase64()}" target="_blank">
                            <img class="thumbnail" src="data:${attachment.getContentType()};base64,${attachment.toBase64()}" />
                        </a>
                </c:when>
                <c:otherwise>
                    <a href="data:${attachment.getContentType()};base64,${attachment.toBase64()}" target="_blank">Text attachment</a>
                </c:otherwise>
            </c:choose>
                    </td>
                    <td>${attachment.getContentType()}</td>
                </tr>
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
                <div class="panel-body">
                    <div class="alert alert-success">
                        <span class="glyphicon glyphicon-info-sign" aria-hidden="true"></span>
                        <span class="sr-only">Info:</span>
                        There is no test attachments exists
                    </div>
                </div>
        </c:otherwise>
    </c:choose>
            </div>
        </div>
        <div class="col-md-4">
            <tags:testInfoBlock test="${testInfo}" />
            <tags:testFailReasonBlock test="${testInfo}" failReasons="${failReasons}" />
        </div>
<jsp:include page="footer.jsp" />
