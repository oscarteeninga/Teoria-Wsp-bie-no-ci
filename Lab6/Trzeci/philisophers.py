import threading
import random
import time

import pylab


N = 5
C = 10
eaten = 0
times = [0.0 for n in range(N)]
waiter = threading.BoundedSemaphore(value=N-1)


class Fork:
    def __init__(self, id):
        self.id = id
        self.sem = threading.Semaphore()
        self.available = True

    def take(self, philoid):
        self.available = False
        self.sem.acquire(blocking=True)
        #print(self.id, ' is acquired by ', philoid)

    
    def put(self):
        #print(self.id, ' is released.')
        self.sem.release()
        self.available = True


class Philosopher(threading.Thread): 
    def __init__(self, id, f1, f2):
        threading.Thread.__init__(self)
        self.id = id
        self.f1 = f1
        self.f2 = f2
 
    def run(self):
        for i in range(C):
            self.hierarchy()
 
    def naive(self):
        fork1, fork2 = self.f1, self.f2
        global eaten
        start = time.time_ns()
        fork1.take(self.id)
        fork2.take(self.id)
        times[self.id] += (time.time_ns() - start)
        print(self.id, ' is eating...')
        eaten += 1
        print(self.id, ' ended eating.')
        fork1.put()
        fork2.put()

    def asym(self):
        fork1, fork2 = self.f1, self.f2
        global eaten

        if (self.id % 2 == 0):
            fork1, fork2 = self.f1, self.f2
        else:
            fork1, fork2 = self.f2, self.f1

        start = time.time_ns()
        fork1.take(self.id)
        fork2.take(self.id)
        times[self.id] += (time.time_ns() - start)
        print(self.id, ' is eating...')
        eaten += 1
        print(self.id, ' ended eating.')
        fork1.put()
        fork2.put()

    def cond(self):
        fork1, fork2 = self.f1, self.f2
        global eaten, waiter
        waiter.acquire(blocking=True)
        start = time.time_ns()
        fork1.take(self.id)
        fork2.take(self.id)
        times[self.id] += (time.time_ns() - start)
        print(self.id, ' is eating...')
        eaten += 1
        print(self.id, ' ended eating.')
        fork1.put()
        fork2.put()
        waiter.release()
    
    def hungry(self):
        fork1, fork2 = self.f1, self.f2
        global eaten
        start = time.time_ns()

        while (not(fork1.available and fork2.available)):
            time.sleep(1)

        fork1.take(self.id)
        fork2.take(self.id)
        times[self.id] += (time.time_ns() - start)
        print(self.id, ' is eating...')
        eaten += 1
        print(self.id, ' ended eating.')
        fork1.put()
        fork2.put()

    def hierarchy(self):
        global eaten
        start = time.time_ns() 

        if (self.f1.id > self.f2.id):
            fork1, fork2 = self.f2, self.f1
        else:
            fork1, fork2 = self.f1, self.f2

        fork1.take(self.id)
        fork2.take(self.id)
        times[self.id] += (time.time_ns() - start)
        print(self.id, ' is eating...')
        eaten += 1
        print(self.id, ' ended eating.')
        fork1.put()
        fork2.put()
        

def DiningPhilosophers():
    forks = [Fork(n) for n in range(N)]
 
    philosophers = [Philosopher(i, forks[i%N], forks[(i+1)%N]) for i in range(N)]
 
    for p in philosophers: p.start()

    while eaten != N*C:
        time.sleep(1)
    
    for i in range(N):
        times[i] /= (C*1000)
        print(i, ' - ', times[i])
    
    pylab.plot([i for i in range(N)], times)
    pylab.show()


DiningPhilosophers()