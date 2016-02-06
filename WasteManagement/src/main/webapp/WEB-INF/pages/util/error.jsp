<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="alert alert-danger">
	<h1>Error Page</h1>
	A Problem was encountered while processing your request.
</div>
<%--
    Failed URL: ${url}
    Exception:  ${exception.message}
    <c:forEach items="${exception.stackTrace}" var="ste">
    	${ste}<br/>
   	</c:forEach>
--%>