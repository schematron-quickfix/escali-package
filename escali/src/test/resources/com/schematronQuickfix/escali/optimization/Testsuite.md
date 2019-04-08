# Test suite

## Basic tests

- delete
- replace
- stringReplace
- UserEntry
- use-when
- use-for-each
- Multiple action elements
- global quickfix
- match attribute

## Basic node creation
- template body
- select attribute
- target / node-type
    - element
    - attibute
    - comment
    - pi
- position
    - first-child
    - last-child
    - before
    - after
    
## Userentry
- used in @match
- types
- default
- default sequence

## Foreign elements
- sqf:description/\*/sch:value-of
- {actionEl}/sch:value-of
- sch:rule/sch:let
- sqf:fix/sch:let
- sqf:fix/{actionEl}/sch:let
- xsl:varibale
- xsl:for-each
- global
    - function usage
    - variable usage
    - template
    - key
- xsl:include/xsl:import
- pattern sch:let usage
- phase sch:let usage

## Call-fix
- basic example
- with parameter
- mix with action elements
- with abstract description
- with abstract parameter

## Mutlipe documents
- doc() in match
- XInclude
- sch:include

## Edge cases
- Multiple action elements order sensitive
- Global / local quick fixes name conflicts
- xslt3 features:
    - text value templates

## Namespaces
- namespace type
- Namespace edge cases



