window.onload = function() {
    var url = window.location.href + '/';
    url = url.replace(/^.*?\w\//, '').replace(/\?.*$/, '').replace(/\/(?=[\/]*$)/g, '');
    history.replaceState("", "", url.charAt(0) == '/' ? url: '/' + url);
};