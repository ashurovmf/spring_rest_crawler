<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>User page</title>
</head>
<body>
    <h1>Personal page</h1>

    <c:url value="/logout" var="logoutUrl" />
    	<form action="${logoutUrl}" method="post" id="logoutForm">
    		<input type="hidden" name="${_csrf.parameterName}"
    			value="${_csrf.token}" />
    	</form>
    	<script>
    		function formSubmit() {
    			document.getElementById("logoutForm").submit();
    		}
    	</script>

    	<c:if test="${pageContext.request.userPrincipal.name != null}">
    		<h2>
    			Welcome : ${pageContext.request.userPrincipal.name} | <a
    				href="javascript:formSubmit()"> Logout</a>
    		</h2>
    </c:if>

    <sec:authorize access="isRememberMe()">
    		<h2># This user is login by "Remember Me Cookies".</h2>
    </sec:authorize>

    <sec:authorize access="isFullyAuthenticated()">
    		<h2># This user is login by username / password.</h2>
    </sec:authorize>
</body>
</html>