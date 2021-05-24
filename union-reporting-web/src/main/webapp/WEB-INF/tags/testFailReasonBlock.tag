<%@ tag description="Fail reason combo box for test" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="failReasons" required="true" rtexprvalue="true" description="List with FaileReason objects" type="java.util.List" %>
<%@ attribute name="test" required="true" rtexprvalue="true" description="Test object" type="com.a1qa.model.domain.Test" %>
<div class="panel panel-default fail-reason-block">
    <div class="panel-heading">Fail Reason Info</div>
        <div class="list-group">
            <div class="list-group-item">
                <h4 class="list-group-item-heading">Set fail reason</h4>
                <p class="list-group-item-text"><tags:testFailReasonComboBox test="${test}" index="0" failReasons="${failReasons}" /></p>
            </div>
<c:if test="${not empty test.getFailReasonToTest()}">
            <div class="list-group-item">
                <h4 class="list-group-item-heading">Current fail reason</h4>
                <p class="list-group-item-text">Fail reason: ${test.getFailReasonToTest().getFailReason().getName()}</p>
            <c:if test="${not empty test.getFailReasonToTest().getComment()}">
                <p class="list-group-item-text">Comment: ${test.getFailReasonToTest().getComment()}</p>
            </c:if>
            </div>
</c:if>
        </div>
    </div>
</div>
<!-- Processing testFailReasonComboBox.tag (placed out of tag to avoid duplicating JS code) -->
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
            var selectedValueParam = $('option:selected', this).val();
            failReasonSelectElem = $(this);
            failReasonOptionElem = $('option:selected', this);
            new Messi('Are you sure to change test fail reason to "' + $('option:selected', this).text() + '"?', {
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
                            url: "test/setFailReason",
                            data: {
                                testId: ${test.getId()},
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
                                    if ($('.fail-reason-block .list-group-item:nth-of-type(2)').length) {
                                        $('.fail-reason-block .list-group-item:nth-of-type(2)').remove();
                                    }
                                    if (failReasonOptionElem.val() != "-1") {
                                        $('.fail-reason-block > .list-group').append('<div class="list-group-item"><h4 class="list-group-item-heading">Current fail reason</h4><p class="list-group-item-text">Fail reason: ' + failReasonOptionElem.text() + '</p>');
                                        if (comment.length) {
                                            $('.fail-reason-block .list-group-item:nth-of-type(2)').append('<p class="list-group-item-text">Comment: ' + comment + '</p>');
                                        }
                                        $('.fail-reason-block .list-group-item:nth-of-type(2)').append('</div>');
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