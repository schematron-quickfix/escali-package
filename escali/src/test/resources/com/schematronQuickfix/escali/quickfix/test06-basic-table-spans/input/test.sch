<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:htm="http://www.html.de" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:es="http://www.escali.schematron-quickfix.com/" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:html="http://www.data2type.de/html" xmlns:xs="http://www.w3.org/2001/XMLSchema" queryBinding="xslt2">
	<es:default-namespace uri="http://www.w3.org/1999/xhtml"/>
    <ns uri="http://www.data2type.de/html" prefix="html"/>
    <ns uri="http://www.w3.org/2001/XMLSchema" prefix="xs"/>
    <xsl:function name="html:getColAndRows" xmlns="http://www.w3.org/1999/xhtml">
		<xsl:param name="preCells"/>
		<xsl:param name="cells"/>
		<xsl:variable name="cellsWithRows">
			<xsl:choose>
				<xsl:when test="$cells[not(@html:rowNumber)]|$cells[not(@colspan|@rowspan)]">
					<xsl:for-each select="$cells">
						<xsl:copy>
							<xsl:attribute name="html:rowNumber" select="count(../preceding-sibling::tr 
                                | ../parent::*[not(self::table)]/preceding-sibling::*/tr)+1"/>
							<xsl:attribute name="colspan" select="'1'"/>
							<xsl:attribute name="rowspan" select="'1'"/>
							<xsl:attribute name="html:tableID" select="generate-id(./ancestor::table[1])"/>
							<xsl:attribute name="html:cellID" select="generate-id()"/>
							<xsl:copy-of select="@*"/>
							<xsl:apply-templates/>
						</xsl:copy>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise>
					<xsl:copy-of select="$cells"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<xsl:variable name="cells" select="$cellsWithRows//td"/>
		<xsl:choose>
			<xsl:when test="count($cells)=0">
				<xsl:copy-of select="$preCells"/>
				<xsl:copy-of select="$cells"/>
			</xsl:when>
			<xsl:when test="count($cells)=1 and count($preCells)=0">
				<td>
					<xsl:attribute name="html:colNumber">1</xsl:attribute>
					<xsl:copy-of select="$cells/@*"/>
					<xsl:copy-of select="$cells/node()"/>
				</td>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="half" select="count($cells) div 2"/>
				<xsl:variable name="firstHalf" select="html:getColAndRows($preCells,$cells[position()&lt;$half])"/>
				<xsl:variable name="secondHalf" select="html:getColAndRows(($firstHalf),
                    $cells[position()&gt;=$half][not(position()=last())])"/>
				<xsl:variable name="cell" select="$cells[last()]"/>
				<xsl:variable name="preCells" select="($secondHalf)"/>
				<xsl:variable name="preCell" select="$preCells[last()]"/>
				<xsl:variable name="colOhneRowspan" select="if (number($preCell/@html:rowNumber) &lt; number($cell/@html:rowNumber)) 
                    then (1) 
                    else ($preCell/@html:colNumber + $preCell/@colspan)"/>
				<xsl:variable name="colMitRowspan" select="html:getCol($colOhneRowspan,$cell/@html:rowNumber,$preCells[@rowspan &gt; 1])"/>
				<xsl:copy-of select="$preCells"/>
				<td>
					<xsl:attribute name="html:colNumber" select="$colMitRowspan"/>
					<xsl:copy-of select="$cell/@*"/>
					<xsl:copy-of select="$cell/node()"/>
				</td>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:function>
	<xsl:function name="html:getColAndRows">
		<xsl:param name="cells"/>
		<xsl:copy-of select="html:getColAndRows((),$cells)"/>
	</xsl:function>
	<xsl:function name="html:getCol">
		<xsl:param name="curCol"/>
		<xsl:param name="curRow"/>
		<xsl:param name="preCells"/>
		<xsl:choose>
			<xsl:when test="$preCells[@html:colNumber &lt;= $curCol and $curCol &lt; @html:colNumber + @colspan]
                [@html:rowNumber + @rowspan &gt; $curRow]">
				<xsl:value-of select="html:getCol($curCol + 1,$curRow,$preCells)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$curCol"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:function>
	<let name="cells" value="for $table in //table return html:getColAndRows($table//td)"/>
	<pattern>
		<rule context="td[@colspan &gt; 1]">
			<let name="tableID" value="generate-id(./ancestor::table[1])"/>
			<let name="cellID" value="generate-id()"/>
			<let name="cells" value="$cells[@html:tableID=$tableID]"/>
			<let name="rowNumber" value="count(../preceding-sibling::tr | 
                ../parent::*[not(self::table)]/preceding-sibling::*/tr)+1"/>
			<let name="cell" value="$cells[@html:cellID=$cellID]"/>
			<let name="row" value="$cells[@html:rowNumber + @rowspan &gt; $rowNumber]
                [@html:rowNumber &lt;= $rowNumber]"/>
			<let name="cols" value="xs:integer($cell/@html:colNumber) to xs:integer($cell/@html:colNumber + $cell/@colspan - 1)"/>
			<let name="cross" value="$row[@html:colNumber = $cols]
                [not(@html:cellID=$cellID)]"/>
			<let name="crossTd" value="//td[generate-id()=$cross/@html:cellID]"/>
			<assert test="count($cross) = 0" sqf:fix="deleteColspan deleteRowspan">This cell overlaps the row-spanning cell(s) of the column(s) <value-of select="string-join($cross/@html:colNumber,', ')"/>.</assert>
			<sqf:fix id="deleteColspan">
				<sqf:description>
				    <sqf:title>Delete the "colspan" attribute from this cell.</sqf:title>
				</sqf:description>
				<sqf:delete match="@colspan"/>
			</sqf:fix>
			<sqf:fix id="deleteRowspan">
				<sqf:description>
				    <sqf:title>Delete the "rowspan" attribute from the overlapping cell.</sqf:title>
				</sqf:description>
				<sqf:delete match="$crossTd/@rowspan"/>
			</sqf:fix>
		</rule>
		<rule context="tr">
			<let name="tableID" value="generate-id(./ancestor::table[1])"/>
			<let name="cells" value="$cells[@html:tableID=$tableID]"/>
			<let name="maxCol" value="xs:integer(max(for $cell in $cells return ($cell/@html:colNumber + $cell/@colspan - 1)))"/>
			<let name="rowNumber" value="count(preceding-sibling::tr | parent::*[not(self::table)]/preceding-sibling::*/tr)+1"/>
			<let name="row" value="for $i 
                in (1 to $maxCol)
                return ($cells[@html:colNumber &lt;= $i and $i &lt; @html:colNumber + @colspan]
                [@html:rowNumber + @rowspan &gt; $rowNumber][@html:rowNumber &lt;= $rowNumber])"/>
			<let name="forcedColCount" value="if (ancestor::table[1]//col) 
                then (count(ancestor::table[1]//col)) 
                else ($maxCol)"/>
			<let name="rowMaxCol" value="max(for $cell in $row return number($cell/@html:colNumber + $cell/@colspan - 1))"/>
			<assert test="count($row) &gt;= $forcedColCount" sqf:fix="addLost">Cells are missing. (Counting of missing cells: <value-of select="$forcedColCount - count($row)"/> of <value-of select="$forcedColCount"/>)</assert>
		    <report test="$rowMaxCol &gt; $forcedColCount" sqf:fix="deleteUeberschuss">Too many cells in this row. (Counting of excessive cells:  <value-of select="count($row) - $forcedColCount"/>)</report>
		    <sqf:fix id="deleteUeberschuss">
				<sqf:description>
				    <sqf:title>Delete the excessive cells.</sqf:title>
				</sqf:description>
				<let name="delete" value="$row[@html:colNumber &gt; $forcedColCount]"/>
				<sqf:delete match="//td[generate-id()=$delete/@html:cellID]"/>
			</sqf:fix>
			<sqf:fix id="addLost">
				<sqf:description>
				    <sqf:title>Add enough empty cells at the end of the row.</sqf:title>
				</sqf:description>
			    <sqf:add match="td[last()]" position="after">
					<xsl:for-each select="1 to xs:integer($forcedColCount - count($row))">
						<td xmlns="http://www.w3.org/1999/xhtml"/>
					</xsl:for-each>
				</sqf:add>
			</sqf:fix>
		</rule>
	</pattern>
</schema>
