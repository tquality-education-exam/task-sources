var successAlert = $('#success');
var errorAlert = $('#error');

var id = Math.floor(Math.random() * 10000000000);

$(document).ready(function() {
    $('#saveTest').attr("id", id);
    $('#' + id).click(function () {

        var formData = new FormData();
        formData.append('testName', $('#testName').val());
        formData.append('status', $('#testStatus').val());
        formData.append('testMethod', $('#testMethod').val());
        formData.append('startTime', $('#startTime').val());
        formData.append('endTime', $('#endTime').val());
        formData.append('environment', $('#environment').val());
        formData.append('browser', $('#browser').val() );
        formData.append('log', $('#log').val() );
        formData.append('projectId', $('#projectId').val() );
        formData.append('attachment', $('#attachment') [0].files[0]);

        $.ajax({
            url: 'allTests',
            data: formData,
            processData: false,
            contentType: false,
            type: 'POST',
            success: function ( data ) {
                successAlert.text("Test " +  $('#testName').val() + " saved");
                errorAlert.hide();
                successAlert.show();
            },
            error: function ( data ) {
                errorAlert.text("Test " +  $('#testName').val() + " not saved");
                errorAlert.show();
                successAlert.hide();
            }
        });

    });
});



$('#addTest').on('show.bs.modal', function() {
    $('#addTestForm')[0].reset()
    successAlert.hide();
    errorAlert.hide();
});
