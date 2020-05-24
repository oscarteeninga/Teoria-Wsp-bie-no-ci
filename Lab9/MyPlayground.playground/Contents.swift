import UIKit

var str = "Hello, playground"

enum Sex {
    case male, female
}


class Person {
    var name:String
    var age:Int
    var sex:Sex
    
    init(name: String, age: Int, sex: Sex) {
        self.name = name
        self.age = age
        self.sex = sex
    }
    
    func toString() -> String {
        return ("Name: " + self.name)
    }
}


var Oscar = Person(name: "Oscar", age: 21, sex: .male)

print(Oscar.toString())
