<?xml version="1.0" encoding="UTF-8"?>
<!--
  transforms documentation on cmake variables into extension point description code for CmakeEd
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output encoding="UTF-8" method="xml" media-type="text/xml" omit-xml-declaration="yes" />

  <xsl:template match="/document/section">
    <xsl:variable name="name" select="title/text()" />
    <!-- assume the first paragraph element contains the description -->
    <xsl:variable name="descr" select="paragraph[1]/descendant-or-self::*[text()]" />
    <!-- first word in description marks deprecated variables -->
    <xsl:variable name="deprecated" select="starts-with($descr, 'Deprecated.')" />

    <!-- construct an element for cmakeed:
      <variable name=".." desc=".." deprecated=".." />
    -->
    <xsl:element name="variable">
      <xsl:attribute name="name"><xsl:value-of select="$name" /></xsl:attribute>
      <xsl:attribute name="desc"><xsl:value-of select="normalize-space($descr)" /></xsl:attribute>
      <xsl:if test="$deprecated">
        <xsl:attribute name="deprecated"><xsl:value-of select="'true'" /></xsl:attribute>
      </xsl:if>
    </xsl:element>
    <xsl:value-of select="'&#10;'" />
  </xsl:template>

  <!-- suppress printing of empty lines -->
  <xsl:template match="@*|node()">
    <xsl:apply-templates select="@*|node()" />
  </xsl:template>
</xsl:stylesheet>