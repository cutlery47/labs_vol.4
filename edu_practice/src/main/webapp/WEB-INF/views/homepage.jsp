<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>

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
            vertical-align: top;
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


        input[type="text"] {
            padding: 8px 14px;
            border: 1px solid #ccc;
            border-radius: 4px;
            margin-bottom: 20px;
            width: calc(100% - 30px);
        }

        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4);
        }

        .modal-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
            border-radius: 8px;
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
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
        <th>Updated At</th>
        <th>Deleted At</th>
        <th>Text</th>
        <th>Document Keywords</th>
        <th>Actions</th>
    </tr>
    <tbody>
        <c:forEach var="document" items="${documents}">
            <tr>
                <td>${document.documentId}</td>
                <td>${document.documentName}</td>
                <td>${document.documentAuthor}</td>
                <td>${document.documentUploadedAt}</td>
                <td>${document.documentUpdatedAt}</td>
                <td>${document.documentText}</td>
                <td>${document.documentKeyWords}</td>
                <td class="actions">
                    <a href="/download/${document.documentId}">
                        <button>Download</button>
                    </a>
                    <a href="/delete/${document.documentId}">
                        <button>Delete</button>
                    </a>

                </td>
            </tr>
        </c:forEach>
    </tbody>

</table>

<button id="openModalBtn">Upload Document</button>

<div id="myModal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <h2>Enter Document Details</h2>
        <form id="documentForm" enctype="multipart/form-data" action="http://localhost:8080/upload" method="post">
            <input type="text" id="docName" name="document_name" placeholder="Document Name">
            <input type="text" id="docAuthor" name="document_author" placeholder="Document Author">
            <div class="upload-wrapper">
                <input type="file" id="fileInput" name="raw_document" accept=".pdf">
                <label for="fileInput" class="upload-text">Select File</label>
            </div>
            <button type="submit" class="upload-button">Upload</button>
        </form>
    </div>
</div>

<script>

    const modal = document.getElementById("myModal");

    const btn = document.getElementById("openModalBtn");

    const span = document.getElementsByClassName("close")[0];

    btn.onclick = function() {
        modal.style.display = "block";
    }

    span.onclick = function() {
        modal.style.display = "none";
    }

    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }

    function requestDownloadDocument(id) {
        return fetch("http://localhost:8080/download/" + id,
            {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                }
            }
        ).then(
            response => {return response}
        )
    }

    async function downloadDocument(id) {
        const res = await requestDownloadDocument(id);
        const bytes = (await res.body.getReader().read()).value

        const blob = new Blob([bytes.buffer], {type: "application/json"})
        const url = URL.createObjectURL(blob);

        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', 'file.pdf')

        document.body.append(link);
        link.click();
        document.body.removeChild(link);

        window.URL.revokeObjectURL(url)
    }

    function deleteDocument(id) {
        fetch("http://localhost:8080/delete/" + id,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                }
            }
        ).then(response => {
            if (response.ok) {
                window.location.href = "http://localhost:8080/home"
            }
        }

        )
    }
</script>
</body>
</html>