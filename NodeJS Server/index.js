var
  express = require('express'),
  app = express(),
  io,
  http,
  id = 0;

var arr = [];

http = require('http').createServer(app);
io = require('socket.io')(http);

http.listen(2222, function () {
	require('dns').lookup(require('os').hostname(), function (err, add, fam) {
		console.log('socket.io server listening on ' + add + ' port', 2222);
	});
});
			
io.on('connection', function(socket) {
	socket.Ident = id++;		
	console.log(socket.Ident + " connected");
	
	socket.on('renew', function() {
		socket.broadcast.emit('renew');
	});	
	
	socket.on('connected', function() {
		socket.broadcast.emit('connected', arr);
	});
	
	socket.on('move', function(data) {
		socket.broadcast.emit('move', data);
	});	
	
	socket.on('completeItem', function(data) {
		socket.broadcast.emit('completeItem', data, socket.Ident);
	});
	
	socket.on('stackOnly', function(data) {
		socket.broadcast.emit('stackOnly', data, socket.Ident);
	});
	
	socket.on('playerInfo', function(data) {
		if (arr.indexOf(socket.Ident) == -1) {		
			console.log(socket.Ident + " playerInfo connected");
			arr.push(socket.Ident);
		}
		socket.broadcast.emit('playerInfo', data, socket.Ident);
	});
	
	socket.on('playerList', function() {
		console.log("playerList sent");
		var info = "";
		for (var i = 0, len = arr.length; i < len; i++) {
			if (i != 0) info += ",";
			info += arr[i];
		}
		socket.broadcast.emit('playerList', info);
	});
	
	socket.on('disconnect', function() {
		console.log(socket.Ident + " disconnected");
		var index = arr.indexOf(socket.Ident);
		if (index > -1) {
			arr.splice(index, 1);					
			console.log(socket.Ident + " playerInfo disconnected");
		}
	});
});