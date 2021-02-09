//
//  ViewController.swift
//  War
//
//  Created by Thomas Varano on 10/15/18.
//  Copyright © 2018 Thomas Varano. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var leftCard: UIImageView!
    
    @IBOutlet weak var rightCard: UIImageView!
    
    @IBOutlet weak var winner: UILabel!
    @IBOutlet weak var cpuScore: UILabel!
    @IBOutlet weak var playerScore: UILabel!
    @IBOutlet weak var playerName: UILabel!
    @IBOutlet weak var cpuName: UILabel!
    var deck: Hand!
    var leftHand: Hand!, rightHand: Hand!
    
    
    /*
    so what were gonna do here
    after each hand, add both cards used to the end of the pile.
    make drawFromTop() to deck, dont change lastDrawn
     this way you can just add all the cards in a war
     also the score is now being the count of the players decks
     alright
     */
    
    private func initialize() {
        winner.isHidden = true
        deck = Hand()
        leftHand = Hand(with: deck.deal(handSize: deck.count() / 2))
        rightHand = Hand(with: deck.cards)
        cpuScore.text = String(rightHand.count())
        playerScore.text = String(leftHand.count())
        for i in 1...8 {
            self.view.viewWithTag(i)!.isHidden = true
        }
    }
    
    @IBAction func resetPress(_ sender: Any) {
        initialize()
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        initialize()
    }

    @IBAction func drawCardPress(_ sender: UIButton) {
        if !winner.isHidden {
            return
        }
        let leftVal = leftHand.draw(), rightVal = rightHand.draw()
        
        for i in 1...8 {
            self.view.viewWithTag(i)!.isHidden = true
        }
        
        leftCard.image = UIImage(named: leftVal!.toString())
        rightCard.image = UIImage(named: rightVal!.toString())
        
        let win = getWinner(leftVal: leftVal!, rightVal: rightVal!)
        print("Winner \(win)")
        //UI Updates
        if win == -1 {
            playerWin(vals: [leftVal!, rightVal!])
        } else if win == 1 {
            computerWin(vals: [leftVal!, rightVal!])
        } else {
            // war
            print("WARWAR")
            var tempImageHolder = self.view.viewWithTag(1)! as! UIImageView
            tempImageHolder.image = UIImage(named: leftVal!.toString())
            tempImageHolder = self.view.viewWithTag(2)! as! UIImageView
            tempImageHolder.image = UIImage(named: rightVal!.toString())
            
            let leftCards = leftHand.deal(handSize: 4)
            let rightCards = rightHand.deal(handSize: 4)
            
            let cardsInContention = [leftVal!, rightVal!] + leftCards + rightCards
            
            leftCard.image = UIImage(named: leftCards[leftCards.count - 1].toString())
            
            rightCard.image = UIImage(named: rightCards[rightCards.count - 1].toString())
            
            for i in 1...8 {
            self.view.viewWithTag(i)!.isHidden = false
            }
            
            let win = getWinner(leftVal: leftCards[leftCards.count - 1], rightVal: rightCards[rightCards.count - 1])
            
            if win == -1 {
                // player
                playerWin(vals: cardsInContention)
            } else {
                //tie goes to computer
                computerWin(vals: cardsInContention)
            }
        }
        if leftHand.count() == 0 || rightHand.count() == 0 {
            winner.isHidden = false
        }
    }
    
    private func playerWin(vals: [Card]) {
        // player win
        playerName.textColor = UIColor.yellow
        playerScore.textColor = UIColor.yellow
        cpuName.textColor = UIColor.darkText
        cpuScore.textColor = UIColor.darkText
        leftHand.append(arr: vals)
        cpuScore.text = String(rightHand.count())
        playerScore.text = String(leftHand.count())
    }
    
    private func computerWin(vals: [Card]) {
        // computer win
        playerName.textColor = UIColor.darkText
        playerScore.textColor = UIColor.darkText
        cpuName.textColor = UIColor.yellow
        cpuScore.textColor = UIColor.yellow
        rightHand.append(arr: vals)
        cpuScore.text = String(rightHand.count())
        playerScore.text = String(leftHand.count())
    }
    
    func getWinner(leftVal: Card, rightVal: Card) -> Int {
        if leftVal.val > rightVal.val {
            return -1
        } else if leftVal.val < rightVal.val {
            return 1
        }
        return 0
    }
}
