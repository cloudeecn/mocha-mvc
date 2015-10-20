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
  <div class="container">
    <div class="row">
      <div class="col-xs-12 col-md-6">
        <form id="form1" action="parameter/show" method="post" target="frame" enctype="multipart/form-data">
          <div class="form-group">
            <label for="user1.username">Username</label>
            <input name="user1.username" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="user1.title">Title</label>
            <input name="user1.title" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="user1.firstName">First Name</label>
            <input name="user1.firstName" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="user1.lastName">Last Name</label>
            <input name="user1.lastName" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="user1.tel">Tel</label>
            <input name="user1.tel" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="user1.income">Income</label>
            <input name="user1.income" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="user1.photo">photo</label>
            <input name="user1.photo" type="file" class="form-control">
          </div>
          <button class="btn btn-primary" type="submit">Submit</button>
        </form>
      </div>
      
      <div class="col-xs-12 col-md-6">
        <iframe id="frame" name="frame"></iframe>
      </div>
    </div>
    
  </div>
</body>
</html>