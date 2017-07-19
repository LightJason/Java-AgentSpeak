<xsl:stylesheet xmlns:xsl = "http://www.w3.org/1999/XSL/Transform" xmlns:xs = "http://www.w3.org/2001/XMLSchema"
                xmlns:xslt = "http://www.w3.org/1999/XSL/Transform"
                version = "2.0">

    <xsl:output method = "text" version = "1.0" encoding = "UTF-8" indent = "no" standalone = "yes" />
    <xsl:strip-space elements = "*" />

    <!-- match root node -->
    <xsl:template match = "/*">
        <xsl:text>{</xsl:text>

        <!-- create json object of a class node (only public classes and if the class is an inheritance of IBuiltinAction) -->
        <xsl:for-each
                select = "compounddef[@kind='class' and @prot='public' and (not(@abstract) or @abstrac!='yes') and inheritancegraph//node/label='org.lightjason.agentspeak.action.builtin.IBuiltinAction']">

            <!-- replace base package and class prefix, replace :: to / and create lower-case -->
            <xsl:variable name = "name" as = "xs:string">
                <xsl:value-of
                        select = "replace(replace(replace( lower-case(normalize-space(compoundname)), 'org::lightjason::agentspeak::action::builtin::', ''), '::c', '::'), '::', '/')" />
            </xsl:variable>

            <xsl:text>"</xsl:text><xsl:value-of select = "$name" /><xsl:text>" : </xsl:text>
            <xsl:text>{</xsl:text>

            <!-- sorting field length of group and string with values -->
            <xsl:text>"sort" : "</xsl:text>
            <xsl:for-each select = "tokenize($name, '/')">
                <xsl:choose>
                    <xsl:when test = "position() = last()">
                        <xsl:value-of select="count(tokenize($name, '/'))-1"/>
                    </xsl:when>
                </xsl:choose>
                <xsl:value-of select = "substring(concat(., '                         '), 1, 25)" />
            </xsl:for-each>
            <xslt:text>",</xslt:text>

            <!-- content data -->
            <xsl:text>"name" : "</xsl:text><xsl:value-of select = "$name" /><xsl:text>",</xsl:text>
            <xsl:text>"id" : "</xsl:text><xsl:value-of select = "@id" /><xsl:text>",</xsl:text>
            <xsl:apply-templates select = "briefdescription" />
            <xsl:text>,</xsl:text>
            <xsl:apply-templates select = "detaileddescription" />
            <xsl:text>,</xsl:text>

            <xsl:text>"group" : "</xsl:text>
            <xsl:for-each select = "tokenize($name, '/')">
                <xsl:choose>
                    <xsl:when test = "position() &lt; last()">
                        <xsl:value-of select = "." />
                    </xsl:when>
                </xsl:choose>
                <xsl:choose>
                    <xsl:when test = "position() &lt; last()-1">
                        <xsl:text>/</xsl:text>
                    </xsl:when>
                </xsl:choose>
            </xsl:for-each>
            <xslt:text>",</xslt:text>

            <xsl:text>"see": [</xsl:text>
            <xsl:for-each select = "detaileddescription/para/simplesect[@kind='see']/para/ulink">
                <xsl:text>"</xsl:text><xsl:value-of select = "@url" /><xsl:text>"</xsl:text>
                <xsl:choose>
                    <xsl:when test = "position() != last()">,</xsl:when>
                </xsl:choose>
            </xsl:for-each>
            <xsl:text>],</xsl:text>

            <xsl:text>"warning": [</xsl:text>
            <xsl:for-each select = "detaileddescription/para/simplesect[@kind='warning']">
                <xsl:text>"</xsl:text><xsl:value-of select = "para" /><xsl:text>"</xsl:text>
                <xsl:choose>
                    <xsl:when test = "position() != last()">,
                        <br />
                    </xsl:when>
                </xsl:choose>
            </xsl:for-each>
            <xsl:text>],</xsl:text>

            <xsl:text>"note": [</xsl:text>
            <xsl:for-each select = "detaileddescription/para/simplesect[@kind='note']">
                <xsl:text>"</xsl:text><xsl:value-of select = "para" /><xsl:text>"</xsl:text>
                <xsl:choose>
                    <xsl:when test = "position() != last()">,</xsl:when>
                </xsl:choose>
            </xsl:for-each>
            <xsl:text>]</xsl:text>

            <xsl:text>}</xsl:text>

            <xsl:choose>
                <xsl:when test = "position() != last()">,</xsl:when>
            </xsl:choose>

        </xsl:for-each>

        <xsl:text>}</xsl:text>
    </xsl:template>


    <!-- brief description -->
    <xsl:template match = "briefdescription">
        <xsl:text>"briefdescription": "</xsl:text>
        <xsl:apply-templates />
        <xsl:text>"</xsl:text>
    </xsl:template>


    <!-- detailed description -->
    <xsl:template match = "detaileddescription">
        <xsl:text>"detaildescription": "</xsl:text>
        <xsl:apply-templates select = "para" />
        <xsl:text>"</xsl:text>
    </xsl:template>


    <!-- latex formula -->
    <xsl:template match = "formula">
        <xsl:text>&lt;!-- htmlmin:ignore --&gt;</xsl:text>
        <xsl:apply-templates />
        <xsl:text>&lt;!-- htmlmin:ignore --&gt;</xsl:text>
    </xsl:template>


    <!-- program listing, ignore simplesect, because it is read on the main template -->
    <xsl:template match = "programlisting">
        <xsl:text> &lt;!-- htmlmin:ignore --&gt;&lt;pre&gt;&lt;code&gt;</xsl:text>
        <xsl:apply-templates />
        <xsl:text>&lt;/code&gt;&lt;/pre&gt;&lt;!-- htmlmin:ignore --&gt;</xsl:text>
    </xsl:template>

    <xsl:template match = "codeline">
        <xsl:apply-templates />
        <xsl:text>\n</xsl:text>
    </xsl:template>

    <xsl:template match = "simplesect"/>

    <!-- list item to translate into html -->
    <xsl:template match = "itemizedlist">
        <xsl:text>&lt;ul&gt;</xsl:text>
        <xsl:apply-templates />
        <xsl:text>&lt;/ul&gt;</xsl:text>
    </xsl:template>

    <xsl:template match = "listitem">
        <xsl:text>&lt;li&gt;</xsl:text>
        <xsl:apply-templates />
        <xsl:text>&lt;/li&gt;</xsl:text>
    </xsl:template>


    <!-- default text node handling -->
    <xsl:template match = "text()|@*">
        <xsl:variable name = "escapedquote">\\"</xsl:variable>
        <xsl:variable name = "singlequote">"</xsl:variable>
        <xsl:value-of select = "replace(replace(., '\\', '\\\\'), $singlequote, $escapedquote)" />
    </xsl:template>

</xsl:stylesheet>