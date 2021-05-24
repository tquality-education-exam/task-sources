<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:include page="header.jsp" />
            <ul class="nav nav-tabs">
                <li role="presentation"><a href="allTests?projectId=${projectId}">All running tests (${projectTestsCount})</a></li>
                <li role="presentation" class="active"><a href="sessions?projectId=${projectId}">Session list</a></li>
            </ul>
            <div class="panel panel-default">
                <table class="table">
                    <tr>
                        <th width="10%">Build number</th>
                        <th width="20%">Session started time</th>
                        <th width="20%">Session tests fail reason</th>
                        <th width="10%">Session duration time (H:m:s.S)</th>
                        <th width="8%">Passed tests</th>
                        <th width="8%">Failed tests</th>
                        <th width="8%">Skipped tests</th>
                        <th width="8%">In progress tests</th>
                        <th width="8%">TOTAL tests</th>
                    </tr>
                    <c:forEach items="${sessions}" var="session" varStatus="sessionStatus">
                    <tr>
                        <td>${session.getSession().getBuildNumber()}</td>
                        <td><a href="tests?sid=${session.getSession().getId()}">${session.getSession().getCreatedTime()}</a></td>
                        <td><tags:sessionFailReasonComboBox failReasons="${failReasons}" index="${sessionStatus.index}" session="${session.getSession()}" /></td>
                        <td>${session.getDuration()}</td>
                        <td><span class="label label-success">${session.getPassedCount()}</span></td>
                        <td><span class="label label-danger">${session.getFailedCount()}</span></td>
                        <td><span class="label label-warning">${session.getSkippedCount()}</span></td>
                        <td><span class="label label-default">${session.getInProgressCount()}</span></td>
                        <td><span class="label label-primary">${session.getTotalCount()}</span></td>
                    </tr>
                    </c:forEach>
                </table>
            </div>
            <tags:pagination totalPages="${pTotalPages}" currentPage="${pCurrentPage}" />
<!-- Processing sessionFailReasonComboBox.tag (placed out of tag to avoid duplicating JS code) -->
<script type="text/javascript">
    $(document).ready(function () {
        var failReasonSelectElem;
        var failReasonOptionElem;
        var prevFailReasonOptionValue;
        var comment = "";
        $('.failReason').on("focus", function() {
            prevFailReasonOptionValue = this.value;
        });
        $('.failReason').change(function() {
            var sessIdParam = $(this).attr('session-id');
            var selectedValueParam = $('option:selected', this).val();
            failReasonSelectElem = $(this);
            failReasonOptionElem = $('option:selected', this);
            new Messi('Are you sure to change status for all tests of selected session to "' + $('option:selected', this).text() + '"?', {
                title: 'Confirm',
                modal: true,
                buttons: [
                    {id: 0, label: 'Yes', val: 'Y', class: 'btn-success'},
                    {id: 1, label: 'No', val: 'N'},
                ],
                callback: function(val) {
                    if (val == 'Y') {
                        $.ajax({
                            method: "POST",
                            url: "sessions/setFailReason",
                            data: {
                                sessionId: sessIdParam,
                                failReasonId: selectedValueParam,
                                comment: comment
                            }
                        })
                            .done(function(data) {
                                if (data) {
                                    new Messi('Fail reason was successfully changed',
                                                                    {title: 'Success', titleClass: 'success', modal: true, buttons: [{id: 0, label: 'Close', val: 'X'}]});
                                    if (failReasonOptionElem.attr('unchangeable') == 'true') {
                                        failReasonSelectElem.attr('disabled', 'disabled');
                                        $('.failReason').trigger("chosen:updated");
                                    }
                                } else {
                                    new Messi('Error occured when trying to change test fail reason',
                                        {title: 'Error', titleClass: 'anim error', modal: true, buttons: [{id: 0, label: 'Close', val: 'X'}]});
                                }
                            })
                            .fail(function() {
                                new Messi('Error occured when trying to change test fail reason',
                                    {title: 'Error', titleClass: 'anim error', modal: true, buttons: [{id: 0, label: 'Close', val: 'X'}]});
                            });
                    } else {
                        failReasonOptionElem.removeAttr('selected');
                        $(this).val(prevFailReasonOptionValue);
                        $('.failReason').trigger("chosen:updated");
                    }
                }
            });
            if ($('option:selected', this).attr('unchangeable') == 'true') {
                $('.messi-content').append('<br><font color="red"><b>WARNING! This reason can not be changed to other in feature.</b></font>');
            }
            $('.messi-content').append('<br>Comment:<br><textarea id="failReasonComment"></textarea>');
            $('#failReasonComment').change(function() {
                comment = $(this).val();
            });
        });
    });
</script>
<jsp:include page="footer.jsp" />
