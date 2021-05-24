<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:include page="header.jsp" />
                    <div class="panel panel-default">
                        <div class="panel-heading">Running tests</div>
                            <c:choose>
                                <c:when test="${empty tests}">
                        <div class="panel-body">
                            <div class="alert alert-danger">
                                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                <span class="sr-only">Error:</span>
                                Sorry, there is no tests found :(
                            </div>
                        </div>
                                </c:when>
                                <c:otherwise>
                        <table class="table">
                            <tr>
                                <th width="25%">Test name</th>
                                <th width="25%">Test method</th>
                                <th width="10%">Test result</th>
                                <th width="10%">Test start time</th>
                                <th width="10%">Test end time</th>
                                <th width="10%">Test duration (H:m:s.S)</th>
                                <th width="10%">History</th>
                            </tr>
                            <c:forEach items="${tests}" var="test">
                            <tr>
                                <td><a href="testInfo?testId=${test.getId()}">${test.getName()}</a></td>
                                <td>${test.getMethodName()}</td>
                                <td><tags:status status="${test.getStatus()}" /></td>
                                <td>${test.getStartTime()}</td>
                                <td>${test.getEndTime()}</td>
                                <td>${test.getDuration()}</td>
                                <td><a href="history?testId=${test.getId()}">Show</a></td>
                            </tr>
                            </c:forEach>
                        </table>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <tags:pagination totalPages="${pTotalPages}" currentPage="${pCurrentPage}" />
<jsp:include page="footer.jsp" />
