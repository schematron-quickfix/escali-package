<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:db="http://docbook.org/ns/docbook" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" queryBinding="xslt2">
	<es:default-namespace uri="http://docbook.org/ns/docbook"/>
	<pattern>
		<rule context="*[listitem]">
			<let name="level" value="count(ancestor::listitem)+1"/>
			<report test="$level >= 3" sqf:fix="delete plain">Too many levels in this list. We are in level <value-of select="$level"/>.</report>
			<sqf:fix id="delete">
				<sqf:description>
				    <sqf:title>Delete this list.</sqf:title>
				</sqf:description>
				<sqf:delete/>
			</sqf:fix>
			<sqf:fix id="plain" use-when="not(.//*[listitem] or $level > 3)">
				<sqf:description>
				    <sqf:title>Resolve the list into plain text.</sqf:title>
				</sqf:description>
			    <sqf:replace match=".|.//*[listitem]">
					<sqf:keep select="listitem/node()"/>
				</sqf:replace>
			</sqf:fix>
		</rule>
	</pattern>
</schema>
