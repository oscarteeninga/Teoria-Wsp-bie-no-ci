var forks = new Array(5);
var philosophers = new Array(5);
var philosophers_fun = new Array(5);

function beb(fun, i, time = 1) {
    if (time < 1025) {
        setTimeout(function() {
            if(check(i)) {
                fun();
            } else {
                console.log("Philosopher " + time + " failed taking fork " + i);
                beb(fun, i, 2*time);
            }
        }, i*time*100);
    }     
}

function check(i) {
    return (forks[i] == true);
}

function put(i) {
    forks[i] = true;
}

function take(i) {
    forks[i] = false;
}

function eat(i) {
    console.log("Philosopher " + i + " is eating...");
    setTimeout(function() {
        console.log("Philosopher " + i + " ended eating");
        put(i);
        console.log("Philosopher " + i + " put " + i);
        put((i+1)%5);
        console.log("Philosopher " + i + " put " + (i+1)%5);
    }, 500);
}
for (var i = 0; i < 5; i++) {
    forks[i] = true;
    philosophers[i] = 0;
    philosophers_fun[i] = function() {
        var j = i;
        //Spróbuj wziąć widelec po prawej j
        beb(function() {
            take(j);
            console.log("Philosopher " + j + " taken " + j)
            //Spróbuj wziąć widelec po lewej j + 1
            beb(function() {
                take((j+1) % 5);
                console.log("Philosopher " + j + " taken " + (j+1)%5);
                eat(j);
            }, (j+1)%5, j)
        }, j, j); 
    }
    philosophers_fun[i]();
}