<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document Upload</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        .actions {
            display: flex;
            gap: 10px;
        }
        button {
            background-color: #4CAF50;
            color: white;
            padding: 8px 14px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        button:hover {
            background-color: #45a049;
        }
        h2 {
            margin-bottom: 20px;
        }
        input[type="file"] {
            display: none;
        }
        .upload-wrapper {
            position: relative;
            display: inline-block;
            margin-bottom: 20px;
        }
        .upload-button {
            background-color: #4CAF50;
            color: white;
            padding: 8px 14px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .upload-button:hover {
            background-color: #45a049;
        }
        .upload-text {
            padding: 8px 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
            cursor: pointer;
            transition: border-color 0.3s ease;
        }
        .upload-text:hover {
            border-color: #aaa;
        }

        input[type="file"]:focus + .upload-text {
            border-color: #45a049;
        }

        /* Additional styles for text inputs */
        input[type="text"] {
            padding: 8px 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-bottom: 20px;
            width: calc(100% - 30px); /* Adjust width to match other elements */
        }
    </style>
</head>
<body>
<h2>Document List</h2>
<table>
    <tr>
        <th>ID</th>
        <th>Document Name</th>
        <th>Document Author</th>
        <th>Document Keywords</th>
        <th>Updated At</th>
        <th>Deleted At</th>
        <th>Actions</th>
    </tr>

    <tr>
        <td>1</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td class="actions">
            <button onclick=downloadDocument(1)>Download</button>
            <button onclick=deleteDocument(1)>Delete</button>
        </td>
    </tr>

    <tr>
        <td>2</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td class="actions">
            <button onclick=downloadDocument(2)>Download</button>
            <button onclick=deleteDocument(2)>Delete</button>
        </td>
    </tr>

</table>

<form method="post" enctype="multipart/form-data" action="http://localhost:8080/upload">
    <input type="text" name="document_name" placeholder="Document Name">
    <input type="text" name="document_author" placeholder="Document Author">

    <div class="upload-wrapper">
        <input type="file" id="fileInput" name="raw_document" accept=".pdf">
        <label for="fileInput" class="upload-text">Select File</label>
    </div>

    <button type="submit" class="upload-button">Upload</button>
</form>


<script>

    function downloadDocument(id) {
        fetch("http://localhost:8080/download/" + id,
            {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            }
        )
    }

    function deleteDocument(id) {
        fetch("http://localhost:8080/delete/" + id,
            {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                }
            }
        )
    }
</script>
</body>
</html>