// const wsUrl = 'ws://localhost:8080/stomp-endpoint';
// const client = Stomp.over(new WebSocket(wsUrl));
// let currentChannelId = '';
// let currentUserId; // 사용자 ID를 저장할 전역 변수
//
// client.connect({}, (frame) => {
//     console.log('Connected: ' + frame);
//
//     // Invite Button Click Event
//     document.getElementById('inviteButton').onclick = function() {
//         const channelId = document.getElementById('inviteChannelIdInput').value;
//         const inviteUserId = document.getElementById('inviterUserIdInput').value;
//         const participantId = document.getElementById('inviteeUserIdInput').value;
//
//         if (channelId && inviteUserId && participantId) {
//             fetch('/participants/invite', {
//                 method: 'POST',
//                 headers: {
//                     'Content-Type': 'application/json',
//                 },
//                 body: JSON.stringify({
//                     channelId: channelId,
//                     inviteUserId: inviteUserId,
//                     participantId: participantId
//                 }),
//             })
//                 .then(response => {
//                     if (!response.ok) {
//                         throw new Error('Network response was not ok');
//                     }
//                     return response.text();
//                 })
//                 .then(data => {
//                     currentUserId = participantId;
//                     document.getElementById('messageChannelIdInput').value = channelId;
//                     alert(`User ${participantId} invited to channel ${data.channelId}`);
//                 })
//                 .catch(error => {
//                     alert(`Error: ${error.message}`);
//                 });
//         } else {
//             alert('Please enter Channel ID, Inviter User ID, and Invitee User ID');
//         }
//     };
//
//     // Create Channel Button Click Event
//     document.getElementById('createChannelButton').onclick = function() {
//         const channelName = document.getElementById('channelNameInput').value;
//         const creatorUserId = document.getElementById('creatorUserIdInput').value;
//
//         if (channelName && creatorUserId) {
//             fetch('/channel/createChannel', {
//                 method: 'POST',
//                 headers: {
//                     'Content-Type': 'application/json'
//                 },
//                 body: JSON.stringify({
//                     channelName: channelName,
//                     creatorUserId: creatorUserId
//                 })
//             })
//                 .then(response => {
//                     if (!response.ok) {
//                         throw new Error('Network response was not ok');
//                     }
//                     return response.json();
//                 })
//                 .then(data => {
//                     alert(`Channel '${data.channelName}' created successfully!`);
//                 })
//                 .catch(error => {
//                     console.error('Error creating channel:', error);
//                     alert('Error creating channel: ' + error.message);
//                 });
//         } else {
//             alert('Please enter both channel name and creator user ID');
//         }
//     };
//
//     // Send Message Button Click Event
//     document.getElementById('sendMessageButton').onclick = function() {
//         const messageContent = document.getElementById('messageInput').value;
//         const channelId = document.getElementById('messageChannelIdInput').value;
//
//         if (channelId && messageContent && currentUserId) {
//             const chatMessage = {
//                 userId: currentUserId,
//                 content: messageContent,
//                 channelId: channelId
//             };
//             console.log(`채널 ${channelId}에서 사용자 ${currentUserId}로 메시지를 전송합니다.`);
//             client.send(`/app/chat/${channelId}`, { 'Content-Type': 'application/json' }, JSON.stringify(chatMessage));
//             document.getElementById('messageInput').value = '';
//         } else {
//             alert('메시지를 입력하고 먼저 채널에 참여하세요.');
//         }
//     };
//
// }, (error) => {
//     console.error('WebSocket connection error: ' + error);
// });
//
// function loadChannelList() {
//     const userId = document.getElementById('userIdInput').value;
//
//     if (userId) {
//         fetch(`/participants/${userId}/channels`)
//             .then(response => {
//                 if (!response.ok) {
//                     throw new Error('채널 목록 로드에 실패했습니다');
//                 }
//                 return response.json();
//             })
//             .then(channelList => {
//                 console.log('채널 목록:', channelList);
//                 const channelsDiv = document.getElementById('channels');
//                 channelsDiv.innerHTML = '';
//
//                 channelList.forEach(channel => {
//                     const channelElement = document.createElement('div');
//                     channelElement.textContent = `채널 ID: ${channel.channelId}, 채널 이름: ${channel.name} `;
//                     console.log(typeof channel.channelId); // 이 코드를 통해 channelId의 타입을 확인
//
//
//                     // 입장하기 버튼 추가
//                     const joinButton = document.createElement('button');
//                     joinButton.textContent = '입장하기';
//                     joinButton.onclick = () => {
//                         loadChatHistory(channel.channelId); // 해당 채널 ID로 채팅 내용 로드
//                     };
//
//                     channelElement.appendChild(joinButton); // 버튼을 채널 요소에 추가
//                     channelsDiv.appendChild(channelElement); // 전체 채널 요소에 추가
//                 });
//             })
//             .catch(error => {
//                 console.error('채널 목록 로드 중 오류 발생:', error);
//             });
//     } else {
//         alert('사용자 ID를 입력하세요.');
//     }
// }
//
// function loadChatHistory(channelId) {
//     fetch(`/${channelId}/chats`)
//         .then(response => {
//             if (!response.ok) {
//                 throw new Error('채팅 기록 로드에 실패했습니다');
//             }
//             return response.json();
//         })
//         .then(chatHistory => {
//             const messagesDiv = document.getElementById('messages');
//             messagesDiv.innerHTML = '';
//             chatHistory.forEach(chat => {
//                 const messageElement = document.createElement('div');
//                 messageElement.textContent = `${chat.userName}: ${chat.content} (${new Date(chat.createdAt).toLocaleString()})`;
//                 messagesDiv.appendChild(messageElement);
//             });
//         })
//         .catch(error => {
//             console.error('채팅 기록 로드 중 오류 발생:', error);
//         });
// }
//
