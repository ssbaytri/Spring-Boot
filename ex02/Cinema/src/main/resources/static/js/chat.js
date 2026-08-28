document.addEventListener('DOMContentLoaded', function () {
    const filmId = document.documentElement.getAttribute('data-film-id');
    const currentUserId = document.documentElement.getAttribute('data-current-user-id');

    const stompClient = new StompJs.Client({
        webSocketFactory: () => new SockJS('/ws'),
        onConnect: () => {
            stompClient.subscribe('/films/' + filmId + '/chat/messages', function (message) {
                const data = JSON.parse(message.body);
                appendMessage(data);
            });
        }
    });

    stompClient.activate();

    function appendMessage(data) {
        const chatBox = document.getElementById('chatBox');
        const messageDiv = document.createElement('div');
        messageDiv.className = 'message' + (String(data.userId) === String(currentUserId) ? ' mine' : '');

        const author = document.createElement('div');
        author.className = 'author';
        author.textContent = String(data.userId) === String(currentUserId)
            ? 'you'
            : data.firstName + ' ' + data.lastName;

        const content = document.createElement('div');
        content.textContent = data.content;

        messageDiv.appendChild(author);
        messageDiv.appendChild(content);
        chatBox.appendChild(messageDiv);
        chatBox.scrollTop = chatBox.scrollHeight;
    }

    const chatForm = document.getElementById('chatForm');
    if (chatForm) {
        chatForm.addEventListener('submit', function (e) {
            e.preventDefault();
            const input = document.getElementById('messageInput');
            const content = input.value.trim();

            if (!content) return;

            stompClient.publish({
                destination: '/app/films/' + filmId + '/chat/send',
                body: JSON.stringify({ content: content })
            });

            input.value = '';
            input.focus();
        });
    }
});