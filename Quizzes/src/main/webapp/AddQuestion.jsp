<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add Question</title>
    <script>
        function showRelevantFields() {
            const type = document.getElementById("type").value;
            document.getElementById("imageUrlRow").style.display = (type === "PICTURE_RESPONSE") ? "block" : "none";
            document.getElementById("mcOptions").style.display = (type === "MC" || type === "FILL") ? "block" : "none";
            document.getElementById("responseAnswer").style.display = (type === "RESPONSE") ? "block" : "none";
        }

        function addOptionField() {
            const container = document.getElementById("optionsContainer");
            const index = container.children.length;

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

        window.onload = showRelevantFields;
    </script>
</head>
<body>
<h2>Add a Question</h2>

<form method="post" action="${pageContext.request.contextPath}/add-question">
    <input type="hidden" name="quizId" value="${param.quizId}" />
    <input type="hidden" name="position" value="${param.position}" />

    <label for="type">Question Type:</label>
    <select name="type" id="type" onchange="showRelevantFields()" required>
        <option value="">--Select--</option>
        <option value="MC">Multiple Choice</option>
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
    </div>

    <div id="mcOptions" style="display: none;">
        <h4>Answer Options</h4>
        <input type="hidden" id="numOptions" name="numOptions" value="0" />
        <div id="optionsContainer"></div>
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
