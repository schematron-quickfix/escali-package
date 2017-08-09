<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:sqf="http://www.schematron-quickfix.com/validator/process" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
    <ns uri="http://www.schematron.info/arche" prefix="arc"/>
    <pattern>
        <rule context="arc:tier[@geschlecht = 'männlich']" role="warn">
            <let name="maxRep" value="//arc:maxReproduktionsalter/arc:tier_art[arc:name = current()/arc:art]/arc:männlich"/>
            <report test="
                    number(arc:alter) &gt;
                    number($maxRep)" sqf:fix="delete young youngUE" sqf:default-fix="delete">Das Männchen ist zu alt, Noah! Es wird sich nicht mehr fortpflanzen können. Sorge für die natürliche Auslese.</report>

            <sqf:fix id="young">
                <sqf:description>
                    <sqf:title>Setze das Alter auf das maximale Reporduktionsalter</sqf:title>
                    <sqf:p>Sei barmherzig, Noah!</sqf:p>
                    <sqf:p>Es gab schon viele, die es beim Alter nicht so genau genommen haben.</sqf:p>
                    <sqf:p>Man ist immer nur so alt, wie man sich fühlt!</sqf:p>
                </sqf:description>
                <sqf:replace match="arc:alter" node-type="element" target="alter" select="$maxRep/string(.)" xmlns="http://www.schematron.info/arche"/>
            </sqf:fix>
            <sqf:fix id="youngUE">
                <sqf:description>
                    <sqf:title>Lege das Alter des Tieres selbst fest.</sqf:title>
                    <sqf:p>Du hast dich bei der Altersangabe geirrt?</sqf:p>
                    <sqf:p>Hiermit kannst du das Alter des Tieres selbst bestimmen.</sqf:p>
                </sqf:description>
                <sqf:user-entry name="newAlter" default="$maxRep">
                    <sqf:description>
                        <sqf:title>Gib ein Alter an, das nicht älter als <value-of select="$maxRep/string(.)"/> ist.</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:replace match="arc:alter" node-type="element" target="alter" select="string($newAlter)" xmlns="http://www.schematron.info/arche"/>
            </sqf:fix>
        </rule>
        <rule context="arc:tier[@geschlecht = 'weiblich']" role="info">
            <let name="maxRep" value="//arc:maxReproduktionsalter/arc:tier_art[arc:name = current()/arc:art]/arc:weiblich"/>
            <report test="
                    number(arc:alter) &gt;
                    number($maxRep)" sqf:fix="delete young">Das Weibchen ist zu alt, Noah! Es wird sich nicht mehr fortpflanzen können. Sorge für die natürliche Auslese.</report>
            <sqf:fix id="young">
                <sqf:description>
                    <sqf:title>Setze das Alter auf das maximale Reporduktionsalter</sqf:title>
                </sqf:description>
                <sqf:replace match="arc:alter" node-type="element" target="alter" select="$maxRep/string(.)" xmlns="http://www.schematron.info/arche"/>
            </sqf:fix>
        </rule>
    </pattern>
    <pattern>
        <rule context="arc:nutzlast" role="fatal">
            <let name="nutzlast" value="sum(//arc:gewicht)"/>
            <report test=". &lt; $nutzlast" sqf:fix="extend" sqf:default-fix="extend">Noah, du hast zu viele Tiere an Bord. Die Ladung überschreitet die Nutzlast deiner Arche.</report>
            <sqf:fix id="extend">
                <sqf:description>
                    <sqf:title>Baue deine Arche aus</sqf:title>
                    <sqf:p>Noah, die Behörden sind manchmal etwas kleinkarriert. Trickse doch einfach ein bisschen bei der Angabe der Nutzlast.</sqf:p>
                </sqf:description>
                <sqf:replace node-type="element" target="nutzlast" xmlns="http://www.schematron.info/arche" select="$nutzlast"/>
            </sqf:fix>
        </rule>
    </pattern>
    <pattern>
        <rule context="arc:tier[@fleischfresser = 'ja']" role="error">
            <report test="parent::*/arc:tier[@fleischfresser = 'nein']" sqf:fix="vegi">Es gibt Fleischfresser und Pflanzenfresser in einer Unterkunft. Die Tiere sind keine Nahrungsquelle!</report>
            <report test="parent::*/arc:tier/arc:gewicht &lt; (arc:gewicht div 2)" sqf:fix="magerkurFleisch">Noah, dieser Fleischfresser ist zu stark (schwer) für seinen Zimmer­genossen. Er könnte ihn als Nahrungsquelle benutzen.</report>
            <sqf:fix id="vegi">
                <sqf:description>
                    <sqf:title>Bekehre das Tier zum Vegitarismus</sqf:title>
                    <sqf:p>Vegitarismus for ever, Noah! Rückfälle sind quasi ausgeschlossen.</sqf:p>
                </sqf:description>
                <sqf:replace match="@fleischfresser" target="fleischfresser" node-type="attribute" select="'nein'"/>
            </sqf:fix>
            <sqf:fix id="magerkurFleisch">
                <sqf:param name="factor" default="10"/>
                <sqf:description>
                    <sqf:title>Mach mit dem Tier eine Magerkur</sqf:title>
                </sqf:description>
                <sqf:call-fix ref="magerkur">
                    <sqf:with-param name="factor" select="2"/>
                </sqf:call-fix>
            </sqf:fix>
        </rule>
        <rule context="arc:tier">
            <report test="parent::*/arc:tier/arc:gewicht &lt; (arc:gewicht div 10)" sqf:fix="magerkur">Noah, das Tier ist zu schwer für seine Zimmergenossen! Es könnte einen zertrampeln.</report>

        </rule>
    </pattern>
    <pattern>
        <rule context="arc:tier">
            <let name="current" value="."/>
            <report test="count(//arc:tier[arc:art = current()/arc:art]) &gt; 2" sqf:fix="delete">In der Arche gibt es mehr als zwei Tiere dieser Art.</report>
            <report test="count(parent::*/arc:tier[arc:art = current()/arc:art]) &lt; 2" sqf:fix="createTier move">In dieser Unterkunft gibt es weniger als zwei Tiere dieser Art.</report>
            <assert test="count(parent::*/arc:tier[arc:art = current()/arc:art][@geschlecht = 'männlich']) = 1" sqf:fix="umwandlung">Ein Paar muss immer aus einem Männchen und einem Weibchen bestehen.</assert>
            <let name="otherZimmer" value="(//arc:zimmer except parent::arc:zimmer)"/>
            <let name="artGenossen" value="$otherZimmer[arc:tier/arc:art = current()/arc:art]"/>
            <sqf:fix id="createTier" use-when="not($artGenossen)">
                <sqf:description>
                    <sqf:title>Erzeuge ein Tier</sqf:title>
                </sqf:description>
                <sqf:user-entry name="gewicht" default="$current/arc:gewicht">
                    <sqf:description>
                        <sqf:title>Gib das Gewicht des neuen Tieres an.</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:user-entry name="alter" default="$current/arc:alter">
                    <sqf:description>
                        <sqf:title>Gib das Alter des neuen Tieres an.</sqf:title>
                    </sqf:description>
                </sqf:user-entry>
                <sqf:add match="parent::arc:zimmer" xmlns="http://www.schematron.info/arche" position="last-child">
                    <tier geschlecht="{ if($current/@geschlecht='weiblich') then 'männlich' else 'weiblich'}" fleischfresser="{$current/@fleischfresser}">
                        <art>
                            <sch:value-of select="$current/arc:art"/>
                        </art>
                        <gewicht>
                            <sch:value-of select="$gewicht"/>
                        </gewicht>
                        <alter>
                            <sch:value-of select="$alter"/>
                        </alter>
                    </tier>
                </sqf:add>
            </sqf:fix>
            <sqf:fix id="move" use-when="$artGenossen">
                <sqf:description>
                    <sqf:title>Ziehe das Tier zu seiner/m Artgenossen/in</sqf:title>
                </sqf:description>
                <sqf:delete/>
                <sqf:add match="$artGenossen[1]" position="last-child">
                    <xsl:copy-of select="$current"/>
                </sqf:add>
            </sqf:fix>
            <sqf:fix id="umwandlung">
                <sqf:description>
                    <sqf:title>Führe eine Geschlechtsumwandlung durch</sqf:title>
                </sqf:description>
                <sqf:replace match="@geschlecht" target="geschlecht" node-type="attribute" select="
                        if ($current/@geschlecht = 'weiblich') then
                            'männlich'
                        else
                            'weiblich'"/>

            </sqf:fix>
        </rule>
    </pattern>
    <pattern>
        <rule context="arc:zimmer">
            <let name="tiere" value="arc:tier"/>
            <report test="count(arc:tier) &gt; 6" sqf:fix="move">Noah, bringst du zu viele Tiere in einem Zimmer unter, könnte sich das schlecht auf die Zimmergemeinschaft auswirken! Du solltest nicht mehr als 6 Tiere in einem Zimmer unterbringen.</report>
            <sqf:fix id="move" xmlns="http://www.schematron.info/arche">
                <sqf:description>
                    <sqf:title>Richte ein neues Zimmer auf die du diese Tiere verteilst</sqf:title>
                </sqf:description>
                <sqf:delete match="$tiere[position() gt 6]"/>
                <sqf:add match="parent::*" position="last-child">
                    <zimmer><xsl:copy-of select="$tiere[position() gt 6]"/></zimmer>
                </sqf:add>
            </sqf:fix>
        </rule>
    </pattern>
    <sqf:fixes>
        <sqf:fix id="magerkur">
            <sqf:param name="factor" default="10"/>
            <sqf:description>
                <sqf:title>Mach mit dem Tier eine Magerkur</sqf:title>
            </sqf:description>
            <sqf:replace match="arc:gewicht" xmlns="http://www.schematron.info/arche" node-type="element" target="gewicht" select="min(../../*:tier/*:gewicht/number()) * $factor"/>
        </sqf:fix>
        <sqf:fix id="delete">
            <sqf:description>
                <sqf:title>Entferne das Tier</sqf:title>
                <sqf:p>Das Tier wird gnadenlos von der Arche entfernt.</sqf:p>
                <sqf:p>Kein Erbarmen, Noah!</sqf:p>
            </sqf:description>
            <sqf:delete/>
        </sqf:fix>

    </sqf:fixes>
</schema>
