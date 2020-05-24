function printAsync(s, cb) {
    var delay = Math.floor((Math.random()*1000)+500);
    setTimeout(function() {
        console.log(s);
        if (cb) cb();
    }, delay);
}

 // Napisz funkcje (bez korzytania z biblioteki async) wykonujaca 
 // rownolegle funkcje znajdujace sie w tablicy 
 // parallel_functions. Po zakonczeniu wszystkich funkcji
 // uruchamia sie funkcja final_function. Wskazowka:  zastosowc 
 // licznik zliczajacy wywolania funkcji rownoleglych 
 
function inparallel(parallel_functions, final_function) {
    var n = 0;
    var i = 0;
    while (i < parallel_functions.length) {
        parallel_functions[i](function() {
            n++;
            if (n == parallel_functions.length) {
                final_function();
            }
        });
        i++;
        console.log(i);
    }
}
 
A=function(cb){printAsync("A",cb);}
B=function(cb){printAsync("B",cb);}
C=function(cb){printAsync("C",cb);}
D=function(cb){printAsync("Done",cb);}
 
inparallel([A,B,C],D) 
 
 // kolejnosc: A, B, C - dowolna, na koncu zawsze "Done"