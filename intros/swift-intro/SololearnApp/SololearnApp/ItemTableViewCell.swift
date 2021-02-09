//
//  ItemTableViewCell.swift
//  SololearnApp
//
//  Created by Thomas Varano on 11/4/18.
//  Copyright © 2018 Thomas Varano. All rights reserved.
//

import UIKit

class ItemTableViewCell: UITableViewCell {

    @IBOutlet weak var nameLabel: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
