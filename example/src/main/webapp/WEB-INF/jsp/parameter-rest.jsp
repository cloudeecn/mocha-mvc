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
        Hello ${userId}! <br/>
        <form id="form1" method="post" target="frame" enctype="multipart/form-data">
          <div class="form-group">
            <label for="user.username">Username</label>
            <input name="user.username" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="user.title">Title</label>
            <input name="user.title" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="user.firstName">First Name</label>
            <input name="user.firstName" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="user.lastName">Last Name</label>
            <input name="user.lastName" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="user.tel">Tel</label>
            <input name="user.tel" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="user.income">Income</label>
            <input name="user.income" type="text" class="form-control">
          </div>
          <div class="form-group">
            <label for="user.photo">photo</label>
            <input name="user.photo" type="file" class="form-control">
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