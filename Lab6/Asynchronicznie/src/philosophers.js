var philo_times = [];
var N = 5;
var forks = [];
var philosophers = []
var philosohpers_counter = 0;
var times_eaten = 0;
var times = 10;

var plotly = require('plotly')("witcherallegro", "EzUn7H5FtVSerQWNNPOq")

var Fork = function(id) {
    this.id = id;
    this.state = 0;
    return this;
}

Fork.prototype.acquire = function(cb, id, time = 1) {
    var fork = this;
    setTimeout(function() {
        if (fork.state == 0) {
            //console.log("Philosopher " + id + " acquired fork " + fork.id);
            fork.state = 1;
            cb();
        } else {
            //console.log("Philosopher " + id + " does not acquired fork " + fork.id);
            fork.acquire(cb, id, time);
        }
    }, time);
    // zaimplementuj funkcje acquire, tak by korzystala z algorytmu BEB
    // (http://pl.wikipedia.org/wiki/Binary_Exponential_Backoff), tzn:
    // 1. przed pierwsza proba podniesienia widelca Filozof odczekuje 1ms
    // 2. gdy proba jest nieudana, zwieksza czas oczekiwania dwukrotnie
    //    i ponawia probe itd.
}

Fork.prototype.release = function() { 
    //console.log("Fork released.");
    this.state = 0; 
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    return this;
}

Philosopher.prototype.startNaive = function(count) {
    var forks = this.forks, f1 = this.f1, f2 = this.f2, id = this.id;
    var philosopher = this;
    if (count > 0) {
        forks[f1].acquire(function() {
            forks[f2].acquire(function() {
                //console.log("Philosopher " + id + " begin eating...");
                setTimeout(function() {
                    forks[f1].release();
                    forks[f2].release();
                    //console.log("Philosopher " + id + " ended eating.");
                    philosopher.startNaive(count-1);
                }, 10);
            }, id);
        }, id);
    }
}

Philosopher.prototype.startHierharchy = function(count) {
    var forks = this.forks, f1 = this.f1, f2 = this.f2, id = this.id;
    var philosopher = this;
    var start = new Date().getTime();
    if (count > 0) {
        if (f1 > f2) {
            tmp = f1;
            f1 = f2;
            f2 = tmp;
        }
        forks[f1].acquire(function() {
            forks[f2].acquire(function() {
                philo_times[id] = philo_times[id] + (new Date().getTime() - start);
                //console.log("Philosopher " + id + " begin eating...");
                setTimeout(function() {
                    forks[f2].release();
                    forks[f1].release();
                    times_eaten++;
                    //console.log("Philosopher " + id + " ended eating.");
                    philosopher.startHierharchy(count-1);
                }, 0);
            }, id);
        }, id);
    }
}

function hungry(forks, f1, f2, cb) {
    setTimeout(function() {
        if (forks[f2].state == 0) {
            setTimeout(function() {
                if (forks[f1].state == 0) {
                    cb();
                } else {
                    hungry(forks, f1, f2, cb);
                }
            }, forks.length % f1);
        } else {
            hungry(forks, f1, f2, cb);
        }
    }, Math.random()*10);
}

Philosopher.prototype.startHungry = function(count) {
    var forks = this.forks, f1 = this.f1, f2 = this.f2, id = this.id;
    var philosopher = this;
    var start = new Date().getTime();
    if (count > 0) {
        hungry(forks, f1, f2, function() {
            forks[f1].acquire(function() {
                forks[f2].acquire(function() {
                    philo_times[id] = philo_times[id] + (new Date().getTime() - start);
                    //console.log("Philosopher " + id + " begin eating...");
                    setTimeout(function() {
                        forks[f2].release();
                        forks[f1].release();
                        times_eaten++;
                        //console.log("Philosopher " + id + " ended eating.");
                        philosopher.startHungry(count-1);
                    }, 0);
                }, id);
            }, id);
        })
    }
}
    // zaimplementuj rozwiazanie naiwne
    // kazdy filozof powinien 'count' razy wykonywac cykl
    // podnoszenia widelcow -- jedzenia -- zwalniania widelcow

