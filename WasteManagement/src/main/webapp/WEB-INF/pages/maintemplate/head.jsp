<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@include file="/WEB-INF/pages/util/common-properties.jsp"%>

<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="root" value="${context}/static" />

<!--BEGIN TOPBAR-->
<div id="header-topbar-option-demo" class="page-header-topbar">
	<nav id="topbar" role="navigation" style="margin-bottom: 0;"
		data-step="3" class="navbar navbar-default navbar-static-top">
		<div class="navbar-header">
			<button type="button" data-toggle="collapse"
				data-target=".sidebar-collapse" class="navbar-toggle">
				<span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span
					class="icon-bar"></span><span class="icon-bar"></span>
			</button>
			<a id="logo" href="index.html" class="navbar-brand"><span
				class="fa fa-rocket"></span><span class="logo-text">RIPN
					Receiver</span></a>
		</div>
		<div class="topbar-main">
			<a id="menu-toggle" href="#" class="hidden-xs"><i
				class="fa fa-bars"></i></a>

			<ul class="nav navbar navbar-top-links navbar-right mbn">
				<li class="dropdown"><a data-hover="dropdown" href="#"
					class="dropdown-toggle"><i class="fa fa-bell fa-fw"></i><span
						class="badge badge-green">3</span></a></li>
				<li class="dropdown"><a data-hover="dropdown" href="#"
					class="dropdown-toggle"><i class="fa fa-envelope fa-fw"></i><span
						class="badge badge-orange">7</span></a></li>
				<li class="dropdown"><a data-hover="dropdown" href="#"
					class="dropdown-toggle"><i class="fa fa-tasks fa-fw"></i><span
						class="badge badge-yellow">8</span></a></li>
				<li class="dropdown topbar-user"><a data-hover="dropdown"
					href="#" class="dropdown-toggle"><img
						src="${root}/templates/admin/images/avatar/48.jpg" alt=""
						class="img-responsive img-circle" />&nbsp;<span class="hidden-xs">Robert
							John</span>&nbsp;<span class="caret"></span></a>
					<ul class="dropdown-menu dropdown-user pull-right">
						<li><a href="#"><i class="fa fa-user"></i>My Profile</a></li>
						<li><a href="#"><i class="fa fa-calendar"></i>My Calendar</a></li>
						<li><a href="#"><i class="fa fa-envelope"></i>My Inbox<span
								class="badge badge-danger">3</span></a></li>
						<li><a href="#"><i class="fa fa-tasks"></i>My Tasks<span
								class="badge badge-success">7</span></a></li>
						<li class="divider"></li>
						<li><a href="#"><i class="fa fa-lock"></i>Lock Screen</a></li>
						<li><a href="Login.html"><i class="fa fa-key"></i>Log Out</a></li>
					</ul></li>
				<li id="topbar-chat" class="hidden-xs"><a
					href="javascript:void(0)" data-step="4"
					data-intro="&lt;b&gt;Form chat&lt;/b&gt; keep you connecting with other coworker"
					data-position="left" class="btn-chat"><i class="fa fa-comments"></i><span
						class="badge badge-info">3</span></a></li>
			</ul>
		</div>
	</nav>
</div>
<!--END TOPBAR-->