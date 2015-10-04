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
  <p>User1:
    <ul>
      <li>username=${user1.username}</li>
      <li>title=${user1.title}</li>
      <li>firstName=${user1.firstName}</li>
      <li>lastName=${user1.lastName}</li>
      <li>tel=${user1.tel}</li>
      <li><img src="${user1.photoUrl}" /></li>
    </ul>
  </p>
</body>
</html>