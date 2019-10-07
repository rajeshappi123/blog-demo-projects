var exampleSocket;
var username;

function connect(nickname){   

    exampleSocket = new WebSocket("ws://localhost:8082/chat/"+nickname);

    username = nickname;

    exampleSocket.onopen = function (event) {
        var msg = {from: username, content: 'Connection request'}
        exampleSocket.send(JSON.stringify(msg));
        console.log('initiating connection to server...');
      };

      exampleSocket.onmessage = function (event){
        console.log('server replied with->'+event.data);
        var msg = JSON.parse(event.data);
        var chatMessages = document.getElementById('chatMessages');
        chatMessages.value =  chatMessages.value +'\n'+'User: '+msg.from+' sent message: '+msg.content;
      };

      exampleSocket.onerror = function (err){
        console.log('server replied with err ->'+err);
      }
}

function send(data){
 
    if(exampleSocket && exampleSocket.readyState=='1'){//OPEN
        var msg = {from: username, content: data}
        exampleSocket.send(JSON.stringify(msg));
        console.log('data message sent to server');
    }
    
}

//connect();