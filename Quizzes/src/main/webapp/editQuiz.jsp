<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="bean.Questions.Question" %>
<%@ page import="bean.Questions.AnswerOption" %>
<%@ page import="bean.Questions.QuestionType" %>
<%@ page import="java.util.*" %>

<html>
<head>
    <title>Edit Quiz</title>
    <link rel="stylesheet" href="/style/editQuiz.css" />
    <script>
        function confirmAndDelete(button, questionId) {
            if (confirm("Are you sure you want to delete this question?")) {
                fetch("deleteQuestion?questionId=" + questionId, { method: 'POST' })
                    .then(response => {
                        if (response.ok) {
                            const card = button.closest('.question-card');
                            card.remove();
                        } else {
                            alert("Failed to delete question.");
                        }
                    });
            }
        }

        function addOption(btn, type) {
            const card = btn.closest('.question-card');
            const optionsContainer = card.querySelector('.options-container');
            const questionId = card.dataset.qid;
            const count = optionsContainer.querySelectorAll('.option-row').length;
            const optionId = 'new_' + count;

            const div = document.createElement("div");
            div.className = "option-row";

            let inputHTML = `
    <label>Option:</label>
    <input type="text" class="option-text"
           name="questions[${questionId}][options][${optionId}][text]" required />

    <label>
        <input type="${type == 'MULTIPLE_CHOICE' ? 'radio' : 'checkbox'}"
               name="questions[` + questionId + `]` +
                (type === 'MULTIPLE_CHOICE'
                    ? '[correctOption]'
                    : '[options][' + optionId + '][correct]') + `"
               value="${optionId}" />
        Correct
    </label>
    <button type="button" onclick="deleteOption(this)">X</button>
`;

            div.innerHTML = inputHTML;
            optionsContainer.appendChild(div);
        }

        function deleteOption(btn) {
            if (confirm("Are you sure you want to delete this option?")) {
                btn.closest('.option-row').remove();

            }
        }
        function confirmAndDelete(button, questionId) {
            if (confirm("Are you sure you want to delete this question?")) {
                const card = button.closest('.question-card');
                card.remove();
            }
        }



        function validateForm() {
            const cards = document.querySelectorAll('.question-card');
            for (const card of cards) {
                const questionText = card.querySelector('.question-text');
                if (!questionText.value.trim()) {
                    alert("Question text is required.");
                    questionText.focus();
                    return false;
                }

                const type = card.dataset.qtype;
                const options = card.querySelectorAll('.option-text');
                for (const opt of options) {
                    if (!opt.value.trim()) {
                        alert("All option texts must be filled.");
                        opt.focus();
                        return false;
                    }
                }

                if (type === "MULTIPLE_CHOICE") {
                    if (!card.querySelector('input[type="radio"]:checked')) {
                        alert("Select exactly one correct answer for MULTIPLE_CHOICE.");
                        return false;
                    }
                }

                if (type === "CHECK_ALL_THAT_APPLY") {
                    if (!card.querySelector('input[type="checkbox"]:checked')) {
                        alert("At least one correct answer is required for CHECK_ALL_THAT_APPLY.");
                        return false;
                    }
                }

                if (type === "PICTURE_RESPONSE") {
                    const imageUrl = card.querySelector('.image-url');
                    if (!imageUrl.value.trim()) {
                        alert("Image URL is required for PICTURE_RESPONSE.");
                        imageUrl.focus();
                        return false;
                    }
                }
            }
            return true;
        }

        function toggleNewQuestionForm() {
            const form = document.getElementById("newQuestionForm");
            form.style.display = form.style.display === "none" ? "block" : "none";
        }

        function showRelevantFields() {
            const type = document.getElementById("newType").value;

            document.getElementById("mcOptions").style.display =
                (type === "MULTIPLE_CHOICE" || type === "MULTI_SELECT") ? "block" : "none";

            document.getElementById("responseAnswer").style.display =
                (type === "QUESTION_RESPONSE" || type === "FILL_IN_THE_BLANK" || type === "PICTURE_RESPONSE") ? "block" : "none";

            document.getElementById("imageUrlRow").style.display = (type === "PICTURE_RESPONSE") ? "block" : "none";

            // Clear existing options when type changes
            document.getElementById("optionsContainer").innerHTML = "";
            document.getElementById("numOptions").value = 0;
        }

        function addOptionField() {
            const container = document.getElementById("optionsContainer");
            const index = container.children.length + 1;
            const type = document.getElementById("newType").value;

            const inputType = (type === "MULTIPLE_CHOICE") ? "radio" : "checkbox";

            const div = document.createElement("div");
            div.className = "single-option";
            div.innerHTML = `
                <label for="option${index}">Option ${index}:</label>
                <input type="text" name="option${index}" id="option${index}" required />
                <label>
                    <input type="${inputType}" name="correct_options" value="${index}" />
                    Correct
                </label>
            `;
            container.appendChild(div);

            document.getElementById("numOptions").value = index;
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
        let newQuestionIndex = 0;

        function addNewQuestionBlock() {
            const container = document.getElementById("addQuestionContainer");
            const template = document.getElementById("question-template").innerHTML;
            const html = template.replace(/__INDEX__/g, newQuestionIndex++);
            const div = document.createElement("div");
            div.innerHTML = html;
            container.appendChild(div.firstElementChild);
        }

        function toggleNewQuestionFields(select) {
            const card = select.closest('.question-card');
            const optionsContainer = card.querySelector('.options-container');
            const imageUrlRow = card.querySelector('.image-url-row');
            const answerTextRow = card.querySelector('.answer-text-row');
            const addOptionBtn = card.querySelector('.add-option-btn');

            const type = select.value;

            optionsContainer.classList.add("hidden");
            imageUrlRow.classList.add("hidden");
            answerTextRow.classList.add("hidden");
            addOptionBtn.classList.add("hidden");

            optionsContainer.innerHTML = "";

            if (type === "MULTIPLE_CHOICE" || type === "MULTI_SELECT") {
                optionsContainer.classList.remove("hidden");
                addOptionBtn.classList.remove("hidden");
                addNewOption(addOptionBtn);
            } else if (type === "PICTURE_RESPONSE") {
                imageUrlRow.classList.remove("hidden");
                answerTextRow.classList.remove("hidden");
            } else if (type === "QUESTION_RESPONSE" || type === "FILL_IN_THE_BLANK") {
                answerTextRow.classList.remove("hidden");
            }
        }

        function addNewOption(button) {
            const card = button.closest('.question-card');
            const optionsContainer = card.querySelector('.options-container');
            const qIndex = card.querySelector('select').name.match(/\[(\d+)\]/)[1];
            const count = optionsContainer.children.length;
            const type = card.querySelector('select').value;
            const inputType = type === "MULTIPLE_CHOICE" ? "radio" : "checkbox";

            const div = document.createElement("div");
            div.className = "option-row";
            div.innerHTML = `
        <label>Option:</label>
        <input type="text" name="newQuestions[${qIndex}][options][${count}][text]" required />
        <label><input type="${inputType}" name="newQuestions[${qIndex}][${type === "MULTIPLE_CHOICE" ? "correctOption" : `options[${count}][correct]`}]" value="${count}" /> Correct</label>
        <button type="button" onclick="this.parentElement.remove()">X</button>
    `;
            optionsContainer.appendChild(div);
        }

        function updatePreview(input) {
            const url = input.value.trim();
            const img = input.closest('.image-url-row').querySelector('img');
            if (url) {
                img.src = url;
                img.style.display = "block";
            } else {
                img.style.display = "none";
            }
        }


        window.onload = function () {
            document.getElementById("image_url")?.addEventListener("input", updateImagePreview);
        }
    </script>
</head>
<body>
<%= "quizTitle: " + request.getAttribute("quizTitle") + "<br/>" %>

<div class="container">

    <h1>Edit Quiz ID: <%= request.getAttribute("quiz_id") %></h1>


    <form method="post" action="editQuiz" onsubmit="return validateForm();">
        <input type="hidden" name="quiz_id" value="<%= request.getAttribute("quiz_id") %>" />
    <%-- ... all existing question cards ... --%>
        <div class="quiz-info">
            <label for="quizTitle">Quiz Title:</label>
            <input type="text" id="quizTitle" name="quizTitle"
                   value="<%= request.getAttribute("quizTitle") != null ? request.getAttribute("quizTitle") : "" %>" required />

            <label for="quizDescription">Description:</label>
            <textarea id="quizDescription" name="quizDescription" rows="3"><%=
            request.getAttribute("quizDescription") != null ? request.getAttribute("quizDescription") : ""
            %></textarea>
        </div>


        <div class="quiz-settings">
            <label>
                <input type="checkbox" name="randomOrder"
                        <%= Boolean.TRUE.equals(request.getAttribute("isRandom")) ? "checked" : "" %> />
                Randomize question order
            </label><br/>

            <label>
                <input type="checkbox" name="multiPage"
                        <%= Boolean.TRUE.equals(request.getAttribute("multiPage")) ? "checked" : "" %> />
                Show one question per page
            </label><br/>

            <label>
                <input type="checkbox" name="immediateCorrection"
                        <%= Boolean.TRUE.equals(request.getAttribute("immediateCorrection")) ? "checked" : "" %> />
                Immediate correction after submitting
            </label>
        </div>
            <%
                Map<Question, List<AnswerOption>> questionOptions = (Map<Question, List<AnswerOption>>) request.getAttribute("question_options");
                Map<Question, String> questionTextAnswers = (Map<Question, String>) request.getAttribute("question_textAnswer");

                Set<Question> allQuestions = new LinkedHashSet<Question>();
                if (questionOptions != null) allQuestions.addAll(questionOptions.keySet());
                if (questionTextAnswers != null) allQuestions.addAll(questionTextAnswers.keySet());

                for (Question question : allQuestions) {
                    QuestionType type = question.getQuestionType();
            %>
            <div class="question-card" data-qtype="<%= type.name() %>" data-qid="<%= question.getId() %>">
                <div class="card-header">
                    <h2>Question ID: <%= question.getId() %> (<%= type.toString() %>)</h2>
                    <button type="button" class="delete-btn"
                            onclick="confirmAndDelete(this, <%= question.getId() %>)">🗑 Delete</button>
                </div>

                <input type="hidden" name="questions[<%= question.getId() %>][type]" value="<%= type.toString() %>" />

                <label>Question Text:</label>
                <input type="text" class="question-text" name="questions[<%= question.getId() %>][text]"
                       value="<%= question.getQuestionText() %>" required />

                <% if (type == QuestionType.MULTIPLE_CHOICE || type == QuestionType.MULTI_SELECT) {
                    List<AnswerOption> options = questionOptions.get(question);
                %>
                <div class="options-container">
                    <% for (AnswerOption option : options) { %>
                    <div class="option-row">
                        <label>Option:</label>
                        <input type="text" class="option-text"
                               name="questions[<%= question.getId() %>][options][<%= option.getId() %>][text]"
                               value="<%= option.getAnswerText() %>" required />
                        <label>
                            <input type="<%= type == QuestionType.MULTIPLE_CHOICE ? "radio" : "checkbox" %>"
                                   name="questions[<%= question.getId() %>]<%= type == QuestionType.MULTIPLE_CHOICE ? "[correctOption]" : "[options][" + option.getId() + "][correct]" %>"
                                   value="<%= option.getId() %>"
                                    <%= option.isCorrect() ? "checked" : "" %> />
                            Correct
                        </label>
                        <button type="button" onclick="deleteOption(this)">X</button>
                    </div>
                    <% } %>
                </div>
                <button type="button" class="add-option-btn"
                        onclick="addOption(this, '<%= type.name() %>')">➕ Add Option</button>
                <% } else if (type == QuestionType.QUESTION_RESPONSE || type == QuestionType.FILL_IN_THE_BLANK) {
                    String answer = questionTextAnswers.get(question);
                %>
                <label>Correct Answer:</label>
                <input type="text" name="questions[<%= question.getId() %>][answer]"
                       value="<%= answer != null ? answer : "" %>" />
                <% } else if (type == QuestionType.PICTURE_RESPONSE) {
                    String answer = questionTextAnswers.get(question);
                %>
                <label>Image URL:</label>
                <input type="text" class="image-url" name="questions[<%= question.getId() %>][image]"
                       value="<%= question.getImageUrl() %>" required />
                <div class="image-preview">
                    <img src="<%= question.getImageUrl() %>" alt="Image Preview" width="200" />
                </div>
                <label>Correct Answer:</label>
                <input type="text" name="questions[<%= question.getId() %>][answer]"
                       value="<%= answer != null ? answer : "" %>" />
                <% } %>
            </div>
            <% } %>

            <div id="addQuestionContainer"></div>

            <button type="button" class="add-option-btn" onclick="addNewQuestionBlock()">➕ Add New Question</button>

            <div class="form-actions">
            <input type="submit" value="💾 Save Changes" class="save-btn" />
        </div>
    </form>
</div>
<template id="question-template">
    <div class="question-card new-question" data-new="true">
        <h3>New Question</h3>

        <label>Question Type:</label>
        <select class="new-qtype" name="newQuestions[__INDEX__][type]" onchange="toggleNewQuestionFields(this)">
            <option value="">--Select--</option>
            <option value="MULTIPLE_CHOICE">Multiple Choice</option>
            <option value="MULTI_SELECT">Multi Select</option>
            <option value="FILL_IN_THE_BLANK">Fill in the Blank</option>
            <option value="QUESTION_RESPONSE">Question Response</option>
            <option value="PICTURE_RESPONSE">Picture Response</option>
        </select>

        <label>Prompt:</label>
        <input type="text" name="newQuestions[__INDEX__][text]" required />

        <div class="image-url-row hidden">
            <label>Image URL:</label>
            <input type="text" name="newQuestions[__INDEX__][image]" oninput="updatePreview(this)" />
            <div class="image-preview"><img src="" style="display:none;" /></div>
        </div>

        <div class="options-container hidden"></div>

        <div class="answer-text-row hidden">
            <label>Correct Answer:</label>
            <input type="text" name="newQuestions[__INDEX__][answer]" />
        </div>

        <button type="button" class="add-option-btn hidden" onclick="addNewOption(this)">➕ Add Option</button>
    </div>
</template>

</body>
</html>
