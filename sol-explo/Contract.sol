pragma solidity ^0.7.0;

contract MyContract {
    string value; // use the public keyword to avoid writing getter
    string public val2 = "initial"; // avoids getter, constructor (i have a feeling this isnt good practice)
    string public constant const = "VALUE";

    bool public myBool = true;

    int public myint = 1;
    uint public unsigned = 1; // default 256 bits
    uint8 public liluint = 1;

    enum State {Waiting, Ready, Active}
    State public curr_state;

    struct Person {
        string _firstName; 
        string _lastName; 
    }

    Person[] public people; // because dynamic array, can't return the whole arr. must index.
    uint256 public peopleCount; //need size of array, since it is not static

    // better to do as mapping
    mapping (uint => Person) public peopleMap;

    constructor() {
        curr_state = State.Waiting;
    } 

    function isActive() public view returns(bool) {
        return curr_state == State.Active;
    }


    function get() public view returns(string memory) {
        return value;
    }   

    function set(string memory _value) public {
        value = _value;
    }


    function addPerson(string memory _firstName, string memory _lastName) public {
        people.push(Person(_firstName, _lastName));
        peopleCount += 1;
    }
}