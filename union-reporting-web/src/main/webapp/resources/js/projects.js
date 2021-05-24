$('.list-group-item').click(function(){
    var sendUrl = this.href.replace("allTests", "allTests/ajax");
    $.ajax({url: String(sendUrl),
        type: 'GET',
        global: false,
        async: false,
        success: function(result){
            sessionStorage.setItem('prev', result);
        }
    });
    return true;
});