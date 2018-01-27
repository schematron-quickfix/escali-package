# Escali Schematron
The Escali Schematron Package is part of the [Schematron QuickFix](http://www.schematron-quickfix.com) project. The core module is a Schematron QuickFix compiler. The other modules provides components, APIs or GUIs to implement Schematron QuickFix or to embed the compiler. 

## Modules

Following a short overview of the projects modules. For more information please read the corresponding README.md

### Escali Schematron Compiler

The Escali Schematron Compiler is a set of XSLT and XProc scripts, Java classes and schema files to compile and validate Schematron QuickFix schemas as well as execute QuickFixes.

### Escali GUI Components

The Escali GUI Components provides Java Swing components, which can be used to integrate the Escali Schematron Compiler into an existing GUI.

### Escali Oxygen Plugin

The Escali Oxygen Plugin is a integration of the Escali Schematron Compiler and the Escali GUI Components into the Oxygen XML Editor via its plugin API.

### Escali Web

This project contains a JavaScript based Web editor to validate XML files by Schematron and execute QuickFixes. Therefor it runs the XSLT scripts of the Escali Schematron Compiler on the XSLT processor Saxon-CE.

This project was rudimentary transferred from a self-designed project structure and still needs to be customized for Maven.

### XPath based String Manipulation (XSM) 

The XSM processor performs manipulations of XML documents based on XPath. The manipulations are done by unparsed text modifications. Any other content will be preserved.

This process is needed to execute QuickFixes without getting the "XSLT effects": resolving all entities, loose XML and/or DTD declarations, make unexpected Whitespace changes, write attributes which was defined by default values into the document, etc.

## Dependencies And Related Projects

Base of this project are some helper JAVA classes. They are maintained in the GitHub organisation [oxygen-plugins](https://github.com/oxygen-plugins):

- [com.github.oxygen-plugins/common-xml](https://github.com/oxygen-plugins/common-xml)
- [com.github.oxygen-plugins/common-gui](https://github.com/oxygen-plugins/common-gui)
- [com.github.oxygen-plugins/common-oxygen](https://github.com/oxygen-plugins/common-oxygen)

## Release Notes

### Version 0.1.3

#### Escali Schematron Compiler

- Bug fixes:
    - [#5 Support of let variables in patterns for SQF](https://github.com/schematron-quickfix/escali-package/issues/5)

#### Escali Oxygen Plugin

- Bug fixes:
    - [#1 Options menu seems not to be modal](https://github.com/schematron-quickfix/escali-package/issues/1)
    - [#6 UserEntry is not available, after switching to another Message](https://github.com/schematron-quickfix/escali-package/issues/6)

### Version 0.1.2

#### Component update

- com.github.oxygen-plugins/common-xml: version 0.1.2
- com.github.oxygen-plugins/common-gui: version 0.1.2
- com.github.oxygen-plugins/common-oxygen: version 0.1.2

### Version 0.1.1

#### Escali Schematron Compiler

- Introduced variable $es:match to have access to the matched text phrase of @es:regex.
- Bug fixes:
    - Attributes of Literal Result Elements in NULL-Namespace in QuickFixes was not be copied into the document
    
#### Escali Oxygen Plugin
- Bug fixes:
    - BaseUri was null in some cases. The error position could not be found.
    - concurrent exception occured, when plugin was switched of in options
    - Wrong report was displayed, if switching the editor during the validation.

#### Dependencies Update
- Usage of common-xml version 0.1.1: Character entities should not hurt anymore.

### Version 0.1.0

- First public release