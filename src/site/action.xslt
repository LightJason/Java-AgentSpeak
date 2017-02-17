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
<xsl:stylesheet xmlns:xsl = "http://www.w3.org/1999/XSL/Transform" version = "2.0"
                xmlns:xs = "http://www.w3.org/2001/XMLSchema" xmlns:fn = "http://www.w3.org/2005/xpath-functions"
                xmlns:local = "localDocumentNamespace" xmlns:xslt = "http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="text" version="1.0" encoding="UTF-8" indent="no" standalone="yes"/>
    <xsl:strip-space elements="*"/>

    <!-- match root node -->
    <xsl:template match="/*">
        <xsl:text>{</xsl:text>

        <!-- create json object of a class node (only public classes and if the class is an inheritance of IBuildinAction) -->
        <xsl:for-each select="compounddef[@kind='class' and (not(@abstract) or @abstrac!='yes') and @prot='public' and inheritancegraph/node/label='org.lightjason.agentspeak.action.buildin.IBuildinAction']">

            <xsl:if test="position() > 1">
                <xsl:text>, </xsl:text>
            </xsl:if>

            <!-- replace base package and class prefix, replace :: to / and create lower-case -->
            <xsl:variable name="name" as="xs:string"><xsl:value-of select="replace(replace(replace( lower-case(normalize-space(compoundname)), 'org::lightjason::agentspeak::action::buildin::', ''), '::c', '::'), '::', '/')" /></xsl:variable>

            <xsl:text>"</xsl:text><xsl:value-of select="$name" /><xsl:text>" : </xsl:text>
            <xsl:text>{</xsl:text>
            <xsl:text>"name" : "</xsl:text><xsl:value-of select="$name" /><xsl:text>",</xsl:text>
            <xsl:text>"id" : "</xsl:text><xsl:value-of select="@id" /><xsl:text>",</xsl:text>
            <xsl:text>"group" : "</xsl:text><xsl:value-of select="replace($name, concat('/', tokenize($name, '/')[last()]), '')" /><xslt:text>",</xslt:text>
            <xsl:text>"briefdescription": "</xsl:text><xsl:value-of select="replace(briefdescription, '\\', '\\\\')" /><xsl:text>",</xsl:text>
            <xsl:text>"detaildescription": "</xsl:text><xsl:value-of select="replace(detaildescription, '\\', '\\\\')"/><xsl:text>"</xsl:text>
            <xsl:text>}</xsl:text>

        </xsl:for-each>

        <xsl:text>}</xsl:text>
    </xsl:template>

    <!-- match all other node -->
    <xsl:template match="node()"/>

    <!-- default text node handling -->
    <xsl:template match="text()|@*">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>

</xsl:stylesheet>