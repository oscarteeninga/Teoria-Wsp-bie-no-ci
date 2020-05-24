var waterfall = require('async-waterfall');

function printAsync(s, cb) {
    var delay = Math.floor((Math.random()*1000)+500);
    setTimeout(function() {
        console.log(s);
        if (cb) cb();
    }, delay);
 }

function loop(n) {
    if (n > 0) {
        waterfall([
            function(callback) {  
                callback(null, function() { 
                    printAsync("3", function() { 
                        loop(n-1); 
                    }); 
                }); 
            }, 
            function(cb, callback) { 
                callback(null, function() { 
                    printAsync("2", function() { 
                        cb(); 
                    });
                }); 
            },
            function(cb, callback) {
                callback(null, function() { 
                    printAsync("1", function() { cb(); });
                });
            },
            function(cb, callback) {
                cb();
            }
        ]);
    } else {
        console.log("done!");
    }
}

 /* 
 ** Zadanie:
 ** Napisz funkcje loop(n), ktora powoduje wykonanie powyzszej 
 ** sekwencji zadan n razy. Czyli: 1 2 3 1 2 3 1 2 3 ... done
 ** wykorzystaj funkcję waterfall biblioteki async.
 */
 
 loop(4);