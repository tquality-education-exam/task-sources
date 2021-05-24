// for some common logic
    $.ajaxSetup({'global':true});
    var ajaxWaiter;
    $(document).ajaxStart(function () {
        ajaxWaiter = new Messi('Please, wait...', { modal: true, closeButton: false });
        $('.messi-content').prepend('<img src="resources/img/ajax-loader.gif">');
    });

    $(document).ajaxStop(function () {
        ajaxWaiter.hide();
    });