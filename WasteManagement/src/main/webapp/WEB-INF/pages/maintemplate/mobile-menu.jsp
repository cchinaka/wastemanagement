<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@include file="/WEB-INF/pages/util/common-properties.jsp"%>
<div id="menu-drawer" class="drawer dw-xs-10 dw-sm-6 dw-md-4 fold in"
	aria-labelledby="menu-drawer hidden">
	<div class="drawer-controls">
		<a href="#menu-drawer" data-toggle="drawer" aria-foldedopen="false"
			aria-controls="menu-drawer"
			class="btn drawer-menu-link hidden btn-drawer-close"> <i
			class="fa fa-bars"></i>
		</a>

	</div>
	<div class="drawer-contents">
		<div class="drawer-heading">
			<h2 class="drawer-title">
				<a href='#' class="drawer-menu-link">Menu</a>
			</h2>
		</div>

		<ul class="drawer-fullnav">
			<li role="presentation" class="home active"><a
				href="${pageContext.request.contextPath}/dash"> <span
					class="glyphicon glyphicon-home"></span> Home
			</a></li>


			<security:authorize
				access="hasAnyRole('ROLE_ADMIN', 'ROLE_VIEWONLY','ROLE_ENCODER')">
				<li class="dropdown userissueslist userissues"><a href="#"><i
						class='fa fa-cogs'></i> Administration</a>
					<ul class="drawer-submenu">
						<li role="presentation" class="menu-item "><a href="#"><i
								class='fa fa-edit'></i> Setup & Config</a>

							<ul class="drawer-submenu">

								<security:authorize
									access="hasAnyRole('ROLE_ADMIN','ROLE_VIEWONLY')">
									<li role="presentation"><a
										href="${pageContext.request.contextPath}/admin/company"> <i
											class="fa fa-building"></i> Manage Companies
									</a></li>
								</security:authorize>

								<li role="presentation"><a
									href="${pageContext.request.contextPath}/admin/usersetup">
										<i class="fa fa-users"></i> Manage Users
								</a></li>
								<li role="presentation" class="userissueslist"><a
									href="${pageContext.request.contextPath}/admin/meters"> <i
										class="fa fa-tachometer"></i> Manage Meters
								</a></li>
							</ul></li>
						<security:authorize
							access="hasAnyRole('ROLE_ADMIN','ROLE_VIEWONLY','ROLE_ENCODER')">
							<li role="presentation" class="menu-item "><a href="#"><i
									class='fa fa-money'></i> Payments Setup</a>
								<ul class="drawer-submenu">


									<security:authorize
										access="hasAnyRole('ROLE_ADMIN','ROLE_VIEWONLY')">
										<li role="presentation"><a
											href="${pageContext.request.contextPath}/admin/billtypes">
												<i class="fa fa-road"></i> Manage Bill Type Definitions
										</a></li>
										<li role="presentation"><a
											href="${pageContext.request.contextPath}/admin/collectionlimit">
												<i class="fa fa-money"></i> Manage Collection Limits
										</a></li>
									</security:authorize>
									<li role="presentation"><a
										href="${pageContext.request.contextPath}/admin/tariffs"> <i
											class="fa fa-calculator"></i> Manage Tariff Indices
									</a></li>
									<li role="presentation"><a
										href="${pageContext.request.contextPath}/admin/payable"> <i
											class="fa fa-money"></i> Manage Deductions &amp; Refunds
									</a></li>
									<security:authorize
										access="hasAnyRole('ROLE_ADMIN','ROLE_VIEWONLY')">
										<security:authorize access="hasAnyRole('ROLE_ADMIN')">
											<li role="presentation"><a
												href="${pageContext.request.contextPath}/admin/scratchcard">
													<i class="fa fa-money"></i> Manage Scratch Off Vouchers
											</a></li>
										</security:authorize>

										<li role="presentation" data-role="presentation"
											class="divider"></li>
										<li role="presentation" data-role="presentation"
											class="dropdown-header hidden">Work Flow</li>
										<li role="presentation" class="hidden"><a
											href="${pageContext.request.contextPath}/admin/groups"> <i
												class="fa fa-users"></i> Manage User Groups
										</a></li>
										<li role="presentation" class="hidden"><a
											href="${pageContext.request.contextPath}/admin/workflows">
												<i class="fa fa-road"></i> Manage Workflows
										</a></li>
									</security:authorize>
								</ul></li>
						</security:authorize>
						<security:authorize access="hasAnyRole('ROLE_ADMIN')">
							<li role="presentation" class="menu-item "><a href="#"><i
									class='fa fa-link'></i> Integration</a>
								<ul class="drawer-submenu">
									<li role="presentation"><a
										href="${pageContext.request.contextPath}/user/apiconnectorinfo">
											<i class="fa fa-cog"></i> Manage API Connector Info
									</a></li>
									<li role="presentation"><a
										href="${pageContext.request.contextPath}/user/apiconnectorterminal">
											<i class="fa fa-cog"></i> Manage Payment Terminal Info
									</a></li>
								</ul></li>
						</security:authorize>

					</ul></li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_VIEWONLY','ROLE_ENCODER')">
				<li role="presentation"><a
					href="${pageContext.request.contextPath}/user/dataload/request">
						<i class="fa fa-cloud-upload"></i> Data Load
				</a></li>
			</security:authorize>

			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_VIEWONLY','ROLE_COMPANYADMIN','ROLE_COLLECTINGAGENT')">
				<li role="presentation" class="userissueslist userissues"><a
					href="${pageContext.request.contextPath}/admin/orders"> <i
						class="fa fa-shopping-cart"></i> Manage Payments
				</a></li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_VIEWONLY','ROLE_COMPANYADMIN')">
				<li role="presentation" class="dropdown userissueslist userissues"><a
					href="#"><i class='fa fa-line-chart'></i> Reports</a>
					<ul class="drawer-submenu">
						</a>
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/user/orderpayable/deduction">
								<i class="fa fa-money"></i> Deductions Report
						</a></li>
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/user/orderpayable/refund">
								<i class="fa fa-money"></i> Refund Report
						</a></li>
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/user/meterhistory">
								<i class="fa fa-clock-o"></i> Meter History
						</a></li>
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/user/payments/settlement">
								<i class="fa fa-money"></i> Settlement Report
						</a></li>
					</ul></li>
			</security:authorize>
			<security:authorize
				access="hasAnyRole('ROLE_ADMIN','ROLE_VIEWONLY','ROLE_COMPANYADMIN')">
				<li role="presentation" class="dropdown userissueslist userissues"><a
					href="#"><i class='fa fa-envelope-o'></i> Messaging</a>
					<ul class="drawer-submenu">
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/admin/inboundsms"> <i
								class="glyphicon glyphicon-download"></i> In Bound SMS
						</a></li>
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/admin/outboundsms">
								<i class="glyphicon glyphicon-upload"></i> Out Bound SMS
						</a></li>
					</ul></li>
			</security:authorize>
			<security:authorize access="hasRole('ROLE_USER')">
				<c:if test="${userOnly}">
					<li role="presentation" class="userissueslist userissues"><a
						href="${pageContext.request.contextPath}/user/orders/myorders">
							<i class="fa fa-shopping-cart"></i> Show All My Orders
					</a></li>
				</c:if>
				<li role="presentation"><a href="#"><i class='fa fa-money'></i>
						Pay Bill<b class="caret"></b></a>
					<ul class="drawer-submenu">
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/user/payments/bill/prepaid">
								<i class='fa fa-money'></i> Pay Pre-Paid Electricity Bill
						</a></li>
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/user/payments/bill/postpaid">
								<i class='fa fa-money'></i> Pay Post-Paid Electricity Bill
						</a></li>
					</ul></li>
				<li role="presentation"><a href="#"><i class='fa fa-ticket'></i>
						Support Tickets <span class='badge hidden menu-unclosed-tickets'></span>
						<b class="caret"></b></a>
					<ul class="drawer-submenu">
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/user/tickets"><i
								class='fa fa-cog'></i>Manage Tickets</a></li>
						<li role="presentation"><a
							href="${pageContext.request.contextPath}/user/tickets/0"><i
								class='fa fa-plus'></i> Create A Ticket</a></li>
					</ul></li>
			</security:authorize>
		</ul>

		<div class="drawer-footer">
			<small></small>
		</div>
	</div>
</div>
<script type="application/javascript">
	
	
	
	
$(function(){
	$('#menu-drawer').drawer({
	    toggle: false
	});
	$(".drawer-menu-link").click(function(e){
		e.preventDefault();
		log("attempting to close...");
		//$("#menu-drawer").removeClass("open in").addClass("folding");//.removeAttr("style");
		
		$("#menu-drawer").css("margin-left", "-83.3333%");
		$(".btn-drawer").removeClass("hidden");
		$(".drawer-menu-link").addClass("hidden");
	});
});




</script>