<%@ page import="bean.Questions.QuestionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Question</title>
    <link rel="stylesheet" type="text/css" href="/style/addQuestion.css" />

    <script>
        function showRelevantFields() {
            const type = document.getElementById("type").value;

            document.getElementById("mcOptions").style.display = (type === "MULTIPLE_CHOICE" || type === "MULTI_SELECT") ? "block" : "none";

            document.getElementById("responseAnswer").style.display =
                (type === "QUESTION_RESPONSE" || type === "FILL_IN_THE_BLANK" || type === "PICTURE_RESPONSE") ? "block" : "none";

            document.getElementById("imageUrlRow").style.display = (type === "PICTURE_RESPONSE") ? "block" : "none";
        }



        function addOptionField() {
            const container = document.getElementById("optionsContainer");
            const index = container.children.length + 1;

            const div = document.createElement("div");
            div.className = "single-option";
            div.innerHTML = `
    <label for="option${index}">Option ${index}:</label>
    <input type="text" name="option${index}" id="option${index}" required />
    <label for="correct${index}">
        <input type="checkbox" name="correct_options" id="correct${index}" value="${index}" />
        Correct
    </label>
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

            if (type === "MULTIPLE_CHOICE") {
                if (checkboxes.length !== 1) {
                    alert("Multiple Choice questions must have exactly one correct option.");
                    return false;
                }
            } else if (type === "MULTI_SELECT") {
                if (checkboxes.length === 0) {
                    alert("Multi Select questions must have at least one correct option.");
                    return false;
                }
            } else if (type === "FILL_IN_THE_BLANK" || type === "QUESTION_RESPONSE" || type === "PICTURE_RESPONSE") {
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

<form method="post" action="/add-question" onsubmit="return validateForm()">
    <input type="hidden" name="quizId" value="${param.quizId}" />
    <input type="hidden" name="position" value="${param.position}" />

    <label for="type">Question Type:</label>
    <select name="type" id="type" onchange="showRelevantFields()" required>
        <option value="">--Select--</option>
        <%
            for (QuestionType type : QuestionType.values()) {
                String displayText = "";
                switch(type) {
                    case MULTIPLE_CHOICE: displayText = "Multiple Choice (1 correct)"; break;
                    case MULTI_SELECT: displayText = "Multi Select (multiple correct)"; break;
                    case FILL_IN_THE_BLANK: displayText = "Fill in the Blank"; break;
                    case QUESTION_RESPONSE: displayText = "Question-Response"; break;
                    case PICTURE_RESPONSE: displayText = "Picture Response"; break;
                    default: continue;
                }
        %>
        <option value="<%= type.name() %>"><%= displayText %></option>
        <% } %>
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


    <div id="mcOptions" class="option-section" style="display: none;">
        <h4>Answer Options:</h4>
        <input type="hidden" id="numOptions" name="numOptions" value="0" />
        <div id="optionsContainer"></div>
        <p id="mcHint" style="font-style: italic; color: gray;"></p>

        <div class="add-option-container">
            <a href="javascript:void(0);" onclick="addOptionField()" class="add-option-link">
                <img src="https://cdn-icons-png.flaticon.com/512/4315/4315609.png" alt="Add" class="add-option-icon" />
                <span class="add-option-text">Add Option</span>
            </a>
        </div>

        <br/><br/>
    </div>

    <div id="responseAnswer" style="display: none;">
        <label for="correctAnswer">Correct Answer (Text):</label>
        <input type="text" name="correctAnswer" id="correctAnswer" />
        <br/><br/>
    </div>

    <input type="submit" class="primary-button" value="Add Question" />

    <br/><br/>
</form>

<div class="center-button">
    <button type="button" class="finish-button"
            onclick="location.href='${pageContext.request.contextPath}/quiz-info?quizId=${param.quizId}'">
        Finish
    </button>
</div>
</body>
</html>
