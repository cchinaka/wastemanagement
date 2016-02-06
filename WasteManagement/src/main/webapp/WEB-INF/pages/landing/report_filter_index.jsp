<!DOCTYPE html>
<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@include file="/WEB-INF/pages/util/common-properties.jsp" %>

<html lang="en">
<head>
	<%@ include file="/WEB-INF/pages/util/header-styles.jsp" %>
	<%@ include file="/WEB-INF/pages/util/template-scripts.jsp" %>
	<title>${productName} - ${pagetitle}</title>
</head>

<body>
	<div class="container">
		<security:authorize access="isAuthenticated()">
			<tiles:insertAttribute name="menu" />

			<div class="row">
				<div class="col-xs-12">
					<blockquote class="text-info">
						<p>
							<i class="<tiles:getAsString name='title-class' />"></i> ${pagetitle}
						</p>
						<footer>
							<a href="${pageContext.request.contextPath}/">Home</a> /
							
						</footer>
					</blockquote>
				</div>
			</div>
		</security:authorize>
		<tiles:insertAttribute name="messages" />
		<tiles:insertAttribute name="body" />
				
		<input type="hidden" name="reportId" value="${reportId}"/>
		<input type="hidden" name="viewFormat" />	
		<c:if test="${not unknownReport}">
			<tiles:insertAttribute name="body" />
				<div class="col-sm-8 col-sm-offset-4">		
					<button class="btn btn-primary"  type="button">
						<i class="fa fa-file-pdf-o"></i> Download PDF
					</button>
					<button class="btn btn-primary"  type="button">
						<i class="fa fa-file-csv-o"></i> Download PDF
					</button>
					<button class="btn btn-primary"  type="button">
						<i class="fa fa-file-excel-o"></i> Download PDF
					</button>
				</div>

			<script type="text/javascript">
				$(function(){
					$("button").click(function(){
						//$("#topReportExportForm")[0].submit();
					});
				});
			</script>
		</c:if>
		<tiles:insertAttribute name="modal" />
	</div>
	<tiles:insertAttribute name="footer" />
</body>
</html>

