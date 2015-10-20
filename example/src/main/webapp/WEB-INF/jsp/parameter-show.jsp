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
    <c:if test="${not empty user1.username}">
      <li>username=${user1.username}</li>
    </c:if>
    <c:if test="${not empty user1.title}">
      <li>title=${user1.title}</li>
    </c:if>
    <c:if test="${not empty user1.firstName}">
      <li>firstName=${user1.firstName}</li>
    </c:if>
    <c:if test="${not empty user1.lastName}">
      <li>lastName=${user1.lastName}</li>
    </c:if>
    <c:if test="${not empty user1.tel}">
      <li>tel=${user1.tel}</li>
    </c:if>
    <c:if test="${not empty user1.income}">
      <li>tel=${user1.income}</li>
    </c:if>
    <c:if test="${not empty user1.photoUrl}">
      <li><img src="${user1.photoUrl}" /></li>
    </c:if>
  </ul>
</body>
</html>