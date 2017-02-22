<!--

    ######################################################################################
    # LGPL License                                                                       #
    #                                                                                    #
    # This file is part of the LightJason AgentSpeak(L++)                                #
    # Copyright (c) 2015-16, LightJason (info@lightjason.org)                            #
    # This program is free software: you can redistribute it and/or modify               #
    # it under the terms of the GNU Lesser General Public License as                     #
    # published by the Free Software Foundation, either version 3 of the                 #
    # License, or (at your option) any later version.                                    #
    #                                                                                    #
    # This program is distributed in the hope that it will be useful,                    #
    # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
    # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
    # GNU Lesser General Public License for more details.                                #
    #                                                                                    #
    # You should have received a copy of the GNU Lesser General Public License           #
    # along with this program. If not, see http://www.gnu.org/licenses/                  #
    ######################################################################################

-->
<xsl:stylesheet xmlns:xsl = "http://www.w3.org/1999/XSL/Transform" xmlns:xs = "http://www.w3.org/2001/XMLSchema"
                xmlns:xslt = "http://www.w3.org/1999/XSL/Transform"
                version = "2.0">

    <xsl:output method = "text" version = "1.0" encoding = "UTF-8" indent = "no" standalone = "yes" />
    <xsl:strip-space elements = "*" />

    <!-- match root node -->
    <xsl:template match = "/*">
        <xsl:text>{</xsl:text>

        <!-- create json object of a class node (only public classes and if the class is an inheritance of IBuildinAction) -->
        <xsl:for-each
                select = "compounddef[@kind='class' and (not(@abstract) or @abstrac!='yes') and @prot='public' and inheritancegraph/node/label='org.lightjason.agentspeak.action.buildin.IBuildinAction']">

            <!-- replace base package and class prefix, replace :: to / and create lower-case -->
            <xsl:variable name = "name" as = "xs:string">
                <xsl:value-of
                        select = "replace(replace(replace( lower-case(normalize-space(compoundname)), 'org::lightjason::agentspeak::action::buildin::', ''), '::c', '::'), '::', '/')" />
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
        <xsl:apply-templates select = "para[count(descendant::*) &gt; 0 and count(text()) &gt; 0]" />
        <xsl:text>"</xsl:text>
    </xsl:template>


    <!-- latex formula -->
    <xsl:template match = "formula">
        <xsl:text> </xsl:text>
        <xsl:apply-templates />
        <xsl:text> </xsl:text>
    </xsl:template>


    <!-- program listing -->
    <xsl:template match = "programlisting">
        <xsl:text> &lt;pre&gt;&lt;code&gt;</xsl:text>
        <xsl:apply-templates />
        <xsl:text>&lt;/code&gt;&lt;/pre&gt; </xsl:text>
    </xsl:template>

    <xsl:template match = "codeline">
        <xsl:apply-templates />
        <xsl:text>\n</xsl:text>
    </xsl:template>


    <!-- default text node handling -->
    <xsl:template match = "text()|@*">
        <xsl:variable name = "escapedquote">\\"</xsl:variable>
        <xsl:variable name = "singlequote">"</xsl:variable>
        <xsl:value-of select = "replace(replace(., '\\', '\\\\'), $singlequote, $escapedquote)" />
    </xsl:template>

</xsl:stylesheet>