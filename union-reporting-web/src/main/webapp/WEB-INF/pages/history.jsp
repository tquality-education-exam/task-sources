<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:include page="header.jsp" />
            <div class="col-md-12">
                <tags:overallProgress testList="${tests}" />
            </div>
            <div class="col-md-8">
                <div class="panel panel-default">
                    <div class="panel-heading">Test history</div>
                    <table class="table">
                        <tr>
                            <th width="80%">Run date</th>
                            <th width="20%">Status</th>
                        </tr>
                    <c:forEach items="${tests}" var="test">
                        <tr>
                            <td><a href="testInfo?testId=${test.getId()}">${test.getStartTime()}</a>
                            <td><tags:status status="${test.getStatus()}" /></td>
                        </tr>
                    </c:forEach>
                    </table>
                </div>
            </div>
            <div class="col-md-4">
                <tags:testInfoBlock test="${tests.get(0)}" />
            </div>
<jsp:include page="footer.jsp" />
