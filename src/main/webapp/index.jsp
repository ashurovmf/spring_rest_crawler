<html>
<head>
		<title>Sign In</title>
</head>
<body>
    <h2>It works</h2>
    <form name="fb_signin" id="fb_signin" th:action="@{/signin/facebook}" method="POST">
    				<input type="hidden" name="_csrf" th:value="${_csrf.token}"></input>
    				<input type="hidden" name="scope" value="publish_stream,user_photos,offline_access"></input>
    				<button type="submit">Submit</button>
    </form>
</body>
</html>
