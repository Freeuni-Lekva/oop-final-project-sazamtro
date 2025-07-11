<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="bean.Questions.Question" %>
<%@ page import="bean.Questions.AnswerOption" %>
<%@ page import="bean.Questions.QuestionType" %>
<%@ page import="java.util.*" %>
<%@ page import="bean.User" %>

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
                const questionText = card.querySelector('.question-text, .prompt-input');
                if (!questionText || !questionText.value.trim()) {
                    alert("Question text is required.");
                    questionText.focus();
                    return false;
                }

                // Determine question type
                let type = card.dataset.qtype;
                if (!type) {
                    const select = card.querySelector('select[name^="newQuestions"]');
                    if (select) {
                        type = select.value;
                    }
                }

                // Check for options if needed
                const options = card.querySelectorAll('.option-text, .options-container input[type="text"]');
                if (type === "MULTIPLE_CHOICE" || type === "MULTI_SELECT") {
                    for (const opt of options) {
                        if (!opt.value.trim()) {
                            alert("All option texts must be filled.");
                            opt.focus();
                            return false;
                        }
                    }
                }

                // Validation rules by type
                if (type === "MULTIPLE_CHOICE") {
                    if (!card.querySelector('input[type="radio"]:checked')) {
                        alert("Select exactly one correct answer for MULTIPLE_CHOICE.");
                        return false;
                    }
                }

                if (type === "MULTI_SELECT") {
                    if (!card.querySelector('input[type="checkbox"]:checked')) {
                        alert("At least one correct answer is required for MULTI_SELECT.");
                        return false;
                    }
                }

                if (type === "PICTURE_RESPONSE") {
                    const imageUrl = card.querySelector('.image-url, input[name^="newQuestions"][name$="[image]"]');
                    if (!imageUrl || !imageUrl.value.trim()) {
                        alert("Image URL is required for PICTURE_RESPONSE.");
                        imageUrl.focus();
                        return false;
                    }
                }
            }

            const newQuestionCards = document.querySelectorAll('.question-card.new-question');
            for (const card of newQuestionCards) {
                const typeSelect = card.querySelector('select.new-qtype');
                if (!typeSelect || !typeSelect.value) {
                    alert("Please select a question type for all new questions.");
                    typeSelect.focus();
                    return false;
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
            const insertBlankBtn = card.querySelector('.insert-blank-btn');
            insertBlankBtn.classList.add("hidden");
            if (type === "FILL_IN_THE_BLANK") {
                insertBlankBtn.classList.remove("hidden");
            }

        }
        function insertBlank(button) {
            const input = button.closest('.prompt-with-button').querySelector('input');
            const cursorPos = input.selectionStart;
            const text = input.value;
            const before = text.substring(0, cursorPos);
            const after = text.substring(cursorPos);
            const blank = " ______ ";

            input.value = before + blank + after;
            input.focus();
            input.selectionStart = input.selectionEnd = cursorPos + blank.length;
        }

        function addNewOption(button) {
            const card = button.closest('.question-card');
            const optionsContainer = card.querySelector('.options-container');
            const qIndex = card.querySelector('select').name.match(/\[(\d+)\]/)[1];
            const count = optionsContainer.children.length;
            const type = card.querySelector('select').value;
            const inputType = type === "MULTIPLE_CHOICE" ? "radio" : "checkbox";

            let nameAttr;
            if (type === "MULTIPLE_CHOICE") {
                nameAttr = `newQuestions[${qIndex}][correctOption]`;
            } else {
                nameAttr = `newQuestions[${qIndex}][options][${count}][correct]`;
            }

            const div = document.createElement("div");
            div.className = "option-row";
            div.innerHTML = `
        <label>Option:</label>
        <input type="text" name="newQuestions[${qIndex}][options][${count}][text]" required />
        <label>
            <input type="${inputType}" name="${nameAttr}" value="${count}" />
            Correct
        </label>
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

<div class="container">
    <div style="text-align: center; margin: 20px 0;">
            <h1>Edit Quiz: <%= request.getAttribute("quizTitle") %></h1>

    </div>


    <form method="post" action="editQuiz" onsubmit="return validateForm();">
        <input type="hidden" name="quiz_id" value="<%= request.getAttribute("quiz_id") %>" />
        <input type="hidden" name="creator_id" value="<%= ((User) request.getAttribute("creator")).getUserId() %>" />
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

                List<Question> questions = (List<Question>)request.getAttribute("questions");


                for (Question question : questions) {
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
                <div class="prompt-with-button">
                    <input type="text" class="question-text prompt-input" name="questions[<%= question.getId() %>][text]"
                           value="<%= question.getQuestionText() %>" required />
                    <% if (type == QuestionType.FILL_IN_THE_BLANK) { %>
                    <button type="button" class="insert-blank-btn" onclick="insertBlank(this)">Insert Blank</button>
                    <% } %>
                </div>


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

        <!-- Go Back button, links back to quiz list or previous page -->
        <a href="/myQuizzes" class="go-back-btn">← Go Back</a>
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
        <div class="prompt-with-button">
            <input type="text" name="newQuestions[__INDEX__][text]" class="prompt-input" required />
            <button type="button" class="insert-blank-btn hidden" onclick="insertBlank(this)">Insert Blank</button>
        </div>


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
