//
//  ViewController.swift
//  SololearnApp
//
//  Created by Thomas Varano on 10/12/18.
//  Copyright © 2018 Thomas Varano. All rights reserved.
//

import UIKit

class ViewController: UIViewController {
    
    var item: Item?
    @IBOutlet weak var saveButton: UIBarButtonItem!
    @IBOutlet weak var nameTextField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let item = item {
            nameTextField.text = item.name
        }
    }
    @IBAction func cancel(_ sender: Any) {
        let isInAddMode = presentingViewController is UINavigationController
        
        if isInAddMode {
            dismiss(animated: true, completion: nil)
        }
        else {
            navigationController!.popViewController(animated: true)
        }

    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if sender as AnyObject? === saveButton {
            let name = nameTextField.text ?? ""
            item = Item(name: name)
        }
    }
}

