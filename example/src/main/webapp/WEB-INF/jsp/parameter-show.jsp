<!DOCTYPE html>
<html>
<head>
<%@include file="head.jsp"%>
<title>forms</title>
<style type="text/css">
iframe {
	border: 1px solid blue;
	height: 320px;
	width: 100%;
}
</style>
</head>
<body>
  <p>User1:</p>
  <ul>
    <c:if test="${not empty user.username}">
      <li>username=${user.username}</li>
    </c:if>
    <c:if test="${not empty user.title}">
      <li>title=${user.title}</li>
    </c:if>
    <c:if test="${not empty user.firstName}">
      <li>firstName=${user.firstName}</li>
    </c:if>
    <c:if test="${not empty user.lastName}">
      <li>lastName=${user.lastName}</li>
    </c:if>
    <c:if test="${not empty user.tel}">
      <li>tel=${user.tel}</li>
    </c:if>
    <c:if test="${not empty user.income}">
      <li>tel=${user.income}</li>
    </c:if>
    <c:if test="${not empty user.photoUrl}">
      <li><img src="${user.photoUrl}" /></li>
    </c:if>
  </ul>
</body>
</html>