//
//  FormData.swift
//  iosApp
//
//  Created by Said on 21/03/2023.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

class FormData: ObservableObject {
    @Published var fields = Fields()
    @Published var hasErrors = false
    
    init(fields initFields: Fields) {
        fields = initFields
    }
    
    func isFieldEmpty(_ key: String) -> Bool {
        return fields[key]?.value.isEmpty ?? true
    }
    
    func fieldValue(for key: String) -> String {
        return fields[key]?.value ?? ""
    }
    
    func fieldError(for key: String) -> String {
        return fields[key]?.error ?? " "
    }
    
    func fieldLabel(for key: String) -> String {
        return fields[key]?.label ?? ""
    }
    
    func clearFields() {
        fields.forEach { key, value in
            fields[key]?.clearField()
        }
    }
    
    // Setting String Errors
    func setErrors(for key: String, _ errors: [String]) {
        if let error = errors.first {
            setHasErrors()
            fields[key]?.setError(error: error)
        } else {
            fields[key]?.clearError()
        }
    }
    
    func setErrors(_ errors: [String: [String]]) {
        errors.forEach { key, value in
            setErrors(for: key, value)
        }
    }
    
    func clearErrors() {
        fields.forEach { key, value in
            fields[key]?.clearError()
        }
    }
    
    func setHasErrors(_ hasErrors: Bool = true) {
        self.hasErrors = hasErrors
    }
    
    func bindingFieldValue(for key: String, set: @escaping (String) -> String = { $0 }) -> Binding<String> {
        return Binding(get: {
            return self.fieldValue(for: key)
        }, set: {
            self.fields[key]?.value = set($0)
        })
    }
}

/// form field structure
struct Field: Codable {
    var value: String = ""
    var label: String = ""
    var error: String = " "
    
    mutating func setError(error: String) {
        self.error = error
    }
    
    mutating func clearField() {
        self.value = ""
    }
    
    mutating func clearError() {
        self.error = ""
    }
    
    init(_ label: String) {
        self.label = label
    }
}

typealias Fields = [String: Field]
