//
//  Card.swift
//  War
//
//  Created by Thomas Varano on 10/18/18.
//  Copyright © 2018 Thomas Varano. All rights reserved.
//

import Foundation

class Card {
    var val: Int
    var suit: String
    
    init(val: Int, suit: String) {
        self.suit = suit
        self.val = val
    }
    
    func toString() -> String {
        return String(val) + suit
    }
    
    func equals(c: Card) -> Bool {
        return val == c.val && suit == c.suit
    }
}

