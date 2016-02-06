<!-- Static navbar -->
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<div class="navbar navbar-default navbar-fixed-top"
	data-role="navigation">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="${pageContext.request.contextPath}">${productName}</a>
	</div>
	<div class="navbar-collapse collapse">
		<ul class="nav navbar-nav">
			<li class="home"><a href="${pageContext.request.contextPath}">
					<span class="glyphicon glyphicon-home"></span> Dash Board
			</a></li>
		</ul>
	</div>
	<!--/.nav-collapse -->
</div>
<script type="text/javascript">
	$(function() {
		try {
			var activeMenu = '${requestScope['
			javax.servlet.forward.servlet_path
			']}';
			$("li." + activeMenu.replace(/\//g, '')).addClass('active');
		} catch (err) {
		}
	});
</script>