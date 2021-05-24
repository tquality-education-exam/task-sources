<%@ tag description="Pagination" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://union-reporting-web/cjf" prefix="cjf" %>
<%@ attribute name="totalPages" required="true" rtexprvalue="true" description="Total pages count" %>
<%@ attribute name="currentPage" required="true" rtexprvalue="true" description="Current page number" %>
<%@ attribute name="uriPart" required="false" rtexprvalue="true"%>
<c:set var="currURI" value="${requestScope['javax.servlet.forward.servlet_path']}?${pageContext.request.queryString}" />
<c:set var="clearedURI" value="${cjf:truncateParams(currURI, 'page')}" />
<c:if test="${not empty uriPart}">
<c:set var="clearedURI" value="${uriPart}" />
</c:if>
<c:set var="startPage" value="${currentPage - 5}" />
<c:set var="endPage" value="${currentPage + 5}" />
<c:if test="${startPage < 1}">
    <c:set var="startPage" value="1" />
</c:if>
<c:if test="${endPage > totalPages}">
    <c:set var="endPage" value="${totalPages}" />
</c:if>
<center>
    <nav>
        <ul class="pagination">
        <c:forEach begin="${startPage}" end="${endPage}" var="idx">
            <c:if test="${idx == startPage}">
                <c:choose>
                    <c:when test="${currentPage == 1}">
                        <li class="disabled"><a href="#" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${clearedURI}page=${currentPage - 1}" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
                    </c:otherwise>
                </c:choose>
                <c:if test="${startPage != 1}">
                    <li><span aria-hidden="true">...</span></li>
                </c:if>
            </c:if>
            <c:choose>
                <c:when test="${idx == currentPage}">
                    <li class="active"><a href="${clearedURI}page=${currentPage}">${currentPage}<span class="sr-only">Current</span></a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${clearedURI}page=${idx}">${idx}</a></li>
                </c:otherwise>
            </c:choose>
            <c:if test="${idx == endPage}">
                <c:if test="${endPage < totalPages}">
                    <li><span aria-hidden="true">...</span></li>
                </c:if>
                <c:choose>
                    <c:when test="${currentPage == idx}">
                        <li class="disabled"><a href="#" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${clearedURI}page=${currentPage + 1}" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </c:forEach>
        </ul>
    </nav>
</center>