Philosopher.prototype.startAsym = function(count) {
    var forks = this.forks, f1 = this.f1, f2 = this.f2, id = this.id;
    var philosopher = this;
    var start = new Date().getTime();
    if (id % 2 == 0) {
        if (count > 0) {
            forks[f1].acquire(function() {
                forks[f2].acquire(function() {
                    philo_times[id] = philo_times[id] + (new Date().getTime() - start);
                    //console.log("Philosopher " + id + " begin eating...");
                    setTimeout(function() {
                        forks[f1].release();
                        forks[f2].release();
                        times_eaten++;
                        //console.log("Philosopher " + id + " ended eating.");
                        philosopher.startAsym(count-1);
                    }, 0);
                }, id);
            }, id);
        }
    } else {
        if (count > 0) {
            forks[f2].acquire(function() {
                forks[f1].acquire(function() {
                    philo_times[id] = philo_times[id] + (new Date().getTime() - start);
                    //console.log("Philosopher " + id + " begin eating...");
                    setTimeout(function() {
                        forks[f2].release();
                        forks[f1].release();
                        times_eaten++;
                        //console.log("Philosopher " + id + " ended eating.");
                        philosopher.startAsym(count-1);
                    }, 0);
                }, id);
            }, id);
        }
    }
    // zaimplementuj rozwiazanie asymetryczne
    // kazdy filozof powinien 'count' razy wykonywac cykl
    // podnoszenia widelcow -- jedzenia -- zwalniania widelcow
}

function conductor(forks, f1, f2, id, fun) {
    setTimeout(function() {
        if (philosohpers_counter < 3) {
            philosohpers_counter++;
            fun();
        } else {
            conductor(forks, f1, f2, id, fun);
        }
    }, 0);

}

Philosopher.prototype.startConductor = function(count) {
    var forks = this.forks, f1 = this.f1, f2 = this.f2, id = this.id;
    var philosopher = this;
    var start = new Date().getTime();
    if (count > 0) {
        conductor(forks, f1, f2, id, function() {
            forks[f1].acquire(function() {
                forks[f2].acquire(function() {
                    //console.log("Philosopher " + id + " begin eating...");
                    philo_times[id] = philo_times[id] + (new Date().getTime() - start);
                    setTimeout(function() {
                        forks[f1].release();
                        forks[f2].release();
                        philosohpers_counter--;
                        times_eaten++;
                        //console.log("Philosopher " + id + " ended eating.");
                        philosopher.startConductor(count-1);
                    }, 0);
                }, id);
            }, id);  
        });
    }
    // zaimplementuj rozwiazanie z kelnerem
    // kazdy filozof powinien 'count' razy wykonywac cykl
    // podnoszenia widelcow -- jedzenia -- zwalniania widelcow
}

for (var i = 0; i < N; i++) {
    forks.push(new Fork(i));
}

for (var i = 0; i < N; i++) {
    philosophers.push(new Philosopher(i, forks));
    philo_times[i] = 0;
}


for (var i = 0; i < N; i++) {
    philosophers[i].startHungry(times);
}


function test() {
    setTimeout(function() {
        if (times_eaten == N*times) {
            console.log(times_eaten);
            if (times_eaten == N*times) {
                console.log("OK - all philosohpers ended eating.");
            }
            for (var i = 0; i < N; i++) {
                philo_times[i] /= times;
                console.log(i + " - avg acquire time: " + philo_times[i]);
            }
            var data = [{
                x:Array.from(Array(N).keys()), 
                y:philo_times, 
                type: 'line',
                name: 'Asym(N:50)',
        
            }];
            var layout = {title : 'Asym(N:50)', fileopt : "overwrite", filename : "Asym"};
        
            plotly.plot(data, layout, function (err, msg) {
                if (err) return console.log(err);
                console.log(msg);
            });  
        } else {
            test();
        }
    }, 1000);
}

test();



