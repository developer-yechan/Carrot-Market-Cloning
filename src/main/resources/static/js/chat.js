// WebSocket 연결 생성
var socket = new SockJS('/chat');
var stompClient = Stomp.over(socket);

// 연결 이벤트 핸들러
stompClient.connect({}, function (frame) {
    console.log('WebSocket 연결 성공');

    // 메시지 수신 구독
    stompClient.subscribe('/topic/messages', function (message) {
        showMessage(message.body);
    });
});

// 메시지 전송 처리
function sendMessage() {
    var messageText = document.getElementById('messageText').value;
    stompClient.send('/app/send', {}, messageText);
    document.getElementById('messageText').value = '';
}

// 메시지 표시
function showMessage(message) {
    var messageElement = document.createElement('li');
    messageElement.textContent = message;
    document.getElementById('chatMessages').appendChild(messageElement);
}
