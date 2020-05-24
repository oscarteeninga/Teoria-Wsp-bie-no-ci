function beb(fun, time = 1) {
    if (time < 1025) {
        setTimeout(function() {
            if(a == b) {
                fun();
            } else {
                beb(fun, 2*time);
            }
        }, time*100);
    }     
}