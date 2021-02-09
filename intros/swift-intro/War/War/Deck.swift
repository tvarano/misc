//
//  Deck.swift
//  War
//
//  Created by Thomas Varano on 10/18/18.
//  Copyright © 2018 Thomas Varano. All rights reserved.
//

import Foundation

class Hand {
    var cards: [Card]
    var lastDrawn: Int
    let suits = ["D", "H", "S", "C"]
    
    init() {
        cards = []
        lastDrawn = 0
        for value in 1...13 {
            for suit in 0...3 {
                cards.append(Card(val:value, suit:suits[suit]))
            }
        }
        shuffle()
    }
    
    init(with cards: [Card]) {
        self.cards = cards
        lastDrawn = 0
        shuffle()
    }
    
    func shuffle() {
        cards.shuffle()
    }
    
    func count() -> Int {
        return cards.count
    }
    
    func deal(handSize: Int) -> [Card] {
        var ret: [Card] = []
        let amt = handSize > count() ? count() : handSize
        
        for _ in 0..<amt {
            ret.append(draw()!)
        }
        return ret
    }
    
    func draw() -> Card? {
        if count() == 0 {return nil}
        return cards.remove(at: 0)
    }
    
    func append(arr: [Card]) {
        for c in arr {
            append(card: c)
        }
    }
    
    func append(card: Card) {
        cards.append(card)
    }
    
    func setLastDrawn(last: Int) {
        lastDrawn = last
    }
    
    func isEmpty() -> Bool {
        return cards.isEmpty
    }
}
