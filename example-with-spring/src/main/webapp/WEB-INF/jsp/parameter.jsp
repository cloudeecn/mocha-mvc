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
        <form action="parameter/show" method="post" target="frame">
          <div class="form-group">
            <label for="username">Username</label>
            <input name="username" type="text" class="form-control" placeholder="Username">
          </div>
          <div class="form-group">
            <label for="title">Title</label>
            <input name="title" type="text" class="form-control" placeholder="Title">
          </div>
          <div class="form-group">
            <label for="firstName">First Name</label>
            <input name="firstName" type="text" class="form-control" placeholder="First Name">
          </div>
          <div class="form-group">
            <label for="lastName">Last Name</label>
            <input name="lastName" type="text" class="form-control" placeholder="Last Name">
          </div>
          <div class="form-group">
            <label for="tel">Tel</label>
            <input name="tel" type="text" class="form-control" placeholder="Tel">
          </div>
          <div class="form-group">
            <label for="photo">photo</label>
            <input name="photo" type="file" class="form-control">
          </div>
          <button class="btn btn-primary" type="submit">Submit</button>
        </form>
      </div>
      
      <div class="col-xs-12 col-md-6">
        <iframe id="frame" name="frame"></iframe>
      </div>
    </div>
    
  </div>
  <script src="//code.jquery.com/jquery-2.1.4.min.js"></script>
  <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</body>
</html>