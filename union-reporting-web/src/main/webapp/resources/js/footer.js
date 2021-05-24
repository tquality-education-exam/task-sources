var minWaitSeconds = 1;
var maxWaitSeconds = 15;
setInterval(function() {
    var footerTextElement = $('.footer-text');
    var footerHtml = footerTextElement.html();
    if (footerHtml.indexOf('span') !== -1) {
        footerHtml = footerHtml.replace('span', 'b');
    }
    else {
        footerHtml = footerHtml.replace('b', 'span');
    }
    footerTextElement.html(footerHtml);
}, Math.floor(minWaitSeconds + Math.random() * (maxWaitSeconds + 1 - minWaitSeconds)) * 1000);