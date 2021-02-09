//
//  Item.swift
//  SololearnApp
//
//  Created by Thomas Varano on 11/4/18.
//  Copyright © 2018 Thomas Varano. All rights reserved.
//

import Foundation


class Item: NSObject, NSCoding {
    static let Dir = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
    
    static let ArchiveURL = Dir.appendingPathComponent("items")
    
    var name: String
    
    init?(name: String) {
        self.name = name
        super.init()
    }
    
    func encode(with aCoder: NSCoder) {
        aCoder.encode(name, forKey: "name")
    }
    
    required convenience init?(coder aDecoder: NSCoder) {
        let name = aDecoder.decodeObject(forKey: "name") as! String
        self.init(name: name)
    }
}
