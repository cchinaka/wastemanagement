<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<spring:eval var="version" expression="@configurationManager.getProperty('resource.version','1.0.0')" htmlEscape="true" />
<c:set var="context" value="${pageContext.request.contextPath}" />
<c:set var="root" value="${context}/static" />
<!-- Just for debugging purposes. Don't actually copy this line! -->
<!--[if lt IE 9]><script src="../../docs-assets/js/ie8-responsive-file-warning.js"></script><![endif]-->
<!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
<!-- Bootstrap core JavaScript
    ================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<%--
<script src="${root}/jquery/jquery.js"></script>
<script src="${root}/bootstrap/js/bootstrap.min.js"></script>
<script src="${root}/jquery/jquery.validate.min.js"></script>
<script src="${root}/jquery/additional-methods.min.js"></script>
<script src="${root}/custom/js/clean-blog.min.js"></script>
<script src="${root}/jquery/plugins/pubsub.js"></script>
<script src="${root}/jquery/plugins/hashtable.js"></script>
<script src="${root}/jquery/plugins/jquery.numberformatter-1.2.3.js"></script>
<script src="${root}/custom/js/moneyutil.js"></script>
<script src="${root}/bootstrap/js/plugins/bootstrap-switch/bootstrap-switch.min.js"></script>
<script src="${root}/jquery/plugins/jquery.plugin.min.js"></script> 
<script src="${root}/jquery/plugins/jquery.realperson.min.js"></script>
 --%>
<script src="${root}/custom/js/app-scripts-landing.js?v=${version}"></script>
<script src="${root}/custom/js/pageutil.js?v=${version}"></script>
