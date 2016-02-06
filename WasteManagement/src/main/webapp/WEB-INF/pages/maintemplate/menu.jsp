<!--BEGIN SIDEBAR MENU-->
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@include file="/WEB-INF/pages/util/common-properties.jsp"%>


<nav id="sidebar" role="navigation" data-step="2"
	data-intro="Template has &lt;b&gt;many navigation styles&lt;/b&gt;"
	data-position="right" class="navbar-default navbar-static-side"
	style="min-height: 100%">
	<div class="sidebar-collapse menu-scroll">
		<ul id="side-menu" class="nav">

			<div class="clearfix"></div>
			<li class="active"><a href="${pageContext.request.contextPath}/dash"><i
					class="fa fa-tachometer fa-fw">
						<div class="icon-bg bg-orange"></div>
				</i><span class="menu-title">Dashboard</span></a></li>
			<li><a href="${pageContext.request.contextPath}/user/services"><i
					class="fa fa-database fa-fw">
						<div class="icon-bg bg-green"></div>
				</i><span class="menu-title">Payment Services</span></a></li>
			<li><a href="Layout.html"><i class="fa fa-clock-o fa-fw">
						<div class="icon-bg bg-pink"></div>
				</i><span class="menu-title">Pending Notifications</span></a></li>
			<li><a href="${pageContext.request.contextPath}/admin/usersetup"><i class="fa fa-edit fa-fw">
						<div class="icon-bg bg-violet"></div>
				</i><span class="menu-title">Manage Users</span></a></li>
		</ul>
	</div>
</nav>
<!--END SIDEBAR MENU-->