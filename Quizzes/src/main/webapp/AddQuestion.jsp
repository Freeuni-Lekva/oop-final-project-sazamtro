<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Question</title>
    <link rel="stylesheet" type="text/css" href="/style/addQuestion.css" />

    <script>
        function showRelevantFields() {
            const type = document.getElementById("type").value;

            // Show MC options for MC or MS types
            document.getElementById("mcOptions").style.display = (type === "MC" || type === "MS") ? "block" : "none";

            // Show response answer input for RESPONSE and FILL types (but NOT picture_response)
            document.getElementById("responseAnswer").style.display =
                (type === "RESPONSE" || type === "FILL" || type === "PICTURE_RESPONSE") ? "block" : "none";

            // Show image URL field only for PICTURE_RESPONSE type
            document.getElementById("imageUrlRow").style.display = (type === "PICTURE_RESPONSE") ? "block" : "none";
        }


        function addOptionField() {
            const container = document.getElementById("optionsContainer");
            const index = container.children.length + 1;

            const div = document.createElement("div");
            div.innerHTML = `
            <label for="option${index}">Option ${index}:</label>
            <input type="text" name="option${index}" id="option${index}" required />
            <label for="correct${index}">
                <input type="checkbox" name="correct_options" id="correct${index}" value="${index}" />
                Correct
            </label><br/>
        `;
            container.appendChild(div);
            document.getElementById("numOptions").value = index + 1;
        }

        function updateImagePreview() {
            const url = document.getElementById("image_url").value.trim();
            const preview = document.getElementById("imagePreview");
            if (url) {
                preview.src = url;
                preview.style.display = "block";
            } else {
                preview.style.display = "none";
            }
        }

        function validateForm() {
            const type = document.getElementById("type").value;
            const checkboxes = document.querySelectorAll('input[name="correct_options"]:checked');

            if (type === "MC") {
                if (checkboxes.length !== 1) {
                    alert("Multiple Choice questions must have exactly one correct option.");
                    return false;
                }
            } else if (type === "MS") {
                if (checkboxes.length === 0) {
                    alert("Multi Select questions must have at least one correct option.");
                    return false;
                }
            } else if (type === "FILL" || type === "RESPONSE" || type === "PICTURE_RESPONSE") {
                const answer = document.getElementById("correctAnswer").value.trim();
                if (answer === "") {
                    alert("Please provide the correct answer.");
                    return false;
                }
            }
            return true;
        }


        window.onload = function () {
            showRelevantFields();
            document.getElementById("image_url").addEventListener("input", updateImagePreview);
        };
    </script>

</head>
<body>
<h2>Add a Question</h2>

<form method="post" action="${pageContext.request.contextPath}/add-question" onsubmit="return validateForm()">
    <input type="hidden" name="quizId" value="${param.quizId}" />
    <input type="hidden" name="position" value="${param.position}" />

    <label for="type">Question Type:</label>
    <select name="type" id="type" onchange="showRelevantFields()" required>
        <option value="">--Select--</option>
        <option value="MC">Multiple Choice (1 correct)</option>
        <option value="MS">Multi Select (multiple correct)</option>
        <option value="FILL">Fill in the Blank</option>
        <option value="RESPONSE">Short Response</option>
        <option value="PICTURE_RESPONSE">Picture Response</option>
    </select>
    <br/><br/>

    <label for="prompt">Prompt:</label>
    <textarea name="prompt" id="prompt" rows="3" cols="40" required></textarea>
    <br/><br/>

    <div id="imageUrlRow" style="display: none;">
        <label for="image_url">Image URL:</label>
        <input type="text" name="image_url" id="image_url" />
        <br/><br/>
        <img id="imagePreview" src="" alt="Image Preview" style="max-width: 300px; display: none; border: 1px solid #ccc;" />
        <div id="imageError" style="color: red; display: none;">Could not load image. Check the URL.</div>
        <br/><br/>
    </div>


    <div id="mcOptions" style="display: none;">
        <h4>Answer Options</h4>
        <input type="hidden" id="numOptions" name="numOptions" value="0" />
        <div id="optionsContainer"></div>
        <p id="mcHint" style="font-style: italic; color: gray;"></p>
        <button type="button" onclick="addOptionField()">Add Option</button>
        <br/><br/>
    </div>

    <div id="responseAnswer" style="display: none;">
        <label for="correctAnswer">Correct Answer (Text):</label>
        <input type="text" name="correctAnswer" id="correctAnswer" />
        <br/><br/>
    </div>

    <input type="submit" value="Add Question" />
</form>
</body>
</html>
