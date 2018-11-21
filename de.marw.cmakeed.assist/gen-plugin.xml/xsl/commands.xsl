<?xml version="1.0" encoding="UTF-8"?>
<!--
  transforms documentation on cmake commands into extension point description code for CmakeEd
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output encoding="UTF-8" method="xml" media-type="text/xml" omit-xml-declaration="yes" />

  <xsl:template match="/">
    <xsl:if test="not(function-available('ends-with'))">
      <xsl:message terminate="yes" ><xsl:value-of select="'XPATH 2 is required to process this file!'" /></xsl:message>
    </xsl:if>

    <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="/document/section">
    <xsl:variable name="name" select="title/text()" />
    <!-- assume the first paragraph element contains the description -->
    <xsl:variable name="descr" select="normalize-space(paragraph[1][text()])" />
    <!-- first word in description marks deprecated command -->
    <xsl:variable name="deprecated" select="starts-with($descr, 'Deprecated.') or starts-with($descr, 'Disallowed.')" />

    <!-- construct an element for cmakeed:
      < name=".." desc=".." deprecated=".." />
    -->
    <xsl:element name="command">
      <xsl:attribute name="name" select="$name" />
      <xsl:attribute name="desc" select="$descr" />

      <xsl:if test="$deprecated">
        <xsl:attribute name="deprecated" select="'true'" />
      </xsl:if>
      <xsl:apply-templates
        select="literal_block|section[@ids!='synopsis']/literal_block|section/section/literal_block">
        <xsl:with-param name="command" select="$name" tunnel="yes" />
      </xsl:apply-templates>
    </xsl:element>
    <xsl:value-of select="'&#10;'" />
  </xsl:template>


  <!-- assume the first literal_block in any section contains a usage description -->
  <xsl:template
    match="literal_block">
    <xsl:param name="command" tunnel="yes" />
      <xsl:message terminate="no" >#<xsl:value-of select="text()" />#</xsl:message>
    <xsl:variable name="text" select="normalize-space(text())" />
    <!-- remove command name from usage description -->
    <xsl:variable name="text2" select="normalize-space(substring-after($text,$command))" />
    <!--
      <xsl:message terminate="no"
      select="concat('DESC #',$text,'# : ',string(string-length($command)))" />
      <xsl:message terminate="no"
      select="concat('DESC #',substring-after($text,$command),'#')" />
    -->
    <xsl:choose>
    <!-- skip useless usages -->
    <xsl:when test="$text2=''"/>
    <xsl:otherwise>
      <xsl:value-of select="'&#10;'" />
      <xsl:element name="usage">
        <xsl:attribute name="value" select="$text2" />
      </xsl:element>
      <xsl:value-of select="'&#10;'" />
    </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- suppress printing of empty lines -->
  <xsl:template match="@*|node()">
    <xsl:apply-templates select="@*|node()" />
  </xsl:template>
</xsl:stylesheet>