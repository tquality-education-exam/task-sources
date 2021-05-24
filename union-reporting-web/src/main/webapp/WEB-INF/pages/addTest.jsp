<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal fade" id="addTest" tabindex="-1" role="dialog" aria-labelledby="title" aria-hidden="true" >
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="title">Add test</h4>
            </div>
            <div class="modal-body" >
                <div id="success" class="alert alert-success"></div>
                <div id="error" class="alert alert-danger"></div>
                <form id="addTestForm" method="post" action="allTests" enctype="multipart/form-data" class="form">
                    <div class="form-group">
                        <label for="testName">Test Name
                            <span style="color:red;">*</span>
                        </label>
                        <input type="text" class="form-control" id="testName" name="testName" placeholder="Enter Test Name">
                    </div>
                    <div class="form-group">
                        <label for="testStatus">Status
                            <span style="color:red;">*</span>
                        </label>
                        <select name="status" class="form-control" id="testStatus">
                            <c:forEach items="${statuses}" var="status">
                                <option>${status.getName()}</option>>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="testMethod">Test method
                            <span style="color:red;">*</span>
                        </label>
                        <input type="text" class="form-control" id="testMethod" name="testMethod" placeholder="Enter Test Method">
                    </div>
                    <div class="form-group">
                        <label for="startTime">Start Time
                            <span style="color:red;">*</span>
                        </label>
                        <input type="text" class="form-control" id="startTime" name="startTime" pattern="(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})" title="yyyy-mm-dd hh:mm:ss" placeholder="yyyy-mm-dd hh:mm:ss">
                    </div>
                    <div class="form-group">
                        <label for="endTime">End Time
                            <span style="color:red;">*</span>
                        </label>
                        <input type="text" class="form-control" id="endTime" name="endTime" pattern="(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})" title="yyyy-mm-dd hh:mm:ss" placeholder="yyyy-mm-dd hh:mm:ss">
                    </div>
                    <div class="form-group">
                        <label for="environment">Environment
                            <span style="color:red;">*</span>
                        </label>
                        <input type="text" class="form-control" id="environment" name="environment" placeholder="Enter Test Environment">
                    </div>
                    <div class="form-group">
                        <label for="browser">Browser
                            <span style="color:red;">*</span>
                        </label>
                        <input type="text" class="form-control" id="browser" name="browser" placeholder="Enter Browser">
                    </div>
                    <div class="form-group">
                        <label for="log">Log</label>
                        <textarea rows="5" class="form-control" id="log" name="log" placeholder="Log"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="attachment">Select File</label>
                        <input name="attachment" id="attachment" type="file" class="file">
                    </div>
                    <input name="projectId" id="projectId" type="hidden" value="${projectId}">
                    <button id="saveTest" type="button" class="btn btn-primary">Save <c:choose>
                        <c:when test="${requestScope.variant.isGrammarMistakeOnSaveTest()}"><c:out value="Tets"/>
                        </c:when>
                        <c:otherwise>
                            <c:out value="Test"/>
                        </c:otherwise>
                    </c:choose></button>
                    <button type="button" class="btn btn-default">Cancel</button>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="${pageContext.request.contextPath}/resources/js/addTestAjax.js"></script>