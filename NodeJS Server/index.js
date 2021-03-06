var
  express = require('express'),
  app = express(),
  io,
  http,
  id = 0;

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
	socket.broadcast.emit('renew');
	
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
		socket.broadcast.emit('playerInfo', data, socket.Ident);
	});
	
	socket.on('disconnect', function() {
		socket.broadcast.emit('disconnected', socket.Ident);
		console.log(socket.Ident + " disconnected");
	});
});