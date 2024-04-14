<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home</title>
</head>
<body>
<h2>Upload a File</h2>
<form action="/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="file" id="file">
    <button type="submit">Upload</button>
</form>
</body>
</html>