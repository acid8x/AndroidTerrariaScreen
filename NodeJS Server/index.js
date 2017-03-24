var
  express = require('express'),
  app = express(),
  io,
  http,
  id = 0;

http = require('http').createServer(app);
io = require('socket.io')(http, { pingInterval: 500 });

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
	
	socket.on('move', function(data) {
		socket.broadcast.emit('move', data);
	});	
	
	socket.on('use', function(data) {
		socket.broadcast.emit('use', data);
	});	
	
	socket.on('completeItem', function(data) {
		socket.broadcast.emit('completeItem', data);
	});
	
	socket.on('stackOnly', function(data) {
		socket.broadcast.emit('stackOnly', data);
	});
	
	socket.on('playerInfo', function(data) {
		socket.broadcast.emit('playerInfo', data);
	});
		
	socket.on('disconnect', function() {
		console.log(socket.Ident + " disconnected");
  });
});