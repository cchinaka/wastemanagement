<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="root" value="${context}/static" />

<security:authorize access="isAuthenticated()" var="isAuthenticated" />


<section id="portfolio">
	<div class="container">
		<div class="row">
			<div class="col-lg-12 text-center">
				<h2>Portfolio</h2>
				<hr class="star-primary">
			</div>
		</div>
		<div class="row">
			<div class="col-sm-4 portfolio-item">
				<a href="#portfolioModal1" class="portfolio-link"
					data-toggle="modal">
					<div class="caption">
						<div class="caption-content">
							<i class="fa fa-search-plus fa-3x"></i>
						</div>
					</div> <img src="/static/dist/images/cabin.png" class="img-responsive"
					alt="">
				</a>
			</div>
			<div class="col-sm-4 portfolio-item">
				<a href="#portfolioModal2" class="portfolio-link"
					data-toggle="modal">
					<div class="caption">
						<div class="caption-content">
							<i class="fa fa-search-plus fa-3x"></i>
						</div>
					</div> <img src="/static/dist/images/cake.png" class="img-responsive"
					alt="">
				</a>
			</div>
			<div class="col-sm-4 portfolio-item">
				<a href="#portfolioModal3" class="portfolio-link"
					data-toggle="modal">
					<div class="caption">
						<div class="caption-content">
							<i class="fa fa-search-plus fa-3x"></i>
						</div>
					</div> <img src="/static/dist/images/circus.png" class="img-responsive"
					alt="">
				</a>
			</div>
			<div class="col-sm-4 portfolio-item">
				<a href="#portfolioModal4" class="portfolio-link"
					data-toggle="modal">
					<div class="caption">
						<div class="caption-content">
							<i class="fa fa-search-plus fa-3x"></i>
						</div>
					</div> <img src="/static/dist/images/game.png" class="img-responsive"
					alt="">
				</a>
			</div>
			<div class="col-sm-4 portfolio-item">
				<a href="#portfolioModal5" class="portfolio-link"
					data-toggle="modal">
					<div class="caption">
						<div class="caption-content">
							<i class="fa fa-search-plus fa-3x"></i>
						</div>
					</div> <img src="/static/dist/images/safe.png" class="img-responsive"
					alt="">
				</a>
			</div>
			<div class="col-sm-4 portfolio-item">
				<a href="#portfolioModal6" class="portfolio-link"
					data-toggle="modal">
					<div class="caption">
						<div class="caption-content">
							<i class="fa fa-search-plus fa-3x"></i>
						</div>
					</div> <img src="/static/dist/images/submarine.png"
					class="img-responsive" alt="">
				</a>
			</div>
		</div>
	</div>
</section>

<!-- About Section -->
<section class="success" id="about">
	<div class="container">
		<div class="row">
			<div class="col-lg-12 text-center">
				<h2>About RIPN Receiver</h2>
				<hr class="star-light">
			</div>
		</div>
		<div class="row">
			<div class="col-lg-4 col-lg-offset-2">
				<p>
					RIPN Receiver is a Remita Integration Payment Notification Receiver
					developed to sort out notificaiton reception needs of the
					organization. <a href="#">Click here</a> to find out features of
					the oh-so-awesome application!
				</p>
			</div>
			<div class="col-lg-4">
				<p>Remita is an e-Payments and e-Collections solution on a
					single multi-bank platform</p>
				<p>Remita is in use by many individuals, public and private
					sector organisations that process over &#x20A6;500 Billion worth of
					transactions on a monthly basis.</p>
			</div>
			<div class="col-lg-8 col-lg-offset-2 text-center">
				<a href="#" class="btn btn-lg btn-outline"> <i
					class="fa fa-download"></i> Download Theme
				</a>
			</div>
		</div>
	</div>
</section>
