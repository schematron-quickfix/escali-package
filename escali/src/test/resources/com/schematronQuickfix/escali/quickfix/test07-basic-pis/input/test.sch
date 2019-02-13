<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" queryBinding="xslt2">
	<pattern id="PIs">
		<rule context="processing-instruction()">
		    <assert test="starts-with(name(),'sqf_')" sqf:fix="delete replace setName">The <name/> PI does not correspond to the name convention. The prefix "sqf_" is missing.</assert>
			<sqf:fix id="delete">
				<sqf:description>
				    <sqf:title>Delete the PI.</sqf:title>
				</sqf:description>
				<sqf:delete/>
			</sqf:fix>
			<sqf:fix id="setName">
				<sqf:description>
				    <sqf:title>Set a new name for this PI.</sqf:title>
				</sqf:description>
				<sqf:user-entry name="newName">
					<sqf:description>
					    <sqf:title>Enter the new name.</sqf:title>
					</sqf:description>
				</sqf:user-entry>
			    <sqf:replace target="{$newName}" node-type="pi">
			        <value-of select="."/>
			    </sqf:replace>
			</sqf:fix>
			<sqf:fix id="replace">
				<sqf:description>
				    <sqf:title>Add the missing prefix.</sqf:title>
				</sqf:description>
			    <sqf:replace target="sqf_{name()}" node-type="pi">
			        <value-of select="."/>
			    </sqf:replace>
			</sqf:fix>
		</rule>
	</pattern>
</schema>
