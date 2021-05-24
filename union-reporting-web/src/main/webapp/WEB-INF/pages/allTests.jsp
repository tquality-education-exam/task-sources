<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<jsp:include page="header.jsp" />
            <script type="text/javascript">
                var refreshTimeout = 10;
                setInterval(function() {
                    $.ajax({url: "allTests/ajax",
                        type: 'GET',
                        data: { projectId: ${projectId}, page: ${pCurrentPage}},
                        global: false,
                        success: function(result){
                            if ( sessionStorage.getItem("prev") != result && !$('#addTest').hasClass('in')) {
                                sessionStorage.setItem("prev", result);
                                document.write(result);
                                document.close();
                            }
                        }
                    })
                }, refreshTimeout * 1000);
            </script>
            <script type="text/javascript">
                var data = [
                    { label: "Passed",  data: ${passedTestsCount}, color: "#5cb85c"},
                    { label: "Failed",  data: ${failedTestsCount}, color: "#d9534f"},
                    { label: "Skipped",  data: ${skippedTestsCount}, color: "#f0ad4e"},
                    { label: "Unfinished",  data: ${unfinishedTestsCount}, color: "#777"}
                ];

                $(document).ready(function () {
                    $.plot($("#pie"), data, {
                         series: {
                             pie: {
                                 show: true,
                                 label: {
                                     show: true,
                                     radius: 1,
                                     formatter: function(label, series) {
                                         return '<div style="font-size:11px; text-align:center; padding:2px; color:white;">'+label+'<br/>'+series.data[0][1]+' ('+Math.round(series.percent)+'%)</div>';
                                     },
                                     background: {
                                         opacity: 0.8,
                                         color: '#444'
                                     },
                                     threshold: 0.05
                                 }
                             }
                         },
                         legend: {
                            labelBoxBorderColor: "none"
                         }
                    });
                    var totalArr = [];
                    var passedArr = [];
                    var failedArr = [];
                    var skippedArr = [];
                    var unfinishedArr = [];
                    var axisPoints = [];
                <c:forEach items="${chronologyGraph}" var="oneStat" varStatus="graphTestStatus">
                    totalArr.push([${graphTestStatus.index}, ${oneStat.get(StatusEnum.TOTAL).getY()}]);
                    passedArr.push([${graphTestStatus.index}, ${oneStat.get(StatusEnum.PASSED).getY()}]);
                    failedArr.push([${graphTestStatus.index}, ${oneStat.get(StatusEnum.FAILED).getY()}]);
                    skippedArr.push([${graphTestStatus.index}, ${oneStat.get(StatusEnum.SKIPPED).getY()}]);
                    unfinishedArr.push([${graphTestStatus.index}, ${oneStat.get(StatusEnum.UNFINISHED).getY()}]);
                    axisPoints.push([${graphTestStatus.index}, '${oneStat.get(StatusEnum.SKIPPED).getX()}']);
                </c:forEach>
                    var graphData = [
                        { label: "Total",  data: totalArr, points: { symbol: "circle", fillColor: "#000" }, color: "#000"},
                        { label: "Passed",  data: passedArr, points: { symbol: "circle", fillColor: "#5cb85c" }, color: "#5cb85c"},
                        { label: "Failed",  data: failedArr, points: { symbol: "circle", fillColor: "#d9534f" }, color: "#d9534f"},
                        { label: "Skipped",  data: skippedArr, points: { symbol: "circle", fillColor: "#f0ad4e" }, color: "#f0ad4e"},
                        { label: "Unfinished",  data: unfinishedArr, points: { symbol: "circle", fillColor: "#777" }, color: "#777"}
                    ];
                    var graphOptions = {
                        xaxis: { ticks: axisPoints },
                        points: {
                            radius: 3,
                            show: true,
                            fill: true
                        },
                        grid: { hoverable: true },
                        lines: { show: true }
                    };
                    function showTooltip(x, y, contents, z) {
                        $('<div id="flot-tooltip">' + contents + '</div>').css({
                            top: y - 30,
                            left: x - 135,
                            'border-color': z,
                        }).appendTo("body").fadeIn(200);
                    }
                    $.plot("#graph", graphData, graphOptions);
                    $("#graph").bind("plothover", function (event, pos, item) {
                        if (item) {
                            if ((previousPoint != item.dataIndex) || (previousLabel != item.series.label)) {
                                previousPoint = item.dataIndex;
                                previousLabel = item.series.label;

                                $("#flot-tooltip").remove();

                                var x = item.datapoint[0];
                                y = item.datapoint[1];
                                z = item.series.color;

                                showTooltip(item.pageX, item.pageY,
                                    "<b>" + item.series.label + "</b><br /> Count: " + y,
                                    z);
                            }
                        } else {
                            $("#flot-tooltip").remove();
                            previousPoint = null;
                        }
                    });
                });
            </script>
<c:if test="${requestScope.variant.isUseAjaxForTestsPage()}">
    <script>
        $(document).ready( function () {
            $.ajax({url: "allTests/ajax",
                type: 'GET',
                data: { projectId: ${projectId}, page: ${pCurrentPage}},
                success: function(result){
                    document.write(result);
                    document.close();
                }
            })
        });
    </script>
</c:if>
            <div class="panel panel-default">
                <div class="panel-heading">Total tests progress
                    <button class="btn btn-xs btn-primary pull-right"  data-toggle="modal" data-target="#addTest">
                        +Add
                    </button>
                </div>
                <div class="panel-body center">
                    <center>
                        <div id="pie" class="col-md-6"></div>
                        <div id="graph" class="col-md-6"></div>
                    </center>
                </div>
            </div>
            <ul class="nav nav-tabs">
                <li role="presentation" class="active"><a href="allTests?projectId=${projectId}">All running tests (${projectTestsCount})</a></li>
                <li role="presentation"><a href="sessions?projectId=${projectId}">Session list</a></li>
            </ul>
            <div class="panel panel-default">
                <table class="table">
                    <tr>
                        <th width="25%">Test name</th>
                        <th width="25%">Test method</th>
                        <th width="10%">Latest test result</th>
                        <th width="10%">Latest test start time</th>
                        <th width="10%">Latest test end time</th>
                        <th width="10%">Latest test duration (H:m:s.S)</th>
                        <th width="10%">History</th>
                    </tr>
            <c:if test="${!requestScope.variant.isUseAjaxForTestsPage()}">
                <c:choose>
                    <c:when test="${empty tests}">
                        <tr>
                            <td colspan="7">
                                <div class="alert alert-danger">
                                    <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                                    <span class="sr-only">Error:</span>
                                    Sorry, there is no tests yet :(
                                </div>
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
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
                    </c:otherwise>
                </c:choose>
            </c:if>
                </table>
            </div>
            <tags:pagination totalPages="${pTotalPages}" currentPage="${pCurrentPage}"  />
<jsp:include page="addTest.jsp" />
<jsp:include page="footer.jsp" />
